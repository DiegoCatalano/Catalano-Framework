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

/*
 * Shape Descriptors.
 * Total: 11
 */
package Catalano.Imaging.Tools;

import Catalano.Core.IntPoint;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Filters.DistanceTransform;
import Catalano.Imaging.Filters.Invert;
import Catalano.Math.Distances.Distance;
import Catalano.Math.Matrix;
import java.util.ArrayList;
import java.util.List;

/**
 * Shape Descriptors.
 * @author Diego Catalano
 */
public final class ShapeDescriptors {
    
    /**
     * Don't let anyone instantiate this class.
     */
    private ShapeDescriptors(){};
    
    /**
     * Area.
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
    
    /**
     * Euler number.
     * Number of holes in the shape.
     * @param fastBitmap Image to be processed.
     * @return Euler number.
     */
    public static int EulerNumber(FastBitmap fastBitmap){
        
        Invert inv = new Invert();
        inv.applyInPlace(fastBitmap);
        
        BlobDetection bd = new BlobDetection();
        List<Blob> blobs = bd.ProcessImage(fastBitmap);
        
        int size = blobs.size() - 1;
        if(size < 0) return 0;
        
        return size;
        
    }
    
    /**
     * Feret Diameter.
     * Feret diameter is also called the maximum diameter in image.
     * @param contour Contour of the image.
     * @return Feret diameter.
     */
    public static double FeretDiameter(List<IntPoint> contour){
        double maxDiameter = 0;
        for (IntPoint p : contour) {
            for (IntPoint pp : contour) {
                double d = Distance.SquaredEuclidean(p.x, p.y, pp.x, pp.y);
                if (d > maxDiameter) {
                    maxDiameter = d;
                }
            }
        }
        
        return Math.sqrt(maxDiameter);
    }
    
    /**
     * Feret Points
     * @param contour Contour of the shape.
     * @return Feret Points.
     */
    public static List<IntPoint> FeretPoints(List<IntPoint> contour){
        List<IntPoint> lst = new ArrayList<IntPoint>();
        
        IntPoint p1 = new IntPoint();
        IntPoint p2 = new IntPoint();
        
        double maxDiameter = 0;
        for (IntPoint p : contour) {
            for (IntPoint pp : contour) {
                double d = Distance.SquaredEuclidean(p.x, p.y, pp.x, pp.y);
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
    
    /**
     * Maximum error circularity.
     * @param mcc Minimum Circumscribed Circle.
     * @param mic Maximum Inscribed Circle.
     * @return Maximum error circularity.
     */
    public static double MaximumErrorCircularity(double mcc, double mic){
        return mcc - mic;
    }
    
    /**
     * Maximum inscribed circle.
     * @param fastBitmap Image to be processed.
     * @return Maximum inscribed circle.
     */
    public static double MaximumInscribedCircle(FastBitmap fastBitmap){
        
        DistanceTransform dt = new DistanceTransform();
        float[][] dist = dt.Compute(fastBitmap);
        
        return Matrix.Max(dist);
        
    }
    
    /**
     * Minimum circumscribed circle.
     * @param starPoint The point where is the most distance from nearest boundary.
     * @param contour Contour of the shape.
     * @return Minimum circumscribed circle.
     */
    public static double MinimumCircumscribedCircle(IntPoint starPoint, List<IntPoint> contour){
        
        double max = Double.MIN_VALUE;
        for (IntPoint p : contour) {
            double d = Distance.SquaredEuclidean(p, starPoint);
            if (d > max){
                max = d;
            }
        }
        
        return Math.sqrt(max);
        
    }
    
    /**
     * Star Point.
     * @param fastBitmap Image to be processed.
     * @return IntPoint The point where is the most distance from nearest boundary.
     */
    public static IntPoint StarPoint (FastBitmap fastBitmap){
        
        DistanceTransform dt = new DistanceTransform();
        float[][] dist = dt.Compute(fastBitmap);
        
        return Matrix.MaxIndex(dist);
        
    }
    
    /**
     * Perimeter Equivalent Diameter.
     * Equation: area / PI
     * @param area Area.
     * @return Perimeter Equivalent Diameter.
     */
    public static double PerimeterEquivalentDiameter(int area){
        return area / Math.PI;
    }
    
    /**
     * Roughness.
     * Equation: (width / height)
     * @param width Width of the object.
     * @param height Height of the object.
     * @return Roughness.
     */
    public static double Roughness(int width, int height){
        return width/height;
    }
    
    /**
     * Roundness.
     * Equation: (4 * area) / (PI * (feretDiameter ^ 2)
     * @param area Area.
     * @param feretDiameter Feret diameter.
     * @return Roundness.
     */
    public static double Roundness(int area, double feretDiameter){
        return (4 * area) / (Math.PI * (feretDiameter * feretDiameter));
    }
    
    /**
     * Shape.
     * Equation: (perimeter ^ 2) / area
     * @param area Area.
     * @param perimeter Perimeter.
     * @return Shape.
     */
    public static double Shape(int area, int perimeter){
        return (perimeter * perimeter) / area;
    }
    
    /**
     * Thinness Ratio.
     * Equation: 4 * Pi * (area/perimeter).
     * @param area Area.
     * @param perimeter Perimeter.
     * @return Thinness ratio.
     */
    public static double ThinnessRatio(int area, int perimeter){
        double v = 12.566370614359172953850573533118;
        return v * (area/perimeter);
    }
    
    /**
     * Ultimate Eroded Points.
     * Computes the ultimate points from erosion operation.
     * @param fastBitmap Image to be processed.
     * @return List of points contains
     */
    public static List<IntPoint> UltimateErodedPoints(FastBitmap fastBitmap){
        
        ArrayList<IntPoint> points = new ArrayList<IntPoint>();
        
        DistanceTransform dt = new DistanceTransform();
        float[][] dist = dt.Compute(fastBitmap);
        
        BlobDetection bd = new BlobDetection();
        List<Blob> blobs = bd.ProcessImage(fastBitmap);
        
        for (Blob blob : blobs) {
            ArrayList<IntPoint> lst = blob.getPoints();
            float max = 0;
            for (IntPoint p : lst) {
                if(dist[p.x][p.y] > max){
                    max = dist[p.x][p.y];
                }
            }
            for (IntPoint p : lst) {
                if(dist[p.x][p.y] == max){
                    points.add(new IntPoint(p));
                }
            }
        }
        return points;
    }
}