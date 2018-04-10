package fi.muni.cz.reliability.tool.utils;

import fi.muni.cz.reliability.tool.utils.exception.UtilsException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Radoslav Micko <445611@muni.cz>
 */
public class OutputWriterImpl implements OutputWriter {

    @Override
    public void writeOutputDataToFileNamed(OutputData data, String name) {
        

        //TODO 
        
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(name))) {
             writer.write(data.toString());
        } catch (IOException ex){
            throw new UtilsException("Error occured during writing to file.", ex);
        }
    }  
}
