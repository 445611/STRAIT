package fi.muni.cz.dataprovider;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
@Entity
public class RepositoryInformation implements Serializable {

    @Id
    private Long id;

    private String name;
    @Lob
    @Column(columnDefinition="clob")
    private String description;
    private int size;
    private int watchers;
    private int forks;
    private int contributors;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date pushedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getPushedAt() {
        return pushedAt;
    }

    public void setPushedAt(Date pushedAt) {
        this.pushedAt = pushedAt;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWatchers() {
        return watchers;
    }

    public void setWatchers(int watchers) {
        this.watchers = watchers;
    }

    public int getForks() {
        return forks;
    }

    public void setForks(int forks) {
        this.forks = forks;
    }

    public int getContributors() {
        return contributors;
    }

    public void setContributors(int contributors) {
        this.contributors = contributors;
    }
}
