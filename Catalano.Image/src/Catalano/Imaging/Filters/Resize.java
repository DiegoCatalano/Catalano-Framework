/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.Imaging.Filters;

import Catalano.Imaging.FastBitmap;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

/**
 *
 * @author Diego Catalano
 */
public class Resize {
    
    public static enum Algorithm{BILINEAR,BICUBIC,NEAREST_NEIGHBOR};
    private Algorithm algorithm;
    private int newWidth, newHeight;

    public Resize(int newWidth, int newHeight) {
        this.newWidth = newWidth;
        this.newHeight = newHeight;
        this.algorithm = Algorithm.NEAREST_NEIGHBOR;
    }
    
    public Resize(int newWidth, int newHeight, Algorithm algorithm) {
        this.newWidth = newWidth;
        this.newHeight = newHeight;
        this.algorithm = algorithm;
    }

    public int getNewHeight() {
        return newHeight;
    }

    public void setNewHeight(int newHeight) {
        this.newHeight = newHeight;
    }

    public int getNewWidth() {
        return newWidth;
    }

    public void setNewWidth(int newWidth) {
        this.newWidth = newWidth;
    }
    
    /**
     * Resize
     * @param fastBitmap
     */
    public void ApplyInPlace(FastBitmap fastBitmap){
        
        double sx = (double)newWidth*1.00/fastBitmap.getWidth();
        double sy = (double)newHeight*1.00/fastBitmap.getHeight();
        AffineTransform tx = new AffineTransform();
        AffineTransformOp op;
        tx.scale(sx, sy);
        switch(algorithm){
            case BILINEAR:
                op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
                break;
            case BICUBIC:
                op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BICUBIC);
                break;
            case NEAREST_NEIGHBOR:
                op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                break;
            default:
                op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        }
        fastBitmap.setImage(op.filter(fastBitmap.toBufferedImage(), null));
    }
}