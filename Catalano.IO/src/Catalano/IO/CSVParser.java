/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.IO;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Reads CSV file
 * @author Diego Catalano
 */
public class CSVParser {
    
    private char delimiter = ';';
    private int startRow = 0;
    private int startCol = 0;
    String charset = "UTF8";

    public CSVParser() {}
    
    public CSVParser(char delimiter) {
        this.delimiter = delimiter;
    }
    
    public CSVParser(char delimiter, int startRow, int startColumn){
        this.delimiter = delimiter;
        this.startRow = startRow;
        this.startCol = startColumn;
    }
    
    public String[][] Read(String filename){
        
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), charset));
            
            String line;
            List<String> lines = new ArrayList<>();
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
    
}
