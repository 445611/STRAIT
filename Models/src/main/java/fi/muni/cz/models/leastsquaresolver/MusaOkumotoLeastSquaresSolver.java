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
public class MusaOkumotoLeastSquaresSolver extends SolverAbstract {

    private static final String MODEL_FUNCTION = "a*log(b * xvalues + 1)";
    private static final String MODEL_NAME = "modelMO";

    /**
     * Initialize Rengine.
     * @param rEngine Rengine.
     */
    public MusaOkumotoLeastSquaresSolver(Rengine rEngine) {
        super(rEngine);
    }
    
    @Override
    public double[] optimize(int[] startParameters, List<Pair<Integer, Integer>> listOfData) {
        initializeOptimizationInR(listOfData);
        rEngine.eval("modelMO2 <- nls2(yvalues ~ " + MODEL_FUNCTION + ", " +
                "start = data.frame(a = c(10, 1000000),b = c(0.00001, 10)), " +
                "algorithm = \"brute-force\", control = list(warnOnly = TRUE, maxiter = 100000))");
        REXP intermediate = rEngine.eval("coef(" + MODEL_NAME + "2)");
        if (intermediate == null) {
            throw new ModelException("Repository data not suitable for R evaluation.");
        }
        rEngine.eval(String.format(Locale.US, "modelMO <- nls(yvalues ~ " + MODEL_FUNCTION + ", "
                + "start = list(a = %.10f,b = %.10f), "
                + "control = list(warnOnly = TRUE, maxiter = 100000), "
                + "algorithm = \"port\")", intermediate.asDoubleArray()[0], intermediate.asDoubleArray()[1]));
        REXP result = rEngine.eval("coef(" + MODEL_NAME + ")");
        rEngine.end();
        if (result == null || result.asDoubleArray().length < 2) {
            throw new ModelException("Repository data not suitable for R evaluation.");
        }
        double[] d = result.asDoubleArray();
        return new double[]{d[0], d[1]};
    }
}
