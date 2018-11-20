// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2018
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
 * Class to handle with Illuminant / Tristimulus reference values.
 * @author Diego Catalano
 */
public class Illuminant {
    
    // XYZ (Tristimulus) Reference values of a perfect reflecting diffuser
    
    //2o Observer (CIE 1931)
    public static double[] CIE2_A = {109.850f, 100f, 35.585f}; //Incandescent
    public static double[] CIE2_C = {98.074f, 100f, 118.232f};
    public static double[] CIE2_D50 = {96.422f, 100f, 82.521f};
    public static double[] CIE2_D55 = {95.682f, 100f, 92.149f};
    public static double[] CIE2_D65 = {95.047f, 100f, 108.883f}; //Daylight
    public static double[] CIE2_D75 = {94.972f, 100f, 122.638f};
    public static double[] CIE2_F2 = {99.187f, 100f, 67.395f}; //Fluorescent
    public static double[] CIE2_F7 = {95.044f, 100f, 108.755f};
    public static double[] CIE2_F11 = {100.966f, 100f, 64.370f};
    
    //10o Observer (CIE 1964)
    public static double[] CIE10_A = {111.144f, 100f, 35.200f}; //Incandescent
    public static double[] CIE10_C = {97.285f, 100f, 116.145f};
    public static double[] CIE10_D50 = {96.720f, 100f, 81.427f};
    public static double[] CIE10_D55 = {95.799f, 100f, 90.926f};
    public static double[] CIE10_D65 = {94.811f, 100f, 107.304f}; //Daylight
    public static double[] CIE10_D75 = {94.416f, 100f, 120.641f};
    public static double[] CIE10_F2 = {103.280f, 100f, 69.026f}; //Fluorescent
    public static double[] CIE10_F7 = {95.792f, 100f, 107.687f};
    public static double[] CIE10_F11 = {103.866f, 100f, 65.627f};
    
    private Illuminant() {};
    
}