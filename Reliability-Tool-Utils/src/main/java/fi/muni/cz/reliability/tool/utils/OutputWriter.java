package fi.muni.cz.reliability.tool.utils;

import java.util.List;

/**
 * @author Radoslav Micko <445611@muni.cz>
 */
public interface OutputWriter {
    
    /**
     * Write data from <code>OutputData</code> to certain file.
     * @param data to write
     */
    void writeOutputDataToFile(OutputData data);
    
    /**
     * Prepare attributes in <code>OutputData</code>
     * @param url of repositry
     * @param listOfTuplesData tuple data 
     * @return OutputData prepared with all attributes
     */
    OutputData prepareOutputData(String url, List<Tuple<Integer, Integer>> listOfTuplesData);
    
}
