package fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.modeldata;

import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import java.util.List;
import org.apache.commons.math3.util.Pair;

/**
 * Prepare data for model as needed.
 * 
 * @author Radoslav Micko, 445611@muni.cz
 */
public interface IssuesCounter {
    
    String SECONDS = "Seconds";
    String MINUTES = "Minutes";
    String HOURS = "Hours";
    String DAYS = "Days";
    String WEEKS = "Weeks";
    String MONTHS = "Months";
    String YEARS = "Years";
    
    
    /**
     * Prepare issues data for model.
     * 
     * @param rawIssues List to prepare.
     * @return          List of counted pairs.
     */
    List<Pair<Integer, Integer>> prepareIssuesDataForModel(List<GeneralIssue> rawIssues);
}