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

    public class SupportVectorMachine{

        private int inputCount;
        private double[][] supportVectors;
        private double[] weights;
        private double threshold;

        /**
         * Creates a new Support Vector Machine.
         * @param inputs The number of inputs for the machine.
         */
        public SupportVectorMachine(int inputs){
            this.inputCount = inputs;
        }

        /**
         * Gets the number of inputs accepted by this machine.
         * <para> If the number of inputs is zero, this means the machine
         * accepts a indefinite number of inputs. This is often the
         * case for kernel vector machines using a sequence kernel.</para>
         * @return Number of inputs.
         */
        public int getInputs(){
            return inputCount;
        }

        /**
         * Gets the collection of support vectors used by this machine.
         * @return Collection of support vectors.
         */
        public double[][] getSupportVectors(){   
		    return supportVectors;
        }
		
        /**
         * Sets the collection of support vectors used by this machine.
         * @param value Collection of support vectors.
         */
        public void getSupportVectors(double[][] value){   
		    supportVectors = value;
        }

        /**
         * Gets whether this machine is in compact mode. Compact
         * machines do not need to keep storing their support vectors.
         * @return 
         */
        public boolean IsCompact(){
            return supportVectors == null;
        }

        /**
         * Gets the collection of weights used by this machine.
         * @return Collection of weights.
         */
        public double[] getWeights(){
            return weights;
        }
		
        /**
         * Sets the collection of weights used by this machine.
         * @param value Collection of weights.
         */
        public void setWeights(double[] value){
            weights = value;
        }
        
        /**
         * Gets the threshold (bias) term for this machine.
         * @return Threshold value.
         */
        public double getThreshold(){
            return threshold;
        }
	
        /**
         * Sets the threshold (bias) term for this machine.
         * @param value Threshold value.
         */
	public void setThreshold(double value){
            threshold = value;
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
        public double Compute(double[] inputs){
            double output = threshold;

            if (supportVectors == null){
                for (int i = 0; i < weights.length; i++)
                    output += weights[i] * inputs[i];
            }
            else{
                for (int i = 0; i < supportVectors.length; i++){
                    double sum = 0;
                    for (int j = 0; j < inputs.length; j++)
                        sum += supportVectors[i][j] * inputs[j];
                    output += weights[i] * sum;
                }
            }

            return output;
        }
    }