/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Signal;

/**
 *
 * @author Diego Catalano
 */
public class Convolution {
    
    public enum Mode {Same, Valid};
    private Mode mode = Mode.Valid;

    public Convolution() {}
    
    public Convolution(Mode mode){
        this.mode = mode;
    }
    
    public double[][] Process(double[][] signal, double[] convolve){
        
        int n;
        switch(mode){
            case Same:
                n = signal.length - convolve.length + 1;
                if (n > 0){
                    double[][] result = new double[signal.length][signal[0].length];
                    for (int i = 0; i < result.length; i++) {
                        double r = 0;
                        for (int j = 0; j < convolve.length; j++) {
                            r += signal[i+j] * convolve[convolve.length -j -1];
                        }
                        result[i] = r;
                    }
                }
                return result;
                else{
                    throw new IllegalArgumentException("The convolve lenght must be > or = of the signal");
                }
            break;
            case Valid:
                n = signal.length - convolve.length + 1;
                if (n > 0){
                    result = new double[signal.length - convolve.length + 1];
                    for (int i = 0; i < result.length; i++) {
                        double r = 0;
                        for (int j = 0; j < convolve.length; j++) {
                            r += signal[i+j] * convolve[convolve.length -j -1];
                        }
                        result[i] = r;
                    }
                }
                else{
                    throw new IllegalArgumentException("The convolve lenght must be > or = of the signal");
                }
            break;
        }
        
        return result;
        
    }
    
    
    
}
