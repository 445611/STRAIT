package fi.muni.cz.core.factory;

import fi.muni.cz.core.ArgsParser;
import fi.muni.cz.core.exception.InvalidInputException;
import fi.muni.cz.models.*;
import fi.muni.cz.models.leastsquaresolver.*;
import fi.muni.cz.models.testing.GoodnessOfFitTest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.math3.util.Pair;
import org.rosuda.JRI.Rengine;

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

    public static final String SOLVER_LEAST_SQUARES = "ls";
    public static final String SOLVER_MAXIMUM_LIKELIHOOD = "ml";
        
    private static final Rengine R_ENGINE = new Rengine(new String[] {"-–no-save"}, false, null);
    
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
            // TODO Add new models + to parser as arguments

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
                        getGOSolverBySolverArgument(parser));
            case GOEL_OKEMURA_SSHAPED:
                return new GOSShapedModelImpl(countedWeeksWithTotal, goodnessOfFitTest, 
                        getGOSShapedSolverBySolverArgument(parser)); 
            case MUSA_OKUMOTO:
                return new MusaOkumotoModelImpl(countedWeeksWithTotal, goodnessOfFitTest, 
                        getMusaOkumotoSolverBySolverArgument(parser)); 
            case DUANE:
                return new DuaneModelImpl(countedWeeksWithTotal, goodnessOfFitTest, 
                        getDuaneSolverBySolverArgument(parser));
            case HOSSAIN_DAHIYA:
                return new HossainDahiyaModelImpl(countedWeeksWithTotal, goodnessOfFitTest, 
                        getHossainDahiyaSolverBySolverArgument(parser));
            case WEIBULL:
                return new WeibullModelImpl(countedWeeksWithTotal, goodnessOfFitTest,
                        getWeibullSolverBySolverArgument(parser));
            default:
                throw new InvalidInputException(Arrays.asList("No such model implemented: '" + modelArg + "'")); 
        }
    }

    private static Solver getWeibullSolverBySolverArgument(ArgsParser parser) throws InvalidInputException {
        if (parser.hasOptionSolver()) {
            switch (parser.getOptionValueSolver()) {
                case SOLVER_LEAST_SQUARES:
                    return new WeibullLeastSquaresSolver(R_ENGINE);
                case SOLVER_MAXIMUM_LIKELIHOOD:
                    throw new InvalidInputException(Arrays.asList("No such solver implemented: '"
                            + parser.getOptionValueSolver() + "'"));
                default:
                    throw new InvalidInputException(Arrays.asList("No such solver implemented: '"
                            + parser.getOptionValueSolver() + "'"));
            }
        } else {
            return new WeibullLeastSquaresSolver(R_ENGINE);
        }
    }

    private static Solver getGOSolverBySolverArgument(ArgsParser parser) throws InvalidInputException {
        if (parser.hasOptionSolver()) {
            switch (parser.getOptionValueSolver()) {
                case SOLVER_LEAST_SQUARES:
                    return new GOLeastSquaresSolver(R_ENGINE);
                case SOLVER_MAXIMUM_LIKELIHOOD:
                    throw new InvalidInputException(Arrays.asList("No such solver implemented: '" 
                            + parser.getOptionValueSolver() + "'"));
                default:
                    throw new InvalidInputException(Arrays.asList("No such solver implemented: '" 
                            + parser.getOptionValueSolver() + "'"));
            }
        } else {
            return new GOLeastSquaresSolver(R_ENGINE);
        }
    }
    
    private static Solver getGOSShapedSolverBySolverArgument(ArgsParser parser) throws InvalidInputException {
        if (parser.hasOptionSolver()) {
            switch (parser.getOptionValueSolver()) {
                case SOLVER_LEAST_SQUARES:
                    return new GOSShapedLeastSquaresSolver(R_ENGINE);
                case SOLVER_MAXIMUM_LIKELIHOOD:
                    throw new InvalidInputException(Arrays.asList("No such solver implemented: '" 
                            + parser.getOptionValueSolver() + "'"));
                default:
                    throw new InvalidInputException(Arrays.asList("No such solver implemented: '" 
                            + parser.getOptionValueSolver() + "'"));
            }
        } else {
            return new GOSShapedLeastSquaresSolver(R_ENGINE);
        }
    }
    
    private static Solver getDuaneSolverBySolverArgument(ArgsParser parser) throws InvalidInputException {
        if (parser.hasOptionSolver()) {
            switch (parser.getOptionValueSolver()) {
                case SOLVER_LEAST_SQUARES:
                    return new DuaneLeastSquaresSolver(R_ENGINE);
                case SOLVER_MAXIMUM_LIKELIHOOD:
                    throw new InvalidInputException(Arrays.asList("No such solver implemented: '" 
                            + parser.getOptionValueSolver() + "'"));
                default:
                    throw new InvalidInputException(Arrays.asList("No such solver implemented: '" 
                            + parser.getOptionValueSolver() + "'"));
            }
        } else {
            return new DuaneLeastSquaresSolver(R_ENGINE);
        }
    }
    
    private static Solver getMusaOkumotoSolverBySolverArgument(ArgsParser parser) throws InvalidInputException {
        if (parser.hasOptionSolver()) {
            switch (parser.getOptionValueSolver()) {
                case SOLVER_LEAST_SQUARES:
                    return new MusaOkumotoLeastSquaresSolver(R_ENGINE);
                case SOLVER_MAXIMUM_LIKELIHOOD:
                    throw new InvalidInputException(Arrays.asList("No such solver implemented: '" 
                            + parser.getOptionValueSolver() + "'"));
                default:
                    throw new InvalidInputException(Arrays.asList("No such solver implemented: '" 
                            + parser.getOptionValueSolver() + "'"));
            }
        } else {
            return new MusaOkumotoLeastSquaresSolver(R_ENGINE);
        }
    }
    
    private static Solver getHossainDahiyaSolverBySolverArgument(ArgsParser parser) throws InvalidInputException {
        if (parser.hasOptionSolver()) {
            switch (parser.getOptionValueSolver()) {
                case SOLVER_LEAST_SQUARES:
                    return new HossainDahiyaLeastSquaresSolver(R_ENGINE);
                case SOLVER_MAXIMUM_LIKELIHOOD:
                    throw new InvalidInputException(Arrays.asList("No such solver implemented: '" 
                            + parser.getOptionValueSolver() + "'"));
                default:
                    throw new InvalidInputException(Arrays.asList("No such solver implemented: '" 
                            + parser.getOptionValueSolver() + "'"));
            }
        } else {
            return new HossainDahiyaLeastSquaresSolver(R_ENGINE);
        }
    }
}
