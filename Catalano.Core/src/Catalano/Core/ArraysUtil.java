// Catalano Core Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2015
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
     * Convert any number class to array of integer.
     * @param <T> Type.
     * @param array Array.
     * @return Integer array.
     */
    private static <T extends Number> int[] asArray(final T... array) {
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
    private static void Shuffle(double[] array){
        Random random = new Random();
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
    private static void Shuffle(int[] array){
        Random random = new Random();
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
    private static void Shuffle(float[] array){
        Random random = new Random();
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