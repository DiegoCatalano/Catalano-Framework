// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
//
// In Aforge.NET, its called ComplexImage. But i adapted to this framework, i just changed the name.
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
import Catalano.Math.ComplexNumber;
import Catalano.Math.Tools;

/**
 * Fourier Transform.
 * @author Diego Catalano
 */
public class FourierTransform {
    
    private ComplexNumber[][] data;
    private int width, height;
    private boolean fourierTransformed = false;

    /**
     * Initialize a new instance of the FourierTransform class.
     * @param fastBitmap FastBitmap.
     */
    public FourierTransform(FastBitmap fastBitmap) {
        if (fastBitmap.isGrayscale()) {
            this.width = fastBitmap.getWidth();
            this.height = fastBitmap.getHeight();
            
            data = new ComplexNumber[height][width];

            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    data[x][y] = new ComplexNumber(0, 0);
                    data[x][y].real = (float) fastBitmap.getGray(x, y);
                }
            }
        }
        else{
            try {
                throw new Exception("FourierTransform works only with Grayscale images");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Complex image width.
     * @return Width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Complex image height.
     * @return Height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Complex image's data.
     * @return Data.
     */
    public ComplexNumber[][] getData() {
        return data;
    }

    /**
     * Complex image's data.
     * @param data Data.
     */
    public void setData(ComplexNumber[][] data) {
        this.data = data;
    }

    /**
     * Status of the image - Fourier transformed or not.
     * @return <b>True</b>: is transformed, otherwise returns <b>False</b>.
     */
    public boolean isFourierTransformed() {
        return fourierTransformed;
    }
    
    /**
     * Convert Complex image's data to FastBitmap.
     * @return FastBitmap.
     */
    public FastBitmap toFastBitmap(){
        
        FastBitmap fb = new FastBitmap(width, height, FastBitmap.ColorSpace.Grayscale);
        
        if(fourierTransformed){
            
            //Calculate the magnitude
            double[][] mag = new double[height][width];
            double min = Double.MAX_VALUE;
            double max = -Double.MAX_VALUE;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    //Compute log for perceptual scaling and +1 since log(0) is undefined.
                    mag[i][j] = Math.log(data[i][j].getMagnitude() + 1);
                    
                    if(mag[i][j] < min) min = mag[i][j];
                    if(mag[i][j] > max) max = mag[i][j];
                }
            }
            
            //Scale the image
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    fb.setGray(i, j, (int)Tools.Scale(min, max, 0, 255, mag[i][j]));
                }
            }
        }
        else{
            
            //Show only the real part
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int real = (int)data[i][j].real;
                    fb.setGray(i, j, fb.clampValues(real, 0, 255));
                }
            }
            
        }
        
        return fb;
    }
    
    /**
     * Applies forward fast Fourier transformation to the complex image.
     */
    public void Forward(){
        if (!fourierTransformed){
            for ( int x = 0; x < height; x++ ){
                for ( int y = 0; y < width; y++ ){
                    if ( ( ( x + y ) & 0x1 ) != 0 ){
                        data[x][y].real *= -1;
                        data[x][y].imaginary *= -1;
                    }
                }
            }

            Catalano.Math.Transforms.FourierTransform.FFT2(data, Catalano.Math.Transforms.FourierTransform.Direction.Forward);
            fourierTransformed = true;
        }
    }
    
    /**
     * Applies backward fast Fourier transformation to the complex image.
     */
    public void Backward( ){
        if ( fourierTransformed ){
            Catalano.Math.Transforms.FourierTransform.FFT2(data, Catalano.Math.Transforms.FourierTransform.Direction.Backward);
            fourierTransformed = false;

            for ( int x = 0; x < height; x++ ){
                for ( int y = 0; y < width; y++ ){
                    if ( ( ( x + y ) & 0x1 ) != 0 ){
                        data[x][y].real *= -1;
                        data[x][y].imaginary *= -1;
                    }
                }
            }
        }
    }
}