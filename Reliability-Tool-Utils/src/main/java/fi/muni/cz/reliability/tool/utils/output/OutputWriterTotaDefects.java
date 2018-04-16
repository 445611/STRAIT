package fi.muni.cz.reliability.tool.utils.output;

import fi.muni.cz.reliability.tool.utils.Tuple;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author Radoslav Micko <445611@muni.cz>
 */
public class OutputWriterTotaDefects extends OutputWriterImpl {
    
    @Override
    public void writeListOfDefects(BufferedWriter writer, 
            List<Tuple<Integer, Integer>> listOfTuplesData) throws IOException {
        writer.write(String.format("%-30s %s", "Total periods =", 
                listOfTuplesData.get(listOfTuplesData.size() - 1).getA()));
        writer.newLine();
        writer.write(String.format("%s", "Sumarization of total defects:"));
        writer.newLine();
        for (Tuple<Integer, Integer> tuple: listOfTuplesData) {
            writer.write(String.format("%s", tuple.getB()));
            writer.newLine();
        }
    }
}
