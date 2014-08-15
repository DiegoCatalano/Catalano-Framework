// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
// diego.catalano at live.com
//
// Copyright © Andrew Kirillov, 2007-2008
// andrew.kirillov at gmail.com
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
import Catalano.Imaging.Tools.ImagePadding;
import Catalano.Math.ComplexNumber;

/**
 * Fourier Transform.
 * @author Diego Catalano
 */
public class FourierTransform {
    
    private ComplexNumber[][] data;
    private int width, height;
    private boolean fourierTransformed = false;
    private boolean useZeroPadding = false;
    private int oriWidth;
    private int oriHeight;
    private int nW;
    private int nH;

    /**
     * Initialize a new instance of the FourierTransform class.
     * @param fastBitmap FastBitmap.
     */
    public FourierTransform(FastBitmap fastBitmap) {
        if (fastBitmap.isGrayscale()) {
            this.width = fastBitmap.getWidth();
            this.height = fastBitmap.getHeight();
            
            boolean a = Catalano.Math.Tools.IsPowerOf2(width);
            boolean b = Catalano.Math.Tools.IsPowerOf2(height);
            
            if (a == false || b == false){
                ZeroPadding(fastBitmap);
                this.width = fastBitmap.getWidth();
                this.height = fastBitmap.getHeight();
            }
            
            data = new ComplexNumber[height][width];

            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    data[x][y] = new ComplexNumber(0, 0);
                    data[x][y].real = (float) fastBitmap.getGray(x, y) / 255;
                }
            }
        }
        else{
            try {
                throw new Exception("ComplexImage works only with Grayscale images");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void ZeroPadding(FastBitmap fastBitmap){
        
        this.oriWidth = fastBitmap.getWidth();
        this.oriHeight = fastBitmap.getHeight();

        this.nW = Catalano.Math.Tools.NextPowerOf2(oriWidth);
        this.nH = Catalano.Math.Tools.NextPowerOf2(oriHeight);

        ImagePadding expand = new ImagePadding(nW - oriWidth, nH - oriHeight);
        expand.applyInPlace(fastBitmap);
        this.useZeroPadding = true;
        
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
        
        double scale = ( fourierTransformed ) ? Math.sqrt( width * height ) : 1;
        
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                fb.setGray(x, y, (int)Math.max( 0, Math.min( 255, data[x][y].getMagnitude() * scale * 255 )));
            }
        }
        
        if (useZeroPadding && !fourierTransformed){
            Crop crop = new Crop((nH - oriHeight) / 2, (nW - oriWidth) / 2, oriWidth, oriHeight);
            crop.ApplyInPlace(fb);
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