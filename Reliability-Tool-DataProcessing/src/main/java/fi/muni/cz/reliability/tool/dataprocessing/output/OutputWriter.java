package fi.muni.cz.reliability.tool.dataprocessing.output;

import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.Tuple;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public interface OutputWriter {
    
    /**
     * Write data from <code>OutputData</code> to certain file.
     * 
     * @param data to write
     * @param fileName name of file
     */
    void writeOutputDataToFile(OutputData data, String fileName);
    
    /**
     * Prepare attributes in <code>OutputData</code>
     * 
     * @param url of repositry
     * @param listOfTuplesData tuple data 
     * @return OutputData prepared with all attributes
     */
    OutputData prepareOutputData(String url, List<Tuple<Integer, Integer>> listOfTuplesData);
    
    /**
     * Write list of Tuple in correct foramt
     * 
     * @param writer BufferedWriter to use
     * @param listOfTuplesData data to write
     * @throws IOException if occures problem while writing to file
     */
    void writeListOfDefects(BufferedWriter writer, 
            List<Tuple<Integer, Integer>> listOfTuplesData) throws IOException;
}
