// Catalano Machine Learning Library
// The Catalano Framework
//
// Copyright 2015 Diego Catalano
// diego.catalano at live.com
//
// Copyright 2015 Haifeng Li
// haifeng.hli at gmail.com
//
// Based on Smile (Statistical Machine Intelligence & Learning Engine)
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package Catalano.MachineLearning.Classification.DecisionTrees.Learning;

import Catalano.Core.ArraysUtil;
import Catalano.Core.Concurrent.MulticoreExecutor;
import Catalano.MachineLearning.Classification.DecisionTrees.DecisionTree;
import Catalano.MachineLearning.Classification.DecisionTrees.DecisionTree.SplitRule;
import Catalano.MachineLearning.Dataset.DecisionVariable;
import Catalano.MachineLearning.Classification.IClassifier;
import Catalano.MachineLearning.Dataset.DatasetClassification;
import Catalano.Math.Matrix;
import Catalano.Math.Tools;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 * Random forest for classification. Random forest is an ensemble classifier
 * that consists of many decision trees and outputs the majority vote of
 * individual trees. The method combines bagging idea and the random
 * selection of features.
 * <p>
 * Each tree is constructed using the following algorithm:
 * <ol>
 * <li> If the number of cases in the training set is N, randomly sample N cases
 * with replacement from the original data. This sample will
 * be the training set for growing the tree. 
 * <li> If there are M input variables, a number m << M is specified such
 * that at each node, m variables are selected at random out of the M and
 * the best split on these m is used to split the node. The value of m is
 * held constant during the forest growing. 
 * <li> Each tree is grown to the largest extent possible. There is no pruning. 
 * </ol>
 * The advantages of random forest are:
 * <ul>
 * <li> For many data sets, it produces a highly accurate classifier.
 * <li> It runs efficiently on large data sets.
 * <li> It can handle thousands of input variables without variable deletion.
 * <li> It gives estimates of what variables are important in the classification.
 * <li> It generates an internal unbiased estimate of the generalization error
 * as the forest building progresses.
 * <li> It has an effective method for estimating missing data and maintains
 * accuracy when a large proportion of the data are missing.
 * </ul>
 * The disadvantages are
 * <ul>
 * <li> Random forests are prone to over-fitting for some datasets. This is
 * even more pronounced on noisy data.
 * <li> For data including categorical variables with different number of
 * levels, random forests are biased in favor of those attributes with more
 * levels. Therefore, the variable importance scores from random forest are
 * not reliable for this type of data.
 * </ul>
 * 
 * @author Haifeng Li
 */
public class RandomForest implements IClassifier, Serializable {
    
    /**
     * Method for choose number of random feature.
     */
    public enum RandomSelection {
        /**
         * Sqrt(number of features).
         */
        Sqrt,
        
        /**
         * Leo Breiman equation.
         */
        Log
    }
    
    private DecisionVariable[] attributes;
    private int T;
    private int M;
    private RandomSelection rs;
    //private long seed = 1;
    private SplitRule rule;
    
    /**
     * Forest of decision trees.
     */
    private List<DecisionTree> trees;
    
    /**
     * The number of classes.
     */
    private int k = 2;
    
    /**
     * Out-of-bag estimation of error rate, which is quite accurate given that
     * enough trees have been grown (otherwise the OOB estimate can
     * bias upward).
     */
    private double error;
    
    /**
     * Variable importance. Every time a split of a node is made on variable
     * the (GINI, information gain, etc.) impurity criterion for the two
     * descendent nodes is less than the parent node. Adding up the decreases
     * for each individual variable over all trees in the forest gives a fast
     * variable importance that is often very consistent with the permutation
     * importance measure.
     */
    private double[] importance;
    
    /**
     * Returns the out-of-bag estimation of error rate. The OOB estimate is
     * quite accurate given that enough trees have been grown. Otherwise the
     * OOB estimate can bias upward.
     * 
     * @return the out-of-bag estimation of error rate
     */
    public double error() {
        return error;
    }
    
    /**
     * Returns the variable importance. Every time a split of a node is made
     * on variable the (GINI, information gain, etc.) impurity criterion for
     * the two descendent nodes is less than the parent node. Adding up the
     * decreases for each individual variable over all trees in the forest
     * gives a fast measure of variable importance that is often very
     * consistent with the permutation importance measure.
     *
     * @return the variable importance
     */
    public double[] importance() {
        return importance;
    }
    
    /**
     * Returns the number of trees in the model.
     * 
     * @return the number of trees in the model 
     */
    public int size() {
        return trees.size();
    }

    /**
     * Get split rule.
     * @return Split rule.
     */
    public SplitRule getRule() {
        return rule;
    }

    /**
     * Set split rule.
     * @param rule Split rule.
     */
    public void setRule(SplitRule rule) {
        this.rule = rule;
    }

//    /**
//     * Random seed.
//     * @return Random seed.
//     */
//    public long getSeed() {
//        if(seed == 0)
//            return Thread.currentThread().getId() * System.currentTimeMillis();
//        return seed;
//    }
//
//    /**
//     * Set Random seed.
//     * @param seed Seed.
//     */
//    public void setSeed(long seed) {
//        this.seed = seed;
//    }
    
    /**
     * Trains a regression tree.
     */
    static class TrainingTask implements Callable<DecisionTree> {
        /**
         * Attribute properties.
         */
        DecisionVariable[] attributes;
        /**
         * Training instances.
         */
        double[][] x;
        /**
         * Training sample labels.
         */
        int[] y;
        /**
         * The index of training values in ascending order. Note that only
         * numeric attributes will be sorted.
         */
        int[][] order;
        /**
         * The number of variables to pick up in each node.
         */
        int M;
        /**
         * The out-of-bag predictions.
         */
        int[][] prediction;
        /**
         * Split rule decision tree.
         */
        SplitRule rule;

        /**
         * Constructor.
         */
        TrainingTask(DecisionVariable[] attributes, double[][] x, int[] y, int M, int[][] order, int[][] prediction, SplitRule rule) {
            this.attributes = attributes;
            this.x = x;
            this.y = y;
            this.order = order;
            this.M = M;
            this.prediction = prediction;
            this.rule = rule;
        }

        @Override
        public DecisionTree call() {            
            int n = x.length;
            Random random = new Random(Thread.currentThread().getId() * System.currentTimeMillis());
            int[] samples = new int[n]; // Training samples draw with replacement.
            for (int i = 0; i < n; i++) {
                samples[random.nextInt(n)]++;
            }
            
            DecisionTree tree = new DecisionTree(attributes, x, y, M, samples, order, rule);
            
            for (int i = 0; i < n; i++) {
                if (samples[i] == 0) {
                    int p = tree.Predict(x[i]);
                    synchronized (prediction[i]) {
                        prediction[i][p]++;
                    }
                }
            }
            
            return tree;
        }
    }
    
    /**
     * Initializes a new instance of the RandomForest class.
     */
    public RandomForest(){
        this(100);
    }

    /**
     * Initializes a new instance of the RandomForest class.
     * 
     * @param T the number of trees.
     */
    public RandomForest(int T) {
        this(T,0);
    }

    /**
     * Initializes a new instance of the RandomForest class.
     *
     * @param T the number of trees.
     * @param M the number of random selected features to be used to determine
     * the decision at a node of the tree. floor(sqrt(dim)) seems to give
     * generally good performance, where dim is the number of variables.
     */
    public RandomForest(int T, int M) {
        this(null, T, M);
    }
    
    /**
     * Initializes a new instance of the RandomForest class.
     * @param T the number of trees.
     * @param M the number of random selected features to be used to determine
     * the decision at a node of the tree. floor(sqrt(dim)) seems to give
     * generally good performance, where dim is the number of variables.
     * @param rule Split rule decision tree.
     */
    public RandomForest(int T, int M, SplitRule rule){
        this(null, T, M, rule);
    }
    
    /**
     * Initializes a new instance of the RandomForest class. 
     * @param T the number of trees.
     * @param randomSelection the number of random selected features.
     */
    public RandomForest(int T, RandomSelection randomSelection){
        this(null, T, randomSelection);
    }
    
    /**
     * Initializes a new instance of the RandomForest class. 
     * @param T the number of trees.
     * @param randomSelection the number of random selected features.
     */
    public RandomForest(int T, RandomSelection randomSelection, SplitRule rule){
        this(null, T, randomSelection, rule);
    }
    
    /**
     * Initializes a new instance of the RandomForest class.
     * @param attributes the attribute properties.
     */
    public RandomForest(DecisionVariable[] attributes){
        this(attributes, 100);
    }

    /**
     * Initializes a new instance of the RandomForest class.
     *
     * @param attributes the attribute properties.
     * @param T the number of trees.
     */
    public RandomForest(DecisionVariable[] attributes, int T) {
        this(attributes, T, (int) Tools.Log2(attributes.length) + 1);
    }
    
    /**
     * Initializes a new instance of the RandomForest class.
     *
     * @param attributes the attribute properties.
     * @param T the number of trees.
     * @param M the number of random selected features to be used to determine
     * the decision at a node of the tree. floor(sqrt(dim)) seems to give
     * generally good performance, where dim is the number of variables.
     */
    public RandomForest(DecisionVariable[] attributes, int T, int M){
        this(attributes, T, M, SplitRule.GINI);
    }
    
    /**
     * Initializes a new instance of the RandomForest class.
     * @param attributes the attribute properties.
     * @param T the number of trees.
     * @param M  the number of random selected features.
     * @param rule Split rule decision tree.
     */
    public RandomForest(DecisionVariable[] attributes, int T, int M, SplitRule rule){
        this.attributes = attributes;
        this.T = T;
        this.M = M;
        //this.seed = seed;
        this.rule = rule;
    }
    
    /**
     * Initializes a new instance of the RandomForest class.
     *
     * @param attributes the attribute properties.
     * @param T the number of trees.
     * @param randomSelection The method for create random selected features to be used to determine
     * the decision at a node of the tree. floor(sqrt(dim)) seems to give
     * generally good performance, where dim is the number of variables.
     */
    public RandomForest(DecisionVariable[] attributes, int T, RandomSelection randomSelection){
        this(attributes, T, randomSelection, SplitRule.GINI);
    }
    
    /**
     * Initializes a new instance of the RandomForest class.
     *
     * @param attributes the attribute properties.
     * @param T the number of trees.
     * @param randomSelection The method for create random selected features to be used to determine
     * the decision at a node of the tree. floor(sqrt(dim)) seems to give
     * generally good performance, where dim is the number of variables.
     * @param rule SplitRule decision tree.
     */
    public RandomForest(DecisionVariable[] attributes, int T, RandomSelection randomSelection, SplitRule rule){
        this.attributes = attributes;
        this.T = T;
        this.rs = randomSelection;
        this.rule = rule;
    }
    
    /**
     * Compute the random forest.
     *
     * @param attributes the attribute properties.
     * @param x the training instances. 
     * @param y the response variable.
     * @param T the number of trees.
     * @param M the number of random selected features to be used to determine
     * the decision at a node of the tree. floor(sqrt(dim)) seems to give
     * generally good performance, where dim is the number of variables.
     */
    private void BuildModel(DecisionVariable[] attributes, double[][] x, int[] y, int T, int M, SplitRule rule) {
        
        if (attributes == null) {
            int s = x[0].length;
            attributes = new DecisionVariable[s];
            for (int i = 0; i < s; i++) {
                attributes[i] = new DecisionVariable("F" + i);
            }
        }
        
        if (x.length != y.length) {
            throw new IllegalArgumentException(String.format("The sizes of X and Y don't match: %d != %d", x.length, y.length));
        }

        if (T < 1) {
            throw new IllegalArgumentException("Invlaid number of trees: " + T);
        }
        
        if (M < 1) {
            throw new IllegalArgumentException("Invalid number of variables for splitting: " + M);
        }
        
        // class label set.
        int[] labels = Tools.Unique(y);
        Arrays.sort(labels);
        
        for (int i = 0; i < labels.length; i++) {
            if (labels[i] < 0) {
                throw new IllegalArgumentException("Negative class label: " + labels[i]); 
            }
            
            if (i > 0 && labels[i] - labels[i-1] > 1) {
                throw new IllegalArgumentException("Missing class: " + labels[i]+1);                 
            }
        }

        k = labels.length;
        if (k < 2) {
            throw new IllegalArgumentException("Only one class.");            
        }

        int n = x.length;
        int[][] prediction = new int[n][k]; // out-of-bag prediction
        int[][] order = sort(attributes, x);
        List<TrainingTask> tasks = new ArrayList<TrainingTask>();
        for (int i = 0; i < T; i++) {
            tasks.add(new TrainingTask(attributes, x, y, M, order, prediction, rule));
        }
        
        try {
            trees = MulticoreExecutor.run(tasks);
        } catch (Exception ex) {
            System.err.println(ex);

            trees = new ArrayList<DecisionTree>(T);
            for (int i = 0; i < T; i++) {
                trees.add(tasks.get(i).call());
            }
        }
        
        int m = 0;
        for (int i = 0; i < n; i++) {
            int pred = Matrix.MaxIndex(prediction[i]);
            if (prediction[i][pred] > 0) {
                m++;
                if (pred != y[i]) {
                    error++;
                }
            }
        }

        if (m > 0) {
            error /= m;
        }
        
        importance = new double[attributes.length];
        for (DecisionTree tree : trees) {
            double[] imp = tree.getImportance();
            for (int i = 0; i < imp.length; i++) {
                importance[i] += imp[i];
            }
        }
    }
    
    /**
     * Sorts each variable and returns the index of values in ascending order.
     * Only numeric attributes will be sorted. Note that the order of original
     * array is NOT altered.
     * 
     * @param x a set of variables to be sorted. Each row is an instance. Each
     * column is a variable.
     * @return the index of values in ascending order
     */
    private int[][] sort(DecisionVariable[] attributes, double[][] x) {
        int n = x.length;
        int p = x[0].length;
        
        double[] a = new double[n];
        int[][] index = new int[p][];
        
        for (int j = 0; j < p; j++) {
            if (attributes[j].type == DecisionVariable.Type.Continuous) {
                for (int i = 0; i < n; i++) {
                    a[i] = x[i][j];
                }
                index[j] = ArraysUtil.Argsort(a, true);
            }
        }
        
        return index;        
    }
    
    /**
     * Trims the tree model set to a smaller size in case of over-fitting.
     * Or if extra decision trees in the model don't improve the performance,
     * we may remove them to reduce the model size and also improve the speed of
     * prediction.
     * 
     * @param T the new (smaller) size of tree model set.
     */
    public void trim(int T) {
        if (T > trees.size()) {
            throw new IllegalArgumentException("The new model size is larger than the current size.");
        }
        
        if (T <= 0) {
            throw new IllegalArgumentException("Invalid new model size: " + T);            
        }

        List<DecisionTree> model = new ArrayList<DecisionTree>(T);
        for (int i = 0; i < T; i++) {
            model.add(trees.get(i));
        }
        
        trees = model;
    }

    @Override
    public void Learn(DatasetClassification dataset) {
        Learn(dataset.getInput(), dataset.getOutput());
    }
    
    @Override
    public void Learn(double[][] input, int[] output){
        if(M == 0){
            if(rs == RandomSelection.Sqrt)
                this.M = (int)Math.floor(Math.sqrt(input[0].length));
            else
                this.M = (int)Tools.Log(input[0].length, 2) + 1;
        }
        
        BuildModel(attributes, input, output, T, M, rule);
    }
    
    @Override
    public int Predict(double[] feature) {
        
        int[] y = new int[k];

        for (DecisionTree tree : trees) {
            y[tree.Predict(feature)]++;
        }

        return Matrix.MaxIndex(y);
    }
    
    public int Predict(double[] feature, double[] posteriori) {
        if (posteriori.length != k) {
            throw new IllegalArgumentException(String.format("Invalid posteriori vector size: %d, expected: %d", posteriori.length, k));
        }

        int[] y = new int[k];
        
        for (DecisionTree tree : trees) {
            y[tree.Predict(feature)]++;
        }
        
        double n = trees.size();
        for (int i = 0; i < k; i++) {
            posteriori[i] = y[i] / n;
        }
        
        return Matrix.MaxIndex(y);
    }
    
    @Override
    public IClassifier clone() {
        try {
            return (IClassifier)super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new IllegalArgumentException("Clone not supported: " + ex.getMessage());
        }
    }
}