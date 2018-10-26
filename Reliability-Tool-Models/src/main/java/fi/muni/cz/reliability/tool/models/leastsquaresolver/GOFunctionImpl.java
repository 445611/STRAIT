package fi.muni.cz.reliability.tool.models.leastsquaresolver;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class GOFunctionImpl extends FunctionAbstract {
    
    /**
     * Initialize variables.
     */
    public GOFunctionImpl() {
        super();
    }

    @Override
    protected double getVectorFunction(double[] variables, int index) {
       return variables[0] *  (1 - Math.exp( - variables[1] * x.get(index)));
    }
    
    @Override
    protected double getJacobianWithRespectToFirstParamFunction(double[] variables, int index) {
        return 1 - Math.exp(-variables[1]*x.get(index));
    }

    @Override
    protected double getJacobianWithRespectToSecondParamFunction(double[] variables, int index) {
        return variables[0] * x.get(index) * Math.exp(-variables[1]*x.get(index));
    }
}