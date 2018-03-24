package fi.muni.cz.reliability.tool.dataprovider;

import fi.muni.cz.reliability.tool.core.GeneralIssue;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.RepositoryService;

/**
 * @author Radoslav Micko <445611@muni.cz>
 */
public class GitHubDataProvider implements DataProvider {

    final static String O_AUTH_2_TOKEN = "07d185523c583404fb7aabe851d6c715e5352dc9";
    
    @Override
    public List<GeneralIssue> getIssues(String owner, String repositoryName) {
        
        GitHubClient client = new GitHubClient();
        client.setOAuth2Token(O_AUTH_2_TOKEN);
        IssueService issueService = new IssueService(client);
        
        try {
            List<Issue> issueList = issueService.getIssues(owner, repositoryName, null);
        } catch (IOException ex) {
            Logger.getLogger(GitHubDataProvider.class.getName())
                    .log(Level.SEVERE, "Error while getting repository.");
        }
        
        return null;
    }
    
}
