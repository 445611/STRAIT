package fi.muni.cz.models.leastsquaresolver;

import java.util.List;
import org.apache.commons.math3.util.Pair;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class DuaneLeastSquaresSolver extends SolverAbstract {

    /**
     * Initialize Rengine.
     * @param rEngine Rengine.
     */
    public DuaneLeastSquaresSolver(Rengine rEngine) {
        super(rEngine);
    }
    
    @Override
    public double[] optimize(int[] startParameters, List<Pair<Integer, Integer>> listOfData) {
        rEngine.eval(String.format("xvalues = c(%s)", getPreparedListWithCommas(getListOfFirstFromPair(listOfData))));
        rEngine.eval(String.format("yvalues = c(%s)", getPreparedListWithCommas(getListOfSecondFromPair(listOfData))));
        rEngine.eval(String.format("model <- nls(yvalues ~ a*(xvalues ^ b), "
                + "start = list(a = %d,b = %d), "
                + "lower = list(a = 0, b = 0), "
                + "algorithm = \"port\")", startParameters[0], startParameters[1]));
        REXP result = rEngine.eval("coef(model)");
        rEngine.end();
        double[] d = result.asDoubleArray();
        return new double[]{d[0], d[1]};
    }
}