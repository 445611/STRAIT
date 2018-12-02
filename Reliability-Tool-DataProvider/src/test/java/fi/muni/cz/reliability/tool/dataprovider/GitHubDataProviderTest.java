package fi.muni.cz.reliability.tool.dataprovider;


import java.io.IOException;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.egit.github.core.Issue;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;
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
public class GitHubDataProviderTest {
    
    @Mock
    IssueService issueService;

    @InjectMocks
    private GitHubDataProvider provider = new GitHubDataProvider(new GitHubClient());
    
    @BeforeClass
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    /*@Mock
    private BeanMapping beanMapping;
    @Mock
    private IssueService issueService;
    @Mock
    private UrlParser parser = new GitHubUrlParser();
            
    @InjectMocks
    private GitHubDataProvider provider = new GitHubDataProvider(new GitHubClient());
    
    @BeforeClass
    public void setUpClass() {
        MockitoAnnotations.initMocks(this);
    }
*/
    /**
     * Test of getIssuesByUrl method, of class GitHubDataProvider.
     * @throws java.io.IOException Exception
     */
    /*@Test
    public void testGetIssuesByUrl() throws IOException {
        Map<String, String> filterClosed = new HashMap<>();
        filterClosed.put(IssueService.FILTER_STATE, IssueService.STATE_CLOSED);
        Map<String, String> folterOpened = new HashMap<>();
        folterOpened.put(IssueService.FILTER_STATE, IssueService.STATE_CLOSED);
        List<Issue> list = new ArrayList<>();
        List<GeneralIssue> listReturn = new ArrayList<>();
        //when(beanMapping.mapTo(list,GeneralIssue.class)).thenReturn(Arrays.asList(new GeneralIssue()));
        when(issueService.getIssues("User", "Repository", filterClosed)).thenReturn(list);
        when(issueService.getIssues("User", "Repository", folterOpened)).thenReturn(list);
        when(parser.parseUrlAndCheck("https://github.com/445611/PA165-project"))
               .thenReturn(new ParsedUrlData(new URL("https://github.com/445611/PA165-project"), "User", "Repository"));
        List<GeneralIssue> result = provider.getIssuesByUrl("https://github.com/445611/PA165-project");
        verify(beanMapping).mapTo(list,GeneralIssue.class);
    }*/
    
    @Test
    public void testGetIssuesByUrl() throws IOException {
        Map<String, String> filter = new HashMap<>();
        filter.put(IssueService.FILTER_STATE, IssueService.STATE_CLOSED);
        when(issueService.getIssues("445611", "PA165", filter)).thenReturn(Arrays.asList(new Issue().setCreatedAt(new Date())));
        filter = new HashMap<>();
        filter.put(IssueService.FILTER_STATE, IssueService.STATE_OPEN);
        when(issueService.getIssues("445611", "PA165", filter)).thenReturn(Arrays.asList(new Issue().setCreatedAt(new Date())));
        assertEquals(2, provider.getIssuesByUrl("https://github.com/445611/PA165").size());
    }
}
