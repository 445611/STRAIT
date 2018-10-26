package fi.muni.cz.reliability.tool.models.leastsquaresolver;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class MusaOkumotoFunctionImpl extends FunctionAbstract {
    
    /**
     * Initialize variables.
     */
    public MusaOkumotoFunctionImpl() {
        super();
    }
    
    @Override
    protected double getVectorFunction(double[] variables, int index) {
       return variables[0] *  Math.log(variables[1] * x.get(index) + 1);
    }
    
    @Override
    protected double getJacobianWithRespectToFirstParamFunction(double[] variables, int index) {
        return Math.log(variables[1] * x.get(index) + 1);
    }

    @Override
    protected double getJacobianWithRespectToSecondParamFunction(double[] variables, int index) {
        return variables[0] * x.get(index) / (variables[1] * x.get(index) + 1);
    }
}