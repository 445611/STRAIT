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
public class HossainDahiyaLeastSquaresSolver extends SolverAbstract {

    private static final String MODEL_FUNCTION = "(a * (1 - exp(-b * xvalues)) / (1 + c * exp(-b * xvalues)))";

    /**
     * Initialize Rengine.
     * @param rEngine Rengine.
     */
    public HossainDahiyaLeastSquaresSolver(Rengine rEngine) {
        super(rEngine);
    }
    
    @Override
    public double[] optimize(int[] startParameters, List<Pair<Integer, Integer>> listOfData) {
        initializeOptimizationInR(listOfData);
        rEngine.eval("modelHD2 <- nls2(yvalues ~ " + MODEL_FUNCTION + ", " +
                "start = data.frame(a = c(10, 1000000),b = c(0.00001, 10), c = c(0.00001, 200)), " +
                "algorithm = \"brute-force\", control = list(warnOnly = TRUE, maxiter = 100000))");
        REXP intermediate = rEngine.eval("coef(modelHD2)");
        if (intermediate == null) {
            throw new ModelException("Repository data not suitable for R evaluation.");
        }
        rEngine.eval(String.format(Locale.US, "modelHD <- nls(yvalues ~ " + MODEL_FUNCTION + ", "
                + "start = list(a = %.10f,b = %.10f,c = %.10f), "
                + "lower = list(a = 0, b = 0, c = 0), "
                + "control = list(warnOnly = TRUE, maxiter = 100000), "
                + "algorithm = \"port\")",
                intermediate.asDoubleArray()[0], intermediate.asDoubleArray()[1], intermediate.asDoubleArray()[2]));
        REXP result = rEngine.eval("coef(modelHD)");
        rEngine.end();
        if (result == null || result.asDoubleArray().length < 3) {
            throw new ModelException("Repository data not suitable for R evaluation.");
        }
        double[] d = result.asDoubleArray();
        return new double[]{d[0], d[1], d[2]};
    }
}
