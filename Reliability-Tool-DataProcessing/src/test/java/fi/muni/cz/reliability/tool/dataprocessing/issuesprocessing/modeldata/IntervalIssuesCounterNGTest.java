package fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.modeldata;

import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class IntervalIssuesCounterNGTest {

    private final List<GeneralIssue> listOfIssues = new ArrayList<>();
    
    private IssuesCounter counter = new IntervalIssuesCounter();
    
    @BeforeClass
    public void setUp() {
        GeneralIssue issue = new GeneralIssue();
        
        Calendar cal = Calendar.getInstance();
        cal.set(2018, 10, 1, 0, 0, 0);
        Date date = cal.getTime();
        issue.setCreatedAt(date);
        listOfIssues.add(issue);
        
        issue = new GeneralIssue();
        cal.set(2018, 10, 2, 0, 0, 0);
        date = cal.getTime();
        issue.setCreatedAt(date);
        listOfIssues.add(issue);
        
        issue = new GeneralIssue();
        cal.set(2018, 10, 3, 0, 0, 0);
        date = cal.getTime();
        issue.setCreatedAt(date);
        listOfIssues.add(issue);
        
        issue = new GeneralIssue();
        cal.set(2018, 10, 4, 0, 0, 0);
        date = cal.getTime();
        issue.setCreatedAt(date);
        listOfIssues.add(issue);
        issue = new GeneralIssue();
        
        cal.set(2018, 10, 8, 0, 0, 0);
        date = cal.getTime();
        issue.setCreatedAt(date);
        listOfIssues.add(issue);
        
    }
    
    @Test
    public void testIntervalIssuesCounter() {
        assertEquals(counter.prepareIssuesDataForModel(listOfIssues).get(0).getSecond(), new Integer(4));
        assertEquals(counter.prepareIssuesDataForModel(listOfIssues).get(1).getSecond(), new Integer(1));
    } 
}