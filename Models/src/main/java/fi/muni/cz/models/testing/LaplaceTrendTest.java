package fi.muni.cz.models.testing;

import fi.muni.cz.dataprocessing.issuesprocessing.modeldata.IssuesCounter;
import fi.muni.cz.dataprocessing.issuesprocessing.modeldata.TimeBetweenIssuesCounter;
import fi.muni.cz.dataprovider.GeneralIssue;
import java.util.List;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class LaplaceTrendTest implements TrendTest {
    
    private double trend;
    private final double criticalValue = -1.645;
    private final String timeUnit;

    /**
     * Initialize timeUnit for IssuesCounter as time between issue unit.
     * @param timeUnit time between issue unit. (eg. IssueCounter.WEEK).
     */
    public LaplaceTrendTest(String timeUnit) {
        this.timeUnit = timeUnit;
    }
    
    @Override
    public void executeTrendTest(List<GeneralIssue> listOfGeneralIssues) {
        IssuesCounter timeBetweenCounter = new TimeBetweenIssuesCounter(timeUnit);
        List<Pair<Integer, Integer>> timeBetweenIssues = timeBetweenCounter
                .prepareIssuesDataForModel(listOfGeneralIssues);
        double totalNumberOfIssues = timeBetweenIssues.size();
        double sumOfDuration = timeBetweenIssues.stream().mapToDouble(a -> a.getSecond().doubleValue()).sum();
        double numerator = (sumOfDuration / totalNumberOfIssues - timeBetweenIssues.get(timeBetweenIssues.size() - 1)
                .getSecond() / 2) ;
        double denumerator = timeBetweenIssues.get(timeBetweenIssues.size() - 1).getSecond() 
                * Math.sqrt(1 / (12 * totalNumberOfIssues));
        trend = numerator / denumerator;
    }

    @Override
    public boolean getResult() {
        return trend < criticalValue;
    }

    @Override
    public double getTrendValue() {
        return trend;
    }
}