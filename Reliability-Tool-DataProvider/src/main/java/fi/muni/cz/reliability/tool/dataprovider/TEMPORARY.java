package fi.muni.cz.reliability.tool.dataprovider;

/**
 * @author Radoslav Micko <445611@muni.cz>
 */
public class TEMPORARY {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        DataProvider provider = new GitHubDataProvider();
        
        provider.getIssues("445611", "PB071");
        for (int i = 0; i < 50; i++) {    
            System.out.println(i);
            provider.getIssues("445611", "PB071");
            provider.getIssues("445611", "Reliability-Tool");
        }
        
        
    }
    
}
