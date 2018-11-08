package fi.muni.cz.reliability.tool.core.factory;

import fi.muni.cz.reliability.tool.core.ArgsParser;
import fi.muni.cz.reliability.tool.dataprocessing.exception.DataProcessingException;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.Filter;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.FilterByLabel;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.FilterByTime;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.FilterClosed;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class FilterFactory {
    
    private static final List<String> FILTERING_WORDS = Arrays.asList("bug","error","fail","fault","defect");
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    
    /**
     * Get string representation of filters.
     * 
     * @param parser parsed CommandLine.
     * @return List of filters as String.
     */
    public static List<String> getFiltersRanAsList(ArgsParser parser) {
        List<String> list = new ArrayList<>();
        if (parser.hasOptionFilterLabels()) {
            if (parser.getOptionValuesFilterLables() == null) {   
                list.add(new FilterByLabel(FILTERING_WORDS).toString());
            } else {
                list.add(new FilterByLabel(
                        Arrays.asList(parser.getOptionValuesFilterLables())).toString());
            }
        }
        if (parser.hasOptionFilterClosed()) {
            list.add(new FilterClosed().toString());
        }
        if (parser.hasOptionFilterTime()) {
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
            Date startOfTesting = null;
            Date endOfTesting = null;
            try {
                startOfTesting = formatter.parse(parser.getOptionValuesFilterTime()[0]);
                endOfTesting = formatter.parse(parser.getOptionValuesFilterTime()[1]);
            } catch (ParseException ex) {
                throw new DataProcessingException("Wrong format of date. Should match: " + DATE_FORMAT);
            }
            list.add(new FilterByTime(startOfTesting, endOfTesting).toString());
        }
        return list;
    }
    
    /**
     * Get string representation of filters with detail info.
     * 
     * @param parser  parsed CommandLine.
     * @return list of filters as String.
     */
    public static List<String> getFiltersRanWithInfoAsList(ArgsParser parser) {
        List<String> list = new ArrayList<>();
        if (parser.hasOptionFilterLabels()) {
            if (parser.getOptionValuesFilterLables() == null) {   
                list.add(new FilterByLabel(FILTERING_WORDS).infoAboutFilter());
            } else {
                list.add(new FilterByLabel(
                        Arrays.asList(parser.getOptionValuesFilterLables())).infoAboutFilter());
            }
        }
        if (parser.hasOptionFilterClosed()) {
            list.add(new FilterClosed().infoAboutFilter());
        }
        if (parser.hasOptionFilterTime()) {
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
            Date startOfTesting = null;
            Date endOfTesting = null;
            try {
                startOfTesting = formatter.parse(parser.getOptionValuesFilterTime()[0]);
                endOfTesting = formatter.parse(parser.getOptionValuesFilterTime()[1]);
            } catch (ParseException ex) {
                throw new DataProcessingException("Wrong format of date. Should match: " + DATE_FORMAT);
            }
            
            list.add(new FilterByTime(startOfTesting, endOfTesting).infoAboutFilter());
        }
        return list;
    }
    
    /**
     * Get all filters to run.
     * 
     * @param parser  parsed CommandLine.
     * @return list of Filters.
     */
    public static List<Filter> getFilters(ArgsParser parser) {
        List<Filter> listOfFilters = new ArrayList<>();
        if (parser.hasOptionFilterLabels()) {
            if (parser.getOptionValuesFilterLables() == null) {   
                listOfFilters.add(new FilterByLabel(FILTERING_WORDS));
            } else {
                listOfFilters.add(new FilterByLabel(
                        Arrays.asList(parser.getOptionValuesFilterLables())));
            }
        }
        if (parser.hasOptionFilterClosed()) {
            listOfFilters.add(new FilterClosed());
        }
        if (parser.hasOptionFilterTime()) {
            DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
            Date startOfTesting = null;
            Date endOfTesting = null;
            try {
                startOfTesting = formatter.parse(parser.getOptionValuesFilterTime()[0]);
                endOfTesting = formatter.parse(parser.getOptionValuesFilterTime()[1]);
            } catch (ParseException ex) {
                throw new DataProcessingException("Wrong format of date. Should match: " + DATE_FORMAT);
            }
            
            listOfFilters.add(new FilterByTime(startOfTesting, endOfTesting));
        }
        return listOfFilters;
    }
}