package fi.muni.cz.reliability.tool.core;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Radoslav Micko <445611@muni.cz>
 */
public class GeneralIssue implements Serializable {
    
    private long id;
    private Date saved;
    
    private Date createdAt;
    private Date closedAt;
    private Date updatedAt;
    
    private String body;
    private String state;
    private List<String> labels;
    
    
    

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

    public void setBody(String Body) {
        this.body = body;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<String> getLabels() {
        return Collections.unmodifiableList(labels);
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
