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

import Catalano.Math.SparseArray;
import Catalano.Statistics.Kernels.IMercerKernel;
import java.io.Serializable;

/**
 * Sparse Multiclass Support Vector Machine.
 * @author Diego Catalano
 */
public class SparseMulticlassSupportVectorMachine implements Serializable{
    
    private IMercerKernel kernel;
    private double c;
    private int numberOfClasses;
    
    private SVM.Strategy strategy;
    private SVM<SparseArray> svm;
    
    /**
     * Initializes a new instance of the SparseMulticlassSupportVectorMachine class.
     * @param kernel Mercer kernel.
     * @param c Soft margin penalty parameter.
     * @param numberOfClasses Number of classes.
     */
    public SparseMulticlassSupportVectorMachine(IMercerKernel kernel, double c, int numberOfClasses) {
        this(kernel, c, numberOfClasses, SVM.Strategy.ONE_VS_ONE);
    }
    
    /**
     * Initializes a new instance of the SparseMulticlassSupportVectorMachine class.
     * @param kernel Mercer kernel.
     * @param c Soft margin penalty parameter.
     * @param numberOfClasses Number of classes.
     * @param strategy Strategy.
     */
    public SparseMulticlassSupportVectorMachine(IMercerKernel kernel, double c, int numberOfClasses, SVM.Strategy strategy) {
        this.kernel = kernel;
        this.c = c;
        this.numberOfClasses = numberOfClasses;
        this.strategy = strategy;
        Initialize(kernel, c, numberOfClasses, strategy);
    }
    
    /**
     * Initializes a new instance of the SparseMulticlassSupportVectorMachine class.
     * @param kernel Mercer kernel.
     * @param c Soft margin penalty parameter.
     * @param weight Class weight.
     */
    public SparseMulticlassSupportVectorMachine(IMercerKernel kernel, double c, double[] weight) {
        this(kernel, c, weight, SVM.Strategy.ONE_VS_ONE);
    }
    
    /**
     * Initializes a new instance of the SparseMulticlassSupportVectorMachine class.
     * @param kernel Mercer kernel.
     * @param c Soft margin penalty parameter.
     * @param weight Class weight.
     * @param strategy Strategy.
     */
    public SparseMulticlassSupportVectorMachine(IMercerKernel kernel, double c, double[] weight, SVM.Strategy strategy) {
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

    public void Learn(SparseArray[] input, int[] output) {
        Initialize(kernel, c, numberOfClasses, strategy);
        svm.Learn(input, output);
    }
    
    /**
     * Online learn.
     * @param input Feature.
     * @param output Label.
     */
    public void Learn(SparseArray input, int output){
        svm.Learn(input, output);
    }

    public int Predict(SparseArray feature) {
        return svm.Predict(feature);
    }
    
    /**
     * Process support vectors until convergence.
     */
    public void Finish(){
        svm.Finish();
    }   
}