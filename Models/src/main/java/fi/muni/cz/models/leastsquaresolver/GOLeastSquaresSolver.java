package fi.muni.cz.models.leastsquaresolver;

import fi.muni.cz.models.exception.ModelException;
import java.util.List;
import org.apache.commons.math3.util.Pair;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class GOLeastSquaresSolver extends SolverAbstract {

    /**
     * Initialize Rengine.
     * @param rEngine Rengine.
     */
    public GOLeastSquaresSolver(Rengine rEngine) {
        super(rEngine);
    }
    
    @Override
    public double[] optimize(int[] startParameters, List<Pair<Integer, Integer>> listOfData) {        
        rEngine.eval(String.format("xvalues = c(%s)", getPreparedListWithCommas(getListOfFirstFromPair(listOfData))));
        rEngine.eval(String.format("yvalues = c(%s)", getPreparedListWithCommas(getListOfSecondFromPair(listOfData))));
        rEngine.eval(String.format("model <- nls(yvalues ~ a*(1 - exp(-b*xvalues)), "
                + "start = list(a = %d,b = %d), "
                + "lower = list(a = 0, b = 0), "
                + "algorithm = \"port\")", startParameters[0], startParameters[1]));
        REXP result = rEngine.eval("coef(model)");
        rEngine.end();
        if (result == null) {
            throw new ModelException("Repository data not suaitable for R evealuation.");
        }
        double[] d = result.asDoubleArray();
        return new double[]{d[0], d[1]};
    }
}