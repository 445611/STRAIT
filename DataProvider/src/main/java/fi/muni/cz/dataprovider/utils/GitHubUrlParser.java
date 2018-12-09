package fi.muni.cz.dataprovider.utils;

import fi.muni.cz.dataprovider.exception.DataProviderException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class GitHubUrlParser implements UrlParser {

    private static final String HOST = "github.com";
    
    @Override
    public ParsedUrlData parseUrlAndCheck(String urlString) {
        try {        
            URL url = new URL(urlString);
            String[] ownerAndRepositoryName = url.getPath().split("/");
            if (ownerAndRepositoryName.length < 3 || !url.getHost().equals(HOST)) {
                throw new DataProviderException("Incorrect URL.");
            }
            return new ParsedUrlData(url, ownerAndRepositoryName[1], ownerAndRepositoryName[2]);
        } catch (MalformedURLException ex) {
            throw new DataProviderException("Incorrect URL.");
        } 
    }
}