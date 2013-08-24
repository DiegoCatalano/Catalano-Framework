/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.Imaging.Filters;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IBaseInPlace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

/**
 *
 * @author Diego Catalano
 */
public class Rotate implements IBaseInPlace{
    
    public static enum Algorithm{BILINEAR,BICUBIC,NEAREST_NEIGHBOR};
    private double angle;
    private Algorithm algorithm;

    public Rotate(Algorithm algorithm) {
        this.algorithm = algorithm;
    }
    
    public Rotate(double angle, Algorithm algorithm) {
        this.angle = angle;
        this.algorithm = algorithm;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
    
    /**
     * Resize
     * @param fastBitmap
     */
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        
        AffineTransform tx = new AffineTransform();
         tx.rotate(-angle * Math.PI / 180.0,width / 2,height / 2);
        
        AffineTransformOp op;
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