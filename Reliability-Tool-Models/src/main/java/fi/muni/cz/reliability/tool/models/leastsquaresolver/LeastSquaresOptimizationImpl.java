package fi.muni.cz.reliability.tool.models.leastsquaresolver;

import fi.muni.cz.reliability.tool.models.exception.ModelException;
import fi.muni.cz.reliability.tool.utils.Tuple;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresBuilder;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

/**
 * @author Radoslav Micko, 445611@muni.cz
 */
public class LeastSquaresOptimizationImpl implements LeastSquaresOptimization{
    
    private static final int MAX_NUMBER_OF_EVALUATIONS = 10000;
    private static final int MAX_NUMBER_OF_ITERATIONS = 10000;
    
    @Override
    public double[] optimizer(double[] startParameters, List<Tuple<Integer, Integer>> listOfData, Function function) {
        
        for (Tuple<Integer, Integer> tuple: listOfData) {
            function.addPoint(tuple.getA(), tuple.getB());
        }
        
        LeastSquaresBuilder leastSquaresBuilder = getLeastSquaresBuilder(startParameters, function);
        LevenbergMarquardtOptimizer levenbergMarquardtOptimizer = new LevenbergMarquardtOptimizer();
        
        try {
            LeastSquaresOptimizer.Optimum lsoo = levenbergMarquardtOptimizer.optimize(leastSquaresBuilder.build());
            
            
            //-----------------------------------------------------------------
            final double[] optimalValues = lsoo.getPoint().toArray();
            System.out.println("A: " + optimalValues[0]);
            System.out.println("B: " + optimalValues[1]);
            System.out.println("Iteration number: "+lsoo.getIterations());
            System.out.println("Evaluation number: "+lsoo.getEvaluations());
            //-----------------------------------------------------------------
            
            
            return optimalValues;
        } catch (Exception ex)  {
            Logger.getLogger(LeastSquaresOptimizationImpl.class.getName())
                    .log(Level.SEVERE, "Error while optimizing parameters of function.", ex);
            throw new ModelException("Error while optimizing parameters of function.", ex);
        }
    }
    
    private LeastSquaresBuilder getLeastSquaresBuilder(double[] startParameters, Function function) {
        LeastSquaresBuilder leastSquaresBuilder = new LeastSquaresBuilder();
        leastSquaresBuilder.model(function.getMultivariateVectorFunction(), 
                function.getMultivariateMatrixFunction());
        
        leastSquaresBuilder.target(function.calculateTarget());
        leastSquaresBuilder.start(startParameters);
        leastSquaresBuilder.maxEvaluations(MAX_NUMBER_OF_EVALUATIONS);
        leastSquaresBuilder.maxIterations(MAX_NUMBER_OF_ITERATIONS);
        return leastSquaresBuilder;
    }
}
