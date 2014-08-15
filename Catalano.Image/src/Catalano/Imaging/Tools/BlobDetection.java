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

package Catalano.Imaging.Tools;

import Catalano.Core.IntPoint;
import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Shapes.IntRectangle;
import Catalano.Math.Geometry.PointsCloud;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Detects blobs.
 * @author Diego Catalano
 */
public class BlobDetection {
    
    private int width;
    private int height;
    private FastBitmap light;
    private int size; //All blobs
    private int rR = 0,rG = 0,rB = 0;
    private ArrayList<Blob> blobs;
    private Blob blob; //Cria blob para instanciar os valores finais
    private int id = 0; //ID blob
    private boolean filterBlob = false;
    private int minArea = 1,maxArea; //filtrar os blobs pela area
    private int idBigBlob; //Biggest blob
    private int areaBig = 0; //Biggest area

    public BlobDetection() {}

    public int size() {
        return this.size;
    }
    
    public boolean isFilterBlob(){
        return this.filterBlob;
    }
    
    public void setFilterBlob(boolean bool){
        this.filterBlob = bool;
    }

    public int getMaxArea() {
        return maxArea;
    }

    public void setMaxArea(int maxArea) {
        this.maxArea = maxArea;
    }

    public int getMinArea() {
        return minArea;
    }

    public void setMinArea(int minArea) {
        this.minArea = minArea;
    }
    
    public int getIdBiggestBlob(){
        return this.idBigBlob;
    }

    public ArrayList<Blob> ProcessImage(FastBitmap fastBitmap) {
        
        width = fastBitmap.getWidth();
        height = fastBitmap.getHeight();
        
        if (maxArea == 0) {
            maxArea = width*height;
        }
        
        //Create another FastBitmap
        FastBitmap l = new FastBitmap(fastBitmap);
        l.toRGB();
        blobs = new ArrayList<Blob>();
        
        light = l;
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                // Can be any color
                if (light.getRed(x, y) == 255) {
                    ShuffleColor();
                    FindBlob(x,y,rR,rG,rB);
                }
            }
        }
        return blobs;
    }
    
    /**
     * Tag blob with any color.
     */
    private void ShuffleColor(){
        if (rB == 255) {
            if (rG == 255) {
                if (rR == 255) {
                    rR = rG = rB = 0;
                }
                rR++;
                rG = 0;
            }
            else{
                rG++;
                rB = 0;
            }
        }
        else{
            rB++;
        }
    }
    
    private void FindBlob(int x,int y, int r, int g, int b){
        ArrayList<IntPoint> blobPoints = new ArrayList<IntPoint>();
        Stack<IntPoint> examList = new Stack<IntPoint>();
        
        int xc = 0,yc = 0; //Centroid
        int blobArea = 0; //Area total
        
        int iR = light.getRed(x, y);
        int iG = light.getGreen(x, y);
        int iB = light.getBlue(x, y);
        int iRGB = iR+iG+iB;
        
        int _r,_g,_b;
        examList.push(new IntPoint(x,y));
        while (examList.size() > 0) {
            IntPoint p = examList.pop();
            _r = light.getRed(p.x, p.y);
            _g = light.getRed(p.x, p.y);
            _b = light.getRed(p.x, p.y);
            int _RGB = _r+_g+_b;
            
            if (_RGB == iRGB) {
                x = p.x;
                y = p.y;
                
                light.setRGB(x, y, r, g, b);
                blobArea++;
                blobPoints.add(new IntPoint(x, y));
                xc += p.x;
                yc += p.y;
                
                if (x-1 > 0) {
                    examList.push(new IntPoint(x-1,y));        // check west neighbor
                }
                if (x+1 < height) {
                    examList.push(new IntPoint(x+1,y));        // check east neighbor
                }
                if (y-1 > 0) {
                    examList.push(new IntPoint(x,y-1));        // check north neighbor
                }
                if (y+1 < width) {
                    examList.push(new IntPoint(x,y+1));        // check south neighbor
                }
            }
        }
        
        if (filterBlob == true) {
            if ((blobArea > minArea ) && (blobArea < maxArea)) {
                    
                    if (blobArea > areaBig) {
                        areaBig = blobArea;
                        idBigBlob = id;
                    }
                    
                    //Discover width, height and bounding box using Point Cloud
                    ArrayList<IntPoint> lst = PointsCloud.GetBoundingRectangle(blobPoints);
                    int h = Math.abs(lst.get(0).x - lst.get(1).x);
                    int w = Math.abs(lst.get(0).y - lst.get(1).y);
                
                    blob = new Blob(id, blobArea, new IntPoint(xc/blobArea, yc/blobArea),blobPoints, new IntRectangle(lst.get(0).x, lst.get(0).y, w, h));
                    blobs.add(blob);
                    size++;
                    id++;
            }
        }else{
            if (blobArea > areaBig) {
                areaBig = blobArea;
                idBigBlob = id;
            }
            
            //Discover width, height and bounding box using Point Cloud
            ArrayList<IntPoint> lst = PointsCloud.GetBoundingRectangle(blobPoints);
            int h = Math.abs(lst.get(0).x - lst.get(1).x);
            int w = Math.abs(lst.get(0).y - lst.get(1).y);
            
            blob = new Blob(id, blobArea, new IntPoint(xc/blobArea, yc/blobArea),blobPoints, new IntRectangle(lst.get(0).x, lst.get(0).y, w, h));
            blobs.add(blob);
            size++;
            id++;
        }
    }
}