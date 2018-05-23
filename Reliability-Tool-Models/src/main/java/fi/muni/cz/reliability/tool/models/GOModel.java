package fi.muni.cz.reliability.tool.models;

import fi.muni.cz.reliability.tool.models.goodnessoffit.GoodnessOfFit;
import fi.muni.cz.reliability.tool.models.leastsquaresolver.Function;
import fi.muni.cz.reliability.tool.models.leastsquaresolver.GOFunction;
import fi.muni.cz.reliability.tool.models.leastsquaresolver.LeastSquaresOptimization;
import fi.muni.cz.reliability.tool.models.leastsquaresolver.LeastSquaresOptimizationImpl;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.util.Pair;

/**
 * Goel-Okumoto (G-O) model
 * 
 * @author Radoslav Micko, 445611@muni.cz
 */
public class GOModel implements Model {

    private final double[] startParameters;

    /**
     * Initialize start parameters of model.
     * 
     * @param startParameters parameters to set
     */
    public GOModel(double[] startParameters) {
        this.startParameters = startParameters;
    }
    
    @Override
    public ModelOutputData calculateModelData(List<Pair<Integer, Integer>> list, 
            double howMuchToPredict) {
        Function function = new GOFunction();
        LeastSquaresOptimization optimization = new LeastSquaresOptimizationImpl();
        double[] params = optimization.optimizer(startParameters, list, function);
        ModelOutputData outputData = new ModelOutputData(getMapWithParameters(params), 
                getEstimatedIssuesByFunction(list, params[0], params[1], howMuchToPredict));
        return calculateModelGoodnessOfFitIntoOutputData(list, outputData);
    }
    
    private ModelOutputData calculateModelGoodnessOfFitIntoOutputData(List<Pair<Integer, Integer>> list, 
            ModelOutputData outputData) {
        GoodnessOfFit test = new GoodnessOfFit(
                getEstimatedIssuesByFunction(
                        list, 
                        outputData.getFunctionParameters().get("a"), 
                        outputData.getFunctionParameters().get("b"), 
                        0),
                list);
        outputData.setChiSquareValue(test.getChiSquareValue());
        outputData.setChiSquareSignificance(test.getChiSquareSignificance());
        outputData.setChiSquareHypothesisConformation(test.getChiSquareHypothesisConformation(0.05));
        return outputData;
    }
    
    private Map<String, Double> getMapWithParameters(double[] params) {
        Map<String, Double> map = new HashMap<>();
        map.put("a", params[0]);
        map.put("b", params[1]);
        return map;
    }
    
    private List<Pair<Integer, Integer>> getEstimatedIssuesByFunction(List<Pair<Integer, Integer>> list,
            double a, double b, double howMuchToPredict) {
        List<Pair<Integer, Integer>> listOfEstimatedIssues = new ArrayList<>();
        for (Pair<Integer, Integer> pair: list) {
            double estimation = a * (1 - Math.exp(- b * pair.getFirst()));
            Integer roundedEstimation = (int) estimation;
            listOfEstimatedIssues.add(new Pair<>(pair.getFirst(), roundedEstimation));
        }
        int last = list.get(list.size() - 1).getFirst();
        for (int i = last + 1; i < last + howMuchToPredict; i++) {
            double estimation = a * (1 - Math.exp(- b * i));
            Integer roundedEstimation = (int) estimation;
            listOfEstimatedIssues.add(new Pair<>(i, roundedEstimation));
        }
        return listOfEstimatedIssues;
    }

    @Override
    public String getTextFormOfTheFunction() {
        return "f(t) = a * (1 - e" + "<html><sup>-b*t</sup></html>" + ")";
    }
    
    @Override
    public String toString() {
        return "Goel-Okemura model";
    }
}
