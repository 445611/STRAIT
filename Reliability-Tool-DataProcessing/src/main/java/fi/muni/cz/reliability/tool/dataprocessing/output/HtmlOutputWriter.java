package fi.muni.cz.reliability.tool.dataprocessing.output;

import fi.muni.cz.reliability.tool.dataprocessing.exception.DataProcessingException;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.BufferedWriter;
import java.io.File;
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
    private final Configuration configuration;
    
    /**
     * Constructor that create freemark template configuration.
     */
    public HtmlOutputWriter() {
        configuration = getConfiguration();
    }
    
    @Override
    public void writeOutputDataToFile(OutputData outputData, String fileName) {
        Map<String, Object> root = new HashMap<>();
        root.put("data", outputData);
        writeTemplateToFile(root, fileName);
    }
    
    private void writeTemplateToFile(Map<String, Object> root, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream(fileName + ".html"), StandardCharsets.UTF_8));) {
            Template template = getTemplateFromConfiguration();
            template.process(root, writer);
        } catch (IOException ex) {
            logAndThrowException("Error occured during writing to file.", ex);
        } catch (TemplateException ex) {
            logAndThrowException("Template error.", ex);
        }
    }
    
    private Template getTemplateFromConfiguration() 
            throws MalformedTemplateNameException, ParseException, IOException {
        return configuration.getTemplate(TEMPLATE);
    }
    
    private void logAndThrowException(String message, Exception ex) {
        Logger.getLogger(HtmlOutputWriter.class.getName())
                    .log(Level.SEVERE, message, ex);
        throw new DataProcessingException(message, ex);
    }
    
    private Configuration getConfiguration() {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
        try {
            cfg.setDirectoryForTemplateLoading(getHtmlTemplateFile());
        } catch (IOException ex) {
            throw new DataProcessingException("No such html template.", ex);
        }
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        return cfg;
    }
    
    private File getHtmlTemplateFile() {
        return new File(getClass().getClassLoader().getResource("templates").getFile());
    }
}