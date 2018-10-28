package fi.muni.cz.reliability.tool.core;

import fi.muni.cz.reliability.tool.dataprovider.DataProvider;
import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import fi.muni.cz.reliability.tool.dataprovider.GitHubDataProvider;
import fi.muni.cz.reliability.tool.dataprovider.authenticationdata.GitHubAuthenticationDataProvider;
//import fi.muni.cz.reliability.tool.models.GOModel;
import fi.muni.cz.reliability.tool.models.Model;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.modeldata.CumulativeIssuesCounter;

import fi.muni.cz.reliability.tool.dataprocessing.output.OutputWriter;

import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.configuration.FilteringConfigurationImpl;
import fi.muni.cz.reliability.tool.dataprocessing.output.OutputData;
import java.util.Calendar;
import java.util.List;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.configuration.FilteringConfiguration;


import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.Filter;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.FilterByLabel;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.FilterClosed;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.modeldata.IntervalIssuesCounter;

import fi.muni.cz.reliability.tool.dataprocessing.persistence.GeneralIssuesSnapshot;
import fi.muni.cz.reliability.tool.dataprocessing.output.HtmlOutputWriter;
import fi.muni.cz.reliability.tool.dataprovider.utils.GitHubUrlParser;
import fi.muni.cz.reliability.tool.dataprovider.utils.ParsedUrlData;
import fi.muni.cz.reliability.tool.dataprovider.utils.UrlParser;
import fi.muni.cz.reliability.tool.models.testing.ChiSquareGoodnessOfFitTest;
import fi.muni.cz.reliability.tool.models.testing.GoodnessOfFitTest;
import fi.muni.cz.reliability.tool.models.testing.LaplaceTrendTest;
import fi.muni.cz.reliability.tool.models.testing.TrendTest;
import java.util.Arrays;
import java.util.Date;
import org.apache.commons.math3.util.Pair;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.modeldata.IssuesCounter;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.modeldata.TimeBetweenIssuesCounter;
import fi.muni.cz.reliability.tool.models.DuaneModelImpl;

//import fi.muni.cz.reliability.tool.dataprocessing.persistence.GeneralIssuesSnapshotDaoImpl;


/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class TEMPORARY {
    
    /*TRUE ALL*///public static final String URL = "https://github.com/eclipse/sumo/";
    /*0.0001 false*///public static final String URL = "https://github.com/beetbox/beets";
    /*never false*///public static final String URL = "https://github.com/spring-projects/spring-boot";
    /*0.05 false*///public static final String URL = "https://github.com/google/guava";
    /*false all*///public static final String URL = "https://github.com/445611/PB071/";
    //public static final String url = "https://github.com/google/error-prone";
    //public static final String URL = "https://github.com/ambv/black";
    //public static final String URL = "https://github.com/facebook/react";
    //public static final String URL = "https://github.com/angular/angular";
    
    public static final String AUTH_FILE_NAME = "git_hub_authentication_file.properties";
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //run("https://github.com/spring-projects/spring-boot");
        //run("https://github.com/eclipse/sumo/");
        //run("https://github.com/angular/angular");
        
        run("https://github.com/beetbox/beets");
        run("https://github.com/google/guava");
        run("https://github.com/google/error-prone");
        run("https://github.com/ambv/black");
        run("https://github.com/facebook/react");
        run("https://github.com/445611/PB071");
        System.exit(0);
    }
    
    /**
     * sdf
     * @param url sdf
     */
    public static void run(String url) {
        
        GitHubAuthenticationDataProvider authProvider = new GitHubAuthenticationDataProvider(AUTH_FILE_NAME);
        DataProvider dataProvider = new GitHubDataProvider(authProvider.getGitHubClientWithCreditials());
        
        List<GeneralIssue> list1 = dataProvider.getIssuesByUrl(url);

        FilteringConfiguration setup = new FilteringConfigurationImpl();
        //setup.addWordToConfigFile("sumo");
        List<String> filteringWords = setup.loadFilteringWordsFromFile();
        Filter issuesFilterByLabel = new FilterByLabel(filteringWords);
        List<GeneralIssue> filteredList = issuesFilterByLabel.filter(list1);
        Filter issuesFilterClosed = new FilterClosed();
        filteredList = issuesFilterClosed.filter(filteredList);
        Calendar cal1 = Calendar.getInstance();
        cal1.set(2008, 1, 1);
        Calendar cal2 = Calendar.getInstance();
        cal2.set(2020, 1, 1);
        
        String periodicOfTesting = IssuesCounter.WEEKS;
        
        GeneralIssuesSnapshot snapshot = new GeneralIssuesSnapshot();
        snapshot.setCreatedAt(new Date());
        snapshot.setHowManyTimeUnitsToAdd(1);
        //snapshot.setTypeOfTimeToSplitTestInto(periodicOfTesting);
        snapshot.setFiltersRan(Arrays.asList(issuesFilterByLabel.toString(), issuesFilterClosed.toString()));
        snapshot.setListOfGeneralIssues(filteredList);
        
        UrlParser parser = new GitHubUrlParser();
        ParsedUrlData parsedUrldata = parser.parseUrlAndCheck(url);
        snapshot.setRepositoryName(parsedUrldata.getRepositoryName());
        snapshot.setUrl(url);
        snapshot.setUserName(parsedUrldata.getUserName());
        
        //----------------------------Database-----------------------------------------
        /*GeneralIssuesSnapshotDaoImpl dao = new GeneralIssuesSnapshotDaoImpl();
        //dao.save(snapshot);
        List<GeneralIssuesSnapshot> fromDB = dao.getAllSnapshots(); 
        for (GeneralIssuesSnapshot snap: fromDB) {
            System.out.println(snap.getListOfGeneralIssues().size());
        }
        System.out.println(fromDB.get(fromDB.size() - 1).getFiltersRan());*/
        //------------------------------------------------------------------------------
        System.out.println(issuesFilterByLabel.toString());
        
        Date startOfTesting = null;
        Date endOfTesting = null;
        snapshot.setStartOfTesting(startOfTesting == null ? filteredList.get(0).getCreatedAt() : startOfTesting);
        snapshot.setEndOfTesting(endOfTesting == null ? new Date() : endOfTesting);
        
        IssuesCounter counter = new IntervalIssuesCounter(periodicOfTesting, 1, 
                startOfTesting, endOfTesting);
        List<Pair<Integer, Integer>> countedWeeks = counter.prepareIssuesDataForModel(filteredList);
        IssuesCounter cumulativeCounter = new CumulativeIssuesCounter(periodicOfTesting, 1, 
                startOfTesting, endOfTesting);
        List<Pair<Integer, Integer>> countedWeeksWithTotal = cumulativeCounter.prepareIssuesDataForModel(filteredList);
        
        IssuesCounter timeBetween = new TimeBetweenIssuesCounter(periodicOfTesting);
        List<Pair<Integer, Integer>> timeBetweenList = timeBetween.prepareIssuesDataForModel(filteredList);
        //TEMPORARYWriter.write(timeBetweenList);
        
        //--------------------ADDED-------------------------
        GoodnessOfFitTest goodnessOfFitTest = new ChiSquareGoodnessOfFitTest();
        Model model = new DuaneModelImpl(countedWeeksWithTotal, goodnessOfFitTest);
        snapshot.setModelName(model.toString());
        model.estimateModelData();
        //------------------------------------------------------
        
        System.out.println(model.getTextFormOfTheFunction());
        
        OutputWriter writer = new HtmlOutputWriter(true);
        
        //int totalDefects = countedWeeksWithTotal.get(countedWeeksWithTotal.size() - 1).getSecond();
        OutputData prepareOutputData = writer.prepareOutputData(url, countedWeeksWithTotal);
        //prepareOutputData.setTotalNumberOfDefects(totalDefects);
        
        //----------------------REQUIRED TO SET------------------------------------
        //----------------------TimeBetween----------------------------------------
        /*TEMPORARYFileDataProvider prov = new TEMPORARYFileDataProvider();
        List<Pair<Integer, Integer>> testData = prov.getIssuesByUrl(null);
        TrendTest trendTest2 = new LaplaceTrendTest();
        GoodnessOfFitTest goodnessOfFitTest2 = new ChiSquareGoodnessOfFitTest();
        Model model2 = new GOModel(new double[]{1,1}, testData, goodnessOfFitTest2);
        model2.estimateModelData();*/
        
        TrendTest trendTest = new LaplaceTrendTest(IssuesCounter.DAYS);
        trendTest.executeTrendTest(filteredList);
        prepareOutputData.setTimeBetweenDefects(timeBetweenList);
        prepareOutputData.setTrend(trendTest.getTrendValue());
        prepareOutputData.setExistTrend(trendTest.getResult());
        //-------------------------------------------------------------------------
        prepareOutputData.setModelParameters(model.getModelParameters());
        prepareOutputData.setGoodnessOfFit(model.getGoodnessOfFitData());
        prepareOutputData.setEstimatedIssuesPrediction(model.getIssuesPrediction(0));
        prepareOutputData.setModelName(model.toString());
        prepareOutputData.setModelFunction(model.getTextFormOfTheFunction());
        prepareOutputData.setStartOfTesting(startOfTesting == null ? 
                filteredList.get(0).getCreatedAt() : startOfTesting);
        prepareOutputData.setEndOfTesting(endOfTesting == null ? new Date() : endOfTesting);
        //-------------------------------------------------------------------------
        
        //-------------------------------Goodness of fit---------------------------
        
        //------------------------------------------------------------------------        
                
                
        writer.writeOutputDataToFile(Arrays.asList(prepareOutputData), parsedUrldata.getRepositoryName() 
                + " - " + model.toString());
        java.awt.Toolkit.getDefaultToolkit().beep();
        
    }
}