package fi.muni.cz.reliability.tool.dataprocessing.persistence;

import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
@Entity
@Table(name = "GENERALISSUE_SNAPSHOT")
public class GeneralIssuesSnapshot implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    /**
     * From {@link java.util.Calendar Calendar} static properties.
     */
    private int typeOfTimeToSplitTestInto;
    private int howManyTimeUnitsToAdd;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date startOfTesting;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date endOfTesting;
    
    @ElementCollection
    private List<String> filtersRan;
    
    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "snapshot_id")
    private List<GeneralIssue> listOfGeneralIssues;
    
    private String url;
    private String userName;
    private String repositoryName;
    private String snapshotName;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    private String modelName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTypeOfTimeToSplitTestInto() {
        return typeOfTimeToSplitTestInto;
    }

    public void setTypeOfTimeToSplitTestInto(int typeOfTimeToSplitTestInto) {
        this.typeOfTimeToSplitTestInto = typeOfTimeToSplitTestInto;
    }

    public int getHowManyTimeUnitsToAdd() {
        return howManyTimeUnitsToAdd;
    }

    public void setHowManyTimeUnitsToAdd(int howManyTimeUnitsToAdd) {
        this.howManyTimeUnitsToAdd = howManyTimeUnitsToAdd;
    }

    public Date getStartOfTesting() {
        return startOfTesting;
    }

    public void setStartOfTesting(Date startOfTesting) {
        this.startOfTesting = startOfTesting;
    }

    public Date getEndOfTesting() {
        return endOfTesting;
    }

    public void setEndOfTesting(Date endOfTesting) {
        this.endOfTesting = endOfTesting;
    }

    public List<String> getFiltersRan() {
        return filtersRan;
    }

    public void setFiltersRan(List<String> filtersRan) {
        this.filtersRan = filtersRan;
    }

    public List<GeneralIssue> getListOfGeneralIssues() {
        return listOfGeneralIssues;
    }

    public String getSnapshotName() {
        return snapshotName;
    }

    public void setSnapshotName(String snapshotName) {
        this.snapshotName = snapshotName;
    }

    public void setListOfGeneralIssues(List<GeneralIssue> listOfGeneralIssues) {
        this.listOfGeneralIssues = listOfGeneralIssues;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.url, this.userName, 
                this.repositoryName, this.createdAt, this.modelName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof GeneralIssuesSnapshot) {
            return false;
        }
        final GeneralIssuesSnapshot other = (GeneralIssuesSnapshot) obj;
        
        if (!Objects.equals(this.url, other.url)) {
            return false;
        }
        if (!Objects.equals(this.userName, other.userName)) {
            return false;
        }
        if (!Objects.equals(this.repositoryName, other.repositoryName)) {
            return false;
        }
        if (!Objects.equals(this.createdAt, other.createdAt)) {
            return false;
        }
        if (!Objects.equals(this.modelName, other.modelName)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return snapshotName + "{url=" + url + ", userName=" + userName 
                + ", repositoryName=" + repositoryName + ", createdAt=" 
                + createdAt + ", modelName=" + modelName + ", List = " + listOfGeneralIssues + '}';
    }
}