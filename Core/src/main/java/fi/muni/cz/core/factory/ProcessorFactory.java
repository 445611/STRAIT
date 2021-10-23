package fi.muni.cz.core.factory;

import fi.muni.cz.core.ArgsParser;
import fi.muni.cz.dataprocessing.issuesprocessing.IssuesProcessor;
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
     * @return list of processors as String.
     */
    public static List<String> getProcessorsRanWithInfoAsList(ArgsParser parser) {
        List<String> list = new ArrayList<>();
        for (IssuesProcessor processor: getProcessors(parser)) {
            list.add(processor.infoAboutProcessor());
        }
        return list;
    }
    
    /**
     * Get all processors to run.
     * 
     * @param parser  parsed CommandLine.
     * @return list of Processors.
     */
    public static List<IssuesProcessor> getProcessors(ArgsParser parser) {
        List<IssuesProcessor> listOfProcessors = new ArrayList<>();

        //Implement for processors

        return listOfProcessors;
    }
}
