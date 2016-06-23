// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Max Bügler, 2010-2013
// max at maxbuegler.eu
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
import Catalano.Imaging.IApplyInPlace;
import Catalano.Math.Functions.Gabor;
import Catalano.Math.Functions.Gabor.Config;

/**
 * Gabor Filter.
 * 
 * <para> In image processing, a Gabor filter, named after Dennis Gabor, is a linear filter used for edge detection.
 * Frequency and orientation representations of Gabor filters are similar to those of the human visual system,
 * and they have been found to be particularly appropriate for texture representation and discrimination.
 * In the spatial domain, a 2D Gabor filter is a Gaussian kernel function modulated by a sinusoidal plane wave. </para>
 * 
 * @see http://en.wikipedia.org/wiki/Gabor_filter
 * @author Diego Catalano
 */
public class GaborFilter implements IApplyInPlace{
    
    // Size of kernel
    private int size = 3;
    
    // Wavelength
    private double lambda = 4.0;
    
    // Orientation
    private double theta = 0.6;
    
    // Phase offset
    private double psi = 1.0;
    
    // Gaussian variance
    private double sigma = 2.0;
    
    // Aspect ratio
    private double gamma = 0.3;
    private Gabor.Config config = Gabor.Config.Imaginary;
    private boolean signed = false;

    /**
     * Get size of Gabor kernel.
     * @return Size.
     */
    public int getSize() {
        return size;
    }

    /**
     * Set size of Gabor kernel.
     * @param size Size.
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Get Wavelength (Lambda).
     * @return Lambda value.
     */
    public double getWavelength() {
        return lambda;
    }

    /**
     * Set Wavelength (Lambda).
     * @param lambda Lambda value.
     */
    public void setWavelength(double lambda) {
        this.lambda = lambda;
    }

    /**
     * Get Orientation (Theta).
     * @return Theta value.
     */
    public double getOrientation() {
        return theta;
    }

    /**
     * Set Orientation (Theta).
     * @param theta Theta value.
     */
    public void setOrientation(double theta) {
        this.theta = theta;
    }

    /**
     * Get Phase offset (Psi).
     * @return Phi value.
     */
    public double getPhaseOffset() {
        return psi;
    }

    /**
     * Set Phase offset (Psi).
     * @param phi 
     */
    public void setPhaseOffset(double psi) {
        this.psi = psi;
    }

    /**
     * Get Gaussian variance (Sigma).
     * @return Sigma value.
     */
    public double getGaussianVar() {
        return sigma;
    }

    /**
     * Set Gaussian variance (Sigma).
     * @param sigma Sigma value.
     */
    public void setGaussianVar(double sigma) {
        this.sigma = sigma;
    }

    /**
     * Get Aspect ratio (Gamma).
     * @return gamma.
     */
    public double getAspectRatio() {
        return gamma;
    }

    /**
     * Set Aspect ratio (Gamma).
     * @param gamma Gamma.
     */
    public void setAspectRatio(double gamma) {
        this.gamma = gamma;
    }

    /**
     * Get Gabor function configuration.
     * @return Configuration.
     */
    public Config getConfig() {
        return config;
    }

    /**
     * Set Gabor function configuration.
     * @param config Configuration.
     */
    public void setConfig(Config config) {
        this.config = config;
    }

    /**
     * Verify if the image is signed.
     * @return True if the image is signed, otherwise false.
     */
    public boolean isSigned() {
        return signed;
    }

    /**
     * Set signed image.
     * If true, image will be converted to RGB.
     * @param signed True for signed image, false for grayscale image.
     */
    public void setSigned(boolean signed) {
        this.signed = signed;
    }

    /**
     * Initializes a new instance of the GaborFilterFinal class.
     */
    public GaborFilter() {}

    /**
     * Initializes a new instance of the GaborFilterFinal class.
     * @param wavelength Wavelength.
     */
    public GaborFilter(double wavelength) {
        this.lambda = wavelength;
    }

    /**
     * Initializes a new instance of the GaborFilterFinal class.
     * @param wavelength Wavelength.
     * @param orientation Orientation.
     */
    public GaborFilter(double wavelength, double orientation) {
        this.lambda = wavelength;
        this.theta = orientation;
    }

    /**
     * Initializes a new instance of the GaborFilterFinal class.
     * @param wavelength Wavelength.
     * @param orientation Orientation.
     * @param phaseOffset Phase offset.
     */
    public GaborFilter(double wavelength, double orientation, double phaseOffset) {
        this.lambda = wavelength;
        this.theta = orientation;
        this.psi = phaseOffset;
    }

    /**
     * Initializes a new instance of the GaborFilterFinal class.
     * @param wavelength Wavelength.
     * @param orientation Orientation.
     * @param phaseOffset Phase offset.
     * @param gaussianVar Gaussian variance.
     */
    public GaborFilter(double wavelength, double orientation, double phaseOffset, double gaussianVar) {
        this.lambda = wavelength;
        this.theta = orientation;
        this.psi = phaseOffset;
        this.sigma = gaussianVar;
    }

    /**
     * Initializes a new instance of the GaborFilterFinal class.
     * @param wavelength Wavelength.
     * @param orientation Orientation.
     * @param phaseOffset Phase offset.
     * @param gaussianVar Gaussian variance.
     * @param aspectRatio Aspect ratio.
     */
    public GaborFilter(double wavelength, double orientation, double phaseOffset, double gaussianVar, double aspectRatio) {
        this.lambda = wavelength;
        this.theta = orientation;
        this.psi = phaseOffset;
        this.sigma = gaussianVar;
        this.gamma = aspectRatio;
    }
    
    /**
     * Initializes a new instance of the GaborFilterFinal class.
     * @param wavelength Wavelength.
     * @param orientation Orientation.
     * @param phaseOffset Phase offset.
     * @param gaussianVar Gaussian variance.
     * @param aspectRatio Aspect ratio.
     * @param config Gabor configuration.
     */
    public GaborFilter(double wavelength, double orientation, double phaseOffset, double gaussianVar, double aspectRatio, Gabor.Config config) {
        this.lambda = wavelength;
        this.theta = orientation;
        this.psi = phaseOffset;
        this.sigma = gaussianVar;
        this.gamma = aspectRatio;
        this.config = config;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        double[][] gaborKernel;
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        
        if (fastBitmap.isGrayscale()){
            
            gaborKernel = Gabor.Kernel2D(size, lambda, theta, psi, sigma, gamma, config);
            int[][] gaborResponse = applyGabor(fastBitmap, gaborKernel);
            int maxG = Integer.MIN_VALUE;
            int minG = Integer.MAX_VALUE;
            
            if (isSigned()){
                fastBitmap.toRGB();

                // Gets max and min gray value.
                for (int i = 0; i < gaborResponse.length; i++) {
                    for (int j = 0; j < gaborResponse[0].length; j++) {
                        int gray = gaborResponse[i][j];
                        if (gray > maxG && gray > 0) maxG = gray;
                        if (gray < minG && gray < 0) minG = gray;
                    }
                }

                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        
                        int g = (int)Math.round((255*(double)(gaborResponse[i][j] / (double)maxG)));
                        if (gaborResponse[i][j] < 0){
                            g = (int)Math.round((255*(double)(gaborResponse[i][j] / (double)minG)));
                        }
                        if (gaborResponse[i][j] > 0)
                            fastBitmap.setRGB(i, j, g, 0, 0);
                        else
                            fastBitmap.setRGB(i, j, 0, 0, g);
                    }
                }
            }
            else{

                // Gets max and min gray value.
                for (int i = 0; i < gaborResponse.length; i++) {
                    for (int j = 0; j < gaborResponse[0].length; j++) {
                        int gray = gaborResponse[i][j];
                        if (gray > maxG) maxG = gray;
                        if (gray < minG) minG = gray;
                    }
                }

                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        int g = (int)Math.round((255*(double)(gaborResponse[i][j] - minG))/(maxG - minG));
                        fastBitmap.setGray(i, j, g);
                    }
                }
            }
        }
        else{
            try {
                throw new IllegalArgumentException("Gabor filter only works with grayscale images.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Apply Gabor 
     * @param fastBitmap Image to be processed.
     * @param gaborKernel Gabor kernel.
     * @return Gabor response.
     */
    private int[][] applyGabor(FastBitmap fastBitmap, double[][] gaborKernel){
        
        int height = fastBitmap.getHeight();
        int width = fastBitmap.getWidth();
        
        int xmax=(int)Math.floor(gaborKernel.length / 2.0);
        int ymax=(int)Math.floor(gaborKernel[0].length / 2.0);
        int[][] gaborResponse = new int[height][width];
        
        for (int x=0; x < height;x++){
            for (int y=0; y < width;y++){
                double sum = 0;
                for (int xf = -xmax; xf <= xmax; xf++){
                    for (int yf = -ymax; yf <= ymax; yf++){
                        if (x-xf >= 0 && x-xf < height && y-yf >= 0 && y-yf < width){
                            int value = fastBitmap.getGray(x - xf, y - yf);
                            sum += gaborKernel[xf + xmax][yf + ymax] * value;
                        }
                    }
                }
                gaborResponse[x][y] = (int)Math.round(sum);
            }
        }
        return gaborResponse;
    }
}