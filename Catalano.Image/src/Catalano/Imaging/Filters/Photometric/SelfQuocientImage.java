/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.Imaging.Filters.Photometric;

import Catalano.Imaging.FastBitmap;

/**
 *
 * @author Diego
 */
public class SelfQuocientImage implements IPhotometricFilter{
    
    private int size;
    private double sigma;

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public SelfQuocientImage() {
        this(5,1);
    }

    public SelfQuocientImage(int size, double sigma) {
        this.size = size;
        this.sigma = sigma;
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        if(!fastBitmap.isGrayscale())
            throw new IllegalArgumentException("Self Quocient Image only works in grayscale images.");
        
        
    }
    
}
