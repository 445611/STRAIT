package fi.muni.cz.dataprovider;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public interface RepositoryInformationDataProvider {

    /**
     * Get information about repository.
     *
     * @param url   url of repository
     * @return      repository information
     */
    RepositoryInformation getRepositoryInformation(String url);
}
