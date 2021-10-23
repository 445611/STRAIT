package fi.muni.cz.dataprocessing.issuesprocessing;

import fi.muni.cz.dataprovider.GeneralIssue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Filtering list of GeneralIssue that are defects
 *
 * @author Radoslav Micko, 445611@muni.cz
 */
public class FilterDefects implements Filter {

    private static final List<String> FILTERING_WORDS = Arrays.asList("bug","error","fail","fault","defect");
    private static final FilterByLabel FILTER_BY_LABELS = new FilterByLabel(FILTERING_WORDS);

    @Override
    public List<GeneralIssue> filter(List<GeneralIssue> list) {
        Set<GeneralIssue> filteredList = new HashSet<>(FILTER_BY_LABELS.filter(list));
        for (GeneralIssue issue: list) {
            if (issue.getBody() == null) {
                continue;
            }
            if (FILTERING_WORDS.stream().anyMatch(filteringWord -> issue.getBody().contains(filteringWord))) {
                filteredList.add(issue);
            }
        }
        return filteredList.stream()
                .sorted((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt())).collect(Collectors.toList());
    }

    @Override
    public String infoAboutFilter() {
        return "FilterDefects used to get only defects.";
    }

    @Override
    public String toString() {
        return "FilterDefects";
    }
}
