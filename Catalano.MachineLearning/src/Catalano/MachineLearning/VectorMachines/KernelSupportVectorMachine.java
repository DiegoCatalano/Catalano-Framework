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

public class KernelSupportVectorMachine extends SupportVectorMachine{

    private IKernel kernel;

    /**
    * Creates a new Kernel Support Vector Machine.
    * <para> If the number of inputs is zero, this means the machine
    * accepts a indefinite number of inputs. This is often the
    * case for kernel vector machines using a sequence kernel.</para>
    * @param kernel The chosen kernel for the machine.
    * @param inputs The number of inputs for the machine.
    */
    public KernelSupportVectorMachine(IKernel kernel, int inputs){
        super(inputs);

        if (kernel == null) throw new IllegalArgumentException("kernel");

        this.kernel = kernel;
    }

    /**
    * Gets the kernel used by this machine.
    * @return Kernel.
    */
    public IKernel getKernel(){
        return kernel;
    }
        
    /**
    * Sets the kernel used by this machine.
    * @param value Kernel.
    */
    public void setKernel(IKernel value){
        kernel = value;
    }
        
    /**
    * Computes the given input to produce the corresponding output.
    * 
    * <para> For a binary decision problem, the decision for the negative
    * or positive class is typically computed by taking the sign of
    * the machine's output.</para>
    * 
    * @param inputs An input vector.
    * @return The decision label for the given input.
    */
    @Override
    public double Compute(double[] inputs){
        double output = getThreshold();

                    double[] weights = getWeights();
                    double[][] vectors = getSupportVectors();

        if (IsCompact()){
            for (int i = 0; i < weights.length; i++)
                output += weights[i] * inputs[i];
        }
        else{
            for (int i = 0; i < vectors.length; i++)
                output += weights[i] * kernel.Function(vectors[i], inputs);
        }

        return output;
    }
}