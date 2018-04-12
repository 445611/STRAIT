package fi.muni.cz.reliability.tool.dataprovider.authenticationdata;

import fi.muni.cz.reliability.tool.dataprovider.GitHubDataProvider;
import fi.muni.cz.reliability.tool.dataprovider.exception.AuthenticationFileErrorException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Radoslav Micko <445611@muni.cz>
 */
public class AuthenticationDataProviderGitHub implements AuthenticationDataProvider {

    private static final String AUTHENTICATION_FILE_NAME = "git_hub_authentication_file.xml";
    private static final String USER_ELEMENT_TAG = "user";
    private static final String NAME_ELEMENT_TAG = "name";
    private static final String PASSWORD_ELEMENT_TAG = "password";
    private static final String TOKEN_ELEMENT_TAG = "token";
    
    @Override
    public List<String> getAuthenticationDataFromFile() {
        Document document = getParsedAuthenticationDocument();
        List<String> list = parseDocumentAndSaveToVariables(document); 
        return list;
    }
    
    
    /**
     * Parse data from authentication file
     * @param document to parse
     * @throw AuthenticationFileErrorException if there occures problem while loading file
     */
    private List<String> parseDocumentAndSaveToVariables(Document document) {
        List<String> listWithData = new ArrayList<>();
        NodeList nodeList = document.getElementsByTagName(USER_ELEMENT_TAG);
        try {
            Node node = nodeList.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                listWithData.add(eElement.getElementsByTagName(NAME_ELEMENT_TAG)
                        .item(0).getTextContent());
                listWithData.add(eElement.getElementsByTagName(PASSWORD_ELEMENT_TAG)
                    .item(0).getTextContent());
                listWithData.add(eElement.getElementsByTagName(TOKEN_ELEMENT_TAG)
                    .item(0).getTextContent());
            } 
            return listWithData;
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
}
