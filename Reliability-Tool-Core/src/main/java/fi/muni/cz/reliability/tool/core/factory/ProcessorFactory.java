package fi.muni.cz.reliability.tool.core.factory;

import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.IssuesProcessor;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli.CommandLine;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class ProcessorFactory {
    
    /**
     * Get string representation of processors with detail info.
     * 
     * @param cmdl  parsed CommandLine.
     * @return list of filters as String.
     */
    public static List<String> getProcessorsRanWithInfoAsList(CommandLine cmdl) {
        List<String> list = new ArrayList<>();
        
        //Implement for processors
        
        return list;
    }
    
    /**
     * Get all filters to run.
     * 
     * @param cmdl  parsed CommandLine.
     * @return list of Filters.
     */
    public static List<IssuesProcessor> getProcessors(CommandLine cmdl) {
        List<IssuesProcessor> list = new ArrayList<>();
        
        //Implement for processors
        
        return list;
    }
}
