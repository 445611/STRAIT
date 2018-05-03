package fi.muni.cz.reliability.tool.dataprocessing.output;

import java.util.List;
import org.apache.commons.math3.util.Pair;


/**

 * @author Radoslav Micko, 445611@muni.cz
 */
public class HtmlOutputWriter extends OutputWriterAbstract {

    @Override
    public void writeOutputDataToFile(String url, List<Pair<Integer, Integer>> listOfPairsData, String fileName) {
        OutputData outputData = prepareOutputData(url, listOfPairsData);
    }
    
}
