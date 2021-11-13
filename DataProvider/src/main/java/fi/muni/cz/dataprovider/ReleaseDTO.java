package fi.muni.cz.dataprovider;

import java.util.Date;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class ReleaseDTO {

    private String name;
    private Date publishedAt;

    /**
     * Name of repository.
     * @return Name of repository.
     */
    public String getName() {
        return name;
    }

    /**
     * Set name of repository.
     * @param name Name of repository.
     * @return object itself.
     */
    public ReleaseDTO setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Date published at.
     * @return Date published at.
     */
    public Date getPublishedAt() {
        return publishedAt;
    }

    /**
     * Set date published at.
     * @param publishedAt Date published at.
     * @return object itself.
     */
    public ReleaseDTO setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
        return this;
    }
}
