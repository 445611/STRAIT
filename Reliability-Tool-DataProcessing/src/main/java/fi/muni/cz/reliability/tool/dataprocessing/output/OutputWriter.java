package fi.muni.cz.reliability.tool.dataprocessing.output;

import java.util.List;

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
}
