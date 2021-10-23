package fi.muni.cz.models.leastsquaresolver;

import fi.muni.cz.models.exception.ModelException;
import java.util.List;
import org.apache.commons.math3.util.Pair;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class WeibullLeastSquaresSolver extends SolverAbstract {

    private static final Rengine R_ENGINE = new Rengine();

    /**
     * Initialize Rengine.
     * @param rEngine Rengine.
     */
    public WeibullLeastSquaresSolver(Rengine rEngine) {
        super(rEngine);
    }

    @Override
    public double[] optimize(int[] startParameters, List<Pair<Integer, Integer>> listOfData) {
        rEngine.eval(String.format("xvalues = c(%s)", getPreparedListWithCommas(getListOfFirstFromPair(listOfData))));
        rEngine.eval(String.format("yvalues = c(%s)", getPreparedListWithCommas(getListOfSecondFromPair(listOfData))));
        rEngine.eval(String.format("modelWeibull <- nls(yvalues ~ a * (1 - exp(-b * (xvalues ^ c))), "
                + "start = list(a = %d,b = %d,c = %d), "
                + "control = list(warnOnly = TRUE), "
                + "lower = list(a = 0,b = 0,c = 0), algorithm = \"port\")",
                startParameters[0], startParameters[1], startParameters[2]));
        REXP result = rEngine.eval("coef(modelWeibull)");
        rEngine.end();
        if (result == null || result.asDoubleArray().length < 3) {
            throw new ModelException("Repository data not suaitable for R evealuation.");
        }
        double[] d = result.asDoubleArray();
        return new double[]{d[0], d[1], d[2]};
    }
}
