// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © OpenIMAJ, 2018
// Delta-E is from the project above
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
    public static double DeltaC(double[] cieLab1, double[] cieLab2){
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
    public static double DeltaH(double[] cieLab1, double[] cieLab2){
        double xDE = DeltaC(cieLab1, cieLab2);
        
        double part1 = Math.pow((cieLab2[1] - cieLab1[1]),2);
        double part2 = Math.pow((cieLab2[2] - cieLab1[2]),2);
        
        double result = part1 + part2 - (xDE * xDE);
        return Math.sqrt(result);
    }
    
    /**
     * Delta-E
     * Calculate "distance" between two colors.
     * @param cieLab1 CIE-L*A*B* color space.
     * @param cieLab2 CIE-L*A*B* color space.
     * @return Distance.
     */
    public static double DeltaE(double[] cieLab1, double[] cieLab2){
        return DeltaE(cieLab1[0], cieLab1[1], cieLab1[2], cieLab2[0], cieLab2[1], cieLab2[2]);
    }
    
    /**
     * Delta-E
     * Calculate "distance" between two colors.
     * @param l1 L* component.
     * @param a1 A* component.
     * @param b1 B* component.
     * @param l2 L* component.
     * @param a2 A* component.
     * @param b2 B* component.
     * @return Distance between the two colors.
     */
    public static double DeltaE(double l1, double a1, double b1, double l2, double a2, double b2){
        
        double C1 =  Math.sqrt(a1*a1 + b1*b1);
        double C2 =  Math.sqrt(a2*a2 + b2*b2);
        
        double lMean = (l1 + l2) / 2.0;
        double cMean = (C1 + C2) / 2.0;

        double G =  ( 1 - Math.sqrt( Math.pow(cMean, 7) / (Math.pow(cMean, 7) + Math.pow(25, 7)) ) ) / 2;
        double a1prime = a1 * (1 + G);
        double a2prime = a2 * (1 + G);
        double C1prime =  Math.sqrt(a1prime*a1prime + b1*b1);
        double C2prime =  Math.sqrt(a2prime*a2prime + b2*b2);
        double Cmeanprime = (C1prime + C2prime) / 2;

        double h1prime =  Math.atan2(b1, a1prime) + 2*Math.PI * (Math.atan2(b1, a1prime)<0 ? 1 : 0);
        double h2prime =  Math.atan2(b2, a2prime) + 2*Math.PI * (Math.atan2(b2, a2prime)<0 ? 1 : 0);
        double Hmeanprime =  ((Math.abs(h1prime - h2prime) > Math.PI) ? (h1prime + h2prime + 2*Math.PI) / 2 : (h1prime + h2prime) / 2);

        double T =  1.0 - 0.17 * Math.cos(Hmeanprime - Math.PI/6.0) + 0.24 * Math.cos(2*Hmeanprime) + 0.32 * Math.cos(3*Hmeanprime + Math.PI/30) - 0.2 * Math.cos(4*Hmeanprime - 21*Math.PI/60);

        double deltahprime =  ((Math.abs(h1prime - h2prime) <= Math.PI) ? h2prime - h1prime : (h2prime <= h1prime) ? h2prime - h1prime + 2*Math.PI : h2prime - h1prime - 2*Math.PI); 

        double deltaLprime = l2 - l1;
        double deltaCprime = C2prime - C1prime;
        double deltaHprime =  2.0 * Math.sqrt(C1prime*C2prime) * Math.sin(deltahprime / 2.0);
        double SL =  1.0 + ( (0.015*(lMean - 50)*(lMean - 50)) / (Math.sqrt( 20 + (lMean - 50)*(lMean - 50) )) );
        double SC =  1.0 + 0.045 * Cmeanprime;
        double SH =  1.0 + 0.015 * Cmeanprime * T;

        double deltaTheta =  (30 * Math.PI / 180) * Math.exp(-((180/Math.PI*Hmeanprime-275)/25)*((180/Math.PI*Hmeanprime-275)/25));
        double RC =  (2 * Math.sqrt(Math.pow(Cmeanprime, 7) / (Math.pow(Cmeanprime, 7) + Math.pow(25, 7))));
        double RT =  (-RC * Math.sin(2 * deltaTheta));

        double KL = 1;
        double KC = 1;
        double KH = 1;

        double deltaE = Math.sqrt(
                        ((deltaLprime/(KL*SL)) * (deltaLprime/(KL*SL))) +
                        ((deltaCprime/(KC*SC)) * (deltaCprime/(KC*SC))) +
                        ((deltaHprime/(KH*SH)) * (deltaHprime/(KH*SH))) +
                        (RT * (deltaCprime/(KC*SC)) * (deltaHprime/(KH*SH)))
                        );

        return deltaE;
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
    public static double DeltaCMC(double[] cieLab1, double[] cieLab2, double wheightL, double wheightC){
        
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
    
    private static double CieLab2Hue(double a, double b){
        
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