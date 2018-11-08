package fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.modeldata;

import fi.muni.cz.reliability.tool.dataprocessing.exception.DataProcessingException;
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

    private final String timeUnit;
    
    /**
     * Initialize timeUnit for time unit between issues.
     * @param timeUnit representation of time unit. (eg. IssueCounter.WEEK).
     */
    public TimeBetweenIssuesCounter(String timeUnit) {
        this.timeUnit = timeUnit;
    }
    
    @Override
    public List<Pair<Integer, Integer>> prepareIssuesDataForModel(List<GeneralIssue> rawIssues) {
        List<Pair<Integer, Integer>> timeBetweenIssuesList = new LinkedList<>();
        Date dateOne = rawIssues.get(0).getCreatedAt();
        int i = 1;
        for (GeneralIssue issue: rawIssues) {
            Date dateTwo = issue.getCreatedAt();
            long diff = dateTwo.getTime() - dateOne.getTime();
            Integer diffInt;
            switch (timeUnit) {
                case SECONDS:
                    diffInt = (int) TimeUnit.MILLISECONDS.toSeconds(diff);
                    break;
                case MINUTES:
                    diffInt = (int) TimeUnit.MILLISECONDS.toMinutes(diff);
                    break;
                case HOURS:
                    diffInt = (int) TimeUnit.MILLISECONDS.toHours(diff);
                    break;
                case DAYS:
                    diffInt = (int) TimeUnit.MILLISECONDS.toDays(diff);
                    break;
                case WEEKS:
                    diffInt = (int) TimeUnit.MILLISECONDS.toDays(diff)/7;
                    break;
                default:
                    throw new DataProcessingException("Wrong time unit: " + timeUnit);
            }
            timeBetweenIssuesList.add(new Pair<>(i, diffInt));
            i++;
            dateOne = dateTwo;
        }
        return timeBetweenIssuesList;
    }
}