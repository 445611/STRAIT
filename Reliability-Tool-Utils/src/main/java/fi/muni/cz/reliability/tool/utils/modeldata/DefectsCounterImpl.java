package fi.muni.cz.reliability.tool.utils.modeldata;

import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import fi.muni.cz.reliability.tool.utils.Tuple;
import fi.muni.cz.reliability.tool.utils.exception.UtilsException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author Radoslav Micko <445611@muni.cz>
 */
public class DefectsCounterImpl implements DefectsCounter {

    private final int typeOfTimeToAdd;
    private final int howManyToAdd;
    private Date startOfTesting;
    private Date endOfTesting;
    
    /**
     * Initialize attributes to default value.
     * Defaul value is one week.
     */
    public DefectsCounterImpl() {
        typeOfTimeToAdd = Calendar.WEEK_OF_MONTH;
        howManyToAdd = 1;
        this.startOfTesting = null;
        this.endOfTesting = null;
    }
    
    /**
     * Initialize attributes to certain values.
     * @param typeOfTimeToAdd type of Calendar enum
     * @param howManyToAdd number of time parts to add 
     * @param startOfTesting date when testing started
     * @param endOfTesting date when testing ended
     */
    public DefectsCounterImpl(int typeOfTimeToAdd, int howManyToAdd, 
            Date startOfTesting, Date endOfTesting) {
        this.typeOfTimeToAdd = typeOfTimeToAdd;
        this.howManyToAdd = howManyToAdd;
        this.startOfTesting = startOfTesting;
        this.endOfTesting = endOfTesting;
    }
    
    @Override
    public List<Tuple<Integer, Integer>> countDefectsIntoPeriodsOfTime(List<GeneralIssue> listOfIssues) {
        if (listOfIssues == null) {
            throw new NullPointerException("listOfIssues is null.");
        }
        if (startOfTesting == null) {
            startOfTesting = getDateFromMidNight(listOfIssues.get(0).getCreatedAt());
        }
        if (endOfTesting == null) {
            endOfTesting = new Date();
        }
        return sortingIssuesIntoTimePeriods(listOfIssues);
    }      
    
    /**
     * Get list of Tuple of week and number of defects.
     * @param startDate Date to starts counting weeks
     * @param listOfIssues List to iterate over
     * @return List of Tuples 
     * 
     * @throw UtilsException when there are no issues in testing period
     */
    private List<Tuple<Integer, Integer>> sortingIssuesIntoTimePeriods(
            List<GeneralIssue> listOfIssues) {
        Date startOfTestingPeriod = startOfTesting;
        Date endOfTestingPeriod = addSpecificTimeToDate(startOfTesting);
        
        
        List<Tuple<Integer, Integer>> countedList = new ArrayList<>();
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
                throw new UtilsException("No issues in testing period.");
            }
            
            if (issue.getCreatedAt().after(endOfTestingPeriod)) {
                countedList.add(new Tuple<>(periodsCounter, defectsCounter));
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
                    countedList.add(new Tuple<>(periodsCounter, defectsCounter));
                    startOfTestingPeriod = endOfTestingPeriod;
                    endOfTestingPeriod = addSpecificTimeToDate(endOfTestingPeriod);
                    while (endOfTestingPeriod.before(endOfTesting)) {
                        periodsCounter++;
                        countedList.add(new Tuple<>(periodsCounter, 0));
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
        c.add(typeOfTimeToAdd, howManyToAdd);
        return c.getTime(); 
    }    
    
    /**
     * Calculate monday of next week after date
     * @param date to calculate for
     * @return Date of monday
     */
    /*private Date findFirstDayOfNextWeekAfterDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        int daysToAdd;
        if (day == 2) {
            daysToAdd = 7;
        } else {
            daysToAdd = (9 - day) % 7;
        }
                
        cal.add(Calendar.DAY_OF_WEEK, daysToAdd);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set( Calendar.MINUTE, 0);
        cal.set( Calendar.SECOND, 0);
        cal.set( Calendar.MILLISECOND, 0);
        
        return cal.getTime();
    }*/
}
