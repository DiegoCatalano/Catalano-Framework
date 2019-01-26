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
 * XYZ (Tristimulus) Reference values of a perfect reflecting diffuser.
 * @author Diego Catalano
 */
public class Illuminant {
    
    /**
     * 2o Observer (CIE 1931)
     */
    public static class CIE2{
        public static double[] A = {109.850f, 100f, 35.585f}; //Incandescent
        public static double[] C = {98.074f, 100f, 118.232f};
        public static double[] D50 = {96.422f, 100f, 82.521f};
        public static double[] D55 = {95.682f, 100f, 92.149f};
        public static double[] D65 = {95.047f, 100f, 108.883f}; //Daylight
        public static double[] D75 = {94.972f, 100f, 122.638f};
        public static double[] F2 = {99.187f, 100f, 67.395f}; //Fluorescent
        public static double[] F7 = {95.044f, 100f, 108.755f};
        public static double[] F11 = {100.966f, 100f, 64.370f};
    }
    
    /**
     * 10o Observer (CIE 1964)
     */
    public static class CIE10{
        public static double[] A = {111.144f, 100f, 35.200f}; //Incandescent
        public static double[] C = {97.285f, 100f, 116.145f};
        public static double[] D50 = {96.720f, 100f, 81.427f};
        public static double[] D55 = {95.799f, 100f, 90.926f};
        public static double[] D65 = {94.811f, 100f, 107.304f}; //Daylight
        public static double[] D75 = {94.416f, 100f, 120.641f};
        public static double[] F2 = {103.280f, 100f, 69.026f}; //Fluorescent
        public static double[] F7 = {95.792f, 100f, 107.687f};
        public static double[] F11 = {103.866f, 100f, 65.627f};
    }
    
    private Illuminant() {};
    
}