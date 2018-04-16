package fi.muni.cz.reliability.tool.core;

import fi.muni.cz.reliability.tool.dataprovider.DataProvider;
import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import fi.muni.cz.reliability.tool.dataprovider.GitHubDataProvider;
import fi.muni.cz.reliability.tool.dataprovider.authenticationdata.AuthenticationDataProvider;
import fi.muni.cz.reliability.tool.dataprovider.authenticationdata.AuthenticationDataProviderGitHub;
import fi.muni.cz.reliability.tool.models.GOModel;
import fi.muni.cz.reliability.tool.models.Model;
import fi.muni.cz.reliability.tool.utils.modeldata.DefectsCounter;
import fi.muni.cz.reliability.tool.utils.modeldata.DefectsCounterImpl;
import fi.muni.cz.reliability.tool.utils.FilterByLable;
import fi.muni.cz.reliability.tool.utils.IssuesProcessor;
import fi.muni.cz.reliability.tool.utils.output.OutputWriter;
import fi.muni.cz.reliability.tool.utils.output.OutputWriterImpl;
import fi.muni.cz.reliability.tool.utils.Tuple;
import fi.muni.cz.reliability.tool.utils.config.FilteringConfigurationImpl;
import fi.muni.cz.reliability.tool.utils.output.OutputData;
import java.util.Calendar;
import java.util.List;
import fi.muni.cz.reliability.tool.utils.config.FilteringConfiguration;

/**
 * @author Radoslav Micko <445611@muni.cz>
 */
public class TEMPORARY {
    
    //public static final String URL = "https://github.com/eclipse/sumo/";
    //public static final String URL = "https://github.com/beetbox/beets/issues";
    public static final String URL = "https://github.com/spring-projects/spring-boot/issues";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        
        AuthenticationDataProvider authProvider = new AuthenticationDataProviderGitHub();
        List<String> authData = authProvider.getAuthenticationDataFromFile();
        DataProvider dataProvider = new GitHubDataProvider(authData.get(0), authData.get(1), authData.get(2));
        
        
        //dataProvider.setOAuthToken("07d185523c583404fb7aabe851d6c715e5352dc9");
        //dataProvider.authenticate();
        
        //List<GeneralIssue> list1 = dataProvider.getIssuesByOwnerRepoName("445611", "PB071");
        //List<GeneralIssue> list2 = dataProvider.getIssuesByOwnerRepoName("eclipse", "xtext-eclipse");
        //System.out.println(list1.get(0).toString() + list1.size());
        //System.out.println(list2.get(0).toString() + list2.size());
        //List<GeneralIssue> list1 = dataProvider.
        //        getIssuesByUrl("https://github.com/dotnet/roslyn/issues?page=3&q=is%3Aissue+is%3Aopen");
        
        List<GeneralIssue> list1 = dataProvider.getIssuesByUrl(URL);
        
        
        IssuesProcessor issuesFilter = new FilterByLable();

        FilteringConfiguration setup = new FilteringConfigurationImpl();
        setup.addWordToConfigFile("sumo");
        List<GeneralIssue> list2 = issuesFilter.process(list1);
        
        Calendar cal1 = Calendar.getInstance();
        cal1.set(2008, 1, 1);
        Calendar cal2 = Calendar.getInstance();
        cal2.set(2020, 1, 1);
        
        
        DefectsCounter counter = new DefectsCounterImpl();
        List<Tuple<Integer, Integer>> countedWeeks = counter.spreadDefectsIntoPeriodsOfTime(list2);
        List<Tuple<Integer, Integer>> countedWeeksWithTotal = counter.countTotalDefectsForPeriodsOfTime(countedWeeks);
        
        
        
        Model model = new GOModel();
        double[] params = model.calculateFunctionParametersOfModel(countedWeeksWithTotal);
        System.out.println(params[0]+" ; "+ params[1]);
        OutputWriter writer = new OutputWriterImpl();
        int totalDefects = countedWeeksWithTotal.get(countedWeeksWithTotal.size() - 1).getB();
        writer.writeOutputDataToFile(writer.prepareOutputData(URL, countedWeeks), "DefectsInWeeks");
        OutputData prepareOutputData = writer.prepareOutputData(URL, countedWeeksWithTotal);
        prepareOutputData.setTotalNumberOfDefects(totalDefects);
        writer.writeOutputDataToFile(prepareOutputData, "TotalDefects");
    }
    
}
