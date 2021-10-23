package fi.muni.cz.dataprocessing.issuesprocessing;

import fi.muni.cz.dataprovider.GeneralIssue;
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
    public String infoAboutProcessor() {
        return "LabelsToLowerCaseProcessor used to lowercase all lables of issues.";
    }
    
    @Override
    public String toString() {
        return "LabelsToLowerCase";
    }
}
