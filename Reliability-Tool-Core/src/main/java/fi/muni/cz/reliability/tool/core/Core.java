package fi.muni.cz.reliability.tool.core;

import fi.muni.cz.reliability.tool.core.exception.InvalidInputException;
import fi.muni.cz.reliability.tool.core.factory.FilterFactory;
import fi.muni.cz.reliability.tool.core.factory.IssuesWriterFactory;
import fi.muni.cz.reliability.tool.core.factory.ModelFactory;
import fi.muni.cz.reliability.tool.core.factory.OutputWriterFactory;
import fi.muni.cz.reliability.tool.core.factory.ProcessorFactory;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.Filter;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.IssuesProcessor;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.modeldata.CumulativeIssuesCounter;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.modeldata.IssuesCounter;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.modeldata.TimeBetweenIssuesCounter;
import fi.muni.cz.reliability.tool.dataprocessing.output.OutputData;
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
     * @param args the command line arguments
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
            System.out.println(e.getCause());
            System.exit(1);
        }
    }
    
    private static void run() throws InvalidInputException {
        System.out.println("Working...");
        
        if (PARSER.getCmdl().hasOption(ArgsParser.OPT_LIST_ALL_SNAPSHOTS)) {
            doListAllSnapshots();
        } else if (PARSER.getCmdl().hasOption(ArgsParser.OPT_HELP)) {
            PARSER.printHelp();
        } else if (PARSER.getCmdl().hasOption(ArgsParser.OPT_URL) && 
                PARSER.getCmdl().hasOption(ArgsParser.OPT_SAVE)) {
            checkUrl(PARSER.getCmdl().getOptionValue(ArgsParser.OPT_URL));
            doSaveToFileFromUrl();
            System.out.println("Saved to file.");
        } else if (PARSER.getCmdl().hasOption(ArgsParser.OPT_URL) && 
                PARSER.getCmdl().hasOption(ArgsParser.OPT_LIST_SNAPSHOTS)) {
            checkUrl(PARSER.getCmdl().getOptionValue(ArgsParser.OPT_URL));
            doListSnapshotsForUrl();
        } else if (PARSER.getCmdl().hasOption(ArgsParser.OPT_URL) && 
                PARSER.getCmdl().hasOption(ArgsParser.OPT_EVALUATE)) {
            checkUrl(PARSER.getCmdl().getOptionValue(ArgsParser.OPT_URL));
            doEvaluateForUrl();
            System.out.println("Evaluated to file.");
        } else if (PARSER.getCmdl().hasOption(ArgsParser.OPT_SNAPSHOT_NAME) &&
                PARSER.getCmdl().hasOption(ArgsParser.OPT_SAVE)) {
            doSaveToFileFromSnapshot();
            System.out.println("Saved.");
        } else if (PARSER.getCmdl().hasOption(ArgsParser.OPT_SNAPSHOT_NAME) &&
                PARSER.getCmdl().hasOption(ArgsParser.OPT_EVALUATE)) {
            doEvaluateForSnapshot();
            System.out.println("Evaluated.");
        } else if (PARSER.getCmdl().hasOption(ArgsParser.OPT_SNAPSHOT_NAME) &&
                PARSER.getCmdl().hasOption(ArgsParser.OPT_LIST_SNAPSHOTS)) {
            PARSER.printHelp();
            System.out.println("[Can't combine '-sn' with '-sl']");
        } else if (PARSER.getCmdl().hasOption(ArgsParser.OPT_SNAPSHOT_NAME)) {
           PARSER.printHelp();
            System.out.println("[Missing option: '-e' / '-s']");
        } else {
            PARSER.printHelp();
            System.out.println("[Missing option: '-e' / '-s' / '-sl']");
        }
        
        System.out.println("Done!");
        System.exit(0);
    }
    
    private static void  doEvaluateForUrl() throws InvalidInputException {
        List<GeneralIssue> listOfGeneralIssues = null;
        if (PARSER.getCmdl().hasOption(ArgsParser.OPT_NEW_SNAPSHOT)) {
            if (DAO.getSnapshotByName(PARSER.getCmdl().getOptionValue(ArgsParser.OPT_NEW_SNAPSHOT)) != null) {
                System.out.println("[-name <New name> should be unique name. '" 
                        + PARSER.getCmdl().getOptionValue(ArgsParser.OPT_NEW_SNAPSHOT) + "' already exists]");
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
                    .setSnapshotName(PARSER.getCmdl().getOptionValue(ArgsParser.OPT_NEW_SNAPSHOT))
                    .build());
    }
    
    private static void doEvaluateForSnapshot() throws InvalidInputException {
        GeneralIssuesSnapshotDaoImpl dao = new GeneralIssuesSnapshotDaoImpl();
        GeneralIssuesSnapshot snapshot = dao.getSnapshotByName(PARSER.getCmdl()
                .getOptionValue(ArgsParser.OPT_SNAPSHOT_NAME));
        checkUrl(snapshot.getUrl());
        doEvaluate(snapshot.getListOfGeneralIssues());
    }
    
    private static List<GeneralIssue> runFilters(List<GeneralIssue> listOfGeneralIssues) {
        List<GeneralIssue> filteredList = new ArrayList<>();
        filteredList.addAll(listOfGeneralIssues);
        List<Filter> listOfFilters = FilterFactory.getFilters(PARSER.getCmdl());
        for (Filter filter: listOfFilters) {
            filteredList = filter.filter(filteredList);
        }
        return filteredList;
    }
    
    private static Date getStartOfTesting(List<GeneralIssue> listOfGeneralIssues) {
        return listOfGeneralIssues.get(0).getCreatedAt();
    }
    
    private static Date getEndOfTesting() {
        return new Date();
    }
    
    private static String getPeriodOfTesting() {
        return IssuesCounter.WEEKS;
    }
    
    private static String getTimeBetweenIssuesUnit() {
        return IssuesCounter.HOURS;
    }
    
    private static List<Pair<Integer, Integer>> getTimeBetweenIssuesList(List<GeneralIssue> listOfGeneralIssues) {
        return new TimeBetweenIssuesCounter(getTimeBetweenIssuesUnit())
                        .prepareIssuesDataForModel(listOfGeneralIssues);
    }
    
    private static List<Pair<Integer, Integer>> getCumulativeIssuesList(List<GeneralIssue> listOfGeneralIssues) {
        return new CumulativeIssuesCounter(getPeriodOfTesting(), 
                        getStartOfTesting(listOfGeneralIssues), getEndOfTesting())
                .prepareIssuesDataForModel(listOfGeneralIssues);
    }
    
    private static List<Model> runModels(List<Pair<Integer, Integer>> countedWeeksWithTotal, 
            GoodnessOfFitTest goodnessOfFitTest) throws InvalidInputException {
        List<Model> models = ModelFactory.getModels(countedWeeksWithTotal, goodnessOfFitTest, PARSER.getCmdl());
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
        if (PARSER.getCmdl().hasOption(ArgsParser.OPT_PREDICT)) {
            try {
                return Integer.parseInt(PARSER.getCmdl().getOptionValue(ArgsParser.OPT_PREDICT));
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
        List<IssuesProcessor> listOfProcessors = ProcessorFactory.getProcessors(PARSER.getCmdl());
        for (IssuesProcessor processor: listOfProcessors) {
            processedList = processor.process(processedList);
        }
        return processedList;
    }
    
    private static void writeOutput(List<OutputData> outputDataList) throws InvalidInputException {
        OutputWriterFactory.getIssuesWriter(PARSER.getCmdl())
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
                    .setStartOfTesting(getStartOfTesting(listOfGeneralIssues))
                    .setEndOfTesting(getEndOfTesting())
                    .setInitialNumberOfIssues(initialNumberOfIssues)
                    .setFiltersUsed(FilterFactory.getFiltersRanWithInfoAsList(PARSER.getCmdl()))
                    .setProcessorsUsed(ProcessorFactory.getProcessorsRanWithInfoAsList(PARSER.getCmdl()))
                    .setTestingPeriodsUnit(getPeriodOfTesting())
                    .setTimeBetweenDefectsUnit(getTimeBetweenIssuesUnit()).build();
            outputDataList.add(outputData);
        }
        return outputDataList;
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
                getIssuesByUrl(PARSER.getCmdl().getOptionValue(ArgsParser.OPT_URL));
        doSaveToFile(listOfInitialIssues, parsedUrlData.getRepositoryName());
    }
    
    private static void doSaveToFileFromSnapshot() throws InvalidInputException {
        String snapshotName = PARSER.getCmdl().getOptionValue(ArgsParser.OPT_SNAPSHOT_NAME);
        doSaveToFile(DAO.getSnapshotByName(snapshotName).getListOfGeneralIssues(), snapshotName);
    }
    
    private static void doSaveToFile(List<GeneralIssue> listOfInitialIssues, String fileName) 
            throws InvalidInputException {
        IssuesWriterFactory.getIssuesWriter(PARSER.getCmdl()).writeToFile(runFilters(listOfInitialIssues), fileName);
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