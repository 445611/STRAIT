package fi.muni.cz.models.leastsquaresolver;

import fi.muni.cz.models.exception.ModelException;
import org.apache.commons.math3.util.Pair;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

import java.util.List;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class YamadaExponentialLeastSquaresSolver extends SolverAbstract {

    /**
     * Initialize Rengine.
     * @param rEngine Rengine.
     */
    public YamadaExponentialLeastSquaresSolver(Rengine rEngine) {
        super(rEngine);
    }

    @Override
    public double[] optimize(int[] startParameters, List<Pair<Integer, Integer>> listOfData) {
        rEngine.eval(String.format("xvalues = c(%s)", getPreparedListWithCommas(getListOfFirstFromPair(listOfData))));
        rEngine.eval(String.format("yvalues = c(%s)", getPreparedListWithCommas(getListOfSecondFromPair(listOfData))));
        rEngine.eval(String.format("modelYamadaExponential <- nls(yvalues ~ a*(1 - exp(-b*c*(1-exp(-d*xvalues)))), "
                + "start = list(a = %d,b = %d, c = %d, d = %d), "
                + "lower = list(a = 0, b = 0, c = 0, d = 0), "
                + "control = list(warnOnly = TRUE), "
                + "algorithm = \"port\")", startParameters[0], startParameters[1],
                startParameters[2], startParameters[3]));
        REXP result = rEngine.eval("coef(modelYamadaExponential)");
        rEngine.end();
        if (result == null || result.asDoubleArray().length < 4) {
            throw new ModelException("Repository data not suitable for R evaluation.");
        }
        double[] d = result.asDoubleArray();
        return new double[]{d[0], d[1], d[2], d[3]};
    }
}
