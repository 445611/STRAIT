package fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.modeldata;

import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.Tuple;
import java.util.List;

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
     * @return List of Tuple where first is week and second is number of defects
     */
    List<Tuple<Integer, Integer>> spreadDefectsIntoPeriodsOfTime(List<GeneralIssue> listOfIssues);
    
    /**
     * Count number of total defects that appeared before certain period of tim
     * 
     * @param spreadedDefects List to count on
     * @return List of counted tuples
     */
    List<Tuple<Integer, Integer>> countTotalDefectsForPeriodsOfTime(
            List<Tuple<Integer, Integer>> spreadedDefects);
    
}
