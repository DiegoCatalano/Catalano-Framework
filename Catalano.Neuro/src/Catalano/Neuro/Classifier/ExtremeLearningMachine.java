// Catalano Machine Learning Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Qin-Yu Zhu, 2016
// EGBHUANG@NTU.EDU.SG
//
// Copyright © Guang-Bin Huang, 2016
// GBHUANG@IEEE.ORG
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

package Catalano.Neuro.Classifier;

import Catalano.MachineLearning.Classification.IClassifier;
import Catalano.MachineLearning.Dataset.DatasetClassification;
import Catalano.Math.Matrix;
import Catalano.Neuro.ActivationFunction.IActivationFunction;
import Catalano.Neuro.ActivationFunction.SigmoidFunction;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

/**
 * Extreme Learning Machine.
 * @author Diego Catalano
 */
public class ExtremeLearningMachine implements IClassifier, Serializable{
    
    private int nHiddenNodes;
    private IActivationFunction function;
    private double c = 1;
    private long seed = 0;
    
    private double[] bias;
    private double[][] inputWeight;
    private double[][] outputWeight;

    /**
     * Get the number of hidden nodes.
     * @return Number of hidden nodes.
     */
    public int getNumberOfHiddenNodes() {
        return nHiddenNodes;
    }

    /**
     * Set the number of hidden nodes.
     * @param nHiddenNodes Number of hidden nodes.
     */
    public void setNumberOfHiddenNodes(int nHiddenNodes) {
        this.nHiddenNodes = nHiddenNodes;
    }

    /**
     * Get regularization factor.
     * @return Regularization factor.
     */
    public double getRegulazationFactor() {
        return c;
    }
    
    /**
     * Set regularization factor.
     * @param c Regularization factor.
     */
    public void setRegularizationFactor(double c){
        this.c = Math.max(1, c);
    }

    /**
     * Get the bias of the hidden nodes.
     * @return Bias of the hidden nodes.
     */
    public double[] getBias() {
        return bias;
    }

    /**
     * Set the bias of the hidden nodes.
     * @param bias Bias of the hidden nodes.
     */
    public void setBias(double[] bias) {
        this.bias = bias;
    }

    /**
     * Get the input weight.
     * @return Input weight.
     */
    public double[][] getInputWeight() {
        return inputWeight;
    }

    /**
     * Set the input weight.
     * @param inputWeight Input weight.
     */
    public void setInputWeight(double[][] inputWeight) {
        this.inputWeight = inputWeight;
    }

    /**
     * Get the output weight.
     * @return Output weight.
     */
    public double[][] getOutputWeight() {
        return outputWeight;
    }

    /**
     * Set the output weight.
     * @param outputWeight Output weight.
     */
    public void setOutputWeight(double[][] outputWeight) {
        this.outputWeight = outputWeight;
    }

    /**
     * Get the activation function.
     * @return Activation function.
     */
    public IActivationFunction getFunction() {
        return function;
    }

    /**
     * Set the activation function.
     * @param function Activation function.
     */
    public void setFunction(IActivationFunction function) {
        this.function = function;
    }

    /**
     * Get random seed.
     * @return Seed.
     */
    public long getSeed() {
        return seed;
    }

    /**
     * Set random seed.
     * @param seed Seed.
     */
    public void setSeed(long seed) {
        this.seed = seed;
    }
    
    /**
     * Initializes a new instance of the ExtremeLearningMachine class.
     */
    public ExtremeLearningMachine(){
        this(20);
    }
    
    /**
     * Initializes a new instance of the ExtremeLearningMachine class.
     * @param nHiddenNodes Number of hidden nodes.
     */
    public ExtremeLearningMachine(int nHiddenNodes){
        this(nHiddenNodes, 100);
    }
    
    /**
     * Initializes a new instance of the ExtremeLearningMachine class.
     * @param nHiddenNodes Number of hidden nodes.
     * @param c Regularization factor.
     */
    public ExtremeLearningMachine(int nHiddenNodes, double c){
        this(nHiddenNodes, c, new SigmoidFunction());
    }

    /**
     * Initializes a new instance of the ExtremeLearningMachine class.
     * @param nHiddenNodes Number of hidden nodes.
     * @param c Regularization factor.
     * @param function Activation function.
     */
    public ExtremeLearningMachine(int nHiddenNodes, double c, IActivationFunction function) {
        this(nHiddenNodes, c, function, 0);
    }
    
    /**
     * Initializes a new instance of the ExtremeLearningMachine class.
     * @param nHiddenNodes Number of hidden nodes.
     * @param c Regularization factor.
     * @param function Activation function.
     * @param seed Random seed.
     */
    public ExtremeLearningMachine(int nHiddenNodes, double c, IActivationFunction function, long seed){
        this.nHiddenNodes = nHiddenNodes;
        this.c = c;
        this.function = function;
        this.seed = seed;
    }

    @Override
    public void Learn(DatasetClassification dataset) {
        Learn(dataset.getInput(), dataset.getOutput());
    }

    @Override
    public void Learn(double[][] input, int[] output) {
        
        Random r = new Random();
        if(seed != 0) r.setSeed(seed);
        
        //Transpose initial data
        double[][] _input = Matrix.Transpose(input);
        
        //Number of attributes
        int nOutputNeurons = Matrix.Max(output) + 1;
        
        //Processing the targets of training
        int[] uniques = Catalano.Math.Tools.Unique(output);
        Arrays.sort(output);
        double[][] t = new double[nOutputNeurons][input.length];
        for (int i = 0; i < t.length; i++) {
            int l = uniques[i];
            for (int j = 0; j < t[0].length; j++) {
                if(output[j] == l)
                    t[i][j] = 1;
                else
                    t[i][j] = -1;
            }
        }
        
        //Create InputWeight (w_i)
        if(inputWeight == null){
            inputWeight = new double[nHiddenNodes][_input.length];
            for (int i = 0; i < inputWeight.length; i++) {
                for (int j = 0; j < inputWeight[0].length; j++) {
                    inputWeight[i][j] = r.nextDouble()* 2 - 1;
                }
            }
        }
        
        //Create random biases BiasofHiddenNeurons
        if (bias == null){
            bias = new double[nHiddenNodes];
            for (int i = 0; i < bias.length; i++) {
                bias[i] = r.nextDouble();
            }
        }
        
        //Add bias
        double[][] h = Matrix.Multiply(inputWeight, _input);
        for (int i = 0; i < h.length; i++) {
            for (int j = 0; j < h[0].length; j++) {
                h[i][j] += bias[i];
            }
        }
        
        //Compute functions
        for (int i = 0; i < h.length; i++) {
            for (int j = 0; j < h[0].length; j++) {
                h[i][j] = function.Function(h[i][j]);
            }
        }
        
        //Add regularization factor.
        double[][] b = Matrix.MultiplyByTranspose(h);
        
        for (int i = 0; i < b.length; i++) {
            b[i][i] += 1.0 / c;
        }
        
        //Calculate the output weight
        outputWeight = Matrix.Multiply(Matrix.Inverse(b), h);
        outputWeight = Matrix.Multiply(outputWeight, Matrix.Transpose(t));
        
    }

    @Override
    public int Predict(double[] feature) {
        
        double[] temp = Matrix.MultiplyByTranspose(inputWeight, feature);
        Matrix.Add(temp, bias);
        
        //Compute the function
        for (int i = 0; i < temp.length; i++) {
            temp[i] = function.Function(temp[i]);
        }
        
        double[] r = Matrix.Multiply(temp, outputWeight);
        int v = Matrix.MaxIndex(r);
        
        return v;
        
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