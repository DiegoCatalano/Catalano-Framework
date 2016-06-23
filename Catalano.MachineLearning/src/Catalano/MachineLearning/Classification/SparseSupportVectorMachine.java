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
 * Sparse Support Vector Machine.
 * @author Diego Catalano
 */
public class SparseSupportVectorMachine implements Serializable{
    
    private IMercerKernel kernel;
    private double c;
    private double cn;
    
    private SVM<SparseArray> svm;
    
    /**
     * Initializes a new instance of the SparseSupportVectorMachine class.
     * @param kernel Mercer kernel.
     * @param c Soft margin penalty parameter.
     */
    public SparseSupportVectorMachine(IMercerKernel kernel, double c) {
        this(kernel, c, c);
    }
    
    /**
     * Initializes a new instance of the SparseSupportVectorMachine class.
     * @param kernel Mercer kernel.
     * @param c Soft margin penalty parameter for positive instances.
     * @param cn Soft margin penalty parameter for negative instances.
     */
    public SparseSupportVectorMachine(IMercerKernel kernel, double c, double cn) {
        this.kernel = kernel;
        this.c = c;
        this.cn = cn;
        Initialize(kernel, c, cn);
    }
    
    private void Initialize(IMercerKernel kernel, double c, double cn){
        this.svm = new SVM(kernel, c, cn);
    }

    
    public void Learn(SparseArray[] input, int[] output) {
        Initialize(kernel, c, cn);
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
    
    /*
     * Process support vectors until converge.
     */
    public void Finish(){
        svm.Finish();
    }   
}