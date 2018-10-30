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
    
    public static List<String> getFiltersRanAsList(CommandLine cmdl) {
        List<String> list = new ArrayList<>();
        if (cmdl.hasOption(ArgsParser.OPT_FILTER_LABELS)) {
            list.add(new FilterByLabel(new ArrayList<>()).toString());
        } else if (cmdl.hasOption(ArgsParser.OPT_FILTER_CLOSED)) {
            list.add(new FilterClosed().toString());
        }
        return list;
    }
    
    public static List<String> getFiltersRanWithInfoAsList(CommandLine cmdl) {
        List<String> list = new ArrayList<>();
        if (cmdl.hasOption(ArgsParser.OPT_FILTER_LABELS)) {
            list.add(new FilterByLabel(new ArrayList<>()).infoAboutFilter());
        } else if (cmdl.hasOption(ArgsParser.OPT_FILTER_CLOSED)) {
            list.add(new FilterClosed().infoAboutFilter());
        }
        return list;
    }
    
    public static List<GeneralIssue> runFilters(List<GeneralIssue> list, CommandLine cmdl) {
        checkFilterByLabel(list, cmdl);
        checkFilterClosed(list, cmdl);
        return list;
    }
    
    private static void checkFilterByLabel(List<GeneralIssue> list, CommandLine cmdl) {
        Filter issuesFilter;
        if (cmdl.hasOption(ArgsParser.OPT_FILTER_LABELS)) {
            if (cmdl.getOptionValue(ArgsParser.OPT_FILTER_LABELS) == null) {   
                issuesFilter = new FilterByLabel(FILTERING_WORDS);
            } else {
                issuesFilter = new FilterByLabel(Arrays.asList(cmdl.getOptionValues(ArgsParser.OPT_FILTER_LABELS)));
            }
            list = issuesFilter.filter(list);
        }
    }
    
    private static void checkFilterClosed(List<GeneralIssue> list, CommandLine cmdl) {
        Filter issuesFilter;
        if (cmdl.hasOption(ArgsParser.OPT_FILTER_CLOSED)) {
            issuesFilter = new FilterClosed();
            list = issuesFilter.filter(list);
        }
    }
}