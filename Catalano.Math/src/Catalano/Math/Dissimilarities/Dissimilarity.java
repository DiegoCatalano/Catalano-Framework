// Catalano Math Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
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

package Catalano.Math.Dissimilarities;

/**
 * Defines a set of extension methods defining dissimilarity measures.
 * @author Diego Catalano
 */
public final class Dissimilarity {

    /**
     * Don't let anyone initialize this class.
     */
    private Dissimilarity() {}
    
    /**
     * Gets the Dice dissimilarity between two points.
     * @param x A point in space.
     * @param y A point in space.
     * @return The Dice dissimilarity between x and y.
     */
    public static double Dice(int[] x, int[] y){
        
        int tf = 0;
        int ft = 0;
        int tt = 0;
        
        for (int i = 0; i < x.length; i++) {
            if(x[i] == 1 && y[i] == 0) tf++;
            if(x[i] == 0 && y[i] == 1) ft++;
            if(x[i] == 1 && y[i] == 1) tt++;
        }
        
        return (tf + ft) / (double)(2*tt + ft + tf);
    }
    
    /**
     * Gets the Jaccard dissimilarity between two points.
     * @param x A point in space.
     * @param y A point in space.
     * @return The Jaccard dissimilarity between x and y.
     */
    public static double Jaccard(int[] x, int[] y){
        
        int tf = 0;
        int ft = 0;
        int tt = 0;
        
        for (int i = 0; i < x.length; i++) {
            if(x[i] == 1 && y[i] == 0) tf++;
            if(x[i] == 0 && y[i] == 1) ft++;
            if(x[i] == 1 && y[i] == 1) tt++;
        }
        
        return (tf + ft) / (double)(tt + ft + tf);
    }
    
    /**
     * Gets the Kulsinsk dissimilarity between two points.
     * @param x A point in space.
     * @param y A point in space.
     * @return The Kulsinsk dissimilarity between x and y.
     */
    public static double Kulsinsk(int[] x, int[] y){
        
        int tf = 0;
        int ft = 0;
        int tt = 0;
        
        for (int i = 0; i < x.length; i++) {
            if(x[i] == 1 && y[i] == 0) tf++;
            if(x[i] == 0 && y[i] == 1) ft++;
            if(x[i] == 1 && y[i] == 1) tt++;
        }
        
        return (tf + ft - tt + x.length) / (double)(ft + tf + x.length);
    }
    
    /**
     * Gets the Matching dissimilarity between two points.
     * @param x A point in space.
     * @param y A point in space.
     * @return The Matching dissimilarity between x and y.
     */
    public static double Matching(int[] x, int[] y){
        
        int tf = 0;
        int ft = 0;
        
        for (int i = 0; i < x.length; i++) {
            if(x[i] == 1 && y[i] == 0) tf++;
            if(x[i] == 0 && y[i] == 1) ft++;
        }
        
        return (tf + ft) / (double)(x.length);
    }
    
    /**
     * Gets the Rogers Tanimoto dissimilarity between two points.
     * @param x A point in space.
     * @param y A point in space.
     * @return The Rogers Tanimoto dissimilarity between x and y.
     */
    public static double RogersTanimoto(int[] x, int[] y){
        
        int tf = 0;
        int ft = 0;
        int tt = 0;
        int ff = 0;
        
        for (int i = 0; i < x.length; i++) {
            if(x[i] == 1 && y[i] == 0) tf++;
            if(x[i] == 0 && y[i] == 1) ft++;
            if(x[i] == 1 && y[i] == 1) tt++;
            if(x[i] == 0 && y[i] == 0) ff++;
        }
        
        int r = 2 * (tf + ft);
        return r / (double)(tt + ff + r);
    }
    
    /**
     * Gets the Russel Rao dissimilarity between two points.
     * @param x A point in space.
     * @param y A point in space.
     * @return The Russel Rao dissimilarity between x and y.
     */
    public static double RusselRao(int[] x, int[] y){
        
        int tt = 0;
        
        for (int i = 0; i < x.length; i++) {
            if(x[i] == 1 && y[i] == 1) tt++;
        }
        
        return (x.length - tt) / (double)(x.length);
    }
    
    /**
     * Gets the Sokal Michener dissimilarity between two points.
     * @param x A point in space.
     * @param y A point in space.
     * @return The Sokal Michener dissimilarity between x and y.
     */
    public static double SokalMichener(int[] x, int[] y){
        
        int tf = 0;
        int ft = 0;
        int tt = 0;
        int ff = 0;
        
        for (int i = 0; i < x.length; i++) {
            if(x[i] == 1 && y[i] == 0) tf++;
            if(x[i] == 0 && y[i] == 1) ft++;
            if(x[i] == 1 && y[i] == 1) tt++;
            if(x[i] == 0 && y[i] == 0) ff++;
        }
        
        int r = 2 * (tf + ft);
        return r / (double)(ff + tt + r);
    }
    
    /**
     * Gets the Sokal Sneath dissimilarity between two points.
     * @param x A point in space.
     * @param y A point in space.
     * @return The Sokal Sneath dissimilarity between x and y.
     */
    public static double SokalSneath(int[] x, int[] y){
        
        int tf = 0;
        int ft = 0;
        int tt = 0;
        
        for (int i = 0; i < x.length; i++) {
            if(x[i] == 1 && y[i] == 0) tf++;
            if(x[i] == 0 && y[i] == 1) ft++;
            if(x[i] == 1 && y[i] == 1) tt++;
        }
        
        int r = 2 * (tf + ft);
        return r / (double)(tt + r);
    }
    
    /**
     * Gets the Yule dissimilarity between two points.
     * @param x A point in space.
     * @param y A point in space.
     * @return The Yule dissimilarity between x and y.
     */
    public static double Yule(int[] x, int[] y){
        
        int tf = 0;
        int ft = 0;
        int tt = 0;
        int ff = 0;
        
        for (int i = 0; i < x.length; i++) {
            if(x[i] == 1 && y[i] == 0) tf++;
            if(x[i] == 0 && y[i] == 1) ft++;
            if(x[i] == 1 && y[i] == 1) tt++;
            if(x[i] == 0 && y[i] == 0) ff++;
        }
        
        double r = 2 * (tf + ft);
        return r / (tt + ff + r/2);
    }
}