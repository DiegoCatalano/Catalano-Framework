/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.Statistics.Analysis;

import Catalano.Math.Decompositions.SingularValueDecomposition;
import Catalano.Math.Matrix;
import Catalano.Statistics.Tools;

/**
 *
 * @author Diego Catalano
 */
public class PrincipalComponentAnalysis {
    
    public static enum AnalysisMethod{
        Standartize,
        Center
    };
    
    private double[][] matrix;
    private double[] meanColumn;
    private double[] stdColumn;
    private double[] singularValues;
    private double[][] eigenVectors;
    private double[] eigenValues;
    private double[][] result;

    public double[][] getResult() {
        return result;
    }

    public double[][] getEigenVectors() {
        return eigenVectors;
    }

    private AnalysisMethod method;

    public PrincipalComponentAnalysis(double[][] matrix) {
        this(matrix, AnalysisMethod.Center);
    }
    
    public PrincipalComponentAnalysis(double[][] matrix, AnalysisMethod method){
        this.matrix = matrix;
        this.method = method;
    }
    
    public void Compute(){
        
        meanColumn = new double[matrix[0].length];
        stdColumn = new double[matrix[0].length];
        
        //Get the mean and the standard deviation for each column
        int cols = matrix[0].length;
        for (int i = 0; i < cols; i++) {
            double[] col = Matrix.getColumn(matrix, i);
            meanColumn[i] = Tools.Mean(col);
            stdColumn[i] = Tools.StandartDeviation(col, meanColumn[i]);
        }
        
        //Center the data
        double[][] m = Center(matrix, meanColumn);
        
        if(method == AnalysisMethod.Standartize){
            for (int i = 0; i < m.length; i++) {
                for (int j = 0; j < m[0].length; j++) {
                    m[i][j] /= stdColumn[j];
                }
            }
        }
        
        //Find eigen vectors and singular values
        SingularValueDecomposition svd = new SingularValueDecomposition(m);
        singularValues = svd.getSingularValues();
        eigenVectors = svd.getV();
        
        //Compute the result
        result = Matrix.MultiplyByDiagonal(svd.getU(), singularValues);
        
        // Eigenvalues are the square of the singular values
        eigenValues = new double[singularValues.length];
        for (int i = 0; i < singularValues.length; i++)
            eigenValues[i] = singularValues[i] * singularValues[i] / (matrix.length - 1);
        
        CreateComponents();
        
    }
    
    private void CreateComponents(){
        
    }
    
    private double[][] Center(double[][] matrix, double[] means){
        double[][] m = new double[matrix.length][matrix[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                m[i][j] = matrix[i][j] - means[j];
            }
        }
        return m;
    }
    
}
