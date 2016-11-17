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

package Catalano.MachineLearning.Regression.RegressionTrees.Learning;

import Catalano.Core.ArraysUtil;
import Catalano.Core.Concurrent.MulticoreExecutor;
import Catalano.MachineLearning.Dataset.DatasetRegression;
import Catalano.MachineLearning.Dataset.DecisionVariable;
import Catalano.MachineLearning.Regression.IRegression;
import Catalano.MachineLearning.Regression.RegressionTrees.RegressionTree;
import Catalano.Math.Random.Random;
import Catalano.Math.Tools;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Random forest for regression. Random forest is an ensemble method that
 * consists of many regression trees and outputs the average of individual
 * trees. The method combines bagging idea and the random selection of features.
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
 * <li> For many data sets, it produces a highly accurate model.
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
 * even more pronounced in noisy classification/regression tasks.
 * <li> For data including categorical variables with different number of
 * levels, random forests are biased in favor of those attributes with more
 * levels. Therefore, the variable importance scores from random forest are
 * not reliable for this type of data.
 * </ul>
 * 
 * @author Haifeng Li
 */
public class RandomForest implements IRegression, Serializable{
    
    private DecisionVariable[] attributes = null;
    private int T;
    private int M;
    private int S;
    
    
    /**
     * Forest of regression trees.
     */
    private List<RegressionTree> trees;
    /**
     * Out-of-bag estimation of RMSE, which is quite accurate given that
     * enough trees have been grown (otherwise the OOB estimate can
     * bias upward).
     */
    private double error;
    /**
     * Variable importance. Every time a split of a node is made on variable
     * the impurity criterion for the two descendent nodes is less than the
     * parent node. Adding up the decreases for each individual variable over
     * all trees in the forest gives a fast variable importance that is often
     * very consistent with the permutation importance measure.
     */
    private double[] importance;
    
    /**
     * Trains a regression tree.
     */
    static class TrainingTask implements Callable<RegressionTree> {
        /**
         * Attribute properties.
         */
        DecisionVariable[] attributes;
        /**
         * Training instances.
         */
        double[][] x;
        /**
         * Training response variable.
         */
        double[] y;
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
         * The minimum number of instances in leaf nodes.
         */
        int S;
        /**
         * Predictions of of out-of-bag samples.
         */
        double[] prediction;
        /**
         * Out-of-bag sample
         */
        int[] oob;

        /**
         * Constructor.
         */
        TrainingTask(DecisionVariable[] attributes, double[][] x, double[] y, int[][] order, int M, int S, double[] prediction, int[] oob) {
            this.attributes = attributes;
            this.x = x;
            this.y = y;
            this.order = order;
            this.M = M;
            this.S = S;
            this.prediction = prediction;
            this.oob = oob;
        }

        @Override
        public RegressionTree call() {
            int n = x.length;
            Random random = new Random(Thread.currentThread().getId() * System.currentTimeMillis());
            int[] samples = new int[n]; // Training samples draw with replacement.
            for (int i = 0; i < n; i++) {
                samples[random.nextInt(n)]++;
            }
            
            RegressionTree tree = new RegressionTree(attributes, x, y, M, S, order, samples);
            
            for (int i = 0; i < n; i++) {
                if (samples[i] == 0) {
                    double pred = tree.Predict(x[i]);
                    synchronized (x[i]) {
                        prediction[i] += pred;
                        oob[i]++;
                    }
                }
            }
            
            return tree;
        }
    }
    
    /**
     * Initialize a new instance of the RandomForest class.
     */
    public RandomForest(){
        this(100);
    }
    
    /**
     * Initialize a new instance of the RandomForest class.
     * @param T Number of the trees.
     */
    public RandomForest(int T){
        this(null, 100);
    }
    
    /**
     * Initialize a new instance of the RandomForest class.
     * @param T the number of trees.
     * @param M the number of input variables to be used to determine the decision
     * at a node of the tree. dim/3 seems to give generally good performance,
     * where dim is the number of variables.
     */
    public RandomForest(int T, int M){
        this(T,M,5);
    }

    /**
     * Initialize a new instance of the RandomForest class.
     *
     * @param T the number of trees.
     * @param M the number of input variables to be used to determine the decision
     * at a node of the tree. dim/3 seems to give generally good performance,
     * where dim is the number of variables.
     * @param S the number of instances in a node below which the tree will
     * not split, setting S = 5 generally gives good results.
     */
    public RandomForest(int T, int M, int S) {
        this(null, T, M, S);
    }
    
    /**
     * Initialize a new instance of the RandomForest class.
     * @param attributes Attributes.
     */
    public RandomForest(DecisionVariable[] attributes){
        this(attributes, 100, -1, 5);
    }

    /**
     * Initialize a new instance of the RandomForest class.
     *
     * @param attributes the attribute properties.
     * @param T Number of trees.
     */
    public RandomForest(DecisionVariable[] attributes, int T) {
        this(attributes, T, -1, 5);
    }
    
    /**
     * Initialize a new instance of the RandomForest class.
     * @param attributes the attribute properties.
     * @param T Number of trees.
     * @param M the number of input variables to be used to determine the decision
     * at a node of the tree. dim/3 seems to give generally good performance,
     * where dim is the number of variables.
     */
    public RandomForest(DecisionVariable[] attributes, int T, int M) {
        this(attributes, T, M, 5);
    }
    
    /**
     * Constructor. Learns a random forest for regression.
     *
     * @param attributes the attribute properties.
     * @param T the Number of trees.
     * @param M the number of input variables to be used to determine the decision
     * at a node of the tree. dim/3 seems to give generally good performance,
     * where dim is the number of variables.
     * @param S the number of instances in a node below which the tree will
     * not split, setting S = 5 generally gives good results.
     */
    public RandomForest(DecisionVariable[] attributes, int T, int M, int S) {
        this.attributes = attributes;
        this.T = T;
        this.M = M;
        this.S = S;
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
     * Returns the out-of-bag estimation of RMSE. The OOB estimate is
     * quite accurate given that enough trees have been grown. Otherwise the
     * OOB estimate can bias upward.
     * 
     * @return the out-of-bag estimation of RMSE
     */
    public double error() {
        return error;
    }
        
    /**
     * Returns the variable importance. Every time a split of a node is made
     * on variable the impurity criterion for the two descendent nodes is less
     * than the parent node. Adding up the decreases for each individual
     * variable over all trees in the forest gives a fast measure of variable
     * importance that is often very consistent with the permutation importance
     * measure.
     *
     * @return the variable importance
     */
    public double[] getImportance() {
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
        
        List<RegressionTree> model = new ArrayList<RegressionTree>(T);
        for (int i = 0; i < T; i++) {
            model.add(trees.get(i));
        }
        
        trees = model;
    }
    
    private void BuildModel(DecisionVariable[] attributes, double[][] x, double[] y, int T, int M, int S){
        if (x.length != y.length) {
            throw new IllegalArgumentException(String.format("The sizes of X and Y don't match: %d != %d", x.length, y.length));
        }

        if (attributes == null) {
            int p = x[0].length;
            attributes = new DecisionVariable[p];
            for (int i = 0; i < p; i++) {
                attributes[i] = new DecisionVariable("F" + i);//NumericAttribute("V" + (i + 1));
            }
        }

        if (M <= 0) {
            M = Math.max(1, x[0].length / 3);
        }
        
        if (S <= 0) {
            throw new IllegalArgumentException("Invalid minimum leaf node size: " + S);
        }
        
        int n = x.length;
        double[] prediction = new double[n];
        int[] oob = new int[n];
        
        int[][] order = sort(attributes, x);
        List<TrainingTask> tasks = new ArrayList<TrainingTask>();
        for (int i = 0; i < T; i++) {
            tasks.add(new TrainingTask(attributes, x, y, order, M, S, prediction, oob));
        }
        
        try {
            trees = MulticoreExecutor.run(tasks);
        } catch (Exception ex) {
            ex.printStackTrace();

            trees = new ArrayList<RegressionTree>(T);
            for (int i = 0; i < T; i++) {
                trees.add(tasks.get(i).call());
            }
        }
        
        int m = 0;
        for (int i = 0; i < n; i++) {
            if (oob[i] > 0) {
                m++;
                double pred = prediction[i] / oob[i];
                error += Tools.Square(pred - y[i]);
            }
        }

        if (m > 0) {
            error = Math.sqrt(error / m);
        }
        
        importance = new double[attributes.length];
        for (RegressionTree tree : trees) {
            double[] imp = tree.getImportance();
            for (int i = 0; i < imp.length; i++) {
                importance[i] += imp[i];
            }
        }
    }
    
    @Override
    public void Learn(DatasetRegression dataset) {
        Learn(dataset.getInput(), dataset.getOutput());
    }

    @Override
    public void Learn(double[][] input, double[] output) {
        BuildModel(attributes, input, output, T, M, S);
    }
    
    @Override
    public double Predict(double[] x) {
        double y = 0;
        for (RegressionTree tree : trees) {
            y += tree.Predict(x);
        }
        
        return y / trees.size();
    }
    
    @Override
    public IRegression clone() {
        try {
            return (IRegression)super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new IllegalArgumentException("Clone not supported: " + ex.getMessage());
        }
    }
}