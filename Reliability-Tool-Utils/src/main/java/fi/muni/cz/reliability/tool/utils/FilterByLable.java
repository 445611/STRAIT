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
    
    @Override
    public List<GeneralIssue> process(List<GeneralIssue> list) {
        list = allLablesToLowerCase(list);
        
        List<GeneralIssue> filteredList = new ArrayList<>();
        for (GeneralIssue issue: list) {
            for (String lable: issue.getLabels()) {
                if (filteringWords.contains(lable)) {
                    filteredList.add(issue);
                }
            }
        }
        return filteredList;
    }
    
    public void addFilteringWords(String word) {
        filteringWords.add(word);
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
