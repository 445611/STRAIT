package fi.muni.cz.reliability.tool.models;

import fi.muni.cz.reliability.tool.models.leastsquaresolver.Function;
import fi.muni.cz.reliability.tool.models.leastsquaresolver.GOFunctionImpl;
import fi.muni.cz.reliability.tool.models.testing.GoodnessOfFitTest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class GOModelImpl extends AbstractModel {
    
    private final String firstParameter = "a";
    private final String secondParameter = "b";
    
    /**
     * Initialize model attributes.
     * 
     * @param listOfIssues          list of issues.
     * @param goodnessOfFitTest     Goodness of fit test to execute.
     */
    public GOModelImpl(List<Pair<Integer, Integer>> listOfIssues, GoodnessOfFitTest goodnessOfFitTest) {
        super(listOfIssues, goodnessOfFitTest);
    }
    
    @Override
    protected double[] getInitialParametersValue() {
        return new double[]{listOfIssues.get(listOfIssues.size() - 1).getSecond(), 1};
    }
    
    @Override
    protected double getFunctionValue(Integer testPeriod) {
        return modelParameters.get(firstParameter) *
                (1 - Math.exp(- modelParameters.get(secondParameter) * testPeriod));         
    }
    
    @Override
    protected  Function getModelFunction() {
        return new GOFunctionImpl();
    }
    
    @Override
    protected void setParametersToMap(double[] params) {
        Map<String, Double> map = new HashMap<>();
        map.put(firstParameter, params[0]);
        map.put(secondParameter, params[1]);
        modelParameters = map;
    }
    
    @Override
    public String getTextFormOfTheFunction() {
        return "μ(t) = a * (1 - e<html><sup>-b*t</sup></html>)";
    }
    
    @Override
    public String toString() {
        return "Goel-Okemura model";
    }
}