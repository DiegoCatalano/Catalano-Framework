/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.Imaging.Texture.BinaryPattern;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Tools.ImageHistogram;

/**
 * Common interface to related binary pattern algorithms.
 * @author Diego Catalano
 */
public interface IBinaryPattern {
    
    /**
     * Compute binary pattern.
     * @param fastBitmap Image to be processed.
     * @return Binary pattern.
     */
    public ImageHistogram ProcessImage(FastBitmap fastBitmap);
}