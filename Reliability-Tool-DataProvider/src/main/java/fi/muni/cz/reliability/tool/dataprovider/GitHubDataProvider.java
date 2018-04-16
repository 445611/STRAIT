package fi.muni.cz.reliability.tool.dataprovider;

import fi.muni.cz.reliability.tool.dataprovider.utils.UrlParserGitHub;
import fi.muni.cz.reliability.tool.dataprovider.utils.UrlParser;
import fi.muni.cz.reliability.tool.dataprovider.mapping.BeanMapping;
import fi.muni.cz.reliability.tool.dataprovider.mapping.BeanMappingImpl;
import fi.muni.cz.reliability.tool.dataprovider.exception.AuthenticationException;
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
    
    private BeanMapping beanMapping;
    
    private GitHubClient gitHubClient;

    /**
     * Initialize <code>beanMapping</code> and <code>gitHubClient</code>
     */
    public GitHubDataProvider() {
        gitHubClient = new GitHubClient();
        beanMapping = new BeanMappingImpl();
    }

    /**
     * Set authentication attributes
     * 
     * @param userName name
     * @param password password
     * @param oAuthToken token
     */
    public GitHubDataProvider(String userName, String password, String oAuthToken) {
        this();
        this.oAuthToken = oAuthToken;
        this.userName = userName;
        this.password = password;
    }
    
    @Override
    public List<GeneralIssue> getIssuesByOwnerRepoName(String owner, String repositoryName) {
        
        loadAuthenticationToClient();
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
    
    @Override
    public List<GeneralIssue> getIssuesByUrl(String urlString) {

            UrlParser parser = new UrlParserGitHub();
            String[] ownerAndRepositoryName = parser.parseUrlAndCheck(urlString);
            return getIssuesByOwnerRepoName(ownerAndRepositoryName[1],
                    ownerAndRepositoryName[2]); 
    }
    
    /**
     * Load credentials from file
     * @throw AuthenticationException if problem loading file
     */
    private void loadAuthenticationToClient() { 
        if (oAuthToken == null || oAuthToken.isEmpty()) {
            gitHubClientSetUserNameAndPassword();
        } else {
            gitHubClientSetOAuthToken();
        }   
    }
    
    /**
     * Set <code>oAuthToken</code> to <code>gitHubClient</code>
     */
    private void gitHubClientSetOAuthToken() {
        gitHubClient.setOAuth2Token(oAuthToken);
    }
    
    /**
     * Set <code>userName</code> and <code>password</code> 
     * to <code>gitHubClient</code>
     */
    private void gitHubClientSetUserNameAndPassword() {
        if (userName == null || password == null) {
            throw new AuthenticationException("OAuthToken + "
                    + "UserName or Password not set.");
        }
        if (userName.isEmpty() || password.isEmpty()) {
            throw new AuthenticationException("OAuthToken or "
                    + "UserName and Password cannot be empty.");
        }
        gitHubClient.setCredentials(userName, password);
    }
}
