package fi.muni.cz.models.leastsquaresolver;

import fi.muni.cz.models.exception.ModelException;
import org.apache.commons.math3.util.Pair;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

import java.util.List;
import java.util.Locale;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class LogLogisticLeastSquaresSolver extends SolverAbstract {

    private static final String MODEL_FUNCTION = "a*((b*xvalues)^c)/(1 + (b*xvalues)^c)";
    private static final String MODEL_NAME = "modelLogLogistic";

    /**
     * Initialize Rengine.
     *
     * @param rEngine Rengine.
     */
    public LogLogisticLeastSquaresSolver(Rengine rEngine) {
        super(rEngine);
    }

    @Override
    public double[] optimize(int[] startParameters, List<Pair<Integer, Integer>> listOfData) {
        initializeOptimizationInR(listOfData);
        rEngine.eval("modelLogLogistic2 <- nls2(yvalues ~ " + MODEL_FUNCTION + ", " +
                "start = data.frame(a = c(10, 10000),b = c(0.001, 10), c = c(0.001, 10)), " +
                "algorithm = \"brute-force\", control = list(warnOnly = TRUE, maxiter = 100000))");
        REXP intermediate = rEngine.eval("coef(" + MODEL_NAME + "2)");
        if (intermediate == null) {
            throw new ModelException("Repository data not suitable for R evaluation.");
        }
        rEngine.eval(String.format(Locale.US, "modelLogLogistic <- nls(yvalues ~ " + MODEL_FUNCTION + ", "
                        + "start = list(a = %.10f,b = %.10f, c = %.10f), "
                        + "lower = list(a = 0, b = 0, c = 0), "
                        + "control = list(warnOnly = TRUE, maxiter = 100000), "
                        + "algorithm = \"port\")",
                intermediate.asDoubleArray()[0], intermediate.asDoubleArray()[1], intermediate.asDoubleArray()[2]));
        REXP result = rEngine.eval("coef(" + MODEL_NAME + ")");
        rEngine.end();
        if (result == null || result.asDoubleArray().length < 3) {
            throw new ModelException("Repository data not suitable for R evaluation.");
        }
        double[] d = result.asDoubleArray();
        return new double[]{d[0], d[1], d[2]};
    }
}
