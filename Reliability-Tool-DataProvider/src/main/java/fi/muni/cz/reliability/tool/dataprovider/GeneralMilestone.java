package fi.muni.cz.reliability.tool.dataprovider;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
@Entity
@Table(name = "GENERALMILESTONE")
public class GeneralMilestone implements Serializable {

    @OneToMany(mappedBy = "generalMilestone")
    private List<GeneralIssue> generalIssues;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueOn;
    
    @Column(columnDefinition="clob")
    @Lob
    private String description;
    @Column(name = "MilestoneState")
    private String state;
    private String title;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<GeneralIssue> getGeneralIssues() {
        return generalIssues;
    }

    public void setGeneralIssues(List<GeneralIssue> generalIssues) {
        this.generalIssues = generalIssues;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getDueOn() {
        return dueOn;
    }

    public void setDueOn(Date dueOn) {
        this.dueOn = dueOn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.title, this.createdAt);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof GeneralMilestone)) {
            return false;
        }
        final GeneralMilestone other = (GeneralMilestone) obj;
        if (!Objects.equals(this.title, other.createdAt)) {
            return false;
        }
        return true;
    }
}