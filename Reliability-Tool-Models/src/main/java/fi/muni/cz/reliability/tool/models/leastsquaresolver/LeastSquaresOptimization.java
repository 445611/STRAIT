package fi.muni.cz.reliability.tool.models.leastsquaresolver;

import java.util.List;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public interface LeastSquaresOptimization {
    
    /**
     * Optimize array of start parameters to approximate function to fit <code>listOfData</code>
     * Solving least-squares problem.
     * 
     * @param startParameters array of start parameters
     * @param listOfData list of data to fit function for
     * @param function  {@link fi.muni.cz.reliability.tool.models.leastsquaresolver.Function function}
     *                  to optimize for
     * @return  array of optimized parameters
     */
    double[] optimizer(double[] startParameters, List<Pair<Integer, Integer>> listOfData, Function function);
}
