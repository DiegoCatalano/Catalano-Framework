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
 * This class represents a Fuzzy Inference System. 
 * @author Diego Catalano
 */
public class InferenceSystem {
    // The linguistic variables of this system
    private Database database;
    // The fuzzy rules of this system
    private Rulebase rulebase;
    // The defuzzifier method choosen 
    private IDefuzzifier defuzzifier;
    // Norm operator used in rules and deffuzification
    private INorm normOperator;
    // CoNorm operator used in rules
    private ICoNorm conormOperator;

    public InferenceSystem(Database database, IDefuzzifier defuzzifier, INorm normOperator, ICoNorm conormOperator) {
        this.database = database;
        this.defuzzifier = defuzzifier;
        this.normOperator = normOperator;
        this.conormOperator = conormOperator;
        this.rulebase = new Rulebase();
    }
    
    public InferenceSystem(Database database, IDefuzzifier defuzzifier){
        this( database, defuzzifier, new MinimumNorm( ), new MaximumCoNorm( ) );
    }
    
    public Rule NewRule( String name, String rule ){
        Rule r = new Rule( database, name, rule, normOperator, conormOperator );
        this.rulebase.addRule( r );
        return r;
    }
    
    public void SetInput( String variableName, float value ){
        this.database.getVariable( variableName ).setNumericInput(value);
    }
    
    public LinguisticVariable GetLinguisticVariable( String variableName ){
        return this.database.getVariable( variableName );
    }
    
    public Rule GetRule( String ruleName ){
        return this.rulebase.getRule ( ruleName );
    }
    
    public float Evaluate( String variableName ){
        // call the defuzzification on fuzzy output 
        FuzzyOutput fuzzyOutput = ExecuteInference( variableName );
        float res = defuzzifier.Defuzzify( fuzzyOutput, normOperator );
        return res;
    }
    
    public FuzzyOutput ExecuteInference( String variableName ){
        // gets the variable
        LinguisticVariable lingVar = database.getVariable( variableName );

        // object to store the fuzzy output
        FuzzyOutput fuzzyOutput = new FuzzyOutput( lingVar );

        // select only rules with the variable as output
        Rule[] rules = rulebase.getRules( );
        for ( Rule r : rules )
        {
            if ( r.getOutput().getVariable().getName().equals(variableName))
            {
                String labelName = r.getOutput().getLabel().Name();
                float firingStrength = r.EvaluateFiringStrength( );
                if ( firingStrength > 0 )
                    fuzzyOutput.addOutput( labelName, firingStrength );
            }
        }

        // returns the fuzzy output obtained
        return fuzzyOutput;
    }
}
