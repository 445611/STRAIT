package fi.muni.cz.models.leastsquaresolver;

import fi.muni.cz.models.exception.ModelException;
import java.util.List;
import java.util.Locale;

import org.apache.commons.math3.util.Pair;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class GOSShapedLeastSquaresSolver extends SolverAbstract {

    private static final String MODEL_FUNCTION = "a*(1 - (1 + b*xvalues)*exp(-b*xvalues))";

    /**
     * Initialize Rengine.
     * @param rEngine Rengine.
     */
    public GOSShapedLeastSquaresSolver(Rengine rEngine) {
        super(rEngine);
    }

    @Override
    public double[] optimize(int[] startParameters, List<Pair<Integer, Integer>> listOfData) {
        initializeOptimizationInR(listOfData);
        rEngine.eval("modelGOS2 <- nls2(yvalues ~ " + MODEL_FUNCTION + ", " +
               "start = data.frame(a = c(100, 1000000),b = c(0.01, 10)), " +
                "algorithm = \"random-search\", control = list(warnOnly = TRUE, maxiter = 1000000))");
        REXP intermediate = rEngine.eval("coef(modelGOS2)");
        if (intermediate == null) {
            throw new ModelException("Repository data not suitable for R evaluation.");
        }
        rEngine.eval(String.format(Locale.US, "modelGOS <- nls(yvalues ~ " + MODEL_FUNCTION + ", "
                + "start = list(a = %.10f,b = %.10f), "
                + "lower = list(a = 0, b = 0), "
                + "control = list(warnOnly = TRUE, maxiter = 1000000), "
                + "algorithm = \"port\")", intermediate.asDoubleArray()[0], intermediate.asDoubleArray()[1]));
        REXP result = rEngine.eval("coef(modelGOS)");
        rEngine.end();
        if (result == null || result.asDoubleArray().length < 2) {
            throw new ModelException("Repository data not suitable for R evaluation.");
        }
        double[] d = result.asDoubleArray();
        return new double[]{d[0], d[1]};
    }
}
