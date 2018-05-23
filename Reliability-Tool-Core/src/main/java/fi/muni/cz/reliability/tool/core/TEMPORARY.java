package fi.muni.cz.reliability.tool.core;

import fi.muni.cz.reliability.tool.dataprovider.DataProvider;
import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import fi.muni.cz.reliability.tool.dataprovider.GitHubDataProvider;
import fi.muni.cz.reliability.tool.dataprovider.authenticationdata.GitHubAuthenticationDataProvider;
import fi.muni.cz.reliability.tool.models.GOModel;
import fi.muni.cz.reliability.tool.models.Model;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.modeldata.DefectsCounter;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.modeldata.DefectsCounterImpl;

import fi.muni.cz.reliability.tool.dataprocessing.output.OutputWriter;

import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.configuration.FilteringConfigurationImpl;
import fi.muni.cz.reliability.tool.dataprocessing.output.OutputData;
import java.util.Calendar;
import java.util.List;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.configuration.FilteringConfiguration;


import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.Filter;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.FilterByLabel;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.FilterClosed;

import fi.muni.cz.reliability.tool.dataprocessing.persistence.GeneralIssuesSnapshot;
import fi.muni.cz.reliability.tool.dataprocessing.output.HtmlOutputWriter;
import fi.muni.cz.reliability.tool.dataprovider.utils.GitHubUrlParser;
import fi.muni.cz.reliability.tool.dataprovider.utils.ParsedUrlData;
import fi.muni.cz.reliability.tool.dataprovider.utils.UrlParser;
import fi.muni.cz.reliability.tool.models.ModelOutputData;
import fi.muni.cz.reliability.tool.models.goodnessoffit.GoodnessOfFit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class TEMPORARY {
    
    /*TRUE ALL*///public static final String URL = "https://github.com/eclipse/sumo/";
    /*0.0001 false*///public static final String URL = "https://github.com/beetbox/beets";
    /*never false*/public static final String URL = "https://github.com/spring-projects/spring-boot";
    /*0.05 false*///public static final String URL = "https://github.com/google/guava";
    /*false all*///public static final String URL = "https://github.com/445611/PB071/";
    
    public static final String AUTH_FILE_NAME = "git_hub_authentication_file.properties";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        GitHubAuthenticationDataProvider authProvider = new GitHubAuthenticationDataProvider(AUTH_FILE_NAME);
        DataProvider dataProvider = new GitHubDataProvider(authProvider.getGitHubClientWithCreditials());
        
        List<GeneralIssue> list1 = dataProvider.getIssuesByUrl(URL);

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
        
        Model model = new GOModel(new double[]{1,1});
        
        GeneralIssuesSnapshot snapshot = new GeneralIssuesSnapshot();
        snapshot.setCreatedAt(new Date());
        snapshot.setHowManyTimeUnitsToAdd(1);
        snapshot.setTypeOfTimeToSplitTestInto(Calendar.HOUR_OF_DAY);
        snapshot.setFiltersRan(Arrays.asList(issuesFilterByLabel.toString(), issuesFilterClosed.toString()));
        snapshot.setListOfGeneralIssues(filteredList);
        snapshot.setModelName(model.toString());
        UrlParser parser = new GitHubUrlParser();
        ParsedUrlData parsedUrldata = parser.parseUrlAndCheck(URL);
        snapshot.setRepositoryName(parsedUrldata.getRepositoryName());
        snapshot.setUrl(URL);
        snapshot.setUserName(parsedUrldata.getUserName());
        
        //----------------------------Database-----------------------------------------
        /*GeneralIssuesSnapshotDaoImpl dao = new GeneralIssuesSnapshotDaoImpl();
        dao.save(snapshot);
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
        
        DefectsCounter counter = new DefectsCounterImpl(Calendar.WEEK_OF_MONTH, 1, 
                startOfTesting, endOfTesting);
        List<Pair<Integer, Integer>> countedWeeks = counter.spreadDefectsIntoPeriodsOfTime(filteredList);
        List<Pair<Integer, Integer>> countedWeeksWithTotal = counter.countTotalDefectsForPeriodsOfTime(countedWeeks);
        
        
        
        
        System.out.println(model.getTextFormOfTheFunction());
        
        
        ModelOutputData modelData = model.calculateModelData(countedWeeksWithTotal, 0);
        
        //System.out.println(params[0]+" ; "+ params[1]);
        
        
        OutputWriter writer = new HtmlOutputWriter();
        
        //int totalDefects = countedWeeksWithTotal.get(countedWeeksWithTotal.size() - 1).getSecond();
        OutputData prepareOutputData = writer.prepareOutputData(URL, countedWeeksWithTotal);
        //prepareOutputData.setTotalNumberOfDefects(totalDefects);
        
        //----------------------REQUIRED TO SET------------------------------------
        prepareOutputData.setModelParameters(modelData.getFunctionParameters());
        prepareOutputData.setModelData(modelData);
        prepareOutputData.setModelName(model.toString());
        prepareOutputData.setModelFunction(model.getTextFormOfTheFunction());
        prepareOutputData.setStartOfTesting(startOfTesting == null ? filteredList.get(0).getCreatedAt() : startOfTesting);
        prepareOutputData.setEndOfTesting(endOfTesting == null ? new Date() : endOfTesting);
        //-------------------------------------------------------------------------
        
        //-------------------------------Goodness of fit---------------------------
        
        //------------------------------------------------------------------------        
                
                
        writer.writeOutputDataToFile(prepareOutputData, "TestHTML");
        System.exit(0);
    }

}
