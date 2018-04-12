package fi.muni.cz.reliability.tool.core;

import fi.muni.cz.reliability.tool.dataprovider.DataProvider;
import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import fi.muni.cz.reliability.tool.dataprovider.GitHubDataProvider;
import fi.muni.cz.reliability.tool.dataprovider.authenticationdata.AuthenticationDataProvider;
import fi.muni.cz.reliability.tool.dataprovider.authenticationdata.AuthenticationDataProviderGitHub;
import fi.muni.cz.reliability.tool.utils.modeldata.DefectsCounter;
import fi.muni.cz.reliability.tool.utils.modeldata.DefectsCounterImpl;
import fi.muni.cz.reliability.tool.utils.FilterByLable;
import fi.muni.cz.reliability.tool.utils.IssuesProcessor;
import fi.muni.cz.reliability.tool.utils.output.OutputWriter;
import fi.muni.cz.reliability.tool.utils.output.OutputWriterImpl;
import fi.muni.cz.reliability.tool.utils.Tuple;
import fi.muni.cz.reliability.tool.utils.config.FilteringSetup;
import fi.muni.cz.reliability.tool.utils.config.FilteringSetupImpl;
import java.util.List;

/**
 * @author Radoslav Micko <445611@muni.cz>
 */
public class TEMPORARY {
    
    public static final String URL = "https://github.com/eclipse/sumo/";
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

        FilteringSetup setup = new FilteringSetupImpl();
        setup.addWordToConfigFile("TEST");
        List<GeneralIssue> list2 = issuesFilter.process(list1);
        DefectsCounter counter = new DefectsCounterImpl();
        List<Tuple<Integer, Integer>> countedWeeks = counter.countDefectsForWeeks(list2);
        System.out.println(countedWeeks);
        System.out.println(list2.get(0).toString() + list2.size());
        
        OutputWriter writer = new OutputWriterImpl();
        writer.writeOutputDataToFile(writer.prepareOutputData(URL, countedWeeks));
    }
    
}
