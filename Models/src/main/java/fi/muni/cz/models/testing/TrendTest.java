package fi.muni.cz.models.testing;

import fi.muni.cz.dataprovider.GeneralIssue;
import java.util.List;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public interface TrendTest {
    
    /**
     * Execute the data trend.
     * 
     * @param listOfGeneralIssues   List of issues.
     */
    void executeTrendTest(List<GeneralIssue> listOfGeneralIssues);
    
    /**
     * Get boolean value if trend exists or not.
     * 
     * @return true if there is trend, false otherwise.
     */
    boolean getResult();
    
    /**
     * Get trend value.
     * 
     * @return double value of trend.
     */
    double getTrendValue();
}
