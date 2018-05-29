package fi.muni.cz.reliability.tool.models.goodnessoffit;

import java.util.List;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class LaplaceTrendTest implements TrendTest {
    
    @Override
    public double executeTrendTest(List<Pair<Integer, Integer>> listOfIssues) {
        double totalNumberOfIssues = listOfIssues.size();
        double sumOfDuration = listOfIssues.stream().mapToDouble(a -> a.getSecond().doubleValue()).sum();
        //double totalOperatingTime = listOfIssues.get(listOfIssues.size() - 1).getFirst();
        
        double numerator = (sumOfDuration / totalNumberOfIssues - listOfIssues.get(listOfIssues.size() - 1).getSecond() / 2) ;
        double denumerator = listOfIssues.get(listOfIssues.size() - 1).getSecond() * Math.sqrt(1 / (12 * totalNumberOfIssues));
        double value = numerator / denumerator;
        return value;
    }
}
