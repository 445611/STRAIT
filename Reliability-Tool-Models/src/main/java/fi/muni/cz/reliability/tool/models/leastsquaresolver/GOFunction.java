
package fi.muni.cz.reliability.tool.models.leastsquaresolver;

import java.util.List;
import org.apache.commons.math3.analysis.MultivariateMatrixFunction;
import org.apache.commons.math3.analysis.MultivariateVectorFunction;

/**
 * @author Radoslav Micko <445611@muni.cz>
 */
public class GOFunction {
    
    private List<Double> x;
    private List<Double> y;

    /**
     * TODO
     * 
     * 
     * 
     * 
     * 
     * @param x SDFS
     * @param y  SDF
     */
    public GOFunction(List<Double> x, List<Double> y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * TODO 
     * 
     * 
     * 
     * 
     * 
     * @param xin AFS
     * @param yin SDFSDF
     */
    public void addPoint(double xin, double yin) {
        this.x.add(xin);
        this.y.add(yin);
    }
    
    /**
     * TODO
     * 
     * 
     * 
     * 
     * @return SDFSDF
     */
    public double[] calculateTarget() {
        double[] target = new double[y.size()];
        for (int i = 0; i < y.size(); i++) {
            target[i] = y.get(i);
        }
        return target;
    }
    
    /**
     * Define model function and return values
     * @return the values of model function by input data
     */
    public MultivariateVectorFunction retMVF() {
        return (double[] variables) -> {
            double[] values = new double[x.size()];
            for (int i = 0; i < values.length; ++i) {
                values[i] = variables[0] *  (1 - Math.exp( - variables[1] * x.get(i)));
            }
            return values;
        };
    }
    
    /**
     * Return the jacobian of the model function
     * @return the jacobian
     */
    public MultivariateMatrixFunction retMMF() {
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
