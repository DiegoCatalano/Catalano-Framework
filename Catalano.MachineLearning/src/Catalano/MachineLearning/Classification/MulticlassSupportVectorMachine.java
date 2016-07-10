// Catalano Machine Learning Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
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

package Catalano.MachineLearning.Classification;

import Catalano.MachineLearning.Dataset.DatasetClassification;
import Catalano.Statistics.Kernels.IMercerKernel;
import java.io.Serializable;

/**
 * Multiclass Support Vector Machine.
 * @author Diego Catalano
 */
public class MulticlassSupportVectorMachine implements IClassifier, Serializable {
    
    private IMercerKernel kernel;
    private double c;
    private int numberOfClasses;
    
    private SVM.Strategy strategy;
    private SVM<double[]> svm;
    
    /**
     * Initializes a new instance of the MulticlassSupportVectorMachine class.
     * @param kernel Mercer kernel.
     * @param c Soft margin penalty parameter.
     * @param numberOfClasses Number of classes.
     */
    public MulticlassSupportVectorMachine(IMercerKernel kernel, double c, int numberOfClasses) {
        this(kernel, c, numberOfClasses, SVM.Strategy.ONE_VS_ONE);
    }
    
    /**
     * Initializes a new instance of the MulticlassSupportVectorMachine class.
     * @param kernel Mercer kernel.
     * @param c Soft margin penalty parameter.
     * @param numberOfClasses Number of classes.
     * @param strategy Strategy.
     */
    public MulticlassSupportVectorMachine(IMercerKernel kernel, double c, int numberOfClasses, SVM.Strategy strategy) {
        this.kernel = kernel;
        this.c = c;
        this.numberOfClasses = numberOfClasses;
        this.strategy = strategy;
        Initialize(kernel, c, numberOfClasses, strategy);
    }
    
    /**
     * Initializes a new instance of the MulticlassSupportVectorMachine class.
     * @param kernel Mercer kernel.
     * @param c Soft margin penalty parameter.
     * @param weight Class weight.
     */
    public MulticlassSupportVectorMachine(IMercerKernel kernel, double c, double[] weight) {
        this(kernel, c, weight, SVM.Strategy.ONE_VS_ONE);
    }
    
    /**
     * Initializes a new instance of the MulticlassSupportVectorMachine class.
     * @param kernel Mercer kernel.
     * @param c Soft margin penalty parameter.
     * @param weight Class weight.
     * @param strategy Strategy.
     */
    public MulticlassSupportVectorMachine(IMercerKernel kernel, double c, double[] weight, SVM.Strategy strategy) {
        this.kernel = kernel;
        this.c = c;
        this.numberOfClasses = weight.length;
        this.strategy = strategy;
        Initialize(kernel, c, weight, strategy);
    }
    
    private void Initialize(IMercerKernel kernel, double c, int nClasses, SVM.Strategy strategy){
        this.svm = new SVM(kernel, c, nClasses, strategy);
    }
    
    private void Initialize(IMercerKernel kernel, double c, double[] weight, SVM.Strategy strategy){
        this.svm = new SVM(kernel, c, weight, strategy);
    }

    @Override
    public void Learn(DatasetClassification dataset) {
        Learn(dataset.getInput(), dataset.getOutput());
    }

    @Override
    public void Learn(double[][] input, int[] output) {
        Initialize(kernel, c, numberOfClasses, strategy);
        svm.Learn(input, output);
    }
    
    /**
     * Online learn.
     * @param input Feature.
     * @param output Label.
     */
    public void Learn(double[] input, int output){
        svm.Learn(input, output);
    }

    @Override
    public int Predict(double[] feature) {
        return svm.Predict(feature);
    }
    
    @Override
    public IClassifier clone() {
        try {
            return (IClassifier)super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new IllegalArgumentException("Clone not supported: " + ex.getMessage());
        }
    }
    
    /**
     * Process support vectors until convergence.
     */
    public void Finish(){
        svm.Finish();
    }   
}