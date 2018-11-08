package fi.muni.cz.reliability.tool.core.factory;

import fi.muni.cz.reliability.tool.core.ArgsParser;
import fi.muni.cz.reliability.tool.core.exception.InvalidInputException;
import fi.muni.cz.reliability.tool.dataprocessing.output.HtmlOutputWriter;
import fi.muni.cz.reliability.tool.dataprocessing.output.OutputWriter;
import java.util.Arrays;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class OutputWriterFactory {
    
    private static final String HTML = "html";
    
    /**
     * Get OutputWriter for CommandLine argument.
     * 
     * @param parser  parsed CommandLine.
     * @return OutputWriter
     * @throws InvalidInputException if there is no sich output type.
     */
    public static OutputWriter getIssuesWriter(ArgsParser parser) throws InvalidInputException {
        if (!parser.hasOptionOut()) {
            return checkMultipleGraphs(parser);
        } else {
            switch (parser.getOptionValueOut()) {
                case HTML:
                    return checkMultipleGraphs(parser);
                default:
                    throw new InvalidInputException(Arrays.asList("No such output type: '" 
                            + parser.getOptionValueOut() + "'"));
            }
        }
    }
    
    private static OutputWriter checkMultipleGraphs(ArgsParser parser) {
        if (parser.hasOptionGraphMultiple()) {
            return new HtmlOutputWriter(true);
        } else {
            return new HtmlOutputWriter(false);
        }
    }
}
