/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.IO;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Reads CSV file
 * @author Diego Catalano
 */
public class CSVParser {
    
    private char delimiter = ',';
    private int startRow = 0;
    private int startCol = 0;
    String charset = "UTF8";

    public char getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(char delimiter) {
        this.delimiter = delimiter;
    }

    public CSVParser() {}
    
    public CSVParser(char delimiter) {
        this.delimiter = delimiter;
    }
    
    public CSVParser(char delimiter, int startRow, int startColumn){
        this.delimiter = delimiter;
        this.startRow = startRow;
        this.startCol = startColumn;
    }
    
    /**
     * Read CSV and convert to object type.
     * The method find the constructor that contains the maximum parameters.
     * @param <T> Object type.
     * @param filename CSV filename.
     * @param clazz Object type.
     * @return List of user definied type.
     */
    public <T> List<T> Read(String filename, Class clazz){
        String[][] data = Read(filename);
        return toObject(data, clazz);
    }
    
    /**
     * Read CSV.
     * @param filename Filename.
     * @return CSV table of string.
     */
    public String[][] Read(String filename){
        
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), charset));
            
            String line;
            List<String> lines = new ArrayList<String>();
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            
            if(lines.size() > 0) {
                String[] temp = lines.get(0).split(String.valueOf(delimiter));
                String[][] data = new String[lines.size()][temp.length];
                for (int i = startRow; i < lines.size(); i++) {
                    temp = lines.get(i).split(String.valueOf(delimiter));
                    for (int j = startCol; j < temp.length; j++) {
                        data[i][j] = temp[j];
                    }
                }

                return data;
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CSVParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CSVParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    /**
     * Convert the CSV string to user definied type.
     * The method find the constructor that contains the maximum parameters.
     * @param <T> Object type.
     * @param data CSV data.
     * @param cls Object type.
     * @return List of user definied type.
     */
    private <T> List<T> toObject(String[][] data, Class cls){
        
        int p = 0;
        Class[] paramTypes = null;
        
        for (Constructor con : cls.getConstructors()) {
            if(con.getParameterCount() > p){
                p = con.getParameterCount();
                paramTypes = con.getParameterTypes();
            }
        }
        
        List<T> lst = new ArrayList<T>();
       
        try{
            for (int i = startRow; i < data.length; i++) {
                Constructor c = cls.getConstructor(paramTypes);
                Object[] obj = new Object[paramTypes.length];
                for (int j = startCol; j < paramTypes.length; j++) {
                    //String cannot be cast in int type.
                    if(paramTypes[j] == int.class){
                        obj[j] = Integer.parseInt(data[i][j]);
                    }
                    else if(paramTypes[j] == double.class){
                        obj[j] = Double.parseDouble(data[i][j]);
                    }
                    else if(paramTypes[j] == float.class){
                        obj[j] = Double.parseDouble(data[i][j]);
                    }
                    else if(paramTypes[j] == String.class){
                        obj[j] = data[i][j];
                    }
                }
                
                lst.add((T) c.newInstance(obj));
            }
            return lst;
        }
        catch (NoSuchMethodException ex) {
            Logger.getLogger(CSVParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SecurityException ex) {
            Logger.getLogger(CSVParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(CSVParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CSVParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(CSVParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(CSVParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void Write(String[][] data, String filename){
        try {
            FileWriter fw = new FileWriter(filename);
            
            boolean isLastCol;
            for (int i = 0; i < data.length; i++) {
                isLastCol = false;
                for (int j = 0; j < data[0].length; j++) {
                    if(j==data[0].length-1) isLastCol = true;
                    if(isLastCol)
                        fw.append(data[i][j]);
                    else
                        fw.append(data[i][j] + delimiter);
                }
                fw.append("\r\n");
            }
            
            fw.flush();
            fw.close();
            
        } catch (IOException ex) {
            Logger.getLogger(CSVParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public <T> void Write(List<T> objects, String filename){
        try {
            FileWriter fw = new FileWriter(filename);
            
            boolean isLastCol;
            for (int i = 0; i < objects.size(); i++) {
                isLastCol = false;
                Object o = objects.get(i);
                Method[] m = o.getClass().getDeclaredMethods();
                for (int j = 0; j < m.length; j++) {
                    if(j==m.length - 1) isLastCol = true;
                    if(m[j].getName().startsWith("get")){
                        if(isLastCol)
                            fw.append(m[j].invoke(o).toString());
                        else
                            fw.append(m[j].invoke(o).toString() + delimiter);
                    }
                }
                fw.append("\r\n");
            }
            
            fw.flush();
            fw.close();
            
        } catch (IOException ex) {
            Logger.getLogger(CSVParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CSVParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(CSVParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(CSVParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}