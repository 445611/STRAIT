package fi.muni.cz.reliability.tool.core.factory;

import fi.muni.cz.reliability.tool.core.ArgsParser;
import fi.muni.cz.reliability.tool.core.exception.InvalidInputException;
import fi.muni.cz.reliability.tool.models.DuaneModelImpl;
import fi.muni.cz.reliability.tool.models.GOModelImpl;
import fi.muni.cz.reliability.tool.models.GOSShapedModelImpl;
import fi.muni.cz.reliability.tool.models.HossainDahiyaModelImpl;
import fi.muni.cz.reliability.tool.models.Model;
import fi.muni.cz.reliability.tool.models.MusaOkumotoModelImpl;
import fi.muni.cz.reliability.tool.models.testing.GoodnessOfFitTest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class ModelFactory {
    
    public static final String  GOEL_OKUMOTO = "go";
    public static final String  GOEL_OKEMURA_SSHAPED = "gos";
    public static final String  MUSA_OKUMOTO = "mo";
    public static final String  DUANE = "du";
    public static final String  HOSSAIN_DAHIYA = "hd";

    /**
     * Get all Model to run.
     * 
     * @param countedWeeksWithTotal cumulative data.
     * @param goodnessOfFitTest     goodnes-of-fit.
     * @param parser                parsed CommandLine.
     * @return                      list of Models.
     * @throws InvalidInputException when there is no such model from cmdl.
     */
    public static List<Model> getModels(List<Pair<Integer, Integer>> countedWeeksWithTotal,
            GoodnessOfFitTest goodnessOfFitTest, ArgsParser parser) throws InvalidInputException {
        List<Model> models = new ArrayList<>();
        if (parser.hasOptionModels()) {
            for (String modelArg: parser.getOptionValuesModels()) {
                models.add(ModelFactory.getModel(countedWeeksWithTotal, goodnessOfFitTest, modelArg));
            }
        } else {
            models.add(ModelFactory.getModel(countedWeeksWithTotal, goodnessOfFitTest, ModelFactory.GOEL_OKUMOTO));
        }
        return models;
    }
    
    /**
     * Get Model for string value.
     * 
     * @param countedWeeksWithTotal cumulative data.
     * @param goodnessOfFitTest     goodnes-of-fit.
     * @param modelArg              represnetation of model.
     * @return Model
     * @throws InvalidInputException when there is no such implmented model.
     */
    private static Model getModel(List<Pair<Integer, Integer>> countedWeeksWithTotal,
            GoodnessOfFitTest goodnessOfFitTest, String modelArg) throws InvalidInputException {
        switch (modelArg) {
            case GOEL_OKUMOTO:
                return new GOModelImpl(countedWeeksWithTotal, goodnessOfFitTest);
            case GOEL_OKEMURA_SSHAPED:
                return new GOSShapedModelImpl(countedWeeksWithTotal, goodnessOfFitTest); 
            case MUSA_OKUMOTO:
                return new MusaOkumotoModelImpl(countedWeeksWithTotal, goodnessOfFitTest); 
            case DUANE:
                return new DuaneModelImpl(countedWeeksWithTotal, goodnessOfFitTest); 
            case HOSSAIN_DAHIYA:
                return new HossainDahiyaModelImpl(countedWeeksWithTotal, goodnessOfFitTest); 
            default:
                throw new InvalidInputException(Arrays.asList("No such model implemented: '" + modelArg + "'")); 
        }
    }
}
