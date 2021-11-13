package fi.muni.cz.models.testing;

import fi.muni.cz.models.testing.GoodnessOfFitTest;
import fi.muni.cz.models.testing.ChiSquareGoodnessOfFitTest;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.util.Pair;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class ChiSquareGoodnessOfFitTestNGTest {

    @Mock
    private Rengine rEngine;

    @InjectMocks
    private GoodnessOfFitTest test = new ChiSquareGoodnessOfFitTest(rEngine);

    private final List<Pair<Integer, Integer>> listOfPairs = new ArrayList<>();  
    private final List<Pair<Integer, Integer>> listOfShiftByOnePairs = new ArrayList<>();
    
    @BeforeClass
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Pair<Integer, Integer> pair = new Pair<>(1, 1);
        listOfPairs.add(pair);
        pair = new Pair<>(2, 2);
        listOfPairs.add(pair);
        pair = new Pair<>(3, 3);
        listOfPairs.add(pair);
        pair = new Pair<>(4, 4);
        listOfPairs.add(pair);
        
        pair = new Pair<>(1, 2);
        listOfShiftByOnePairs.add(pair);
        pair = new Pair<>(2, 3);
        listOfShiftByOnePairs.add(pair);
        pair = new Pair<>(3, 4);
        listOfShiftByOnePairs.add(pair);
        pair = new Pair<>(4, 5);
        listOfShiftByOnePairs.add(pair);
    }
    
    @Test
    public void testExactMatch() {
        double[] doubleArr = {1.000};
        when(rEngine.eval("glance(testGO)$r.squared")).thenReturn(new REXP(doubleArr));
        when(rEngine.eval("glance(testGO)$AIC")).thenReturn(new REXP(doubleArr));
        when(rEngine.eval("glance(testGO)$BIC")).thenReturn(new REXP(doubleArr));
        when(rEngine.eval("glance(testGO)$sigma")).thenReturn(new REXP(doubleArr));
        assertEquals(test.executeGoodnessOfFitTest(listOfPairs, listOfPairs, "GO").get("Chi-Square = "), "1.000");
    }
    
    @Test
    public void testShiftMatch() {
        double[] doubleArr = {1.0};
        when(rEngine.eval("glance(testGO)$r.squared")).thenReturn(new REXP(doubleArr));
        when(rEngine.eval("glance(testGO)$AIC")).thenReturn(new REXP(doubleArr));
        when(rEngine.eval("glance(testGO)$BIC")).thenReturn(new REXP(doubleArr));
        when(rEngine.eval("glance(testGO)$sigma")).thenReturn(new REXP(doubleArr));
        assertNotEquals(test.executeGoodnessOfFitTest(listOfPairs, listOfShiftByOnePairs, "GO").get("Chi-Square = "), "0.0");
    }
}
