package fi.muni.cz.reliability.tool.models.leastsquaresolver;

import java.util.List;
import org.apache.commons.math3.util.Pair;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class GOLeastSquaresSolver implements LeastSquaresSolver {

    @Override
    public double[] optimize(double[] startParameters, List<Pair<Integer, Integer>> listOfData) {
        Rengine re= new Rengine(new String[] {"–no-save"}, false, null);
        //"xvalues = c(1,2,3,4,5,6,7,8,9,10,)"
        //"yvalues = c(8,16,22,28,34,38,43,46,50,52,)"
        //"model <- nls(yvalues ~ a*(1 - exp(-b*xvalues)), 
        //start = list(a = %d,b = %d), lower = list(a = 0, b = 0), algorithm = \"port\")"
        String a = String.format("xvalues = c(%s)", getXValues(listOfData));
        String b = String.format("yvalues = c(%s)", getYValues(listOfData));
        String c = String.format("model <- nls(yvalues ~ a*(1 - exp(-b*xvalues)), "
                + "start = list(a = %d,b = %d), "
                + "lower = list(a = 0, b = 0), "
                + "algorithm = \"port\")",(int) startParameters[0],(int) startParameters[1]);
        re.eval(a);
        re.eval(b);
        re.eval(c);
        REXP result = re.eval("confint(model)");
        double[] d = result.asDoubleArray();
        return new double[]{d[2], d[3]};
    }
    
    private String getXValues(List<Pair<Integer, Integer>> listOfData) {
        String out = new String();
        for (Pair<Integer, Integer> pair: listOfData) {
            if (pair != listOfData.get(listOfData.size() - 1)) {
                out = out + pair.getFirst() + ',';
            } else {
                out = out + pair.getFirst();
            }
        }
        return out;
    }
    
    private String getYValues(List<Pair<Integer, Integer>> listOfData) {
        String out = new String();
        for (Pair<Integer, Integer> pair: listOfData) {
            if (pair != listOfData.get(listOfData.size() - 1)) {
                out = out + pair.getSecond()+ ',';
            } else {
                out = out + pair.getSecond();
            }
        }
        return out;
    }
}