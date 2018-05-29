package fi.muni.cz.reliability.tool.dataprocessing.output;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.commons.math3.util.Pair;

/**
 * @author rados
 */
public class TEMPORARYWriter {
     
    /**
     * TEMPORARY
     * @param list  sada
     */
    public static void write(List<Pair<Integer, Integer>> list) {
        
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream("dataForCasre.dat"), StandardCharsets.UTF_8));) {
            double last = list.get(list.size() - 1).getFirst();
            String split = "    ";
            int i = 0;
            writer.write("Weeks");
            writer.newLine();
            for (Pair<Integer, Integer> pair: list) {
                writer.write(pair.getFirst() + split + pair.getSecond() +
                        split + last + split + 1.0 + split + i + split + 1.0 + split + 1);
                writer.newLine();
                i++;
            }
        } catch (IOException ex) {

        }
    }
}
