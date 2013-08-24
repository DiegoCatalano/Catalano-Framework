// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2013
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

package Catalano.Imaging.Tools;

import Catalano.Imaging.FastBitmap;

/**
 * Represents line of Hough Line Transformation.
 * @author Diego Catalano
 */
public class HoughLine implements Comparable{
    
    double theta;
    double radius;
    int intensity;

    /**
     * Initialize a new instance of the HoughLine class.
     */
    public HoughLine() {}

    /**
     * Initialize a new instance of the HoughLine class.
     * @param theta Angle.
     * @param r Radius.
     * @param intensity Intensity.
     */
    public HoughLine(double theta, double r, int intensity) {
        this.theta = theta;
        this.radius = r;
        this.intensity = intensity;
    }

    /**
     * Get Radius.
     * @return Radius.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Set Radius.
     * @param r Radius.
     */
    public void setRadius(double r) {
        this.radius = r;
    }

    /**
     * Get Theta.
     * @return Theta.
     */
    public double getTheta() {
        return theta;
    }

    /**
     * Set Theta.
     * @param theta Theta.
     */
    public void setTheta(double theta) {
        this.theta = theta;
    }

    /**
     * Get Intensity.
     * @return Intensity.
     */
    public int getIntensity() {
        return intensity;
    }

    /**
     * Set Intensity.
     * @param intensity Intensity.
     */
    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }
    
    /**
     * Draw line.
     * @param fastBitmap Image to be processed.
     * @param red Red Channel.
     * @param green Green Channel.
     * @param blue Blue Channel.
     */
    public void drawLine(FastBitmap fastBitmap, int red, int green, int blue) { 
        int height = fastBitmap.getHeight(); 
        int width = fastBitmap.getWidth(); 
 
        // During processing h_h is doubled so that -ve r values 
        int houghHeight = (int) (Math.sqrt(2) * Math.max(height, width)) / 2; 
 
        // Find edge points and vote in array 
        float centerX = width / 2; 
        float centerY = height / 2; 
 
        // Draw edges in output array 
        double tsin = Math.sin(theta); 
        double tcos = Math.cos(theta); 
 
        if (theta < Math.PI * 0.25 || theta > Math.PI * 0.75) {
            // Draw vertical-ish lines 
            for (int y = 0; y < width; y++) { 
                int x = (int) ((((radius - houghHeight) - ((y - centerY) * tsin)) / tcos) + centerX); 
                if (x < height && x >= 0) {
                    fastBitmap.setRGB(x, y, red, green, blue); 
                } 
            } 
        } else { 
            // Draw horizontal-sh lines 
            for (int x = 0; x < height; x++) { 
                int y = (int) ((((radius - houghHeight) - ((x - centerX) * tcos)) / tsin) + centerY); 
                if (y < width && y >= 0) { 
                    fastBitmap.setRGB(x, y, red, green, blue); 
                }
            } 
        }
    }
    
    @Override
    public int compareTo(Object o) {
        HoughLine hl = (HoughLine)o;
        if (this.intensity > hl.intensity) return -1;
        if (this.intensity < hl.intensity) return 1;
        return 0;
    }
}