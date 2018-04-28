package fi.muni.cz.reliability.tool.dataprovider;

import java.io.Serializable;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
@Entity
@Table(name = "GENERALISSUE")
public class GeneralIssue implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date saved;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date closedAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    
    @Column(columnDefinition="clob")
    @Lob
    private String body;
    @Column(name = "IssueState")
    private String state;
    
    @ElementCollection
    private List<String> labels;

    
    private Long snapshotid;
    
    public Long getSnapshotid() {
        return snapshotid;
    }
    
    @NotNull
    @Column(name = "snapshot_id")
    public void setSnapshotid(Long snapshotid) {
        this.snapshotid = snapshotid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Date closedAt) {
        this.closedAt = closedAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public Date getSaved() {
        return saved;
    }

    public void setSaved(Date saved) {
        this.saved = saved;
    }

    /**
     * Covert all <code>labels</code> to lower case.
     */
    public void allLabelsToLowerCase() {
        labels.replaceAll(String::toLowerCase);
    }
    
    @Override
    public String toString() {
        return "GeneralIssue{" + "createdAt=" + createdAt 
                + ", closedAt=" + closedAt + ", state=" + state + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.body, this.state, this.createdAt, this.closedAt);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof GeneralIssue) {
            return false;
        }
        final GeneralIssue other = (GeneralIssue) obj;
        if (!Objects.equals(this.body, other.body)) {
            return false;
        }
        if (!Objects.equals(this.state, other.state)) {
            return false;
        }
        if (!Objects.equals(this.createdAt, other.createdAt)) {
            return false;
        }
        if (!Objects.equals(this.closedAt, other.closedAt)) {
            return false;
        }
        return true;
    }
}
