package fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing;

import fi.muni.cz.reliability.tool.dataprocessing.exception.DataProcessingException;
import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class FilterByLabelNGTest {
    
    private final Filter filterWithNoWords = new FilterByLabel(new ArrayList<>());
    private final Filter filterForWordError = new FilterByLabel(Arrays.asList("error"));
    private final Filter filterForSomeWord = new FilterByLabel(Arrays.asList("some"));
    
    private final List<GeneralIssue> listOfIssues = new ArrayList<>();
    
    @BeforeClass
    public void setUp() {
        GeneralIssue issue = new GeneralIssue();
        issue.setLabels(Arrays.asList("bug", "error", "fault"));
        listOfIssues.add(issue);
        
        issue = new GeneralIssue();
        issue.setLabels(Arrays.asList("otherLabel"));
        listOfIssues.add(issue);
        
        issue = new GeneralIssue();
        issue.setLabels(new ArrayList<>());
        listOfIssues.add(issue);
    }
    
    @Test(expectedExceptions = DataProcessingException.class)
    public void testFilterWithNoWords() {
        filterWithNoWords.filter(listOfIssues);
    }
    
    @Test
    public void testFilterWithWords() {
        assertEquals(filterForWordError.filter(listOfIssues).size(), 1);
        assertEquals(filterForSomeWord.filter(listOfIssues).size(), 0);
    }
}