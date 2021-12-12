package fi.muni.cz.dataprovider;

import fi.muni.cz.dataprovider.authenticationdata.GitHubAuthenticationDataProvider;
import fi.muni.cz.dataprovider.exception.AuthenticationException;
import fi.muni.cz.dataprovider.utils.GitHubUrlParser;
import fi.muni.cz.dataprovider.utils.ParsedUrlData;
import fi.muni.cz.dataprovider.utils.UrlParser;
import org.dozer.DozerBeanMapper;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class GitHubRepositoryInformationDataProvider implements RepositoryInformationDataProvider {

    private final RepositoryService repositoryService;
    private final CommitService commitService;
    private final DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
    private final GitHub github;

    /**
     * Initialize RepositoryService with client.
     *
     * @param client GitHubClient with preset authentication data
     */
    public GitHubRepositoryInformationDataProvider(GitHubClient client) {
        repositoryService = new RepositoryService(client);
        commitService = new CommitService(client);
        try {
            GitHubAuthenticationDataProvider authProvider = new GitHubAuthenticationDataProvider();
            if (authProvider.getOAuthToken() == null || authProvider.getOAuthToken().isEmpty()) {
                github = new GitHubBuilder()
                        .withPassword(authProvider.getUserName(), authProvider.getPassword()).build();
            } else {
                github = new GitHubBuilder().withOAuthToken(authProvider.getOAuthToken()).build();
            }
        } catch (IOException ex) {
            throw new AuthenticationException("Couldn't create GitHub of hub4j.");
        }

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
            // Takes too long for 100k+ commits
            //List<RepositoryCommit> commits = commitService.getCommits(repository);
            repositoryInformation = dozerBeanMapper
                    .map(repository, RepositoryInformation.class);
            repositoryInformation.setPushedAtFirst(repository.getCreatedAt());
            // for linux repository, error is thrown too many contributors
            repositoryInformation.setContributors(repository.getName().equals("linux") ?
                    0 : repositoryService.getContributors(repository, false).size());
            addReleases(owner + "/" + repositoryName, repositoryInformation);
        } catch (RequestException ex) {
            log(Level.SEVERE, "Error while getting repository by Owner and Repository name.", ex);
            throw new AuthenticationException("Bad credentials set. "
                    + "Wrong name or password in authentication file.", ex);
        } catch (IOException ex) {
            log(Level.SEVERE, "Error while getting repository by Owner and Repository name.", ex);
        }

        return repositoryInformation;
    }

    private void addReleases(String repoFullName, RepositoryInformation repositoryInformation) throws IOException {
        github.getRepository(repoFullName).listReleases().forEach(release -> {
            Release r = new Release();
            r.setName(release.getName());
            r.setPublishedAt(release.getPublished_at());
            repositoryInformation.addRelease(r);
        });
    }

    private void log(Level level, String message, Exception ex) {
        Logger.getLogger(GitHubGeneralIssueDataProvider.class.getName())
                .log(level, message, ex);
    }
}
