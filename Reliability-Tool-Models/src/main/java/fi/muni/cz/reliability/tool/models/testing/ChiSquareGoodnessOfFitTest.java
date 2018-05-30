package fi.muni.cz.reliability.tool.models.testing;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.stat.inference.ChiSquareTest;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class ChiSquareGoodnessOfFitTest implements GoodnessOfFitTest {
    
    private final ChiSquareTest test;
    private double[] expected;
    private long[] observed;
    private final double alpha = 0.0001;
    
    /**
     * Default contructor to initialize attributes.
     */
    public ChiSquareGoodnessOfFitTest() {
        test = new ChiSquareTest();
    }
    
    @Override
    public Map<String, String> executeGoodnessOfFitTest(List<Pair<Integer, Integer>> expectedIssues, 
            List<Pair<Integer, Integer>> observedIssues) {
        calculateExpectedAndObservedValues(expectedIssues, observedIssues);
        return calculateChiSquareTest();
    }
    
    private void calculateExpectedAndObservedValues(List<Pair<Integer, Integer>> expectedIssues, 
            List<Pair<Integer, Integer>> observedIssues) {
        expected = getArrayOfDoubleFromList(expectedIssues);
        observed = getArrayOfLongFromList(observedIssues);
    }
    
    private Map<String, String> calculateChiSquareTest() {
        Map<String, String> chiSquareMap = new LinkedHashMap<>();
        chiSquareMap.put("Chi-Square = ", String.valueOf(test.chiSquare(expected, observed)));
        chiSquareMap.put("Chi-Square significance level = ",
                String.valueOf(test.chiSquareTest(expected, observed) * 100) + "%");
        chiSquareMap.put("Chi-Square null hypothesis rejection = ", 
                test.chiSquareTest(expected, observed, alpha) ? "REJECT" : "ACCEPT");
        return chiSquareMap;
    }
    
    private double[] getArrayOfDoubleFromList(List<Pair<Integer, Integer>> list) {
        List<Double> arr = new ArrayList<>();
        for (Pair<Integer, Integer> pair: list) {
             arr.add(pair.getSecond().doubleValue());
        }
        return arr.stream().mapToDouble(d -> d).toArray();
    }
    
    private long[] getArrayOfLongFromList(List<Pair<Integer, Integer>> list) {
        List<Double> arr = new ArrayList<>();
        for (Pair<Integer, Integer> pair: list) {
             arr.add(pair.getSecond().doubleValue());
        }
        return arr.stream().mapToLong(d -> d.longValue()).toArray();
    } 
}