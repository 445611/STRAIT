package fi.muni.cz.reliability.tool.models.testing;

import java.util.List;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public interface TrendTest {
    
    /**
     * Execute the data trend.
     * 
     * @param listOfIssues          data to calcuate trend for.
     * @return                      double value of trend.
     * @throw TrendModelException   if there is no such trend in data.
     */
    double executeTrendTest(List<Pair<Integer, Integer>> listOfIssues);
}
