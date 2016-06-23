// Catalano IO Library
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
 *
 * @author Diego Catalano
 */
public class ArffParser {
    
    boolean ignoreMissingValues = false;

    public boolean isIgnoreMissingValues() {
        return ignoreMissingValues;
    }

    public void setIgnoreMissingValues(boolean ignoreMissingValues) {
        this.ignoreMissingValues = ignoreMissingValues;
    }

    public ArffParser() {}
    
    public ArffParser(boolean ignoreMissingValues){
        this.ignoreMissingValues = ignoreMissingValues;
    }
    
    public String[][] Read(String filename){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
            
            String line;
            List<String> lines = new ArrayList<String>();
            while ((line = br.readLine()) != null) {
                if(line.toLowerCase().contains("@data"))
                    break;
            }
            
            while ((line = br.readLine()) != null) {
                if(!line.startsWith("%")){
                    if(ignoreMissingValues){
                        if(!lines.contains("?"))
                            lines.add(line);
                    }
                    lines.add(line);
                }
            }
            
            if(lines.size() > 0) {
                String[] temp = lines.get(0).split(String.valueOf(","));
                String[][] data = new String[lines.size()][temp.length];
                for (int i = 0; i < lines.size(); i++) {
                    temp = lines.get(i).split(String.valueOf(","));
                    for (int j = 0; j < temp.length; j++) {
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