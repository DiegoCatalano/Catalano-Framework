/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.Imaging.Tools;

/**
 * Interpolation routines.
 * @author Diego Catalano
 */
public final class Interpolation {

    /**
     * Don't let anyone instantiate this class.
     */
    private Interpolation() {}
    
    /**
     * Bicubic kernel.
     * 
     * <para>The function implements bicubic kernel W(x) as described on
     * http://en.wikipedia.org/wiki/Bicubic_interpolation#Bicubic_convolution_algorithm
     * (coefficient <b>a</b> is set to <b>-0.5</b>).</para>
     * 
     * @param x X value.
     * @return Bicubic cooefficient.
     */
    public static double BiCubicKernel( double x ){
        if ( x < 0 )
        {
            x = -x;
        }

        double biCoef = 0;

        if ( x <= 1 )
        {
            biCoef = ( 1.5 * x - 2.5 ) * x * x + 1;
        }
        else if ( x < 2 )
        {
            biCoef = ( ( -0.5 * x + 2.5 ) * x - 4 ) * x + 2;
        }

        return biCoef;
    }
    
}