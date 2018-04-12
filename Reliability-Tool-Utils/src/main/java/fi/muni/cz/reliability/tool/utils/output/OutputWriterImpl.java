package fi.muni.cz.reliability.tool.utils.output;

import fi.muni.cz.reliability.tool.dataprovider.utils.UrlParser;
import fi.muni.cz.reliability.tool.dataprovider.utils.UrlParserGitHub;
import fi.muni.cz.reliability.tool.utils.Tuple;
import fi.muni.cz.reliability.tool.utils.exception.UtilsException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Radoslav Micko <445611@muni.cz>
 */
public class OutputWriterImpl implements OutputWriter {

    private static final String DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss";
    private static final String FILE_NAME = "OutputData";
    
    @Override
    public void writeOutputDataToFile(OutputData data) {
        
        //TODO 
        
        String timeStamp = new SimpleDateFormat(DATE_FORMAT).format(Calendar.getInstance().getTime());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME + "_" + timeStamp + ".txt"))) {
             writer.write(String.format("%-30s %s", "URL = ", data.getUrl()));
             writer.newLine();
             writer.write(String.format("%-30s %s", "User name = ", data.getUserName()));
             writer.newLine();
             writer.write(String.format("%-30s %s", "Repository name = ", data.getRepositoryName()));            
             writer.newLine();
             writer.write(String.format("%-30s %s", "Created = ", data.getCreatedAt().toString()));
             writer.newLine();
             writer.write(String.format("%-30s %s", "Total number of defects = ", 
                     data.getTotalNumberOfDefects()));
             writer.newLine();
             writer.newLine();
             writeWeeksAndDefectsIntoFile(writer, data.getWeeksAndDefects());
        } catch (IOException ex){
            throw new UtilsException("Error occured during writing to file.", ex);
        }
    }  
    
    @Override
    public OutputData prepareOutputData(String url, List<Tuple<Integer, Integer>> countedTuples) {
        UrlParser parser = new UrlParserGitHub();
        OutputData data = new OutputData();
        data.setCreatedAt(new Date());
        data.setRepositoryName(parser.parseUrlAndCheck(url)[2]);
        data.setTotalNumberOfDefects(countedTuples.stream().mapToInt(a -> a.getB()).sum());
        data.setUrl(url);
        data.setUserName(parser.parseUrlAndCheck(url)[1]);
        data.setWeeksAndDefects(countedTuples);
        return data;
    }
    
    /**
     * Write Tuples to new lines
     * @param writer to use
     * @param countedWeeks  List of Tuples to write
     * @throws IOException if any problem to write 
     */
    private void writeWeeksAndDefectsIntoFile(BufferedWriter writer, 
            List<Tuple<Integer, Integer>> countedWeeks) throws IOException {
        writer.write(String.format("%-30s %s", "Weeks: ", "Defects:"));
        writer.newLine();
        for (Tuple<Integer, Integer> tuple: countedWeeks) {
            writer.write(String.format("%-30s %s", tuple.getA(), tuple.getB()));
            writer.newLine();
        }
    }

}
