package fi.muni.cz.reliability.tool.utils.modeldata;

import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import fi.muni.cz.reliability.tool.utils.Tuple;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author Radoslav Micko <445611@muni.cz>
 */
public class DefectsCounterImpl implements DefectsCounter {

    @Override
    public List<Tuple<Integer, Integer>> countDefectsForWeeks(List<GeneralIssue> listOfIssues) {
        if (listOfIssues == null || listOfIssues.isEmpty()) {
            return null;
        }
        Date start = getDateFromMidNight(listOfIssues.get(0).getCreatedAt());
        return sortingIssuesIntoWeeks(start, listOfIssues);
    }      
    
    /**
     * Get list of Tuple of week and number of defects.
     * @param startDate Date to starts counting weeks
     * @param listOfIssues List to iterate over
     * @return List of Tuples 
     */
    private List<Tuple<Integer, Integer>> sortingIssuesIntoWeeks(Date startDate, 
            List<GeneralIssue> listOfIssues) {
        //whitch to USE
        //Date afterWeek = addOneWeekToDate(start);
        Date firstDayNextWeek = findFirstDayOfNextWeekAfterDate(startDate);
        //whitch to USE
        
        List<Tuple<Integer, Integer>> countedList = new ArrayList<>();
        int weekCounter = 1;
        int defectsCounter = 0;
        
        Iterator<GeneralIssue> issuesIterator = listOfIssues.iterator();
        GeneralIssue issue = issuesIterator.next();
        while (startDate.before(new Date())) {
            if (issue == null) {
                break;
            }
            if (issue.getCreatedAt().after(startDate) && 
                    issue.getCreatedAt().before(firstDayNextWeek)) {
                defectsCounter++;
                if (issuesIterator.hasNext()) {
                    issue = issuesIterator.next();
                } else {
                    countedList.add(new Tuple<>(weekCounter, defectsCounter));
                    break;
                }
            } else {
                countedList.add(new Tuple<>(weekCounter, defectsCounter));
                weekCounter++;
                defectsCounter = 0;
                startDate = firstDayNextWeek;
                firstDayNextWeek = findFirstDayOfNextWeekAfterDate(startDate);
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
     * Calculate monday of next week after date
     * @param date to calculate for
     * @return Date of monday
     */
    private Date findFirstDayOfNextWeekAfterDate(Date date) {
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
    }
    
    /**
     * Calculate date after one week (7 days)
     * @param date to add to
     * @return Date after week
     */
    private Date addOneWeekToDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.WEEK_OF_MONTH, 1);
        return c.getTime(); 
    }    
}
