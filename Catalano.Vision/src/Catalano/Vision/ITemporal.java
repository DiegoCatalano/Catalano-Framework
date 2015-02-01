/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Vision;

import Catalano.Imaging.FastBitmap;
import java.util.List;

/**
 *
 * @author Diego Catalano
 */
public interface ITemporal {
    FastBitmap Process(List<FastBitmap> sequenceImage);
}