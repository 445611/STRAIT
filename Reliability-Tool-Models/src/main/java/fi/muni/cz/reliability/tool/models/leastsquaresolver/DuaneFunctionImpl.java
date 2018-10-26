package fi.muni.cz.reliability.tool.models.leastsquaresolver;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class DuaneFunctionImpl extends FunctionAbstract {
    
    /**
     * Initialize variables.
     */
    public DuaneFunctionImpl() {
        super();
    }
    
    @Override
    protected double getVectorFunction(double[] variables, int index) {
       return variables[0] *  Math.pow(index, variables[1]);
    }
    
    @Override
    protected double getJacobianWithRespectToFirstParamFunction(double[] variables, int index) {
        return Math.pow(x.get(index), variables[1]);
    }

    @Override
    protected double getJacobianWithRespectToSecondParamFunction(double[] variables, int index) {
        return variables[0] *  Math.pow(x.get(index), variables[1]) * Math.log(x.get(index));
    }
}