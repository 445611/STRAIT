package fi.muni.cz.reliability.tool.dataprovider.utils;

/**
 * @author Radoslav Micko <445611@muni.cz>
 */
public interface UrlParser {
    
    /**
     * Check URL if it is with owner and repository name then parse
     * @param urlString to check an parse
     * @return String[] parsed URL
     */
    String[] parseUrlAndCheck(String urlString);
}
