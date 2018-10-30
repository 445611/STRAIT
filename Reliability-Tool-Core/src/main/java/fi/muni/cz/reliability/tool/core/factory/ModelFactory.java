package fi.muni.cz.reliability.tool.core.factory;

import fi.muni.cz.reliability.tool.models.DuaneModelImpl;
import fi.muni.cz.reliability.tool.models.GOModelImpl;
import fi.muni.cz.reliability.tool.models.GOSShapedModelImpl;
import fi.muni.cz.reliability.tool.models.HossainDahiyaModelImpl;
import fi.muni.cz.reliability.tool.models.Model;
import fi.muni.cz.reliability.tool.models.MusaOkumotoModelImpl;
import fi.muni.cz.reliability.tool.models.testing.GoodnessOfFitTest;
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
    
    public static Model getIssuesWriter(List<Pair<Integer, Integer>> countedWeeksWithTotal,
            GoodnessOfFitTest goodnessOfFitTest, String modulArg) {
        switch (modulArg) {
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
                return new GOModelImpl(countedWeeksWithTotal, goodnessOfFitTest); 
        }
    }
}
