package fi.muni.cz.reliability.tool.models;

import java.util.List;
import java.util.Map;
import org.apache.commons.math3.util.Pair;

/**
 * Represents output data of models.
 * 
 * @author Radoslav Micko, 445611@muni.cz
 */
public class ModelOutputData {
    
    private Map<String, Double> functionParameters;
    private List<Pair<Integer, Integer>> estimatedIssues;

    /**
     * Default constructor.
     * @param functionParameters estimated parameters
     * @param estimatedIssues estimated issues
     */
    public ModelOutputData(Map<String, Double> functionParameters, List<Pair<Integer, Integer>> estimatedIssues) {
        this.functionParameters = functionParameters;
        this.estimatedIssues = estimatedIssues;
    }

    public Map<String, Double> getFunctionParameters() {
        return functionParameters;
    }

    public void setFunctionParameters(Map<String, Double> functionParameters) {
        this.functionParameters = functionParameters;
    }

    public List<Pair<Integer, Integer>> getEstimatedIssues() {
        return estimatedIssues;
    }

    public void setEstimatedIssues(List<Pair<Integer, Integer>> estimatedIssues) {
        this.estimatedIssues = estimatedIssues;
    } 
}
