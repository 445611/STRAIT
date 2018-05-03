package fi.muni.cz.reliability.tool.dataprocessing.output;

import fi.muni.cz.reliability.tool.dataprovider.utils.GitHubUrlParser;
import fi.muni.cz.reliability.tool.dataprovider.utils.ParsedUrlData;
import fi.muni.cz.reliability.tool.dataprovider.utils.UrlParser;
import java.util.Date;
import java.util.List;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public interface OutputWriter {
    
    /**
     * Write data from <code>OutputData</code> to certain file.
     * 
     * @param url of repositry
     * @param listOfPairsData Pair data 
     * @param fileName name of file
     */
    void writeOutputDataToFile(String url, List<Pair<Integer, Integer>> listOfPairsData, String fileName);
    
    /**
     * Prepare attributes in <code>OutputData</code>
     * 
     * @param url of repositry
     * @param listOfPairsData Pair data 
     * @return OutputData prepared with all attributes
     */
    
    
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
