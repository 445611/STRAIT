package fi.muni.cz.reliability.tool.models;

import fi.muni.cz.reliability.tool.utils.Tuple;
import java.util.List;

/**
 * Represents interface for software reliability models
 * 
 * @author Radoslav Micko <445611@muni.cz>
 */
public interface Model {
    
    /**
     * Calculate all parameters need for models functions.
     * 
     * @param list of Tuples for models
     * @return double[] parameters of function
     */
    double[] calculateFunctionParametersOfModel(List<Tuple<Integer, Integer>> list);
}
