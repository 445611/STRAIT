package fi.muni.cz.reliability.tool.models;

import fi.muni.cz.reliability.tool.utils.Tuple;
import java.util.List;
import org.orangepalantir.leastsquares.Fitter;
import org.orangepalantir.leastsquares.Function;
import org.orangepalantir.leastsquares.fitters.NonLinearSolver;

/**
 * Goel-Okumoto (G-O) model
 * 
 * @author Radoslav Micko <445611@muni.cz>
 */
public class GOModel implements Model {

    @Override
    public double[] calculateFunctionParametersOfModel(List<Tuple<Integer, Integer>> list) {
        //double[][] x = {{0}, {1}, {2}, {3}, {4}, {5}};
        double[][] x = getXValues(list);
        //double[] y = {1.1, 0.9, 1.0, 1.35, 1.82, 2.5};
        double[] y = getYValues(list);
        
        Fitter fitter = new NonLinearSolver(getModelFunction());
        fitter.setData(x, y);
        fitter.setParameters(new double[]{1, 1});

        fitter.fitData();
        return fitter.getParameters();
    }
    
    private double[][] getXValues(List<Tuple<Integer, Integer>> list) {
        double[][] xValues = new double[list.size()][1];
        for (int i = 0; i < list.size(); i++) {
            xValues[i][0] = i + 1;
        }
        return xValues;
    }
    
    private double[] getYValues(List<Tuple<Integer, Integer>> list) {
        double[] xValues = new double[list.size()];
        int i = 0;
        for (Tuple<Integer, Integer> tuple: list) {
            xValues[i] = tuple.getB();
            i++;
        }
        return xValues;
    }
    
    private Function getModelFunction() {
        Function function = new Function(){
        
        @Override
        public double evaluate(double[] values, double[] parameters) {
            /*double A = parameters[0];
            double B = parameters[1];
            double x = values[0];
            return A*x*x + B*x;*/
            
            double a = parameters[0];
            double b = parameters[1];

            double x = values[0];
            double exp = Math.exp(-(b*x));
            
            return a*(1 - exp);
        }
        @Override
        public int getNParameters() {
            return 2;
            }

        @Override
        public int getNInputs() {
            return 1;
            }
        };
        return function;
    }
    
}
