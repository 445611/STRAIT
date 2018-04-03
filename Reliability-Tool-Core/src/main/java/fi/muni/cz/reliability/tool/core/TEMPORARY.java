package fi.muni.cz.reliability.tool.core;

import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import fi.muni.cz.reliability.tool.dataprovider.DataProvider;
import fi.muni.cz.reliability.tool.dataprovider.GitHubDataProvider;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Radoslav Micko <445611@muni.cz>
 */
public class TEMPORARY {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        
        GitHubDataProvider dataProvider = (GitHubDataProvider) context.getBean("dataProvider");
        dataProvider.setOAuthToken("07d185523c583404fb7aabe851d6c715e5352dc9");
        dataProvider.authenticate();
        List<GeneralIssue> list1 = dataProvider.getIssuesByOwnerRepoName("445611", "PB071");
        List<GeneralIssue> list2 = dataProvider.getIssuesByOwnerRepoName("eclipse", "xtext-eclipse");
        System.out.println(list1.get(0).toString() + list1.size());
        System.out.println(list2.get(0).toString() + list2.size());
    }
    
}
