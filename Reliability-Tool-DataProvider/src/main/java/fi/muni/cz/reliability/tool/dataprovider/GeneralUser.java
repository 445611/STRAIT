package fi.muni.cz.reliability.tool.dataprovider;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
@Entity
@Table(name = "GENERALUSER")
public class GeneralUser implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToMany(mappedBy = "generalUser")
    private List<GeneralIssue> generalIssues;
    
    private String name;
    private String email;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public int hashCode() {
        return email.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof GeneralUser)) {
            return false;
        }
        final GeneralUser other = (GeneralUser) obj;
        return email.equals(other.getEmail());
    }
}