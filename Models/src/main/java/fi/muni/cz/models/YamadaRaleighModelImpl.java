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
public class YamadaRaleighModelImpl extends ModelAbstract {

    private final String firstParameter = "a";
    private final String secondParameter = "r";
    private final String thirdParameter = "α";
    private final String fourthParameter = "β";

    /**
     * Initialize model attributes.
     *
     * @param listOfIssues      list of issues.
     * @param goodnessOfFitTest Goodness of fit test to execute.
     * @param solver            Solver to estimate model parameters.
     */
    public YamadaRaleighModelImpl(
            List<Pair<Integer, Integer>> listOfIssues,
            GoodnessOfFitTest goodnessOfFitTest,
            Solver solver) {
        super(listOfIssues, goodnessOfFitTest, solver);
    }


    @Override
    protected double getFunctionValue(Integer testPeriod) {
        return modelParameters.get(firstParameter)
                * (1 - Math.exp(- modelParameters.get(secondParameter) * modelParameters.get(thirdParameter) *
                (1 - Math.exp(- modelParameters.get(fourthParameter) *  Math.pow(testPeriod, 2) / 2))));
    }

    @Override
    protected void setParametersToMap(double[] params) {
        Map<String, Double> map = new HashMap<>();
        map.put(firstParameter, params[0]);
        map.put(secondParameter, params[1]);
        map.put(thirdParameter, params[2]);
        map.put(fourthParameter, params[3]);
        modelParameters = map;
    }

    @Override
    protected int[] getInitialParametersValue() {
        return new int[]{listOfIssues.get(listOfIssues.size() - 1).getSecond(), 1, 1, 1};
    }

    @Override
    public String getTextFormOfTheFunction() {
        return "μ(t) = a * (1 - <html>e<sup>-r*a*(1 - e<sup>-β*t<sup>2</sup>/2</sup>)</sup></html>)";
    }

    @Override
    public String toString() {
        return "Yamada Raleigh model";
    }
}
