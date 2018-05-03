package fi.muni.cz.reliability.tool.dataprocessing.output;

import fi.muni.cz.reliability.tool.dataprocessing.exception.DataProcessingException;
import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**

 * @author Radoslav Micko, 445611@muni.cz
 */
public class HtmlOutputWriter extends OutputWriterAbstract {

    @Override
    public void writeOutputDataToFile(OutputData outputData, String fileName) {
        try {
            Template temp = configuration.getTemplate("template_one.html");
            
            Map<String, Object> root = new HashMap<>();
            root.put("data", outputData);
            root.put("parameters", outputData.getParameters());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName + ".html"))) {
            temp.process(root, writer);
        } catch (IOException ex) {
            throw new DataProcessingException("Error occured during writing to file.", ex);
        } catch (TemplateException ex) {
            throw new DataProcessingException("Template error.", ex);
        }
            
        } catch (MalformedTemplateNameException ex) {
            Logger.getLogger(HtmlOutputWriter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(HtmlOutputWriter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HtmlOutputWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
}
