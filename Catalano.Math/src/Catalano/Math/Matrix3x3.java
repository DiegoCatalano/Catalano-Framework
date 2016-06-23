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

package Catalano.Math;

/**
 * A class representing 3x3 matrix.
 * 
 * <para>The structure incapsulates elements of a 3x3 matrix and
 * provides some operations with it.</para>
 * 
 * @author Diego Catalano
 */
public class Matrix3x3 {
    
    /**
     * Row 0 column 0 element of the matrix.
     */
    public float V00;
    
    /**
     * Row 0 column 1 element of the matrix.
     */
    public float V01;
    
    /**
     * Row 0 column 2 element of the matrix.
     */
    public float V02;

    /**
     * Row 1 column 0 element of the matrix.
     */
    public float V10;
    
    /**
     * Row 1 column 1 element of the matrix.
     */
    public float V11;
    
    /**
     * Row 1 column 2 element of the matrix.
     */
    public float V12;

    /**
     * Row 2 column 0 element of the matrix.
     */
    public float V20;
    
    /**
     * Row 2 column 1 element of the matrix.
     */
    public float V21;
    
    /**
     * Row 2 column 2 element of the matrix.
     */
    public float V22;

    /**
     * Initializes a new instance of the Matrix3x3 class.
     */
    public Matrix3x3() {
        this.V00 = V01 = V02 = 0;
        this.V10 = V11 = V12 = 0;
        this.V22 = V21 = V22 = 0;
    }

    /**
     * Initializes a new instance of the Matrix3x3 class.
     * @param V00 Row 0 column 0 element of the matrix.
     * @param V01 Row 0 column 1 element of the matrix.
     * @param V02 Row 0 column 2 element of the matrix.
     * @param V10 Row 1 column 0 element of the matrix.
     * @param V11 Row 1 column 1 element of the matrix.
     * @param V12 Row 1 column 2 element of the matrix.
     * @param V20 Row 2 column 0 element of the matrix.
     * @param V21 Row 2 column 1 element of the matrix.
     * @param V22 Row 2 column 2 element of the matrix.
     */
    public Matrix3x3(float V00, float V01, float V02, float V10, float V11, float V12, float V20, float V21, float V22) {
        this.V00 = V00;
        this.V01 = V01;
        this.V02 = V02;
        this.V10 = V10;
        this.V11 = V11;
        this.V12 = V12;
        this.V20 = V20;
        this.V21 = V21;
        this.V22 = V22;
    }
    
    /**
     * Provides an identity matrix with all diagonal elements set to 1.
     * @return Identity matrix.
     */
    public static Matrix3x3 Identity(){
        return new Matrix3x3(1, 0, 0, 0, 1, 0, 0, 0, 1);
    }
    
    /**
     * Calculates determinant of the matrix.
     * @return Determinant.
     */
    public float Determinant(){
        return V00 * V11 * V22 + V01 * V12 * V20 + V02 * V10 * V21 -
               V00 * V12 * V21 - V01 * V10 * V22 - V02 * V11 * V20;
    }
    
    /**
     * Returns array representation of the matrix.
     * @return Returns array which contains all elements of the matrix in the row-major order.
     */
    public float[] toArray(){
        return new float[] {V00, V01, V02, V10, V11, V12, V20, V21, V22};
    }
    
    /**
     * Creates rotation matrix around Y axis.
     * @param radians Rotation angle around Y axis in radians.
     * @return Returns rotation matrix to rotate an object around Y axis.
     */
    public static Matrix3x3 CreateRotationY( float radians ){
        Matrix3x3 m = new Matrix3x3( );

        float cos = (float) Math.cos( radians );
        float sin = (float) Math.sin( radians );

        m.V00 = m.V22 = cos;
        m.V02 = sin;
        m.V20 = -sin;
        m.V11 = 1;

        return m;
    }
    
    /**
     * Creates rotation matrix around X axis.
     * @param radians Rotation angle around X axis in radians.
     * @return Returns rotation matrix to rotate an object around X axis.
     */
    public static Matrix3x3 CreateRotationX( float radians ){
        Matrix3x3 m = new Matrix3x3( );

        float cos = (float) Math.cos( radians );
        float sin = (float) Math.sin( radians );

        m.V11 = m.V22 = cos;
        m.V12 = -sin;
        m.V21 = sin;
        m.V00 = 1;

        return m;
    }
    
    /**
     * Creates rotation matrix around Z axis.
     * @param radians Rotation angle around Z axis in radians.
     * @return Returns rotation matrix to rotate an object around Z axis.
     */
    public static Matrix3x3 CreateRotationZ( float radians ){
        Matrix3x3 m = new Matrix3x3( );

        float cos = (float) Math.cos( radians );
        float sin = (float) Math.sin( radians );

        m.V00 = m.V11 = cos;
        m.V01 = -sin;
        m.V10 = sin;
        m.V22 = 1;

        return m;
    }
    
    /**
     * Creates rotation matrix to rotate an object around X, Y and Z axes.
     * 
     * <para>The routine assumes roll-pitch-yaw rotation order, when creating rotation
     * matrix, i.e. an object is first rotated around Z axis, then around X axis and finally around
     * Y axis.</para>
     * 
     * @param yaw Rotation angle around Y axis in radians.
     * @param pitch Rotation angle around X axis in radians.
     * @param roll Rotation angle around Z axis in radians.
     * @return Returns rotation matrix to rotate an object around all 3 axes.
     */
    public static Matrix3x3 CreateFromYawPitchRoll( float yaw, float pitch, float roll ){
        
        Matrix3x3 r = Matrix3x3.Multiply(CreateRotationY( yaw ), CreateRotationX( pitch ));
        r.Multiply(CreateRotationZ( roll ));
        
        return r;
    }
    
    /**
     * Extract rotation angles from the rotation matrix.
     * 
     * <para>The routine assumes roll-pitch-yaw rotation order when extracting rotation angle.
     * Using extracted angles with the @see CreateFromYawPitchRoll should provide same rotation matrix.</para>
     * 
     * <para>The method assumes the provided matrix represent valid rotation matrix.</para>
     * 
     * @return 
     */
    public float[] ExtractYawPitchRoll()
    {
        float[] v = new float[3];
        v[0] = (float) Math.atan2( V02, V22 );
        v[1] = (float) Math.asin( -V12 );
        v[2] = (float) Math.atan2( V10, V11 );
        return v;
    }
    
    /**
     * Creates a matrix from 3 rows specified as vectors.
     * @param row0 First row of the matrix to create.
     * @param row1 Second row of the matrix to create.
     * @param row2 Third row of the matrix to create.
     * @return Returns a matrix from specified rows.
     */
    public static Matrix3x3 CreateFromRows( Vector3 row0, Vector3 row1, Vector3 row2 ){
        Matrix3x3 m = new Matrix3x3( );

        m.V00 = row0.x;
        m.V01 = row0.y;
        m.V02 = row0.z;

        m.V10 = row1.x;
        m.V11 = row1.y;
        m.V12 = row1.z;

        m.V20 = row2.x;
        m.V21 = row2.y;
        m.V22 = row2.z;

        return m;
    }
    
    /**
     * Creates a matrix from 3 columns specified as vectors.
     * @param column0 First column of the matrix to create.
     * @param column1 Second column of the matrix to create.
     * @param column2 Third column of the matrix to create.
     * @return Returns a matrix from specified columns.
     */
    public static Matrix3x3 CreateFromColumns( Vector3 column0, Vector3 column1, Vector3 column2 ){
        Matrix3x3 m = new Matrix3x3( );

        m.V00 = column0.x;
        m.V10 = column0.y;
        m.V20 = column0.z;

        m.V01 = column1.x;
        m.V11 = column1.y;
        m.V21 = column1.z;

        m.V02 = column2.x;
        m.V12 = column2.y;
        m.V22 = column2.z;

        return m;
    }
    
    /**
     * Creates a diagonal matrix using the specified vector as diagonal elements.
     * @param vector Vector to use for diagonal elements of the matrix.
     * @return Returns a diagonal matrix.
     */
    public static Matrix3x3 CreateDiagonal( Vector3 vector ){
        Matrix3x3 m = new Matrix3x3( );

        m.V00 = vector.x;
        m.V11 = vector.y;
        m.V22 = vector.z;

        return m;
    }
    
    public void Multiply(Matrix3x3 matrix){
        this.V00 = V00 * matrix.V00 + V01 * matrix.V10 + V02 * matrix.V20;
        this.V01 = V00 * matrix.V01 + V01 * matrix.V11 + V02 * matrix.V21;
        this.V02 = V00 * matrix.V02 + V01 * matrix.V12 + V02 * matrix.V22;

        this.V10 = V10 * matrix.V00 + V11 * matrix.V10 + V12 * matrix.V20;
        this.V11 = V10 * matrix.V01 + V11 * matrix.V11 + V12 * matrix.V21;
        this.V12 = V10 * matrix.V02 + V11 * matrix.V12 + V12 * matrix.V22;

        this.V20 = V20 * matrix.V00 + V21 * matrix.V10 + V22 * matrix.V20;
        this.V21 = V20 * matrix.V01 + V21 * matrix.V11 + V22 * matrix.V21;
        this.V22 = V20 * matrix.V02 + V21 * matrix.V12 + V22 * matrix.V22;
    }
    
    /**
     *  Multiplies two specified matrices.
     * @param matrix1 Matrix to multiply.
     * @param matrix2 Matrix to multiply by.
     * @return Return new matrix, which the result of multiplication of the two specified matrices.
     */
    public static Matrix3x3 Multiply(Matrix3x3 matrix1, Matrix3x3 matrix2){
        Matrix3x3 m = new Matrix3x3( );

        m.V00 = matrix1.V00 * matrix2.V00 + matrix1.V01 * matrix2.V10 + matrix1.V02 * matrix2.V20;
        m.V01 = matrix1.V00 * matrix2.V01 + matrix1.V01 * matrix2.V11 + matrix1.V02 * matrix2.V21;
        m.V02 = matrix1.V00 * matrix2.V02 + matrix1.V01 * matrix2.V12 + matrix1.V02 * matrix2.V22;

        m.V10 = matrix1.V10 * matrix2.V00 + matrix1.V11 * matrix2.V10 + matrix1.V12 * matrix2.V20;
        m.V11 = matrix1.V10 * matrix2.V01 + matrix1.V11 * matrix2.V11 + matrix1.V12 * matrix2.V21;
        m.V12 = matrix1.V10 * matrix2.V02 + matrix1.V11 * matrix2.V12 + matrix1.V12 * matrix2.V22;

        m.V20 = matrix1.V20 * matrix2.V00 + matrix1.V21 * matrix2.V10 + matrix1.V22 * matrix2.V20;
        m.V21 = matrix1.V20 * matrix2.V01 + matrix1.V21 * matrix2.V11 + matrix1.V22 * matrix2.V21;
        m.V22 = matrix1.V20 * matrix2.V02 + matrix1.V21 * matrix2.V12 + matrix1.V22 * matrix2.V22;

        return m;
    }
    
    public void Add(Matrix3x3 matrix){
        this.V00 = V00 + matrix.V00;
        this.V01 = V01 + matrix.V01;
        this.V02 = V02 + matrix.V02;

        this.V10 = V10 + matrix.V10;
        this.V11 = V11 + matrix.V11;
        this.V12 = V12 + matrix.V12;

        this.V20 = V20 + matrix.V20;
        this.V21 = V21 + matrix.V21;
        this.V22 = V22 + matrix.V22;
    }
    
    /**
     * Adds corresponding components of two matrices.
     * @param matrix1 The matrix to add to.
     * @param matrix2 The matrix to add to the first matrix.
     * @return Returns a matrix which components are equal to sum of corresponding components of the two specified matrices.
     */
    public static Matrix3x3 Add(Matrix3x3 matrix1, Matrix3x3 matrix2){
        Matrix3x3 m = new Matrix3x3( );

        m.V00 = matrix1.V00 + matrix2.V00;
        m.V01 = matrix1.V01 + matrix2.V01;
        m.V02 = matrix1.V02 + matrix2.V02;

        m.V10 = matrix1.V10 + matrix2.V10;
        m.V11 = matrix1.V11 + matrix2.V11;
        m.V12 = matrix1.V12 + matrix2.V12;

        m.V20 = matrix1.V20 + matrix2.V20;
        m.V21 = matrix1.V21 + matrix2.V21;
        m.V22 = matrix1.V22 + matrix2.V22;

        return m;
    }
    
    public void Subtract(Matrix3x3 matrix){
        this.V00 = V00 - matrix.V00;
        this.V01 = V01 - matrix.V01;
        this.V02 = V02 - matrix.V02;

        this.V10 = V10 - matrix.V10;
        this.V11 = V11 - matrix.V11;
        this.V12 = V12 - matrix.V12;

        this.V20 = V20 - matrix.V20;
        this.V21 = V21 - matrix.V21;
        this.V22 = V22 - matrix.V22;
    }
    
    /**
     * Subtracts corresponding components of two matrices.
     * @param matrix1 The matrix to subtract from.
     * @param matrix2 The matrix to subtract from the first matrix.
     * @return Returns a matrix which components are equal to difference of corresponding components of the two specified matrices.
     */
    public static Matrix3x3 Subtract(Matrix3x3 matrix1, Matrix3x3 matrix2){
        Matrix3x3 m = new Matrix3x3( );

        m.V00 = matrix1.V00 - matrix2.V00;
        m.V01 = matrix1.V01 - matrix2.V01;
        m.V02 = matrix1.V02 - matrix2.V02;

        m.V10 = matrix1.V10 - matrix2.V10;
        m.V11 = matrix1.V11 - matrix2.V11;
        m.V12 = matrix1.V12 - matrix2.V12;

        m.V20 = matrix1.V20 - matrix2.V20;
        m.V21 = matrix1.V21 - matrix2.V21;
        m.V22 = matrix1.V22 - matrix2.V22;

        return m;
    }
    
}