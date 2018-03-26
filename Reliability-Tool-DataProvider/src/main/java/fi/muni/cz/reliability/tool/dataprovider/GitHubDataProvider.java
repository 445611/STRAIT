package fi.muni.cz.reliability.tool.dataprovider;

import fi.muni.cz.reliability.tool.core.GeneralIssue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dozer.DozerBeanMapper;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;

/**
 * @author Radoslav Micko <445611@muni.cz>
 */
public class GitHubDataProvider implements DataProvider {

    static final String O_AUTH_2_TOKEN = "07d185523c583404fb7aabe851d6c715e5352dc9";
    private DozerBeanMapper mapper = new DozerBeanMapper();
    
    @Override
    public List<GeneralIssue> getIssuesByOwnerRepoName(String owner, String repositoryName) {
        GitHubClient client = new GitHubClient();
        client.setOAuth2Token(O_AUTH_2_TOKEN);
        IssueService issueService = new IssueService(client);
        List<GeneralIssue> generalIssueList = new ArrayList<>();
        
        try {
            List<Issue> issueList = issueService.getIssues(owner, repositoryName, null);
            mapper.setMappingFiles(Arrays.asList("github_mapping.xml"));
            generalIssueList = mapTo(issueList, GeneralIssue.class);
        } catch (IOException ex) {
            Logger.getLogger(GitHubDataProvider.class.getName())
                    .log(Level.SEVERE, "Error while getting repository by Owner and Repository name.");
        }
        return generalIssueList;
    }
    
    @Override
    public List<GeneralIssue> getIssuesByUrl(String url) {
        GitHubClient client = new GitHubClient();
        client.setOAuth2Token(O_AUTH_2_TOKEN);
        IssueService issueService = new IssueService(client);
        List<Issue> issueList;
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
    
    
    public  <T> List<T> mapTo(Collection<?> objects, Class<T> mapToClass) {
        List<T> mappedCollection = new ArrayList<>();
        for (Object object : objects) {
            mappedCollection.add(mapper.map(object, mapToClass));
        }
        return mappedCollection;
    }
    
}
