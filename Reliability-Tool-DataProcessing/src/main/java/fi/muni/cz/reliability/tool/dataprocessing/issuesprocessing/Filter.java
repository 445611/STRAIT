package fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing;

import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import java.util.List;

/**
 * Represents classes that in some way filter List of 
 * {@link fi.muni.cz.reliability.tool.dataprovider.GeneralIssue}
 * 
 * @author Radoslav Micko, 445611@muni.cz
 */
public interface Filter {
    
    /**
     * Filter list of <code>GeneralIssue</code> 
     * 
     * @param list to be filtered
     * @return {@link java.util.List List} filtered list
     */
    List<GeneralIssue> filter(List<GeneralIssue> list);
    
    /**
     * Information about filter used with atributes.
     * 
     * @return String info
     */
    String infoAboutFilter();
}
