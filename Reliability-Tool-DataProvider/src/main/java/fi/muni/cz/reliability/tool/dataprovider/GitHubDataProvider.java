package fi.muni.cz.reliability.tool.dataprovider;

import fi.muni.cz.reliability.tool.dataprovider.exception.AuthenticationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Radoslav Micko <445611@muni.cz>
 */
public class GitHubDataProvider implements DataProvider {

    //"07d185523c583404fb7aabe851d6c715e5352dc9"
    
    private String oAuthToken;
    private String userName;
    private String password;
    
    @Autowired
    private BeanMapping beanMapping;
    
    @Autowired
    private GitHubClient gitHubClient;
    
    @Override
    public List<GeneralIssue> getIssuesByOwnerRepoName(String owner, String repositoryName) {
        
        
        IssueService issueService = new IssueService(gitHubClient);
        List<GeneralIssue> generalIssueList = new ArrayList<>();
        
        try {
            List<Issue> issueList = issueService.getIssues(owner, repositoryName, null);
            generalIssueList = beanMapping.mapTo(issueList, GeneralIssue.class);
        } catch (IOException ex) {
            Logger.getLogger(GitHubDataProvider.class.getName())
                    .log(Level.SEVERE, "Error while getting repository by Owner and Repository name.", ex);
        }
        return generalIssueList;
    }
    
    @Override
    public List<GeneralIssue> getIssuesByUrl(String url) {
        GitHubClient client = new GitHubClient();
        client.setOAuth2Token(oAuthToken);
        IssueService issueService = new IssueService(client);
        List<Issue> issueList;
        //TODO
        /*
        try {
            issueList = issueService.getIssues(, null);
        } catch (IOException ex) {
            Logger.getLogger(GitHubDataProvider.class.getName())
                    .log(Level.SEVERE, "Error while getting repository by URL.");
        }
        */
        return null; 
    }

    public void setOAuthToken(String oAuthToken) {
        this.oAuthToken = oAuthToken;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    //TODO
    @Override
    public void authenticate() {
        
        //TODO
        
        if (oAuthToken == null) {
            authenticateByUser();
        } else {
            authenticateByOAuthToken();
        }   
    }
    
    /**
     * 
     */
    private void authenticateByOAuthToken() {
        if (oAuthToken.isEmpty()) {
            throw new AuthenticationException("OAuthToken cannot be empty.");
        }
        gitHubClient.setOAuth2Token(oAuthToken);
    }
    
    /**
     * 
     */
    private void authenticateByUser() {
        if (userName == null || password == null) {
            throw new AuthenticationException("OAuthToken + "
                    + "UserName or Password not set.");
        }
        if (userName.isEmpty() || password.isEmpty()) {
            throw new AuthenticationException("OAuthToken + "
                    + "UserName and Password cannot be empty.");
        }
        gitHubClient.setCredentials(userName, password);
    }
}
