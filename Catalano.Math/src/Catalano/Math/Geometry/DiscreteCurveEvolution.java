/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Math.Geometry;

import Catalano.Core.IntPoint;
import Catalano.Math.ComplexNumber;
import Catalano.Math.Matrix;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Diego
 */
public class DiscreteCurveEvolution implements IShapeOptimizer{
    
    private int vertices;

    public DiscreteCurveEvolution() {
        this(20);
    }

    public DiscreteCurveEvolution(int vertices) {
        this.vertices = vertices;
    }

    @Override
    public List<IntPoint> OptimizeShape(List<IntPoint> shape) {
        if (vertices > shape.size())
            throw new IllegalArgumentException("Number of points left must be higher than number of the shape.");

        ArrayList<ComplexNumber> complex = new ArrayList<ComplexNumber>();
        for (int i = 0; i < shape.size(); i++)
            complex.add(new ComplexNumber(shape.get(i).x, shape.get(i).y));

        for (int i = 0; i < shape.size() - vertices; i++){
            double[] winkelmaass = winkel(complex);

            int index = Matrix.MinIndex(winkelmaass);
            complex.remove(index);
        }

        ArrayList<IntPoint> newShape = new ArrayList<IntPoint>(complex.size());

        for (int i = 0; i < complex.size(); i++)
            newShape.add(new IntPoint((int)complex.get(i).real, (int)complex.get(i).imaginary));

        return newShape;
    }
    
    private double[] winkel(ArrayList<ComplexNumber> z){
        int n = z.size();
        double max = -Double.MAX_VALUE;

        double[] his = new double[n];
        for (int j = 1; j < n - 1; j++){
            ComplexNumber c = ComplexNumber.Subtract(z.get(j - 1), z.get(j + 1));

            double lm = c.getMagnitude();

            c = ComplexNumber.Subtract(z.get(j), z.get(j + 1));
            double lr = c.getMagnitude();

            c = ComplexNumber.Subtract(z.get(j - 1), z.get(j));
            double ll = c.getMagnitude();

            double alpha = Math.acos((lr * lr + ll * ll - lm * lm) / (2 * lr * ll));

            // turning angle (0-180)
            double a = 180 - alpha * 180 / Math.PI;

            // the original relevance measure
            his[j] = a * lr * ll / (lr + ll);

            if (his[j] > max) 
                max = his[j];

        }

        his[0] = max;
        his[n - 1] = max;

        return his;
    }
    
}
