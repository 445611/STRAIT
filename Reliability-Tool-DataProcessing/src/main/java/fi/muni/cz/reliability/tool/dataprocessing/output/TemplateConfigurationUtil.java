package fi.muni.cz.reliability.tool.dataprocessing.output;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class TemplateConfigurationUtil {
    
    /**
     * Get confuguration for template writer.
     * @return Configuration
     */
    public static Configuration getConfiguration() {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
        ClassTemplateLoader loader = new ClassTemplateLoader(new TemplateConfigurationUtil().getClass(), "/templates");
        cfg.setTemplateLoader(loader);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        return cfg;
    }
}