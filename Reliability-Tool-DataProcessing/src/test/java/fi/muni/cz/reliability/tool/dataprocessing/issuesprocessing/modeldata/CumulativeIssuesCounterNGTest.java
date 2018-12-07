package fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.modeldata;

import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.util.Pair;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class CumulativeIssuesCounterNGTest {

    private final List<GeneralIssue> listOfIssues = new ArrayList<>();
    private final List<Pair<Integer, Integer>> listOfPairs = new ArrayList<>();  
    
    @Mock
    private IssuesCounter counter;

    @InjectMocks
    private IssuesCounter cumulativeIssuesCounter = new CumulativeIssuesCounter();
    
    @BeforeClass
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        GeneralIssue issue = new GeneralIssue();
        listOfIssues.add(issue);
        
        issue = new GeneralIssue();
        listOfIssues.add(issue);
        
        Pair<Integer, Integer> pair = new Pair<>(1, 1);
        listOfPairs.add(pair);
        pair = new Pair<>(2, 1);
        listOfPairs.add(pair);
        pair = new Pair<>(3, 0);
        listOfPairs.add(pair);
        pair = new Pair<>(4, 0);
        listOfPairs.add(pair);
        
    }
    
    @Test
    public void testCumulativeIssuesCounter() {
        when(counter.prepareIssuesDataForModel(listOfIssues)).thenReturn(listOfPairs);
        assertEquals(cumulativeIssuesCounter.prepareIssuesDataForModel(listOfIssues).get(0).getSecond(), 
                new Integer(1));
        assertEquals(cumulativeIssuesCounter.prepareIssuesDataForModel(listOfIssues).get(1).getSecond(), 
                new Integer(2));
        assertEquals(cumulativeIssuesCounter.prepareIssuesDataForModel(listOfIssues).get(3).getSecond(), 
                new Integer(2));
    } 
}