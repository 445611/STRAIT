package fi.muni.cz.dataprocessing.issuesprocessing;

import fi.muni.cz.dataprovider.GeneralIssue;
import java.util.List;

/**
 * Represents classes that in some way filter List of 
 * {@link fi.muni.cz.reliability.tool.dataprovider.GeneralIssue}
 * 
 * @author Radoslav Micko, 445611@muni.cz
 */
public interface Filter {
    
    /**
     * Filter list of GeneralIssue 
     * 
     * @param list to be filtered
     * @return List filtered list
     */
    List<GeneralIssue> filter(List<GeneralIssue> list);
    
    /**
     * Information about filter used with atributes.
     * 
     * @return String info
     */
    String infoAboutFilter();
}
