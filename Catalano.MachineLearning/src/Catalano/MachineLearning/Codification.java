// Catalano Machine Learning Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Convert the string dataset for symbolic integers.
 * @author Diego Catalano
 */
public class Codification {
    
    private String[] columnNames;
    private String[][] labels;
    
    private List<HashMap<String, Integer>> lst;
    
    /**
     * Set the data for codification.
     * @param columnName Column's name.
     * @param labels Labels.
     */
    public void setData(String columnName, String[] labels){
        this.columnNames = new String[] {columnName};
        this.labels = new String[labels.length][1];
        for (int i = 0; i < labels.length; i++) {
            this.labels[i][0] = labels[i];
        }
        Map(this.labels);
    }
    
    /**
     * Set the data for codification.
     * @param columnNames Column's name.
     * @param labels Labels.
     */
    public void setData(String[] columnNames, String[][] labels){
        if(columnNames.length != labels[0].length)
            throw new IllegalArgumentException("The size of column names must be the same size of number of columns of labels.");
        
        this.columnNames = columnNames;
        this.labels = labels;
        Map(labels);
    }
    
    /**
     * Initializes a new instance of the Codification class.
     * @param columnName Column's name.
     * @param labels Labels.
     */
    public Codification(String columnName, String[] labels){
        setData(columnName, labels);
    }
    
    /**
     * Initializes a new instance of the ConfusionMatrix class.
     * @param columnNames Column's name.
     * @param labels Labels.
     */
    public Codification(String[] columnNames, String[][] labels){
        setData(columnNames, labels);
    }
    
    private void Map(String[][] labels){
        
        //Find uniques labels from each column
        lst = new ArrayList<HashMap<String, Integer>>();
        HashMap<String, Integer> map;
        
        int idx;
        for (int j = 0; j < labels[0].length; j++) {
            idx = 0;
            map = new HashMap<String, Integer>();
            for (int i = 0; i < labels.length; i++) {
                if(!map.containsKey(labels[i][j]))
                    map.put(labels[i][j], idx++);
            }
            
            //Add in the list of maps
            lst.add(map);
        }
    }
    
    /**
     * Convert labels to symbols.
     * @param columnName Column's name.
     * @param labels Labels.
     * @return The array of symbol.
     */
    public int[] Translate(String columnName, String[] labels){
        int[] symbols = new int[labels.length];
        for (int i = 0; i < columnNames.length; i++) {
            if(columnNames[i].equals(columnName)){
                for (int k = 0; k < symbols.length; k++) {
                    symbols[k] = lst.get(i).get(labels[k]);
                }
            }
            else{
                throw new IllegalArgumentException("Invalid column name.");
            }
        }
        return symbols;
    }
    
    /**
     * Get the specified symbol from the name.
     * @param columnName Column name.
     * @param name Name.
     * @return Symbol.
     */
    public int Translate(String columnName, String name){
        
        for (int i = 0; i < columnNames.length; i++) {
            if(columnNames[i].equals(columnName))
                return lst.get(i).get(name);
            else
                throw new IllegalArgumentException("Invalid column name.");
        }
        
        return -1;
        
    }
    
    /**
     * Get the name from the symbol.
     * @param columnName Column name.
     * @param symbol Symbol.
     * @return Name.
     */
    public String getName(String columnName, int symbol){
        for (int i = 0; i < columnNames.length; i++) {
            if(columnNames[i].equals(columnName)){
                for (Map.Entry m : lst.get(i).entrySet())
                    if((Integer)(m.getValue()) == symbol)
                        return (String)m.getKey();
            }
            else{
                throw new IllegalArgumentException("Invalid column name.");
            }
        }
        return null;
    }
}