package fi.muni.cz.reliability.tool.core.factory;

import fi.muni.cz.reliability.tool.core.ArgsParser;
import static fi.muni.cz.reliability.tool.core.ArgsParser.OPT_GRAPH_MULTIPLE;
import static fi.muni.cz.reliability.tool.core.ArgsParser.OPT_OUT;
import fi.muni.cz.reliability.tool.core.exception.InvalidInputException;
import fi.muni.cz.reliability.tool.dataprocessing.output.HtmlOutputWriter;
import static junit.framework.Assert.assertTrue;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class OutputWriterFactoryNGTest {
    
    @Mock
    private CommandLine cmdl;
    
    @InjectMocks
    private ArgsParser argsParser = new ArgsParser();
    
    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testOptionOutHtml() throws ParseException, InvalidInputException {
        when(cmdl.hasOption(OPT_OUT)).thenReturn(true);
        when(cmdl.getOptionValue(OPT_OUT)).thenReturn("html");
        assertTrue(OutputWriterFactory.getIssuesWriter(argsParser) instanceof HtmlOutputWriter);
    }
    
    @Test(expectedExceptions = InvalidInputException.class)
    public void testOptionOutNoHtml() throws ParseException, InvalidInputException {
        when(cmdl.hasOption(OPT_OUT)).thenReturn(true);
        when(cmdl.getOptionValue(OPT_OUT)).thenReturn("nohtml");
        OutputWriterFactory.getIssuesWriter(argsParser);
    }
    
    @Test
    public void testOptionNoOut() throws ParseException, InvalidInputException {
        when(cmdl.hasOption(OPT_OUT)).thenReturn(false);
        when(cmdl.hasOption(OPT_GRAPH_MULTIPLE)).thenReturn(false);
        assertTrue(OutputWriterFactory.getIssuesWriter(argsParser) instanceof HtmlOutputWriter);
        when(cmdl.hasOption(OPT_GRAPH_MULTIPLE)).thenReturn(true);
        assertTrue(OutputWriterFactory.getIssuesWriter(argsParser) instanceof HtmlOutputWriter);
    }
}
