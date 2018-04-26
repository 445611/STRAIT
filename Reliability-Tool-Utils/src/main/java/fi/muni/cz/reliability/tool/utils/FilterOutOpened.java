package fi.muni.cz.reliability.tool.utils;

import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import java.util.ArrayList;
import java.util.List;

/**
 * Filtering out opened issues from list of 
 * {@link fi.muni.cz.reliability.tool.dataprovider.GeneralIssue GeneralIssue} 
 * 
 * @author Radoslav Micko, 445611@muni.cz
 */
public class FilterOutOpened implements Filter {

    @Override
    public List<GeneralIssue> filter(List<GeneralIssue> list) {
        List<GeneralIssue> cloesedIssues = new ArrayList<>();
        for (GeneralIssue issue: list) {
            if (issue.getClosedAt() != null) {
                cloesedIssues.add(issue);
            }
        }
        return cloesedIssues;
    }
    
}
