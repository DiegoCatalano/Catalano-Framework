// Catalano Machine Learning Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2015
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

package Catalano.MachineLearning.FeatureSelection;

import Catalano.MachineLearning.Classification.DecisionTrees.DecisionVariable;
import Catalano.MachineLearning.Classification.DecisionTrees.Learning.RandomForest;

/**
 * Ranker for feature selection.
 * @author Diego Catalano
 */
public class Ranker {
    
    /**
     * Info gain.
     * @param input Input.
     * @param output Output.
     * @return Rank of the attributes.
     */
    public static double[] InfoGain(double[][] input, int[] output){
        return InfoGain(null, input, output);
    }
    
    /**
     * Info gain.
     * @param attributes Attributes.
     * @param input Input.
     * @param output Output.
     * @return Rank of the attributes.
     */
    public static double[] InfoGain(DecisionVariable[] attributes, double[][] input, int[] output){
        RandomForest rf = new RandomForest(attributes, 1, attributes.length, 1);
        rf.Learn(input, output);
        return rf.importance();
    }
}