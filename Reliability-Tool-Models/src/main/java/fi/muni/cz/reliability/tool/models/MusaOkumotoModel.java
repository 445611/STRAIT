package fi.muni.cz.reliability.tool.models;

import fi.muni.cz.reliability.tool.models.leastsquaresolver.Function;
import fi.muni.cz.reliability.tool.models.leastsquaresolver.LeastSquaresOptimization;
import fi.muni.cz.reliability.tool.models.leastsquaresolver.LeastSquaresOptimizationImpl;
import fi.muni.cz.reliability.tool.models.leastsquaresolver.MusaOkumotoFunction;
import fi.muni.cz.reliability.tool.models.testing.GoodnessOfFitTest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class MusaOkumotoModel implements Model {

    private final double[] startParameters;
    private Map<String, Double> modelParameters;
    private final List<Pair<Integer, Integer>> listOfIssues;
    private Map<String, String> goodnessOfFit;
    
    private final GoodnessOfFitTest goodnessOfFitTest;

    private final String firstParameter = "α";
    private final String secondParameter = "β";
    
    /**
     * Initialize model attributes.
     * 
     * @param startParameters       start parameters to set.
     * @param listOfIssues          list of issues.
     * @param goodnessOfFitTest     Goodness of fit test to execute.
     */
    public MusaOkumotoModel(double[] startParameters, List<Pair<Integer, Integer>> listOfIssues, 
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
    public Map<String, String> getGoodnessOfFitData() {
        return goodnessOfFit;
    }

    @Override
    public Map<String, Double> getModelParameters() {
        return modelParameters;
    }

    @Override
    public List<Pair<Integer, Integer>> getIssuesPrediction(double howMuchToPredict) {
        return calculateEstimatedIssues(listOfIssues, modelParameters.get(firstParameter), 
                modelParameters.get(secondParameter), howMuchToPredict);
    }
    
    private void calculateModelParameters() {
        Function function = new MusaOkumotoFunction();
        LeastSquaresOptimization optimization = new LeastSquaresOptimizationImpl();
        setParametersToMap(optimization.optimizer(startParameters, listOfIssues, function));
    }
    
    private void calculateModelGoodnessOfFit() {
        goodnessOfFit = goodnessOfFitTest.executeGoodnessOfFitTest(calculateEstimatedIssues(
                        listOfIssues, 
                        modelParameters.get(firstParameter), 
                        modelParameters.get(secondParameter), 
                        0),
                listOfIssues);
    }
    
    private void setParametersToMap(double[] params) {
        Map<String, Double> map = new HashMap<>();
        map.put(firstParameter, params[0]);
        map.put(secondParameter, params[1]);
        modelParameters = map;
    }
    
    private List<Pair<Integer, Integer>> calculateEstimatedIssues(List<Pair<Integer, Integer>> list,
            double a, double b, double howMuchToPredict) {
        List<Pair<Integer, Integer>> listOfEstimatedIssues = new ArrayList<>();
        for (Pair<Integer, Integer> pair: list) {
            double estimation = a * Math.log(b * pair.getFirst() + 1);
            Integer roundedEstimation = (int) estimation;
            listOfEstimatedIssues.add(new Pair<>(pair.getFirst(), 
                    roundedEstimation == 0 ? 1 : roundedEstimation));
        }
        int last = list.get(list.size() - 1).getFirst();
        for (int i = last + 1; i < last + howMuchToPredict; i++) {
            double estimation = a * Math.log(b * i + 1);
            Integer roundedEstimation = (int) estimation;
            listOfEstimatedIssues.add(new Pair<>(i, roundedEstimation));
        }
        return listOfEstimatedIssues;
    }
    
    @Override
    public String getTextFormOfTheFunction() {
        return "μ(t) = α * ln(β * t + 1)";
    }
    
    @Override
    public String toString() {
        return "Musa-Okumoto model";
    }
}