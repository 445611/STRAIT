package fi.muni.cz.reliability.tool.dataprovider;

import fi.muni.cz.reliability.tool.dataprovider.utils.UrlParserGitHub;
import fi.muni.cz.reliability.tool.dataprovider.utils.UrlParser;
import fi.muni.cz.reliability.tool.dataprovider.mapping.BeanMapping;
import fi.muni.cz.reliability.tool.dataprovider.exception.AuthenticationException;
import fi.muni.cz.reliability.tool.dataprovider.mapping.GitHubBeanMappingImpl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.IssueService;

/**
 * @author Radoslav Micko <445611@muni.cz>
 */
public class GitHubDataProvider implements DataProvider {
    
    private String oAuthToken;
    private String userName;
    private String password;
    
    private final BeanMapping beanMapping;
    
    private final GitHubClient gitHubClient;

    /**
     * Initialize <code>beanMapping</code> and <code>gitHubClient</code>
     * 
     * @param client GitHubClient with preset authentication data
     */
    public GitHubDataProvider(GitHubClient client) {
        gitHubClient = client;
        beanMapping = new GitHubBeanMappingImpl();
    }

    @Override
    public List<GeneralIssue> getIssuesByUrl(String urlString) {

            UrlParser parser = new UrlParserGitHub();
            String[] ownerAndRepositoryName = parser.parseUrlAndCheck(urlString);
            return getIssuesByOwnerRepoName(ownerAndRepositoryName[1],
                    ownerAndRepositoryName[2]); 
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
        
        IssueService issueService = new IssueService(gitHubClient);
        List<GeneralIssue> generalIssueList = new ArrayList<>();
        
        try {
            List<Issue> issueList = issueService.getIssues(owner, repositoryName, null);
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
