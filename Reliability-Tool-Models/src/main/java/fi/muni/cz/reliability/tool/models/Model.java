package fi.muni.cz.reliability.tool.models;

import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.Tuple;
import java.util.List;
import java.util.Map;

/**
 * Represents interface for software reliability models
 * 
 * @author Radoslav Micko, 445611@muni.cz
 */
public interface Model {
    
    /**
     * Calculate all parameters need for models functions.
     * 
     * @param list of Tuples for models
     * @return double[] parameters of function
     */
    Map<String, Double> calculateFunctionParametersOfModel(List<Tuple<Integer, Integer>> list);
}
