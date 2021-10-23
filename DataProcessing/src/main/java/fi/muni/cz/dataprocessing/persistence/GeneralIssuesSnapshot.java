package fi.muni.cz.dataprocessing.persistence;

import fi.muni.cz.dataprovider.GeneralIssue;
import fi.muni.cz.dataprovider.RepositoryInformation;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
@Entity
@Table(name = "GENERALISSUE_SNAPSHOT")
public class GeneralIssuesSnapshot implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
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

    @OneToOne(cascade = CascadeType.MERGE)
    private RepositoryInformation repositoryInformation;

    /**
     * Default constructor.
     */
    public GeneralIssuesSnapshot() {
    }

    private GeneralIssuesSnapshot(GeneralIssuesSnapshotBuilder builder) {
        this.listOfGeneralIssues = builder.listOfGeneralIssues;
        this.url = builder.url;
        this.userName = builder.userName;
        this.repositoryName = builder.repositoryName;
        this.snapshotName = builder.snapshotName;
        this.createdAt = builder.createdAt;
        this.repositoryInformation = builder.repositoryInformation;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public RepositoryInformation getRepositoryInformation() {
        return repositoryInformation;
    }

    public void setRepositoryInformation(RepositoryInformation repositoryInformation) {
        this.repositoryInformation = repositoryInformation;
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
        return snapshotName + " { url=" + url + ", userName=" + userName 
                + ", repositoryName=" + repositoryName + ", createdAt=" 
                + createdAt + ", ListOfIssues size = " + listOfGeneralIssues.size() + " }";
    }
    
    /**
     * Builder.
     */
    public static class GeneralIssuesSnapshotBuilder {
        private List<GeneralIssue> listOfGeneralIssues;
        private String url;
        private String userName;
        private String repositoryName;
        private String snapshotName;
        private Date createdAt;
        private RepositoryInformation repositoryInformation;

        /**
         * Set list of general isues.
         * 
         * @param listOfGeneralIssues list of general issues.
         * @return this builder, to allow method chaining.
         */
        public GeneralIssuesSnapshotBuilder setListOfGeneralIssues(List<GeneralIssue> listOfGeneralIssues) {
            this.listOfGeneralIssues = listOfGeneralIssues;
            return this;
        }

        /**
         * Set url.
         * 
         * @param url url.
         * @return this builder, to allow method chaining.
         */
        public GeneralIssuesSnapshotBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        /**
         * Set user name.
         * 
         * @param userName user name.
         * @return this builder, to allow method chaining.
         */
        public GeneralIssuesSnapshotBuilder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        /**
         * Set repository name.
         * 
         * @param repositoryName repository name.
         * @return this builder, to allow method chaining.
         */
        public GeneralIssuesSnapshotBuilder setRepositoryName(String repositoryName) {
            this.repositoryName = repositoryName;
            return this;
        }

        /**
         * Set snapshot name.
         * 
         * @param snapshotName snapshot name.
         * @return this builder, to allow method chaining.
         */
        public GeneralIssuesSnapshotBuilder setSnapshotName(String snapshotName) {
            this.snapshotName = snapshotName;
            return this;
        }

        /**
         * Set date when snapshot was created.
         * 
         * @param createdAt date.
         * @return this builder, to allow method chaining.
         */
        public GeneralIssuesSnapshotBuilder setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        /**
         * Set repository information.
         *
         * @param repositoryInformation repository information.
         * @return this builder, to allow method chaining.
         */
        public GeneralIssuesSnapshotBuilder setRepositoryInformation(RepositoryInformation repositoryInformation) {
            this.repositoryInformation = repositoryInformation;
            return this;
        }

        /**
         * Constructs an GeneralIssuesSnapshot with the values declared by 
         * this GeneralIssuesSnapshot.GeneralIssuesSnapshotBuilder
         * 
         * @return the new GeneralIssuesSnapshot.
         */
        public GeneralIssuesSnapshot build() {
            return new GeneralIssuesSnapshot(this);
        } 
    }
}
