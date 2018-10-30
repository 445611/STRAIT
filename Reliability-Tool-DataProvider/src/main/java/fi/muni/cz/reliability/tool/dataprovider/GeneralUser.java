package fi.muni.cz.reliability.tool.dataprovider;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class GeneralUser {
    
    private String name;
    private String email;

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