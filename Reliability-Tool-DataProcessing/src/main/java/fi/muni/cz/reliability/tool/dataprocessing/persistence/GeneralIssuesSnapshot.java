package fi.muni.cz.reliability.tool.dataprocessing.persistence;

import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
    
    private String periodicOfTesting;
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
    
    @Column(unique=true)
    private String snapshotName;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public GeneralIssuesSnapshot() {
    }

    private GeneralIssuesSnapshot(GeneralIssuesSnapshotBuilder builder) {
        this.periodicOfTesting = builder.periodicOfTesting;
        this.startOfTesting = builder.startOfTesting;
        this.endOfTesting = builder.endOfTesting;
        this.filtersRan = builder.filtersRan;
        this.listOfGeneralIssues = builder.listOfGeneralIssues;
        this.url = builder.url;
        this.userName = builder.userName;
        this.repositoryName = builder.repositoryName;
        this.snapshotName = builder.snapshotName;
        this.createdAt = builder.createdAt;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeOfTimeToSplitTestInto() {
        return periodicOfTesting;
    }

    public void setTypeOfTimeToSplitTestInto(String typeOfTimeToSplitTestInto) {
        this.periodicOfTesting = typeOfTimeToSplitTestInto;
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

    @Override
    public int hashCode() {
        return Objects.hash(this.url, this.userName, 
                this.repositoryName, this.createdAt);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof GeneralIssuesSnapshot)) {
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
        return true;
    }

    @Override
    public String toString() {
        return snapshotName + "{url=" + url + ", userName=" + userName 
                + ", repositoryName=" + repositoryName + ", createdAt=" 
                + createdAt + ", List = " + listOfGeneralIssues + '}';
    }
    
    public static class GeneralIssuesSnapshotBuilder {
        private String periodicOfTesting;
        private Date startOfTesting;
        private Date endOfTesting;
        private List<String> filtersRan;
        private List<GeneralIssue> listOfGeneralIssues;
        private String url;
        private String userName;
        private String repositoryName;
        private String snapshotName;
        private Date createdAt;

        public GeneralIssuesSnapshotBuilder setTypeOfTimeToSplitTestInto(String periodicOfTesting) {
            this.periodicOfTesting = periodicOfTesting;
            return this;
        }

        public GeneralIssuesSnapshotBuilder setStartOfTesting(Date startOfTesting) {
            this.startOfTesting = startOfTesting;
            return this;
        }

        public GeneralIssuesSnapshotBuilder setEndOfTesting(Date endOfTesting) {
            this.endOfTesting = endOfTesting;
            return this;
        }

        public GeneralIssuesSnapshotBuilder setFiltersRan(List<String> filtersRan) {
            this.filtersRan = filtersRan;
            return this;
        }

        public GeneralIssuesSnapshotBuilder setListOfGeneralIssues(List<GeneralIssue> listOfGeneralIssues) {
            this.listOfGeneralIssues = listOfGeneralIssues;
            return this;
        }

        public GeneralIssuesSnapshotBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public GeneralIssuesSnapshotBuilder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public GeneralIssuesSnapshotBuilder setRepositoryName(String repositoryName) {
            this.repositoryName = repositoryName;
            return this;
        }

        public GeneralIssuesSnapshotBuilder setSnapshotName(String snapshotName) {
            this.snapshotName = snapshotName;
            return this;
        }

        public GeneralIssuesSnapshotBuilder setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        public GeneralIssuesSnapshot build() {
            return new GeneralIssuesSnapshot(this);
        } 
    }
}