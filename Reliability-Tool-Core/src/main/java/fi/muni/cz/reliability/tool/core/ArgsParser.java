package fi.muni.cz.reliability.tool.core;

import fi.muni.cz.reliability.tool.core.exception.InvalidInputException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class ArgsParser {
    
    //Initial Mandatory options
    public static final String OPT_URL = "url";
    public static final String OPT_SNAPSHOT_NAME = "sn";
    public static final String OPT_LIST_ALL_SNAPSHOTS = "asl";
    public static final String OPT_HELP = "h";
    public static final String OPT_CONFIG_FILE = "cf";
    
    //Rest of Mandatory options
    public static final String OPT_LIST_SNAPSHOTS = "sl";
    public static final String OPT_SAVE = "s";
    public static final String OPT_EVALUATE = "e";
    public static final String OPT_PREDICT = "p";
    public static final String OPT_FILTER_LABELS = "fl";
    public static final String OPT_FILTER_CLOSED = "fc";
    public static final String OPT_MODELS = "ms";
    public static final String OPT_OUT = "out";
    public static final String OPT_GRAPH_MULTIPLE = "gm";
    public static final String OPT_NEW_SNAPSHOT = "ns";
    
    //Configuraton file option
    private static final String FLAG_CONFIG_FILE = "-cf";
    
    private static CommandLine cmdl;
    private static Options options;
    
    /**
     * Parse and check input arguments.
     * 
     * @param args arguments to check and parse.
     * @throws InvalidInputException If some error occures.
     */
    public void parse(String[] args) throws InvalidInputException {
        List<String> errors = new ArrayList<>();
        getConfiguratedOptions();
        if (checkToReadFromConfigFile(args, errors)) {
            cmdl = parseArgumentsFromConfigFile(args[1], options, errors);
        } else {
            cmdl = parseArgumentsFromCommandLine(args, options, errors);
        }

        if (!errors.isEmpty()) {
            throw new InvalidInputException(errors);
        }
    }
   
    /**
     * Print argument opritons.
     */
    public void printHelp(){
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Help:", options);
    }
    
    private CommandLine parseArgumentsFromConfigFile(String path, Options options, List<String> errors) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
            String[] args = reader.readLine().split(" ");
            return parseArgumentsFromCommandLine(args, options, errors);
        } catch (IOException ex) {
            errors.add(ex.getMessage());
        }
        return null;
    }

    private CommandLine parseArgumentsFromCommandLine(String[] args, Options options, List<String> errors) {
        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);
            return cmd;
        } catch (ParseException ex) {
            errors.add(ex.getMessage());
        }
        return null;
    }
    
    private boolean checkToReadFromConfigFile(String[] args, List<String> errors) {
        if (args.length < 1) {
            errors.add("Missing options.");
            return false;
        }
        if (args[0].equals(ArgsParser.FLAG_CONFIG_FILE) && args.length != 2) {
            errors.add("Missing options.");
            return false;
        }
        return args[0].equals(ArgsParser.FLAG_CONFIG_FILE);
    }
    
    private void getConfiguratedOptions() {
        options = new Options();
        
        OptionGroup mandatoryOptionGroup = new OptionGroup();
        Option option = Option.builder(OPT_CONFIG_FILE).hasArg().
                argName("Path to file").desc("Configuration file.").build();
        mandatoryOptionGroup.addOption(option);
        option = Option.builder(OPT_URL).hasArg().argName("URL of repository").build();
        mandatoryOptionGroup.addOption(option);
        option = Option.builder(OPT_SNAPSHOT_NAME).longOpt("snapshotName").hasArg().argName("Name of snapshot").build();
        mandatoryOptionGroup.addOption(option);
        option = Option.builder(OPT_LIST_ALL_SNAPSHOTS).longOpt("allSnapshotsList").build();
        mandatoryOptionGroup.addOption(option);
        option = Option.builder(OPT_HELP).longOpt("help").build();
        mandatoryOptionGroup.addOption(option);
        mandatoryOptionGroup.isRequired();
        options.addOptionGroup(mandatoryOptionGroup);
        
        mandatoryOptionGroup = new OptionGroup();
        option = Option.builder(OPT_LIST_SNAPSHOTS).longOpt("snapshotsList").build();
        mandatoryOptionGroup.addOption(option);
        option = Option.builder(OPT_SAVE).longOpt("save").hasArg().argName("Format of data").
                desc("Save repository data to file with specified format.").build();
        mandatoryOptionGroup.addOption(option);
        option = Option.builder(OPT_EVALUATE).longOpt("evaluate")
                .desc("Evaluate repository data and persist to new snapshot with name.").build();
        mandatoryOptionGroup.addOption(option);
        options.addOptionGroup(mandatoryOptionGroup);
        
        option = Option.builder(OPT_PREDICT).longOpt("predict").type(Number.class).hasArg().argName("Number").
                desc("Number of test periods to predict.").build();
        options.addOption(option);
        option = Option.builder(OPT_NEW_SNAPSHOT).hasArg().argName("Name of new snapshot").
                desc("Name of new snapshot that will be persisted.").build();
        options.addOption(option);
        option = Option.builder(OPT_FILTER_LABELS).longOpt("filterLabel").optionalArg(true)
                .hasArgs().argName("Filtering labels").desc("Filter by specified labels.").build();
        options.addOption(option);
        option = Option.builder(OPT_FILTER_CLOSED).longOpt("filterClosed").desc("Filter closed.").build();
        options.addOption(option);
        option = Option.builder(OPT_MODELS).longOpt("models").hasArgs().argName("Model name").
                desc("Models to evaluate.").build();
        options.addOption(option);
        option = Option.builder(OPT_GRAPH_MULTIPLE).longOpt("graphMultiple")
                .desc("Data show in multiple graphs.").build();
        options.addOption(option);
        option = Option.builder(OPT_OUT).hasArg().desc("Output type.").build();
        options.addOption(option);
    }

    /**
     * Parsed command line.
     * @return CommandLine
     */
    public CommandLine getCmdl() {
        return cmdl;
    }

    /**
     * Configured options.
     * @return Options
     */
    public  Options getOptions() {
        return options;
    }
}