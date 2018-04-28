package fi.muni.cz.reliability.tool.dataprocessing.output;

import fi.muni.cz.reliability.tool.dataprovider.utils.UrlParser;
import fi.muni.cz.reliability.tool.dataprovider.utils.GitHubUrlParser;
import fi.muni.cz.reliability.tool.dataprovider.utils.ParsedUrlData;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.Tuple;
import fi.muni.cz.reliability.tool.dataprocessing.exception.DataProcessingException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public abstract class OutputWriterImpl implements OutputWriter {

    private static final String DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss";
    
    @Override
    public void writeOutputDataToFile(OutputData data,String fileName) {
        
        String timeStamp = new SimpleDateFormat(DATE_FORMAT).format(Calendar.getInstance().getTime());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName + "_" + timeStamp + ".txt"))) {
             
            writeTwoStingsInFormatWithNewLine(writer, "URL = ", data.getUrl());
            writeTwoStingsInFormatWithNewLine(writer, "User name = ", data.getUserName());
            writeTwoStingsInFormatWithNewLine(writer, "Repository name = ", data.getRepositoryName());
            writeTwoStingsInFormatWithNewLine(writer, "Created = ", data.getCreatedAt().toString());
            
            for (String key: data.getParameters().keySet()) {
                writeTwoStingsInFormatWithNewLine(writer, "Function param. " + key + " = ", 
                    Double.toString(data.getParameters().get(key)));
            }

            writeTwoStingsInFormatWithNewLine(writer, "Model name = ", data.getModelName());
            writeTwoStingsInFormatWithNewLine(writer, "Total number of defects = ", 
                    Integer.toString(data.getTotalNumberOfDefects()));
            writer.newLine();
            writeListOfDefects(writer, data.getWeeksAndDefects());  
        } catch (IOException ex){
            throw new DataProcessingException("Error occured during writing to file.", ex);
        }
    }  
    
    @Override
    public OutputData prepareOutputData(String url, List<Tuple<Integer, Integer>> countedTuples) {
        UrlParser parser = new GitHubUrlParser();
        ParsedUrlData parsedUrldata = parser.parseUrlAndCheck(url);
        OutputData data = new OutputData();
        data.setCreatedAt(new Date());
        data.setRepositoryName(parsedUrldata.getRepositoryName());
        data.setTotalNumberOfDefects(countedTuples.stream().mapToInt(a -> a.getB()).sum());
        data.setUrl(url);
        data.setUserName(parsedUrldata.getUserName());
        data.setWeeksAndDefects(countedTuples);
        return data;
    }
    
    private void writeTwoStingsInFormatWithNewLine(BufferedWriter writer, 
            String first, String second) throws IOException {
        writer.write(String.format("%-30s %s", first, second));
        writer.newLine();
    }
}
