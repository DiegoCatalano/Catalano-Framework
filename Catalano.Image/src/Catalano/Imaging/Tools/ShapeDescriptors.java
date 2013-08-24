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

/*
 * Shape Features v0.2
 * Total: 11
 * 
 * Feret's Diameter: Maximum diameter.
 * Feret's Points: Points of Feret's Diameter
 * Irregularity: 1/ThinnessRatio
 * Perimeter Equivalent Diameter: area / Pi
 * Roudness: 4*Area/(Pi*Feret^2)
 * Shape: Perimeter^2 / area
 * ThinnessRatio: 4*Pi*(area/perimeter)
 */
package Catalano.Imaging.Tools;

import Catalano.Core.IntPoint;
import Catalano.Imaging.FastBitmap;
import Catalano.Math.Distance;
import java.util.ArrayList;

/**
 * 
 * @author Diego Catalano
 */
public final class ShapeDescriptors {
    
    /**
     * Don't let anyone instantiate this class.
     */
    private ShapeDescriptors(){};
    
    /**
     * Total of pixels white.
     * Equation: Sum(Image).
     * @param fastBitmap Image to be processed.
     * @return Area.
     */
    public static int Area(FastBitmap fastBitmap){
        int area = 0;
        for (int x = 0; x < fastBitmap.getHeight(); x++) {
            for (int y = 0; y < fastBitmap.getWidth(); y++) {
                if (fastBitmap.getGray(x, y) == 255) {
                    area++;
                }
            }
        }
        return area;
    }
    
    /**
     * Area equivalent diameter.
     * Equation: sqrt((4 / Pi) * Area)
     * @param area Area.
     * @return Area equivalent diameter.
     */
    public static double AreaEquivalentDiameter(int area){
        // Formula: sqrt((4/Pi)*Area)
        double v = 1.2732395447351626861510701069801;
        return Math.sqrt(v*area);
    }
    
    /**
     * Circularity.
     * Equation: 4 * Pi * Area / (Perimeter ^ 2)
     * @param area Area.
     * @param perimeter Perimeter.
     * @return Circularity.
     */
    public double Circularity(int area, int perimeter){
        // Circularity = 4*Pi*Area/Perimeter^2
        double v = 12.566370614359172953850573533118;
        return (v*area) / (perimeter*perimeter);
    }
    
    /**
     * Compactness.
     * Equation: AreaEquivalentDiameter / feretDiameter
     * @param area Area
     * @param feretDiameter Feret diameter.
     * @return Compactness.
     */
    public double Compactness(int area, double feretDiameter){
        return AreaEquivalentDiameter(area) / feretDiameter;
    }
    
    public static double FeretDiameter(ArrayList<IntPoint> contour){
        double maxDiameter = 0;
        for (IntPoint p : contour) {
            for (IntPoint pp : contour) {
                double d = Distance.SquaredEuclidean(p.x, p.y, pp.x, pp.y);
                if (d > maxDiameter) {
                    maxDiameter = d;
                }
            }
        }
        
        return maxDiameter;
    }
    
    public static ArrayList<IntPoint> FeretPoints(ArrayList<IntPoint> contour){
        ArrayList<IntPoint> lst = new ArrayList<IntPoint>();
        
        IntPoint p1 = new IntPoint();
        IntPoint p2 = new IntPoint();
        
        double maxDiameter = 0;
        for (IntPoint p : contour) {
            for (IntPoint pp : contour) {
                double d = Distance.Euclidean(p.x, p.y, pp.x, pp.y);
                if (d > maxDiameter) {
                    maxDiameter = d;
                    p1 = p;
                    p2 = pp;
                }
            }
        }
        
        lst.add(p1);
        lst.add(p2);
        
        return lst;
    }
    
    public static double Irregularity(double thinnessRatio){
        return 1/thinnessRatio;
    }
    
    public static double PerimeterEquivalentDiameter(int area){
        return area / Math.PI;
    }
    
    public static double Roundness(int area, double feretDiameter){
        return (4 * area) / (Math.PI * (feretDiameter * feretDiameter));
    }
    
    public static double Shape(int area, int perimeter){
        return (perimeter * perimeter) / area;
    }
    
    public static double ThinnessRatio(int area, int perimeter){
        // Original: 4 * Math.Pi * (area/perimeter)
        double v = 12.566370614359172953850573533118;
        return v * (area/perimeter);
    }
}