package fi.muni.cz.dataprovider;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "repo_info_id")
    private List<Release> listOfReleases = new ArrayList<>();

    // Last commit
    @Temporal(TemporalType.TIMESTAMP)
    private Date pushedAt;

    // First commit
    @Temporal(TemporalType.TIMESTAMP)
    private Date pushedAtFirst;

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

    public Date getPushedAtFirst() {
        return pushedAtFirst;
    }

    public void setPushedAtFirst(Date pushedAtFirst) {
        this.pushedAtFirst = pushedAtFirst;
    }

    public List<Release> getListOfReleases() {
        return listOfReleases.stream()
                .sorted(Comparator.comparing(Release::getPublishedAt))
                .collect(Collectors.toList());
    }

    /**
     * Add one Release to list
     * @param release   Release to be added
     */
    public void addRelease(Release release) {
        this.listOfReleases.add(release);
    }
}
