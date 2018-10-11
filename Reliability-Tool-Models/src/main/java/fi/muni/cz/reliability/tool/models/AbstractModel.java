package fi.muni.cz.reliability.tool.models;

import fi.muni.cz.reliability.tool.models.testing.GoodnessOfFitTest;
import fi.muni.cz.reliability.tool.models.leastsquaresolver.Function;
import fi.muni.cz.reliability.tool.models.leastsquaresolver.LeastSquaresOptimization;
import fi.muni.cz.reliability.tool.models.leastsquaresolver.LeastSquaresOptimizationImpl;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public abstract class AbstractModel implements Model {

    protected final double[] startParameters;
    protected Map<String, Double> modelParameters;
    protected final List<Pair<Integer, Integer>> listOfIssues;
    protected Map<String, String> goodnessOfFit;
    
    protected final GoodnessOfFitTest goodnessOfFitTest;

    /**
     * Initialize model attributes.
     * 
     * @param startParameters       start parameters to set.
     * @param listOfIssues          list of issues.
     * @param goodnessOfFitTest     Goodness of fit test to execute.
     */
    public AbstractModel(double[] startParameters, List<Pair<Integer, Integer>> listOfIssues, 
            GoodnessOfFitTest goodnessOfFitTest) {
        this.startParameters = startParameters;
        this.listOfIssues = listOfIssues;
        this.goodnessOfFitTest = goodnessOfFitTest;
    }
    
    @Override
    public void estimateModelData() {
        calculateModelParameters();
        calculateModelGoodnessOfFit();
    }
    
    @Override
    public List<Pair<Integer, Integer>> getIssuesPrediction(double howMuchToPredict) {
        return calculateEstimatedIssuesOccurance(howMuchToPredict);
    }
    
    private void calculateModelParameters() {
        Function function = getModelFunction();
        LeastSquaresOptimization optimization = new LeastSquaresOptimizationImpl();
        setParametersToMap(optimization.optimizer(startParameters, listOfIssues, function));
    }
    
    private void calculateModelGoodnessOfFit() {
        goodnessOfFit = goodnessOfFitTest.executeGoodnessOfFitTest(calculateEstimatedIssuesOccurance(0), listOfIssues);
    }
    
    /**
     * Calculate estimated and predicted issues.
     * 
     * @param howMuchToPredict count of time unites to predict to future.
     * @return Estimated issues occurance.
     */
    protected abstract List<Pair<Integer, Integer>> calculateEstimatedIssuesOccurance(double howMuchToPredict);
    
    /**
     * Set estimated parameters of model with name to set.
     * 
     * @param params to be saved.
     */
    protected abstract void setParametersToMap(double[] params);
    
    /**
     * Get specific model function.
     * 
     * @return model function.
     */
    protected abstract Function getModelFunction();
    
    @Override
    public Map<String, String> getGoodnessOfFitData() {
        return goodnessOfFit;
    }

    @Override
    public Map<String, Double> getModelParameters() {
        return modelParameters;
    }
}