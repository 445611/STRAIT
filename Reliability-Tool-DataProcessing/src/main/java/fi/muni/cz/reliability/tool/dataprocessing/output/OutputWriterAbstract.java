package fi.muni.cz.reliability.tool.dataprocessing.output;

import fi.muni.cz.reliability.tool.dataprovider.utils.GitHubUrlParser;
import fi.muni.cz.reliability.tool.dataprovider.utils.ParsedUrlData;
import fi.muni.cz.reliability.tool.dataprovider.utils.UrlParser;
import java.util.Date;
import java.util.List;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public abstract class OutputWriterAbstract implements OutputWriter {

    @Override
    public abstract void writeOutputDataToFile(OutputData outputData, String fileName);

    @Override
    public OutputData prepareOutputData(String url, List<Pair<Integer, Integer>> listOfPairsData) {
        UrlParser parser = new GitHubUrlParser();
        ParsedUrlData parsedUrldata = parser.parseUrlAndCheck(url);
        OutputData data = new OutputData();
        data.setCreatedAt(new Date());
        data.setRepositoryName(parsedUrldata.getRepositoryName());
        data.setTotalNumberOfDefects(listOfPairsData.get(listOfPairsData.size() - 1).getSecond());
        data.setUrl(url);
        data.setUserName(parsedUrldata.getUserName());
        data.setWeeksAndDefects(listOfPairsData);
        return data;
    }
}
