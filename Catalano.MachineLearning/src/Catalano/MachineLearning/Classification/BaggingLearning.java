/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.MachineLearning.Classification;

import Catalano.MachineLearning.DatasetClassification;
import Catalano.Math.Matrix;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Diego
 */
public class BaggingLearning implements IClassifier{
    
    private int times;
    private List<IClassifier> classifiers;
    private IClassifier classifier;

    public BaggingLearning(IClassifier classifier) {
        this(classifier, 10);
    }

    public BaggingLearning(IClassifier classifier, int times) {
        this.classifier = classifier;
        this.classifiers = new ArrayList<IClassifier>(times);
        this.times = times;
    }

    @Override
    public void Learn(DatasetClassification dataset) {
        Learn(dataset.getInput(), dataset.getOutput());
    }
    
    @Override
    public void Learn(double[][] input, int[] labels){
        
        for (int i = 0; i < times; i++) {
            
            //Create random trainning sample
            Random r = new Random();
            int[] index = new int[input.length];
            for (int j = 0; j < index.length; j++) {
                index[j] = r.nextInt(input.length);
            }
            
            double[][] train = Matrix.getRows(input, index);
            int[] labelsTrain = Matrix.getColumns(labels, index);
            
            classifier.Learn(train, labelsTrain);
            classifiers.add(classifier);
        }
    }
    
    public int Predict(double[] sample){
        int[] map = new int[classifiers.size()];
        for (int i = 0; i < map.length; i++) {
            map[i] = classifiers.get(i).Predict(sample);
        }
        
        //TODO in statistics
        return Catalano.Statistics.Tools.Mode(map);
    }
    
}
