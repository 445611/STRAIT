package fi.muni.cz.reliability.tool.models.leastsquaresolver;

import org.apache.commons.math3.analysis.MultivariateMatrixFunction;
import org.apache.commons.math3.analysis.MultivariateVectorFunction;

/**
 * @author Radoslav Micko <445611@muni.cz>
 */
public interface Function {
    
    /**
     * Add new point of data
     * 
     * @param xin x axis
     * @param yin y axis
     */
    void addPoint(double xin, double yin);
    
    /**
     * Compute target data for function to approximate to
     * 
     * @return array of target data
     */
    double[] calculateTarget();
    
    /**
     * Get {@link org.apache.commons.math3.analysis.MultivariateVectorFunction MultivariateVectorFunction} 
     * which is able to compute array of results of implemented function 
     * for input array of variables
     * 
     * @return  {@link org.apache.commons.math3.analysis.MultivariateVectorFunction MultivariateVectorFunction}
     */
    MultivariateVectorFunction getMultivariateVectorFunction();
    
    /**
     * Get {@link org.apache.commons.math3.analysis.MultivariateMatrixFunction MultivariateMatrixFunction}
     * which is able to compute array of arrays of jacobiansof implemented function
     * for input array of variables
     * 
     * @return {@link org.apache.commons.math3.analysis.MultivariateMatrixFunction MultivariateMatrixFunction}
     */
    MultivariateMatrixFunction getMultivariateMatrixFunction();
}
