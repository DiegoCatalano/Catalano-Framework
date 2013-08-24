// Catalano Fuzzy Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2013
// diego.catalano at live.com
//
// Copyright © Andrew Kirillov, 2007-2008
// andrew.kirillov at gmail.com
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

package Catalano.Fuzzy;

import java.util.ArrayList;

/**
 * The class represents the output of a Fuzzy Inference System.
 * @author Diego Catalano
 */
public class FuzzyOutput {
    
    /**
     *  Inner class to store the pair fuzzy label / firing strength of a fuzzy output.
     */
    public class OutputConstraint{
        
        private String label;
        private float firingStrength;

        /**
         * Initializes a new instance of the OutputConstraint class.
         * @param label A string representing the output label of a Rule.
         * @param firingStrength The firing strength of a Rule, to be applied to its output label.
         */
        OutputConstraint(String label, float firingStrength) {
            this.label = label;
            this.firingStrength = firingStrength;
        }

        /**
         * The FuzzySet representing the output label of a Rule.
         * @return Label.
         */
        public String getLabel() {
            return label;
        }

        /**
         * The firing strength of a Rule, to be applied to its output label.
         * @return Firing strength.
         */
        public float getFiringStrength() {
            return firingStrength;
        }
    }
    
    // the linguistic variables repository 
    private ArrayList<OutputConstraint> outputList;

    // the output linguistic variable 
    private LinguisticVariable outputVar;

    /**
     * Get a list with OutputConstraint of a Fuzzy Inference System's output.
     * @return Output Constrain list.
     */
    public ArrayList<OutputConstraint> getOutputList() {
        return outputList;
    }

    /**
     * Gets the LinguisticVariable acting as a Fuzzy Inference System Output.
     * @return Linguistic Variable.
     */
    public LinguisticVariable getOutputVariable(){
        return outputVar;
    }
    
    /**
     * Initializes a new instance of the FuzzyOutput class.
     * @param outputVar A LinguisticVariable representing a Fuzzy Inference System's output.
     */
    FuzzyOutput(LinguisticVariable outputVar) {
        this.outputList = new ArrayList<OutputConstraint>(20);
        this.outputVar = outputVar;
    }
    
    /**
     * Adds an output to the Fuzzy Output.
     * @param labelName The name of a label representing a fuzzy rule's output.
     * @param firingStrength The firing strength [0..1] of a fuzzy rule.
     */
    public void addOutput(String labelName, float firingStrength){
        this.outputVar.getLabel( labelName );
        this.outputList.add(new OutputConstraint(labelName, firingStrength));
    }
    
    /**
     * Removes all the linguistic variables of the database.
     */
    public void clearOutput(){
        this.outputList.clear();
    }
}