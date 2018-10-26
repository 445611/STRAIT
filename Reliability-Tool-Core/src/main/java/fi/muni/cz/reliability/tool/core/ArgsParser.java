package fi.muni.cz.reliability.tool.core;

import fi.muni.cz.reliability.tool.core.exception.InvalidInputException;
import fi.muni.cz.reliability.tool.dataprovider.exception.DataProviderException;
import fi.muni.cz.reliability.tool.dataprovider.utils.GitHubUrlParser;
import fi.muni.cz.reliability.tool.dataprovider.utils.ParsedUrlData;
import fi.muni.cz.reliability.tool.dataprovider.utils.UrlParser;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class ArgsParser {

    private static final String GITHUB_HOST_NAME = "github.com";
    
    private static final String EVALUATION_FLAG = "-eval";
    private boolean evaluate = false;
    
    private static final String LIST_FLAG = "-list";
    private boolean list = false;
    private boolean listAll = false;
    
    private static final String SAVE_FLAG = "-save";
    private boolean save = false;
    
    private ParsedUrlData parsedUrlData;
    private int predictionLength = 0;
    private String snapshotName;
    
    /**
     * Parse and check input arguments.
     * 
     * @param args arguments to check and parse.
     * @throws InvalidInputException If some error occures.
     */
    public void parse(String[] args) throws InvalidInputException {
        List<String> errors = new ArrayList<>();
        switch (args.length) {
            case 0:
                errors.add("Missing URL and Main flag.");
                break;
            case 1:
                readListAllFlag(args[0], errors);
                break;
            case 2:
                readUrlAndMainFlag(args, errors);
                break;
            case 3:
                readUrlAndMainFlag(args, errors);
                readPredictionLength(args[2], errors);
                break;
            default:
                errors.add("Wrong input parameters.");
        }
        if (!errors.isEmpty()) {
            throw new InvalidInputException(errors);
        }
    }
    
    private void readListAllFlag(String flag, List<String> errors) {
        if (flag.equals(LIST_FLAG)) {
            listAll = true;
        } else {
            errors.add("No '" + flag + "' flag allowed.");
        }
    }
    
    private void readUrlAndMainFlag(String[] args, List<String> errors) {
        readUrlOrSnapshotName(args[0], errors);
        readMainFlag(args[1], errors);
    }

    private void readMainFlag(String flag, List<String> errors) {
        switch (flag) {
            case EVALUATION_FLAG:
                evaluate = true;
                break;
            case LIST_FLAG:
                if (snapshotName != null) {
                    errors.add("Flag '" + flag + "' not allowed.");
                } else {
                    list = true;
                }
                break;
            case SAVE_FLAG:
                save = true;
                break;
            default:
                errors.add("Flag '" + flag + "' not allowed.");
        }
    }
    
    private void readUrlOrSnapshotName(String arg, List<String> errors) {
        try {
            URL url = new URL(arg);
            parseUrlByHostName(url.getHost(), arg, errors);
        } catch (DataProviderException | MalformedURLException ex) {
            snapshotName = arg;
        }
    }

    private void parseUrlByHostName(String hostName, String url, List<String> errors) {
        switch (hostName) {
            case GITHUB_HOST_NAME:
                UrlParser parser = new GitHubUrlParser();
                parsedUrlData = parser.parseUrlAndCheck(url);
                break;
            default:
                errors.add("'" + hostName + "' repositories not supported.");
        }
    }
    
    private void readPredictionLength(String arg, List<String> errors) {
        if (list || listAll) {
            errors.add("Input for length of prediction '" + arg + "' is not allowed with '-list' flag.");
        } else {
            int length;
        try {
            length = Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            errors.add("Input for length of prediction '" + arg + "' is not a number.");
            return;
        }
        predictionLength = length;
        }
    }

    public boolean isListAll() {
        return listAll;
    }

    public ParsedUrlData getParsedUrlData() {
        return parsedUrlData;
    }

    public boolean isList() {
        return list;
    }
    
    public String getSnapshotName() {
        return snapshotName;
    }
    
    public int getPredictionLength() {
        return predictionLength;
    }

    public boolean isEvaluate() {
        return evaluate;
    }

    public boolean isSave() {
        return save;
    }
}