package fi.muni.cz.reliability.tool.models.goodnessoffit;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.stat.inference.ChiSquareTest;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class GoodnessOfFit {
    
    private final ChiSquareTest test;
    private final double[] expected;
    private final long[] observed;
    
    /**
     * Default contructor to initialize <code>ChiSquareTest</code>.
     * 
     * @param expectedIssues list of expected occurance of issues.
     * @param observedIssues list of observed occurance of issues.
     */
    public GoodnessOfFit(List<Pair<Integer, Integer>> expectedIssues, 
            List<Pair<Integer, Integer>> observedIssues) {
        test = new ChiSquareTest();
        expected = getArrayOfDoubleFromList(expectedIssues);
        observed = getArrayOfLongFromList(observedIssues);
    }
    
    public double getChiSquareValue() {
        /*double pValue = test.chiSquare(expected, observed);
        System.out.println("Goodness of fit 'chiSquare' = " + pValue);
        System.out.println("Conform for 0.50 = " + 
                test.chiSquareTest(expected, observed, 0.50));
        System.out.println("Conform for 0.20 = " + 
                test.chiSquareTest(expected, observed, 0.20));
        System.out.println("Conform for 0.1 = " + 
                test.chiSquareTest(expected, observed, 0.1));
        System.out.println("Conform for 0.05 = " + 
                test.chiSquareTest(expected, observed, 0.05));
        System.out.println("Conform for 0.002 = " + 
                test.chiSquareTest(expected, observed, 0.002));
        System.out.println("Conform for 0.00001 = " + 
                test.chiSquareTest(expected, observed, 0.00001));
        System.out.println("Conform for 0.00000001 = " + 
                test.chiSquareTest(expected, observed, 0.00000001));
        System.out.println("Conform for 0.000000000001 = " + 
                test.chiSquareTest(expected, observed, 0.000000000001));
        System.out.println("Conform for 0.00000000000000000000001 = " + 
                test.chiSquareTest(expected, observed, 0.00000000000000000000001));*/
        return test.chiSquare(expected, observed);
    }
    
    public double getChiSquareSignificance() {
        return test.chiSquareTest(expected, observed);
    }
    
    public boolean getChiSquareHypothesisConformation(double alpha) {
        return test.chiSquareTest(expected, observed, alpha);
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
