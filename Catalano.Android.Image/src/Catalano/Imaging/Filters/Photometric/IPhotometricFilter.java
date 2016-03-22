/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.Imaging.Filters.Photometric;

import Catalano.Imaging.FastBitmap;

/**
 *
 * @author Diego Catalano
 */
public interface IPhotometricFilter {
    public void applyInPlace(FastBitmap fastBitmap);
}
