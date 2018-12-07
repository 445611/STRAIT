package fi.muni.cz.reliability.tool.models.leastsquaresolver;

import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.math3.util.Pair;
import org.rosuda.JRI.Rengine;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public abstract class SolverAbstract implements Solver {
    
    protected Rengine rEngine; 

    /**
     * Initialize Rengine.
     * @param rEngine Rengine.
     */
    public SolverAbstract(Rengine rEngine) {
        this.rEngine = rEngine;
    }
    
    /**
     * Create string of elements separated with comma.
     * 
     * @param list  list of integers to separate.
     * @return      string of elements separated with comma.
     */
    protected String getPreparedListWithCommas(List<Integer> list) {
        String out = new String();
        for (int i = 0; i < list.size(); i++) {
            if(i == list.size() - 1) {
                out = out + list.get(i);
            } else {
                out = out + list.get(i) + ",";
            }
        }
        return out;
    }
    
    /**
     * Pull out first element of list.
     * 
     * @param listOfData    list
     * @return              List of first elements.
     */
    protected List<Integer> getListOfFirstFromPair(List<Pair<Integer, Integer>> listOfData) {
        return listOfData.stream().map((pair) -> pair.getFirst()).collect(Collectors.toList());
    }
    
    /**
     * Pull out second element of list.
     * 
     * @param listOfData    list
     * @return              List of second elements.
     */
    protected List<Integer> getListOfSecondFromPair(List<Pair<Integer, Integer>> listOfData) {
        return listOfData.stream().map((pair) -> pair.getSecond()).collect(Collectors.toList());
    }
}