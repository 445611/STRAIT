package fi.muni.cz.reliability.tool.dataprovider;

import java.util.List;

/**
 * @author Radoslav Micko <445611@muni.cz>
 */
public interface DataProvider {
    
    /**
     * Get list of issues for owner of specified repository
     * 
     * @param owner             Name of owner
     * @param repositoryName    Name of repositry
     * @return                  list of GeneralIssue
     * @throw  AuthenticationException when there occures problem with authentication
     */
    List<GeneralIssue> getIssuesByOwnerRepoName(String owner, String repositoryName);
    
    /**
     * Get list of issues for url of repository
     * 
     * @param url   url of repo
     * @return      list of GeneralIssue
     */
    List<GeneralIssue> getIssuesByUrl(String url);
}
