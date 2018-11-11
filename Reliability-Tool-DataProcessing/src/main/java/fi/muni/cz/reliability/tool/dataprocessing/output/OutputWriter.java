package fi.muni.cz.reliability.tool.dataprocessing.output;

import java.util.List;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public interface OutputWriter {
    
    /**
     * Write data from OutputData to certain file.
     * 
     * @param outputData data to write 
     * @param fileName name of file
     */
    void writeOutputDataToFile(List<OutputData> outputData, String fileName);
    
    /**
     * Prepare attributes in OutputData
     * 
     * @param   url of repositry
     * @param   listOfPairsData Pair data 
     * @return  OutputData prepared with all attributes 
     */
    OutputData prepareOutputData(String url, List<Pair<Integer, Integer>> listOfPairsData);
}
