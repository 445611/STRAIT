package fi.muni.cz.models.testing;

import fi.muni.cz.models.testing.GoodnessOfFitTest;
import fi.muni.cz.models.testing.ChiSquareGoodnessOfFitTest;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.util.Pair;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class ChiSquareGoodnessOfFitTestNGTest {
    
    private final List<Pair<Integer, Integer>> listOfPairs = new ArrayList<>();  
    private final List<Pair<Integer, Integer>> listOfShiftByOnePairs = new ArrayList<>();
    private final GoodnessOfFitTest test = new ChiSquareGoodnessOfFitTest();
    
    @BeforeClass
    public void setUp() {
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
        assertEquals(test.executeGoodnessOfFitTest(listOfPairs, listOfPairs).get("Chi-Square = "), "0.0");
    }
    
    @Test
    public void testShiftMatch() {
        assertNotEquals(test.executeGoodnessOfFitTest(listOfPairs, listOfShiftByOnePairs).get("Chi-Square = "), "0.0");
    }
}