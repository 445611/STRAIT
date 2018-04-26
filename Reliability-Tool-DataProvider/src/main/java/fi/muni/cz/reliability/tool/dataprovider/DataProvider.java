package fi.muni.cz.reliability.tool.dataprovider;

import java.util.List;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public interface DataProvider {
    
    /**
     * Get list of issues for url of repository
     * 
     * @param url   url of repo
     * @return      list of GeneralIssue
     */
    List<GeneralIssue> getIssuesByUrl(String url);
}
