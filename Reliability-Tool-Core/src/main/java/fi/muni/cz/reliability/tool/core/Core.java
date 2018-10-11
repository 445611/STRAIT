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
import fi.muni.cz.reliability.tool.dataprocessing.output.HtmlOutputWriter;
import fi.muni.cz.reliability.tool.dataprocessing.output.OutputData;
import fi.muni.cz.reliability.tool.dataprocessing.output.OutputWriter;
import fi.muni.cz.reliability.tool.dataprocessing.persistence.GeneralIssuesSnapshot;
import fi.muni.cz.reliability.tool.dataprovider.DataProvider;
import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import fi.muni.cz.reliability.tool.dataprovider.GitHubDataProvider;
import fi.muni.cz.reliability.tool.dataprovider.authenticationdata.GitHubAuthenticationDataProvider;
import fi.muni.cz.reliability.tool.models.GOModel;
import fi.muni.cz.reliability.tool.models.Model;
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

    public static final String AUTH_FILE_NAME = "git_hub_authentication_file.properties";
    
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
                    + "URL of github.com repository.\n"
                    + "\n"
                    + "Predisction sign\n"
                    + "     -p\n"
                    + "\n"
                    + "Number of time periods to predict\n"
                    + "Errors:\n"
                    + e.causes());
        }
        doAnalysis(parser);
    }
    
    /**
     * Analysis.
     * @param parser with data.
     */
    public static void doAnalysis(ArgsParser parser) {
        
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
        Model model = new GOModel(new double[]{1,1}, countedWeeksWithTotal, goodnessOfFitTest);
        //Model model = new MusaOkumotoModelImpl(new double[]{1,1}, countedWeeksWithTotal, goodnessOfFitTest);
        model.estimateModelData();
        TrendTest trendTest = new LaplaceTrendTest();
        trendTest.executeTrendTest(filteredList);
        // MODEL
        
        // OUTPUT
        OutputWriter writer = new HtmlOutputWriter();
        
        OutputData prepareOutputData = writer.prepareOutputData(parser.getParsedUrlData().getUrl().toString(), 
                countedWeeksWithTotal);
        prepareOutputData.setTimeBetweenDefects(timeBetweenList);
        prepareOutputData.setTrend(trendTest.getTrendValue());
        prepareOutputData.setExistTrend(trendTest.getResult());
        prepareOutputData.setModelParameters(model.getModelParameters());
        prepareOutputData.setGoodnessOfFit(model.getGoodnessOfFitData());
        prepareOutputData.setEstimatedIssuesPrediction(model.getIssuesPrediction(10));
        prepareOutputData.setModelName(model.toString());
        prepareOutputData.setModelFunction(model.getTextFormOfTheFunction());
        prepareOutputData.setStartOfTesting(startOfTesting == null ? 
                filteredList.get(0).getCreatedAt() : startOfTesting);
        prepareOutputData.setEndOfTesting(endOfTesting == null ? new Date() : endOfTesting);
        writer.writeOutputDataToFile(prepareOutputData, parser.getParsedUrlData().getRepositoryName());
        // OUTPUT
        
        java.awt.Toolkit.getDefaultToolkit().beep();
    }
}