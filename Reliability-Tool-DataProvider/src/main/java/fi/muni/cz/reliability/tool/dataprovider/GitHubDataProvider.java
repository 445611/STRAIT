package fi.muni.cz.reliability.tool.dataprovider;

import fi.muni.cz.reliability.tool.dataprovider.mapping.BeanMapping;
import fi.muni.cz.reliability.tool.dataprovider.exception.AuthenticationException;
import fi.muni.cz.reliability.tool.dataprovider.mapping.GitHubMapping;
import fi.muni.cz.reliability.tool.dataprovider.utils.GitHubUrlParser;
import fi.muni.cz.reliability.tool.dataprovider.utils.ParsedUrlData;
import fi.muni.cz.reliability.tool.dataprovider.utils.UrlParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.IssueService;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class GitHubDataProvider implements DataProvider {
    
    private static final String MAPPING_FILE = "github_dozer_mapping.xml";
    
    private final BeanMapping beanMapping; 
    private final IssueService issueService;
    
    /**
     * Initialize <code>beanMapping</code> and <code>gitHubClient</code>
     * 
     * @param client GitHubClient with preset authentication data
     */
    public GitHubDataProvider(GitHubClient client) {
        issueService = new IssueService(client);
        beanMapping = new GitHubMapping(MAPPING_FILE); 
    }

    @Override
    public List<GeneralIssue> getIssuesByUrl(String urlString) {
            UrlParser parser = new GitHubUrlParser();
            ParsedUrlData parsedUrlData = parser.parseUrlAndCheck(urlString);
            return getIssuesByOwnerRepoName(parsedUrlData.getUserName(),
                    parsedUrlData.getRepositoryName()); 
    }
    
    /**
     * Get list of issues for owner of specified repository
     * 
     * @param owner             Name of owner
     * @param repositoryName    Name of repositry
     * @return                  list of GeneralIssue
     * @throw  AuthenticationException when there occures problem with authentication
     */
    private List<GeneralIssue> getIssuesByOwnerRepoName(String owner, String repositoryName) {
        List<GeneralIssue> generalIssueList = new ArrayList<>();
        try {
            generalIssueList = getAllGeneralIssues(owner, repositoryName);
        } catch (RequestException ex) {
            log(Level.SEVERE, "Error while getting repository by Owner and Repository name.", ex);
            throw new AuthenticationException("Bad credenrials set. "
                    + "Wrong name or password in authentication file.", ex);
        } catch (IOException ex) {
            log(Level.SEVERE, "Error while getting repository by Owner and Repository name.", ex);
        }
        return generalIssueList;
    }
    
    private List<GeneralIssue> getAllGeneralIssues(String owner, String repositoryName) throws IOException {
        return beanMapping.mapTo(getClosedAndOpenedIssues(owner, repositoryName), GeneralIssue.class);
    }
    
    private List<Issue> getClosedAndOpenedIssues(String owner, String repositoryName) throws IOException {
        List<Issue> allIssues = new ArrayList<>();
        allIssues.addAll(issueService.getIssues(owner, repositoryName, getFilterForClosedIssues()));
        allIssues.addAll(issueService.getIssues(owner, repositoryName, getFilterForOpenedIssues()));
        allIssues.sort((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()));
        return allIssues;
    }
    
    private Map<String, String> getFilterForClosedIssues() {
        Map<String, String> filter = new HashMap<>();
        filter.put(IssueService.FILTER_STATE, IssueService.STATE_CLOSED);
        return filter;
    }
    
    
    private Map<String, String> getFilterForOpenedIssues() {
        Map<String, String> filter = new HashMap<>();
        filter.put(IssueService.FILTER_STATE, IssueService.STATE_OPEN);
        return filter;
    }
    
    
    private void log(Level level, String message, Exception ex) {
        Logger.getLogger(GitHubDataProvider.class.getName())
                    .log(level, message, ex);
    }
}
