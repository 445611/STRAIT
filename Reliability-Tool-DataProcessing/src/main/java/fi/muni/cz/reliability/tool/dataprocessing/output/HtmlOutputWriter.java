package fi.muni.cz.reliability.tool.dataprocessing.output;

import fi.muni.cz.reliability.tool.dataprovider.utils.GitHubUrlParser;
import fi.muni.cz.reliability.tool.dataprovider.utils.ParsedUrlData;
import fi.muni.cz.reliability.tool.dataprovider.utils.UrlParser;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.body;
import static j2html.TagCreator.div;
import static j2html.TagCreator.h1;
import static j2html.TagCreator.head;
import static j2html.TagCreator.link;
import static j2html.TagCreator.title;
import static j2html.TagCreator.html;
import static j2html.TagCreator.document;
import static j2html.TagCreator.footer;
import static j2html.TagCreator.main;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.List;
import org.apache.commons.math3.util.Pair;


/**

 * @author Radoslav Micko, 445611@muni.cz
 */
public class HtmlOutputWriter extends OutputWriterAbstract {

    @Override
    public void writeOutputDataToFile(String url, List<Pair<Integer, Integer>> listOfPairsData, String fileName) {
        prepareOutputData(url, listOfPairsData);
    }
    
}
