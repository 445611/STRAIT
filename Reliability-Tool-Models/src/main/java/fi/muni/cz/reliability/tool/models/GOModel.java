package fi.muni.cz.reliability.tool.models;

import fi.muni.cz.reliability.tool.models.leastsquaresolver.GOFunction;
import fi.muni.cz.reliability.tool.utils.Tuple;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresBuilder;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;


/**
 * Goel-Okumoto (G-O) model
 * 
 * @author Radoslav Micko <445611@muni.cz>
 */
public class GOModel implements Model {

    @Override
    public double[] calculateFunctionParametersOfModel(List<Tuple<Integer, Integer>> list) {
        
        //double[] y = getYValues(list);
        //double[] x = getXValues(list);
        
        List<Double> x = new ArrayList<>();
        List<Double> y = new ArrayList<>();
        
        GOFunction function = new GOFunction(x, y);
        
        for (Tuple<Integer, Integer> tuple: list) {
            function.addPoint(tuple.getA(), tuple.getB());
        }
        
        LeastSquaresBuilder lsb = new LeastSquaresBuilder();
        lsb.model(function.retMVF(), function.retMMF());
        
        double[] newTarget = function.calculateTarget();
        
        //set target data
        lsb.target(newTarget);
        double[] newStart = {1,1};
        //set initial parameters
        lsb.start(newStart);
        //set upper limit of evaluation time
        lsb.maxEvaluations(100000);
        //set upper limit of iteration time
        lsb.maxIterations(100000);

        
        LevenbergMarquardtOptimizer lmo = new LevenbergMarquardtOptimizer();
        try {
            //do LevenbergMarquardt optimization
            LeastSquaresOptimizer.Optimum lsoo = lmo.optimize(lsb.build());
            //get optimized parameters
            final double[] optimalValues = lsoo.getPoint().toArray();
            //output data
            System.out.println("A: " + optimalValues[0]);
            System.out.println("B: " + optimalValues[1]);
            System.out.println("Iteration number: "+lsoo.getIterations());
            System.out.println("Evaluation number: "+lsoo.getEvaluations());
            return new double[]{optimalValues[0], optimalValues[1]};
        } catch (Exception e) {
            System.out.println(e.toString());
            //TODO

            throw new RuntimeException();
        }
        //double[][] x = {{0}, {1}, {2}, {3}, {4}, {5}};
        /*double[][] x = getXValues(list);
        //double[] y = {1.1, 0.9, 1.0, 1.35, 1.82, 2.5};
        double[] y = getYValues(list);
        
        Fitter fitter = new NonLinearSolver(getModelFunction());
        fitter.setData(x, y);
        fitter.setParameters(new double[]{1, 1});

        fitter.fitData();
        return fitter.getParameters();*/
    }
    
    /*private MultivariateJacobianFunction getMultivariateJacobianFunction() {
        MultivariateJacobianFunction distancesToCurrentCenter = new MultivariateJacobianFunction() {
        
            @Override
            public Pair<RealVector, RealMatrix> value(final RealVector point) {

            Vector2D center = new Vector2D(point.getEntry(0), point.getEntry(1));

            RealVector value = new ArrayRealVector(observedPoints.length);
            RealMatrix jacobian = new Array2DRowRealMatrix(observedPoints.length, 2);

            for (int i = 0; i < observedPoints.length; ++i) {
                Vector2D o = observedPoints[i];
                double modelI = Vector2D.distance(o, center);
                value.setEntry(i, modelI);
                // derivative with respect to p0 = x center
                jacobian.setEntry(i, 0, (center.getX() - o.getX()) / modelI);
                // derivative with respect to p1 = y center
                jacobian.setEntry(i, 1, (center.getX() - o.getX()) / modelI);
            }

            return new Pair<RealVector, RealMatrix>(value, jacobian);
            }
        };
    }*/
    
    
    
    /*private double[][] getXValues(List<Tuple<Integer, Integer>> list) {
        double[][] xValues = new double[list.size()][1];
        for (int i = 0; i < list.size(); i++) {
            xValues[i][0] = i + 1;
        }
        return xValues;
    }*/
    
    private double[] getXValues(List<Tuple<Integer, Integer>> list) {
        double[] xValues = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            xValues[i] = i + 1;
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
    
    /*private Function getModelFunction() {
        Function function = new Function(){
        
        @Override
        public double evaluate(double[] values, double[] parameters) {*/
            /*double A = parameters[0];
            double B = parameters[1];
            double x = values[0];
            return A*x*x + B*x;*/
            /*
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
    }*/
    
}
