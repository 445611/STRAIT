package fi.muni.cz.reliability.tool.dataprocessing.persistence;

import fi.muni.cz.reliability.tool.dataprocessing.exception.DataProcessingException;
import java.util.List;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public interface GeneralIssuesSnapshotDao {
    
    /**
     * Save new snapshot to Database.
     * 
     * @param snapshot to save.
     */
    void save(GeneralIssuesSnapshot snapshot);
    
    /**
     * Get all saved snapshots.
     * 
     * @return list of <code>GeneralIssuesSnapshot</code>.
     */
    List<GeneralIssuesSnapshot> getAllSnapshots();
    
    /**
     * Get snapshot by name.
     * 
     * @param name  of snapshot.
     * @throws DataProcessingException  When there is no such snapshot.
     * @return      <code>GeneralIssuesSnapshot</code>.
     */
    GeneralIssuesSnapshot getSnapshotByName(String name) throws DataProcessingException;
    
    /**
     * Get snapshot by user and repositry name.
     * 
     * @param user          user name.
     * @param repository    repository name.
     * @return      list of  <code>GeneralIssuesSnapshot</code> for repository.
     */
    List<GeneralIssuesSnapshot> getAllSnapshotsForUserAndRepository(String user, String repository);
}
