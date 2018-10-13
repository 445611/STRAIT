package fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing;

import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import java.util.List;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class LabelsToLowerCaseProcessor implements IssuesProcessor {
    
    @Override
    public List<GeneralIssue> process(List<GeneralIssue> list) {
        for (GeneralIssue issue: list) {
            issue.allLabelsToLowerCase();
        } 
        return list;
    }

    @Override
    public String infoAboutFilter() {
        return "LabelsToLowerCaseProcessor used to lowercase all lables of issues.";
    }
    
    @Override
    public String toString() {
        return "LabelsToLowerCase";
    }
}
