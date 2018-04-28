package fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing;

import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



/**
 * Filtering list of {@link fi.muni.cz.reliability.tool.dataprovider.GeneralIssue GeneralIssue}
 * by labels
 * 
 * @author Radoslav Micko, 445611@muni.cz
 */
public class FilterByLabel implements Filter, Serializable {

    private final List<String> filteringWords;

    /**
     * Initialize <code>filteringWords</code>.
     * 
     * @param filteringWords words for filter
     */
    public FilterByLabel(List<String> filteringWords) {
        this.filteringWords = filteringWords;
    }
    
    @Override
    public List<GeneralIssue> filter(List<GeneralIssue> list) {
        if (filteringWords.isEmpty()) {
            return list;
        }
        return filterByLabels(allLabelsToLowerCase(list));
    }
    
    /**
     * Filter List of {@link fi.muni.cz.reliability.tool.dataprovider.GeneralIssue GeneralIssue}
     * 
     * @param list to be filtered
     * @return filtered list.
     */
    private List<GeneralIssue> filterByLabels(List<GeneralIssue> list) {
        List<GeneralIssue> filteredList = new ArrayList<>();
        for (GeneralIssue issue: list) {
            if (checkLabelsForMatchWithFilteringWords(issue.getLabels())) {
                filteredList.add(issue);
            }
        }
        return filteredList;
    }
    
    /**
     * Check labels for any match with <code>filteringWords</code>
     * @param labels to check
     * @return true if any match, false otherwise
     */
    private boolean checkLabelsForMatchWithFilteringWords(List<String> labels) {
        for (String label: labels) {
            for (String filteringWord: filteringWords) {
                if (label.contains(filteringWord)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Make all labels to lower case
     * @param list to iterate over
     * @return List
     */
    private List<GeneralIssue> allLabelsToLowerCase(List<GeneralIssue> list) {
        IssuesProcessor toLowerCaseProcessor = new LabelsToLowerCase();
        return toLowerCaseProcessor.process(list);
    }

    @Override
    public String toString() {
        return "FilterByLabel";
    }
}
