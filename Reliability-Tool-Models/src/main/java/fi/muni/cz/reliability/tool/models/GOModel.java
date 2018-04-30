package fi.muni.cz.reliability.tool.models;

import fi.muni.cz.reliability.tool.models.leastsquaresolver.Function;
import fi.muni.cz.reliability.tool.models.leastsquaresolver.GOFunction;
import fi.muni.cz.reliability.tool.models.leastsquaresolver.LeastSquaresOptimization;
import fi.muni.cz.reliability.tool.models.leastsquaresolver.LeastSquaresOptimizationImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.util.Pair;

/**
 * Goel-Okumoto (G-O) model
 * 
 * @author Radoslav Micko, 445611@muni.cz
 */
public class GOModel implements Model {

    private final double[] startParameters;

    /**
     * Initialize start parameters of model.
     * 
     * @param startParameters parameters to set
     */
    public GOModel(double[] startParameters) {
        this.startParameters = startParameters;
    }
    
    @Override
    public Map<String, Double> calculateFunctionParametersOfModel(List<Pair<Integer, Integer>> list) {
        Function function = new GOFunction();
        LeastSquaresOptimization optimization = new LeastSquaresOptimizationImpl();
        return getMapWithParameters(optimization.optimizer(startParameters, list, function));
    }
    
    private Map<String, Double> getMapWithParameters(double[] params) {
        Map<String, Double> map = new HashMap<>();
        map.put("a", params[0]);
        map.put("b", params[1]);
        return map;
    }
}
