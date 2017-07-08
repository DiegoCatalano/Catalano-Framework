/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.MachineLearning.FeatureSelection;

import Catalano.Core.ArraysUtil;
import Catalano.MachineLearning.Classification.DecisionTrees.Learning.RandomForest;
import Catalano.MachineLearning.Dataset.DatasetClassification;
import java.util.Arrays;

/**
 *
 * @author Diego
 */
public class RandomForestFeatureSelection implements ISupervisionedFeatureSelection{
    
    private double[] rank;
    private int[] features;

    public RandomForestFeatureSelection() {}

    @Override
    public void Compute(DatasetClassification dataset) {
        Compute(dataset.getInput(), dataset.getOutput());
    }

    @Override
    public void Compute(double[][] input, int[] labels) {
        RandomForest rf = new RandomForest();
        rf.Learn(input,labels);
        
        this.rank = rf.importance();
        this.features = ArraysUtil.Argsort(rank, false);
        
    }

    @Override
    public int[] getFeatureIndex() {
        return features;
    }

    @Override
    public double[] getRank() {
        return rank;
    }
}