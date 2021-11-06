package fi.muni.cz.models;

import fi.muni.cz.models.testing.GoodnessOfFitTest;
import fi.muni.cz.models.leastsquaresolver.Solver;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public abstract class ModelAbstract implements Model {

    protected Map<String, Double> modelParameters;
    protected List<Pair<Integer, Integer>> listOfIssues;
    protected Map<String, String> goodnessOfFit;
    protected GoodnessOfFitTest goodnessOfFitTest;
    protected Solver solver;
    
    /**
     * Initialize model attributes.
     * 
     * @param listOfIssues          list of issues.
     * @param goodnessOfFitTest     Goodness of fit test to execute.
     * @param solver                Solver to estimate model parameters.
     */
    public ModelAbstract(List<Pair<Integer, Integer>> listOfIssues, 
            GoodnessOfFitTest goodnessOfFitTest, Solver solver) {
        this.listOfIssues = listOfIssues;
        this.goodnessOfFitTest = goodnessOfFitTest;
        this.solver = solver;
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
    
    /**
     * Calculate model parameters.
     */
    protected void calculateModelParameters() {
        setParametersToMap(solver.optimize(getInitialParametersValue(), listOfIssues));
    }
    
    private void calculateModelGoodnessOfFit() {
        goodnessOfFit = goodnessOfFitTest.executeGoodnessOfFitTest(calculateEstimatedIssuesOccurance(0),
                listOfIssues, getModelShortName());
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
     * Get short name of Model.
     *
     * @return  short name of model.
     */
    protected abstract String getModelShortName();
    
    /**
     * Get initial estimation of model parameters.
     * 
     * @return initial parameters.
     */
    protected abstract int[] getInitialParametersValue();
    
    @Override
    public Map<String, String> getGoodnessOfFitData() {
        return goodnessOfFit;
    }

    @Override
    public Map<String, Double> getModelParameters() {
        return modelParameters;
    }
}
