package fi.muni.cz.reliability.tool.dataprovider;

import fi.muni.cz.reliability.tool.core.GeneralIssue;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * @author Radoslav Micko <445611@muni.cz>
 */
@Component
public interface DataProvider {
    
    /**
     * Get list of issues for owner of specified repository
     * 
     * @param owner             Name of owner
     * @param repositoryName    Name of repositry
     * @return                  list of GeneralIssue
     */
    List<GeneralIssue> getIssuesByOwnerRepoName(String owner, String repositoryName);
    
    /**
     * Get list of issues for url of repo
     * @param url   url of repo
     * @return      list of GeneralIssue
     */
    List<GeneralIssue> getIssuesByUrl(String url);
    
}
