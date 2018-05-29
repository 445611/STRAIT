package fi.muni.cz.reliability.tool.dataprovider;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class TEMPORARYFileDataProvider {

    public List<Pair<Integer, Integer>> getIssuesByUrl(String url) {
        List<Pair<Integer, Integer>> list = new LinkedList<>();
        try (InputStream stream = getClass().getClassLoader()
                .getResourceAsStream("testInputData.txt")) {
            DataInputStream in = new DataInputStream(stream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));    

            
            String strLine;
            while ((strLine = br.readLine()) != null)   {
                    String[] splited = strLine.split("\\s+");
                    list.add(new Pair(Integer.parseInt(splited[0]), Integer.parseInt(splited[1])));
            }

        } catch (IOException ex) {
            
        }
        return list;
    }  
}