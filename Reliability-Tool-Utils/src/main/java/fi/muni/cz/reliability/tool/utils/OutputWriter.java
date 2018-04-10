package fi.muni.cz.reliability.tool.utils;

/**
 * @author Radoslav Micko <445611@muni.cz>
 */
public interface OutputWriter {
    
    /**
     * Write data from <code>OutputData</code> to certain file.
     * @param data to write
     * @param name of file
     */
    void writeOutputDataToFileNamed(OutputData data, String name);
    
}
