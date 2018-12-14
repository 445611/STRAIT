package fi.muni.cz.core;

import static fi.muni.cz.core.RunConfiguration.LIST_ALL_SNAPSHOTS;
import static fi.muni.cz.core.RunConfiguration.NOT_SUPPORTED;
import static fi.muni.cz.core.RunConfiguration.SNAPSHOT_NAME_AND_EVALUATE;
import static fi.muni.cz.core.RunConfiguration.SNAPSHOT_NAME_AND_LIST_SNAPSHOTS;
import static fi.muni.cz.core.RunConfiguration.SNAPSHOT_NAME_AND_SAVE;
import static fi.muni.cz.core.RunConfiguration.URL_AND_EVALUATE;
import static fi.muni.cz.core.RunConfiguration.URL_AND_LIST_SNAPSHOTS;
import static fi.muni.cz.core.RunConfiguration.URL_AND_SAVE;
import fi.muni.cz.core.exception.InvalidInputException;
import fi.muni.cz.core.factory.FilterFactory;
import fi.muni.cz.core.factory.IssuesWriterFactory;
import fi.muni.cz.core.factory.ModelFactory;
import fi.muni.cz.core.factory.OutputWriterFactory;
import fi.muni.cz.core.factory.ProcessorFactory;
import fi.muni.cz.dataprocessing.issuesprocessing.Filter;
import fi.muni.cz.dataprocessing.issuesprocessing.IssuesProcessor;
import fi.muni.cz.dataprocessing.issuesprocessing.modeldata.CumulativeIssuesCounter;
import fi.muni.cz.dataprocessing.issuesprocessing.modeldata.IssuesCounter;
import fi.muni.cz.dataprocessing.issuesprocessing.modeldata.TimeBetweenIssuesCounter;
import fi.muni.cz.dataprocessing.output.OutputData;
import fi.muni.cz.dataprocessing.persistence.GeneralIssuesSnapshot;
import fi.muni.cz.dataprocessing.persistence.GeneralIssuesSnapshotDaoImpl;
import fi.muni.cz.dataprovider.DataProvider;
import fi.muni.cz.dataprovider.GeneralIssue;
import fi.muni.cz.dataprovider.GitHubDataProvider;
import fi.muni.cz.dataprovider.authenticationdata.GitHubAuthenticationDataProvider;
import fi.muni.cz.dataprovider.utils.GitHubUrlParser;
import fi.muni.cz.dataprovider.utils.ParsedUrlData;
import fi.muni.cz.dataprovider.utils.UrlParser;
import fi.muni.cz.models.Model;
import fi.muni.cz.models.testing.ChiSquareGoodnessOfFitTest;
import fi.muni.cz.models.testing.GoodnessOfFitTest;
import fi.muni.cz.models.testing.LaplaceTrendTest;
import fi.muni.cz.models.testing.TrendTest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class Core {

    private static final ArgsParser PARSER = new ArgsParser();
    private static final DataProvider DATA_PROVIDER = 
            new GitHubDataProvider(new GitHubAuthenticationDataProvider().getGitHubClientWithCreditials());
    private static ParsedUrlData parsedUrlData;
    private static final GeneralIssuesSnapshotDaoImpl DAO = new GeneralIssuesSnapshotDaoImpl();
    
    /**
     * Main method, takes command line arguments.
     * 
     * @param args  command line arguments
     */
    public static void main(String[] args) {
        try {
            PARSER.parse(args);
            run();
        } catch (InvalidInputException e) {
            PARSER.printHelp();
            System.out.println(e.causes());
            //e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //e.printStackTrace();
            System.exit(1);
        }
    }
    
    private static void run() throws InvalidInputException {
        System.out.println("Working...");
        switch (PARSER.getRunConfiguration()) {
            case LIST_ALL_SNAPSHOTS:
               doListAllSnapshots();
               break;
            case HELP:
                PARSER.printHelp();
            case URL_AND_SAVE:
                checkUrl(PARSER.getOptionValueUrl());
                doSaveToFileFromUrl();
                System.out.println("Saved to file.");
                break;
            case URL_AND_LIST_SNAPSHOTS:
                checkUrl(PARSER.getOptionValueUrl());
                doListSnapshotsForUrl();
                break;
            case URL_AND_EVALUATE:
                checkUrl(PARSER.getOptionValueUrl());
                doEvaluateForUrl();
                System.out.println("Evaluated to file.");
                break;
            case SNAPSHOT_NAME_AND_SAVE:
                doSaveToFileFromSnapshot();
                System.out.println("Saved.");
                break;
            case SNAPSHOT_NAME_AND_EVALUATE:
                doEvaluateForSnapshot();
                System.out.println("Evaluated.");
                break;
            case SNAPSHOT_NAME_AND_LIST_SNAPSHOTS:
                PARSER.printHelp();
                System.out.println("[Can't combine '-sn' with '-sl']");
                break;
            case NOT_SUPPORTED:
                PARSER.printHelp();
                System.out.println("[Missing option: '-e' / '-s']");
                break;
            default:
                PARSER.printHelp();
                System.out.println("[Missing option: '-e' / '-s' / '-sl']");
        }
        System.out.println("Done!");
        System.exit(0);
    }
    
    private static void  doEvaluateForUrl() throws InvalidInputException {
        List<GeneralIssue> listOfGeneralIssues = null;
        if (PARSER.hasOptionNewSnapshot()) {
            if (DAO.getSnapshotByName(PARSER.getOptionValueNewSnapshot()) != null) {
                System.out.println("[-name <New name> should be unique name. '" 
                        + PARSER.getOptionValueNewSnapshot() + "' already exists]");
                System.exit(1);
            } else {
                listOfGeneralIssues = DATA_PROVIDER.getIssuesByUrl(parsedUrlData.getUrl().toString());
                prepareGeneralIssuesSnapshotAndSave(listOfGeneralIssues);
            }
        } else {
            listOfGeneralIssues = DATA_PROVIDER.getIssuesByUrl(parsedUrlData.getUrl().toString());
        }
        doEvaluate(listOfGeneralIssues);
    }
    
    private static void prepareGeneralIssuesSnapshotAndSave(List<GeneralIssue> listOfGeneralIssues) {
        DAO.save(new GeneralIssuesSnapshot.GeneralIssuesSnapshotBuilder()
                    .setCreatedAt(new Date())
                    .setListOfGeneralIssues(listOfGeneralIssues)
                    .setRepositoryName(parsedUrlData.getRepositoryName())
                    .setUrl(parsedUrlData.getUrl().toString())
                    .setUserName(parsedUrlData.getUserName())
                    .setSnapshotName(PARSER.getOptionValueNewSnapshot())
                    .build());
    }
    
    private static void doEvaluateForSnapshot() throws InvalidInputException {
        GeneralIssuesSnapshotDaoImpl dao = new GeneralIssuesSnapshotDaoImpl();
        GeneralIssuesSnapshot snapshot = dao.getSnapshotByName(PARSER.getOptionValueSnapshotName());
        if (snapshot == null) {
            System.out.println("No such snapshot '" + PARSER.getOptionValueSnapshotName() + "' in database.");
            System.exit(1);
        }
        checkUrl(snapshot.getUrl());
        doEvaluate(snapshot.getListOfGeneralIssues());
    }
    
    private static List<GeneralIssue> runFilters(List<GeneralIssue> listOfGeneralIssues) {
        List<GeneralIssue> filteredList = new ArrayList<>();
        filteredList.addAll(listOfGeneralIssues);
        List<Filter> listOfFilters = FilterFactory.getFilters(PARSER);
        for (Filter filter: listOfFilters) {
            filteredList = filter.filter(filteredList);
        }
        return filteredList;
    } 
    
    private static String getPeriodOfTesting() {
        if (PARSER.hasOptionPeriodOfTestiong()) {
            return PARSER.getOptionValuePeriodOfTesting();
        }
        return IssuesCounter.WEEKS;
    }
    
    private static String getTimeBetweenIssuesUnit() {
        if (PARSER.hasOptionTimeBetweenIssuesUnit()) {
            return PARSER.getOptionValueTimeBetweenIssuesUnit();
        }
        return IssuesCounter.HOURS;
    }
    
    private static List<Pair<Integer, Integer>> getTimeBetweenIssuesList(List<GeneralIssue> listOfGeneralIssues) {
        return new TimeBetweenIssuesCounter(getTimeBetweenIssuesUnit())
                        .prepareIssuesDataForModel(listOfGeneralIssues);
    }
    
    private static List<Pair<Integer, Integer>> getCumulativeIssuesList(List<GeneralIssue> listOfGeneralIssues) {
        return new CumulativeIssuesCounter(getPeriodOfTesting()).prepareIssuesDataForModel(listOfGeneralIssues);
    }
    
    private static List<Model> runModels(List<Pair<Integer, Integer>> countedWeeksWithTotal, 
            GoodnessOfFitTest goodnessOfFitTest) throws InvalidInputException {
        List<Model> models = ModelFactory.getModels(countedWeeksWithTotal, goodnessOfFitTest, PARSER);
        for (Model model: models) {
            model.estimateModelData();
        }
        return models;
    }
    
    private static GoodnessOfFitTest getGoodnessOfFitTest() {
        return new ChiSquareGoodnessOfFitTest();
    }
    
    private static TrendTest runTrendTest(List<GeneralIssue> listOfGeneralIssues) {
        TrendTest trendTest = new LaplaceTrendTest(getTimeBetweenIssuesUnit());
        trendTest.executeTrendTest(listOfGeneralIssues);
        return trendTest;
    }
    
    private static int getLengthOfPrediction() {
        if (PARSER.hasOptionPredict()) {
            try {
                return Integer.parseInt(PARSER.getOptionValuePredict());
            } catch (NumberFormatException e) {
                System.out.println("[Argument of option '-p' is not a number]");
                System.exit(1);
            }
        }
        return 0;
    }
    
    private static List<GeneralIssue> runProcessors(List<GeneralIssue> listOfGeneralIssues) {
        List<GeneralIssue> processedList = new ArrayList<>();
        processedList.addAll(listOfGeneralIssues);
        List<IssuesProcessor> listOfProcessors = ProcessorFactory.getProcessors(PARSER);
        for (IssuesProcessor processor: listOfProcessors) {
            processedList = processor.process(processedList);
        }
        return processedList;
    }
    
    private static void writeOutput(List<OutputData> outputDataList) throws InvalidInputException {
        OutputWriterFactory.getIssuesWriter(PARSER)
                .writeOutputDataToFile(outputDataList, parsedUrlData.getRepositoryName());
    }
    
    private static List<GeneralIssue> runFiltersAndProcessors(List<GeneralIssue> listOfGeneralIssues) {
        List<GeneralIssue> filteredList = checkFilteredListForEmpty(runFilters(listOfGeneralIssues));
        filteredList = runProcessors(filteredList);
        return filteredList;
    }
    
    private static List<GeneralIssue> checkFilteredListForEmpty(List<GeneralIssue> filteredList) {
        if (filteredList.isEmpty()) {
            System.out.println("[There are no issues after filtering]");
            System.exit(1);
        }
        return filteredList;
    }
    
    private static List<OutputData> prepareOutputData(int initialNumberOfIssues, 
            List<GeneralIssue> listOfGeneralIssues, List<Pair<Integer, Integer>> countedWeeksWithTotal, 
            TrendTest trendTest) throws InvalidInputException {
        List<OutputData> outputDataList = new ArrayList<>();
        OutputData outputData;
        for (Model model: runModels(countedWeeksWithTotal, getGoodnessOfFitTest())) {
            outputData = new OutputData.OutputDataBuilder()
                    .setCreatedAt(new Date())
                    .setRepositoryName(parsedUrlData.getRepositoryName())
                    .setUrl(parsedUrlData.getUrl().toString())
                    .setUserName(parsedUrlData.getUserName())
                    .setTotalNumberOfDefects(countedWeeksWithTotal.get(countedWeeksWithTotal.size() - 1).getSecond())
                    .setCumulativeDefects(countedWeeksWithTotal)
                    .setTimeBetweenDefects(getTimeBetweenIssuesList(listOfGeneralIssues))
                    .setTrend(trendTest.getTrendValue())
                    .setExistTrend(trendTest.getResult())
                    .setModelParameters(model.getModelParameters())
                    .setGoodnessOfFit(model.getGoodnessOfFitData())
                    .setEstimatedIssuesPrediction(model.getIssuesPrediction(getLengthOfPrediction()))
                    .setModelName(model.toString())
                    .setModelFunction(model.getTextFormOfTheFunction())
                    .setInitialNumberOfIssues(initialNumberOfIssues)
                    .setFiltersUsed(FilterFactory.getFiltersRanWithInfoAsList(PARSER))
                    .setProcessorsUsed(ProcessorFactory.getProcessorsRanWithInfoAsList(PARSER))
                    .setTestingPeriodsUnit(getPeriodOfTesting())
                    .setTimeBetweenDefectsUnit(getTimeBetweenIssuesUnit())
                    .setSolver(getSolver()).build();
            outputDataList.add(outputData);
        }
        return outputDataList;
    }
    
    private static String getSolver() {
        if (PARSER.hasOptionSolver()) {
            if (PARSER.getOptionValueSolver().equals(ModelFactory.SOLVER_LEAST_SQUARES)) {
                return "Least Squares";
            } else if (PARSER.getOptionValueSolver().equals(ModelFactory.SOLVER_MAXIMUM_LIKELIHOOD)) {
                return "Maximum Likelihood";
            }
        }
        return "Least Squares";
    }
    
    private static void doEvaluate(List<GeneralIssue> listOfGeneralIssues) throws InvalidInputException { 
        List<GeneralIssue> filteredAndProcessedList = runFiltersAndProcessors(listOfGeneralIssues);
        List<Pair<Integer, Integer>> countedWeeksWithTotal = getCumulativeIssuesList(filteredAndProcessedList); 
        TrendTest trendTest = runTrendTest(filteredAndProcessedList); 
        List<OutputData> outputDataList = 
                prepareOutputData(listOfGeneralIssues.size(), 
                        filteredAndProcessedList, countedWeeksWithTotal, trendTest);
        writeOutput(outputDataList);
    }

    private static void doSaveToFileFromUrl() throws InvalidInputException {
        List<GeneralIssue> listOfInitialIssues = DATA_PROVIDER.
                getIssuesByUrl(PARSER.getOptionValueUrl());
        doSaveToFile(listOfInitialIssues, parsedUrlData.getRepositoryName());
    }
    
    private static void doSaveToFileFromSnapshot() throws InvalidInputException {
        String snapshotName = PARSER.getOptionValueSnapshotName();
        doSaveToFile(DAO.getSnapshotByName(snapshotName).getListOfGeneralIssues(), snapshotName);
    }
    
    private static void doSaveToFile(List<GeneralIssue> listOfInitialIssues, String fileName) 
            throws InvalidInputException {
        IssuesWriterFactory.getIssuesWriter(PARSER).writeToFile(runFilters(listOfInitialIssues), fileName);
    }

    private static void doListAllSnapshots() {
        List<GeneralIssuesSnapshot> listFromDB = DAO.getAllSnapshots(); 
        if (listFromDB.isEmpty()) {
            System.out.println("No snapshots in Database.");
        } else {
            for (GeneralIssuesSnapshot snap: listFromDB) {
                System.out.println(snap);
            }
        }
    }

    private static UrlParser getUrlParser() {
        return new GitHubUrlParser();
    }
    
    private static void checkUrl(String url) {
        UrlParser urlParser = getUrlParser();
        parsedUrlData = urlParser.parseUrlAndCheck(url);
    }

    private static void doListSnapshotsForUrl() {
        List<GeneralIssuesSnapshot> listFromDB = DAO.
                getAllSnapshotsForUserAndRepository(parsedUrlData.getUserName(), 
                        parsedUrlData.getRepositoryName()); 
        for (GeneralIssuesSnapshot snap: listFromDB) {
            System.out.println(snap);
        }
    } 
}