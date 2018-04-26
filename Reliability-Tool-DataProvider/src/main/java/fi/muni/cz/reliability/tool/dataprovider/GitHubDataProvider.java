package fi.muni.cz.reliability.tool.dataprovider;

import fi.muni.cz.reliability.tool.dataprovider.mapping.BeanMapping;
import fi.muni.cz.reliability.tool.dataprovider.exception.AuthenticationException;
import fi.muni.cz.reliability.tool.dataprovider.mapping.GitHubBeanMappingImpl;
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
    
    private final BeanMapping beanMapping; 
    private final IssueService issueService;
    
    /**
     * Initialize <code>beanMapping</code> and <code>gitHubClient</code>
     * 
     * @param client GitHubClient with preset authentication data
     */
    public GitHubDataProvider(GitHubClient client) {
        issueService = new IssueService(client);
        beanMapping = new GitHubBeanMappingImpl(); 
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
            Map<String, String> filderdata = new HashMap<String, String>();
            filderdata.put(IssueService.FILTER_STATE, IssueService.STATE_CLOSED);
            List<Issue> issueList = issueService.getIssues(owner, repositoryName, filderdata);
            generalIssueList = beanMapping.mapTo(issueList, GeneralIssue.class);
        } catch (RequestException ex) {
            Logger.getLogger(GitHubDataProvider.class.getName())
                    .log(Level.SEVERE, "Error while getting repository by Owner and Repository name.", ex);
            throw new AuthenticationException("Bad credenrials set. "
                    + "Wrong name or password in authentication file.", ex);
        } catch (IOException ex) {
            Logger.getLogger(GitHubDataProvider.class.getName())
                    .log(Level.SEVERE, "Error while getting repository by Owner and Repository name.", ex);
        }
        Collections.reverse(generalIssueList);
        return generalIssueList;
    }
}
