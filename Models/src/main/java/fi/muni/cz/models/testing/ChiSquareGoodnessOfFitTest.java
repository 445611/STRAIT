package fi.muni.cz.models.testing;

import org.apache.commons.math3.util.Pair;
import org.rosuda.JRI.Rengine;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class ChiSquareGoodnessOfFitTest implements GoodnessOfFitTest {

    private static final double ALPHA = 0.05;
    private Rengine rEngine;

    /**
     *  Default constructor to initialize attributes.
     *
     * @param rEngine engine for R chisq.test
     */
    public ChiSquareGoodnessOfFitTest(Rengine rEngine) {
        this.rEngine = rEngine;
    }
    
    @Override
    public Map<String, String> executeGoodnessOfFitTest(List<Pair<Integer, Integer>> expectedIssues, 
            List<Pair<Integer, Integer>> observedIssues) {
        return calculateChiSquareTest(getPreparedListWithCommas(expectedIssues),
                getPreparedListWithCommas(observedIssues));
    }
    
    private Map<String, String> calculateChiSquareTest(String expected, String observe) {
        Map<String, String> chiSquareMap = new LinkedHashMap<>();

        rEngine.eval(String.format("expected = c(%s)", expected));
        rEngine.eval(String.format("observed = c(%s)", observe));
        rEngine.eval("test <- chisq.test(cbind(expected, observed))");

        double chisqTestStatistic = rEngine.eval("test$statistic").asDoubleArray()[0];
        double chisqTestPValue = rEngine.eval("test$p.value").asDoubleArray()[0];

        chiSquareMap.put("Chi-Square = ", String.valueOf(chisqTestStatistic));
        chiSquareMap.put("Chi-Square significance level = ", String.valueOf(chisqTestPValue));
        chiSquareMap.put("Chi-Square null hypothesis rejection = ",
                chisqTestPValue < ALPHA ? "REJECT" : "ACCEPT");
        return chiSquareMap;
    }

    private String getPreparedListWithCommas(List<Pair<Integer, Integer>> list) {
        return list.stream().map(value -> value.getSecond().toString()).collect(Collectors.joining(","));
    }
}
