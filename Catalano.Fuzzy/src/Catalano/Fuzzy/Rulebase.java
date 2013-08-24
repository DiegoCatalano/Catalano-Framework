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
 * The class represents a fuzzy rulebase, a set of fuzzy rules used in a Fuzzy Inference System.
 * @author Diego Catalano
 */
public class Rulebase {
    // the fuzzy rules repository
    private HashMap<String, Rule> rules;

    /**
     * Initializes a new instance of the Rulebase class.
     */
    public Rulebase() {
        this.rules = new HashMap<String, Rule>(20);
    }
    
    /**
     * Adds a fuzzy rule to the database.
     * @param rule >A fuzzy Rule to add to the database.
     */
    public void addRule(Rule rule){
        // checking for existing name
        if ( this.rules.containsKey( rule.getName() ) )
            try {
            throw new Exception( "The fuzzy rule name already exists in the rulebase." );
        } catch (Exception ex) {
            Logger.getLogger(Rulebase.class.getName()).log(Level.SEVERE, null, ex);
        }

        // adding rule
        this.rules.put( rule.getName(), rule );
    }
    
    /**
     * Removes all the fuzzy rules of the database.
     */
    public void clearRules(){
        this.rules.clear();
    }
    
    /**
     * Get an existing fuzzy rule from the rulebase.
     * @param ruleName Name of the fuzzy Rule to retrieve.
     * @return Reference to named Rule.
     */
    public Rule getRule(String ruleName){
        return rules.get(ruleName);
    }
    
    /**
     * Gets all the rules of the rulebase.
     * @return An array with all the rulebase rules.
     */
    public Rule[] getRules(){
        Rule[] r = new Rule[rules.size()];

        for (int i = 0; i < rules.size(); i++) {
            r[i] = rules.get(i);
        }
        
        //for ( KeyValuePair<String, Rule> kvp : rules )
            //r[i++] = kvp.Value;

        return r;
    }
}