// Catalano Imaging Library
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

package Catalano.Imaging.Tools;

import Catalano.Imaging.FastBitmap;

/**
 * Weber Local Descriptor. (WLD)
 * Paper: http://www.jdl.ac.cn/user/sgshan/pub/CVPR08_JChen_Print.pdf
 * @author Diego Catalano
 */
public class WeberLocalDescriptor {
    
    private final int beta = 5;
    private final double epsilon = 0.0000001D;
    
    private int alpha = 3;
    private double[][] differentialExcitation;
    private double[][] gradientOrientation;

    /**
     * Get Alpha.
     * @return Alpha value.
     */
    public int getAlpha() {
        return alpha;
    }

    /**
     * Set Alpha.
     * @param alpha Alpha value.
     */
    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    /**
     * Get Gradient orientation.
     * @return Gradient orientation.
     */
    public double[][] getGradientOrientation() {
        return gradientOrientation;
    }
    
    //Filter
    private final int[][] kernel = {
        {1, 1, 1},
        {1, -8, 1},
        {1, 1, 1}
    };

    /**
     * Initialize a new instance of the WeberLocalDescriptor class.
     */
    public WeberLocalDescriptor() {}
    
    /**
     * Compute Weber local descriptor.
     * @param fastBitmap Image to be processed.
     * @return Differential Excitation.
     */
    public double[][] Compute(FastBitmap fastBitmap){
        
        if (fastBitmap.isGrayscale()){
            
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();
            
            differentialExcitation = new double[height - 1][width - 1];
            gradientOrientation = new double[height - 1][width - 1];
            
            for (int i = 1; i < height - 1; i++) {
                for (int j = 1; j < width - 1; j++) {
                    
                    //Step 1: Compute differential excitation
                    double v00 =
                              kernel[0][0] * fastBitmap.getGray(i - 1, j - 1)
                            + kernel[0][1] * fastBitmap.getGray(i - 1, j)
                            + kernel[0][2] * fastBitmap.getGray(i - 1, j + 1)
                            + kernel[1][0] * fastBitmap.getGray(i, j - 1)
                            + kernel[1][1] * fastBitmap.getGray(i, j)
                            + kernel[1][2] * fastBitmap.getGray(i, j + 1)
                            + kernel[2][0] * fastBitmap.getGray(i + 1, j - 1)
                            + kernel[2][1] * fastBitmap.getGray(i + 1, j)
                            + kernel[2][2] * fastBitmap.getGray(i + 1, j + 1);
                    
                    double v01 = fastBitmap.getGray(i, j) + beta;
                    
                    if (v01 != 0){
                        differentialExcitation[i - 1][j - 1] = Math.atan(alpha * v00 / v01);
                    }
                    else{
                        differentialExcitation[i - 1][j - 1] = 0.1;
                    }
                    
                    //Step 2: Compute gradient orientation
                    int n1 = fastBitmap.getGray(i - 1, j);
                    int n3 = fastBitmap.getGray(i, j + 1);
                    int n5 = fastBitmap.getGray(i + 1, j);
                    int n7 = fastBitmap.getGray(i, j - 1);
                    
                    if (Math.abs(n7-n3) < epsilon){
                        gradientOrientation[i - 1][j - 1] = 0;
                    }
                    else{
                        double v10 = n5 - n1;
                        double v11 = n7 - n3;
                        gradientOrientation[i - 1][j - 1] = Math.atan(v10/v11) * 180 / Math.PI;
                        
                        if (v11 > epsilon && v10 > epsilon){
                            gradientOrientation[i - 1][j - 1] += 0; 
                        }
                        else if (v11 < -epsilon && v10 > epsilon){
                            gradientOrientation[i - 1][j - 1] += 180; 
                        }
                        else if (v11 < -epsilon && v10 < -epsilon){
                            gradientOrientation[i - 1][j - 1] += 180; 
                        }
                        else if (v11 > epsilon && v10 < -epsilon){
                            gradientOrientation[i - 1][j - 1] += 360; 
                        }
                    }
                }
            }
            return differentialExcitation;
        }
        else{
            throw new IllegalArgumentException("Weber Local Descriptor only works in grayscale images.");
        }
    }
    
    /**
     * Display the diffential excitation.
     * @return Differential excitation.
     */
    public FastBitmap toFastBitmap(){
        
        FastBitmap fb = new FastBitmap(differentialExcitation[0].length, differentialExcitation.length, FastBitmap.ColorSpace.Grayscale);
        
        for (int i = 0; i < differentialExcitation.length; i++) {
            for (int j = 0; j < differentialExcitation[0].length; j++) {
                fb.setGray(i, j, (int)(255 *  (differentialExcitation[i][j] - (-Math.PI/2))/((Math.PI/2)-(-Math.PI/2))));
            }
        }
        
        return fb;
        
    }
}