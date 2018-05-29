package fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.modeldata;

import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class TimeBetweenIssuesCounter implements IssuesCounter {

    @Override
    public List<Pair<Integer, Integer>> prepareIssuesDataForModel(List<GeneralIssue> rawIssues) {
        List<Pair<Integer, Integer>> timeBetweenIssuesList = new LinkedList<>();
        Date dateOne = rawIssues.get(0).getCreatedAt();
        int i = 1;
        for (GeneralIssue issue: rawIssues) {
            Date dateTwo = issue.getCreatedAt();
            long diff = dateTwo.getTime() - dateOne.getTime();
            Integer diffInt = (int) TimeUnit.MILLISECONDS.toSeconds(diff);
            timeBetweenIssuesList.add(new Pair<>(i, diffInt));
            i++;
            dateOne = dateTwo;
        }
        return timeBetweenIssuesList;
    }
}