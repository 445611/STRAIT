package fi.muni.cz.reliability.tool.core.factory;

import fi.muni.cz.reliability.tool.core.ArgsParser;
import fi.muni.cz.reliability.tool.dataprocessing.output.HtmlOutputWriter;
import fi.muni.cz.reliability.tool.dataprocessing.output.OutputWriter;
import org.apache.commons.cli.CommandLine;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class OutputWriterFactory {
    
    private static final String HTML = "html";
    
    /**
     * Get OutputWriter for CommandLine argument.
     * 
     * @param cmdl  parsed CommandLine.
     * @return OutputWriter
     */
    public static OutputWriter getIssuesWriter(CommandLine cmdl) {
        if (!cmdl.hasOption(ArgsParser.OPT_OUT)) {
            return checkMultipleGraphs(cmdl);
        } else {
            switch (cmdl.getOptionValue(ArgsParser.OPT_OUT)) {
                case HTML:
                    return checkMultipleGraphs(cmdl);
                default:
                    return new HtmlOutputWriter(false);
            }
        }
    }
    
    private static OutputWriter checkMultipleGraphs(CommandLine cmdl) {
        if (cmdl.hasOption(ArgsParser.OPT_GRAPH_MULTIPLE)) {
            return new HtmlOutputWriter(true);
        } else {
            return new HtmlOutputWriter(false);
        }
    }
}
