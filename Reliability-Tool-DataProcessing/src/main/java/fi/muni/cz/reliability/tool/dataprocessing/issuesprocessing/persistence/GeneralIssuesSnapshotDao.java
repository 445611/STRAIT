package fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.persistence;

import java.util.List;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public interface GeneralIssuesSnapshotDao {
    
    /**
     * TODO
     * @param snapshot TODO
     */
    void save(GeneralIssuesSnapshot snapshot);
    
    /**
     * TODO
     * @return TODO
     */
    List<GeneralIssuesSnapshot> getAllSnapshots();
    
}
