package fi.muni.cz.reliability.tool.core.factory;

import fi.muni.cz.reliability.tool.core.ArgsParser;
import fi.muni.cz.reliability.tool.dataprocessing.output.CsvFileIssuesWriter;
import fi.muni.cz.reliability.tool.dataprocessing.output.IssuesWriter;
import org.apache.commons.cli.CommandLine;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class IssuesWriterFactory {
    
    private static final String CSV = "csv";
    
    /**
     * Get IssueWriter for CommandLine argument.
     * 
     * @param cmdl  parsed CommandLine.
     * @return IssuesWriter
     */
    public static IssuesWriter getIssuesWriter(CommandLine cmdl) {
        switch (cmdl.getOptionValue(ArgsParser.OPT_SAVE)) {
            case CSV:
                return new CsvFileIssuesWriter(); 
            default:
                return new CsvFileIssuesWriter();
        }
    }
}
