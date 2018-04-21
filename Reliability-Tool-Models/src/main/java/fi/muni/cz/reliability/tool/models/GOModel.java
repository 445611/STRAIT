package fi.muni.cz.reliability.tool.models;

import fi.muni.cz.reliability.tool.models.leastsquaresolver.Function;
import fi.muni.cz.reliability.tool.models.leastsquaresolver.GOFunction;
import fi.muni.cz.reliability.tool.models.leastsquaresolver.LeastSquaresOptimization;
import fi.muni.cz.reliability.tool.models.leastsquaresolver.LeastSquaresOptimizationImpl;
import fi.muni.cz.reliability.tool.utils.Tuple;
import java.util.List;

/**
 * Goel-Okumoto (G-O) model
 * 
 * @author Radoslav Micko <445611@muni.cz>
 */
public class GOModel implements Model {

    private final double[] startParameters;

    /**
     * Initialize start parameters of model.
     * @param startParameters parameters to set
     */
    public GOModel(double[] startParameters) {
        this.startParameters = startParameters;
    }
    
    @Override
    public double[] calculateFunctionParametersOfModel(List<Tuple<Integer, Integer>> list) {
        
        Function function = new GOFunction();
        
        LeastSquaresOptimization optimization = new LeastSquaresOptimizationImpl();
        return optimization.optimizer(startParameters, list, function);
    }
}
