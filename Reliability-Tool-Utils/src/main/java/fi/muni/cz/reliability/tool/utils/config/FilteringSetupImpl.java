package fi.muni.cz.reliability.tool.utils.config;

import fi.muni.cz.reliability.tool.utils.FilterByLable;
import fi.muni.cz.reliability.tool.utils.exception.UtilsException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Radoslav Micko <445611@muni.cz>
 */
public class FilteringSetupImpl implements FilteringSetup {

    private static final String FILTERING_CONFIG_FILE = "Filtering_config";
    private static final String SPLITTER = "/"; 
    
    @Override
    public void addWordToConfigFile(String word) {
        List<String> actual = loadFilteringWordsFromFile();
        if (!actual.contains(word.toLowerCase())) {
            actual.add(word.toLowerCase());
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getConfigurationFile()))) {
            for (String wordToWrite: actual) {
                writer.write(wordToWrite.toLowerCase());
                writer.write(SPLITTER);
            }
        } catch (IOException ex){
            throw new UtilsException("Error occured during writing to file.", ex);
        }
    }
    
    @Override
    public void removeWordfromConfigFile(String word) {
        List<String> actual = loadFilteringWordsFromFile();
    }
    
    @Override
    public List<String> loadFilteringWordsFromFile() {
        File file = getConfigurationFile();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line == null || line.isEmpty()) {
                Logger.getLogger(FilterByLable.class.getName()).log(Level.CONFIG,
                    "File " + FILTERING_CONFIG_FILE + " is empty.");
                return new ArrayList<>();
            }
            String[] words = line.split(SPLITTER);
            return new ArrayList<>(Arrays.asList(words));
        } catch (IOException ex) {
            Logger.getLogger(FilterByLable.class.getName()).log(Level.SEVERE,
                    "Error loading " + FILTERING_CONFIG_FILE 
                            + " file from resources.", ex);
            throw new UtilsException("Error loading " + FILTERING_CONFIG_FILE
                    + " file from resources.", ex);
        } 
    }
    
    private File getConfigurationFile() {
        return new File(getClass().getClassLoader()
                .getResource(FILTERING_CONFIG_FILE).getFile());
    }
}
