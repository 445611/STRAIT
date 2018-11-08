package fi.muni.cz.reliability.tool.core.factory;

import fi.muni.cz.reliability.tool.core.ArgsParser;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.IssuesProcessor;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class ProcessorFactory {
    
    /**
     * Get string representation of processors with detail info.
     * 
     * @param parser  parsed CommandLine.
     * @return list of filters as String.
     */
    public static List<String> getProcessorsRanWithInfoAsList(ArgsParser parser) {
        List<String> list = new ArrayList<>();
        
        //Implement for processors
        
        return list;
    }
    
    /**
     * Get all filters to run.
     * 
     * @param parser  parsed CommandLine.
     * @return list of Filters.
     */
    public static List<IssuesProcessor> getProcessors(ArgsParser parser) {
        List<IssuesProcessor> list = new ArrayList<>();
        
        //Implement for processors
        
        return list;
    }
}
