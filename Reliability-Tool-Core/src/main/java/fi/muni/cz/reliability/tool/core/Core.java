package fi.muni.cz.reliability.tool.core;

import fi.muni.cz.reliability.tool.core.exception.InvalidInputException;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.Filter;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.FilterByLabel;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.FilterClosed;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.configuration.FilteringConfiguration;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.configuration.FilteringConfigurationImpl;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class Core {

    private static final String AUTH_FILE_NAME = "git_hub_authentication_file.properties";
    private static final String CSV_FILE_SUFFIX = "csv";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            run(args);
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
    }
    
    /**
     * Run method.
     * @param args as input
     */
    public static void run(String[] args) {
        ArgsParser parser = new ArgsParser();
        try {
            parser.parse(args);
        } catch (InvalidInputException e) {
            throw new IllegalArgumentException("Invalid input.\n"
                    + "Expected arguments:\n"
                    + "+ URL of github.com repository.\n"
                    + "+ Evaluate / Save data / List snapshots flag, -eval / -save / -list, respectively.\n"
                    + "- Number of time periods to predict.\n"
                    + "\n"
                    + "(+ Required argumnet, - optional argument)\n"
                    + "e.g.: www.github.com/ambv/black -eval 10\n"
                    + "\n"
                    + "Errors:\n"
                    + e.causes());
        }
        if (parser.isEvaluate()) {
            System.out.println("Analyzing");
            doAnalysis(parser); 
        } else if (parser.isSave()) {
            System.out.println("Saving");
            doSave(parser);
        } else if (parser.isList() || parser.isListAll()) {
            doList(parser);
        }
        System.out.println("Done!");
        System.exit(0);
    }
    
    /**
     * Analysis.
     * @param parser with data.
     */
    private static void doAnalysis(ArgsParser parser) {
        
        GitHubAuthenticationDataProvider authProvider = new GitHubAuthenticationDataProvider(AUTH_FILE_NAME);
        DataProvider dataProvider = new GitHubDataProvider(authProvider.getGitHubClientWithCreditials());
        
        List<GeneralIssue> listOfInitialIssues = dataProvider.
                getIssuesByUrl(parser.getParsedUrlData().getUrl().toString());

        // FILTERS
        FilteringConfiguration setup = new FilteringConfigurationImpl();
        List<String> filteringWords = setup.loadFilteringWordsFromFile();
        Filter issuesFilterByLabel = new FilterByLabel(filteringWords);
        List<GeneralIssue> filteredList = issuesFilterByLabel.filter(listOfInitialIssues);
        Filter issuesFilterClosed = new FilterClosed();
        filteredList = issuesFilterClosed.filter(filteredList);
        // FILTERS
        
        int periodicOfTesting = Calendar.WEEK_OF_MONTH;
        Date startOfTesting = null;
        Date endOfTesting = null;
        String testingPeriodsUnit = "week";
        
        // SNAPSHOT
        GeneralIssuesSnapshot snapshot = new GeneralIssuesSnapshot();
        snapshot.setCreatedAt(new Date());
        snapshot.setHowManyTimeUnitsToAdd(1);
        snapshot.setTypeOfTimeToSplitTestInto(periodicOfTesting);
        snapshot.setFiltersRan(Arrays.asList(issuesFilterByLabel.toString(), issuesFilterClosed.toString()));
        snapshot.setListOfGeneralIssues(filteredList);
        snapshot.setRepositoryName(parser.getParsedUrlData().getRepositoryName());
        snapshot.setUrl(parser.getParsedUrlData().getUrl().toString());
        snapshot.setUserName(parser.getParsedUrlData().getUserName());
        snapshot.setStartOfTesting(startOfTesting == null ? filteredList.get(0).getCreatedAt() : startOfTesting);
        snapshot.setEndOfTesting(endOfTesting == null ? new Date() : endOfTesting);
        // SNAPSHOT
        
        // DATABASE
        /*GeneralIssuesSnapshotDaoImpl dao = new GeneralIssuesSnapshotDaoImpl();
        dao.save(snapshot);
        List<GeneralIssuesSnapshot> fromDB = dao.getAllSnapshots(); 
        for (GeneralIssuesSnapshot snap: fromDB) {
            System.out.println(snap.getListOfGeneralIssues().size());
        }
        System.out.println(fromDB.get(fromDB.size() - 1).getFiltersRan());*/
        // DATABASE
        
        // COUNTERS
        IssuesCounter counter = new IntervalIssuesCounter(periodicOfTesting, 1, 
                startOfTesting, endOfTesting);
        List<Pair<Integer, Integer>> countedWeeks = counter.prepareIssuesDataForModel(filteredList);
        
        IssuesCounter cumulativeCounter = new CumulativeIssuesCounter(periodicOfTesting, 1, 
                startOfTesting, endOfTesting);
        List<Pair<Integer, Integer>> countedWeeksWithTotal = cumulativeCounter.prepareIssuesDataForModel(filteredList);
        
        IssuesCounter timeBetween = new TimeBetweenIssuesCounter();
        List<Pair<Integer, Integer>> timeBetweenList = timeBetween.prepareIssuesDataForModel(filteredList);
        // COUNTERS
        
        // MODEL
        GoodnessOfFitTest goodnessOfFitTest = new ChiSquareGoodnessOfFitTest();
        Model goModel = new GOModelImpl(new double[]{1,1}, countedWeeksWithTotal, goodnessOfFitTest);
        Model moModel = new MusaOkumotoModelImpl(new double[]{1,1}, countedWeeksWithTotal, goodnessOfFitTest);
        Model duaneModel = new DuaneModelImpl(new double[]{1,1}, countedWeeksWithTotal, goodnessOfFitTest);
        Model goSShapedModel = new GOSShapedModelImpl(new double[]{1,1}, countedWeeksWithTotal, goodnessOfFitTest);
        Model hdModel = new HossainDahiyaModelImpl(new double[]{countedWeeksWithTotal
                .get(countedWeeksWithTotal.size() - 1).getSecond(),1,1}, countedWeeksWithTotal, goodnessOfFitTest);
        hdModel.estimateModelData();
        duaneModel.estimateModelData();
        goModel.estimateModelData();
        moModel.estimateModelData();
        goSShapedModel.estimateModelData();
        TrendTest trendTest = new LaplaceTrendTest();
        trendTest.executeTrendTest(filteredList);
        // MODEL
        
        // OUTPUT
        OutputWriter writer = new HtmlOutputWriter();
        
        OutputData hdData = new OutputData();
        hdData.setModelName(hdModel.toString());
        hdData.setModelFunction(hdModel.getTextFormOfTheFunction());
        hdData.setModelParameters(hdModel.getModelParameters());
        hdData.setGoodnessOfFit(hdModel.getGoodnessOfFitData());
        hdData.setEstimatedIssuesPrediction(hdModel.getIssuesPrediction(parser.getPredictionLength()));
        hdData.setWeeksAndDefects(countedWeeksWithTotal);
        hdData.setTestingPeriodsUnit(testingPeriodsUnit);
        
        OutputData goSShapedData = new OutputData();
        goSShapedData.setModelName(goSShapedModel.toString());
        goSShapedData.setModelFunction(goSShapedModel.getTextFormOfTheFunction());
        goSShapedData.setModelParameters(goSShapedModel.getModelParameters());
        goSShapedData.setGoodnessOfFit(goSShapedModel.getGoodnessOfFitData());
        goSShapedData.setEstimatedIssuesPrediction(goSShapedModel.getIssuesPrediction(parser.getPredictionLength()));
        goSShapedData.setWeeksAndDefects(countedWeeksWithTotal);
        goSShapedData.setTestingPeriodsUnit(testingPeriodsUnit);
        
        OutputData goData = new OutputData();
        goData.setModelName(goModel.toString());
        goData.setModelFunction(goModel.getTextFormOfTheFunction());
        goData.setModelParameters(goModel.getModelParameters());
        goData.setGoodnessOfFit(goModel.getGoodnessOfFitData());
        goData.setEstimatedIssuesPrediction(goModel.getIssuesPrediction(parser.getPredictionLength()));
        goData.setWeeksAndDefects(countedWeeksWithTotal);
        goData.setTestingPeriodsUnit(testingPeriodsUnit);
        
        OutputData moData = new OutputData();
        moData.setModelName(moModel.toString());
        moData.setModelFunction(moModel.getTextFormOfTheFunction());
        moData.setModelParameters(moModel.getModelParameters());
        moData.setGoodnessOfFit(moModel.getGoodnessOfFitData());
        moData.setEstimatedIssuesPrediction(moModel.getIssuesPrediction(parser.getPredictionLength()));
        moData.setWeeksAndDefects(countedWeeksWithTotal);
        moData.setTestingPeriodsUnit(testingPeriodsUnit);
        
        OutputData prepareOutputData = writer.prepareOutputData(parser.getParsedUrlData().getUrl().toString(), 
                countedWeeksWithTotal);
        prepareOutputData.setTimeBetweenDefects(timeBetweenList);
        prepareOutputData.setTrend(trendTest.getTrendValue());
        prepareOutputData.setExistTrend(trendTest.getResult());
        prepareOutputData.setModelParameters(duaneModel.getModelParameters());
        prepareOutputData.setGoodnessOfFit(duaneModel.getGoodnessOfFitData());
        prepareOutputData.setEstimatedIssuesPrediction(duaneModel.getIssuesPrediction(parser.getPredictionLength()));
        prepareOutputData.setModelName(duaneModel.toString());
        prepareOutputData.setModelFunction(duaneModel.getTextFormOfTheFunction());
        prepareOutputData.setStartOfTesting(startOfTesting == null ? 
                filteredList.get(0).getCreatedAt() : startOfTesting);
        prepareOutputData.setEndOfTesting(endOfTesting == null ? new Date() : endOfTesting);
        
        prepareOutputData.setInitialNumberOfIssues(listOfInitialIssues.size());
        prepareOutputData.setFiltersUsed(Arrays.asList(issuesFilterByLabel.infoAboutFilter(), 
                issuesFilterClosed.infoAboutFilter()));
        prepareOutputData.setProcessorsUsed(Arrays.asList());
        prepareOutputData.setTestingPeriodsUnit(testingPeriodsUnit);
        
        writer.writeOutputDataToFile(Arrays.asList(prepareOutputData, goData, moData, goSShapedData, hdData), 
                parser.getParsedUrlData().getRepositoryName() 
                + " - Models comparison");
        writer.writeOutputDataToFile(Arrays.asList(prepareOutputData), 
                parser.getParsedUrlData().getRepositoryName() 
                + " - " + prepareOutputData.getModelName());
        // OUTPUT
        
        java.awt.Toolkit.getDefaultToolkit().beep();
    }

    private static void doSave(ArgsParser parser) {
        
        
        GitHubAuthenticationDataProvider authProvider = new GitHubAuthenticationDataProvider(AUTH_FILE_NAME);
        DataProvider dataProvider = new GitHubDataProvider(authProvider.getGitHubClientWithCreditials());
        
        List<GeneralIssue> listOfInitialIssues = dataProvider.
                getIssuesByUrl(parser.getParsedUrlData().getUrl().toString());
        
        // FILTERS
        FilteringConfiguration setup = new FilteringConfigurationImpl();
        List<String> filteringWords = setup.loadFilteringWordsFromFile();
        Filter issuesFilterByLabel = new FilterByLabel(filteringWords);
        List<GeneralIssue> filteredList = issuesFilterByLabel.filter(listOfInitialIssues);
        Filter issuesFilterClosed = new FilterClosed();
        filteredList = issuesFilterClosed.filter(filteredList);
        
        CsvFileWriter.writeCsvFile(filteredList, parser.getParsedUrlData().getRepositoryName() + "." + CSV_FILE_SUFFIX);
    }

    private static void doList(ArgsParser parser) {
        if (parser.isListAll()) {
            listAllSnapshots();
        } else if (parser.isList()) {
            listAllSnapshotsForUrl(parser);
        }
    }
    
    private static void listAllSnapshots() {
        GeneralIssuesSnapshotDaoImpl dao = new GeneralIssuesSnapshotDaoImpl();
        List<GeneralIssuesSnapshot> listFromDB = dao.getAllSnapshots(); 
        for (GeneralIssuesSnapshot snap: listFromDB) {
            System.out.println(snap);
        }
    }
    
    private static void listAllSnapshotsForUrl(ArgsParser parser) {
        GeneralIssuesSnapshotDaoImpl dao = new GeneralIssuesSnapshotDaoImpl();
        List<GeneralIssuesSnapshot> listFromDB = dao.getAllSnapshotsForUserAndRepository(
                parser.getParsedUrlData().getUserName(), parser.getParsedUrlData().getRepositoryName()); 
        if (listFromDB.isEmpty()) {
            System.out.println("No snapshots for: " + parser.getParsedUrlData().getUrl().toString());
        } else {
            for (GeneralIssuesSnapshot snap: listFromDB) {
                System.out.println(snap);
            }
        }
    }
}