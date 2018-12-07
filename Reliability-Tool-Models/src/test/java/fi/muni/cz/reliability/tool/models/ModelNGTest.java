package fi.muni.cz.reliability.tool.models;

import fi.muni.cz.reliability.tool.models.leastsquaresolver.Solver;
import fi.muni.cz.reliability.tool.models.testing.GoodnessOfFitTest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.util.Pair;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *  @author Radoslav Micko, 445611@muni.cz
 */
public class ModelNGTest {
    
    private List<Pair<Integer, Integer>> listOfPairs = new ArrayList<>();
    
    @Mock
    private GoodnessOfFitTest goodnessOfFitTest;
    
    @Mock
    private Solver solver;
    
    @InjectMocks
    private Model GOModel = new GOModelImpl(listOfPairs, goodnessOfFitTest, solver);
    @InjectMocks
    private Model GOSShapedModel = new GOSShapedModelImpl(listOfPairs, goodnessOfFitTest, solver);
    @InjectMocks
    private Model DuaneModel = new DuaneModelImpl(listOfPairs, goodnessOfFitTest, solver);
    @InjectMocks
    private Model HossainDahiyaModel = new HossainDahiyaModelImpl(listOfPairs, goodnessOfFitTest, solver);
    @InjectMocks
    private Model MusaOkumotoModel = new MusaOkumotoModelImpl(listOfPairs, goodnessOfFitTest, solver);
    
    @BeforeClass
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        Pair<Integer, Integer> pair = new Pair<>(1, 1);
        listOfPairs.add(pair);
    }
    
    private void prepareMocksForTwoParams() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("a", "a");
        when(solver.optimize(any(int[].class), any(List.class))).thenReturn(new double[]{1, 1});
        when(goodnessOfFitTest.executeGoodnessOfFitTest(any(List.class), any(List.class)))
                .thenReturn(map);
    }
    
    private void prepareMocksForThreeParams() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("a", "a");
        when(solver.optimize(any(int[].class), any(List.class))).thenReturn(new double[]{1, 1, 1});
        when(goodnessOfFitTest.executeGoodnessOfFitTest(any(List.class), any(List.class)))
                .thenReturn(map);
    }
    
    @Test
    public void testGOModel() {
        prepareMocksForTwoParams();
        
        GOModel.estimateModelData();
        assertEquals(GOModel.getGoodnessOfFitData().get("a"), "a");
        assertEquals(GOModel.getModelParameters().get("a"), new Double(1));
        assertEquals(GOModel.getModelParameters().get("b"), new Double(1));
    }
    
    @Test
    public void testGOSShapedModel() {
        prepareMocksForTwoParams();
        
        GOSShapedModel.estimateModelData();
        assertEquals(GOSShapedModel.getGoodnessOfFitData().get("a"), "a");
        assertEquals(GOSShapedModel.getModelParameters().get("a"), new Double(1));
        assertEquals(GOSShapedModel.getModelParameters().get("b"), new Double(1));
    }
    
    @Test
    public void testDuaneModel() {
        prepareMocksForTwoParams();
        
        DuaneModel.estimateModelData();
        assertEquals(DuaneModel.getGoodnessOfFitData().get("a"), "a");
        assertEquals(DuaneModel.getModelParameters().get("α"), new Double(1));
        assertEquals(DuaneModel.getModelParameters().get("β"), new Double(1));
    }
    
    @Test
    public void testMusaOkumotoModel() {
        prepareMocksForTwoParams();
        
        MusaOkumotoModel.estimateModelData();
        assertEquals(MusaOkumotoModel.getGoodnessOfFitData().get("a"), "a");
        assertEquals(MusaOkumotoModel.getModelParameters().get("α"), new Double(1));
        assertEquals(MusaOkumotoModel.getModelParameters().get("β"), new Double(1));
    }
    
    @Test
    public void testHossainDahiyaModel() {
        prepareMocksForThreeParams();
        
        HossainDahiyaModel.estimateModelData();
        assertEquals(HossainDahiyaModel.getGoodnessOfFitData().get("a"), "a");
        assertEquals(HossainDahiyaModel.getModelParameters().get("a"), new Double(1));
        assertEquals(HossainDahiyaModel.getModelParameters().get("b"), new Double(1));
        assertEquals(HossainDahiyaModel.getModelParameters().get("c"), new Double(1));
    }
}