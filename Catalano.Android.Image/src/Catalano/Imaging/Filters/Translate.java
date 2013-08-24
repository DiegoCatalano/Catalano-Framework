/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.Imaging.Filters;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IBaseInPlace;

/**
 *
 * @author Diego Catalano
 */
public class Translate implements IBaseInPlace{
    
    private double sX = 0, sY = 0;

    public Translate() {}
    
    public Translate(double xTrans, double yTrans){
        this.sX = xTrans;
        this.sY = yTrans;
    }

    public double getXtrans() {
        return sX;
    }

    public void setXtrans(double sX) {
        this.sX = sX;
    }

    public double getYtrans() {
        return sY;
    }

    public void getYtrans(double sY) {
        this.sY = sY;
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                
            }
        }
    }
}