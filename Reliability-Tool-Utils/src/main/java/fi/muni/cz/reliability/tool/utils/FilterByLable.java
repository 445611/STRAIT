package fi.muni.cz.reliability.tool.utils;

import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import java.util.ArrayList;
import java.util.List;

/**
 * Filtering list of <code>GeneralIssue</code> by lables
 * 
 * @author Radoslav Micko <445611@muni.cz>
 */
public class FilterByLable implements IssuesProcessor {

    private List<String> filteringWords = new ArrayList<>();
    
    /**
     * Filters list of issues by <code>filteringWords</code> if lables contains 
     * words or words as substring
     * @param list to be filtered
     * @return list of filtered issues
     */
    public List<GeneralIssue> process(List<GeneralIssue> list) {
        list = allLablesToLowerCase(list);
        
        List<GeneralIssue> filteredList = new ArrayList<>();
        for (GeneralIssue issue: list) {
            for (String lable: issue.getLabels()) {
                for (String filteringWord: filteringWords) {
                    if (lable.contains(filteringWord)) {
                        filteredList.add(issue);
                    }
                }
            }
        }
        return filteredList;
    }
    
    /**
     * Add new word to <code>filteringWords</code>
     * @param word to be added
     */
    public void addFilteringWords(String word) {
        filteringWords.add(word.toLowerCase());
    }
    
    /**
     * Remove word from <code>filteringWords</code>
     * @param word to be removed
     */
    public void removeFilteringWord(String word) {
        filteringWords.remove(word);
    }

    /**
     * Make all lables to lower case
     * @param list to iterate over
     * @return List
     */
    private List<GeneralIssue> allLablesToLowerCase(List<GeneralIssue> list) {
        for (GeneralIssue issue: list) {
            List<String> lowerCaseLables = new ArrayList<>();
            for (String lable: issue.getLabels()) {
                lowerCaseLables.add(lable.toLowerCase());
            }
            issue.setLabels(lowerCaseLables);
        } 
        return list;
    }
    
    public List<String> getFilteringWords() {
        return filteringWords;
    }

    public void setFilteringWords(List<String> filteringWords) {
        this.filteringWords = filteringWords;
    }
}
