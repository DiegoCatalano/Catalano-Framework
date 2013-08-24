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
 * The class represents a fuzzy database, a set of linguistic variables used in a Fuzzy Inference System.
 * @author Diego Catalano
 */
public class Database {
    
    private HashMap<String, LinguisticVariable> variables;

    /**
     * Initializes a new instance of the Database class.
     */
    public Database() {
        this.variables = new HashMap<String, LinguisticVariable>(10);
    }
    
    /**
     * Adds a linguistic variable to the database.
     * @param variable A linguistic variable to add.
     */
    public void addVariable(LinguisticVariable variable){
        // checking for existing name
        if ( this.variables.containsKey( variable.getName() ) )
            try {
            throw new Exception( "The linguistic variable name already exists in the database." );
        } catch (Exception ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }

        // adding label
        this.variables.put( variable.getName(), variable );
    }
    
    /**
     * Removes all the linguistic variables of the database.
     */
    public void ClearVariables(){
        this.variables.clear();
    }
    
    /**
     * Returns an existing linguistic variable from the database.
     * @param variableName Name of the linguistic variable to retrieve.
     * @return Reference to named LinguisticVariable.
     */
    public LinguisticVariable getVariable(String variableName){
        return variables.get(variableName);
    }
}
