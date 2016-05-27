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

import Catalano.Core.ArraysUtil;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Dataset for classification.
 * @author Diego Catalano
 */
public class DatasetClassification implements Serializable{
    
    private String name;
    private double[][] input;
    private int[] output;
    private DecisionVariable[] attributes;
    private int numClasses;
    private int continuous = 0;
    private int classIndex = -1;

    /**
     * Get the name of the dataset.
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
        return Matrix.RemoveColumn(attributes, classIndex);
    }
    
    /**
     * Get the decision variables including the output.
     * @return Decision Variables.
     */
    public DecisionVariable[] getAllDecisionVariables(){
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
     * Read dataset from a CSV structure.
     * The last column of CSV is interpreted as output.
     * 
     * @param filepath File path.
     * @param name Name of the dataset.
     * @return Classification Dataset.
     */
    public static DatasetClassification FromCSV(String filepath, String name){
        return FromCSV(filepath, name,false);
    }
    
    /**
     * Read dataset from a CSV structure.
     * The last column of CSV is interpreted as output.
     * 
     * @param filepath File path.
     * @param name Name of the dataset.
     * @param ignoreAttributeInfo Ignore attribute information.
     * @return Classification dataset.
     */
    public static DatasetClassification FromCSV(String filepath, String name, boolean ignoreAttributeInfo){
        return FromCSV(filepath, name, ignoreAttributeInfo, -1);
    }
    
    /**
     * Construct a dataset from an CSV file.
     * @param filepath File.
     * @param name Name of the dataset.
     * @param classIndex Index of the attribute for to be setup as output.
     * @param ignoreAttributeInfo Ignore attribute information.
     * @return Classification Dataset.
     */
    public static DatasetClassification FromCSV(String filepath, String name, boolean ignoreAttributeInfo, int classIndex){
        double[][] input = null;
        int[] output = null;
        DecisionVariable[] attributes = null;
        int numClasses = 0;
        int continuous = 0;
        int start;
        
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), "UTF-8"));
            
            String line;
            List<String> lines = new ArrayList<String>();
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            if(lines.size() > 0) {
                String[] header = null;
                String[] firstInstance = null;
                if(ignoreAttributeInfo){
                    firstInstance = lines.get(0).split(String.valueOf(','));
                    header = new String[firstInstance.length];
                    for (int i = 0; i < header.length - 1; i++) {
                        header[i] = "F" + i;
                    }
                    header[header.length - 1] = "Class";
                    start = 0;
                }
                else{
                    header = lines.get(0).split(String.valueOf(','));
                    firstInstance = lines.get(1).split(String.valueOf(','));
                    start = 1;
                }
                
                if(classIndex == -1) classIndex = header.length - 1;

                //Build: Decision variable
                attributes = new DecisionVariable[header.length];
                HashSet<String> hs = new HashSet<String>();
                int discretes = 0;
                int idx = 0;
                for (int i = 0; i < header.length; i++) {
                    hs.add(header[i]);
                    if(Tools.isNumeric(firstInstance[i])){
                        attributes[idx] = new DecisionVariable(header[i], DecisionVariable.Type.Continuous);
                        continuous++;
                    }
                    else{
                        attributes[idx] = new DecisionVariable(header[i], DecisionVariable.Type.Discrete);
                        discretes++;
                    }
                    idx++;
                }
                
                if(hs.size() != attributes.length)
                    throw new IllegalArgumentException("The column names of attributes must be unique.");


                //Build: Input data
                input = new double[lines.size() - start][attributes.length - 1];
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
                for (int i = start; i < lines.size(); i++) { //i=1
                    idxAtt = 0;
                    String[] temp = lines.get(i).split(String.valueOf(','));
                    idx = 0;
                    for (int j = 0; j < attributes.length; j++) {
                        if(j != classIndex){
                            if(attributes[j].type == DecisionVariable.Type.Continuous){
                                input[i-start][idx++] = Double.valueOf(fix(temp[j]));
                            }
                            else{
                                HashMap<String,Integer> map = lst.get(idxAtt);
                                if(!map.containsKey(temp[j]))
                                    map.put(temp[j], indexes[idxAtt]++);
                                idxAtt++;
                                input[i-start][idx++] = map.get(temp[j]);
                            }
                        }
                    }
                }

                //Build Output data from the class index.
                output = new int[lines.size() - start];
                idx = 0;
                HashMap<String,Integer> map = new HashMap<String, Integer>();
                for (int j = 1; j < lines.size(); j++) {
                    String[] temp = lines.get(j).split(String.valueOf(","));
                    String s = temp[classIndex];
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
            Logger.getLogger(DatasetClassification.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DatasetClassification.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return new DatasetClassification(name, attributes, input, output, numClasses, continuous, classIndex);
    }
    
    /**
     * Fix invalid chars.
     * @param x String
     * @return String.
     */
    private static String fix(String x){
        String r = "";
        for (int i = 0; i < x.length(); i++) {
            char c = x.charAt(i);
            if(Character.isDigit(c) || c == '.' || c == '-' || c == 'E')
                r += c;
        }
        return r;
    }
    
    /**
     * Initializes a new instance of the DatasetClassification class.
     * @param filepath File path.
     */
    public DatasetClassification(String filepath){
        this(filepath, "Unknown");
    }
    
    /**
     * Initializes a new instance of the DatasetClassification class.
     * @param filepath File path.
     * @param name Name of the dataset.
     */
    public DatasetClassification(String filepath, String name){
        this(filepath, name, false);
    }
    
    /**
     * Initializes a new instance of the DatasetClassification class.
     * @param filepath File path.
     * @param name Name of the dataset.
     * @param ignoreAttributeInfo Ignore first line (Attribute information).
     */
    public DatasetClassification(String filepath, String name, boolean ignoreAttributeInfo){
        this(filepath, name, ignoreAttributeInfo, -1);
    }
    
    /**
     * Initializes a new instance of the DatasetClassification class.
     * @param filepath File path.
     * @param name Name of the dataset.
     * @param classIndex Class index.
     * @param ignoreAttributeInfo Ignore first line (Attribute information).
     */
    public DatasetClassification(String filepath, String name, boolean ignoreAttributeInfo, int classIndex){
        DatasetClassification dataset = FromCSV(filepath, name, ignoreAttributeInfo, classIndex);
        
        this.name = dataset.getName();
        this.attributes = dataset.getAllDecisionVariables();
        this.continuous = dataset.getNumberOfContinuous();
        this.input = dataset.getInput();
        this.output = dataset.getOutput();
        this.numClasses = dataset.getNumberOfClasses();
    }
    
    /**
     * Initializes a new instance of the DatasetClassification class.
     * @param name Name of the dataset.
     * @param input Input data.
     * @param output Output data.
     */
    public DatasetClassification(String name, double[][] input, int[] output){
        this(name, input, output, null);
    }
    
    /**
     * Initializes a new instance of the DatasetClassification class.
     * @param name Name of the dataset.
     * @param attributes Decision variables.
     * @param input Input data.
     * @param output Output data.
     */
    public DatasetClassification(String name, double[][] input, int[] output, DecisionVariable[] attributes){
        this.name = name;
        this.input = input;
        this.output = output;
        this.numClasses = Matrix.Max(output) + 1;
        this.classIndex = input[0].length;
        if(attributes == null){
            attributes = new DecisionVariable[input[0].length + 1];
            for (int i = 0; i < attributes.length; i++) {
                attributes[i] = new DecisionVariable("F" + i);
            }
        }
        
        int c = 0;
        for (int i = 0; i < attributes.length; i++)
            if(attributes[i].type == DecisionVariable.Type.Continuous)
                c++;
        
        this.attributes = attributes;
        this.continuous = c;
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
    private DatasetClassification(String name, DecisionVariable[] attributes, double[][] input, int[] output, int numClasses, int continuous, int classIndex){
        this.name = name;
        this.attributes = attributes;
        this.input = input;
        this.output = output;
        this.numClasses = numClasses;
        this.continuous = continuous;
        this.classIndex = classIndex;
    }
    
    /**
     * Normalize all continuous data.
     * Default: (0..1).
     * @return Coefficient of the normalization.
     */
    public double[][] Normalize(){
        return Normalize(0,1);
    }
    
    /**
     * Normalize all continuous data.
     * @param min Minimum.
     * @param max Maximum.
     * @return Range of the normalization.
     */
    public double[][] Normalize(double min, double max){
        
        int continuous = 0;
        for (int i = 0; i < attributes.length; i++) {
            if(i != classIndex)
                if(attributes[i].type == DecisionVariable.Type.Continuous)
                    continuous++;
        }
        
        double[][] range = new double[2][continuous];
        
        int idx = 0;
        for (int i = 0; i < attributes.length - 1; i++) {
            if(i != classIndex){
                if(attributes[i].type == DecisionVariable.Type.Continuous){
                    double[] temp = Matrix.getColumn(input, i);
                    double _min = Catalano.Statistics.Tools.Min(temp);
                    double _max = Catalano.Statistics.Tools.Max(temp);
                    range[0][idx] = _min;
                    range[1][idx] = _max;
                    idx++;
                    for (int j = 0; j < temp.length; j++) {
                        input[j][i] = Catalano.Math.Tools.Scale(_min, _max, min, max, temp[j]);
                    }
                }
            }
        }
        
        return range;
    }
    
    /**
     * Remove an attribute.
     * @param index Index of the attribute.
     */
    public void RemoveAttribute(int index){
        this.input = Matrix.RemoveColumn(input, index);
        this.attributes = Matrix.RemoveColumn(attributes, index);
    }
    
    /**
     * Split the percentange of the dataset in Trainning and the rest in Validation.
     * @param percentage Percentage.
     * @return Validation dataset.
     */
    public DatasetClassification Split(float percentage){
        return Split(percentage,this.name + "_Validation");
    }
    
    /**
     * Split the percentange of the dataset in Trainning and the rest in Validation.
     * @param percentage Percentage.
     * @param name Name of the validation dataset.
     * @return Validation dataset.
     */
    public DatasetClassification Split(float percentage, String name){
        
        //Count labels and amount.
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int i = 0; i < output.length; i++) {
            if(!map.containsKey(output[i])){
                map.put(output[i], 1);
            }
            else{
                int t = map.get(output[i]) + 1;
                map.put(output[i], t);
            }
        }
        
        //Define size of training
        int[] sizeClass = new int[map.size()];
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            sizeClass[entry.getKey()] = (int)(entry.getValue() * percentage);
        }
        
        //Get the index of training and validation features
        int size = 0;
        for (int i = 0; i < sizeClass.length; i++) {
            size += sizeClass[i];
        }
        
        int[] indexTraining = new int[size];
        int[] indexValidation = new int[input.length - size];
        
        int idxT = 0;
        int idxV = 0;
        for (int i = 0; i < input.length; i++) {
            if(sizeClass[output[i]] > 0){
                indexTraining[idxT++] = i;
                sizeClass[output[i]]--;
            }
            else{
                indexValidation[idxV++] = i;
            }
        }
        
        //Build data validation
        double[][] valInput = Matrix.getRows(input, indexValidation);
        int[] valOutput = Matrix.getRows(output, indexValidation);
        
        //Build data trainning
        this.input = Matrix.getRows(input, indexTraining);
        this.output = Matrix.getRows(output, indexTraining);
        
        return new DatasetClassification(name, valInput, valOutput, this.attributes);
        
    }
    
    /**
     * Sort the dataset in relation of the labels.
     */
    public void Sort(){
        
        //Sort input
        int[] idx = ArraysUtil.Argsort(output, true);
        for (int j = 0; j < input[0].length; j++) {
            double[] col = Matrix.getColumn(input, j);
            for (int i = 0; i < col.length; i++)
                input[i][j] = col[idx[i]];
        }
        
        //Sort labels
        int[] copy = (int[])output.clone();
        for (int i = 0; i < output.length; i++)
            output[i] = copy[idx[i]];
        
    }
    
    /**
     * Standartize all continuous data.
     * x = (x - u) / s
     * @return Coefficients of the standartization.
     */
    public double[][] Standartize(){
        
        int continuous = 0;
        for (int i = 0; i < attributes.length; i++) {
            if(i != classIndex)
                if(attributes[i].type == DecisionVariable.Type.Continuous)
                    continuous++;
        }
        
        double[][] range = new double[2][continuous];
        
        int idx = 0;
        for (int i = 0; i < attributes.length - 1; i++) {
            if(i != classIndex){
                if(attributes[i].type == DecisionVariable.Type.Continuous){
                    double[] temp = Matrix.getColumn(input, i);
                    double mean = Catalano.Statistics.Tools.Mean(temp);
                    double std = Catalano.Statistics.Tools.StandartDeviation(temp, mean);
                    range[0][idx] = mean;
                    range[1][idx] = std;
                    idx++;
                    for (int j = 0; j < temp.length; j++) {
                        input[j][i] = (input[j][i] - mean) / std;
                    }
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
    
    /**
     * Write the dataset as CSV file.
     * @param filename Filename.
     */
    public void WriteAsCSV(String filename){
        WriteAsCSV(filename,-1,',', System.getProperty("line.separator"));
    }
    
    /**
     * Write the dataset as CSV file.
     * @param filename Filename.
     * @param decimalPlaces Decimal places for continous data.
     */
    public void WriteAsCSV(String filename, int decimalPlaces){
        WriteAsCSV(filename,decimalPlaces,',', System.getProperty("line.separator"));
    }
    
    /**
     * Write the dataset as CSV file.
     * @param filename Filename.
     * @param decimalPlaces Decimal places for continous data.
     * @param delimiter Delimiter.
     */
    public void WriteAsCSV(String filename, int decimalPlaces, char delimiter){
        WriteAsCSV(filename,decimalPlaces,delimiter, System.getProperty("line.separator"));
    }
    
    /**
     *  Write the dataset as CSV file.
     * @param filename Filename.
     * @param decimalPlaces Decimal places.
     * @param delimiter Delimiter.
     * @param newLine Newline char.
     */
    public void WriteAsCSV(String filename, int decimalPlaces, char delimiter, String newLine){
        try {
            String dec = "%." + decimalPlaces + "f";
            
            FileWriter fw = new FileWriter(filename);
            
            //Header
            for (int i = 0; i < attributes.length; i++) {
                if(i!=classIndex)
                    fw.append(attributes[i].name + delimiter);
            }
            fw.append(attributes[classIndex].name);
            fw.append(newLine);
            
            //Data
            for (int i = 0; i < input.length; i++) {
                for (int j = 0; j < input[0].length; j++) {
                    if(attributes[j].type == DecisionVariable.Type.Continuous){
                        if(decimalPlaces >= 0)
                            fw.append(String.format(Locale.US, dec, input[i][j]) + delimiter);
                        else
                            fw.append(String.valueOf(input[i][j]) + delimiter);
                    }
                    else{
                        fw.append(String.valueOf(input[i][j]) + delimiter);
                    }
                }
                fw.append(String.valueOf(output[i]) + newLine);
            }
            
            fw.flush();
            fw.close();
            
        } catch (IOException ex) {
            Logger.getLogger(DatasetClassification.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Write dataset as ARFF file.
     * @param filename Filename.
     */
    public void WriteAsARFF(String filename){
        WriteAsARFF(filename, -1);
    }
    
    /**
     * Write dataset as ARFF file.
     * @param filename Filename.
     * @param decimalPlaces Decimal places.
     */
    public void WriteAsARFF(String filename, int decimalPlaces){
        try {
            String dec = "%." + decimalPlaces + "f";
            String newLine = System.getProperty("line.separator");
            char delimiter = ',';
            
            FileWriter fw = new FileWriter(filename);
            
            //Relation name
            fw.append("@RELATION " + name);
            fw.append(newLine + newLine);
            
            //Attribute information
            for (int i = 0; i < attributes.length; i++) {
                if(i != classIndex){
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
            }
            
            //Class information
            String nominal = "{";
            int max = Matrix.Max(output);
            for (int j = 0; j < max; j++) {
                nominal += j + ", ";
            }
            nominal += max + "}";
            fw.append("@ATTRIBUTE " + attributes[classIndex].name + " " + nominal);
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
            Logger.getLogger(DatasetClassification.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}