package fi.muni.cz.reliability.tool.dataprovider.authenticationdata;

import java.util.List;

/**
 * @author Radoslav Micko <445611@muni.cz>
 */
public interface AuthenticationDataProvider {
    
    /**
     * Get needed authentication data from file
     * @return List of Strings
     */
    List<String> getAuthenticationDataFromFile();
    
}
