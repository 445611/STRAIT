package fi.muni.cz.models.leastsquaresolver;

import java.util.List;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public interface Solver {
    
    /**
     * Optimize array of start parameters to approximate function to fit <code>listOfData</code>
     * Solving least-squares problem.
     * 
     * @param startParameters array of start parameters
     * @param listOfData list of data to fit function for
     * @return  array of optimized parameters
     */
    double[] optimize(int[] startParameters, List<Pair<Integer, Integer>> listOfData);
}