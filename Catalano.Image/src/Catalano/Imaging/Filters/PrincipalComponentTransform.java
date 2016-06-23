// Catalano Imaging Library
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

package Catalano.Imaging.Filters;

import Catalano.Imaging.FastBitmap;
import Catalano.Math.Decompositions.SingularValueDecomposition;
import Catalano.Math.Matrix;
import Catalano.Statistics.Tools;

/**
 * Principal Component Transform.
 * @author Diego Catalano
 */
public class PrincipalComponentTransform {
    
    /**
     * Component.
     */
    public static enum Component{
        /**
         * Red channel.
         */
        Red,
        /**
         * Green channel.
         */
        Green,
        /**
         * Blue Channel.
         */
        Blue,
        /**
         * RGB.
         */
        RGB
    };
    
    private double[][] image;
    private int width;
    private int height;

    /**
     * Initialize a new instance of the PrincipalComponentTransform class.
     */
    public PrincipalComponentTransform() {}
    
    /**
     * Compute PCA in the image.
     * @param fastBitmap Image to be processed.
     */
    public void Compute(FastBitmap fastBitmap){
        
        if(fastBitmap.isRGB()){
            this.width = fastBitmap.getWidth();
            this.height = fastBitmap.getHeight();

            //Transform the image in vectors
            int size = fastBitmap.getSize();
            this.image = new double[size][3];
            for (int i = 0; i < size; i++) {
                image[i][0] = fastBitmap.getRed(i);
                image[i][1] = fastBitmap.getGreen(i);
                image[i][2] = fastBitmap.getBlue(i);
            }

            //Run the PCA
            double[] means = getMeans(image);
            
            //Center the data
            image = Center(image, means);
            
            //Find the eigen vectors.
            SingularValueDecomposition svd = new SingularValueDecomposition(image, false, true);
            image = Matrix.Multiply(image, svd.getV());
            
        }
        else{
            throw new IllegalArgumentException("Principal Component Transform only works in RGB images.");
        }
    }
    
    private double[] getMeans(final double[][] matrix){
        double[] means = new double[matrix[0].length];
        for (int i = 0; i < matrix[0].length; i++) {
            double[] col = Matrix.getColumn(matrix, i);
            means[i] = Tools.Mean(col);
        }
        return means;
    }
    
    private double[][] Center(double[][] matrix, double[] means){
        double[][] m = new double[matrix.length][matrix[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                m[i][j] = matrix[i][j] - means[j];
            }
        }
        return m;
    }
    
    /**
     * Extract principal component from the image.
     * @param component Component.
     * @return Principal component of the color.
     */
    public FastBitmap Extract(Component component){
        
        if(component == Component.Red){
            //Find the min and max values
            double min = Matrix.Min(Matrix.getColumn(image, 0));
            double max = Matrix.Max(Matrix.getColumn(image, 0));
            
             FastBitmap fb = new FastBitmap(width, height, FastBitmap.ColorSpace.Grayscale);
             
             int size = width * height;
             for (int i = 0; i < size; i++) {
                int c = (int)Catalano.Math.Tools.Scale(min, max, 0, 255, image[i][0]);
                fb.setGray(i, c);
            }
             
             return fb;
             
        }
        else if(component == Component.Green){
            //Find the min and max values
            double min = Matrix.Min(Matrix.getColumn(image, 1));
            double max = Matrix.Max(Matrix.getColumn(image, 1));
            
             FastBitmap fb = new FastBitmap(width, height, FastBitmap.ColorSpace.Grayscale);
             
             int size = width * height;
             for (int i = 0; i < size; i++) {
                int c = (int)Catalano.Math.Tools.Scale(min, max, 0, 255, image[i][1]);
                fb.setGray(i, c);
            }
             
             return fb;
        }
        else if (component == Component.Blue){
            //Find the min and max values
            double min = Matrix.Min(Matrix.getColumn(image, 2));
            double max = Matrix.Max(Matrix.getColumn(image, 2));
            
             FastBitmap fb = new FastBitmap(width, height, FastBitmap.ColorSpace.Grayscale);
             
             int size = width * height;
             for (int i = 0; i < size; i++) {
                int c = (int)Catalano.Math.Tools.Scale(min, max, 0, 255, image[i][2]);
                fb.setGray(i, c);
            }
             
             return fb;
        }
        else{
            //Find the min and max values
            double minRed = Matrix.Min(Matrix.getColumn(image, 0));
            double maxRed = Matrix.Max(Matrix.getColumn(image, 0));
            
            double minGreen = Matrix.Min(Matrix.getColumn(image, 1));
            double maxGreen = Matrix.Max(Matrix.getColumn(image, 1));
            
            double minBlue = Matrix.Min(Matrix.getColumn(image, 2));
            double maxBlue = Matrix.Max(Matrix.getColumn(image, 2));
            
             FastBitmap fb = new FastBitmap(width, height, FastBitmap.ColorSpace.RGB);
             
             int size = width * height;
             for (int i = 0; i < size; i++) {
                int r = (int)Catalano.Math.Tools.Scale(minRed, maxRed, 0, 255, image[i][0]);
                int g = (int)Catalano.Math.Tools.Scale(minGreen, maxGreen, 0, 255, image[i][1]);
                int b = (int)Catalano.Math.Tools.Scale(minBlue, maxBlue, 0, 255, image[i][2]);
                
                fb.setRGB(i, r,g,b);
            }
             return fb;
        }
    }
}