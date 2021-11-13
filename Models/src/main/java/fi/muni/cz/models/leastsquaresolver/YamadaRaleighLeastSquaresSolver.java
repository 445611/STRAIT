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
public class YamadaRaleighLeastSquaresSolver extends SolverAbstract {

    private static final String MODEL_FUNCTION = "a*(1 - exp(-b*(1-exp(-c*(xvalues^2)/2))))";
    private static final String MODEL_NAME = "modelYamadaRaleigh";

    /**
     * Initialize Rengine.
     * @param rEngine Rengine.
     */
    public YamadaRaleighLeastSquaresSolver(Rengine rEngine) {
        super(rEngine);
    }

    @Override
    public double[] optimize(int[] startParameters, List<Pair<Integer, Integer>> listOfData) {
        // TODO CHECK
        initializeOptimizationInR(listOfData);
        rEngine.eval("modelYamadaRaleigh2 <- nls2(yvalues ~ " + MODEL_FUNCTION + ", " +
                "start = data.frame(a = c(1, 10000),b = c(0.00001, 100), c = c(0.00001, 100)), " +
                "algorithm = \"brute-force\", control = list(warnOnly = TRUE, maxiter = 100000))");
        REXP intermediate = rEngine.eval("coef(" + MODEL_NAME + "2)");
        if (intermediate == null) {
            throw new ModelException("Repository data not suitable for R evaluation.");
        }
        rEngine.eval(String.format(Locale.US, "modelYamadaRaleigh <- nls(yvalues ~ " + MODEL_FUNCTION + ", "
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
