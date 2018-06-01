// Catalano Machine Learning Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2018
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

package Catalano.MachineLearning;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Codebook.
 * Translate predicted class into a name.
 * @author Diego Catalano
 */
public class Codebook implements Serializable{
    
    private final String[] names;

    /**
     * Initialize a new instance of the Codebook class.
     * @param names Names
     */
    public Codebook(String[] names) {
        this.names = names;
    }
    
    /**
     * Initialize a new instance of the Codebook class.
     * @param map Map of the names.
     */
    public Codebook(HashMap<String, Integer> map){
        
        names = new String[map.size()];
        
        Iterator it = map.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            names[(Integer)pair.getValue()] = (String)pair.getKey();
        }
        
    }
    
    /**
     * Translate.
     * @param code Code.
     * @return Name.
     */
    public String Translate(int code){
        return names[code];
    }
    
}
