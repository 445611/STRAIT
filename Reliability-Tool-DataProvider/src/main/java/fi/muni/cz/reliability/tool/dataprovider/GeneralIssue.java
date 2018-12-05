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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
@Entity
public class GeneralIssue implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date closedAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    
    private int comments;
    private int number;
    
    
    @Lob
    @Column(columnDefinition="clob")
    private String body;
    @Column(name = "IssueState")
    private String state;
    
    private String title;
    private String url;
    private String htmlUrl;
    
    @ElementCollection
    private List<String> labels;

    @NotNull
    @Column(name = "snapshot_id")
    private Long snapshotid;
    
    private String userName;
    private String userEmail;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date milestoneCreatedAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date milestoneDueOn;
    
    @Lob
    @Column(columnDefinition="clob")
    private String milestoneDescription;
    
    @Column(name = "MilestoneState")
    private String milestoneState;
    private String milestoneTitle;

    public Date getMilestoneCreatedAt() {
        return milestoneCreatedAt;
    }

    public void setMilestoneCreatedAt(Date milestoneCreatedAt) {
        this.milestoneCreatedAt = milestoneCreatedAt;
    }

    public Date getMilestoneDueOn() {
        return milestoneDueOn;
    }

    public void setMilestoneDueOn(Date milestoneDueOn) {
        this.milestoneDueOn = milestoneDueOn;
    }

    public String getMilestoneDescription() {
        return milestoneDescription;
    }

    public void setMilestoneDescription(String milestoneDescription) {
        this.milestoneDescription = milestoneDescription;
    }

    public String getMilestoneState() {
        return milestoneState;
    }

    public void setMilestoneState(String milestoneState) {
        this.milestoneState = milestoneState;
    }

    public String getMilestoneTitle() {
        return milestoneTitle;
    }

    public void setMilestoneTitle(String milestoneTitle) {
        this.milestoneTitle = milestoneTitle;
    }
    

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    
    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }
    
    public Long getSnapshotid() {
        return snapshotid;
    }
    
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

    /**
     * Covert all labels to lower case.
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
        if (!(obj instanceof GeneralIssue)) {
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