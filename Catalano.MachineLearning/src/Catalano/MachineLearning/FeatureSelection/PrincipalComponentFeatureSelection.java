// Catalano Machine Learning Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
//
//    This library is free software; you can redistribute it and/or
//    modify it under the terms of the GNU Lesser General Public
//    License as published by the Free Software Foundation; either
//    version 2.1 of the License, or (at your option) any later version.
//
//    This library is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//    Lesser General Public License for more details.
//
//    You should have received a copy of the GNU Lesser General Public
//    License along with this library; if not, write to the Free Software
//    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
//

package Catalano.MachineLearning.FeatureSelection;

import Catalano.Math.Decompositions.EigenvalueDecomposition;
import Catalano.Math.Matrix;
import Catalano.Math.Tools;

/**
 * Principal components Feature Selection.
 * @author Diego Catalano
 */
public class PrincipalComponentFeatureSelection implements IUnsupervisionedFeatureSelection{
    
    private double p;
    private double[] rank;
    private int[] features;

    /**
     * Get the percentage.
     * @return Percentage.
     */
    public double getPercentage() {
        return p;
    }

    /**
     * Set the percentage.
     * @param p Percentage.
     */
    public void setPercentage(double p) {
        this.p = Math.max(0, Math.min(p, 1));
    }

    @Override
    public double[] getRank() {
        return rank;
    }
    
    @Override
    public int[] getFeatureIndex() {
        return features;
    }

    /**
     * Initializes a new instance of the PrincipalComponentFeatureSelection class.
     */
    public PrincipalComponentFeatureSelection() {
        this.p = 0.95;
    }

    /**
     * Initializes a new instance of the PrincipalComponentFeatureSelection class.
     * @param percentage Percentange
     */
    public PrincipalComponentFeatureSelection(double percentage) {
        setPercentage(percentage);
    }
    
    @Override
    public void Compute(double[][] input) {
        
        //Creta a correlation matrix.
        double[][] mat = Catalano.Statistics.Tools.Correlation(input);
        
        //Calculate eigen values (rank).
        EigenvalueDecomposition evd = new EigenvalueDecomposition(mat);
        double[] eigen = evd.getRealEigenvalues();
        
        //The sum of the all elements.
        double den = Tools.Sum(eigen);
        
        double sum = 0;
        int pos = 1;
        do {
            sum += eigen[eigen.length - pos];
            pos++;
        } while ((sum/den) <= p);
        
        double[] reverse = new double[eigen.length];
        for (int i = 0; i < reverse.length; i++) {
            reverse[i] = eigen[eigen.length - i - 1];
        }
        
        this.rank = reverse;
        this.features = Matrix.Indices(0, pos - 1);
        
    }
}
