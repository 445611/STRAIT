package fi.muni.cz.reliability.tool.dataprovider;

import fi.muni.cz.reliability.tool.core.GeneralIssue;
import java.util.List;

/**
 * @author Radoslav Micko <445611@muni.cz>
 */
public class TEMPORARY {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        DataProvider provider = new GitHubDataProvider();
        
        List<GeneralIssue> list = provider.getIssuesByOwnerRepoName("445611", "PB071");
        System.out.println(list.get(0));

    }
    
}
