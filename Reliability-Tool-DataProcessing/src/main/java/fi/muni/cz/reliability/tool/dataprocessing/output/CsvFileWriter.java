package fi.muni.cz.reliability.tool.dataprocessing.output;

import fi.muni.cz.reliability.tool.dataprocessing.exception.DataProcessingException;
import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class CsvFileWriter {
    private static final String COMMA_DELIMITER = "\t";
    private static final String NEW_LINE_SEPARATOR = "\n";

    private static final String FILE_HEADER = "Created at\tClosed at\tUpdated at\tSaved at\tState\tLabels";

    /**
     * Create .csv file with data.
     * 
     * @param list      data to save.
     * @param fileName  name of file to save into.
     */
    public static void writeCsvFile(List<GeneralIssue> list, String fileName) {
        try (FileWriter fileWriter = new FileWriter(fileName)){
                fileWriter.append(FILE_HEADER);
                fileWriter.append(NEW_LINE_SEPARATOR);
                for (GeneralIssue issue : list) {
                    writeElementWithDelimiter(issue.getCreatedAt().toString(), fileWriter);
                    writeElementWithDelimiter(issue.getClosedAt() == null ? 
                            "null" : issue.getClosedAt().toString(), fileWriter);
                    writeElementWithDelimiter(issue.getUpdatedAt() == null ? 
                            "null" : issue.getUpdatedAt().toString(), fileWriter);
                    writeElementWithDelimiter(issue.getSaved() == null ? 
                            "null" : issue.getSaved().toString(), fileWriter);
                    writeElementWithDelimiter(issue.getState() == null ? 
                            "null" : issue.getState(), fileWriter);
                    writeElementWithDelimiter(issue.getLabels() == null ? 
                            "null" : issue.getLabels().toString(), fileWriter);
                    //fileWriter.append(issue.getBody() == null ? 
                    //        "null" : issue.getBody());
                    fileWriter.append(NEW_LINE_SEPARATOR);
                }
        } catch (IOException ex) {
            throw new DataProcessingException("Error while creating csv file.", ex);
        }
    } 
    
    private static void writeElementWithDelimiter(String element, FileWriter fileWriter) throws IOException {
        fileWriter.append(element);
        fileWriter.append(COMMA_DELIMITER);
    }
}