package fi.muni.cz.reliability.tool.dataprocessing.output;

import fi.muni.cz.reliability.tool.dataprocessing.exception.DataProcessingException;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class HtmlOutputWriter implements OutputWriter {

    private static final String TEMPLATE_ONE = "template_one.html";
    private static final String TEMPLATE_TWO = "template_two.html";
    private static final String TEMPLATE_THREE = "template_three.html";
    private final Configuration configuration;
    private final boolean multipleGraphs;
    
    /**
     * Constructor that create freemark template configuration.
     * 
     * @param multipleGraphs    True if want to have multiple graphs for models.
     */
    public HtmlOutputWriter(boolean multipleGraphs) {
        configuration = TemplateConfigurationUtil.getConfiguration();
        this.multipleGraphs = multipleGraphs;
    }
    
    @Override
    public void writeOutputDataToFile(List<OutputData> outputData, String fileName) {
        Map<String, Object> root = new HashMap<>();
        if (outputData.size() == 1) {
            root.put("data", outputData.get(0));
        } else {
            root.put("dataList", outputData);
        }
        writeTemplateToFile(root, fileName);
    }
    
    private void writeTemplateToFile(Map<String, Object> root, String fileName) {
        if (root.get("data") == null) {
            fileName = fileName + " - Models comparison";
        }
        File file = new File("./output/" + fileName + ".html");
        file.getParentFile().mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream(file), StandardCharsets.UTF_8));) {
            Template template;
            if (root.get("data") != null) {
                template = getTemplateFromConfiguration(TEMPLATE_ONE);
            } else if (multipleGraphs){
                template = getTemplateFromConfiguration(TEMPLATE_TWO);
            } else {
                template = getTemplateFromConfiguration(TEMPLATE_THREE);
            }
            template.process(root, writer);
        } catch (IOException ex) {
            logAndThrowException("Error occured during writing to file.", ex);
        } catch (TemplateException ex) {
            logAndThrowException("Template error.", ex);
        }
    }
    
    private Template getTemplateFromConfiguration(String templateName) 
            throws MalformedTemplateNameException, ParseException, IOException {
        return configuration.getTemplate(templateName);
    }
    
    private void logAndThrowException(String message, Exception ex) {
        Logger.getLogger(HtmlOutputWriter.class.getName())
                    .log(Level.SEVERE, message, ex);
        throw new DataProcessingException(message, ex);
    }
}