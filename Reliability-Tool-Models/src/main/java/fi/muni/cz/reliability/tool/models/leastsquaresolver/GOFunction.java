package fi.muni.cz.reliability.tool.models.leastsquaresolver;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.analysis.MultivariateMatrixFunction;
import org.apache.commons.math3.analysis.MultivariateVectorFunction;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class GOFunction implements Function {
    
    private final List<Double> x;
    private final List<Double> y;

    /**
     * Initialize variables.
     */
    public GOFunction() {
        this.x = new ArrayList<>();
        this.y = new ArrayList<>();
    }
    
    @Override
    public void addPoint(double xin, double yin) {
        this.x.add(xin);
        this.y.add(yin);
    }
    
    @Override
    public double[] calculateTarget() {
        double[] target = new double[y.size()];
        for (int i = 0; i < y.size(); i++) {
            target[i] = y.get(i);
        }
        return target;
    }
    
    @Override
    public MultivariateVectorFunction getMultivariateVectorFunction() {
        return (double[] variables) -> {
            double[] values = new double[x.size()];
            for (int i = 0; i < values.length; ++i) {
                values[i] = variables[0] *  (1 - Math.exp( - variables[1] * x.get(i)));
            }
            return values;
        };
    }
    
    @Override
    public MultivariateMatrixFunction getMultivariateMatrixFunction() {
        return new MultivariateMatrixFunction() {
            @Override
            public double[][] value(double[] point)
                throws IllegalArgumentException {
                return jacobian(point);
            }

            /**
             * calculate and retjacobian
             * @param variables parameters of model function
             * @return jacobian jacobian of the model function
             */
            private double[][] jacobian(double[] variables) {
                double[][] jacobian = new double[x.size()][2];
                for (int i = 0; i < jacobian.length; ++i) {
                    jacobian[i][0] = 1 - Math.exp(-variables[1]*x.get(i));
                    jacobian[i][1] = variables[0] * x.get(i) * Math.exp(-variables[1]*x.get(i));
                }
                return jacobian;
            }
        };
    }
}
