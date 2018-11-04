package fi.muni.cz.reliability.tool.core;

import fi.muni.cz.reliability.tool.core.exception.InvalidInputException;
import fi.muni.cz.reliability.tool.core.factory.FilterFactory;
import fi.muni.cz.reliability.tool.core.factory.IssuesWriterFactory;
import fi.muni.cz.reliability.tool.core.factory.ModelFactory;
import fi.muni.cz.reliability.tool.core.factory.OutputWriterFactory;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.modeldata.CumulativeIssuesCounter;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.modeldata.IssuesCounter;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.modeldata.TimeBetweenIssuesCounter;
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
import fi.muni.cz.reliability.tool.models.Model;
import fi.muni.cz.reliability.tool.models.testing.ChiSquareGoodnessOfFitTest;
import fi.muni.cz.reliability.tool.models.testing.GoodnessOfFitTest;
import fi.muni.cz.reliability.tool.models.testing.LaplaceTrendTest;
import fi.muni.cz.reliability.tool.models.testing.TrendTest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class Core {

    private static ArgsParser parser = new ArgsParser();
    private static final DataProvider DATA_PROVIDER = 
            new GitHubDataProvider(new GitHubAuthenticationDataProvider().getGitHubClientWithCreditials());
    private static ParsedUrlData parsedUrlData;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            parser.parse(args);
            run(args);
        } catch (InvalidInputException e) {
            parser.printHelp();
            System.out.println(e.causes());
            //e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.out.println(e.getCause());
            System.exit(1);
        }
    }
    
    private static void run(String[] args) {
        System.out.println("Working...");
        
        if (parser.getCmdl().hasOption(ArgsParser.OPT_LIST_ALL_SNAPSHOTS)) {
            doListAllSnapshots();
        } else if (parser.getCmdl().hasOption(ArgsParser.OPT_HELP)) {
            parser.printHelp();
        } else if (parser.getCmdl().hasOption(ArgsParser.OPT_URL) && 
                parser.getCmdl().hasOption(ArgsParser.OPT_SAVE)) {
            checkUrl(parser.getCmdl().getOptionValue(ArgsParser.OPT_URL));
            doSaveToFileFromUrl();
            System.out.println("Saved to file.");
        } else if (parser.getCmdl().hasOption(ArgsParser.OPT_URL) && 
                parser.getCmdl().hasOption(ArgsParser.OPT_LIST_SNAPSHOTS)) {
            checkUrl(parser.getCmdl().getOptionValue(ArgsParser.OPT_URL));
            doListSnapshotsForUrl();
        } else if (parser.getCmdl().hasOption(ArgsParser.OPT_URL) && 
                parser.getCmdl().hasOption(ArgsParser.OPT_EVALUATE)) {
            checkUrl(parser.getCmdl().getOptionValue(ArgsParser.OPT_URL));
            doEvaluateForUrl();
            System.out.println("Evaluated to file.");
        } else if (parser.getCmdl().hasOption(ArgsParser.OPT_SNAPSHOT_NAME) &&
                parser.getCmdl().hasOption(ArgsParser.OPT_SAVE)) {
            doSaveToFileFromSnapshot();
            System.out.println("Saved.");
        } else if (parser.getCmdl().hasOption(ArgsParser.OPT_SNAPSHOT_NAME) &&
                parser.getCmdl().hasOption(ArgsParser.OPT_EVALUATE)) {
            doEvaluateForSnapshot();
            System.out.println("Evaluated.");
        } else if (parser.getCmdl().hasOption(ArgsParser.OPT_SNAPSHOT_NAME) &&
                parser.getCmdl().hasOption(ArgsParser.OPT_LIST_SNAPSHOTS)) {
            parser.printHelp();
            System.out.println("[Can't combine '-sn' with '-sl']");
        } else if (parser.getCmdl().hasOption(ArgsParser.OPT_SNAPSHOT_NAME)) {
           parser.printHelp();
            System.out.println("[Missing argument: '-e' / '-s']");
        } else {
            parser.printHelp();
            System.out.println("[Missing argument: '-e' / '-s' / '-sl']");
        }
        
        System.out.println("Done!");
        System.exit(0);
    }
    
    private static void  doEvaluateForUrl() {
        GeneralIssuesSnapshotDaoImpl dao = new GeneralIssuesSnapshotDaoImpl();
        List<GeneralIssue> listOfGeneralIssues;
        if (parser.getCmdl().hasOption(ArgsParser.OPT_NEW_SNAPSHOT)) {
            if (dao.getSnapshotByName(parser.getCmdl().getOptionValue(ArgsParser.OPT_NEW_SNAPSHOT)) != null) {
                System.out.println("[-name <New name> should be unique name. '" 
                        + parser.getCmdl().getOptionValue(ArgsParser.OPT_NEW_SNAPSHOT) + "' already exists]");
                System.exit(1);
            } else {
                listOfGeneralIssues = DATA_PROVIDER.getIssuesByUrl(parsedUrlData.getUrl().toString());
                GeneralIssuesSnapshot snapshot = new GeneralIssuesSnapshot.GeneralIssuesSnapshotBuilder()
                    .setCreatedAt(new Date())
                    .setListOfGeneralIssues(listOfGeneralIssues)
                    .setRepositoryName(parsedUrlData.getRepositoryName())
                    .setUrl(parsedUrlData.getUrl().toString())
                    .setUserName(parsedUrlData.getUserName())
                    .setSnapshotName(parser.getCmdl().getOptionValue(ArgsParser.OPT_NEW_SNAPSHOT))
                    .build();
                dao.save(snapshot);
                doEvaluate(listOfGeneralIssues);
            }
        } else {
            listOfGeneralIssues = DATA_PROVIDER.getIssuesByUrl(parsedUrlData.getUrl().toString());
            doEvaluate(listOfGeneralIssues);
        }
    }
    
    private static void doEvaluateForSnapshot() {
        GeneralIssuesSnapshotDaoImpl dao = new GeneralIssuesSnapshotDaoImpl();
        GeneralIssuesSnapshot snapshot = dao.getSnapshotByName(parser.getCmdl()
                .getOptionValue(ArgsParser.OPT_SNAPSHOT_NAME));
        checkUrl(snapshot.getUrl());
        doEvaluate(snapshot.getListOfGeneralIssues());
    }
    
    private static void doEvaluate(List<GeneralIssue> listOfGeneralIssues) {
        int initialNumberOfIssues = listOfGeneralIssues.size();
        //model <- nls(yvalues ~ b1*(1 - exp(-b2*xvalues))/(1 + b3*exp(-b2*xvalues)),
        //start = list(b1 = 70,b2 = 1,b3 = 1), lower = list(b1 = 0,b2 = 0, b3 = 0), algorithm = "port")
        //model <- nls(yvalues ~ a*(1 - (1 + b*xvalues)*exp(-b*xvalues)),
        //start = list(a = 70,b = 1), lower = list(a = 0,b = 0), algorithm = "port")
        
        List<GeneralIssue> filteredList = FilterFactory.runFilters(listOfGeneralIssues, parser.getCmdl());

        String periodicOfTesting = IssuesCounter.WEEKS;
        Date startOfTesting = filteredList.get(0).getCreatedAt();
        Date endOfTesting = new Date();
        
        // SNAPSHOT

        // COUNTERS
        IssuesCounter cumulativeCounter = new CumulativeIssuesCounter(periodicOfTesting, startOfTesting, endOfTesting);
        List<Pair<Integer, Integer>> countedWeeksWithTotal = cumulativeCounter.prepareIssuesDataForModel(filteredList);
        
        String timeBetweenIssuesUnit = IssuesCounter.HOURS;
        IssuesCounter timeBetween = new TimeBetweenIssuesCounter(timeBetweenIssuesUnit);
        List<Pair<Integer, Integer>> timeBetweenList = timeBetween.prepareIssuesDataForModel(filteredList);
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
            models.add(ModelFactory.
                    getIssuesWriter(countedWeeksWithTotal, goodnessOfFitTest, ModelFactory.GOEL_OKUMOTO));
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
        trendTest.executeTrendTest(filteredList);
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
                    .setCumulativeDefects(countedWeeksWithTotal)
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
        OutputWriter writer = OutputWriterFactory.getIssuesWriter(parser.getCmdl());
        writer.writeOutputDataToFile(outputDataList, parsedUrlData.getRepositoryName());
        
        java.awt.Toolkit.getDefaultToolkit().beep();
    }

    private static void doSaveToFileFromUrl() {
        List<GeneralIssue> listOfInitialIssues = DATA_PROVIDER.
                getIssuesByUrl(parser.getCmdl().getOptionValue(ArgsParser.OPT_URL));
        doSaveToFile(listOfInitialIssues, parsedUrlData.getRepositoryName());
    }
    
    private static void doSaveToFileFromSnapshot() {
        GeneralIssuesSnapshotDaoImpl dao = new GeneralIssuesSnapshotDaoImpl();
        String snapshotName = parser.getCmdl().getOptionValue(ArgsParser.OPT_SNAPSHOT_NAME);
        doSaveToFile(dao.getSnapshotByName(snapshotName).getListOfGeneralIssues(), snapshotName);
    }
    
    private static void doSaveToFile(List<GeneralIssue> listOfInitialIssues, String fileName) {
        FilterFactory.runFilters(listOfInitialIssues, parser.getCmdl());
        IssuesWriterFactory.getIssuesWriter(parser.getCmdl()).writeToFile(listOfInitialIssues, fileName);
    }

    private static void doListAllSnapshots() {
        GeneralIssuesSnapshotDaoImpl dao = new GeneralIssuesSnapshotDaoImpl();
        List<GeneralIssuesSnapshot> listFromDB = dao.getAllSnapshots(); 
        if (listFromDB.isEmpty()) {
            System.out.println("No snapshots in Database.");
        } else {
            for (GeneralIssuesSnapshot snap: listFromDB) {
                System.out.println(snap);
            }
        }
    }

    private static void checkUrl(String url) {
        UrlParser urlParser = new GitHubUrlParser();
        parsedUrlData = urlParser.parseUrlAndCheck(url);
    }

    private static void doListSnapshotsForUrl() {
        GeneralIssuesSnapshotDaoImpl dao = new GeneralIssuesSnapshotDaoImpl();
        List<GeneralIssuesSnapshot> listFromDB = dao.
                getAllSnapshotsForUserAndRepository(parsedUrlData.getUserName(), 
                        parsedUrlData.getRepositoryName()); 
        for (GeneralIssuesSnapshot snap: listFromDB) {
            System.out.println(snap);
        }
    } 
}