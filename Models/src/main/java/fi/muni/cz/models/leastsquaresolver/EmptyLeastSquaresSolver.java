package fi.muni.cz.models.leastsquaresolver;

import fi.muni.cz.models.exception.ModelException;
import org.apache.commons.math3.util.Pair;
import org.rosuda.JRI.Rengine;

import java.util.List;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class EmptyLeastSquaresSolver extends SolverAbstract {

    /**
     * Initialize Rengine.
     * @param rEngine Rengine.
     */
    public EmptyLeastSquaresSolver(Rengine rEngine) {
        super(rEngine);
    }

    @Override
    public double[] optimize(int[] startParameters, List<Pair<Integer, Integer>> listOfData) {
        throw new ModelException("Empty model");
    }
}
