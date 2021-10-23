package fi.muni.cz.models.leastsquaresolver;

import fi.muni.cz.models.exception.ModelException;
import java.util.List;
import org.apache.commons.math3.util.Pair;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class MusaOkumotoLeastSquaresSolver extends SolverAbstract {

    /**
     * Initialize Rengine.
     * @param rEngine Rengine.
     */
    public MusaOkumotoLeastSquaresSolver(Rengine rEngine) {
        super(rEngine);
    }
    
    @Override
    public double[] optimize(int[] startParameters, List<Pair<Integer, Integer>> listOfData) {
        rEngine.eval(String.format("xvalues = c(%s)", getPreparedListWithCommas(getListOfFirstFromPair(listOfData))));
        rEngine.eval(String.format("yvalues = c(%s)", getPreparedListWithCommas(getListOfSecondFromPair(listOfData))));
        rEngine.eval(String.format("modelMO <- nls(yvalues ~ a*log(b * xvalues + 1), "
                + "start = list(a = %d,b = %d), "
                + "control = list(warnOnly = TRUE), "
                + "algorithm = \"port\")", startParameters[0], startParameters[1]));
        /* FOR DUBUGGING */
        /*REXP a = rEngine.eval(String.format("try(modelMO <- nls(yvalues ~ a*log(b * xvalues + 1), "
                + "start = list(a = %d,b = %d), "
                + "control = list(warnOnly = TRUE), "
                + "algorithm = \"port\"), silent = TRUE)", startParameters[0], startParameters[1]));*/
        REXP result = rEngine.eval("coef(modelMO)");
        rEngine.end();
        if (result == null || result.asDoubleArray().length < 2) {
            throw new ModelException("Repository data not suaitable for R evealuation.");
        }
        double[] d = result.asDoubleArray();
        return new double[]{d[0], d[1]};
    }
}
