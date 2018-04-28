package fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.configuration;

import java.util.List;

/**
 * This class is used for setting data in Filtering_config file
 * 
 * @author Radoslav Micko, 445611@muni.cz
 */
public interface FilteringConfiguration {
    
    /**
     * Add word to configuration file
     * 
     * @param word to add
     */
    //void addWordToConfigFile(String word);
    
    /**
     * Remove word from configuration file
     * 
     * @param word to remove
     */
    //void removeWordfromConfigFile(String word);
    
    /**
     * Load filtering words from file
     * 
     * @return List of filtering words
     */
    List<String> loadFilteringWordsFromFile();
}
