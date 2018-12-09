package fi.muni.cz.dataprocessing.issuesprocessing;

import fi.muni.cz.dataprocessing.exception.DataProcessingException;
import fi.muni.cz.dataprovider.GeneralIssue;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Filtering list of GeneralIssue by labels
 * 
 * @author Radoslav Micko, 445611@muni.cz
 */
public class FilterByLabel implements Filter, Serializable {

    private final List<String> filteringWords;

    /**
     * Initialize List of filteringWords.
     * 
     * @param filteringWords words for filter
     */
    public FilterByLabel(List<String> filteringWords) {
        this.filteringWords = filteringWords;
    }
    
    @Override
    public List<GeneralIssue> filter(List<GeneralIssue> list) {
        if (filteringWords.isEmpty()) {
            throw new DataProcessingException("No filtering words");
        }
        return filterByLabels(allLabelsToLowerCase(list));
    }
    
    /**
     * Filter List of GeneralIssue.
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
     * Check labels for any match with List of filteringWords.
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
        IssuesProcessor toLowerCaseProcessor = new LabelsToLowerCaseProcessor();
        return toLowerCaseProcessor.process(list);
    }

    @Override
    public String infoAboutFilter() {
        return "FilterByLabel used, with filtering words: " + filteringWords;
    }

    @Override
    public String toString() {
        return "FilterByLabel" + filteringWords;
    }
}
