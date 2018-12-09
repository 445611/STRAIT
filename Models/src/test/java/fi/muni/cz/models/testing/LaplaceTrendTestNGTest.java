package fi.muni.cz.models.testing;

import fi.muni.cz.dataprocessing.issuesprocessing.modeldata.IssuesCounter;
import fi.muni.cz.dataprovider.GeneralIssue;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class LaplaceTrendTestNGTest {

    Calendar cal = new GregorianCalendar();
    private final List<GeneralIssue> listOfIssues = new ArrayList<>();
    private final List<GeneralIssue> decreasingReliabilityListOfIssues = new ArrayList<>();
    private final List<GeneralIssue> increasingReliabilityListOfIssues = new ArrayList<>();
    
    @BeforeClass
    public void setUp() {
        listOfIssues.add(getGeneralIssueWithSetDate(2018, 10, 1, 0, 0, 0));
        
        listOfIssues.add(getGeneralIssueWithSetDate(2018, 10, 2, 0, 0, 0));
        
        listOfIssues.add(getGeneralIssueWithSetDate(2018, 10, 3, 0, 0, 0));

        listOfIssues.add(getGeneralIssueWithSetDate(2018, 10, 4, 0, 0, 0));

        decreasingReliabilityListOfIssues.add(getGeneralIssueWithSetDate(2018, 10, 1, 0, 0, 0));

        decreasingReliabilityListOfIssues.add(getGeneralIssueWithSetDate(2018, 10, 5, 0, 0, 0));

        decreasingReliabilityListOfIssues.add(getGeneralIssueWithSetDate(2018, 10, 7, 0, 0, 0));
        
        decreasingReliabilityListOfIssues.add(getGeneralIssueWithSetDate(2018, 10, 8, 0, 0, 0));

        increasingReliabilityListOfIssues.add(getGeneralIssueWithSetDate(2018, 10, 1, 0, 0, 0));

        increasingReliabilityListOfIssues.add(getGeneralIssueWithSetDate(2018, 10, 2, 0, 0, 0));

        increasingReliabilityListOfIssues.add(getGeneralIssueWithSetDate(2018, 10, 3, 0, 0, 0));
        
        increasingReliabilityListOfIssues.add(getGeneralIssueWithSetDate(2018, 10, 5, 0, 0, 0));

        increasingReliabilityListOfIssues.add(getGeneralIssueWithSetDate(2018, 10, 8, 0, 0, 0));
        
        increasingReliabilityListOfIssues.add(getGeneralIssueWithSetDate(2018, 10, 17, 0, 0, 0));
    }
    
    private GeneralIssue getGeneralIssueWithSetDate(int year, int month, int day, 
            int hours, int minutes, int seconds) {
        GeneralIssue issue = new GeneralIssue();
        cal.set(year, month, day, hours, minutes, seconds);
        Date date = cal.getTime();
        issue.setCreatedAt(date);
        return issue;
    }
    
    @Test
    public void testNoTrend() {
        TrendTest test = new LaplaceTrendTest(IssuesCounter.SECONDS);
        test.executeTrendTest(listOfIssues);
        assertEquals(test.getResult(), false);
        assertTrue(test.getTrendValue() > -1.645);
    } 
    
    @Test
    public void testDecreasingTrend() {
        TrendTest test = new LaplaceTrendTest(IssuesCounter.SECONDS);
        test.executeTrendTest(decreasingReliabilityListOfIssues);
        assertEquals(test.getResult(), false);
        assertTrue(test.getTrendValue() > 1.645);
    }
    
    @Test
    public void testIncreasingTrend() {
        TrendTest test = new LaplaceTrendTest(IssuesCounter.SECONDS);
        test.executeTrendTest(increasingReliabilityListOfIssues);
        assertEquals(test.getResult(), true);
        assertTrue(test.getTrendValue() < -1.645);
    }
}
