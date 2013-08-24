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

/**
 * This class represents a fuzzy clause, a linguistic expression of the type "Variable IS Value".
 * @author Diego Catalano
 */
public class Clause {
    
    // the linguistic var of the clause
    private LinguisticVariable variable;
    // the label of the clause
    private FuzzySet label;

    /**
     * Initializes a new instance of the <see cref="Clause"/> class.
     * @param variable Linguistic variable of the clause.
     * @param label Label of the linguistic variable, a fuzzy set used as label into the linguistic variable.
     */
    public Clause(LinguisticVariable variable, FuzzySet label) {
        
        variable.getLabel(label.Name());
        
        this.variable = variable;
        this.label = label;
    }

    /**
     * Gets the <see cref="LinguisticVariable"/> of the <see cref="Clause"/>.
     * @return LinguisticVariable.
     */
    public LinguisticVariable getVariable() {
        return variable;
    }

    /**
     * Gets the <see cref="FuzzySet"/> of the <see cref="Clause"/>.
     * @return FuzzySet.
     */
    public FuzzySet getLabel() {
        return label;
    }
    
    /**
     * Evaluates the fuzzy clause.
     * @return Degree of membership [0..1] of the clause.
     */
    public float Evaluate(){
        return label.getMembership(variable.getNumericInput());
    }
    
    @Override
    public String toString(){
        return this.variable.getName() + " IS " + this.label.Name();
    }
}
