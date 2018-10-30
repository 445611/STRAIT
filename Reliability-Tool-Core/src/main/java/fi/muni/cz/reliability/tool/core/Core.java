package fi.muni.cz.reliability.tool.core;

import fi.muni.cz.reliability.tool.core.exception.InvalidInputException;
import fi.muni.cz.reliability.tool.core.factory.FilterFactory;
import fi.muni.cz.reliability.tool.core.factory.IssuesWriterFactory;
import fi.muni.cz.reliability.tool.core.factory.ModelFactory;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.Filter;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.FilterByLabel;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.FilterClosed;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.modeldata.CumulativeIssuesCounter;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.modeldata.IntervalIssuesCounter;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.modeldata.IssuesCounter;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.modeldata.TimeBetweenIssuesCounter;
import fi.muni.cz.reliability.tool.dataprocessing.output.CsvFileWriter;
import fi.muni.cz.reliability.tool.dataprocessing.output.HtmlOutputWriter;
import fi.muni.cz.reliability.tool.dataprocessing.output.OutputData;
import fi.muni.cz.reliability.tool.dataprocessing.output.OutputWriter;
import fi.muni.cz.reliability.tool.dataprocessing.persistence.GeneralIssuesSnapshot;
import fi.muni.cz.reliability.tool.dataprocessing.persistence.GeneralIssuesSnapshotDaoImpl;
import fi.muni.cz.reliability.tool.dataprovider.DataProvider;
import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import fi.muni.cz.reliability.tool.dataprovider.GitHubDataProvider;
import fi.muni.cz.reliability.tool.dataprovider.authenticationdata.GitHubAuthenticationDataProvider;
import fi.muni.cz.reliability.tool.dataprovider.utils.GitHubUrlParser;
import fi.muni.cz.reliability.tool.dataprovider.utils.ParsedUrlData;
import fi.muni.cz.reliability.tool.dataprovider.utils.UrlParser;
import fi.muni.cz.reliability.tool.models.DuaneModelImpl;
import fi.muni.cz.reliability.tool.models.GOModelImpl;
import fi.muni.cz.reliability.tool.models.GOSShapedModelImpl;
import fi.muni.cz.reliability.tool.models.HossainDahiyaModelImpl;
import fi.muni.cz.reliability.tool.models.Model;
import fi.muni.cz.reliability.tool.models.MusaOkumotoModelImpl;
import fi.muni.cz.reliability.tool.models.testing.ChiSquareGoodnessOfFitTest;
import fi.muni.cz.reliability.tool.models.testing.GoodnessOfFitTest;
import fi.muni.cz.reliability.tool.models.testing.LaplaceTrendTest;
import fi.muni.cz.reliability.tool.models.testing.TrendTest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class Core {

    private static ArgsParser parser;
    private static final DataProvider DATA_PROVIDER = new GitHubDataProvider(new GitHubAuthenticationDataProvider().getGitHubClientWithCreditials());
    private static ParsedUrlData parsedUrlData;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            parseArgs(args);
            run(args);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Run method.
     * @param args as input
     */
    public static void run(String[] args) {
        System.out.println("Working...");
        
        if (parser.getCmdl().hasOption(ArgsParser.OPT_LIST_ALL_SNAPSHOTS)) {
            doListAllSnapshots();
        } else if (parser.getCmdl().hasOption(ArgsParser.OPT_URL) && 
                parser.getCmdl().hasOption(ArgsParser.OPT_SAVE)) {
            checkUrl();
            doSaveToFile();
        } else if (parser.getCmdl().hasOption(ArgsParser.OPT_URL) && 
                parser.getCmdl().hasOption(ArgsParser.OPT_LIST_SNAPSHOTS)) {
            checkUrl();
            doListSnapshots();
        } else if (parser.getCmdl().hasOption(ArgsParser.OPT_URL) && 
                parser.getCmdl().hasOption(ArgsParser.OPT_EVALUATE)) {
            checkUrl();
            doEvaluate();
        }
        
        System.out.println("Done!");
        System.exit(0);
    }
    
    /**
     * Evaluate.
     * @param parser with data.
     */
    private static void doEvaluate() {
        List<GeneralIssue> listOfGeneralIssues = DATA_PROVIDER.
                getIssuesByUrl(parsedUrlData.getUrl().toString());
        int initialNumberOfIssues = listOfGeneralIssues.size();
        //model <- nls(yvalues ~ b1*(1 - exp(-b2*xvalues))/(1 + b3*exp(-b2*xvalues)),
        //start = list(b1 = 70,b2 = 1,b3 = 1), lower = list(b1 = 0,b2 = 0, b3 = 0), algorithm = "port")
        //model <- nls(yvalues ~ a*(1 - (1 + b*xvalues)*exp(-b*xvalues)),
        //start = list(a = 70,b = 1), lower = list(a = 0,b = 0), algorithm = "port")
        
        FilterFactory.runFilters(listOfGeneralIssues, parser.getCmdl());

        String periodicOfTesting = IssuesCounter.WEEKS;
        Date startOfTesting = listOfGeneralIssues.get(0).getCreatedAt();
        Date endOfTesting = new Date();
        
        GeneralIssuesSnapshot snapshot = new GeneralIssuesSnapshot.GeneralIssuesSnapshotBuilder()
                .setCreatedAt(new Date())
                .setTypeOfTimeToSplitTestInto(periodicOfTesting)
                .setFiltersRan(FilterFactory.getFiltersRanAsList(parser.getCmdl()))
                .setListOfGeneralIssues(listOfGeneralIssues)
                .setRepositoryName(parsedUrlData.getRepositoryName())
                .setUrl(parsedUrlData.getUrl().toString())
                .setUserName(parsedUrlData.getUserName())
                .setStartOfTesting(startOfTesting)
                .setEndOfTesting(endOfTesting)
                .setSnapshotName(parser.getCmdl().getOptionValue(ArgsParser.OPT_EVALUATE))
                .build();
        // SNAPSHOT
        
        // DATABASE
        //GeneralIssuesSnapshotDaoImpl dao = new GeneralIssuesSnapshotDaoImpl();
        //dao.save(snapshot);
        // DATABASE

        // COUNTERS
        IssuesCounter cumulativeCounter = new CumulativeIssuesCounter(periodicOfTesting, startOfTesting, endOfTesting);
        List<Pair<Integer, Integer>> countedWeeksWithTotal = cumulativeCounter.prepareIssuesDataForModel(listOfGeneralIssues);
        
        String timeBetweenIssuesUnit = IssuesCounter.HOURS;
        IssuesCounter timeBetween = new TimeBetweenIssuesCounter(timeBetweenIssuesUnit);
        List<Pair<Integer, Integer>> timeBetweenList = timeBetween.prepareIssuesDataForModel(listOfGeneralIssues);
        // COUNTERS
        
        //TEMP
        //TEMPORARYWriter.write(timeBetweenList);
        
        // MODEL
        GoodnessOfFitTest goodnessOfFitTest = new ChiSquareGoodnessOfFitTest();
        
        List<Model> models = new ArrayList<>();
        if (parser.getCmdl().hasOption(ArgsParser.OPT_MODELS)) {
            for (String modelArg: parser.getCmdl().getOptionValues(ArgsParser.OPT_MODELS)) {
                models.add(ModelFactory.getIssuesWriter(countedWeeksWithTotal, goodnessOfFitTest, modelArg));
            }
        } else {
            models.add(ModelFactory.getIssuesWriter(countedWeeksWithTotal, goodnessOfFitTest, ModelFactory.GOEL_OKUMOTO));
        }
        
        
        /*Model goModel = new GOModelImpl(countedWeeksWithTotal, goodnessOfFitTest);
        Model moModel = new MusaOkumotoModelImpl(countedWeeksWithTotal, goodnessOfFitTest);
        Model duaneModel = new DuaneModelImpl(countedWeeksWithTotal, goodnessOfFitTest);
        Model goSShapedModel = new GOSShapedModelImpl(countedWeeksWithTotal, goodnessOfFitTest);
        Model hdModel = new HossainDahiyaModelImpl(countedWeeksWithTotal, goodnessOfFitTest);*/
        for (Model model: models) {
            model.estimateModelData();
        }
        /*hdModel.estimateModelData();
        duaneModel.estimateModelData();
        goModel.estimateModelData();
        moModel.estimateModelData();
        goSShapedModel.estimateModelData();*/
        TrendTest trendTest = new LaplaceTrendTest(timeBetweenIssuesUnit);
        trendTest.executeTrendTest(listOfGeneralIssues);
        // MODEL
        
        int howMuchToPredict;
        if (parser.getCmdl().hasOption(ArgsParser.OPT_PREDICT)) {
            howMuchToPredict = Integer.valueOf(parser.getCmdl().getOptionValue(ArgsParser.OPT_PREDICT));
        } else {
            howMuchToPredict = 0;
        }
   
        List<String> processorsUsed = new ArrayList<>();
        
        List<OutputData> outputDataList = new ArrayList<>();
        OutputData outputData;
        for (Model model: models) {
            outputData = new OutputData.OutputDataBuilder()
                    .setCreatedAt(new Date())
                    .setRepositoryName(parsedUrlData.getRepositoryName())
                    .setUrl(parsedUrlData.getUrl().toString())
                    .setUserName(parsedUrlData.getUserName())
                    .setTotalNumberOfDefects(countedWeeksWithTotal.get(countedWeeksWithTotal.size() - 1).getSecond())
                    .setWeeksAndDefects(countedWeeksWithTotal)
                    .setTimeBetweenDefects(timeBetweenList)
                    .setTrend(trendTest.getTrendValue())
                    .setExistTrend(trendTest.getResult())
                    .setModelParameters(model.getModelParameters())
                    .setGoodnessOfFit(model.getGoodnessOfFitData())
                    .setEstimatedIssuesPrediction(model.getIssuesPrediction(howMuchToPredict))
                    .setModelName(model.toString())
                    .setModelFunction(model.getTextFormOfTheFunction())
                    .setStartOfTesting(startOfTesting)
                    .setEndOfTesting(endOfTesting)
                    .setInitialNumberOfIssues(initialNumberOfIssues)
                    .setFiltersUsed(FilterFactory.getFiltersRanWithInfoAsList(parser.getCmdl()))
                    .setProcessorsUsed(processorsUsed)
                    .setTestingPeriodsUnit(periodicOfTesting)
                    .setTimeBetweenDefectsUnit(timeBetweenIssuesUnit).build();
            outputDataList.add(outputData);
        }
        // OUTPUT
        OutputWriter writer = new HtmlOutputWriter(false);
        
        writer.writeOutputDataToFile(outputDataList, parsedUrlData.getRepositoryName());
        
        java.awt.Toolkit.getDefaultToolkit().beep();
    }

    private static void doSaveToFile() {
        List<GeneralIssue> listOfInitialIssues = DATA_PROVIDER.
                getIssuesByUrl(parser.getCmdl().getOptionValue(ArgsParser.OPT_URL));
        
        FilterFactory.runFilters(listOfInitialIssues, parser.getCmdl());

        IssuesWriterFactory.getIssuesWriter(parser.getCmdl())
                .writeToFile(listOfInitialIssues, parsedUrlData.getRepositoryName());
    }

    private static void doListAllSnapshots() {
        GeneralIssuesSnapshotDaoImpl dao = new GeneralIssuesSnapshotDaoImpl();
        List<GeneralIssuesSnapshot> listFromDB = dao.getAllSnapshots(); 
        for (GeneralIssuesSnapshot snap: listFromDB) {
            System.out.println(snap);
        }
    }
    
    private static void listAllSnapshotsForUrl(ArgsParser parser) {
        /*GeneralIssuesSnapshotDaoImpl dao = new GeneralIssuesSnapshotDaoImpl();
        List<GeneralIssuesSnapshot> listFromDB = dao.getAllSnapshotsForUserAndRepository(
                parser.getParsedUrlData().getUserName(), parser.getParsedUrlData().getRepositoryName()); 
        if (listFromDB.isEmpty()) {
            System.out.println("No snapshots for: " + parser.getParsedUrlData().getUrl().toString());
        } else {
            for (GeneralIssuesSnapshot snap: listFromDB) {
                System.out.println(snap);
            }
        }*/
    }
    
    private static void printHelp(){
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Help:", parser.getOptions());
    }
    
    private static void parseArgs(String[] args) {
        parser = new ArgsParser();
        try {
            parser.parse(args);
        } catch (InvalidInputException e) {
            throw new IllegalArgumentException(e.causes().toString());
        }
    }

    private static void checkUrl() {
        UrlParser urlParser = new GitHubUrlParser();
        parsedUrlData = urlParser.parseUrlAndCheck(parser.getCmdl().getOptionValue(ArgsParser.OPT_URL));
    }

    private static void doListSnapshots() {
        GeneralIssuesSnapshotDaoImpl dao = new GeneralIssuesSnapshotDaoImpl();
        List<GeneralIssuesSnapshot> listFromDB = dao.
                getAllSnapshotsForUserAndRepository(parsedUrlData.getUserName(), 
                        parsedUrlData.getRepositoryName()); 
        for (GeneralIssuesSnapshot snap: listFromDB) {
            System.out.println(snap);
        }
    } 
}