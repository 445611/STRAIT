package fi.muni.cz.reliability.tool.dataprocessing.output;

import fi.muni.cz.reliability.tool.dataprovider.utils.GitHubUrlParser;
import fi.muni.cz.reliability.tool.dataprovider.utils.ParsedUrlData;
import fi.muni.cz.reliability.tool.dataprovider.utils.UrlParser;
import freemarker.template.Configuration;
import java.util.Date;
import java.util.List;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public abstract class OutputWriterAbstract implements OutputWriter {

    public abstract void writeOutputDataToFile(String url, List<Pair<Integer, Integer>> listOfPairsData, String fileName);

    protected OutputData prepareOutputData(String url, List<Pair<Integer, Integer>> listOfPairsData) {
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
    
    //private Configuration 
}
