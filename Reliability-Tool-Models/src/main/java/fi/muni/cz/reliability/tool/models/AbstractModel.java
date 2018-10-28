package fi.muni.cz.reliability.tool.models;

import fi.muni.cz.reliability.tool.models.testing.GoodnessOfFitTest;
import fi.muni.cz.reliability.tool.models.leastsquaresolver.Function;
import fi.muni.cz.reliability.tool.models.leastsquaresolver.LeastSquaresOptimization;
import fi.muni.cz.reliability.tool.models.leastsquaresolver.LeastSquaresOptimizationImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public abstract class AbstractModel implements Model {

    protected Map<String, Double> modelParameters;
    protected final List<Pair<Integer, Integer>> listOfIssues;
    protected Map<String, String> goodnessOfFit;
    
    protected final GoodnessOfFitTest goodnessOfFitTest;

    /**
     * Initialize model attributes.
     * 
     * @param listOfIssues          list of issues.
     * @param goodnessOfFitTest     Goodness of fit test to execute.
     */
    public AbstractModel(List<Pair<Integer, Integer>> listOfIssues, 
            GoodnessOfFitTest goodnessOfFitTest) {
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
        setParametersToMap(optimization.optimizer(getInitialParametersValue(), listOfIssues, function));
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
    private List<Pair<Integer, Integer>> calculateEstimatedIssuesOccurance(double howMuchToPredict) {
        List<Pair<Integer, Integer>> listOfEstimatedIssues = new ArrayList<>();
        for (Pair<Integer, Integer> pair: listOfIssues) {
            double estimation = getFunctionValue(pair.getFirst());
            Integer roundedEstimation = (int) estimation;
            listOfEstimatedIssues.add(new Pair<>(pair.getFirst(), 
                    roundedEstimation == 0 ? 1 : roundedEstimation));
        }
        int last = listOfIssues.get(listOfIssues.size() - 1).getFirst();
        for (int i = last + 1; i < last + howMuchToPredict; i++) {
            double estimation = getFunctionValue(i);
            Integer roundedEstimation = (int) estimation;
            listOfEstimatedIssues.add(new Pair<>(i, roundedEstimation));
        }
        return listOfEstimatedIssues;
    }
    
    /**
     * Get initial estimation of model parameters.
     * 
     * @return initial parameters.
     */
    protected abstract double[] getInitialParametersValue();
    
    /**
     * Get function value fo testPeriod. 
     * 
     * @param testPeriod    i-th test period.
     * @return              value of function.
     */
    protected abstract double getFunctionValue(Integer testPeriod);
    
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