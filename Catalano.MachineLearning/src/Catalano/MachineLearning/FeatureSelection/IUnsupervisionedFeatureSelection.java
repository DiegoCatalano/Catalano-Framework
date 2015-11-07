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
public interface IUnsupervisionedFeatureSelection {
    public void Compute(double[][] input);
    public double[] getRank();
    public int[] getFeatureIndex();
}