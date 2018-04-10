package fi.muni.cz.reliability.tool.dataprovider;

import fi.muni.cz.reliability.tool.dataprovider.exception.DataProviderException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Radoslav Micko <445611@muni.cz>
 */
public class UrlParserGitHub implements UrlParser {

    @Override
    public String[] parseUrlAndCheck(String urlString) {
        try {        
            URL url = new URL(urlString);
            String[] ownerAndRepositoryName = url.getPath().split("/");
            if (ownerAndRepositoryName.length < 3 || !url.getHost().equals("github.com")) {
                throw new DataProviderException("Incorrect URL.");
            }
            return ownerAndRepositoryName;
        } catch (MalformedURLException ex) {
            Logger.getLogger(GitHubDataProvider.class.getName()).log(Level.SEVERE, "Incorrect URL.", ex);
            throw new DataProviderException("Incorrect URL.");
        } 
    }
}
