// Catalano Math Library
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

package Catalano.Math.Functions;

import Catalano.Math.ComplexNumber;

/**
 * Gabor function.
 * @see http://en.wikipedia.org/wiki/Gabor_filter
 * @author Diego Catalano
 */
public final class Gabor {
    
    /**
     * Gabor configuration.
     */
    public enum Config {

        /**
         * Creates kernel based in real part.
         */
        Real,
        /**
         * Creates kernel based in imaginary part.
         */
        Imaginary,
        /**
         * Creates kernel based in magnitude part.
         */
        Magnitude,
        /**
         * Creates kernel based in squared magnitude part.
         */
        SquaredMagnitude
    };

    /**
     * Don't let anyone instantiate this class.
     */
    private Gabor() {}
    
    /**
     * 1-D Gabor function.
     * @param x Value.
     * @param mean Mean.
     * @param amplitude Amplitude.
     * @param position Position.
     * @param width Width.
     * @param phase Phase.
     * @param frequency Frequency.
     * @return Gabor response.
     */
    public static double Function1D(double x, double mean, double amplitude, double position, double width, double phase, double frequency){
        double envelope = mean + amplitude * Math.exp(-Math.pow((x - position),2) / Math.pow((2 * width),2));
        double carry = Math.cos(2 * Math.PI * frequency * (x - position) + phase);
        return envelope * carry;
    }
    
    /**
     * 2-D Complex Gabor function.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @param wavelength Wavelength.
     * @param orientation Orientation.
     * @param phaseOffset Phase offset.
     * @param gaussVariance Gaussian variance.
     * @param aspectRatio Aspect ratio.
     * @return Gabor response.
     */
    public static ComplexNumber Function2D(int x, int y, double wavelength, double orientation, double phaseOffset, double gaussVariance, double aspectRatio){
        
        double X = x * Math.cos(orientation) + y * Math.sin(orientation);
        double Y = -x * Math.sin(orientation) + y * Math.cos(orientation);
        
        double envelope = Math.exp(- ((X*X + aspectRatio*aspectRatio*Y*Y) / (2 * gaussVariance*gaussVariance)));
        double real = Math.cos(2 * Math.PI * (X / wavelength) + phaseOffset);
        double imaginary = Math.sin(2 * Math.PI * (X / wavelength) + phaseOffset);
        
        return new ComplexNumber(envelope * real, envelope * imaginary);
    }
    
    /**
     * 2-D Gabor function.
     * Compute only real part.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @param wavelength Wavelength.
     * @param orientation Orientation.
     * @param phaseOffset Phase offset.
     * @param gaussVariance Gaussian variance.
     * @param aspectRatio Aspect ratio.
     * @return Gabor response.
     */
    public static double RealFunction2D(int x, int y, double wavelength, double orientation, double phaseOffset, double gaussVariance, double aspectRatio){
        
        double X = x * Math.cos(orientation) + y * Math.sin(orientation);
        double Y = -x * Math.sin(orientation) + y * Math.cos(orientation);
        
        double envelope = Math.exp(- ((X*X + aspectRatio*aspectRatio*Y*Y) / (2 * gaussVariance*gaussVariance)));
        double carrier = Math.cos(2 * Math.PI * (X / wavelength) + phaseOffset);
        
        return envelope * carrier;
    }
    
    /**
     * 2-D Gabor function.
     * Compute only imaginary part.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @param wavelength Wavelength.
     * @param orientation Orientation.
     * @param phaseOffset Phase offset.
     * @param gaussVariance Gaussian variance.
     * @param aspectRatio Aspect ratio.
     * @return Gabor response.
     */
    public static double ImaginaryFunction2D(int x, int y, double wavelength, double orientation, double phaseOffset, double gaussVariance, double aspectRatio){
        
        double X = x * Math.cos(orientation) + y * Math.sin(orientation);
        double Y = -x * Math.sin(orientation) + y * Math.cos(orientation);
        
        double envelope = Math.exp(- ((X*X + aspectRatio*aspectRatio*Y*Y) / (2 * gaussVariance*gaussVariance)));
        double carrier = Math.sin(2 * Math.PI * (X / wavelength) + phaseOffset);
        
        return envelope * carrier;
    }
    
    /**
     * 2-D Gabor kernel.
     * @param size Kernel size (should be odd).
     * @param wavelength Wavelength.
     * @param orientation Orientation.
     * @param phaseOffset Phase offset.
     * @param gaussVariance Gaussian variance.
     * @param aspectRatio Aspect ratio.
     * @return Gabor kernel.
     */
    public static double[][] Kernel2D(int size, double wavelength, double orientation, double phaseOffset, double gaussVariance, double aspectRatio){
        double sigmaX = gaussVariance;
        double sigmaY = gaussVariance / aspectRatio;

        int xMax = (int)Math.ceil(Math.max(1,Math.max(Math.abs(size * sigmaX * Math.cos(orientation)), Math.abs(size * sigmaY * Math.sin(orientation)))));
        int yMax = (int)Math.ceil(Math.max(1,Math.max(Math.abs(size * sigmaX * Math.sin(orientation)), Math.abs(size * sigmaY * Math.cos(orientation)))));

        double[][] kernel = new double[2 * xMax + 1][2 * yMax + 1];

        double sum=0;
        for (int x = -xMax;x <= xMax; x++){
            for (int y = -yMax;y <= yMax; y++){
                kernel[x + xMax][y + yMax] = Gabor.ImaginaryFunction2D(x, y, wavelength, orientation, phaseOffset, gaussVariance, aspectRatio);
                sum += kernel[x + xMax][y + yMax];
            }
        }
        for (int x = -xMax;x <= xMax;x++){
            for (int y = -yMax;y <= yMax;y++){
                kernel[x + xMax][y + yMax] /= sum;
            }
        }
        return kernel;
    }
    
    /**
     * 2-D Gabor kernel.
     * @param size Kernel size (should be odd).
     * @param wavelength Wavelength.
     * @param orientation Orientation.
     * @param phaseOffset Phase offset.
     * @param gaussVariance Gaussian variance.
     * @param aspectRatio Aspect ratio.
     * @param config Gabor configuration.
     * @return Gabor kernel.
     */
    public static double[][] Kernel2D(int size, double wavelength, double orientation, double phaseOffset, double gaussVariance, double aspectRatio, Config config){
        double sigmaX = gaussVariance;
        double sigmaY = gaussVariance / aspectRatio;

        int xMax = (int)Math.ceil(Math.max(1,Math.max(Math.abs(size * sigmaX * Math.cos(orientation)), Math.abs(size * sigmaY * Math.sin(orientation)))));
        int yMax = (int)Math.ceil(Math.max(1,Math.max(Math.abs(size * sigmaX * Math.sin(orientation)), Math.abs(size * sigmaY * Math.cos(orientation)))));

        double[][] kernel = new double[2 * xMax + 1][2 * yMax + 1];

        double sum=0;
        
        switch(config){
            case Real:
                for (int x = -xMax;x <= xMax; x++){
                    for (int y = -yMax;y <= yMax; y++){
                        kernel[x + xMax][y + yMax] = Gabor.RealFunction2D(x, y, wavelength, orientation, phaseOffset, gaussVariance, aspectRatio);
                        sum += kernel[x + xMax][y + yMax];
                    }
                }
            break;
            case Imaginary:
                for (int x = -xMax;x <= xMax; x++){
                    for (int y = -yMax;y <= yMax; y++){
                        kernel[x + xMax][y + yMax] = Gabor.ImaginaryFunction2D(x, y, wavelength, orientation, phaseOffset, gaussVariance, aspectRatio);
                        sum += kernel[x + xMax][y + yMax];
                    }
                }
            break;
            case Magnitude:
                for (int x = -xMax;x <= xMax; x++){
                    for (int y = -yMax;y <= yMax; y++){
                        ComplexNumber c = Gabor.Function2D(x, y, wavelength, orientation, phaseOffset, gaussVariance, aspectRatio);
                        kernel[x + xMax][y + yMax] = c.getMagnitude();
                        sum += kernel[x + xMax][y + yMax];
                    }
                }
            break;
            case SquaredMagnitude:
                for (int x = -xMax;x <= xMax; x++){
                    for (int y = -yMax;y <= yMax; y++){
                        ComplexNumber c = Gabor.Function2D(x, y, wavelength, orientation, phaseOffset, gaussVariance, aspectRatio);
                        kernel[x + xMax][y + yMax] = c.getSquaredMagnitude();
                        sum += kernel[x + xMax][y + yMax];
                    }
                }
            break;
        }
        
        for (int x = -xMax;x <= xMax;x++){
            for (int y = -yMax;y <= yMax;y++){
                kernel[x + xMax][y + yMax] /= sum;
            }
        }
        return kernel;
    }
}