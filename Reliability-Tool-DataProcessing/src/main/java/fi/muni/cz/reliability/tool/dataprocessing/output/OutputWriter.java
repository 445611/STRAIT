package fi.muni.cz.reliability.tool.dataprocessing.output;

import java.util.List;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public interface OutputWriter {
    
    /**
     * Write data from <code>OutputData</code> to certain file.
     * 
     * @param outputData data to write 
     * @param fileName name of file
     */
    void writeOutputDataToFile(OutputData outputData, String fileName);
    
    /**
     * Prepare attributes in <code>OutputData</code>
     * 
     * @param url of repositry
     * @param listOfPairsData Pair data 
     * @return OutputData prepared with all attributes
     */
    OutputData prepareOutputData(String url, List<Pair<Integer, Integer>> listOfPairsData);
    
    
    /**
     * Write list of Pair in correct foramt
     * 
     * @param writer BufferedWriter to use
     * @param listOfPairsData data to write
     * @throws IOException if occures problem while writing to file
     */
    //void writeListOfDefects(BufferedWriter writer, 
    //        List<Pair<Integer, Integer>> listOfPairsData) throws IOException;
}
