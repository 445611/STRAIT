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
     * @param howMuchToPredict how many time periods of issues occurance predict to future
     * @return double[] parameters of function
     */
    ModelOutputData calculateModelData(List<Pair<Integer, Integer>> list, double howMuchToPredict);
    
    /**
     * Get text form of the model function.
     * 
     * @return String form
     */
    String getTextFormOfTheFunction();
}
