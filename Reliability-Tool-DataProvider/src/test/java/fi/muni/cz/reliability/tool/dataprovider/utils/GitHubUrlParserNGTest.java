package fi.muni.cz.reliability.tool.dataprovider.utils;

import fi.muni.cz.reliability.tool.dataprovider.exception.DataProviderException;
import java.net.MalformedURLException;
import java.net.URL;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class GitHubUrlParserNGTest {

    UrlParser parser = new GitHubUrlParser();
    
    @Test
    public void testParseUrlAndCheck() throws MalformedURLException {
        ParsedUrlData parsedData = parser.parseUrlAndCheck("https://github.com/user/repository");
        assertEquals("repository", parsedData.getRepositoryName());
        assertEquals("user", parsedData.getUserName());
        assertEquals(new URL("https://github.com/user/repository"), parsedData.getUrl());
    }
    
    @Test(expectedExceptions = DataProviderException.class)
    public void testIncorrectUrl() {
        parser.parseUrlAndCheck("notUrl");
    }   

    @Test(expectedExceptions = DataProviderException.class)
    public void testNotCompleteUrl() {
        parser.parseUrlAndCheck("https://github.com/user/");
    }
    
    @Test(expectedExceptions = DataProviderException.class)
    public void testNotGithubUrl() {
        parser.parseUrlAndCheck("https://notgithub.com/user/repository");
    }
}