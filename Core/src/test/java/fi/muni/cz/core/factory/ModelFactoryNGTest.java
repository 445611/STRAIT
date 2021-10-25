package fi.muni.cz.core.factory;

import fi.muni.cz.core.ArgsParser;
import static fi.muni.cz.core.ArgsParser.OPT_MODELS;
import fi.muni.cz.core.exception.InvalidInputException;
import fi.muni.cz.models.*;
import fi.muni.cz.models.testing.ChiSquareGoodnessOfFitTest;
import java.util.ArrayList;
import org.apache.commons.cli.CommandLine;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class ModelFactoryNGTest {
    
    @Mock
    private CommandLine cmdl;
    
    @InjectMocks
    private ArgsParser argsParser = new ArgsParser();
    
    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetModelsWithoutModelOption() throws InvalidInputException {
        when(cmdl.hasOption(OPT_MODELS)).thenReturn(false);
        assertEquals(ModelFactory.getModels(new ArrayList<>(), 
                new ChiSquareGoodnessOfFitTest(), argsParser).size(), 9);
        assertTrue(ModelFactory.getModels(new ArrayList<>(), 
                new ChiSquareGoodnessOfFitTest(), argsParser).get(0) instanceof GOModelImpl);
    }
    
    @Test
    public void testGetModelsWithModelOption() throws InvalidInputException {
        when(cmdl.hasOption(OPT_MODELS)).thenReturn(true);
        when(cmdl.getOptionValues(OPT_MODELS)).thenReturn(new String[]{});
        assertEquals(ModelFactory.getModels(new ArrayList<>(), 
                new ChiSquareGoodnessOfFitTest(), argsParser).size(), 0);
        
        when(cmdl.getOptionValues(OPT_MODELS)).thenReturn(new String[]{"go"});
        assertEquals(ModelFactory.getModels(new ArrayList<>(), 
                new ChiSquareGoodnessOfFitTest(), argsParser).size(), 1);
        assertTrue(ModelFactory.getModels(new ArrayList<>(), 
                new ChiSquareGoodnessOfFitTest(), argsParser).get(0) instanceof GOModelImpl);
    }
    
    @Test(expectedExceptions = InvalidInputException.class)
    public void testGetModelsWithModelOptionButIncorrectModelName() throws Exception {
        when(cmdl.hasOption(OPT_MODELS)).thenReturn(true);
        when(cmdl.getOptionValues(OPT_MODELS)).thenReturn(new String[]{"nomodelname"});
        ModelFactory.getModels(new ArrayList<>(), new ChiSquareGoodnessOfFitTest(), argsParser);
    }
    
    @Test
    public void testGetModelsWithGOModelOption() throws InvalidInputException {
        when(cmdl.hasOption(OPT_MODELS)).thenReturn(true);
        when(cmdl.getOptionValues(OPT_MODELS)).thenReturn(new String[]{"go"});
        assertTrue(ModelFactory.getModels(new ArrayList<>(), 
                new ChiSquareGoodnessOfFitTest(), argsParser).get(0) instanceof GOModelImpl);
    }
    
    @Test
    public void testGetModelsWithGOSModelOption() throws InvalidInputException {
        when(cmdl.hasOption(OPT_MODELS)).thenReturn(true);
        when(cmdl.getOptionValues(OPT_MODELS)).thenReturn(new String[]{"gos"});
        assertTrue(ModelFactory.getModels(new ArrayList<>(), 
                new ChiSquareGoodnessOfFitTest(), argsParser).get(0) instanceof GOSShapedModelImpl);
    }
    
    @Test
    public void testGetModelsWithMOModelOption() throws InvalidInputException {
        when(cmdl.hasOption(OPT_MODELS)).thenReturn(true);
        when(cmdl.getOptionValues(OPT_MODELS)).thenReturn(new String[]{"mo"});
        assertTrue(ModelFactory.getModels(new ArrayList<>(), 
                new ChiSquareGoodnessOfFitTest(), argsParser).get(0) instanceof MusaOkumotoModelImpl);
    }
    
    @Test
    public void testGetModelsWithDUModelOption() throws InvalidInputException {
        when(cmdl.hasOption(OPT_MODELS)).thenReturn(true);
        when(cmdl.getOptionValues(OPT_MODELS)).thenReturn(new String[]{"du"});
        assertTrue(ModelFactory.getModels(new ArrayList<>(), 
                new ChiSquareGoodnessOfFitTest(), argsParser).get(0) instanceof DuaneModelImpl);
    }
    
    @Test
    public void testGetModelsWithHDModelOption() throws InvalidInputException {
        when(cmdl.hasOption(OPT_MODELS)).thenReturn(true);
        when(cmdl.getOptionValues(OPT_MODELS)).thenReturn(new String[]{"hd"});
        assertTrue(ModelFactory.getModels(new ArrayList<>(), 
                new ChiSquareGoodnessOfFitTest(), argsParser).get(0) instanceof HossainDahiyaModelImpl);
    }

    @Test
    public void testGetModelsWithWEModelOption() throws InvalidInputException {
        when(cmdl.hasOption(OPT_MODELS)).thenReturn(true);
        when(cmdl.getOptionValues(OPT_MODELS)).thenReturn(new String[]{"we"});
        assertTrue(ModelFactory.getModels(new ArrayList<>(),
                new ChiSquareGoodnessOfFitTest(), argsParser).get(0) instanceof WeibullModelImpl);
    }

    @Test
    public void testGetModelsWithYEModelOption() throws InvalidInputException {
        when(cmdl.hasOption(OPT_MODELS)).thenReturn(true);
        when(cmdl.getOptionValues(OPT_MODELS)).thenReturn(new String[]{"ye"});
        assertTrue(ModelFactory.getModels(new ArrayList<>(),
                new ChiSquareGoodnessOfFitTest(), argsParser).get(0) instanceof YamadaExponentialModelImpl);
    }

    @Test
    public void testGetModelsWithYRModelOption() throws InvalidInputException {
        when(cmdl.hasOption(OPT_MODELS)).thenReturn(true);
        when(cmdl.getOptionValues(OPT_MODELS)).thenReturn(new String[]{"yr"});
        assertTrue(ModelFactory.getModels(new ArrayList<>(),
                new ChiSquareGoodnessOfFitTest(), argsParser).get(0) instanceof YamadaRaleighModelImpl);
    }

    @Test
    public void testGetModelsWithLLModelOption() throws InvalidInputException {
        when(cmdl.hasOption(OPT_MODELS)).thenReturn(true);
        when(cmdl.getOptionValues(OPT_MODELS)).thenReturn(new String[]{"ll"});
        assertTrue(ModelFactory.getModels(new ArrayList<>(),
                new ChiSquareGoodnessOfFitTest(), argsParser).get(0) instanceof LogLogisticModelImpl);
    }
    
    @Test
    public void testGetModelsWithAllModelOption() throws InvalidInputException {
        when(cmdl.hasOption(OPT_MODELS)).thenReturn(true);
        when(cmdl.getOptionValues(OPT_MODELS)).thenReturn(new String[]{"hd", "go", "gos", "du", "mo", "we", "ye", "yr", "ll"});
        assertEquals(ModelFactory.getModels(new ArrayList<>(), 
                new ChiSquareGoodnessOfFitTest(), argsParser).size(), 9);
    }
}
