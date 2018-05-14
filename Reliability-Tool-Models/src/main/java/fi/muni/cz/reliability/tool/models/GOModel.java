package fi.muni.cz.reliability.tool.models;

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
    public ModelOutputData calculateFunctionParametersOfModel(List<Pair<Integer, Integer>> list) {
        Function function = new GOFunction();
        LeastSquaresOptimization optimization = new LeastSquaresOptimizationImpl();
        double[] params = optimization.optimizer(startParameters, list, function);
        return new ModelOutputData(getMapWithParameters(params), 
                getEstimatedIssuesByFunction(list, params[0], params[1]));
    }
    
    private Map<String, Double> getMapWithParameters(double[] params) {
        Map<String, Double> map = new HashMap<>();
        map.put("a", params[0]);
        map.put("b", params[1]);
        return map;
    }
    
    private List<Pair<Integer, Integer>> getEstimatedIssuesByFunction(List<Pair<Integer, Integer>> list,
            double a, double b) {
        List<Pair<Integer, Integer>> listOfEstimatedIssues = new ArrayList<>();
        for (Pair<Integer, Integer> pair: list) {
            double estimation = a * (1 - Math.exp(- b * pair.getFirst()));
            Integer roundedEstimation = (int) estimation;
            listOfEstimatedIssues.add(new Pair<>(pair.getFirst(), roundedEstimation));
        }
        return listOfEstimatedIssues;
    }

    @Override
    public String getTextFormOfTheFunction() {
        AttributedString as = new AttributedString("-b*t");
        as.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER, 0, 3);
        return "f(t) = a * (1 - e" + "<html><sup>-b*t</sup></html>" + ")";
    }
}
