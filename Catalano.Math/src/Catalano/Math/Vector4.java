// Catalano Math Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Andrew Kirillov, 2007-2008
// andrew.kirillov at gmail.com
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
 * 4D Vector class with X, Y, Z and W coordinates.
 * 
 * <para>The class incapsulates X, Y, Z and W coordinates of a 4D vector and
 * provides some operations with it.</para>
 * 
 * @author Diego Catalano
 */
public class Vector4 {
    
    /**
     * X coordinate of the vector.
     */
    public float x;
    
    /**
     * Y coordinate of the vector.
     */
    public float y;
    
    /**
     * Z coordinate of the vector.
     */
    public float z;
    
    /**
     * W coordinate of the vector.
     */
    public float w;

    /**
     * Initialize a new instance of the Vector4 class.
     */
    public Vector4() {}

    /**
     * Initialize a new instance of the Vector4 class.
     * @param x X coordinate of the vector.
     * @param y Y coordinate of the vector.
     * @param z Z coordinate of the vector.
     * @param w W coordinate of the vector.
     */
    public Vector4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    /**
     * Initialize a new instance of the Vector4 class.
     * @param value Value, which is set to all 4 coordinates of the vector.
     */
    public Vector4(float value){
        x = y = z = w = value;
    }
    
    /**
     * Get maximum value of the vector.
     * @return Returns maximum value of all 4 vector's coordinates.
     */
    public float getMax(){
        float v1 = ( x > y ) ? x : y;
        float v2 = ( z > w ) ? z : w;

        return ( v1 > v2 ) ? v1 : v2;
    }
    
    /**
     * Get minimum value of the vector.
     * @return Returns minimum value of all 4 vector's coordinates.
     */
    public float getMin(){
        float v1 = ( x < y ) ? x : y;
        float v2 = ( z < w ) ? z : w;

        return ( v1 < v2 ) ? v1 : v2;
    }
    
    /**
     * Get index of the coordinate with maximum value.
     * @return Index of the coordinate with maximum value.
     */
    public int getMaxIndex(){
        float v1 = 0;
        float v2 = 0;
        int i1 = 0;
        int i2 = 0;

        if ( x >= y )
        {
            v1 = x;
            i1 = 0;
        }
        else
        {
            v1 = y;
            i1 = 1;
        }

        if ( z >= w )
        {
            v2 = z;
            i2 = 2;
        }
        else
        {
            v2 = w;
            i2 = 3;
        }

        return ( v1 >= v2 ) ? i1 : i2;
    }
    
    /**
     * Get index of the coordinate with minimum value.
     * @return Index of the coordinate with minimum value.
     */
    public int getMinIndex(){
        float v1 = 0;
        float v2 = 0;
        int i1 = 0;
        int i2 = 0;

        if ( x <= y )
        {
            v1 = x;
            i1 = 0;
        }
        else
        {
            v1 = y;
            i1 = 1;
        }

        if ( z <= w )
        {
            v2 = z;
            i2 = 2;
        }
        else
        {
            v2 = w;
            i2 = 3;
        }

        return ( v1 <= v2 ) ? i1 : i2;
    }
    
    /**
     * Returns vector's normalization.
     * @return Euclidean normalization of the vector, which is a square root of the sum.
     */
    public float Norm(){
        return (float) Math.sqrt( x * x + y * y + z * z + w * w );
    }
    
    /**
     * Returns square of the vector's norm.
     * @return Square of the vector's norm.
     */
    public float Square(){
        return x * x + y * y+ z * z + w * w;
    }
    
    /**
     * Returns array representation of the vector.
     * @return Array with 3 values containing X/Y/Z coordinates.
     */
    public float[] toArray(){
        return new float[] {x,y,z,w};
    }
    
    /**
     * Adds corresponding coordinates of two vectors.
     * @param vector1 First vector to use for dot product calculation.
     * @param vector2 Second vector to use for dot product calculation.
     * @return Returns a vector which coordinates are equal to sum of corresponding coordinates of the two specified vectors.
     */
    public static Vector4 Add(Vector4 vector1, Vector4 vector2){
        return new Vector4( vector1.x + vector2.x, vector1.y + vector2.y, vector1.z + vector2.z, vector1.w + vector2.w );
    }
    
    /**
     * Adds a value to all coordinates of the specified vector.
     * @param vector Vector to add the specified value to.
     * @param value Value to add to all coordinates of the vector.
     * @return Returns new vector with all coordinates increased by the specified value.
     */
    public static Vector4 Add(Vector4 vector1, float value){
        return new Vector4( vector1.x + value, vector1.y + value, vector1.z + value, vector1.w + value );
    }
    
    /**
     * Subtracts corresponding coordinates of two vectors.
     * @param vector1 First vector to use for dot product calculation.
     * @param vector2 Second vector to use for dot product calculation.
     * @return Returns a vector which coordinates are equal to difference of corresponding coordinates of the two specified vectors.
     */
    public static Vector4 Subtract(Vector4 vector1, Vector4 vector2){
        return new Vector4( vector1.x - vector2.x, vector1.y - vector2.y, vector1.z - vector2.z, vector1.w - vector2.w );
    }
    
    /**
     * Subtracts a value from all coordinates of the specified vector.
     * @param vector Vector to subtract the specified value from.
     * @param value Value to subtract from all coordinates of the vector.
     * @return Returns new vector with all coordinates decreased by the specified value.
     */
    public static Vector4 Subtract(Vector4 vector1, float value){
        return new Vector4( vector1.x - value, vector1.y - value, vector1.z - value, vector1.w - value );
    }
    
    /**
     * Multiplies corresponding coordinates of two vectors.
     * @param vector1 The first vector to multiply.
     * @param vector2 The second vector to multiply.
     * @return Returns a vector which coordinates are equal to multiplication of corresponding coordinates of the two specified vectors.
     */
    public static Vector4 Multiply(Vector4 vector1, Vector4 vector2){
        return new Vector4( vector1.x * vector2.x, vector1.y * vector2.y, vector1.z * vector2.z, vector1.w * vector2.w );
    }
    
    /**
     * Multiplies coordinates of the specified vector by the specified factor.
     * @param vector Vector to multiply coordinates of.
     * @param factor Factor to multiple coordinates of the specified vector by.
     * @return Returns new vector with all coordinates multiplied by the specified factor.
     */
    public static Vector4 Multiply(Vector4 vector, float factor){
        return new Vector4( vector.x * factor, vector.y * factor, vector.z * factor, vector.w * factor );
    }
    
    /**
     * Divides corresponding coordinates of two vectors.
     * @param vector1 The first vector to divide.
     * @param vector2 The second vector to divide.
     * @return Returns a vector which coordinates are equal to coordinates of the first vector divided by corresponding coordinates of the second vector.
     */
    public static Vector4 Divide(Vector4 vector1, Vector4 vector2){
        return new Vector4( vector1.x / vector2.x, vector1.y / vector2.y, vector1.z / vector2.z, vector1.w / vector2.w );
    }
    
    /**
     * Divides coordinates of the specified vector by the specified factor.
     * @param vector Vector to divide coordinates of.
     * @param factor Factor to divide coordinates of the specified vector by.
     * @return Returns new vector with all coordinates divided by the specified factor.
     */
    public static Vector4 Divide(Vector4 vector, float factor){
        return new Vector4( vector.x / factor, vector.y / factor, vector.z / factor, vector.w / factor );
    }
    
    /**
     * Normalizes the vector by dividing it’s all coordinates with the vector's norm.
     * @return Returns the value of vectors’ norm before normalization.
     */
    public float Normalize( ){
        float norm = (float) Math.sqrt( x * x + y * y + z * z + w * w );
        float invNorm = 1.0f / norm;

        x *= invNorm;
        y *= invNorm;
        z *= invNorm;
        w *= invNorm;

        return norm;
    }
    
    /**
     * Inverse the vector.
     * @return Returns a vector with all coordinates equal to 1.0 divided by the value of corresponding coordinate
     * in this vector (or equal to 0.0 if this vector has corresponding coordinate also set to 0.0).
     */
    public Vector4 Inverse( ){
        return new Vector4(
            ( x == 0 ) ? 0 : 1.0f / z,
            ( y == 0 ) ? 0 : 1.0f / y,
            ( y == 0 ) ? 0 : 1.0f / y,
            ( w == 0 ) ? 0 : 1.0f / w );
    }
    
    /**
     * Calculate absolute values of the vector.
     * @return Returns a vector with all coordinates equal to absolute values of this vector's coordinates.
     */
    public Vector4 Abs(){
        return new Vector4( Math.abs( x ), Math.abs( y ), Math.abs( z ), Math.abs( w ) );
    }
    
    
    /**
     * Calculates dot product of two vectors.
     * @param vector1 First vector to use for dot product calculation.
     * @param vector2 Second vector to use for dot product calculation.
     * @return Returns dot product of the two specified vectors.
     */
    public static float Dot( Vector4 vector1, Vector4 vector2 ){
            return vector1.x * vector2.x + vector1.y * vector2.y +
                   vector1.z * vector2.z + vector1.w * vector2.w;
    }
    
    /**
     * Converts the vector to a 3D vector.
     * @return Returns 3D vector which has X/Y/Z coordinates equal to X/Y/Z coordinates of this vector divided by W.
     */
    public Vector3 toVector3( ){
        return new Vector3( x / w, y / w, z / w);
    }
    
    @Override
    public boolean equals(Object obj){
        if (obj.getClass().isAssignableFrom(Vector3.class)) {
            Vector4 v = (Vector4)obj;
            if ((this.x == v.x) && (this.y == v.y) && (this.z == v.z) && (this.w == v.w)) {
                return true;
            }
        }
        return false;
    }
}