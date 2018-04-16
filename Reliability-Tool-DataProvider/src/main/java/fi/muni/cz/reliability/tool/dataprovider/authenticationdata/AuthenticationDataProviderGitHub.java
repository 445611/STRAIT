package fi.muni.cz.reliability.tool.dataprovider.authenticationdata;

import fi.muni.cz.reliability.tool.dataprovider.GitHubDataProvider;
import fi.muni.cz.reliability.tool.dataprovider.exception.AuthenticationFileErrorException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author Radoslav Micko <445611@muni.cz>
 */
public class AuthenticationDataProviderGitHub implements AuthenticationDataProvider {

    private static final String AUTHENTICATION_FILE_NAME = "git_hub_authentication_file.properties";
    private static final String NAME_ELEMENT_TAG = "name";
    private static final String PASSWORD_ELEMENT_TAG = "password";
    private static final String TOKEN_ELEMENT_TAG = "token";
    
    @Override
    public List<String> getAuthenticationDataFromFile() {
        List<String> list = getListOfParsedData(); 
        return list;
    }
    
    /**
     * Parse data from authentication file
     * @return List of authentication data. List={Name, Password, OAuthToken}
     * @throw AuthenticationFileErrorException if there occures problem while loading file
     */
    private List<String> getListOfParsedData() {
        List<String> listWithData = new ArrayList<>();
        Properties properties = new Properties();
        try (InputStream stream = getClass().getClassLoader()
                .getResourceAsStream(AUTHENTICATION_FILE_NAME)) {
            properties.load(stream);
            listWithData.add(properties.getProperty(NAME_ELEMENT_TAG));
            listWithData.add(properties.getProperty(PASSWORD_ELEMENT_TAG));
            listWithData.add(properties.getProperty(TOKEN_ELEMENT_TAG));
        } catch (IOException ex) {
            Logger.getLogger(GitHubDataProvider.class.getName())
                    .log(Level.SEVERE, "Error while getting data from " + 
                            AUTHENTICATION_FILE_NAME, ex);
            throw new AuthenticationFileErrorException("Error while getting data from " + 
                            AUTHENTICATION_FILE_NAME, ex);
        }
        return listWithData;   
    }
}
