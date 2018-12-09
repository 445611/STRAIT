package fi.muni.cz.dataprovider.utils;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public interface UrlParser {
    
    /**
     * Check URL if it is with owner and repository name then parse
     * 
     * @param urlString to check an parse
     * @return String[] parsed URL. String[]={Host, Owner, Repository}
     */
    ParsedUrlData parseUrlAndCheck(String urlString);
}
