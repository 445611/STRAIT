package fi.muni.cz.models.leastsquaresolver;

import fi.muni.cz.models.leastsquaresolver.DuaneLeastSquaresSolver;
import fi.muni.cz.models.leastsquaresolver.MusaOkumotoLeastSquaresSolver;
import fi.muni.cz.models.leastsquaresolver.GOLeastSquaresSolver;
import fi.muni.cz.models.leastsquaresolver.HossainDahiyaLeastSquaresSolver;
import fi.muni.cz.models.leastsquaresolver.GOSShapedLeastSquaresSolver;
import fi.muni.cz.models.leastsquaresolver.Solver;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.util.Pair;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class LeastSquaresSolverNGTest {
    
    private final List<Pair<Integer, Integer>> listOfPairs = new ArrayList<>();  
    
    @Mock
    private Rengine rEngine;
    
    @InjectMocks
    private Solver GOSolver = new GOLeastSquaresSolver(rEngine);
    @InjectMocks
    private Solver GOSShapedSolver = new GOSShapedLeastSquaresSolver(rEngine);
    @InjectMocks
    private Solver duaneSolver = new DuaneLeastSquaresSolver(rEngine);
    @InjectMocks
    private Solver hossainDahiyaSolver = new HossainDahiyaLeastSquaresSolver(rEngine);
    @InjectMocks
    private Solver musaOkumotoSolver = new MusaOkumotoLeastSquaresSolver(rEngine);
    @InjectMocks
    private Solver weibullSolver = new WeibullLeastSquaresSolver(rEngine);
    @InjectMocks
    private Solver yamadaExponentialSolver = new YamadaExponentialLeastSquaresSolver(rEngine);
    @InjectMocks
    private Solver yamadaRaleighSolver = new YamadaRaleighLeastSquaresSolver(rEngine);
    @InjectMocks
    private Solver logLogisticSolver = new LogLogisticLeastSquaresSolver(rEngine);
    
    @BeforeClass
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        Pair<Integer, Integer> pair = new Pair<>(1, 1);
        listOfPairs.add(pair);
    }
    
    @Test
    public void testOptimizeOnAllSolvers() {
        double[] doubleArr = {1.0, 1.0};
        when(rEngine.eval("coef(modelGO)")).thenReturn(new REXP(doubleArr));
        when(rEngine.eval("coef(modelGOS)")).thenReturn(new REXP(doubleArr));
        when(rEngine.eval("coef(modelDuane)")).thenReturn(new REXP(doubleArr));
        when(rEngine.eval("coef(modelMO)")).thenReturn(new REXP(doubleArr));
        int[] intArr = {1, 1};
        
        assertEquals(GOSolver.optimize(intArr, listOfPairs), doubleArr); 
        assertEquals(GOSShapedSolver.optimize(intArr, listOfPairs), doubleArr); 
        assertEquals(duaneSolver.optimize(intArr, listOfPairs), doubleArr); 
        assertEquals(musaOkumotoSolver.optimize(intArr, listOfPairs), doubleArr); 
        
        int[] int3Arr = {1, 1, 1};
        double[] double3Arr = {1.0, 1.0, 1.0};
        when(rEngine.eval("coef(modelHD)")).thenReturn(new REXP(double3Arr));
        when(rEngine.eval("coef(modelWeibull)")).thenReturn(new REXP(double3Arr));
        when(rEngine.eval("coef(modelLogLogistic)")).thenReturn(new REXP(double3Arr));
        assertEquals(hossainDahiyaSolver.optimize(int3Arr, listOfPairs), double3Arr);
        assertEquals(weibullSolver.optimize(int3Arr, listOfPairs), double3Arr);
        assertEquals(logLogisticSolver.optimize(int3Arr, listOfPairs), double3Arr);

        int[] int4Arr = {1, 1, 1, 1};
        double[] double4Arr = {1.0, 1.0, 1.0, 1.0};
        when(rEngine.eval("coef(modelYamadaExponential)")).thenReturn(new REXP(double4Arr));
        when(rEngine.eval("coef(modelYamadaRaleigh)")).thenReturn(new REXP(double4Arr));
        assertEquals(yamadaExponentialSolver.optimize(int4Arr, listOfPairs), double4Arr);
        assertEquals(yamadaRaleighSolver.optimize(int4Arr, listOfPairs), double4Arr);
    }
}
