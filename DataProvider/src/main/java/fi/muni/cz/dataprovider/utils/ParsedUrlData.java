package fi.muni.cz.dataprovider.utils;

import java.net.URL;

/**
 * Represents parsed URL.
 * 
 * @author Radoslav Micko, 445611@muni.cz
 */
public class ParsedUrlData {
    
    private URL url;
    private String userName;
    private String repositoryName;

    /**
     * Initialize all private attributes.
     * 
     * @param url URL
     * @param userName user name
     * @param repositoryName repository name
     */
    public ParsedUrlData(URL url, String userName, String repositoryName) {
        this.url = url;
        this.userName = userName;
        this.repositoryName = repositoryName;
    }
    
    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    @Override
    public String toString() {
        return "ParsedUrlData{" + "url=" + url.toString() + ", userName=" 
                + userName + ", repositoryName=" + repositoryName + '}';
    }
}
