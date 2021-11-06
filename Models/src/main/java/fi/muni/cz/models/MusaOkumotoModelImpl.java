package fi.muni.cz.models;

import fi.muni.cz.models.leastsquaresolver.Solver;
import fi.muni.cz.models.testing.GoodnessOfFitTest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class MusaOkumotoModelImpl extends ModelAbstract {

    private final String firstParameter = "α";
    private final String secondParameter = "β";
    
    /**
     * Initialize model attributes.
     * 
     * @param listOfIssues          list of issues.
     * @param goodnessOfFitTest     Goodness of fit test to execute.
     * @param solver                Solver to estimate model parameters.
     */
    public MusaOkumotoModelImpl(List<Pair<Integer, Integer>> listOfIssues, GoodnessOfFitTest goodnessOfFitTest,
            Solver solver) {
        super(listOfIssues, goodnessOfFitTest, solver);
    }
    
    @Override
    protected int[] getInitialParametersValue() {
        return new int[]{listOfIssues.get(listOfIssues.size() - 1).getSecond(), 1};
    }
    
    @Override
    protected double getFunctionValue(Integer testPeriod) {
        return modelParameters.get(firstParameter) *
                Math.log(modelParameters.get(secondParameter) * testPeriod + 1);        
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
        return "μ(t) = α * ln(β * t + 1)";
    }
    
    @Override
    public String toString() {
        return "Musa-Okumoto model";
    }

    @Override
    protected String getModelShortName() {
        return "MO";
    }
}
