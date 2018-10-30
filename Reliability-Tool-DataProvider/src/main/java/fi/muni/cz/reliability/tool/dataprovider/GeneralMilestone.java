package fi.muni.cz.reliability.tool.dataprovider;

import java.util.Date;
import java.util.Objects;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class GeneralMilestone {

    private Date createdAt;
    private Date dueOn;
    private String description;
    private String state;
    private String title;

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