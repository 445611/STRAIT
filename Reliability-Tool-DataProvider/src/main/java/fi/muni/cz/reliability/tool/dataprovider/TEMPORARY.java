package fi.muni.cz.reliability.tool.dataprovider;

import fi.muni.cz.reliability.tool.core.GeneralIssue;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
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
        DataProvider dataProvider = (GitHubDataProvider) context.getBean("dataProvider");
        
        List<GeneralIssue> list = dataProvider.getIssuesByOwnerRepoName("445611", "PB071");
        System.out.println(list.get(0));

    }
    
}
