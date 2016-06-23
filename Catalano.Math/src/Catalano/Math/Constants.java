// Catalano Math Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © César Souza, 2009-2012
// cesarsouza at gmail.com
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

// Contains functions from the Cephes Math Library Release 2.8:
// June, 2000 Copyright 1984, 1987, 1988, 2000 by Stephen L. Moshier
//
// Original license is listed below:
//
//   Some software in this archive may be from the book _Methods and
// Programs for Mathematical Functions_ (Prentice-Hall or Simon & Schuster
// International, 1989) or from the Cephes Mathematical Library, a
// commercial product. In either event, it is copyrighted by the author.
// What you see here may be used freely but it comes with no support or
// guarantee.
//
//   The two known misprints in the book are repaired here in the
// source listings for the gamma function and the incomplete beta
// integral.
//
//
//   Stephen L. Moshier
//   moshier@na-net.ornl.gov
//

package Catalano.Math;

/**
 * Common mathematical constants.
 * @author Diego Catalano
 */
public final class Constants {

    /**
     * Don't let anyone instantiate this class.
     */
    private Constants() {}
    
    /**
     * Euler-Mascheroni constant.
     */
    public final static double EulerGamma = 0.5772156649015328606065120;

    /**
     * Double-precision machine roundoff error.
     * This value is actually different from Double.Epsilon.
     */
    public final static double DoubleEpsilon = 1.11022302462515654042E-16;
    
    /**
     * Episilon constant. 4.94...
     */
    public final static double DoubleEpsilon2 = 4.94065645841247E-324;
    
    /**
     * Complementary Golden ratio.
     */
    public final static double ComplementaryGoldenRatio = 0.3819660112501051517954131;
    
    /**
     * Golden ratio constant.
     */
    public final static double GoldenRatio = 1.6180339887498948482045868;

    /**
     * Single-precision machine roundoff error.
     * This value is actually different from Single.Epsilon.
     */
    public final static float SingleEpsilon = 1.1920929E-07f;

    /**
     * Double-precision small value.
     */
    public final static double DoubleSmall = 1.493221789605150e-300;

    /**
     * Single-precision small value.
     */
    public final static float SingleSmall = 1.493221789605150e-40f;

    /**
     * Maximum log on the machine.
     */
    public final static double LogMax = 7.09782712893383996732E2;

    /**
     * Minimum log on the machine.
     */
    public final static double LogMin = -7.451332191019412076235E2;

    /**
     * Log of number pi: log(pi).
     */
    public final static double LogPI = 1.14472988584940017414;

    /**
     * Log of two: log(2).
     */
    public final static double Log2 = 0.69314718055994530941;
    
    /**
     * Log of three: log(3).
     */
    public final static double Log3 = 1.098612288668109691395;

    /**
     * Log of square root of twice number pi: log(sqrt(2*pi).
     */
    public final static double LogSqrt2PI = 0.91893853320467;

    /**
     * Log of twice number pi: log(2*pi).
     */
    public final static double Log2PI = 1.837877066409345483556;

    /**
     * Square root of twice number pi: sqrt(2*pi).
     */
    public final static double Sqrt2PI = 2.50662827463100050242E0;

    /**
     * Square root of half number pi: sqrt(pi/2).
     */
    public final static double SqrtHalfPI = 1.25331413731550025121E0;

    /**
     * Square root of 2: sqrt(2).
     */
    public final static double Sqrt2 = 1.4142135623730950488016887;
    
    /**
     * Square root of 3: sqrt(3).
     */
    public final static double Sqrt3 = 1.7320508075688772935274463;

    /**
     * Half square root of 2: sqrt(2)/2.
     */
    public final static double Sqrt2H = 7.07106781186547524401E-1;
}