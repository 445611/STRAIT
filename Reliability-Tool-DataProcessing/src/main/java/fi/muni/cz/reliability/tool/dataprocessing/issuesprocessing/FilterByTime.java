package fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing;

import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class FilterByTime implements Filter {

    private final Date startOfTesting;
    private final Date endOfTesting;
    
    /**
     * Initialize.
     * 
     * @param startOfTesting start of testing
     * @param endOfTesting end of testing
     */
    public FilterByTime(Date startOfTesting, Date endOfTesting) {
        this.startOfTesting = startOfTesting;
        this.endOfTesting = endOfTesting;
    }
    
    @Override
    public List<GeneralIssue> filter(List<GeneralIssue> list) {
        List<GeneralIssue> filteredList = new ArrayList<>();
        for (GeneralIssue issue: list) {
            if (issue.getCreatedAt().after(startOfTesting) && issue.getCreatedAt().before(endOfTesting)) {
                filteredList.add(issue);
            }
        }
        return filteredList;
    }

    @Override
    public String infoAboutFilter() {
        return "FilterByTime start of testing = " + startOfTesting 
                + ", end of testing = " + endOfTesting;
    }
    
    @Override
    public String toString() {
        return "FilterByTime";
    }
}