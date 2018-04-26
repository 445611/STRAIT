package fi.muni.cz.reliability.tool.core;

import fi.muni.cz.reliability.tool.dataprovider.DataProvider;
import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import fi.muni.cz.reliability.tool.dataprovider.GitHubDataProvider;
import fi.muni.cz.reliability.tool.dataprovider.authenticationdata.GitHubAuthenticationDataProviderImpl;
import fi.muni.cz.reliability.tool.models.GOModel;
import fi.muni.cz.reliability.tool.models.Model;
import fi.muni.cz.reliability.tool.utils.modeldata.DefectsCounter;
import fi.muni.cz.reliability.tool.utils.modeldata.DefectsCounterImpl;

import fi.muni.cz.reliability.tool.utils.output.OutputWriter;
import fi.muni.cz.reliability.tool.utils.Tuple;
import fi.muni.cz.reliability.tool.utils.config.FilteringConfigurationImpl;
import fi.muni.cz.reliability.tool.utils.output.OutputData;
import java.util.Calendar;
import java.util.List;
import fi.muni.cz.reliability.tool.utils.config.FilteringConfiguration;
import fi.muni.cz.reliability.tool.utils.output.OutputWriterDefectsForPeriods;
import fi.muni.cz.reliability.tool.utils.output.OutputWriterTotaDefects;

import fi.muni.cz.reliability.tool.dataprovider.authenticationdata.GitHubAuthenticationDataProvider;
import fi.muni.cz.reliability.tool.utils.Filter;

import fi.muni.cz.reliability.tool.utils.FilterByLabel;
import fi.muni.cz.reliability.tool.utils.FilterOutOpened;
import java.util.Date;
import java.util.Map;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class TEMPORARY {
    
    //public static final String URL = "https://github.com/eclipse/sumo/";
    //public static final String URL = "https://github.com/beetbox/beets/issues";
    //public static final String URL = "https://github.com/spring-projects/spring-boot/issues";
    public static final String URL = "https://github.com/google/guava";
    public static final String AUTH_FILE_NAME = "git_hub_authentication_file.properties";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        
        
        
        GitHubAuthenticationDataProvider authProvider = new GitHubAuthenticationDataProviderImpl(AUTH_FILE_NAME);
        DataProvider dataProvider = new GitHubDataProvider(authProvider.getGitHubClientWithCreditials());
        
        
        //dataProvider.setOAuthToken("07d185523c583404fb7aabe851d6c715e5352dc9");
        //dataProvider.authenticate();
        
        //List<GeneralIssue> list1 = dataProvider.getIssuesByOwnerRepoName("445611", "PB071");
        //List<GeneralIssue> list2 = dataProvider.getIssuesByOwnerRepoName("eclipse", "xtext-eclipse");
        //System.out.println(list1.get(0).toString() + list1.size());
        //System.out.println(list2.get(0).toString() + list2.size());
        //List<GeneralIssue> list1 = dataProvider.
        //        getIssuesByUrl("https://github.com/dotnet/roslyn/issues?page=3&q=is%3Aissue+is%3Aopen");
        
        List<GeneralIssue> list1 = dataProvider.getIssuesByUrl(URL);
        
        
        

        FilteringConfiguration setup = new FilteringConfigurationImpl();
        //setup.addWordToConfigFile("sumo");
        Filter issuesFilterByLabel = new FilterByLabel(setup.loadFilteringWordsFromFile());
        List<GeneralIssue> list2 = issuesFilterByLabel.filter(list1);
        //Filter issuesFilterOpened = new FilterOutOpened();
        //list2 = issuesFilterOpened.filter(list2);
        Calendar cal1 = Calendar.getInstance();
        cal1.set(2008, 1, 1);
        Calendar cal2 = Calendar.getInstance();
        cal2.set(2020, 1, 1);
        
        
        DefectsCounter counter = new DefectsCounterImpl(Calendar.HOUR_OF_DAY, 1, null, null);
        List<Tuple<Integer, Integer>> countedWeeks = counter.spreadDefectsIntoPeriodsOfTime(list2);
        List<Tuple<Integer, Integer>> countedWeeksWithTotal = counter.countTotalDefectsForPeriodsOfTime(countedWeeks);
        
        
        
        Model model = new GOModel(new double[]{1,1});
        Map<String, Double> params = model.calculateFunctionParametersOfModel(countedWeeksWithTotal);
        //System.out.println(params[0]+" ; "+ params[1]);
        OutputWriter writer = new OutputWriterTotaDefects();
        OutputWriter writerWithPeriods = new OutputWriterDefectsForPeriods();
        
        int totalDefects = countedWeeksWithTotal.get(countedWeeksWithTotal.size() - 1).getB();
        OutputData prepareOutputData = writer.prepareOutputData(URL, countedWeeksWithTotal);
        prepareOutputData.setTotalNumberOfDefects(totalDefects);
        prepareOutputData.setParameters(params);
        prepareOutputData.setModelName("Goel-Okemura model");
        
        writer.writeOutputDataToFile(prepareOutputData, "TotalDefects");
        
        prepareOutputData.setWeeksAndDefects(countedWeeks);
        writerWithPeriods.writeOutputDataToFile(prepareOutputData, "DefectsInWeeks");
        
    }

}
