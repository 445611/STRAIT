package fi.muni.cz.dataprocessing.issuesprocessing;

import fi.muni.cz.dataprovider.GeneralIssue;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class FilterDuplications implements Filter {

    @Override
    public List<GeneralIssue> filter(List<GeneralIssue> list) {
        List<GeneralIssue> filteredList = new ArrayList<>();
        for (GeneralIssue issue: allLabelsToLowerCase(list)) {
            if (issue.getLabels().stream()
                    .noneMatch(label -> label.contains("duplicate") ||  label.contains("duplication"))) {
                filteredList.add(issue);
            }
        }
        return filteredList;
    }

    @Override
    public String infoAboutFilter() {
        return "FilterDuplications used to remove duplication issues.";
    }

    @Override
    public String toString() {
        return "FilterDuplications";
    }

    private List<GeneralIssue> allLabelsToLowerCase(List<GeneralIssue> list) {
        IssuesProcessor toLowerCaseProcessor = new LabelsToLowerCaseProcessor();
        return toLowerCaseProcessor.process(list);
    }
}
