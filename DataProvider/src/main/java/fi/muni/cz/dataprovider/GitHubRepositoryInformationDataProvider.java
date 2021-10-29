package fi.muni.cz.dataprovider;

import fi.muni.cz.dataprovider.exception.AuthenticationException;
import fi.muni.cz.dataprovider.utils.GitHubUrlParser;
import fi.muni.cz.dataprovider.utils.ParsedUrlData;
import fi.muni.cz.dataprovider.utils.UrlParser;
import org.dozer.DozerBeanMapper;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class GitHubRepositoryInformationDataProvider implements RepositoryInformationDataProvider {

    private RepositoryService repositoryService;
    private  DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();

    /**
     * Initialize RepositoryService with client.
     *
     * @param client GitHubClient with preset authentication data
     */
    public GitHubRepositoryInformationDataProvider(GitHubClient client) {
        repositoryService = new RepositoryService(client);
    }

    @Override
    public RepositoryInformation getRepositoryInformation(String url) {
        UrlParser parser = new GitHubUrlParser();
        ParsedUrlData parsedUrlData = parser.parseUrlAndCheck(url);
        return getRepositoryInformationOwnerRepoName(parsedUrlData.getUserName(), parsedUrlData.getRepositoryName());
    }

    private RepositoryInformation getRepositoryInformationOwnerRepoName(String owner, String repositoryName) {
        RepositoryInformation repositoryInformation = null;
        try {
            System.out.println("Downloading repository information ...");
            Repository repository = repositoryService.getRepository(owner, repositoryName);
            repositoryInformation = dozerBeanMapper
                    .map(repository, RepositoryInformation.class);
            repositoryInformation.setContributors(repositoryService.getContributors(repository, false).size());
        } catch (RequestException ex) {
            log(Level.SEVERE, "Error while getting repository by Owner and Repository name.", ex);
            throw new AuthenticationException("Bad credentials set. "
                    + "Wrong name or password in authentication file.", ex);
        } catch (IOException ex) {
            log(Level.SEVERE, "Error while getting repository by Owner and Repository name.", ex);
        }

        return repositoryInformation;
    }

    private void log(Level level, String message, Exception ex) {
        Logger.getLogger(GitHubGeneralIssueDataProvider.class.getName())
                .log(level, message, ex);
    }
}
