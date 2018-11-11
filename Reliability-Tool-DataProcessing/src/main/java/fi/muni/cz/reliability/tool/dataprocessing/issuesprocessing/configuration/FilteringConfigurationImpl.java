package fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.configuration;

import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.FilterByLabel;
import fi.muni.cz.reliability.tool.dataprocessing.exception.DataProcessingException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class FilteringConfigurationImpl implements FilteringConfiguration {

    private static final String FILTERING_CONFIG_FILE = "Filtering_config";
    private static final String SPLITTER = "/"; 
    
    @Override
    public List<String> loadFilteringWordsFromFile() {
        File file = getConfigurationFile();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line == null || line.isEmpty()) {
                log(Level.CONFIG, "File " + FILTERING_CONFIG_FILE + " is empty.");
                return new ArrayList<>();
            }
            String[] words = line.split(SPLITTER);
            return new ArrayList<>(Arrays.asList(words));
        } catch (IOException ex) {
            log(Level.SEVERE, "Error loading " + FILTERING_CONFIG_FILE + " file from resources.", ex);
            throw new DataProcessingException("Error loading " + FILTERING_CONFIG_FILE
                    + " file from resources.", ex);
        } 
    }
    
    private File getConfigurationFile() {
        return new File(getClass().getClassLoader()
                .getResource(FILTERING_CONFIG_FILE).getFile());
    }
    
    private void log(Level level, String message, Exception ex) {
        Logger.getLogger(FilterByLabel.class.getName())
                    .log(level, message, ex);
    }
    
    private void log(Level level, String message) {
        Logger.getLogger(FilterByLabel.class.getName())
                    .log(level, message);
    }
}