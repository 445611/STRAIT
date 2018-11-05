package fi.muni.cz.reliability.tool.core.factory;

import fi.muni.cz.reliability.tool.core.ArgsParser;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.Filter;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.FilterByLabel;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.FilterClosed;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.cli.CommandLine;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class FilterFactory {
    
    private static final List<String> FILTERING_WORDS = Arrays.asList("bug","error","fail","fault","defect");
    
    /**
     * Get string representation of filters.
     * 
     * @param cmdl parsed CommandLine.
     * @return List of filters as String.
     */
    public static List<String> getFiltersRanAsList(CommandLine cmdl) {
        List<String> list = new ArrayList<>();
        if (cmdl.hasOption(ArgsParser.OPT_FILTER_LABELS)) {
            if (cmdl.getOptionValue(ArgsParser.OPT_FILTER_LABELS) == null) {   
                list.add(new FilterByLabel(FILTERING_WORDS).toString());
            } else {
                list.add(new FilterByLabel(
                        Arrays.asList(cmdl.getOptionValue(ArgsParser.OPT_FILTER_LABELS))).toString());
            }
        }
        if (cmdl.hasOption(ArgsParser.OPT_FILTER_CLOSED)) {
            list.add(new FilterClosed().toString());
        }
        return list;
    }
    
    /**
     * Get string representation of filters with detail info.
     * 
     * @param cmdl  parsed CommandLine.
     * @return list of filters as String.
     */
    public static List<String> getFiltersRanWithInfoAsList(CommandLine cmdl) {
        List<String> list = new ArrayList<>();
        if (cmdl.hasOption(ArgsParser.OPT_FILTER_LABELS)) {
            if (cmdl.getOptionValue(ArgsParser.OPT_FILTER_LABELS) == null) {   
                list.add(new FilterByLabel(FILTERING_WORDS).infoAboutFilter());
            } else {
                list.add(new FilterByLabel(
                        Arrays.asList(cmdl.getOptionValue(ArgsParser.OPT_FILTER_LABELS))).infoAboutFilter());
            }
        }
        if (cmdl.hasOption(ArgsParser.OPT_FILTER_CLOSED)) {
            list.add(new FilterClosed().infoAboutFilter());
        }
        return list;
    }
    
    /**
     * Get all filters to run.
     * 
     * @param cmdl  parsed CommandLine.
     * @return list of Filters.
     */
    public static List<Filter> getFilters(CommandLine cmdl) {
        List<Filter> listOfFilters = new ArrayList<>();
        if (cmdl.hasOption(ArgsParser.OPT_FILTER_LABELS)) {
            if (cmdl.getOptionValue(ArgsParser.OPT_FILTER_LABELS) == null) {   
                listOfFilters.add(new FilterByLabel(FILTERING_WORDS));
            } else {
                listOfFilters.add(new FilterByLabel(
                        Arrays.asList(cmdl.getOptionValues(ArgsParser.OPT_FILTER_LABELS))));
            }
        }
        if (cmdl.hasOption(ArgsParser.OPT_FILTER_CLOSED)) {
            listOfFilters.add(new FilterClosed());
        }
        return listOfFilters;
    }
}