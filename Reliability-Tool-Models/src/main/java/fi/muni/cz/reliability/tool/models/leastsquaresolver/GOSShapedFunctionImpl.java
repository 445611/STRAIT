package fi.muni.cz.reliability.tool.models.leastsquaresolver;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class GOSShapedFunctionImpl extends FunctionAbstract {
    
    /**
     * Initialize variables.
     */
    public GOSShapedFunctionImpl() {
        super();
    }
    
    @Override
    protected double getVectorFunction(double[] variables, int index) {
       return variables[0] *  (1 - (1 + variables[1] * x.get(index)) * Math.exp(- variables[1] * x.get(index)));
    }
    
    @Override
    protected double getJacobianWithRespectToFirstParamFunction(double[] variables, int index) {
        return 1 - Math.exp(-variables[1]*x.get(index)) * (variables[1] * x.get(index) + 1);
    }

    @Override
    protected double getJacobianWithRespectToSecondParamFunction(double[] variables, int index) {
        return variables[0] * variables[1] * Math.pow(x.get(index), 2) * Math.exp(-variables[1]*x.get(index));
    }
}