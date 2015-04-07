/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Vision.Detection;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Shapes.IntRectangle;
import java.util.List;

/**
 *
 * @author Diego
 */
public interface IObjectDetector {
    
    List<IntRectangle> DetectedObjects();
    List<IntRectangle> ProcessFrame(FastBitmap fastBitmap);
    
}
