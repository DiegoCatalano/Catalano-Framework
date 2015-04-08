// Catalano IO Library
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

package Catalano.IO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Serilize objects using standart JVM.
 * @author Diego Catalano.
 */
public class Serialization {

    private Serialization() {}
    
    /**
     * Serialize object.
     * @param object Object.
     * @param filename Filename to save the data.
     */
    public static void Serialize(Object object, String filename){
        try {
            FileOutputStream fOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fOut);
            out.writeObject(object);
            out.close();
            fOut.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Serialization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Serialization.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Deserialize object.
     * @param filename Filename for to deserialize.
     * @return Object.
     */
    public static Object Deserialize(String filename){
        try {
            FileInputStream fIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fIn);
            Object o = in.readObject();
            in.close();
            fIn.close();
            return o;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Serialization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Serialization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Serialization.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
}