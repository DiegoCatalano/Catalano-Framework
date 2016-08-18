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

package Catalano.Imaging;

import Catalano.Core.IntPoint;
import Catalano.Imaging.Shapes.IntPolygon;
import Catalano.Imaging.Shapes.IntRectangle;
import java.util.List;

/**
 * Fast Graphics.
 * Allow to drawing over Fast Bitmap.
 * @author Diego Catalano
 */
public class FastGraphics {
    
    private FastBitmap fastBitmap;
    private Color color;
    private int gray = 0;
    
    /**
     * Set color.
     * @param color Color.
     */
    public void setColor(Color color){
        this.color = color;
    }
    
    /**
     * Set color.
     * @param red Red channel.
     * @param green Green channel.
     * @param blue Blue channel.
     */
    public void setColor(int red, int green, int blue){
        color = new Color(red, green, blue);
    }
    
    /**
     * Set color.
     * @param gray Gray channel.
     */
    public void setColor(int gray){
        this.gray = gray;
    }
    
    /**
     * Set Image.
     * @param fastBitmap Image to be processed.
     */
    public void setImage(FastBitmap fastBitmap) {
        this.fastBitmap = fastBitmap;
    }

    /**
     * Initialize a new instance of the FastGraphics class.
     * @param fastBitmap Image to be processed.
     */
    public FastGraphics(FastBitmap fastBitmap) {
        this.fastBitmap = fastBitmap;
    }
    
    /**
     * Draw Circle.
     * @param p IntPoint contains X and Y coordinates.
     * @param radius Radius.
     */
    public void DrawCircle(IntPoint p, int radius){
        DrawCircle(p.x, p.y, radius);
    }
    
    /**
     * Draw Circle.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @param radius Radius.
     */
    public void DrawCircle(int x, int y, int radius){
        
        if(fastBitmap.isRGB()){
            int i = radius, j = 0;
            int radiusError = 1-i;

            while(i >= j){
              fastBitmap.setRGB(i + x, j + y, color.r, color.g, color.b);
              fastBitmap.setRGB(j + x, i + y, color.r, color.g, color.b);
              fastBitmap.setRGB(-i + x, j + y, color.r, color.g, color.b);
              fastBitmap.setRGB(-j + x, i + y, color.r, color.g, color.b);
              fastBitmap.setRGB(-i + x, -j + y, color.r, color.g, color.b);
              fastBitmap.setRGB(-j + x, -i + y, color.r, color.g, color.b);
              fastBitmap.setRGB(i + x, -j + y, color.r, color.g, color.b);
              fastBitmap.setRGB(j + x, -i + y, color.r, color.g, color.b);

              j++;
              if(radiusError<0)

                  radiusError+=2*j+1;
              else{
                  i--;
                  radiusError+=2*(j-i+1);
              }
            }
        }
        else{
            int i = radius, j = 0;
            int radiusError = 1-i;

            while(i >= j){
              fastBitmap.setGray(i + x, j + y, gray);
              fastBitmap.setGray(j + x, i + y, gray);
              fastBitmap.setGray(-i + x, j + y, gray);
              fastBitmap.setGray(-j + x, i + y, gray);
              fastBitmap.setGray(-i + x, -j + y, gray);
              fastBitmap.setGray(-j + x, -i + y, gray);
              fastBitmap.setGray(i + x, -j + y, gray);
              fastBitmap.setGray(j + x, -i + y, gray);

              j++;
              if(radiusError<0)

                  radiusError+=2*j+1;
              else{
                  i--;
                  radiusError+=2*(j-i+1);
              }
            }
        }
    }
    
    /**
     * Draw Line.
     * @param p IntPoint contains X and Y coordinates.
     * @param q IntPoint contains X and Y coordinates.
     */
    public void DrawLine(IntPoint p, IntPoint q){
        DrawLine(p.x, p.y, q.x, q.y);
    }
    
    /**
     * http://tech-algorithm.com/articles/drawing-line-using-bresenham-algorithm/
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @param x2 X2 axis coordinate.
     * @param y2 Y2 axis coordinate.
     */
    public void DrawLine(int x, int y, int x2, int y2) {
        
        if(fastBitmap.isRGB()){
            int w = x2 - x ;
            int h = y2 - y ;
            int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0 ;
            if (w<0) dx1 = -1 ; else if (w>0) dx1 = 1 ;
            if (h<0) dy1 = -1 ; else if (h>0) dy1 = 1 ;
            if (w<0) dx2 = -1 ; else if (w>0) dx2 = 1 ;
            int longest = Math.abs(w) ;
            int shortest = Math.abs(h) ;
            if (!(longest>shortest)) {
                longest = Math.abs(h) ;
                shortest = Math.abs(w) ;
                if (h<0) dy2 = -1 ; else if (h>0) dy2 = 1 ;
                dx2 = 0 ;            
            }
            int numerator = longest >> 1 ;
            for (int i=0;i<=longest;i++) {
                fastBitmap.setRGB(x, y, color.r, color.g, color.b);
                numerator += shortest ;
                if (!(numerator<longest)) {
                    numerator -= longest ;
                    x += dx1 ;
                    y += dy1 ;
                } else {
                    x += dx2 ;
                    y += dy2 ;
                }
            }
        }
        else{
            int w = x2 - x ;
            int h = y2 - y ;
            int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0 ;
            if (w<0) dx1 = -1 ; else if (w>0) dx1 = 1 ;
            if (h<0) dy1 = -1 ; else if (h>0) dy1 = 1 ;
            if (w<0) dx2 = -1 ; else if (w>0) dx2 = 1 ;
            int longest = Math.abs(w) ;
            int shortest = Math.abs(h) ;
            if (!(longest>shortest)) {
                longest = Math.abs(h) ;
                shortest = Math.abs(w) ;
                if (h<0) dy2 = -1 ; else if (h>0) dy2 = 1 ;
                dx2 = 0 ;            
            }
            int numerator = longest >> 1 ;
            for (int i=0;i<=longest;i++) {
                fastBitmap.setGray(x, y, gray);
                numerator += shortest ;
                if (!(numerator<longest)) {
                    numerator -= longest ;
                    x += dx1 ;
                    y += dy1 ;
                } else {
                    x += dx2 ;
                    y += dy2 ;
                }
            }
        }
    }
    
    /**
     * Draw image over a image.
     * @param image Image.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     */
    public void DrawImage(FastBitmap image, int x, int y){
        int width = image.getWidth();
        int height = image.getHeight();
        
        if(image.getCoordinateSystem() == FastBitmap.CoordinateSystem.Matrix){
            if(image.isGrayscale()){
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        fastBitmap.setGray(i+x, j+y, image.getGray(i, j));
                    }
                }
            }
            else if(image.isRGB()){
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        fastBitmap.setRGB(i+x, j+y, image.getRGB(i, j));
                    }
                }
            }
            else{
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        fastBitmap.setARGB(i+x, j+y, image.getARGB(i, j));
                    }
                }
            }
        }
        else{
            if(image.isGrayscale()){
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        fastBitmap.setGray(j+x, i+y, image.getGray(j, i));
                    }
                }
            }
            else if(image.isRGB()){
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        fastBitmap.setRGB(j+x, i+y, image.getRGB(j, i));
                    }
                }
            }
            else{
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        fastBitmap.setARGB(j+x, i+y, image.getARGB(j, i));
                    }
                }
            }
        }
    }
    
    /**
     * Draw Polygon.
     * @param polygon Polygon.
     */
    public void DrawPolygon(IntPolygon polygon){
        DrawPolygon(polygon.getX(), polygon.getY());
    }
    
    /**
     * Draw Polygon.
     * @param polygon Polygon.
     * @param nPoints Number of points.
     */
    public void DrawPolygon(IntPolygon polygon, int nPoints){
        DrawPolygon(polygon.getX(), polygon.getY(), nPoints);
    }
    
    /**
     * Draw Polygon
     * @param points List of points.
     * @param n Number of points.
     */
    public void DrawPolygon(List<IntPoint> points, int n){
        if (points.size() > 2 && n > 2){
            for (int i = 1; i < n; i++) {
                DrawLine(points.get(i), points.get(i - 1));
            }
            DrawLine(points.get(n - 1), points.get(0));
        }
        else{
            throw new IllegalArgumentException("Draw Polygon needs at least 3 points.");
        }
    }
    
    /**
     * Draw Polygon
     * @param points List of points.
     */
    public void DrawPolygon(List<IntPoint> points){
        DrawPolygon(points, points.size());
    }
    
    /**
     * Draw Polygon.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     */
    public void DrawPolygon(int[] x, int[] y){
        DrawPolygon(x, y, x.length);
    }
    
    /**
     * Draw Polygon.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @param n Number of points.
     */
    public void DrawPolygon(int[] x, int[] y, int n){
        if (x.length > 2 && y.length > 2){
            if (x.length == y.length){
                for (int i = 1; i < n; i++) {
                    DrawLine(x[i], y[i], x[i - 1], y[i - 1]);
                }
                DrawLine(x[n - 1], y[n - 1], x[0], y[0]);
            }
            else{
                throw new IllegalArgumentException("Draw Polygon: X and Y must be the same size.");
            }
        }
        else{
            throw new IllegalArgumentException("Draw Polygon: X and Y needs at least 3 points.");
        }
    }
    
    /**
     * Draw Rectangle.
     * @param rectangle IntRectangle shape.
     */
    public void DrawRectangle(IntRectangle rectangle){
        DrawRectangle(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }
    
    /**
     * Draw Rectangle.
     * @param p IntPoint contains X and Y coordinates.
     * @param width Width of rectangle.
     * @param height Height of rectangle.
     */
    public void DrawRectangle(IntPoint p, int width, int height){
        DrawRectangle(p.x, p.y, width, height);
    }
    
    /**
     * Draw Rectangle.
     * @param x X axis coordinate.
     * @param y Y axis coordinate.
     * @param width Width of rectangle.
     * @param height Height of rectangle.
     */
    public void DrawRectangle(int x, int y, int width, int height){
        
        if (fastBitmap.isRGB()){
            
            for (int j = y; j < y + width; j++) {
                fastBitmap.setRGB(x, j, color);
            }
            
            for (int j = y; j < y + width; j++) {
                fastBitmap.setRGB(x + height, j, color);
            }
            
            for (int i = x; i < x + height; i++) {
                fastBitmap.setRGB(i, y, color);
                fastBitmap.setRGB(i, y + width, color);
            }
            
        } else{
            
            for (int j = y; j < y + width; j++) {
                fastBitmap.setGray(x, j, gray);
            }
            
            for (int j = y; j < y + width; j++) {
                fastBitmap.setGray(x + height, j, gray);
            }
            
            for (int i = x; i < x + height; i++) {
                fastBitmap.setGray(i, y, gray);
                fastBitmap.setGray(i, y + width, gray);
            }
        }
    }
}