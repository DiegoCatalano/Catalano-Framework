/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Imaging.Shapes;

import Catalano.Core.IntPoint;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Diego Catalano
 */
public class IntPolygon {
    
    private int[] xPoints;
    private int[] yPoints;
    private int nPoints;

    public int[] getX() {
        if(xPoints.length == nPoints)
            return xPoints;
        return Arrays.copyOf(xPoints, nPoints);
    }

    public int[] getY() {
        if(yPoints.length == nPoints)
            return yPoints;
        return Arrays.copyOf(yPoints, nPoints);
    }

    public int getNPoints() {
        return nPoints;
    }
    
    public List<IntPoint> getPoints(){
        List<IntPoint> lst = new ArrayList<IntPoint>();
        for (int i = 0; i < nPoints; i++) {
            lst.add(new IntPoint(xPoints[i], yPoints[i]));
        }
        return lst;
    }

    public IntPolygon() {
        this.xPoints = new int[4];
        this.yPoints = new int[4];
        this.nPoints = 0;
    }
    
    public IntPolygon(int[] x, int[] y){
        if(x.length != y.length)
            throw new IllegalArgumentException("The size of xPoints and yPoints must be the same.");
        
        this.xPoints = Arrays.copyOf(x, x.length);
        this.yPoints = Arrays.copyOf(y, y.length);
        this.nPoints = x.length;
    }
    
    public IntPolygon(int[] x, int[] y, int nPoints){
        if(x.length < nPoints || y.length < nPoints)
            throw new IllegalArgumentException("the number of points is higher than lenght of xPoints or yPoints.");
        
        if(nPoints < 0)
            throw new NegativeArraySizeException("nPoints < 0");
        
        this.xPoints = Arrays.copyOf(x, nPoints);
        this.yPoints = Arrays.copyOf(y, nPoints);
        this.nPoints = nPoints;
    }
    
    public IntPolygon(List<IntPoint> lstPoints){
        
        this.nPoints = lstPoints.size();
        this.xPoints = new int[lstPoints.size()];
        this.yPoints = new int[lstPoints.size()];
        
        for (int i = 0; i < lstPoints.size(); i++) {
            xPoints[i] = lstPoints.get(i).x;
            yPoints[i] = lstPoints.get(i).y;
        }
    }
    
    public IntPolygon(List<IntPoint> lstPoints, int nPoints){
        
        if(lstPoints.size() < nPoints)
            throw new IllegalArgumentException("the number of points is higher than lenght of lstPoints.");
        
        if(nPoints < 0)
            throw new NegativeArraySizeException("nPoints < 0");
        
        this.nPoints = nPoints;
        this.xPoints = new int[nPoints];
        this.yPoints = new int[nPoints];
        
        for (int i = 0; i < nPoints; i++) {
            xPoints[i] = lstPoints.get(i).x;
            yPoints[i] = lstPoints.get(i).y;
        }
    }
    
    public void addPoint(int x, int y){
        
        if(nPoints >= xPoints.length || nPoints >= yPoints.length){
            int newSize = nPoints * 2;
            if(!Catalano.Math.Tools.isPowerOf2(newSize))
                newSize = Integer.highestOneBit(newSize);
            
            xPoints = Arrays.copyOf(xPoints, newSize);
            yPoints = Arrays.copyOf(yPoints, newSize);
            
        }
        
        xPoints[nPoints] = x;
        yPoints[nPoints] = y;
        nPoints++;
        
    }
}