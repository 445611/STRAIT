package fi.muni.cz.reliability.tool.core;

import fi.muni.cz.reliability.tool.core.exception.InvalidInputException;
import fi.muni.cz.reliability.tool.dataprovider.exception.DataProviderException;
import fi.muni.cz.reliability.tool.dataprovider.utils.GitHubUrlParser;
import fi.muni.cz.reliability.tool.dataprovider.utils.ParsedUrlData;
import fi.muni.cz.reliability.tool.dataprovider.utils.UrlParser;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class ArgsParser {
    
    private static final String PREDICTION_SIGN = "-p";
    
    private ParsedUrlData parsedUrlData;
    private boolean shouldPredict;
    private int predictionLength;
    
    /**
     * Parse and check input arguments.
     * 
     * @param args arguments to check and parse.
     * @throws InvalidInputException If some error occures.
     */
    public void parse(String[] args) throws InvalidInputException {
        List<String> errors = new ArrayList<>();
        readUrl(args[0], errors);
        readPredictionSign(args[1], errors);
        readPredictionLength(args[2], errors);
        if (!errors.isEmpty()) {
            throw new InvalidInputException(errors);
        }
    }

    private void readUrl(String arg, List<String> errors) {
        UrlParser parser = new GitHubUrlParser();
        try {
            parsedUrlData = parser.parseUrlAndCheck(arg);
        } catch (DataProviderException ex) {
            errors.add("URL " + arg + " is not valid.");
        }
    }

    private void readPredictionSign(String arg, List<String> errors) {
        if (!arg.toLowerCase().equals(PREDICTION_SIGN)) {
            errors.add("Sign for prediction " + arg + " is not a '-p'.");
            return;
        }
        shouldPredict = true;
    }

    private void readPredictionLength(String arg, List<String> errors) {
        int length;
        try {
            length = Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            errors.add("Input for length " + arg + " is not a number.");
            return;
        }
        this.predictionLength = length;
    }

    public ParsedUrlData getParsedUrlData() {
        return parsedUrlData;
    }

    public boolean isPredict() {
        return shouldPredict;
    }

    public int getPredictionLength() {
        return predictionLength;
    }
}