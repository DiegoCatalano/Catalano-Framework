// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2013
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
}