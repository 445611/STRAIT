package fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.modeldata;

import fi.muni.cz.reliability.tool.dataprocessing.exception.DataProcessingException;
import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class IntervalIssuesCounter implements IssuesCounter {

    private final String typeOfTimeToAdd;
    private final int howManyToAdd;
    private Date startOfTesting;
    private Date endOfTesting;

    /**
     * Initialize attributes to default value.
     * Defaul value is one week.
     */
    public IntervalIssuesCounter() {
        typeOfTimeToAdd = WEEKS;
        howManyToAdd = 1;
        this.startOfTesting = null;
        this.endOfTesting = null;
    }

    /**
     * Initialize attributes to certain values.
     * @param typeOfTimeToAdd type of Calendar enum.
     * @param howManyToAdd number of time parts to add.
     * @param startOfTesting date when testing started.
     * @param endOfTesting date when testing ended.
     */
    public IntervalIssuesCounter(String typeOfTimeToAdd, int howManyToAdd, 
            Date startOfTesting, Date endOfTesting) {
        this.typeOfTimeToAdd = typeOfTimeToAdd;
        this.howManyToAdd = howManyToAdd;
        this.startOfTesting = startOfTesting;
        this.endOfTesting = endOfTesting;
    }

    @Override
    public List<Pair<Integer, Integer>> prepareIssuesDataForModel(List<GeneralIssue> rawIssues) {
        if (rawIssues == null || rawIssues.isEmpty()) {
            throw new NullPointerException("listOfIssues is null or empty.");
        }
        if (startOfTesting == null) {
            startOfTesting = getDateFromMidNight(rawIssues.get(0).getCreatedAt());
        }
        if (endOfTesting == null) {
            endOfTesting = new Date();
        }
        return sortingIssuesIntoTimePeriods(rawIssues);
    }
    
    /**
     * Get list of Pair of week and number of defects.
     * @param startDate Date to starts counting weeks
     * @param listOfIssues List to iterate over
     * @return List of Pairs 
     * 
     * @throw UtilsException when there are no issues in testing period
     */
    private List<Pair<Integer, Integer>> sortingIssuesIntoTimePeriods(
            List<GeneralIssue> listOfIssues) {
        Date startOfTestingPeriod = startOfTesting;
        Date endOfTestingPeriod = addSpecificTimeToDate(startOfTesting);

        List<Pair<Integer, Integer>> countedList = new ArrayList<>();
        int periodsCounter = 1;
        int defectsCounter = 0;
        
        Iterator<GeneralIssue> issuesIterator = listOfIssues.iterator();
        GeneralIssue issue = issuesIterator.next();
        while (endOfTestingPeriod.before(endOfTesting)) {
            
            if (issue.getCreatedAt().before(startOfTestingPeriod)) {
                if (issuesIterator.hasNext()) {
                    issue = issuesIterator.next();
                    continue;
                }
                throw new DataProcessingException("No issues in testing period.");
            }
            
            if (issue.getCreatedAt().after(endOfTestingPeriod)) {
                countedList.add(new Pair<>(periodsCounter, defectsCounter));
                periodsCounter++;
                defectsCounter = 0;
                startOfTestingPeriod = endOfTestingPeriod;
                endOfTestingPeriod = addSpecificTimeToDate(endOfTestingPeriod);
                continue;
            }
            
            if (issue.getCreatedAt().after(startOfTestingPeriod) && 
                    issue.getCreatedAt().before(endOfTestingPeriod)) {
                defectsCounter++;
                if (issuesIterator.hasNext()) {
                    issue = issuesIterator.next();
                } else {
                    countedList.add(new Pair<>(periodsCounter, defectsCounter));
                    startOfTestingPeriod = endOfTestingPeriod;
                    endOfTestingPeriod = addSpecificTimeToDate(endOfTestingPeriod);
                    while (endOfTestingPeriod.before(endOfTesting)) {
                        periodsCounter++;
                        countedList.add(new Pair<>(periodsCounter, 0));
                        startOfTestingPeriod = endOfTestingPeriod;
                        endOfTestingPeriod = addSpecificTimeToDate(endOfTestingPeriod);
                    }
                }
            } 
        }
        return countedList;
    }
    
    /**
     * Get date set to midnight
     * @param date update
     * @return Date updated
     */
    private Date getDateFromMidNight(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set( Calendar.MINUTE, 0);
        cal.set( Calendar.SECOND, 0);
        cal.set( Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
    /**
     * Calculate date after <code>howManyToAdd</code> <code>typeOfTimeToAdd</code>
     * units of time.
     * 
     * @param date to add to
     * @return Date after week
     */
    private Date addSpecificTimeToDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        switch (typeOfTimeToAdd) {
            case SECONDS:
                    c.add(Calendar.SECOND, howManyToAdd);
                    break;
                case MINUTES:
                    c.add(Calendar.MINUTE, howManyToAdd);
                    break;
                case HOURS:
                    c.add(Calendar.HOUR, howManyToAdd);
                    break;
                case DAYS:
                    c.add(Calendar.DAY_OF_MONTH, howManyToAdd);
                    break;
                case WEEKS:
                    c.add(Calendar.WEEK_OF_MONTH, howManyToAdd);
                    break;
                case YEARS:
                    c.add(Calendar.YEAR, howManyToAdd);
                    break;
        }
        return c.getTime(); 
    }  
}