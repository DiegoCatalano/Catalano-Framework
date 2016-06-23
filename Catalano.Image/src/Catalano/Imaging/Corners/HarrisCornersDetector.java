// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © César Souza, 2009-2013
// cesarsouza at gmail.com
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

package Catalano.Imaging.Corners;

import Catalano.Core.ArraysUtil;
import Catalano.Core.IntPoint;
import Catalano.Imaging.FastBitmap;
import Catalano.Math.Constants;
import java.util.ArrayList;
import java.util.List;

/**
 * Harris Corners Detector.
 * @author Diego Catalano
 */
public class HarrisCornersDetector implements ICornersDetector{
    
    public enum HarrisCornerMeasure {Harris, Noble};
    HarrisCornerMeasure algo;
    
    // Harris parameters
    private HarrisCornerMeasure measure = HarrisCornerMeasure.Harris;
    private float k = 0.04f;
    private float threshold = 20000f;

    // Non-maximum suppression parameters
    private int suppression = 3;

    // Gaussian smoothing parameters
    private double sigma = 1.2;
    private float[] kernel;
    private int size = 7;

    /**
     * Gets the measure to use when detecting corners.
     * @return Measure.
     */
    public HarrisCornerMeasure getMeasure() {
        return measure;
    }

    /**
     * Sets the measure to use when detecting corners.
     * @param measure Measure.
     */
    public void setMeasure(HarrisCornerMeasure measure) {
        this.measure = measure;
    }

    /**
     * Gets Non-maximum suppression window radius. Default value is 3.
     * @return Non-maximum suppression window radius.
     */
    public int getSuppression() {
        return suppression;
    }

    /**
     * Sets Non-maximum suppression window radius. Default value is 3.
     * @param suppression Non-maximum suppression window radius.
     */
    public void setSuppression(int suppression) {
        this.suppression = suppression;
    }

    /**
     * Gets Harris parameter k. Default value is 0.04.
     * @return Harris parameter.
     */
    public float getK() {
        return k;
    }

    /**
     * Sets Harris parameter k. Default value is 0.04.
     * @param k Harris parameter.
     */
    public void setK(float k) {
        this.k = k;
    }

    /**
     * Get Harris threshold. Default value is 20000.
     * @return Harris threshold.
     */
    public float getThreshold() {
        return threshold;
    }

    /**
     * Set Harris threshold. Default value is 20000.
     * @param threshold Harris threshold.
     */
    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }

    /**
     * Get Gaussian smoothing sigma. Default value is 1.2.
     * @return Gaussian smoothing.
     */
    public double getSigma() {
        return sigma;
    }

    /**
     * Set Gaussian smoothing sigma. Default value is 1.2.
     * @param sigma Gaussian smoothing.
     */
    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    /**
     * Initializes a new instance of the HarrisCornersDetector class.
     */
    public HarrisCornersDetector() {
        init(HarrisCornerMeasure.Harris, k, threshold, sigma, suppression, size);
    }
    
    /**
     * Initializes a new instance of the HarrisCornersDetector class.
     * @param k Harris parameter.
     */
    public HarrisCornersDetector(float k){
        init(HarrisCornerMeasure.Harris, k, threshold, sigma, suppression, size);
    }
    
    /**
     * Initializes a new instance of the HarrisCornersDetector class.
     * @param k Harris parameter.
     * @param threshold Harris threshold.
     */
    public HarrisCornersDetector(float k, float threshold){
        init(HarrisCornerMeasure.Harris, k, threshold, sigma, suppression, size);
    }
    
    /**
     * Initializes a new instance of the HarrisCornersDetector class.
     * @param k Harris parameter.
     * @param threshold Harris threshold.
     * @param sigma Gaussian smoothing.
     */
    public HarrisCornersDetector(float k, float threshold, double sigma){
        init(HarrisCornerMeasure.Harris, k, threshold, sigma, suppression, size);
    }
    
    /**
     * Initializes a new instance of the HarrisCornersDetector class.
     * @param k Harris parameter.
     * @param threshold Harris threshold.
     * @param sigma Gaussian smoothing.
     * @param suppression Non-maximum suppression window radius.
     */
    public HarrisCornersDetector(float k, float threshold, double sigma, int suppression){
        init(HarrisCornerMeasure.Harris, k, threshold, sigma, suppression, size);
    }
    
    /**
     * Initializes a new instance of the HarrisCornersDetector class.
     * @param measure Measure to use when detecting corners.
     * @param threshold Harris threshold.
     * @param sigma Gaussian smoothing.
     * @param suppression Non-maximum suppression window radius.
     */
    public HarrisCornersDetector(HarrisCornerMeasure measure, float threshold, double sigma, int suppression){
        init(measure, k, threshold, sigma, suppression, size);
    }
    
    /**
     * Initializes a new instance of the HarrisCornersDetector class.
     * @param measure Measure to use when detecting corners.
     * @param threshold Harris threshold.
     * @param sigma Gaussian smoothing.
     */
    public HarrisCornersDetector(HarrisCornerMeasure measure, float threshold, double sigma){
        init(measure, k, threshold, sigma, suppression, size);
    }
    
    /**
     * Initializes a new instance of the HarrisCornersDetector class.
     * @param measure Measure to use when detecting corners.
     * @param threshold Harris threshold.
     */
    public HarrisCornersDetector(HarrisCornerMeasure measure, float threshold){
        init(measure, k, threshold, sigma, suppression, size);
    }
    
    /**
     * Initializes a new instance of the HarrisCornersDetector class.
     * @param measure Measure to use when detecting corners.
     */
    public HarrisCornersDetector(HarrisCornerMeasure measure){
        init(measure, k, threshold, sigma, suppression, size);
    }
    
    private void init(HarrisCornerMeasure measure, float k, float threshold, double sigma, int suppression, int size){
        this.measure = measure;
        this.threshold = threshold;
        this.k = k;
        this.suppression = suppression;
        this.sigma = sigma;
        this.size = size;
        
        createGaussian();
    }
    
    private void createGaussian(){
        double[] kernel = new Catalano.Math.Functions.Gaussian(sigma).Kernel1D(size);
        this.kernel = ArraysUtil.toFloat(kernel);
    }
    

    @Override
    public List<IntPoint> ProcessImage(FastBitmap fastBitmap) {
        
        FastBitmap gray;
        
        if (fastBitmap.isGrayscale())
        {
            gray = fastBitmap;
        }
        else{
            gray = new FastBitmap(fastBitmap);
            gray.toGrayscale();
        }
        
        int width = gray.getWidth();
        int height = gray.getHeight();
        
        // 1. Calculate partial differences
        float[][] diffx = new float[height][width];
        float[][] diffy = new float[height][width];
        float[][] diffxy = new float[height][width];
        
        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                int p1 = gray.getGray(i - 1, j + 1);
                int p2 = gray.getGray(i, j + 1);
                int p3 = gray.getGray(i + 1, j + 1);
                int p4 = gray.getGray(i - 1, j - 1);
                int p5 = gray.getGray(i, j - 1);
                int p6 = gray.getGray(i + 1, j - 1);
                int p7 = gray.getGray(i + 1, j);
                int p8 = gray.getGray(i - 1, j);
                
                float h = ((p1 + p2 + p3) - (p4 + p5 + p6)) * 0.166666667f;
                float v = ((p6 + p7 + p3) - (p4 + p8 + p1)) * 0.166666667f;
                
                diffx[i][j] = h * h;
                diffy[i][j] = v * v;
                diffxy[i][j] = h * v;
            }
        }
        
        // 2. Smooth the diff images
        if (sigma > 0.0)
        {
            float[][] temp = new float[height][width];

            // Convolve with Gaussian kernel
            convolve(diffx, temp, kernel);
            convolve(diffy, temp, kernel);
            convolve(diffxy, temp, kernel);
        }
        
        // 3. Compute Harris Corner Response Map
        float[][] map = new float[height][width];
        
        float M, A, B, C;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                A = diffx[i][j];
                B = diffy[i][j];
                C = diffxy[i][j];
                if (measure == HarrisCornerMeasure.Harris){
                    M = (A * B - C * C) - (k * ((A + B) * (A + B)));
                }
                else{
                    M = (A * B - C * C) / (A + B + Constants.SingleEpsilon);
                }
                
                if (M > threshold)
                    map[i][j] = M;
                
            }
        }
        
        // 4. Suppress non-maximum points
        ArrayList<IntPoint> cornersList = new ArrayList<IntPoint>();
        
        for (int x = suppression, maxX = height - suppression; x < maxX; x++) {
            for (int y = suppression, maxY = width - suppression; y < maxY; y++) {
                float currentValue = map[x][y];
                
                // for each windows' row
                for (int i = -suppression; (currentValue != 0) && (i <= suppression); i++) {
                    
                    // for each windows' pixel
                    for (int j = -suppression; j <= suppression; j++) {
                        if (map[x + i][y + j] > currentValue){
                            currentValue = 0;
                            break;
                        }
                    }
                }
                
                // check if this point is really interesting
                if (currentValue != 0){
                    cornersList.add(new IntPoint(x, y));
                }
            }
        }
        
        return cornersList;
    }
    
    /**
     * Convolution with decomposed 1D kernel.
     * @param image Original image.
     * @param temp Temporary image.
     * @param kernel Kernel.
     */
    private void convolve(float[][] image, float[][] temp, float[] kernel){
        int width = image[0].length;
        int height = image.length;
        int radius = kernel.length / 2;
        
        for (int x = 0; x < height; x++){
            for (int y = radius; y < width - radius; y++){
                float v = 0;
                for (int k = 0; k < kernel.length; k++){
                    v += image[x][y + k - radius] * kernel[k];
                }
                temp[x][y] = v;
            }
        }


        for (int y = 0; y < width; y++)
        {
            for (int x = radius; x < height - radius; x++)
            {
                float v = 0;
                for (int k = 0; k < kernel.length; k++){
                    v += temp[x + k - radius][y] * kernel[k];
                }
                
                image[x][y] = v;
            }
        }
    }
}