package fi.muni.cz.reliability.tool.dataprovider;

import fi.muni.cz.reliability.tool.core.GeneralIssue;
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
     */
    List<GeneralIssue> getIssues(String owner, String repositoryName);
    
}
