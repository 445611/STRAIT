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
public abstract class OutputWriterImpl implements OutputWriter {

    private static final String DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss";
    
    @Override
    public void writeOutputDataToFile(OutputData data,String fileName) {
        
        String timeStamp = new SimpleDateFormat(DATE_FORMAT).format(Calendar.getInstance().getTime());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName + "_" + timeStamp + ".txt"))) {
             
            writeTwoStingInFormatWithNewLine(writer, "URL = ", data.getUrl());
            writeTwoStingInFormatWithNewLine(writer, "User name = ", data.getUserName());
            writeTwoStingInFormatWithNewLine(writer, "Repository name = ", data.getRepositoryName());
            writeTwoStingInFormatWithNewLine(writer, "Created = ", data.getCreatedAt().toString());
            writeTwoStingInFormatWithNewLine(writer, "Function param. A = ", 
                    Double.toString(data.getEvaluatedFunctionParameterA()));
            writeTwoStingInFormatWithNewLine(writer, "Function param. B = ", 
                    Double.toString(data.getEvaluatedFunctionParameterB()));
            writeTwoStingInFormatWithNewLine(writer, "Model name = ", data.getModelName());
            writeTwoStingInFormatWithNewLine(writer, "Total number of defects = ", 
                    Integer.toString(data.getTotalNumberOfDefects()));
            writer.newLine();
            writeListOfDefects(writer, data.getWeeksAndDefects());  
        } catch (IOException ex){
            throw new UtilsException("Error occured during writing to file.", ex);
        }
    }  
    
    @Override
    public OutputData prepareOutputData(String url, List<Tuple<Integer, Integer>> countedTuples) {
        UrlParser parser = new UrlParserGitHub();
        String[] parsedUrl = parser.parseUrlAndCheck(url);
        OutputData data = new OutputData();
        data.setCreatedAt(new Date());
        data.setRepositoryName(parsedUrl[2]);
        data.setTotalNumberOfDefects(countedTuples.stream().mapToInt(a -> a.getB()).sum());
        data.setUrl(url);
        data.setUserName(parsedUrl[1]);
        data.setWeeksAndDefects(countedTuples);
        return data;
    }
    
    private void writeTwoStingInFormatWithNewLine(BufferedWriter writer, 
            String first, String second) throws IOException {
        writer.write(String.format("%-30s %s", first, second));
        writer.newLine();
    }
}
