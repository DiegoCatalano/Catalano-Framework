/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.MachineLearning.FeatureSelection;

import Catalano.MachineLearning.Normalization;
import Catalano.Math.Decompositions.EigenvalueDecomposition;
import Catalano.Math.Matrix;
import Catalano.Math.Tools;

/**
 *
 * @author Diego Catalano
 */
public class PrincipalComponentFeatureSelection implements IUnsupervisionedFeatureSelection{
    
    private double p;
    private double[] rank;
    private int[] features;

    @Override
    public double[] getRank() {
        return rank;
    }
    
    @Override
    public int[] getFeatureIndex() {
        return features;
    }

    public PrincipalComponentFeatureSelection() {
        this.p = 0.95;
    }

    public PrincipalComponentFeatureSelection(double percentage) {
        this.p = percentage;
    }
    
    @Override
    public void Compute(double[][] input) {
        
        //Normalize data [0..1]
        Normalization norm = new Normalization();
        double[][] data = norm.Normalize(input);
        
        //Creta a correlation matrix.
        double[][] mat = Catalano.Statistics.Tools.Correlation(data);
        
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
