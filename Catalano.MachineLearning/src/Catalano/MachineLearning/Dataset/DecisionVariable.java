// Catalano Machine Learning Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © César Souza, 2009-2013
// cesarsouza at gmail.com
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

package Catalano.MachineLearning.Dataset;

import java.io.Serializable;

/**
 * Decision attribute.
 * @author Diego Catalano
 */
public class DecisionVariable implements Serializable{
    
    /**
     * Attribute category.
     */
    public static enum Type{
        /**
         * Attribute is continuous-valued.
         */
        Continuous,
        
        /**
         * Attribute is discrete-valued.
         */
        Discrete
    };
    
    /**
     * Name of the attribute.
     */
    public String name;
    
    /**
     * Type of the attribute.
     */
    public Type type;
    
    /**
     * Create a new DecisionVariable.
     * @param name Name of the attribute.
     */
    public DecisionVariable(String name){
        this.name = name;
        this.type = Type.Continuous;
    }

    /**
     * Create a new DecisionVariable.
     * @param name Name of the attribute.
     * @param type Type of the attribute.
     */
    public DecisionVariable(String name, Type type) {
        this.name = name;
        this.type = type;
    }
}