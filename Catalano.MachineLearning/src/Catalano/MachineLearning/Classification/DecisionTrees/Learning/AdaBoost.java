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
import Catalano.MachineLearning.Classification.DecisionTrees.DecisionTree;
import Catalano.MachineLearning.Dataset.DecisionVariable;
import Catalano.MachineLearning.Classification.IClassifier;
import Catalano.MachineLearning.Dataset.DatasetClassification;
import Catalano.Math.Matrix;
import Catalano.Math.Tools;
import java.io.Serializable;
import java.util.Arrays;

/**
 * AdaBoost (Adaptive Boosting) classifier with decision trees. In principle,
 * AdaBoost is a meta-algorithm, and can be used in conjunction with many other
 * learning algorithms to improve their performance. In practice, AdaBoost with
 * decision trees is probably the most popular combination. AdaBoost is adaptive
 * in the sense that subsequent classifiers built are tweaked in favor of those
 * instances misclassified by previous classifiers. AdaBoost is sensitive to
 * noisy data and outliers. However in some problems it can be less susceptible
 * to the over-fitting problem than most learning algorithms.
 * <p>
 * AdaBoost calls a weak classifier repeatedly in a series of rounds from
 * total T classifiers. For each call a distribution of weights is updated
 * that indicates the importance of examples in the data set for the
 * classification. On each round, the weights of each incorrectly classified
 * example are increased (or alternatively, the weights of each correctly
 * classified example are decreased), so that the new classifier focuses more
 * on those examples.
 * <p>
 * The basic AdaBoost algorithm is only for binary classification problem.
 * For multi-class classification, a common approach is reducing the
 * multi-class classification problem to multiple two-class problems.
 * This implementation is a multi-class AdaBoost without such reductions.
 * 
 * <h2>References</h2>
 * <ol>
 * <li> Yoav Freund, Robert E. Schapire. A Decision-Theoretic Generalization of on-Line Learning and an Application to Boosting, 1995.</li>
 * <li> Ji Zhu, Hui Zhou, Saharon Rosset and Trevor Hastie. Multi-class Adaboost, 2009.</li>
 * </ol>
 * 
 * @author Haifeng Li
 */
public class AdaBoost implements IClassifier, Serializable {
    
    private DecisionVariable[] attributes;
    private int T;
    private int J;
    
    /**
     * The number of classes.
     */
    private int k;
    
    /**
     * Forest of decision trees.
     */
    private DecisionTree[] trees;
    
    /**
     * The weight of each decision tree.
     */
    private double[] alpha;
    
    /**
     * The weighted error of each decision tree during training.
     */
    private double[] error;
    
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
     * Returns the variable importance. Every time a split of a node is made
     * on variable the (GINI, information gain, etc.) impurity criterion for
     * the two descendent nodes is less than the parent node. Adding up the
     * decreases for each individual variable over all trees in the forest
     * gives a simple measure of variable importance.
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
        return trees.length;
    }
    
    /**
     * Get number of trees.
     * @return Number of trees.
     */
    public int getNumberOfTrees(){
        return T;
    }
    
    /**
     * Set number of the trees.
     * @param T Number of trees.
     */
    public void setNumberOfTrees(int T){
        this.T = T;
    }
    
    /**
     * Get the number of maximum leafs.
     * @return Number of maximum leafs.
     */
    public int getNumberOfLeafs(){
        return J;
    }
    
    /**
     * Set the number of maximum leafs.
     * @param J Number of maximum leafs.
     */
    public void setNumberOfLeafs(int J){
        this.J = J;
    }
    
    public AdaBoost(){
        this(10);
    }
    
    /**
     * Initializes a new instance of the AdaBoost class.
     *
     * @param T the number of trees.
     */
    public AdaBoost(int T) {
        this(T, 2);
    }

    /**
     * Initializes a new instance of the AdaBoost class.
     *
     * @param T the number of trees.
     * @param J the maximum number of leaf nodes in the trees.
     */
    public AdaBoost(int T, int J) {
        this.T = T;
        this.J = J;
    }
    
    /**
     * Initializes a new instance of the AdaBoost class.
     * 
     * @param attributes the attribute properties.
     */
    public AdaBoost(DecisionVariable[] attributes){
        this(null, 10);
    }

    /**
     * Initializes a new instance of the AdaBoost class.
     *
     * @param attributes the attribute properties.
     * @param T the number of trees.
     */
    public AdaBoost(DecisionVariable[] attributes, int T) {
        this(attributes, T, 2);
    }
    
    /**
     * Initializes a new instance of the AdaBoost class.
     *
     * @param attributes the attribute properties.
     * @param T the number of trees.
     * @param J the maximum number of leaf nodes in the trees.
     */
    public AdaBoost(DecisionVariable[] attributes, int T, int J) {
        this.attributes = attributes;
        this.T = T;
        this.J = J;
    }
    
    private void BuildModel(DecisionVariable[] attributes, double[][] x, int[] y, int T, int J){
        this.T = T;
        this.J = J;
        
        if (x.length != y.length) {
            throw new IllegalArgumentException(String.format("The sizes of X and Y don't match: %d != %d", x.length, y.length));
        }

        if (T < 1) {
            throw new IllegalArgumentException("Invalid number of trees: " + T);
        }
        
        if (J < 2) {
            throw new IllegalArgumentException("Invalid maximum leaves: " + J);
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
        
        if (attributes == null) {
            int s = x[0].length;
            attributes = new DecisionVariable[s];
            for (int i = 0; i < s; i++) {
                attributes[i] = new DecisionVariable("F" + i);
            }
        }

        int[][] order = sort(attributes, x);
        
        int n = x.length;
        int[] samples = new int[n];
        double[] w = new double[n];
        boolean[] err = new boolean[n];
        for (int i = 0; i < n; i++) {
            w[i] = 1.0;
        }
        
        double guess = 1.0 / k; // accuracy of random guess.
        double b = Math.log(k - 1); // the baise to tree weight in case of multi-class.
        
        trees = new DecisionTree[T];
        alpha = new double[T];
        error = new double[T];
        for (int t = 0; t < T; t++) {
            double W = Tools.Sum(w);
            for (int i = 0; i < n; i++) {
                w[i] /= W;
            }
            
            Arrays.fill(samples, 0);  
            int[] rand = random(w, n);
            for (int s : rand) {
                samples[s]++;
            }
            
            trees[t] = new DecisionTree(attributes, J, samples, order, DecisionTree.SplitRule.GINI);
            trees[t].Learn(x, y);
            
            for (int i = 0; i < n; i++) {
                err[i] = trees[t].Predict(x[i]) != y[i];
            }
            
            double e = 0.0; // weighted error
            for (int i = 0; i < n; i++) {
                if (err[i]) {
                    e += w[i];
                }
            }
            
            if (1 - e <= guess) {
                trees = Arrays.copyOf(trees, t);
                alpha = Arrays.copyOf(alpha, t);
                error = Arrays.copyOf(error, t);
                break;
            }
            
            error[t] = e;
            alpha[t] = Math.log((1-e)/Math.max(1E-10,e)) + b;
            double a = Math.exp(alpha[t]);
            for (int i = 0; i < n; i++) {
                if (err[i]) {
                    w[i] *= a;
                }
            }
        }
        
        importance = new double[attributes.length];
        for (DecisionTree tree : trees) {
            double[] imp = tree.getImportance();
            for (int i = 0; i < imp.length; i++) {
                importance[i] += imp[i];
            }
        }
    }

    @Override
    public void Learn(DatasetClassification dataset) {
        Learn(dataset.getInput(), dataset.getOutput());
    }
    
    @Override
    public void Learn(double[][] input, int[] output){
        BuildModel(attributes, input, output, T, J);
    }
    
    @Override
    public int Predict(double[] feature) {   
        if (k == 2) {
            double y = 0.0;
            for (int i = 0; i < trees.length; i++) {
                y += alpha[i] * trees[i].Predict(feature);
            }

            return y > 0 ? 1 : 0;
        } else {
            double[] y = new double[k];
            for (int i = 0; i < trees.length; i++) {
                y[trees[i].Predict(feature)] += alpha[i];
            }
            
            return Matrix.MaxIndex(y);
        }
    }
    
    /**
     * Given a set of m probabilities, draw with replacement a set of n random
     * number in [0, m).
     * @param prob probabilities of size n. The prob argument can be used to
     * give a vector of weights for obtaining the elements of the vector being
     * sampled. They need not sum to one, but they should be nonnegative and
     * not all zero.
     * @return an random array of length n in range of [0, m).
     */
    private int[] random(double[] prob, int n) {
        // set up alias table
        double[] q = new double[prob.length];
        for (int i = 0; i < prob.length; i++) {
            q[i] = prob[i] * prob.length;
        }

        // initialize a with indices
        int[] a = new int[prob.length];
        for (int i = 0; i < prob.length; i++) {
            a[i] = i;
        }

        // set up H and L
        int[] HL = new int[prob.length];
        int head = 0;
        int tail = prob.length - 1;
        for (int i = 0; i < prob.length; i++) {
            if (q[i] >= 1.0) {
                HL[head++] = i;
            } else {
                HL[tail--] = i;
            }
        }

        while (head != 0 && tail != prob.length - 1) {
            int j = HL[tail + 1];
            int k = HL[head - 1];
            a[j] = k;
            q[k] += q[j] - 1;
            tail++;                                  // remove j from L
            if (q[k] < 1.0) {
                HL[tail--] = k;                      // add k to L
                head--;                              // remove k
            }
        }

        // generate sample
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            double rU = Tools.RandomNextDouble() * prob.length;

            int k = (int) (rU);
            rU -= k;  /* rU becomes rU-[rU] */

            if (rU < q[k]) {
                ans[i] = k;
            } else {
                ans[i] = a[k];
            }
        }

        return ans;
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
        if (T > trees.length) {
            throw new IllegalArgumentException("The new model size is larger than the current size.");
        }
        
        if (T <= 0) {
            throw new IllegalArgumentException("Invalid new model size: " + T);            
        }
        
        if (T < trees.length) {
            trees = Arrays.copyOf(trees, T);
            alpha = Arrays.copyOf(alpha, T);
            error = Arrays.copyOf(error, T);
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
    
    @Override
    public IClassifier clone() {
        try {
            return (IClassifier)super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new IllegalArgumentException("Clone not supported: " + ex.getMessage());
        }
    }
}

