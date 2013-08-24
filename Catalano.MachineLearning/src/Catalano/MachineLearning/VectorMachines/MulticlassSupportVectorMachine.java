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


import Catalano.Math.Matrix;
import Catalano.Statistics.Kernels.IKernel;
   
    
    public class MulticlassSupportVectorMachine{

        // List of underlying binary classifiers
        private KernelSupportVectorMachine[][] machines;
        
        /**
         * Constructs a new Multi-class Kernel Support Vector Machine.
         * 
         * <para> If the number of inputs is zero, this means the machine
         * accepts a indefinite number of inputs. This is often the
         * case for kernel vector machines using a sequence kernel.</para>
         * 
         * @param inputs The number of inputs for the machine.
         * @param kernel The chosen kernel for the machine.
         * @param classes The number of classes in the classification problem.
         */
        public MulticlassSupportVectorMachine(int inputs, IKernel kernel, int classes)
        {
            if (classes <= 1)
                throw new IllegalArgumentException("The machine must have at least two classes.");

            // Create the kernel machines
            machines = new KernelSupportVectorMachine[classes - 1][];
            for (int i = 0; i < machines.length; i++){
                machines[i] = new KernelSupportVectorMachine[i + 1];

                for (int j = 0; j <= i; j++)
                    machines[i][j] = new KernelSupportVectorMachine(kernel, inputs);
            }
        }

        /**
         * Constructs a new Multi-class Kernel Support Vector Machine.
         * @param machines The machines to be used in each of the pairwise class subproblems.
         */
        public MulticlassSupportVectorMachine(KernelSupportVectorMachine[][] machines)
        {
            if (machines == null)
                throw new IllegalArgumentException("machines is null");

            this.machines = machines;
        }

        /**
         * Gets the classifier for class1 against class2.
         * <para> If the index of class1 is greater than class2,
         * the classifier for the class2 against class1
         * will be returned instead. If both indices are equal, null will be returned instead.
         * @param class1 Class1.
         * @param class2 Class2.
         * @return Classifier.
         */
        public KernelSupportVectorMachine getMachine(int class1, int class2)
        {
                if (class1 == class2)
                    return null;
                if (class1 > class2)
                    return machines[class1 - 1][class2];
                else
                    return machines[class2 - 1][class1];
        }

        /**
         * Gets the total number of machines in this multi-class classifier.
         * @return Total number of machines.
         */
        public int getMachinesCount(){
            return ((machines.length + 1) * machines.length) / 2;
        }

        /**
         * Gets the number of classes.
         * @return Number of classes.
         */
        public int getClasses(){
            return machines.length + 1;
        }
        
        /**
         * Gets the number of inputs of the machines.
         * @return Number of inputs.
         */
        public int getInputs(){
            return machines[0][0].getInputs();
        }

        /**
         * Compute.
         * @param inputs Number of inputs.
         * @param responses Responses.
         * @return Result.
         */
        public int Compute(double[] inputs, double[] responses){
                int[] votes = new int[getClasses()];
                int result = computeVoting(inputs, votes);

                responses = new double[votes.length];
                for (int i = 0; i < responses.length; i++)
                    responses[i] = votes[i] * (2.0 / (getClasses() * (getClasses() - 1)));

                return result;
        }
        
        /**
         * Computes the given input to produce the corresponding output.
         * @param inputs An input vector.
         * @param votes A vector containing the number of votes for each class.
         * @return The decision label for the given input.
         */
        private int computeVoting(double[] inputs, int[] votes){
            // Compute decision by Voting

            // out variables cannot be passed into delegates,
            // so will be creating a copy for the vote array.
            //int[] voting = new int[getClasses()];

            // For each class
            for (int i = 0; i < getClasses(); i++){
                // For each other class
                for (int j = 0; j < i; j++){
                    // Retrieve and compute the two-class problem for classes i x j
                    double answer = machines[i - 1][j].Compute(inputs);

                    // Determine the winner class
                    int y = (answer < 0) ? i : j;

                    // Increment votes for the winner
                    votes[y]++;
                }
            }


            // Select class with maximum number of votes
            return Matrix.MaxIndex(votes); // Return the winner as the output.
        }
    }