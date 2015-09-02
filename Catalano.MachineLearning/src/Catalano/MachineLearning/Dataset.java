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
import Catalano.Statistics.DescriptiveStatistics;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Dataset.
 * @author Diego Catalano
 */
public class Dataset implements Serializable{
    
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
    private int continuous = 0;

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
     * Get the number of type continuous.
     * @return Number of type continuous.
     */
    public int getNumberOfContinuous() {
        return continuous;
    }
    
    /**
     * Get the number of type discrete.
     * @return Number of type discrete.
     */
    public int getNumberOfDiscrete(){
        return this.attributes.length - continuous;
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
        int continuous = 0;
        
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
                        continuous++;
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
        
        
        return new Dataset(name, attributes, input, output, numClasses, continuous);
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
     * @param continuous Number of continuous type.
     */
    public Dataset(String name, DecisionVariable[] attributes, double[][] input, int[] output, int numClasses, int continuous){
        this.name = name;
        this.attributes = attributes;
        this.input = input;
        this.output = output;
        this.numClasses = numClasses;
        this.continuous = continuous;
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
    
    public DatasetStatistics[] DatasetStatistics(){
        
        int continuous = 0;
        for (int i = 0; i < attributes.length; i++) {
            if(attributes[i].type == DecisionVariable.Type.Continuous)
                continuous++;
        }
        
        DatasetStatistics[] stat = new DatasetStatistics[continuous];
        int idx = 0;
        for (int i = 0; i < attributes.length; i++) {
            if(attributes[i].type == DecisionVariable.Type.Continuous){
                
                double[] temp = Matrix.getColumn(input, i);
                double mean = DescriptiveStatistics.Mean(temp);
                double median = DescriptiveStatistics.Median(temp);
                double min = DescriptiveStatistics.Minimum(temp);
                double max = DescriptiveStatistics.Maximum(temp);
                double std = DescriptiveStatistics.StandartDeviation(temp, mean);
                double kurtosis = DescriptiveStatistics.Kurtosis(temp, mean, std);
                double skewness = DescriptiveStatistics.Skewness(temp, mean, std);
                
                stat[idx++] = new DatasetStatistics(attributes[i].name, mean, median, min, max, std, skewness, kurtosis);
            }
        }
        return stat;
    }
    
    public void WriteAsCSV(String filename){
        WriteAsCSV(filename,-1,',', System.getProperty("line.separator"));
    }
    
    public void WriteAsCSV(String filename, int decimalPlaces){
        WriteAsCSV(filename,decimalPlaces,',', System.getProperty("line.separator"));
    }
    
    public void WriteAsCSV(String filename, int decimalPlaces, char delimiter){
        WriteAsCSV(filename,decimalPlaces,delimiter, System.getProperty("line.separator"));
    }
    
    public void WriteAsCSV(String filename, int decimalPlaces, char delimiter, String newLine){
        try {
            String dec = "%." + decimalPlaces + "f";
            
            FileWriter fw = new FileWriter(filename);
            
            //Header
            for (int i = 0; i < attributes.length; i++) {
                fw.append(attributes[i].name + delimiter);
            }
            fw.append("Class" + newLine);
            
            //Data
            for (int i = 0; i < input.length; i++) {
                for (int j = 0; j < input[0].length; j++) {
                    if(decimalPlaces >= 0)
                        fw.append(String.format(Locale.US, dec, input[i][j]) + delimiter);
                    else
                        fw.append(String.valueOf(input[i][j]) + delimiter);
                }
                fw.append(String.valueOf(output[i]) + newLine);
            }
            
            fw.flush();
            fw.close();
            
        } catch (IOException ex) {
            Logger.getLogger(Dataset.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void WriteAsARFF(String filename, String relationName){
        WriteAsARFF(filename, relationName, -1);
    }
    
    public void WriteAsARFF(String filename, String relationName, int decimalPlaces){
        try {
            String dec = "%." + decimalPlaces + "f";
            String newLine = System.getProperty("line.separator");
            char delimiter = ',';
            
            FileWriter fw = new FileWriter(filename);
            
            //Relation name
            fw.append("@RELATION " + relationName);
            fw.append(newLine + newLine);
            
            //Attribute information
            for (int i = 0; i < attributes.length; i++) {
                if(attributes[i].type == DecisionVariable.Type.Continuous){
                    fw.append("@ATTRIBUTE " + attributes[i].name.replace(" ", "_") + " NUMERIC");
                    fw.append(newLine);
                }
                else{
                    String nominal = "{";
                    int max = (int)Matrix.Max(Matrix.getColumn(input, i));
                    for (int j = 0; j < max; j++) {
                        nominal += j + ", ";
                    }
                    nominal += max + "}";
                    fw.append("@ATTRIBUTE " + attributes[i].name.replace(" ", "_") + nominal);
                    fw.append(newLine);
                }
            }
            
            //Class information
            String nominal = "{";
            int max = Matrix.Max(output);
            for (int j = 0; j < max; j++) {
                nominal += j + ", ";
            }
            nominal += max + "}";
            fw.append("@ATTRIBUTE class " + nominal);
            fw.append(newLine);
            
            //Data
            fw.append(newLine);
            fw.append("@DATA" + newLine);
            for (int i = 0; i < input.length; i++) {
                for (int j = 0; j < input[0].length; j++) {
                    if(decimalPlaces >= 0)
                        fw.append(String.format(Locale.US, dec, input[i][j]) + delimiter);
                    else
                        fw.append(String.valueOf(input[i][j]) + delimiter);
                }
                fw.append(String.valueOf(output[i]) + newLine);
            }
            
            fw.flush();
            fw.close();
            
        } catch (IOException ex) {
            Logger.getLogger(Dataset.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}