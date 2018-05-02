package fi.muni.cz.reliability.tool.dataprocessing.output;

import fi.muni.cz.reliability.tool.dataprovider.utils.GitHubUrlParser;
import fi.muni.cz.reliability.tool.dataprovider.utils.ParsedUrlData;
import fi.muni.cz.reliability.tool.dataprovider.utils.UrlParser;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.body;
import static j2html.TagCreator.h1;
import static j2html.TagCreator.head;
import static j2html.TagCreator.link;
import static j2html.TagCreator.title;
import static j2html.TagCreator.html;
import static j2html.TagCreator.document;
import static j2html.TagCreator.main;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.List;
import org.apache.commons.math3.util.Pair;


/**

 * @author Radoslav Micko, 445611@muni.cz
 */
public class HtmlOutputWriter implements OutputWriter {

    @Override
    public void writeOutputDataToFile(OutputData data, String fileName) {
        
        String s = document(html(
            head(
                title("Title"),
                link().withRel("stylesheet").withHref("/css/main.css")
            ),
            body(main(attrs("#main.content"),
            h1("Heading!"))
            )
        ));
        
        try(PrintStream out = new PrintStream(new FileOutputStream("Task.html"))){
            out.println(s);
        } catch (Exception ex) {
            
        }
        
    }

    @Override
    public OutputData prepareOutputData(String url, List<Pair<Integer, Integer>> countedPairs) {
        UrlParser parser = new GitHubUrlParser();
        ParsedUrlData parsedUrldata = parser.parseUrlAndCheck(url);
        OutputData data = new OutputData();
        data.setCreatedAt(new Date());
        data.setRepositoryName(parsedUrldata.getRepositoryName());
        data.setTotalNumberOfDefects(countedPairs.stream().mapToInt(a -> a.getSecond()).sum());
        data.setUrl(url);
        data.setUserName(parsedUrldata.getUserName());
        data.setWeeksAndDefects(countedPairs);
        return data;
    }

}
