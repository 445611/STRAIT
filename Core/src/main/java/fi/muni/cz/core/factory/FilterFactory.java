package fi.muni.cz.core.factory;

import fi.muni.cz.core.ArgsParser;
import fi.muni.cz.dataprocessing.exception.DataProcessingException;
import fi.muni.cz.dataprocessing.issuesprocessing.*;

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
     * Get string representation of filters with detail info.
     * 
     * @param parser  parsed CommandLine.
     * @return list of filters as String.
     */
    public static List<String> getFiltersRanWithInfoAsList(ArgsParser parser) {
        List<String> list = new ArrayList<>();
        for (Filter filter: getFilters(parser)) {
            list.add(filter.infoAboutFilter());
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

        if (parser.hasOptionFilterDuplications()) {
            listOfFilters.add(new FilterDuplications());
        }

        if (parser.hasOptionFilterDefects()) {
            listOfFilters.add(new FilterDefects());
        }
        
        return listOfFilters;
    }
}
