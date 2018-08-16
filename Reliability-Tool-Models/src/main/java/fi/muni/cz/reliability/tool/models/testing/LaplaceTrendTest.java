package fi.muni.cz.reliability.tool.models.testing;

import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.modeldata.IssuesCounter;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.modeldata.TimeBetweenIssuesCounter;
import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import java.util.List;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class LaplaceTrendTest implements TrendTest {
    
    private double trend;
    private final double criticalValue = -1.645;
    
    @Override
    public void executeTrendTest(List<GeneralIssue> listOfGeneralIssues) {
        
        IssuesCounter timeBetweenCounter = new TimeBetweenIssuesCounter();
        List<Pair<Integer, Integer>> timeBetweenIssues = timeBetweenCounter
                .prepareIssuesDataForModel(listOfGeneralIssues);
        double totalNumberOfIssues = timeBetweenIssues.size();
        double sumOfDuration = timeBetweenIssues.stream().mapToDouble(a -> a.getSecond().doubleValue()).sum();
        //double totalOperatingTime = listOfIssues.get(listOfIssues.size() - 1).getFirst();
        
        double numerator = (sumOfDuration / totalNumberOfIssues - timeBetweenIssues.get(timeBetweenIssues.size() - 1)
                .getSecond() / 2) ;
        double denumerator = timeBetweenIssues.get(timeBetweenIssues.size() - 1).getSecond() 
                * Math.sqrt(1 / (12 * totalNumberOfIssues));
        trend = numerator / denumerator;
        
        //EVALUATION
        //THROW TrendModelException when no trend or detoriorating trend
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