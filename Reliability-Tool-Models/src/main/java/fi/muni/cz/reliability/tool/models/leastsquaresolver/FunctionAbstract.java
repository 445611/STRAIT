package fi.muni.cz.reliability.tool.models.leastsquaresolver;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.analysis.MultivariateMatrixFunction;
import org.apache.commons.math3.analysis.MultivariateVectorFunction;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public abstract class FunctionAbstract implements Function {
    
    protected final List<Double> x;
    protected final List<Double> y;

    /**
     * Initialize variables.
     */
    public FunctionAbstract() {
        this.x = new ArrayList<>();
        this.y = new ArrayList<>();
    }

    @Override
    public void addPoint(double xin, double yin) {
        x.add(xin);
        y.add(yin);
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
                values[i] = getVectorFunction(variables, i);
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
                    jacobian[i][0] = getJacobianWithRespectToFirstParamFunction(variables, i);
                    jacobian[i][1] = getJacobianWithRespectToSecondParamFunction(variables, i);
                }
                return jacobian;
            }
        };
    }

    /**
     * Get value for i-th element of function.
     * 
     * @param variables for fuction
     * @param index     index of element
     * @return          value of function
     */
    protected abstract double getVectorFunction(double[] variables, int index);
    
    /**
     * Jacobian with respect to first paramter.
     * 
     * @param variables function variables.
     * @param index     index of element.
     * @return          value of jacobian.
     */
    protected abstract double getJacobianWithRespectToFirstParamFunction(double[] variables, int index);
    
    /**
     * Jacobian with respect to second parameter
     * 
     * @param variables function variables.
     * @param index     index of element.
     * @return          value of jacobian.
     */
    protected abstract double getJacobianWithRespectToSecondParamFunction(double[] variables, int index);
}