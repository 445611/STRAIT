package fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.configuration;

import java.util.List;

/**
 * This class is used for setting data in Filtering_config file
 * 
 * @author Radoslav Micko, 445611@muni.cz
 */
public interface FilteringConfiguration {
    /**
     * Load filtering words from file
     * 
     * @return List of filtering words
     */
    List<String> loadFilteringWordsFromFile();
}
