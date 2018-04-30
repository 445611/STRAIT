package fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.modeldata;

import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import java.util.List;
import org.apache.commons.math3.util.Pair;

/**
 * Spread issues into weeks
 * 
 * @author Radoslav Micko, 445611@muni.cz
 */
public interface DefectsCounter {
    
    /**
     * Count number of defects for every week
     * 
     * @param listOfIssues to count over
     * @return List of Pair where first is week and second is number of defects
     */
    List<Pair<Integer, Integer>> spreadDefectsIntoPeriodsOfTime(List<GeneralIssue> listOfIssues);
    
    /**
     * Count number of total defects that appeared before certain period of tim
     * 
     * @param spreadedDefects List to count on
     * @return List of counted Pairs
     */
    List<Pair<Integer, Integer>> countTotalDefectsForPeriodsOfTime(
            List<Pair<Integer, Integer>> spreadedDefects);
    
}
