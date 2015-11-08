/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.MachineLearning.FeatureSelection;

/**
 *
 * @author Diego Catalano
 */
public interface ISupervisionedFeatureSelection {
    public void Compute(double[][] input, int[] labels);
    public int[] getFeatureIndex();
    public double[] getRank();
}