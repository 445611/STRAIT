package fi.muni.cz.models;

import fi.muni.cz.models.leastsquaresolver.Solver;
import fi.muni.cz.models.testing.GoodnessOfFitTest;
import org.apache.commons.math3.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class LogLogisticModelImpl extends ModelAbstract {

    private final String firstParameter = "a";
    private final String secondParameter = "λ";
    private final String thirdParameter = "K";

    /**
     * Initialize model attributes.
     *
     * @param listOfIssues          list of issues.
     * @param goodnessOfFitTest     Goodness of fit test to execute.
     * @param solver                Solver to estimate model parameters.
     */
    public LogLogisticModelImpl(
            List<Pair<Integer, Integer>> listOfIssues,
            GoodnessOfFitTest goodnessOfFitTest,
            Solver solver) {
        super(listOfIssues, goodnessOfFitTest, solver);
    }

    @Override
    protected double getFunctionValue(Integer testPeriod) {
        return modelParameters.get(firstParameter)
                * Math.pow((modelParameters.get(secondParameter) * testPeriod), modelParameters.get(thirdParameter))
                / (1 + Math.pow(
                        (modelParameters.get(secondParameter) * testPeriod), modelParameters.get(thirdParameter)));
    }

    @Override
    protected void setParametersToMap(double[] params) {
        Map<String, Double> map = new HashMap<>();
        map.put(firstParameter, params[0]);
        map.put(secondParameter, params[1]);
        map.put(thirdParameter, params[2]);
        modelParameters = map;
    }

    @Override
    protected int[] getInitialParametersValue() {
        return new int[]{listOfIssues.get(listOfIssues.size() - 1).getSecond(), 1, 1};
    }

    @Override
    public String getTextFormOfTheFunction() {
        return "μ(t) = a * <html><sup>(λ*t)<sup>K</sup></sup>&frasl;<sub>1 + (λ*t)<sup>K</sup></sub></html>";
    }

    @Override
    public String toString() {
        return "Log-Logistic model";
    }
}
