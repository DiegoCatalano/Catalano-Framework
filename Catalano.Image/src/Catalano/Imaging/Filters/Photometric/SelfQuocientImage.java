/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.Imaging.Filters.Photometric;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Tools.ImageUtils;
import Catalano.Imaging.Tools.Kernel;
import Catalano.Math.Functions.Gaussian;
import Catalano.Math.PaddingMatrix;

/**
 *
 * @author Diego
 */
public class SelfQuocientImage implements IPhotometricFilter{
    
    private int size;
    private double sigma;
    private double[][] vectors;

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
        BuildKernel();
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
        BuildKernel();
    }

    public SelfQuocientImage() {
        this(5,1);
    }

    public SelfQuocientImage(int size, double sigma) {
        this.size = size;
        this.sigma = sigma;
        BuildKernel();
    }
    
    private void BuildKernel(){
        Gaussian g = new Gaussian(sigma);
        this.vectors = Kernel.Decompose(g.Kernel2D(size));
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        if(!fastBitmap.isGrayscale())
            throw new IllegalArgumentException("Self Quocient Image only works in grayscale images.");
        
        double[][] image = fastBitmap.toMatrixGrayAsDouble();
        
        double[][] result = Process(image, true);
        
        
        
        
    }
    
    public double[][] Process(double[][] image, boolean normalize){
        
        //Normalize the image
        ImageUtils.Normalize(image);
        
        //Apply gaussian smoothing
        double[][] filt1 = ImageUtils.Convolution(image, vectors[0], vectors[1]);
        
        //Image padding
        int padSize = (int)Math.floor(size/2.0);
        PaddingMatrix pad = new PaddingMatrix(padSize, padSize, true);
        double[][] padX = pad.Create(image);
        
        
    }
    
}
