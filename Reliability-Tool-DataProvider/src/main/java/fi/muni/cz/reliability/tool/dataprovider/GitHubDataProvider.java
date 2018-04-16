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
    
    /*private static final String AUTHENTICATION_FILE_NAME = "git_hub_authentication_file.xml";
    private static final String USER_ELEMENT_TAG = "user";
    private static final String NAME_ELEMENT_TAG = "name";
    private static final String PASSWORD_ELEMENT_TAG = "password";
    private static final String TOKEN_ELEMENT_TAG = "token";*/
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
     * Parse data from authentication file
     * @param document to parse
     * @throw AuthenticationFileErrorException if there occures problem while loading file
     */
    /*private void parseDocumentAndSaveToVariables(Document document) {
        NodeList nodeList = document.getElementsByTagName(USER_ELEMENT_TAG);
        try {
            Node node = nodeList.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                userName = eElement.getElementsByTagName(NAME_ELEMENT_TAG)
                    .item(0).getTextContent();
                password = eElement.getElementsByTagName(PASSWORD_ELEMENT_TAG)
                    .item(0).getTextContent();
                oAuthToken = eElement.getElementsByTagName(TOKEN_ELEMENT_TAG)
                    .item(0).getTextContent();
            } 
        } catch (NullPointerException ex) {
            throw new AuthenticationFileErrorException("Error while parsing "
                    + "authentication file. Wrong format.", ex);
        } 
    }
    */
    /**
     * Get normalized and parsed authentication file
     * @return Document parsed
     * @throw AuthenticationFileErrorException if there occures problem while loading file
     */
    /*private Document getParsedAuthenticationDocument() {
        Document document = null;
        try {
        File authenticationFile = getAuthenticationFile();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        document = documentBuilder.parse(authenticationFile);
        document.getDocumentElement().normalize(); 
        } catch (SAXException ex) { 
            Logger.getLogger(GitHubDataProvider.class.getName()).log(Level.SEVERE, 
                    "Error while parsing authenticationFile.xml file.", ex);
        } catch (IOException ex) {
            Logger.getLogger(GitHubDataProvider.class.getName()).log(Level.SEVERE,
                    "Error loading authenticationFile.xml file from resources.", ex);
            throw new AuthenticationFileErrorException("Error loading "
                    + "authenticationFile.xml file from resources.", ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(GitHubDataProvider.class.getName()).log(Level.SEVERE, 
                    "Error while creating DocumentBuilder.", ex);
        }
        return document; 
    }
    */
    /**
     * Get authentication file from resource
     * @return File
     */
    /*private File getAuthenticationFile() {
        return new File(getClass().getClassLoader()
                .getResource(AUTHENTICATION_FILE_NAME).getFile());
    }*/
    
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
