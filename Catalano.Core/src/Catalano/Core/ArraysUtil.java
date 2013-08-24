// Catalano Core Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2013
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

package Catalano.Core;

/**
 * Array Utilities.
 * @author Diego Catalano
 */
public class ArraysUtil {

    /**
     * Don`t let anyone to instantiate this class.
     */
    private ArraysUtil() {}
    
    /**
     * 1-D Integer array to float array.
     * @param array Integer array.
     * @return Float array.
     */
    public static float[] toFloat(int[] array){
        float[] n = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            n[i] = (float)array[i];
        }
        return n;
    }
    
    /**
     * 2-D Integer array to float array.
     * @param array Integer array.
     * @return Float array.
     */
    public static float[][] toFloat(int[][] array){
        float[][] n = new float[array.length][array[0].length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                n[i][j] = (float)array[i][j];
            }
        }
        return n;
    }
    
    /**
     * 1-D Double array to float array.
     * @param array Double array.
     * @return Float array.
     */
    public static float[] toFloat(double[] array){
        float[] n = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            n[i] = (float)array[i];
        }
        return n;
    }
    
    /**
     * 2-D Double array to float array.
     * @param array Double array.
     * @return Float array.
     */
    public static float[][] toFloat(double[][] array){
        float[][] n = new float[array.length][array[0].length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                n[i][j] = (float)array[i][j];
            }
        }
        return n;
    }
    
    /**
     * 1-D Double array to integer array.
     * @param array Double array.
     * @return Integer array.
     */
    public static int[] toInt(double[] array){
        int[] n = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            n[i] = (int)array[i];
        }
        return n;
    }
    
    /**
     * 2-D Double array to integer array.
     * @param array Double array.
     * @return Integer array.
     */
    public static int[][] toInt(double[][] array){
        int[][] n = new int[array.length][array[0].length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                n[i][j] = (int)array[i][j];
            }
        }
        return n;
    }
    
    /**
     * 1-D Float array to integer array.
     * @param array Float array.
     * @return Integer array.
     */
    public static int[] toInt(float[] array){
        int[] n = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            n[i] = (int)array[i];
        }
        return n;
    }
    
    /**
     * 2-D Float array to integer array.
     * @param array Float array.
     * @return Integer array.
     */
    public static int[][] toInt(float[][] array){
        int[][] n = new int[array.length][array[0].length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                n[i][j] = (int)array[i][j];
            }
        }
        return n;
    }
    
    /**
     * 1-D Integer array to double array.
     * @param array Integer array.
     * @return Double array.
     */
    public static double[] toDouble(int[] array){
        double[] n = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            n[i] = (double)array[i];
        }
        return n;
    }
    
    /**
     * 2-D Integer array to double array.
     * @param array Integer array.
     * @return Double array.
     */
    public static double[][] toDouble(int[][] array){
        double[][] n = new double[array.length][array[0].length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                n[i][j] = (double)array[i][j];
            }
        }
        return n;
    }
    
    /**
     * 1-D Float array to double array.
     * @param array Float array.
     * @return Double array.
     */
    public static double[] toDouble(float[] array){
        double[] n = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            n[i] = (double)array[i];
        }
        return n;
    }
    
    /**
     * 2-D Float array to double array.
     * @param array Float array.
     * @return Double array.
     */
    public static double[][] toDouble(float[][] array){
        double[][] n = new double[array.length][array[0].length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                n[i][j] = (double)array[i][j];
            }
        }
        return n;
    }
}