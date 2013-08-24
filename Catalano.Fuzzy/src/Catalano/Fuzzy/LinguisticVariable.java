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

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class represents a linguistic variable.
 * @author Diego Catalano
 */
public class LinguisticVariable {
    // name of the linguistic variable
    private String name;
    // right limit within the lingusitic variable works
    private float start;
    // left limit within the lingusitic variable works
    private float end;
    // the linguistic labels of the linguistic variable
    private HashMap<String, FuzzySet> labels;
    // the numeric input of this variable
    private float numericInput;
    
    /**
     * Initializes a new instance of the LinguisticVariable class.
     * @param name Name of the linguistic variable.
     * @param start Left limit of the valid variable range.
     * @param end Right limit of the valid variable range.
     */
    public LinguisticVariable(String name, float start, float end){
        this.name = name;
        this.start = start;
        this.end = end;
        
        this.labels = new HashMap<String, FuzzySet>(10);
    }

    /**
     * Get numerical value of the input of this linguistic variable.
     * @return Value.
     */
    public float getNumericInput() {
        return numericInput;
    }

    /**
     * Set numerical value of the input of this linguistic variable.
     * @param numericInput Value.
     */
    public void setNumericInput(float numericInput) {
        this.numericInput = numericInput;
    }

    /**
     * Get name of the linguistic variable.
     * @return Name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get left limit of the valid variable range.
     * @return Value.
     */
    public float getStart() {
        return start;
    }

    /**
     * Get right limit of the valid variable range.
     * @return Value.
     */
    public float getEnd() {
        return end;
    }
    
    /**
     * Adds a linguistic label to the variable.
     * @param label A FuzzySet that will be a linguistic label of the linguistic variable.
     */
    public void addLabel(FuzzySet label){
        // checking for existing name
        if ( this.labels.containsKey( label.Name() ) )
            try {
            throw new Exception( "The linguistic label name already exists in the linguistic variable." );
        } catch (Exception ex) {
            Logger.getLogger(LinguisticVariable.class.getName()).log(Level.SEVERE, null, ex);
        }

        // checking ranges
        if ( label.LeftLimit() < this.start )
            try {
            throw new Exception( "The left limit of the fuzzy set can not be lower than the linguistic variable's starting point." );
        } catch (Exception ex) {
            Logger.getLogger(LinguisticVariable.class.getName()).log(Level.SEVERE, null, ex);
        }
        if ( label.RightLimit() > this.end )
            try {
            throw new Exception( "The right limit of the fuzzy set can not be greater than the linguistic variable's ending point." );
        } catch (Exception ex) {
            Logger.getLogger(LinguisticVariable.class.getName()).log(Level.SEVERE, null, ex);
        }

        // adding label
        this.labels.put(label.Name(), label);
    }
    
    public void ClearLabels(){
        this.labels.clear();
    }
    
    public FuzzySet getLabel(String labelName){
        return labels.get(labelName);
    }
    
    public float GetLabelMembership(String labelName, float value){
        FuzzySet fs = labels.get(labelName);
        return fs.getMembership(value);
    }
}
