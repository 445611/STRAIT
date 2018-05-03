package fi.muni.cz.reliability.tool.dataprocessing.output;

import fi.muni.cz.reliability.tool.dataprocessing.exception.DataProcessingException;
import fi.muni.cz.reliability.tool.dataprovider.utils.GitHubUrlParser;
import fi.muni.cz.reliability.tool.dataprovider.utils.ParsedUrlData;
import fi.muni.cz.reliability.tool.dataprovider.utils.UrlParser;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public abstract class OutputWriterAbstract implements OutputWriter {

    private static final String HTML_TEMPLATE_ONE = "template_one.ftlh";
    protected final Configuration configuration;
    
    /**
     * Constructor that create freemark template configuration.
     */
    public OutputWriterAbstract() {
        configuration = getConfiguration();
    }

    @Override
    public abstract void writeOutputDataToFile(OutputData outputData, String fileName);

    @Override
    public OutputData prepareOutputData(String url, List<Pair<Integer, Integer>> listOfPairsData) {
        UrlParser parser = new GitHubUrlParser();
        ParsedUrlData parsedUrldata = parser.parseUrlAndCheck(url);
        OutputData data = new OutputData();
        data.setCreatedAt(new Date());
        data.setRepositoryName(parsedUrldata.getRepositoryName());
        data.setTotalNumberOfDefects(listOfPairsData.stream().mapToInt(a -> a.getSecond()).sum());
        data.setUrl(url);
        data.setUserName(parsedUrldata.getUserName());
        data.setWeeksAndDefects(listOfPairsData);
        return data;
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
