package fi.muni.cz.reliability.tool.dataprocessing.output;

import fi.muni.cz.reliability.tool.dataprocessing.exception.DataProcessingException;
import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class CsvFileIssuesWriter implements IssuesWriter {
    private static final String CSV_FILE_SUFFIX = ".csv";
    private static final String COMMA_DELIMITER = "\t";
    private static final String NEW_LINE_SEPARATOR = "\n";

    private static final String FILE_HEADER = "Created at\tClosed at\tUpdated at\tState\tLabels\tBody";

    @Override
    public void writeToFile(List<GeneralIssue> list, String fileName) {
        File file = new File("./output/" + fileName + CSV_FILE_SUFFIX);
        try (FileWriter fileWriter = new FileWriter(file)){
                fileWriter.append(FILE_HEADER);
                fileWriter.append(NEW_LINE_SEPARATOR);
                for (GeneralIssue issue : list) {
                    writeElementWithDelimiter(
                            eliminateSeparatorAndCheckNullValue(issue.getCreatedAt().toString()), fileWriter);
                    writeElementWithDelimiter(
                            eliminateSeparatorAndCheckNullValue(issue.getClosedAt()), fileWriter);
                    writeElementWithDelimiter(
                            eliminateSeparatorAndCheckNullValue(issue.getUpdatedAt()), fileWriter);
                    writeElementWithDelimiter(
                            eliminateSeparatorAndCheckNullValue(issue.getState()), fileWriter);
                    writeElementWithDelimiter(
                            eliminateSeparatorAndCheckNullValue(issue.getLabels()), fileWriter);
                    String s = eliminateSeparatorAndCheckNullValue(issue.getBody());
                    writeElementWithDelimiter(
                            eliminateSeparatorAndCheckNullValue(issue.getBody()), fileWriter);
                    fileWriter.append(NEW_LINE_SEPARATOR);
                }
        } catch (IOException ex) {
            throw new DataProcessingException("Error while creating csv file.", ex);
        }
    } 
    
    private static String checkNullValueToString(Object obj) {
        return obj == null ? "null" : obj.toString();
    }
    
    private static void writeElementWithDelimiter(String element, FileWriter fileWriter) throws IOException {
        fileWriter.append(element);
        fileWriter.append(COMMA_DELIMITER);
    }

    private static String eliminateSeparatorAndCheckNullValue(Object obj) {
        return checkNullValueToString(obj).replaceAll("\\t", " ").replaceAll("\\n", " ").replaceAll("\\v", " ");
    }
}