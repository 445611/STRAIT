package fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.modeldata;


import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class CumulativeIssuesCounter implements IssuesCounter {

    private final String typeOfTimeToAdd;
    private final int howManyToAdd;
    private final Date startOfTesting;
    private final Date endOfTesting;
    
    /**
     * Initialize attributes to default value.
     * Defaul value is one week.
     */
    public CumulativeIssuesCounter() {
        typeOfTimeToAdd = WEEKS;
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
    public CumulativeIssuesCounter(String typeOfTimeToAdd, int howManyToAdd, 
            Date startOfTesting, Date endOfTesting) {
        this.typeOfTimeToAdd = typeOfTimeToAdd;
        this.howManyToAdd = howManyToAdd;
        this.startOfTesting = startOfTesting;
        this.endOfTesting = endOfTesting;
    }
    
    @Override
    public List<Pair<Integer, Integer>> prepareIssuesDataForModel(
            List<GeneralIssue> listOfIssues) {
        List<Pair<Integer, Integer>> spreadedIssues = getIntervalIssues(listOfIssues);
        Integer totalNumber = 0;
        List<Pair<Integer, Integer>> listOfTotalIssues = new ArrayList<>();
        for (Pair<Integer, Integer> pair: spreadedIssues) {
            totalNumber = totalNumber + pair.getSecond();
            listOfTotalIssues.add(new Pair(pair.getFirst(), totalNumber));
        }
        return listOfTotalIssues;
    }
    
    private List<Pair<Integer, Integer>> getIntervalIssues(List<GeneralIssue> listOfIssues) {
         IssuesCounter counter = new IntervalIssuesCounter(typeOfTimeToAdd, howManyToAdd, 
                startOfTesting, endOfTesting);
         return counter.prepareIssuesDataForModel(listOfIssues);
    }
}