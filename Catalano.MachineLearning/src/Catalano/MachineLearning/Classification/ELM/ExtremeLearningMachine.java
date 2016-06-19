// Catalano Machine Learning Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2016
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

package Catalano.MachineLearning.Classification.ELM;

import Catalano.MachineLearning.Classification.IClassifier;
import Catalano.MachineLearning.DatasetClassification;
import Catalano.Math.Matrix;
import java.util.Arrays;
import java.util.Random;

/**
 * Extreme Learning Machine.
 * @author Diego Catalano
 */
public class ExtremeLearningMachine implements IClassifier{
    
    private int nHiddenNodes;
    private IActivationFunction function;
    private double c = 1;
    
    private double[] bias;
    private double[][] inputWeight;
    private double[][] outputWeight;
    
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
        this(20, 1);
    }
    
    /**
     * Initializes a new instance of the ExtremeLearningMachine class.
     * @param nHiddenNodes Number of hidden nodes.
     * @param c Regularization factor.
     */
    public ExtremeLearningMachine(int nHiddenNodes, double c){
        this(nHiddenNodes, c, new Sigmoid());
    }

    /**
     * Initializes a new instance of the ExtremeLearningMachine class.
     * @param nHiddenNodes Number of hidden nodes.
     * @param c Regularization factor.
     * @param function Activation function.
     */
    public ExtremeLearningMachine(int nHiddenNodes, double c, IActivationFunction function) {
        this.nHiddenNodes = nHiddenNodes;
        this.c = c;
        this.function = function;
    }

    @Override
    public void Learn(DatasetClassification dataset) {
        Learn(dataset.getInput(), dataset.getOutput());
    }

    @Override
    public void Learn(double[][] input, int[] output) {
        
        Random r = new Random();
        
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
        inputWeight = new double[nHiddenNodes][_input.length];
        for (int i = 0; i < inputWeight.length; i++) {
            for (int j = 0; j < inputWeight[0].length; j++) {
                inputWeight[i][j] = r.nextDouble()* 2 - 1;
            }
        }
        
        //Create random biases BiasofHiddenNeurons
        bias = new double[nHiddenNodes];
        for (int i = 0; i < bias.length; i++) {
            bias[i] = r.nextDouble();
        }
        
        //Add bias
        double[][] h = Matrix.Multiply(inputWeight, _input);
        for (int i = 0; i < h.length; i++) {
            for (int j = 0; j < h[0].length; j++) {
                h[i][j] += bias[i];
            }
        }
        
        //Compute a function
        function.Compute(h);
        
        //Add regularization factor.
        double[][] b = Matrix.MultiplyByTranspose(h);
        
        for (int i = 0; i < b.length; i++) {
            b[i][i] += 1.0/c;
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
        function.Compute(temp);
        
        double[] r = Matrix.Multiply(temp, outputWeight);
        int v = Matrix.MaxIndex(r);
        System.out.println(v);
        
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