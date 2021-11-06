package fi.muni.cz.models;

import fi.muni.cz.models.leastsquaresolver.Solver;
import fi.muni.cz.models.testing.GoodnessOfFitTest;
import org.apache.commons.math3.util.Pair;

import java.util.HashMap;
import java.util.List;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class EmptyModelImpl extends ModelAbstract {

    /**
     * Initialize model attributes.
     *
     * @param listOfIssues      list of issues.
     * @param goodnessOfFitTest Goodness of fit test to execute.
     * @param solver            Solver to estimate model parameters.
     */
    public EmptyModelImpl(
            List<Pair<Integer, Integer>> listOfIssues,
            GoodnessOfFitTest goodnessOfFitTest,
            Solver solver) {
        super(listOfIssues, goodnessOfFitTest, solver);
    }


    @Override
    protected double getFunctionValue(Integer testPeriod) {
        return 0;
    }

    @Override
    protected void setParametersToMap(double[] params) {
        modelParameters = new HashMap<>();
    }

    @Override
    protected int[] getInitialParametersValue() {
        return new int[]{};
    }

    @Override
    public String getTextFormOfTheFunction() {
        return "μ(t) =";
    }

    @Override
    public String toString() {
        return "Empty model";
    }

    @Override
    protected String getModelShortName() {
        return "EM";
    }
}
