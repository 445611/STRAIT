package fi.muni.cz.dataprocessing.issuesprocessing;

import fi.muni.cz.dataprovider.GeneralIssue;
import java.util.List;

/**
 * Represents classes that in some way process List of GeneralIssue
 * 
 * @author Radoslav Micko, 445611@muni.cz
 */
public interface IssuesProcessor {
    
    /**
     * Process list of GeneralIssue 
     * 
     * @param list to be processed
     * @return List processed list
     */
    List<GeneralIssue> process(List<GeneralIssue> list);
    
    /**
     * Information about filter used with atributes.
     * 
     * @return String info
     */
    String infoAboutFilter();
}
