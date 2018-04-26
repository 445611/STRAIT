package fi.muni.cz.reliability.tool.dataprovider.authenticationdata;

import org.eclipse.egit.github.core.client.GitHubClient;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public interface GitHubAuthenticationDataProvider {
    
    /**
     * Get user name from parsed file.
     * 
     * @return String user name
     */
    String getUserName();
    
    /**
     * Get user password from parsed file.
     * 
     * @return String user password
     */
    String getPassword();
    
    /**
     * Get OAuth 2.0 token from parsed file.
     * 
     * @return String OAuth token
     */
    String getOAuthToken();
    
    /**
     * Get {@link GitHubClient} with preset
     * crediatials.
     * 
     * @return {@link org.eclipse.egit.github.core.client.GitHubClient GitHubClient}
     */
    GitHubClient getGitHubClientWithCreditials();
}
