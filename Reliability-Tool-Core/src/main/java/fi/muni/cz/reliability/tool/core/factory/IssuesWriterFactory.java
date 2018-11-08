package fi.muni.cz.reliability.tool.core.factory;

import fi.muni.cz.reliability.tool.core.ArgsParser;
import fi.muni.cz.reliability.tool.core.exception.InvalidInputException;
import fi.muni.cz.reliability.tool.dataprocessing.output.CsvFileIssuesWriter;
import fi.muni.cz.reliability.tool.dataprocessing.output.IssuesWriter;
import java.util.Arrays;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class IssuesWriterFactory {
    
    private static final String CSV = "csv";
    
    /**
     * Get IssueWriter for CommandLine argument.
     * 
     * @param parser  parsed CommandLine.
     * @return IssuesWriter
     * @throws InvalidInputException if there is no such format of file.
     */
    public static IssuesWriter getIssuesWriter(ArgsParser parser) throws InvalidInputException {
        switch (parser.getOptionValueSave()) {
            case CSV:
                return new CsvFileIssuesWriter(); 
            default:
                throw new InvalidInputException(Arrays.asList("No such format of file: '" 
                            + parser.getOptionValueSave() + "'"));
        }
    }
}
