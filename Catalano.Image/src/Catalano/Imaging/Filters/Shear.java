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
public class Shear implements IBaseInPlace{
    
    public static enum Algorithm{BILINEAR,BICUBIC,NEAREST_NEIGHBOR};
    private Algorithm algorithm = Algorithm.NEAREST_NEIGHBOR;
    private double sX = 0, sY = 0;

    public Shear() {
        
    }
    
    public Shear(double xTrans, double yTrans){
        this.sX = xTrans;
        this.sY = yTrans;
    }
    
    public Shear(double xTrans, double yTrans, Algorithm algorithm) {
        this.sX = xTrans;
        this.sY = yTrans;
        this.algorithm = algorithm;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public double getXtrans() {
        return sX;
    }

    public void setXtrans(double sX) {
        this.sX = sX;
    }

    public double getYtrans() {
        return sY;
    }

    public void getYtrans(double sY) {
        this.sY = sY;
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap){
        AffineTransform tx = new AffineTransform();
        AffineTransformOp op;
        tx.shear(sY, sX);
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