package fi.muni.cz.dataprovider;

import java.util.List;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public interface GeneralIssueDataProvider {
    
    /**
     * Get list of issues for url of repository
     * 
     * @param url   url of repo
     * @return      list of GeneralIssue
     */
    List<GeneralIssue> getIssuesByUrl(String url);
}
