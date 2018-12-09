package fi.muni.cz.core.factory;

import fi.muni.cz.core.ArgsParser;
import static fi.muni.cz.core.ArgsParser.OPT_SAVE;
import fi.muni.cz.core.exception.InvalidInputException;
import fi.muni.cz.dataprocessing.output.CsvFileIssuesWriter;
import org.apache.commons.cli.CommandLine;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class IssuesWriterFactoryNGTest {
    
    @Mock
    private CommandLine cmdl;
    
    @InjectMocks
    private ArgsParser argsParser = new ArgsParser();
    
    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCsvIssuesWriter() throws Exception {
        when(cmdl.getOptionValue(OPT_SAVE)).thenReturn("csv");
        assertTrue(IssuesWriterFactory.getIssuesWriter(argsParser) instanceof CsvFileIssuesWriter);
    }
    
    @Test(expectedExceptions = InvalidInputException.class)
    public void testGetNoCsvIssuesWriter() throws Exception {
        when(cmdl.getOptionValue(OPT_SAVE)).thenReturn("nocsv");
        IssuesWriterFactory.getIssuesWriter(argsParser);
    }
}