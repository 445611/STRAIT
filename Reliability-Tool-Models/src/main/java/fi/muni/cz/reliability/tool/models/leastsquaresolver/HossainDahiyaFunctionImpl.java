package fi.muni.cz.reliability.tool.models.leastsquaresolver;

import org.apache.commons.math3.analysis.MultivariateMatrixFunction;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class HossainDahiyaFunctionImpl extends FunctionAbstract {
    
    /**
     * Initialize variables.
     */
    public HossainDahiyaFunctionImpl() {
        super();
    }
    
    @Override
    protected double getVectorFunction(double[] variables, int index) {
       return (variables[0] *  (1 - Math.exp( - variables[1] * x.get(index))))
               /(1 + variables[2] * Math.exp( - variables[1] * x.get(index)));
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
             * Calculate and jacobian
             * @param variables parameters of model function
             * @return jacobian jacobian of the model function
             */
            private double[][] jacobian(double[] variables) {
                double[][] jacobian = new double[x.size()][3];
                for (int i = 0; i < jacobian.length; ++i) {
                    jacobian[i][0] = getJacobianWithRespectToFirstParamFunction(variables, i);
                    jacobian[i][1] = getJacobianWithRespectToSecondParamFunction(variables, i);
                    jacobian[i][2] = getJacobianWithRespectToThirdParamFunction(variables, i);
                }
                return jacobian;
            }
        };
    }
    
    @Override
    protected double getJacobianWithRespectToFirstParamFunction(double[] variables, int index) {
        double nominator = (Math.exp(variables[1]*x.get(index)) - 1);
        double denominator = (Math.exp(variables[1]*x.get(index)) + variables[2]);
        /*if (Double.isNaN(nominator / denominator) || Double.isInfinite(nominator / denominator)) {
            return Double.MAX_VALUE;
        }*/
        return nominator / denominator;
    }

    @Override
    protected double getJacobianWithRespectToSecondParamFunction(double[] variables, int index) {
        double nominator = variables[0] * (variables[2] + 1) * x.get(index) * Math.exp(variables[1]*x.get(index));
        double denominator = (Math.exp(variables[1]*x.get(index)) + variables[2]) * (Math.exp(variables[1]*x.get(index)) + variables[2]);
        /*if (Double.isNaN(nominator / denominator) || Double.isInfinite(nominator / denominator)) {
            return Double.MAX_VALUE;
        }*/
        return nominator / denominator;

        /*return (variables[0] *(variables[2] + 1) * x.get(index) * Math.exp(variables[1]*x.get(index))) 
                / Math.pow(Math.exp((variables[1]*x.get(index)) + variables[2]), 2);*/
    }
    
    private double getJacobianWithRespectToThirdParamFunction(double[] variables, int index) {
        double nominator = variables[0] * (Math.exp(variables[1]*x.get(index)) - 1);
        double denominator = (Math.exp(variables[1]*x.get(index)) + variables[2]) * (Math.exp(variables[1]*x.get(index)) + variables[2]);
        /*if (Double.isNaN(- nominator / denominator) || Double.isInfinite(- nominator / denominator)) {
            return Double.MAX_VALUE;
        }*/
        return (- nominator / denominator );
        /*return - (variables[0] * (Math.exp(variables[1]*x.get(index)) - 1)) 
                / Math.pow(Math.exp((variables[1]*x.get(index)) + variables[2]), 2);*/
    }
}