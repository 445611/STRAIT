package fi.muni.cz.core.factory;

import fi.muni.cz.core.ArgsParser;
import static fi.muni.cz.core.ArgsParser.OPT_FILTER_CLOSED;
import static fi.muni.cz.core.ArgsParser.OPT_FILTER_LABELS;
import static fi.muni.cz.core.ArgsParser.OPT_FILTER_TIME;
import fi.muni.cz.dataprocessing.exception.DataProcessingException;
import fi.muni.cz.dataprocessing.issuesprocessing.FilterByLabel;
import fi.muni.cz.dataprocessing.issuesprocessing.FilterByTime;
import fi.muni.cz.dataprocessing.issuesprocessing.FilterClosed;
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
 *  @author Radoslav Micko, 445611@muni.cz
 */
public class FilterFactoryNGTest {
    
    @Mock
    private CommandLine cmdl;
    
    @InjectMocks
    private ArgsParser argsParser = new ArgsParser();
    
    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testOptionFilterByLabel() {
        when(cmdl.hasOption(OPT_FILTER_LABELS)).thenReturn(true);
        assertEquals(FilterFactory.getFilters(argsParser).size(), 1);
        assertTrue(FilterFactory.getFilters(argsParser).get(0) instanceof FilterByLabel);
    }
    
    @Test
    public void testOptionFilterClosed() {
        when(cmdl.hasOption(OPT_FILTER_CLOSED)).thenReturn(true);
        assertEquals(FilterFactory.getFilters(argsParser).size(), 1);
        assertTrue(FilterFactory.getFilters(argsParser).get(0) instanceof FilterClosed);
    }
    
    @Test
    public void testOptionFilterByTime() {
        when(cmdl.hasOption(OPT_FILTER_TIME)).thenReturn(true);
        when(cmdl.getOptionValues(OPT_FILTER_TIME)).thenReturn(new String[]{"2018-10-10T10:10:10", "2018-10-11T10:10:10"});
        assertEquals(FilterFactory.getFilters(argsParser).size(), 1);
        assertTrue(FilterFactory.getFilters(argsParser).get(0) instanceof FilterByTime);
    }
    
    @Test(expectedExceptions = DataProcessingException.class)
    public void testOptionFilterByTimeWrongFormat() {
        when(cmdl.hasOption(OPT_FILTER_TIME)).thenReturn(true);
        when(cmdl.getOptionValues(OPT_FILTER_TIME)).thenReturn(new String[]{"2018-10-10", "2018-10-11"});
        FilterFactory.getFilters(argsParser);
    }
}