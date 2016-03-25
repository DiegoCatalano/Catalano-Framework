/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.Imaging.Filters.Photometric;

import Catalano.Imaging.FastBitmap;
import Catalano.Statistics.Tools;

/**
 *
 * @author Diego
 */
public class RobustPostprocessor {
    
    private double alfa;
    private double tao;

    public RobustPostprocessor() {
        this(0.1,10);
    }

    public RobustPostprocessor(double alfa, double tao) {
        this.alfa = alfa;
        this.tao = tao;
    }
    
    public void ApplyInPlace(FastBitmap fastBitmap){
        
    }
    
    public double[][] Apply(double[][] image){
        
        //First stage normalization
        double mean = 0;
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                mean += Math.pow(Math.abs(image[i][j]), alfa);;
            }
        }
        
        mean /= (double)(image.length * image[0].length);
        mean = Math.pow(mean, 1.0 / alfa);
        
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                image[i][j] /= mean;
            }
        }
        
        //Second stage normalization
        mean = 0;
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                mean += Math.pow(Math.min(Math.abs(image[i][j]), tao), alfa);;
            }
        }
        
        mean /= (double)(image.length * image[0].length);
        mean = Math.pow(mean, 1.0 / alfa);
        
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                image[i][j] /= mean;
            }
        }
        
        //Nonlinear mapping
        double[][] result = new double[image.length][image[0].length];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                result[i][j] = tao * Math.tanh(image[i][j] / tao);
            }
        }
        
        return result;
    }
}