package fi.muni.cz.reliability.tool.dataprovider.authenticationdata;

import fi.muni.cz.reliability.tool.dataprovider.GitHubDataProvider;
import fi.muni.cz.reliability.tool.dataprovider.exception.AuthenticationException;
import fi.muni.cz.reliability.tool.dataprovider.exception.AuthenticationFileErrorException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.egit.github.core.client.GitHubClient;


/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class GitHubAuthenticationDataProvider {
    
    private static final String AUTH_FILE_NAME = "git_hub_authentication_file.properties";
    
    private static final String NAME_ELEMENT_TAG = "name";
    private static final String PASSWORD_ELEMENT_TAG = "password";
    private static final String TOKEN_ELEMENT_TAG = "token";
    
    private String userName;
    private String password;
    private String oAuthToken;
    private final GitHubClient gitHubClient;

    /**
     * Constructor that runs <code>loadAuthenticationToClient</code> and 
     * <code>parseDataFromFile</code>.
     */
    public GitHubAuthenticationDataProvider() {
        this.gitHubClient = new GitHubClient();
        parseDataFromAuthenticationFile();
        loadAuthenticationToClient();
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getOAuthToken() {
        return oAuthToken;
    }

    /**
     * Get {@link GitHubClient} with preset
     * crediatials.
     * 
     * @return {@link org.eclipse.egit.github.core.client.GitHubClient GitHubClient}
     */
    public GitHubClient getGitHubClientWithCreditials() {
        return gitHubClient;
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
    
    /**
     * Parse data from authentication file
     * 
     * @return List of authentication data. List={Name, Password, OAuthToken}
     * @throw AuthenticationFileErrorException if there occures problem while loading file
     */
    private void parseDataFromAuthenticationFile() {
        Properties properties = new Properties();
        try (InputStream stream = getClass().getClassLoader()
                .getResourceAsStream(AUTH_FILE_NAME)) {
            properties.load(stream);
            userName = properties.getProperty(NAME_ELEMENT_TAG);
            password = properties.getProperty(PASSWORD_ELEMENT_TAG);
            oAuthToken = properties.getProperty(TOKEN_ELEMENT_TAG);
        } catch (IOException ex) {
            Logger.getLogger(GitHubDataProvider.class.getName())
                    .log(Level.SEVERE, "Error while getting data from " + 
                            AUTH_FILE_NAME, ex);
            throw new AuthenticationFileErrorException("Error while getting data from " + 
                            AUTH_FILE_NAME, ex);
        }   
    }
}
