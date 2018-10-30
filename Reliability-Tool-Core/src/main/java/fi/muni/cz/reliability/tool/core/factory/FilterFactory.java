package fi.muni.cz.reliability.tool.core.factory;

import fi.muni.cz.reliability.tool.core.ArgsParser;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.Filter;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.FilterByLabel;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.FilterClosed;
import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
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
     * c
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
                list.add(new FilterByLabel(FILTERING_WORDS).toString());
            } else {
                list.add(new FilterByLabel(
                        Arrays.asList(cmdl.getOptionValue(ArgsParser.OPT_FILTER_LABELS))).toString());
            }
        }
        if (cmdl.hasOption(ArgsParser.OPT_FILTER_CLOSED)) {
            list.add(new FilterClosed().infoAboutFilter());
        }
        return list;
    }
    
    /**
     * Run filters that are arguments in CommandLine ove data list.
     * 
     * @param list  to run filters on.
     * @param cmdl  parsed CommandLine.
     * @return list of GeneralIssue
     */
    public static List<GeneralIssue> runFilters(List<GeneralIssue> list, CommandLine cmdl) {
        List<GeneralIssue> filteredList = new ArrayList<>();
        filteredList.addAll(checkFilterByLabel(list, cmdl));
        filteredList = checkFilterClosed(filteredList, cmdl);
        return filteredList;
    }
    
    private static List<GeneralIssue> checkFilterByLabel(List<GeneralIssue> list, CommandLine cmdl) {
        Filter issuesFilter;
        if (cmdl.hasOption(ArgsParser.OPT_FILTER_LABELS)) {
            if (cmdl.getOptionValue(ArgsParser.OPT_FILTER_LABELS) == null) {   
                issuesFilter = new FilterByLabel(FILTERING_WORDS);
            } else {
                issuesFilter = new FilterByLabel(Arrays.asList(cmdl.getOptionValues(ArgsParser.OPT_FILTER_LABELS)));
            }
            return issuesFilter.filter(list);
        }
        return list;
    }
    
    private static List<GeneralIssue> checkFilterClosed(List<GeneralIssue> list, CommandLine cmdl) {
        Filter issuesFilter;
        if (cmdl.hasOption(ArgsParser.OPT_FILTER_CLOSED)) {
            issuesFilter = new FilterClosed();
            return issuesFilter.filter(list);
        }
        return list;
    }
}