// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2014
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

package Catalano.Imaging.Filters.Artistic;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IBaseInPlace;

/**
 * Opacity filter.
 * @author Diego Catalano
 */
public class Opacity implements IBaseInPlace{
    
    private FastBitmap overlay;
    private double p = 0.65D;

    /**
     * Get overlay image.
     * @return Overlay image.
     */
    public FastBitmap getOverlay() {
        return overlay;
    }

    /**
     * Set overlay image.
     * @param overlay Overlay image.
     */
    public void setOverlay(FastBitmap overlay) {
        this.overlay = overlay;
    }

    /**
     * Get percentage.
     * @return Percentage.
     */
    public double getPercentage() {
        return p;
    }

    /**
     * Set percetange.
     * @param p Percentage [0..1].
     */
    public void setPercentage(double p) {
        this.p = Math.max(0, Math.min(1, p));
    }

    /**
     * Initializes a new instance of the HeatMap class.
     * @param overlay Overlay image.
     */
    public Opacity(FastBitmap overlay) {
        this.overlay = overlay;
    }
    
    /**
     * Initializes a new instance of the HeatMap class.
     * @param overlay Overlay image.
     * @param percentage Percentage [0..1].
     */
    public Opacity(FastBitmap overlay, double percentage){
        this.overlay = overlay;
        setPercentage(percentage);
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if(fastBitmap.isRGB()){
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    double r1 = fastBitmap.getRed(i, j);
                    double g1 = fastBitmap.getGreen(i, j);
                    double b1 = fastBitmap.getBlue(i, j);

                    double r2 = overlay.getRed(i, j);
                    double g2 = overlay.getGreen(i, j);
                    double b2 = overlay.getBlue(i, j);

                    double r = (1 - p) * r1 + p * r2;
                    double g = (1 - p) * g1 + p * g2;
                    double b = (1 - p) * b1 + p * b2;

                    fastBitmap.setRGB(i, j, (int)r, (int)g, (int)b);
                }
            }
        }
        else if (fastBitmap.isARGB()){
            int width = fastBitmap.getWidth();
            int height = fastBitmap.getHeight();

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    double a1 = fastBitmap.getAlpha(i, j);
                    double r1 = fastBitmap.getRed(i, j);
                    double g1 = fastBitmap.getGreen(i, j);
                    double b1 = fastBitmap.getBlue(i, j);

                    double a2 = overlay.getAlpha(i, j);
                    double r2 = overlay.getRed(i, j);
                    double g2 = overlay.getGreen(i, j);
                    double b2 = overlay.getBlue(i, j);

                    double a = (1 - p) * a1 + p * a2;
                    double r = (1 - p) * r1 + p * r2;
                    double g = (1 - p) * g1 + p * g2;
                    double b = (1 - p) * b1 + p * b2;

                    fastBitmap.setARGB(i, j, (int)a, (int)r, (int)g, (int)b);
                }
            }
        }
        else{
            throw new IllegalArgumentException("Opacity only works in RGB or ARGB images.");
        }
    }
}