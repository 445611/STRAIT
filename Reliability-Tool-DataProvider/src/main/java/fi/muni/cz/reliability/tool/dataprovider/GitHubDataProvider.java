package fi.muni.cz.reliability.tool.dataprovider;

import fi.muni.cz.reliability.tool.dataprovider.exception.AuthenticationException;
import fi.muni.cz.reliability.tool.dataprovider.exception.AuthenticationFileErrorException;
import fi.muni.cz.reliability.tool.dataprovider.exception.DataProviderException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Radoslav Micko <445611@muni.cz>
 */
public class GitHubDataProvider implements DataProvider {
    
    private static final String AUTHENTICATION_FILE_NAME = "git_hub_authentication_file.xml";
    private static final String USER_ELEMENT_TAG = "user";
    private static final String NAME_ELEMENT_TAG = "name";
    private static final String PASSWORD_ELEMENT_TAG = "password";
    private static final String TOKEN_ELEMENT_TAG = "token";
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
        } catch (RequestException ex) {
            Logger.getLogger(GitHubDataProvider.class.getName())
                    .log(Level.SEVERE, "Error while getting repository by Owner and Repository name.", ex);
            throw new AuthenticationException("Bad credenrials set. "
                    + "Wrong name or password in authentication file.", ex);
        } catch (IOException ex) {
            Logger.getLogger(GitHubDataProvider.class.getName())
                    .log(Level.SEVERE, "Error while getting repository by Owner and Repository name.", ex);
        }
        return generalIssueList;
    }
    
    @Override
    public List<GeneralIssue> getIssuesByUrl(String urlString) {

        try { 
            URL url = new URL(urlString);
            String[] ownerAndRepositoryName = parseUrlAndCheck(url);
            return getIssuesByOwnerRepoName(ownerAndRepositoryName[1],
                    ownerAndRepositoryName[2]);
        } catch (MalformedURLException ex) {
            Logger.getLogger(GitHubDataProvider.class.getName()).log(Level.SEVERE, "Incorrect URL.", ex);
            throw new DataProviderException("Incorrect URL.");
        }   
    }
     
    @Override
    public void loadAuthenticationDataFromFile() {
        Document document = getParsedAuthenticationDocument();
        parseDocumentAndSaveToVariables(document);
        if (oAuthToken == null || oAuthToken.isEmpty()) {
            gitHubClientSetUserNameAndPassword();
        } else {
            gitHubClientSetOAuthToken();
        }   
    }
    
    /**
     * Check URL if it is github with owner and repository name then parse
     * @param url to check an parse
     * @return String[] parsed URL
     */
    private String[] parseUrlAndCheck(URL url) {
        String[] ownerAndRepositoryName = url.getPath().split("/");
        if (ownerAndRepositoryName.length < 3 || !url.getHost().equals("github.com")) {
            throw new DataProviderException("Incorrect URL.");
        }
        return ownerAndRepositoryName;
    }
    
    /**
     * Parse data from authentication file
     * @param document to parse
     * @throw AuthenticationFileErrorException if there occures problem while loading file
     */
    private void parseDocumentAndSaveToVariables(Document document) {
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
    
    /**
     * Get normalized and parsed authentication file
     * @return Document parsed
     * @throw AuthenticationFileErrorException if there occures problem while loading file
     */
    private Document getParsedAuthenticationDocument() {
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
    
    /**
     * Get authentication file from resource
     * @return File
     */
    private File getAuthenticationFile() {
        return new File(getClass().getClassLoader()
                .getResource(AUTHENTICATION_FILE_NAME).getFile());
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
    
    public void setOAuthToken(String oAuthToken) {
        this.oAuthToken = oAuthToken;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
