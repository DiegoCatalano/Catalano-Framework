/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Signal;

import Catalano.Math.ComplexNumber;

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
    
    public double[][] Process(double[][] signal, double[][] kernel){
        
        double[][] result = null;
        int width;
        int height;
        int lineI;
        int lineJ;
        int Xline;
        int Yline;
        switch(mode){
            case Same:
                    width = signal[0].length;
                    height = signal[1].length;

                    lineI = (kernel.length - 1) / 2;
                    lineJ = (kernel[0].length - 1) / 2;
                    result = new double[height][width];
                    for (int i = 0; i < height; i++)
                    {
                        double conv = 0;
                        for (int j = 0; j < width; j++)
                        {
                            for (int k = 0; k < kernel.length; k++)
                            {
                                Xline = i + (k - lineI);
                                for (int l = 0; l < kernel[0].length; l++)
                                {
                                    Yline = j + (l - lineJ);
                                    if ((Xline >= 0) && (Xline < height) && (Yline >= 0) && (Yline < width))
                                    {
                                        conv += result[Xline][Yline] * kernel[kernel.length - k - 1][kernel[0].length - l - 1];
                                    }
                                }
                            }
                            result[i][j] = conv;
                        }
                    }
                break;
            case Valid:
                    width = signal[0].length - kernel[0].length + 1;
                    height = signal.length - kernel.length + 1;

                    result = new double[height][width];
                    for (int i = 0; i < height; i++)
                    {
                        for (int j = 0; j < width; j++)
                        {

                            double conv = 0;
                            for (int k = 0; k < kernel.length; k++)
                            {
                                for (int l = 0; l < kernel[0].length; l++)
                                {
                                    conv += signal[i+k][j+l] * kernel[kernel.length - k - 1][kernel[0].length - l - 1];
                                }
                            }
                            result[i][j] = conv;
                        }
                    }
            break;
        }
        return result;
    }
    
    public ComplexNumber[][] Process(ComplexNumber[][] signal, ComplexNumber[][] kernel){
        
        ComplexNumber[][] result = null;
        int width;
        int height;
        int lineI;
        int lineJ;
        int Xline;
        int Yline;
        switch(mode){
            case Same:
                    width = signal[0].length;
                    height = signal[1].length;

                    lineI = (kernel.length - 1) / 2;
                    lineJ = (kernel[0].length - 1) / 2;
                    result = new ComplexNumber[height][width];
                    for (int i = 0; i < height; i++)
                    {
                        ComplexNumber conv = new ComplexNumber(0, 0);
                        for (int j = 0; j < width; j++)
                        {
                            for (int k = 0; k < kernel.length; k++)
                            {
                                Xline = i + (k - lineI);
                                for (int l = 0; l < kernel[0].length; l++)
                                {
                                    Yline = j + (l - lineJ);
                                    if ((Xline >= 0) && (Xline < height) && (Yline >= 0) && (Yline < width))
                                    {
                                        conv = ComplexNumber.Add(conv,
                                               ComplexNumber.Multiply(result[Xline][Yline], kernel[kernel.length - k - 1][kernel[0].length - l - 1]));
                                    }
                                }
                            }
                            result[i][j] = conv;
                        }
                    }
                break;
            case Valid:
                    width = signal[0].length - kernel[0].length + 1;
                    height = signal.length - kernel.length + 1;

                    result = new ComplexNumber[height][width];
                    for (int i = 0; i < height; i++)
                    {
                        for (int j = 0; j < width; j++)
                        {

                            ComplexNumber conv = new ComplexNumber(0, 0);
                            for (int k = 0; k < kernel.length; k++)
                            {
                                for (int l = 0; l < kernel[0].length; l++)
                                {
                                    conv = ComplexNumber.Add(conv,
                                           ComplexNumber.Multiply(result[i+k][j+l], kernel[kernel.length - k - 1][kernel[0].length - l - 1]));
                                }
                            }
                            result[i][j] = conv;
                        }
                    }
            break;
        }
        return result;
    }
    
}