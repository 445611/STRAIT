package fi.muni.cz.reliability.tool.utils;

import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import java.util.List;

/**
 * Spread issues into weeks
 * 
 * @author Radoslav Micko <445611@muni.cz>
 */
public interface DefectsCounter {
    
    /**
     * Count number of defects for every week
     * @param listOfIssues to count over
     * @return List of Tuple where first is week and second is number of defects
     */
    List<Tuple<Integer, Integer>> countDefectsForWeeks(List<GeneralIssue> listOfIssues);
}
