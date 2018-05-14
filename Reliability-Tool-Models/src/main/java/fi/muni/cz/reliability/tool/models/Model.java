package fi.muni.cz.reliability.tool.models;

import java.util.List;
import org.apache.commons.math3.util.Pair;

/**
 * Represents interface for software reliability models
 * 
 * @author Radoslav Micko, 445611@muni.cz
 */
public interface Model {
    
    /**
     * Calculate all parameters need for models functions.
     * 
     * @param list of Pairs for models
     * @return double[] parameters of function
     */
    ModelOutputData calculateFunctionParametersOfModel(List<Pair<Integer, Integer>> list);
    
    /**
     * Get text form of the model function.
     * 
     * @return String form
     */
    String getTextFormOfTheFunction();
}
