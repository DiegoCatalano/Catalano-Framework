// Catalano Machine Learning Library
// The Catalano Framework
//
// Copyright © César Souza, 2009-2013
// cesarsouza at gmail.com
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

package Catalano.MachineLearning.VectorMachines;

import Catalano.Statistics.Kernels.IKernel;

/**
* One-against-all Multi-label Kernel Support Vector Machine Classifier.
* @author Diego Catalano
*/
public class MultilabelSupportVectorMachine{

        // Underlying classifiers
    private KernelSupportVectorMachine[] machines;

       
    /// <summary>
    ///   Constructs a new Multi-label Kernel Support Vector Machine
    /// </summary>
    /// 
    /// <param name="kernel">The chosen kernel for the machine.</param>
    /// <param name="inputs">The number of inputs for the machine.</param>
    /// <param name="classes">The number of classes in the classification problem.</param>
    /// 
    /// <remarks>
    ///   If the number of inputs is zero, this means the machine
    ///   accepts a indefinite number of inputs. This is often the
    ///   case for kernel vector machines using a sequence kernel.
    /// </remarks>
    /// 
    public MultilabelSupportVectorMachine(int inputs, IKernel kernel, int classes){
        if (classes <= 1)
            throw new IllegalArgumentException("The machine must have at least two classes.");

        // Create the kernel machines
        machines = new KernelSupportVectorMachine[classes];
        for (int i = 0; i < machines.length; i++)
            machines[i] = new KernelSupportVectorMachine(kernel, inputs);
    }

    /// <summary>
    ///   Constructs a new Multi-label Kernel Support Vector Machine
    /// </summary>
    /// 
    /// <param name="machines">
    ///   The machines to be used for each class.
    /// </param>
    /// 
    public MultilabelSupportVectorMachine(KernelSupportVectorMachine[] machines){
        if (machines == null) throw new IllegalArgumentException("machines is null");

        this.machines = machines;
    }

    /// <summary>
    ///   Gets the number of classes.
    /// </summary>
    /// 
    public int getClasses(){
        return machines.length; 
    }

    /// <summary>
    ///   Gets the number of inputs of the machines.
    /// </summary>
    /// 
    public int getInputs(){
        return machines[0].getInputs();
    }

        
    /// <summary>
    ///   Gets the subproblems classifiers.
    /// </summary>
    /// 
    public KernelSupportVectorMachine[] getMachines(){
        return machines;
    }

    /// <summary>
    ///   Computes the given input to produce the corresponding outputs.
    /// </summary>
    /// 
    /// <param name="inputs">An input vector.</param>
    /// <param name="responses">The model response for each class.</param>
    /// 
    /// <returns>The decision label for the given input.</returns>
    /// 
    public int[] Compute(double[] inputs){
        int[] labels = new int[machines.length];
        double[] outputs = new double[machines.length];

        // For each machine
        for (int i = 0; i < machines.length; i++){
            double y = machines[i].Compute(inputs);
            labels[i] = (int)Math.signum(y);
        }

        return labels;
    }
}