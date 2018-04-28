package fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing;

import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import java.util.List;

/**
 * Represents classes that in some way process List of 
 * {@link fi.muni.cz.reliability.tool.dataprovider.GeneralIssue}
 * 
 * @author Radoslav Micko, 445611@muni.cz
 */
public interface IssuesProcessor {
    
    /**
     * Process list of <code>GeneralIssue</code> 
     * 
     * @param list to be processed
     * @return List processed list
     */
    List<GeneralIssue> process(List<GeneralIssue> list);
}
