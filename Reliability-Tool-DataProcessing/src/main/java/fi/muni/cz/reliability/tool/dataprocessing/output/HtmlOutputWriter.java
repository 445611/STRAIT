package fi.muni.cz.reliability.tool.dataprocessing.output;

import fi.muni.cz.reliability.tool.dataprocessing.exception.DataProcessingException;
import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**

 * @author Radoslav Micko, 445611@muni.cz
 */
public class HtmlOutputWriter extends OutputWriterAbstract {

    private static final String TEMPLATE = "template_one.html";
    
    @Override
    public void writeOutputDataToFile(OutputData outputData, String fileName) {
        Map<String, Object> root = new HashMap<>();
        root.put("data", outputData);
        writeTemplateToFile(root, fileName);
    }
    
    private void writeTemplateToFile(Map<String, Object> root, String fileName) {
        Template template = getTemplateFromConfiguration();
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream(fileName + ".html"), StandardCharsets.UTF_8));) {
            template.process(root, writer);
        } catch (IOException ex) {
            logAndThrowException(Level.SEVERE, "Error occured during writing to file.", ex);
        } catch (TemplateException ex) {
            logAndThrowException(Level.SEVERE, "Template error.", ex);
        }
    }
    
    private Template getTemplateFromConfiguration() {
        Template template = null;
        try {
            template = configuration.getTemplate(TEMPLATE);
        } catch (MalformedTemplateNameException ex) {
            logAndThrowException(Level.SEVERE, "Incorrect Template name.", ex);
        } catch (ParseException ex) {
            logAndThrowException(Level.SEVERE, "Template syntaxe error.", ex);
        } catch (IOException ex) {
            logAndThrowException(Level.SEVERE, "Template error.", ex);
        }
        return template;
    }
    
    private void logAndThrowException(Level level, String message, Exception ex) {
        Logger.getLogger(HtmlOutputWriter.class.getName())
                    .log(level, message, ex);
        throw new DataProcessingException(message, ex);
    }
}
