package fi.muni.cz.reliability.tool.utils;

import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import fi.muni.cz.reliability.tool.utils.config.FilteringSetup;
import fi.muni.cz.reliability.tool.utils.config.FilteringSetupImpl;
import java.util.ArrayList;
import java.util.List;


/**
 * Filtering list of <code>GeneralIssue</code> by lables
 * 
 * @author Radoslav Micko <445611@muni.cz>
 */
public class FilterByLable implements IssuesProcessor {

    private List<String> filteringWords;

    /**
     * Initialize <code>filteringWords</code>
     */
    public FilterByLable() {
        this.filteringWords = new ArrayList<>();
    }
        
    /*public FilterByLable(List<String> list) {
        this.filteringWords = list;
    }*/
    
    @Override
    public List<GeneralIssue> process(List<GeneralIssue> list) {
        FilteringSetup setup = new FilteringSetupImpl();
        filteringWords = setup.loadFilteringWordsFromFile();
        if (filteringWords.isEmpty()) {
            return list;
        }
        list = allLablesToLowerCase(list);
        List<GeneralIssue> filteredList = new ArrayList<>();
        for (GeneralIssue issue: list) {
            if (checkLablesForMatchWithFilteringWords(issue.getLabels())) {
                filteredList.add(issue);
            }
        }
        return filteredList;
    }

    /**
     * Check lables for any match with <code>filteringWords</code>
     * @param lables to check
     * @return true if any match, false otherwise
     */
    private boolean checkLablesForMatchWithFilteringWords(List<String> lables) {
        for (String lable: lables) {
            for (String filteringWord: filteringWords) {
                if (lable.contains(filteringWord)) {
                    return true;
                }
            }
        }
        return false;
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
}
