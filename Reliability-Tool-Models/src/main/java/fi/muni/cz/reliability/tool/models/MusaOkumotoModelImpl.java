package fi.muni.cz.reliability.tool.models;

import fi.muni.cz.reliability.tool.models.leastsquaresolver.Function;
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
public class MusaOkumotoModelImpl extends AbstractModel {

    private final String firstParameter = "α";
    private final String secondParameter = "β";
    
    /**
     * Initialize model attributes.
     * 
     * @param startParameters       start parameters to set.
     * @param listOfIssues          list of issues.
     * @param goodnessOfFitTest     Goodness of fit test to execute.
     */
    public MusaOkumotoModelImpl(double[] startParameters, List<Pair<Integer, Integer>> listOfIssues, 
            GoodnessOfFitTest goodnessOfFitTest) {
        super(startParameters, listOfIssues, goodnessOfFitTest);
    }
    
    @Override
    protected List<Pair<Integer, Integer>> calculateEstimatedIssuesOccurance(double howMuchToPredict) {
        List<Pair<Integer, Integer>> listOfEstimatedIssues = new ArrayList<>();
        for (Pair<Integer, Integer> pair: listOfIssues) {
            double estimation = modelParameters.get(firstParameter) * 
                    Math.log(modelParameters.get(secondParameter) * pair.getFirst() + 1);
            Integer roundedEstimation = (int) estimation;
            listOfEstimatedIssues.add(new Pair<>(pair.getFirst(), 
                    roundedEstimation == 0 ? 1 : roundedEstimation));
        }
        int last = listOfIssues.get(listOfIssues.size() - 1).getFirst();
        for (int i = last + 1; i < last + howMuchToPredict; i++) {
            double estimation = modelParameters.get(firstParameter) * 
                    Math.log(modelParameters.get(secondParameter) * i + 1);
            Integer roundedEstimation = (int) estimation;
            listOfEstimatedIssues.add(new Pair<>(i, roundedEstimation));
        }
        return listOfEstimatedIssues;
    }
    
    @Override
    protected  Function getModelFunction() {
        return new MusaOkumotoFunction();
    }
    
    @Override
    protected void setParametersToMap(double[] params) {
        Map<String, Double> map = new HashMap<>();
        map.put(firstParameter, params[0]);
        map.put(secondParameter, params[1]);
        modelParameters = map;
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