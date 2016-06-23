// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
//    This library is free software; you can redistribute it and/or
//    modify it under the terms of the GNU Lesser General Public
//    License as published by the Free Software Foundation; either
//    version 2.1 of the License, or (at your option) any later version.
//
//    This library is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//    Lesser General Public License for more details.
//
//    You should have received a copy of the GNU Lesser General Public
//    License along with this library; if not, write to the Free Software
//    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
//

package Catalano.Imaging.Tools;

import Catalano.Math.Tools;

/**
 * Calculate color differences.
 * All of them refers to the CIE-L*ab color space.
 * @author Diego Catalano
 */
public class ColorDifference {

    /**
     * Don't let anyone instantiate this class.
     */
    private ColorDifference() {}
    
    /**
     * Delta-C
     * Calculate "distance" between two colors.
     * @param cieLab1 CIE-LAB color space.
     * @param cieLab2 CIE-LAB color space.
     * @return Distance.
     */
    public static double DeltaC(float[] cieLab1, float[] cieLab2){
        double part1 = Math.sqrt(Math.pow(cieLab2[1], 2) + Math.pow(cieLab2[2], 2));
        double part2 = Math.sqrt(Math.pow(cieLab1[1], 2) + Math.pow(cieLab1[2], 2));
        
        return part1 - part2;
    }
    
    /**
     * Delta-H
     * Calculate "distance" between two colors.
     * @param cieLab1 CIE-LAB color space.
     * @param cieLab2 CIE-LAB color space.
     * @return Distance.
     */
    public static double DeltaH(float[] cieLab1, float[] cieLab2){
        double xDE = DeltaC(cieLab1, cieLab2);
        
        double part1 = Math.pow((cieLab2[1] - cieLab1[1]),2);
        double part2 = Math.pow((cieLab2[2] - cieLab1[2]),2);
        
        double result = part1 + part2 - (xDE * xDE);
        return Math.sqrt(result);
    }
    
    /**
     * Delta-E
     * Calculate "distance" between two colors.
     * @param cieLab1 CIE-LAB color space.
     * @param cieLab2 CIE-LAB color space.
     * @return Distance.
     */
    public static double DeltaE(float[] cieLab1, float[] cieLab2){
        double result = Math.sqrt(
                Math.pow(cieLab1[0] - cieLab2[0],2) +
                Math.pow(cieLab1[1] - cieLab2[1],2) +
                Math.pow(cieLab1[2] - cieLab2[2],2)
        );
        return result;
    }
    
    /**
     * Delta CMC
     * Calculate "distance" between two colors.
     * @param cieLab1 CIE-LAB color space.
     * @param cieLab2 CIE-LAB color space.
     * @param wheightL Wheight L.
     * @param wheightC Wheight C.
     * @return Distance.
     */
    public static double DeltaCMC(float[] cieLab1, float[] cieLab2, float wheightL, float wheightC){
        
        double xc1 = Math.sqrt(cieLab1[1]*cieLab1[1] + cieLab1[2]*cieLab1[2]);
        double xc2 = Math.sqrt(cieLab2[1]*cieLab2[1] + cieLab2[2]*cieLab2[2]);
        
        double xc1_4 = xc1*xc1*xc1*xc1;
        double xff = Math.sqrt(xc1_4 / (xc1_4 + 1900));
        double xh1 = CieLab2Hue(cieLab1[1], cieLab1[2]);
        
        double xTT;
        if ( xh1 < 164 || xh1 > 345 )
            xTT = 0.36D + Math.abs( 0.4 * Math.cos( 35 + xh1 ) );
        else
            xTT = 0.56D + Math.abs( 0.2 * Math.cos( 168 + xh1 ) );
        
        double xSL;
        if ( cieLab1[0] < 16 )
            xSL = 0.511D;
        else
            xSL = ( 0.040975D * cieLab1[0] ) / ( 1.0 + ( 0.01765D * cieLab1[0] ) );
        
        double xSC = ( ( 0.0638D * xc1 ) / ( 1 + ( 0.0131D * xc1 ) ) ) + 0.638D;
        double xSH = ( ( xff * xTT ) + 1 - xff ) * xSC;
        double xDH = Math.sqrt( Math.pow(cieLab2[1] - cieLab1[1], 2) + Math.pow(cieLab2[2] - cieLab1[2], 2) - Math.pow(xc2 - xc1, 2));
        
        xSL = ( cieLab2[0] - cieLab1[0] ) / wheightL * xSL;
        xSC = ( xc2 - xc1 ) / wheightL * xSC;
        xSH = xDH / xSH;
        return Math.sqrt( xSL*xSL + xSC*xSC + xSH*xSH );
        
    }
    
    private static double CieLab2Hue(float a, float b){
        
        if (a >= 0 && b == 0) return 0;
        if (a < 0 && b == 0) return 180;
        if (a == 0 && b > 0) return 90;
        if (a == 0 && b < 0) return 270;
        
        double var = 0;
        if (a > 0 && b > 0) var = 0;
        if (a < 0) var = 180;
        if (a > 0 && b < 0) var = 360;
        
        double atan = Math.atan2(b, a);
        return Math.toRadians(atan) + var;
        
    }
}