// Catalano Machine Learning Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2015
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

import Catalano.Core.DoubleRange;
import Catalano.MachineLearning.Classification.DecisionTrees.DecisionVariable;
import Catalano.Math.Matrix;
import Catalano.Math.Tools;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Dataset.
 * @author Diego Catalano
 */
public class Dataset {
    
    public static enum Format{
        /**
         * CSV structure.
         */
        CSV
    };
    
    private final String name;
    private final double[][] input;
    private final int[] output;
    private final DecisionVariable[] attributes;
    private final int numClasses;

    /**
     *Get the name of the dataset.
     * @return Name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the input data.
     * @return Input data.
     */
    public double[][] getInput() {
        return input;
    }

    /**
     * Get the output data.
     * @return Output data.
     */
    public int[] getOutput() {
        return output;
    }

    /**
     * Get the decision variables.
     * @return Decision Variables.
     */
    public DecisionVariable[] getDecisionVariables() {
        return attributes;
    }
    
    /**
     * Get the number of instances.
     * @return Number of instances.
     */
    public int getNumberOfInstances(){
        return input.length;
    }
    
    /**
     * Get the number of attributes.
     * @return Number of attributes.
     */
    public int getNumberOfAttributes(){
        return attributes.length + 1;
    }
    
    /**
     * Get the number of classes.
     * @return Number of classes.
     */
    public int getNumberOfClasses(){
        return numClasses;
    }
    
    /**
     * Construct a dataset from an Input Strem Reader.
     * @param reader Reader.
     * @param name Name of the dataset.
     * @param format Format of file.
     * @return Dataset.
     */
    public static Dataset FromReader(InputStreamReader reader, String name, Format format){
        double[][] input = null;
        int[] output = null;
        DecisionVariable[] attributes = null;
        int numClasses = 0;
        
        try {
            BufferedReader br = new BufferedReader(reader);//new InputStreamReader(new FileInputStream(filepath), "UTF-8"));
            
            String line;
            List<String> lines = new ArrayList<String>();
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            
            if(lines.size() > 0) {
                String[] header = lines.get(0).split(String.valueOf(','));
                String[] firstInstance = lines.get(1).split(String.valueOf(','));
                
                //Build: Decision variable
                attributes = new DecisionVariable[header.length - 1];
                HashSet<String> hs = new HashSet<String>();
                int discretes = 0;
                for (int i = 0; i < attributes.length; i++) {
                    hs.add(header[i]);
                    if(Tools.isNumeric(firstInstance[i])){
                        attributes[i] = new DecisionVariable(header[i], DecisionVariable.Type.Continuous);
                    }
                    else{
                        attributes[i] = new DecisionVariable(header[i], DecisionVariable.Type.Discrete);
                        discretes++;
                    }
                }
                if(hs.size() != attributes.length)
                    throw new IllegalArgumentException("The column names of attributes must be unique.");
                
                
                //Build: Input data
                input = new double[lines.size() - 1][attributes.length];
                List<HashMap<String,Integer>> lst;
                int[] indexes;
                if(discretes == 0){
                    lst = null;
                    indexes = null;
                }
                else{
                    lst = new ArrayList<HashMap<String,Integer>>(discretes);
                    for (int i = 0; i < discretes; i++) {
                        lst.add(new HashMap<String, Integer>());
                    }
                    indexes = new int[discretes];
                }
                
                int idxAtt;
                for (int i = 1; i < lines.size(); i++) {
                    idxAtt = 0;
                    String[] temp = lines.get(i).split(String.valueOf(','));
                    for (int j = 0; j < attributes.length; j++) {
                        if(attributes[j].type == DecisionVariable.Type.Continuous){
                            input[i-1][j] = Double.valueOf(temp[j]);
                        }
                        else{
                            HashMap<String,Integer> map = lst.get(idxAtt);
                            if(!map.containsKey(temp[j]))
                                map.put(temp[j], indexes[idxAtt]++);
                            idxAtt++;
                            input[i-1][j] = map.get(temp[j]);
                        }
                    }
                }
                
                //Build Output data
                output = new int[lines.size() - 1];
                int idx = 0;
                HashMap<String,Integer> map = new HashMap<String, Integer>();
                for (int j = 1; j < lines.size(); j++) {
                    String[] temp = lines.get(j).split(String.valueOf(","));
                    String s = temp[temp.length - 1];
                    if(!map.containsKey(s)){
                        map.put(s, idx++);
                        output[j-1] = map.get(s);
                        numClasses++;
                    }
                    else{
                        output[j-1] = map.get(s);
                    }
                }
                
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Dataset.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Dataset.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new Dataset(name, attributes, input, output, numClasses);
    }
    
    /**
     * Read dataset from a CSV structure.
     * @param filepath File path.
     * @param name Name of the dataset.
     * @return Dataset.
     */
    public static Dataset FromCSV(String filepath, String name){
        
        try {
            return FromReader(new InputStreamReader(new FileInputStream(filepath), "UTF-8"), name, Format.CSV);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Dataset.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Dataset.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
        
    }
    
    /**
     * Initializes a new instance of the Dataset class.
     * @param name Name of the dataset.
     * @param attributes Decision variables.
     * @param input Input data.
     * @param output Output data.
     * @param numClasses Number of classes.
     */
    public Dataset(String name, DecisionVariable[] attributes, double[][] input, int[] output, int numClasses){
        this.name = name;
        this.attributes = attributes;
        this.input = input;
        this.output = output;
        this.numClasses = numClasses;
    }
    
    /**
     * Normalize all continuous data.
     * Default: (0..1).
     * @return Range of the normalization.
     */
    public DoubleRange[] Normalize(){
        return Normalize(0,1);
    }
    
    /**
     * Normalize all continuous data.
     * @param min Minimum.
     * @param max Maximum.
     * @return Range of the normalization.
     */
    public DoubleRange[] Normalize(double min, double max){
        
        int continuous = 0;
        for (int i = 0; i < attributes.length; i++) {
            if(attributes[i].type == DecisionVariable.Type.Continuous)
                continuous++;
        }
        
        DoubleRange[] range = new DoubleRange[continuous];
        
        int idx = 0;
        for (int i = 0; i < attributes.length; i++) {
            if(attributes[i].type == DecisionVariable.Type.Continuous){
                double[] temp = Matrix.getColumn(input, i);
                double _min = Catalano.Statistics.Tools.Min(temp);
                double _max = Catalano.Statistics.Tools.Max(temp);
                range[idx++] = new DoubleRange(_min, _max);
                for (int j = 0; j < temp.length; j++) {
                    input[j][i] = Catalano.Math.Tools.Scale(_min, _max, min, max, temp[j]);
                }
            }
        }
        
        return range;
    }
    
    /**
     * Standartize all continuous data.
     * x = (x - u) / s
     * @return Range of standartization.
     */
    public DoubleRange[] Standartize(){
        
        int continuous = 0;
        for (int i = 0; i < attributes.length; i++) {
            if(attributes[i].type == DecisionVariable.Type.Continuous)
                continuous++;
        }
        
        DoubleRange[] range = new DoubleRange[continuous];
        
        int idx = 0;
        for (int i = 0; i < attributes.length; i++) {
            if(attributes[i].type == DecisionVariable.Type.Continuous){
                double[] temp = Matrix.getColumn(input, i);
                double mean = Catalano.Statistics.Tools.Mean(temp);
                double std = Catalano.Statistics.Tools.StandartDeviation(temp, mean);
                range[idx++] = new DoubleRange(mean, std);
                for (int j = 0; j < temp.length; j++) {
                    input[j][i] = (input[j][i] - mean) / std;
                }
            }
        }
        
        return range;
    }
}