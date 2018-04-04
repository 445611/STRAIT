package fi.muni.cz.reliability.tool.utils;

import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import java.util.List;

/**
 * @author Radoslav Micko <445611@muni.cz>
 */
public interface IssuesProcessor {
    
    /**
     * Process list of <code>GeneralIssue</code> 
     * @param list to be processed
     * @return List processed list
     */
    List<GeneralIssue> process(List<GeneralIssue> list);
}
