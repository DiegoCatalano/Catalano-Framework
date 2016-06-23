// Catalano Math Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Andrew Kirillov, 2005-2009
// andrew.kirillov@aforgenet.com
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

package Catalano.Math.Geometry;

import Catalano.Core.IntPoint;
import java.util.ArrayList;

/**
 * Performs math operations for Quadrilateral Transformation.
 * @author Diego Catalano
 */
public final class QuadrilateralTransformationCalc {
    
    private final static double TOLERANCE = 1e-13;

    /**
     * Don't let anyone instantiate this class.
     */
    private QuadrilateralTransformationCalc() {}
    
    // Calculates determinant of a 2x2 matrix
    private static double Det2( double a, double b, double c, double d ){
        return ( a * d - b * c );
    }
    
    // Multiply two 3x3 matrices
    private static double[][] MultiplyMatrix( double[][] a, double[][] b ){
        double[][] c = new double[3][3];

        c[0][0] = a[0][0] * b[0][0] + a[0][1] * b[1][0] + a[0][2] * b[2][0];
        c[0][1] = a[0][0] * b[0][1] + a[0][1] * b[1][1] + a[0][2] * b[2][1];
        c[0][2] = a[0][0] * b[0][2] + a[0][1] * b[1][2] + a[0][2] * b[2][2];
        c[1][0] = a[1][0] * b[0][0] + a[1][1] * b[1][0] + a[1][2] * b[2][0];
        c[1][1] = a[1][0] * b[0][1] + a[1][1] * b[1][1] + a[1][2] * b[2][1];
        c[1][2] = a[1][0] * b[0][2] + a[1][1] * b[1][2] + a[1][2] * b[2][2];
        c[2][0] = a[2][0] * b[0][0] + a[2][1] * b[1][0] + a[2][2] * b[2][0];
        c[2][1] = a[2][0] * b[0][1] + a[2][1] * b[1][1] + a[2][2] * b[2][1];
        c[2][2] = a[2][0] * b[0][2] + a[2][1] * b[1][2] + a[2][2] * b[2][2];

        return c;
    }
    
    // Calculates adjugate 3x3 matrix
    private static double[][] AdjugateMatrix( double[][] a ){
        
        double[][] b = new double[3][3];
        b[0][0] = Det2( a[1][1], a[1][2], a[2][1], a[2][2] );
        b[1][0] = Det2( a[1][2], a[1][0], a[2][2], a[2][0] );
        b[2][0] = Det2( a[1][0], a[1][1], a[2][0], a[2][1] );
        b[0][1] = Det2( a[2][1], a[2][2], a[0][1], a[0][2] );
        b[1][1] = Det2( a[2][2], a[2][0], a[0][2], a[0][0] );
        b[2][1] = Det2( a[2][0], a[2][1], a[0][0], a[0][1] );
        b[0][2] = Det2( a[0][1], a[0][2], a[1][1], a[1][2] );
        b[1][2] = Det2( a[0][2], a[0][0], a[1][2], a[1][0] );
        b[2][2] = Det2( a[0][0], a[0][1], a[1][0], a[1][1] );

        return b;
    }
    
    // Calculate matrix for unit square to quad mapping
    private static double[][] MapSquareToQuad( ArrayList<IntPoint> quad ){
        double[][] sq = new double[3][3];
        double px, py;

        px = quad.get(0).x - quad.get(1).x + quad.get(2).x - quad.get(3).x;
        py = quad.get(0).y - quad.get(1).y + quad.get(2).y - quad.get(3).y;

        if ( ( px < TOLERANCE ) && ( px > -TOLERANCE ) &&
             ( py < TOLERANCE ) && ( py > -TOLERANCE ) )
        {
            sq[0][0] = quad.get(1).x - quad.get(0).x;
            sq[0][1] = quad.get(2).x - quad.get(1).x;
            sq[0][2] = quad.get(0).x;

            sq[1][0] = quad.get(1).y - quad.get(0).y;
            sq[1][1] = quad.get(2).y - quad.get(1).y;
            sq[1][2] = quad.get(0).y;

            sq[2][0] = 0.0;
            sq[2][1] = 0.0;
            sq[2][2] = 1.0;
        }
        else
        {
            double dx1, dx2, dy1, dy2, del;

            dx1 = quad.get(1).x - quad.get(2).x;
            dx2 = quad.get(3).x - quad.get(2).x;
            dy1 = quad.get(1).y - quad.get(2).y;
            dy2 = quad.get(3).y - quad.get(2).y;

            del = Det2( dx1, dx2, dy1, dy2 );

            if ( del == 0.0 )
                return null;

            sq[2][0] = Det2( px, dx2, py, dy2 ) / del;
            sq[2][1] = Det2( dx1, px, dy1, py ) / del;
            sq[2][2] = 1.0;

            sq[0][0] = quad.get(1).x - quad.get(0).x + sq[2][0] * quad.get(1).x;
            sq[0][1] = quad.get(3).x - quad.get(0).x + sq[2][1] * quad.get(3).x;
            sq[0][2] = quad.get(0).x;

            sq[1][0] = quad.get(1).y - quad.get(0).y + sq[2][0] * quad.get(1).y;
            sq[1][1] = quad.get(3).y - quad.get(0).y + sq[2][1] * quad.get(3).y;
            sq[1][2] = quad.get(0).y;
        }
        return sq;
    }
    
    // Calculate matrix for general quad to quad mapping
    public static double[][] MapQuadToQuad( ArrayList<IntPoint> input, ArrayList<IntPoint> output ){
        double[][] squareToInpit = MapSquareToQuad( input );
        double[][] squareToOutput = MapSquareToQuad( output );

        if ( squareToOutput == null )
            return null;

        return MultiplyMatrix( squareToOutput, AdjugateMatrix( squareToInpit ) );
    }
}