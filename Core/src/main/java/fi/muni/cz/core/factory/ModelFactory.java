package fi.muni.cz.core.factory;

import fi.muni.cz.core.ArgsParser;
import fi.muni.cz.core.exception.InvalidInputException;
import fi.muni.cz.models.*;
import fi.muni.cz.models.leastsquaresolver.*;
import fi.muni.cz.models.testing.GoodnessOfFitTest;
import org.apache.commons.math3.util.Pair;
import org.rosuda.JRI.Rengine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class ModelFactory {
    
    public static final String  GOEL_OKUMOTO = "go";
    public static final String  GOEL_OKEMURA_SSHAPED = "gos";
    public static final String  MUSA_OKUMOTO = "mo";
    public static final String  DUANE = "du";
    public static final String  HOSSAIN_DAHIYA = "hd";
    public static final String  WEIBULL = "we";
    public static final String  YAMADA_EXPONENTIAL = "ye";
    public static final String  YAMADA_RALEIGH = "yr";
    public static final String  LOG_LOGISITC = "ll";
    public static final String EMPTY_MODEL = "em";

    public static final String SOLVER_LEAST_SQUARES = "ls";
    public static final String SOLVER_MAXIMUM_LIKELIHOOD = "ml";
        
    private static Rengine rengine;

    public static void setREngine(Rengine rEngine) {
        rengine = rEngine;
    }

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
                models.add(ModelFactory.getModel(countedWeeksWithTotal, goodnessOfFitTest, modelArg, parser));
            }
        } else {
            models.add(ModelFactory.getModel(countedWeeksWithTotal, goodnessOfFitTest, 
                    ModelFactory.GOEL_OKUMOTO, parser));
            models.add(ModelFactory.getModel(countedWeeksWithTotal, goodnessOfFitTest, 
                    ModelFactory.GOEL_OKEMURA_SSHAPED, parser));
            models.add(ModelFactory.getModel(countedWeeksWithTotal, goodnessOfFitTest, 
                    ModelFactory.HOSSAIN_DAHIYA, parser));
            models.add(ModelFactory.getModel(countedWeeksWithTotal, goodnessOfFitTest, 
                    ModelFactory.MUSA_OKUMOTO, parser));
            models.add(ModelFactory.getModel(countedWeeksWithTotal, goodnessOfFitTest, 
                    ModelFactory.DUANE, parser));
            models.add(ModelFactory.getModel(countedWeeksWithTotal, goodnessOfFitTest,
                    ModelFactory.WEIBULL, parser));
            models.add(ModelFactory.getModel(countedWeeksWithTotal, goodnessOfFitTest,
                    ModelFactory.YAMADA_EXPONENTIAL, parser));
            models.add(ModelFactory.getModel(countedWeeksWithTotal, goodnessOfFitTest,
                    ModelFactory.YAMADA_RALEIGH, parser));
            models.add(ModelFactory.getModel(countedWeeksWithTotal, goodnessOfFitTest,
                    ModelFactory.LOG_LOGISITC, parser));
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
            GoodnessOfFitTest goodnessOfFitTest, String modelArg, ArgsParser parser) throws InvalidInputException {
        switch (modelArg) {
            case GOEL_OKUMOTO:
                return new GOModelImpl(countedWeeksWithTotal, goodnessOfFitTest,
                        getSolverBySolverArgument(parser, GOLeastSquaresSolver.class));
            case GOEL_OKEMURA_SSHAPED:
                return new GOSShapedModelImpl(countedWeeksWithTotal, goodnessOfFitTest,
                        getSolverBySolverArgument(parser, GOSShapedLeastSquaresSolver.class));
            case MUSA_OKUMOTO:
                return new MusaOkumotoModelImpl(countedWeeksWithTotal, goodnessOfFitTest,
                        getSolverBySolverArgument(parser, MusaOkumotoLeastSquaresSolver.class));
            case DUANE:
                return new DuaneModelImpl(countedWeeksWithTotal, goodnessOfFitTest,
                        getSolverBySolverArgument(parser, DuaneLeastSquaresSolver.class));
            case HOSSAIN_DAHIYA:
                return new HossainDahiyaModelImpl(countedWeeksWithTotal, goodnessOfFitTest,
                        getSolverBySolverArgument(parser, HossainDahiyaLeastSquaresSolver.class));
            case WEIBULL:
                return new WeibullModelImpl(countedWeeksWithTotal, goodnessOfFitTest,
                        getSolverBySolverArgument(parser, WeibullLeastSquaresSolver.class));
            case YAMADA_EXPONENTIAL:
                return new YamadaExponentialModelImpl(countedWeeksWithTotal, goodnessOfFitTest,
                        getSolverBySolverArgument(parser, YamadaExponentialLeastSquaresSolver.class));
            case YAMADA_RALEIGH:
                return new YamadaRaleighModelImpl(countedWeeksWithTotal, goodnessOfFitTest,
                        getSolverBySolverArgument(parser, YamadaRaleighLeastSquaresSolver.class));
            case LOG_LOGISITC:
                return new LogLogisticModelImpl(countedWeeksWithTotal, goodnessOfFitTest,
                        getSolverBySolverArgument(parser, LogLogisticLeastSquaresSolver.class));
            case EMPTY_MODEL:
                return new EmptyModelImpl(countedWeeksWithTotal, goodnessOfFitTest,
                        getSolverBySolverArgument(parser, EmptyLeastSquaresSolver.class));
            default:
                throw new InvalidInputException(Arrays.asList("No such model implemented: '" + modelArg + "'")); 
        }
    }

    private static <T> T getSolverBySolverArgument(ArgsParser parser, Class<T> solverClass)
            throws InvalidInputException {
        try {
            if (parser.hasOptionSolver()) {
                switch (parser.getOptionValueSolver()) {
                    case SOLVER_LEAST_SQUARES:
                        return solverClass.getDeclaredConstructor(Rengine.class).newInstance(rengine);
                    case SOLVER_MAXIMUM_LIKELIHOOD:
                        // To be implemented
                        throw new InvalidInputException(Arrays.asList("No such solver implemented: '"
                                + parser.getOptionValueSolver() + "'"));
                    default:
                        throw new InvalidInputException(Arrays.asList("No such solver implemented: '"
                                + parser.getOptionValueSolver() + "'"));
                }
            } else {
                return solverClass.getDeclaredConstructor(Rengine.class).newInstance(rengine);
            }
        } catch (ReflectiveOperationException ex) {
            throw new IllegalArgumentException(ex);
        }

    }
}
