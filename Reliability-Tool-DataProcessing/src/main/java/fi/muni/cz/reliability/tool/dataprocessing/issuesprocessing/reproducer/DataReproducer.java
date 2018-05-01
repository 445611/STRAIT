package fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.reproducer;

import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.Filter;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.FilterByLabel;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.FilterOutOpened;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.modeldata.DefectsCounter;
import fi.muni.cz.reliability.tool.dataprocessing.issuesprocessing.modeldata.DefectsCounterImpl;
import fi.muni.cz.reliability.tool.dataprovider.GeneralIssue;
import fi.muni.cz.reliability.tool.dataprocessing.persistence.GeneralIssuesSnapshot;
import fi.muni.cz.reliability.tool.models.GOModel;
import fi.muni.cz.reliability.tool.models.Model;

import java.util.List;
import java.util.Map;
import org.apache.commons.math3.util.Pair;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class DataReproducer {
    
    /**
     * TODO
     * @param snapshot TODO
     * @return TODO
     */
    public Map<String, Double> getReproducedData(GeneralIssuesSnapshot snapshot) {
        Filter issuesFilterByLabel = new FilterByLabel(snapshot.getFilteringWords());
        List<GeneralIssue> filtered = issuesFilterByLabel.filter(snapshot.getListOfGeneralIssues());
        Filter issuesFilterClosed = new FilterOutOpened();
        filtered = issuesFilterClosed.filter(filtered);
        
        
        DefectsCounter counter = new DefectsCounterImpl(snapshot.getTypeOfTimeToSplitTestInto(),
                snapshot.getHowManyTimeUnitsToAdd(), snapshot.getStartOfTesting(), 
                snapshot.getEndOfTesting());
        
        List<Pair<Integer, Integer>> countedWeeks = counter.spreadDefectsIntoPeriodsOfTime(filtered);
        List<Pair<Integer, Integer>> countedWeeksWithTotal = counter.countTotalDefectsForPeriodsOfTime(countedWeeks);
        
        Model model = new GOModel(new double[]{1,1});
        Map<String, Double> params = model.calculateFunctionParametersOfModel(countedWeeksWithTotal);
        
        return params;
    }   
}
