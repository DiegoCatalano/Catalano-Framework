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
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents a Fuzzy Rule, a linguistic expression representing some behavioral aspect of a Fuzzy Inference System.
 * @author Diego Catalano
 */
public class Rule {
    
    // name of the rule 
    private String name;
    // the original expression 
    private String rule;
    // the parsed RPN (reverse polish notation) expression
    private ArrayList<Object> rpnTokenList;
    // the consequento (output) of the rule
    private Clause output;
    // the database with the linguistic variables
    private Database database;
    // the norm operator
    private INorm normOperator;
    // the conorm operator
    private ICoNorm conormOperator;
    // the complement operator
    private IUnaryOperator notOperator;
    // the unary operators that the rule parser supports
    private String unaryOperators = "NOT;VERY";

    /**
     * The name of the fuzzy rule.
     * @return Name.
     */
    public String getName() {
        return name;
    }

    /**
     * The name of the fuzzy rule.
     * @param name Name.
     */
    public void setName(String name) {
        this.name = name;
    }
        
    /**
     * The fuzzy Clause that represents the consequent of the Rule.
     * @see Clause.
     * @see Rule.
     * @return Clause.
     */
    public Clause getOutput(){
        return output;
    }
    
    /**
     * Initializes a new instance of the <see cref="Rule"/> class.
     * @param fuzzyDatabase A fuzzy <see cref="Database"/> containig the linguistic variables see <see cref="LinguisticVariable"/>) that will be used in the Rule.
     * @param name Name of this <see cref="Rule"/>.
     * @param rule A string representing the <see cref="Rule"/>. It must be a "IF..THEN" statement. For a more detailed  description see <see cref="Rule"/> class.
     * @param normOperator A class that implements a <see cref="INorm"/> interface to evaluate the AND operations of the Rule.
     * @param coNormOperator A class that implements a <see cref="ICoNorm"/> interface to evaluate the OR operations of the Rule.
     */
    public Rule(Database fuzzyDatabase, String name, String rule, INorm normOperator, ICoNorm coNormOperator){
        // the list with the RPN expression
        rpnTokenList = new ArrayList<Object>( );

        // setting attributes
        this.name           = name;
        this.rule           = rule;
        this.database       = fuzzyDatabase;
        this.normOperator   = normOperator;
        this.conormOperator = coNormOperator;
        this.notOperator    = new NotOperator( );

        // parsing the rule to obtain RPN of the expression
        ParseRule();
    }
    
    /**
     * Initializes a new instance of the <see cref="Rule"/> class using as CoNorm the <see cref="MaximumCoNorm"/> and as Norm the <see cref="MinimumNorm"/>.
     * @param fuzzyDatabase A fuzzy <see cref="Database"/> containig the linguistic variables see <see cref="LinguisticVariable"/>) that will be used in the Rule.
     * @param name Name of this <see cref="Rule"/>.
     * @param rule A string representing the <see cref="Rule"/>. It must be a "IF..THEN" statement. For a more detailed  description see <see cref="Rule"/> class.
     */
    public Rule(Database fuzzyDatabase, String name, String rule){
        this( fuzzyDatabase, name, rule, new MinimumNorm(), new MaximumCoNorm());
    }
    
    private int Priority(String Operator){
        // if its unary
        if ( unaryOperators.indexOf(Operator) >= 0 )
            return 4;

        if (Operator.equals("(")) return 1;
        if (Operator.equals("OR")) return 2;
        if (Operator.equals("AND")) return 3;
        
        return 0;
    }
    
    /**
     * Converts the RPN fuzzy expression into a string representation.
     * @return String representation of the RPN fuzzy expression.
     */
    public String GetRPNExpression() {
        String result = "";
        for ( Object o : rpnTokenList ){
            // if its a fuzzy clause we can call clause's ToString()
            if ( o.getClass().isAssignableFrom(Clause.class) )
            {
                Clause c = (Clause)o;
                result += c.toString( );
            }
            else
                result += o.toString( );
            result += ", ";
        }
        result += "#";
        result = result.replace( ", #", "" );
        return result;
    }
    
    private void ParseRule() {
        // flag to incicate we are on consequent state
        boolean consequent = false;

        // tokens like IF and THEN will be searched always in upper case
        String upRule = rule.toUpperCase();

        // the rule must start with IF, and must have a THEN somewhere
        if ( !upRule.startsWith( "IF" ) )
            try {
                throw new Exception( "A Fuzzy Rule must start with an IF statement." );
            } catch (Exception ex) {
                Logger.getLogger(Rule.class.getName()).log(Level.SEVERE, null, ex);
            }
        if ( upRule.indexOf( "THEN" ) < 0 )
            try {
                throw new Exception( "Missing the consequent (THEN) statement." );
            } catch (Exception ex) {
                Logger.getLogger(Rule.class.getName()).log(Level.SEVERE, null, ex);
            }

        // building a list with all the expression (rule) string tokens
        String spacedRule = rule.replace( "(", " ( " ).replace( ")", " ) " );
        // getting the tokens list
        String[] tokensList = GetRuleTokens( spacedRule );

        // stack to convert to RPN
        Stack<String> s = new Stack<String>( );
        // storing the last token
        String lastToken = "IF";
        // linguistic var read, used to build clause
        LinguisticVariable lingVar = null;

        // verifying each token
        for ( int i = 0; i < tokensList.length; i++ ){
            // removing spaces
            String token = tokensList[i].trim();
            // getting upper case
            String upToken = token.toUpperCase();

            // ignoring these tokens
            if (upToken.equals("") || upToken.equals("IF")) continue;

            // if the THEN is found, the rule is now on consequent
            if ( upToken.equals("THEN") ){
                lastToken = upToken;
                consequent = true;
                continue;
            }

            // if we got a linguistic variable, an IS statement and a label is needed
            if ( lastToken.equals("VAR")){
                if (upToken.equals("IS")) 
                    lastToken = upToken;
                else
                    try {
                        throw new Exception( "An IS statement is expected after a linguistic variable." );
                    } catch (Exception ex) {
                        Logger.getLogger(Rule.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
            // if we got an IS statement, a label must follow it
            else if (lastToken.equals("IS")){
                try {
                    FuzzySet fs = lingVar.getLabel(token);
                    Clause c = new Clause(lingVar, fs);
                    if (consequent)
                        output = c;
                    else
                        rpnTokenList.add(c);
                    lastToken = "LAB";
                } catch(Exception e){
                    try {
                        throw new Exception( "Linguistic label " + token + " was not found on the variable " + lingVar.getName() + "." );
                    } catch (Exception ex) {
                        Logger.getLogger(Rule.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            // not VAR and not IS statement 
            else{
                // openning new scope
                if ( upToken.equals("(") ){
                    // if we are on consequent, only variables can be found
                    if ( consequent )
                        try {
                            throw new Exception( "Linguistic variable expected after a THEN statement." );
                        } catch (Exception ex) {
                            Logger.getLogger(Rule.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    // if its a (, just push it
                    s.push( upToken );
                    lastToken = upToken;
                }
                // operators
                else if ( upToken.equals("AND") || upToken.equals("OR") || unaryOperators.indexOf( upToken ) >= 0 ){
                    // if we are on consequent, only variables can be found
                    if ( consequent )
                        try {
                            throw new Exception( "Linguistic variable expected after a THEN statement." );
                        } catch (Exception ex) {
                            Logger.getLogger(Rule.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    // pop all the higher priority operators until the stack is empty 
                    while ( ( s.size() > 0 ) && ( Priority( s.peek( ) ) > Priority( upToken ) ) )
                        rpnTokenList.add( s.pop( ) );

                    // pushing the operator    
                    s.push( upToken );
                    lastToken = upToken;
                }
                // closing the scope
                else if ( upToken.equals(")") ){
                    // if we are on consequent, only variables can be found
                    if ( consequent )
                        try {
                            throw new Exception( "Linguistic variable expected after a THEN statement." );
                    } catch (Exception ex) {
                        Logger.getLogger(Rule.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    // if there is nothing on the stack, an oppening parenthesis is missing.
                    if ( s.size() == 0 )
                        try {
                            throw new Exception( "Openning parenthesis missing." );
                        } catch (Exception ex) {
                            Logger.getLogger(Rule.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    // pop the tokens and copy to output until openning is found 
                    while ( !s.peek().equals("(") ){
                        rpnTokenList.add( s.pop( ) );
                        if ( s.size() == 0 )
                            try {
                                throw new Exception( "Openning parenthesis missing." );
                            } catch (Exception ex) {
                                Logger.getLogger(Rule.class.getName()).log(Level.SEVERE, null, ex);
                            }
                    }
                    s.pop( );

                    // saving last token...
                    lastToken = upToken;
                }
                // finally, the token is a variable
                else{
                    // find the variable
                    try{
                        lingVar = database.getVariable(token);
                        lastToken = "VAR";
                    }
                    catch (Exception e){
                        try {
                            throw new Exception( "Linguistic variable " + token + " was not found on the database." );
                        } catch (Exception ex) {
                            Logger.getLogger(Rule.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }

        // popping all operators left in stack
        while (s.size() > 0)
            rpnTokenList.add(s.pop());
    }
        
    private String[] GetRuleTokens( String rule ){
        // breaking in tokens
        String[] tokens = rule.split(" ");

        // looking for unary operators
        for ( int i = 0; i < tokens.length; i++ ){
            // if its unary and there is an "IS" token before, we must change positions
            if ( ( unaryOperators.indexOf( tokens[i].toUpperCase( ) ) >= 0 ) &&
                    ( i > 1 ) && ( tokens[i - 1].toUpperCase( ) == "IS" ) )
            {
                // placing VAR name
                tokens[i - 1] = tokens[i - 2];
                tokens[i - 2] = tokens[i];
                tokens[i] = "IS";
            }
        }

        return tokens;
    }
        
    /**
    * Evaluates the firing strength of the Rule, the degree of confidence that the consequent of this Rule must be executed.
    * @return The firing strength [0..1] of the Rule.
    */
    public float EvaluateFiringStrength(){
    // Stack to store the operand values
    Stack<Float> s = new Stack<Float>();

    // Logic to calculate the firing strength
    for (Object o : rpnTokenList){
        // if its a clause, then its value must be calculated and pushed
        if (o.getClass().isAssignableFrom(Clause.class))
        {
            Clause c = (Clause)o;
            s.push(c.Evaluate());
        }
        // if its an operator (AND / OR) the operation is performed and the result 
        // returns to the stack
        else
        {
            float y = s.pop( );
            float x = 0;

            // unary pops only one value
            if ( unaryOperators.indexOf( o.toString( ) ) < 0 )
                x = s.pop( );

            // operation
            if (o.toString().equals("AND")) s.push( normOperator.Evaluate( x, y ) );
            if (o.toString().equals("OR")) s.push( normOperator.Evaluate( x, y ) );
            if (o.toString().equals("NOT")) s.push( normOperator.Evaluate( x, y ) );
        }
    }

    // result on the top of stack
    return s.pop();
    }
    
}
