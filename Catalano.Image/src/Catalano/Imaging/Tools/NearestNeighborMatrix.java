// Catalano Imaging Library
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

package Catalano.Imaging.Tools;

import Catalano.Core.IntPoint;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Filters.DistanceTransform;
import Catalano.Math.Distances.Distance;
import java.util.ArrayList;

/**
 * Nearest Neighbor Matrix.
 * @author Diego Catalano
 */
public class NearestNeighborMatrix {
    
    public static enum Direction {Horizontal, Vertical};
    private Direction direction = Direction.Vertical;
    private int startIndex = 1;

    /**
     * Get Direction.
     * @return Direction.
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Set Direction.
     * @param direction Direction.
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Get Start index.
     * @return Start index.
     */
    public int getStartIndex() {
        return startIndex;
    }

    /**
     * Set Start index.
     * @param startIndex Start index.
     */
    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    /**
     * Initialize a new instance of the NearestNeighborMatrix class.
     */
    public NearestNeighborMatrix() {}
    
    /**
     * Initialize a new instance of the NearestNeighborMatrix class.
     * @param startIndex Start index.
     */
    public NearestNeighborMatrix(int startIndex){
        this.startIndex = startIndex;
    }
    
    /**
     * Initialize a new instance of the NearestNeighborMatrix class.
     * @param direction Direction.
     */
    public NearestNeighborMatrix(Direction direction){
        this.direction = direction;
    }

    /**
     * Initialize a new instance of the NearestNeighborMatrix class.
     * @param direction Direction.
     * @param startIndex Start index.
     */
    public NearestNeighborMatrix(Direction direction, int startIndex) {
        this.direction = direction;
        this.startIndex = startIndex;
    }
    
    /**
     * Process image.
     * @param fastBitmap Image to be processed.
     * @return Nearest Neighbor Matrix.
     */
    public int[][] ProcessImage(FastBitmap fastBitmap){
        
        float[][] image = new DistanceTransform().Compute(fastBitmap);
        
        ArrayList<IntPoint> boundary = new ArrayList<IntPoint>();
        int height = image.length;
        int width = image[0].length;
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if(image[i][j] == 1.0f){
                    boundary.add(new IntPoint(i, j));
                }
            }
        }
        
        return ProcessImage(fastBitmap, boundary);
        
    }
    
    /**
     * Process image.
     * @param fastBitmap Image to be processed.
     * @param boundary List of points contains boundary of object.
     * @return Nearest Neighbor Matrix.
     */
    public int[][] ProcessImage(FastBitmap fastBitmap, ArrayList<IntPoint> boundary){
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        int[][] matrix = new int[height][width];
        
        if (fastBitmap.isGrayscale()){
            
            if(this.direction == Direction.Vertical){
                //Map indexes
                int index = startIndex;
                for (int j = 0; j < width; j++) {
                    for (int i = 0; i < height; i++) {
                        if (fastBitmap.getGray(i, j) == 255){
                            matrix[i][j] = index;
                        }
                        index++;
                    }
                }

                //Process background
                for (int j = 0; j < width; j++) {
                    for (int i = 0; i < height; i++) {

                        if (fastBitmap.getGray(i, j) == 0){

                            double minDistance = Double.MAX_VALUE;
                            IntPoint point = new IntPoint();
                            for (IntPoint p : boundary) {
                                double dist = Distance.SquaredEuclidean(i, j, p.x, p.y);
                                if (dist < minDistance){
                                    minDistance = dist;
                                    point = p;
                                }
                            }
                            matrix[i][j] = matrix[point.x][point.y];

                        }

                    }
                }
                return matrix;
            }
            
            //Map indexes
            int index = startIndex;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (fastBitmap.getGray(i, j) == 255){
                        matrix[i][j] = index;
                    }
                    index++;
                }
            }
            
            //Process background
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    
                    if (fastBitmap.getGray(i, j) == 0){
                        
                        double minDistance = Double.MAX_VALUE;
                        IntPoint point = new IntPoint();
                        for (IntPoint p : boundary) {
                            double dist = Distance.SquaredEuclidean(i, j, p.x, p.y);
                            if (dist < minDistance){
                                minDistance = dist;
                                point = p;
                            }
                        }
                        matrix[i][j] = matrix[point.x][point.y];
                        
                    }
                    
                }
            }
            return matrix;
        }
        return null;
    }
    
    /**
     * Get the point relative nearest neighbor matrix.
     * @param nearestNeighborIndex Nearest Neighbor index.
     * @param width Width of the matrix.
     * @param height Height of the matrix.
     * @return Point.
     */
    public IntPoint getPoint(int nearestNeighborIndex, int width, int height){
        
        int i = nearestNeighborIndex - 1;
        int x = (int)Math.floor(i / height);
        int y = (i % width);
        
        return new IntPoint(x, y);
        
    }
}