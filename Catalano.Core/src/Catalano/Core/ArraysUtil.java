// Catalano Core Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
//
// Contains some methods for arrays, distributed
// under the BSD license. The original license terms are given below:
//
//   Copyright © Albert Strasheim, 2008
//
//   Redistribution and use in source and binary forms, with or without
//   modification, are permitted provided that the following conditions are
//   met:
//
//       * Redistributions of source code must retain the above copyright
//         notice, this list of conditions and the following disclaimer.
//       * Redistributions in binary form must reproduce the above copyright
//         notice, this list of conditions and the following disclaimer in
//         the documentation and/or other materials provided with the distribution
//  
//   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
//   AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
//   IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
//   ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
//   LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
//   CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
//   SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
//   INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
//   CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
//   ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
//   POSSIBILITY OF SUCH DAMAGE.
//

package Catalano.Core;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

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
     * Returns the indices that would sort an array.
     * @param array Array.
     * @param ascending Ascending order.
     * @return Array of indices.
     */
    public static int[] Argsort(final double[] array, final boolean ascending) {
        Integer[] indexes = new Integer[array.length];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = i;
        }
        Arrays.sort(indexes, new Comparator<Integer>() {
            @Override
            public int compare(final Integer i1, final Integer i2) {
                return (ascending ? 1 : -1) * Double.compare(array[i1], array[i2]);
            }
        });
        return asArray(indexes);
    }
    
    /**
     * Returns the indices that would sort an array.
     * @param array Array.
     * @param ascending Ascending order.
     * @return Array of indices.
     */
    public static int[] Argsort(final int[] array, final boolean ascending) {
        Integer[] indexes = new Integer[array.length];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = i;
        }
        Arrays.sort(indexes, new Comparator<Integer>() {
            @Override
            public int compare(final Integer i1, final Integer i2) {
                return (ascending ? 1 : -1) * Integer.compare(array[i1], array[i2]);
            }
        });
        return asArray(indexes);
    }
    
    /**
     * Returns the indices that would sort an array.
     * @param array Array.
     * @param ascending Ascending order.
     * @return Array of indices.
     */
    public static int[] Argsort(final float[] array, final boolean ascending) {
        Integer[] indexes = new Integer[array.length];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = i;
        }
        Arrays.sort(indexes, new Comparator<Integer>() {
            @Override
            public int compare(final Integer i1, final Integer i2) {
                return (ascending ? 1 : -1) * Float.compare(array[i1], array[i2]);
            }
        });
        return asArray(indexes);
    }
    
    /**
     * Concatenate the arrays.
     * @param array First array.
     * @param array2 Second array.
     * @return Concatenate between first and second array.
     */
    public static int[] Concatenate(int[] array, int[] array2){
        int[] all = new int[array.length + array2.length];
        int idx = 0;
        
        //First array
        for (int i = 0; i < array.length; i++)
            all[idx++] = array[i];
        
        //Second array
        for (int i = 0; i < array2.length; i++)
            all[idx++] = array2[i];
        
        return all;
    }
    
    /**
     * Concatenate the arrays.
     * @param array First array.
     * @param array2 Second array.
     * @return Concatenate between first and second array.
     */
    public static double[] Concatenate(double[] array, double[] array2){
        double[] all = new double[array.length + array2.length];
        int idx = 0;
        
        //First array
        for (int i = 0; i < array.length; i++)
            all[idx++] = array[i];
        
        //Second array
        for (int i = 0; i < array2.length; i++)
            all[idx++] = array2[i];
        
        return all;
    }
    
    /**
     * Concatenate the arrays.
     * @param array First array.
     * @param array2 Second array.
     * @return Concatenate between first and second array.
     */
    public static float[] Concatenate(float[] array, float[] array2){
        float[] all = new float[array.length + array2.length];
        int idx = 0;
        
        //First array
        for (int i = 0; i < array.length; i++)
            all[idx++] = array[i];
        
        //Second array
        for (int i = 0; i < array2.length; i++)
            all[idx++] = array2[i];
        
        return all;
    }
    
    /**
     * Concatenate all the arrays in the list into a vector.
     * @param arrays List of arrays.
     * @return Vector.
     */
    public static int[] ConcatenateInt(List<int[]> arrays){
        
        int size = 0;
        for (int i = 0; i < arrays.size(); i++) {
            size += arrays.get(i).length;
        }
        
        int[] all = new int[size];
        int idx = 0;
        
        for (int i = 0; i < arrays.size(); i++) {
            int[] v = arrays.get(i);
            for (int j = 0; j < v.length; j++) {
                all[idx++] = v[i];
            }
        }
        
        return all;
    }
    
    /**
     * Concatenate all the arrays in the list into a vector.
     * @param arrays List of arrays.
     * @return Vector.
     */
    public static double[] ConcatenateDouble(List<double[]> arrays){
        
        int size = 0;
        for (int i = 0; i < arrays.size(); i++) {
            size += arrays.get(i).length;
        }
        
        double[] all = new double[size];
        int idx = 0;
        
        for (int i = 0; i < arrays.size(); i++) {
            double[] v = arrays.get(i);
            for (int j = 0; j < v.length; j++) {
                all[idx++] = v[i];
            }
        }
        
        return all;
    }
    
    /**
     * Concatenate all the arrays in the list into a vector.
     * @param arrays List of arrays.
     * @return Vector.
     */
    public static float[] ConcatenateFloat(List<float[]> arrays){
        
        int size = 0;
        for (int i = 0; i < arrays.size(); i++) {
            size += arrays.get(i).length;
        }
        
        float[] all = new float[size];
        int idx = 0;
        
        for (int i = 0; i < arrays.size(); i++) {
            float[] v = arrays.get(i);
            for (int j = 0; j < v.length; j++) {
                all[idx++] = v[i];
            }
        }
        
        return all;
    }
    
    /**
     * Convert any number class to array of integer.
     * @param <T> Type.
     * @param array Array.
     * @return Integer array.
     */
    public static <T extends Number> int[] asArray(final T... array) {
        int[] b = new int[array.length];
        for (int i = 0; i < b.length; i++) {
            b[i] = array[i].intValue();
        }
        return b;
    }
    
    /**
     * Shuffle an array.
     * @param array Array.
     */
    public static void Shuffle(double[] array){
        Shuffle(array, 0);
    }
    
    /**
     * Shuffle an array.
     * @param array Array.
     * @param seed Random seed.
     */
    public static void Shuffle(double[] array, long seed){
        Random random = new Random();
        if(seed != 0) random.setSeed(seed);
        
        for (int i = array.length - 1; i > 0; i--)
        {
            int index = random.nextInt(i + 1);
            double temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
    
    /**
     * Shuffle an array.
     * @param array Array.
     */
    public static void Shuffle(int[] array){
        Shuffle(array, 0);
    }
    
    /**
     * Shuffle an array.
     * @param array Array.
     * @param seed Seed of the random.
     */
    public static void Shuffle(int[] array, long seed){
        
        Random random = new Random();
        if(seed != 0) random.setSeed(seed);
        
        for (int i = array.length - 1; i > 0; i--)
        {
            int index = random.nextInt(i + 1);
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
    
    /**
     * Shuffle an array.
     * @param array Array.
     */
    public static void Shuflle(float[] array){
        Shuffle(array, 0);
    }
    
    /**
     * Shuffle an array.
     * @param array Array.
     * @param seed Random seed.
     */
    public static void Shuffle(float[] array, long seed){
        Random random = new Random();
        if(seed != 0) random.setSeed(seed);
        
        for (int i = array.length - 1; i > 0; i--)
        {
            int index = random.nextInt(i + 1);
            float temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
    
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