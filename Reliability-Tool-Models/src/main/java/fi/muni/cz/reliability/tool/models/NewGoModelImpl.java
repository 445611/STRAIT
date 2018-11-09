package fi.muni.cz.reliability.tool.models;

import fi.muni.cz.reliability.tool.models.leastsquaresolver.GOLeastSquaresSolver;
import fi.muni.cz.reliability.tool.models.leastsquaresolver.LeastSquaresSolver;
import fi.muni.cz.reliability.tool.models.testing.GoodnessOfFitTest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class NewGoModelImpl extends NewAbstractModel {
    
    private final String firstParameter = "a";
    private final String secondParameter = "b";
    
    /**
     * Initialize model attributes.
     * 
     * @param listOfIssues          list of issues.
     * @param goodnessOfFitTest     Goodness of fit test to execute.
     */
    public NewGoModelImpl(List<Pair<Integer, Integer>> listOfIssues, 
            GoodnessOfFitTest goodnessOfFitTest) {
        super(listOfIssues, goodnessOfFitTest);
    }

    @Override
    protected double getFunctionValue(Integer testPeriod) {
        return modelParameters.get(firstParameter) *
                (1 - Math.exp(- modelParameters.get(secondParameter) * testPeriod));
    }

    @Override
    protected void setParametersToMap(double[] params) {
        Map<String, Double> map = new HashMap<>();
        map.put(firstParameter, params[0]);
        map.put(secondParameter, params[1]);
        modelParameters = map;
    }

    @Override
    protected double[] getInitialParametersValue() {
        return new double[]{listOfIssues.get(listOfIssues.size() - 1).getSecond(), 1};
    }

    @Override
    public String getTextFormOfTheFunction() {
        return "μ(t) = a * (1 - e<html><sup>-b*t</sup></html>)";
    }
    
    @Override
    public String toString() {
        return "Goel-Okemura model";
    }

    @Override
    protected void calculateModelParameters() {
        LeastSquaresSolver solver = new GOLeastSquaresSolver();
        setParametersToMap(solver.optimize(getInitialParametersValue(), listOfIssues));
    }
}