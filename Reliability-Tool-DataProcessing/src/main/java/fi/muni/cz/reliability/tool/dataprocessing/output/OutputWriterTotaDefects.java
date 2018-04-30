package fi.muni.cz.reliability.tool.dataprocessing.output;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class OutputWriterTotaDefects extends OutputWriterImpl {
    
    @Override
    public void writeListOfDefects(BufferedWriter writer, 
            List<Pair<Integer, Integer>> listOfPairsData) throws IOException {
        writer.write(String.format("%-30s %s", "Total periods =", 
                listOfPairsData.get(listOfPairsData.size() - 1).getFirst()));
        writer.newLine();
        writer.write(String.format("%s", "Sumarization of total defects:"));
        writer.newLine();
        for (Pair<Integer, Integer> pair: listOfPairsData) {
            writer.write(String.format("%s", pair.getSecond()));
            writer.newLine();
        }
    }
}
