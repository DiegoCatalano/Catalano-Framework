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
 * 3D Vector class with X, Y and Z coordinates.
 * 
 * <para>The class incapsulates X, Y and Z coordinates of a 3D vector and
 * provides some operations with it.</para>
 * 
 * @author Diego Catalano
 */
public class Vector3 {
    
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
     * Initialize a new instance of the Vector3 class.
     */
    public Vector3() {}

    /**
     * Initialize a new instance of the Vector3 class.
     * @param x X coordinate of the vector.
     * @param y Y coordinate of the vector.
     * @param z Z coordinate of the vector.
     */
    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * Initialize a new instance of the Vector3 class.
     * @param value Value, which is set to all 3 coordinates of the vector.
     */
    public Vector3(float value){
        x = y = z = value;
    }
    
    /**
     * Get maximum value of the vector.
     * @return Returns maximum value of all 3 vector's coordinates.
     */
    public float getMax(){
        return ( x > y ) ? ( ( x > z ) ? x : z ) : ( ( y > z ) ? y : z );
    }
    
    /**
     * Get minimum value of the vector.
     * @return Returns minimum value of all 3 vector's coordinates.
     */
    public float getMin(){
        return ( x < y ) ? ( ( x < z ) ? x : z ) : ( ( y < z ) ? y : z );
    }
    
    /**
     * Get index of the coordinate with maximum value.
     * @return Index of the coordinate with maximum value.
     */
    public int getMaxIndex(){
        return ( x >= y ) ? ( ( x >= z ) ? 0 : 2 ) : ( ( y >= z ) ? 1 : 2 );
    }
    
    /**
     * Get index of the coordinate with minimum value.
     * @return Index of the coordinate with minimum value.
     */
    public int getMinIndex(){
        return ( x <= y ) ? ( ( x <= z ) ? 0 : 2 ) : ( ( y <= z ) ? 1 : 2 );
    }
    
    /**
     * Returns vector's normalization.
     * @return Euclidean normalization of the vector, which is a square root of the sum.
     */
    public float Norm(){
        return (float) Math.sqrt( x * x + y * y + z * z );
    }
    
    /**
     * Returns square of the vector's norm.
     * @return Square of the vector's norm.
     */
    public float Square(){
        return x * x + y * y+ z * z;
    }
    
    /**
     * Returns array representation of the vector.
     * @return Array with 3 values containing X/Y/Z coordinates.
     */
    public float[] toArray(){
        return new float[] {x,y,z};
    }
    
    /**
     * Adds corresponding coordinates of two vectors.
     * @param vector1 First vector to use for dot product calculation.
     * @param vector2 Second vector to use for dot product calculation.
     * @return Returns a vector which coordinates are equal to sum of corresponding coordinates of the two specified vectors.
     */
    public static Vector3 Add(Vector3 vector1, Vector3 vector2){
        return new Vector3( vector1.x + vector2.x, vector1.y + vector2.y, vector1.z + vector2.z );
    }
    
    /**
     * Adds a value to all coordinates of the specified vector.
     * @param vector Vector to add the specified value to.
     * @param value Value to add to all coordinates of the vector.
     * @return Returns new vector with all coordinates increased by the specified value.
     */
    public static Vector3 Add(Vector3 vector, float value){
        return new Vector3( vector.x + value, vector.y + value, vector.z + value );
    }
    
    /**
     * Subtracts corresponding coordinates of two vectors.
     * @param vector1 First vector to use for dot product calculation.
     * @param vector2 Second vector to use for dot product calculation.
     * @return Returns a vector which coordinates are equal to difference of corresponding coordinates of the two specified vectors.
     */
    public static Vector3 Subtract(Vector3 vector1, Vector3 vector2){
        return new Vector3( vector1.x - vector2.x, vector1.y - vector2.y, vector1.z - vector2.z );
    }
    
    /**
     * Subtracts a value from all coordinates of the specified vector.
     * @param vector Vector to subtract the specified value from.
     * @param value Value to subtract from all coordinates of the vector.
     * @return Returns new vector with all coordinates decreased by the specified value.
     */
    public static Vector3 Subtract(Vector3 vector, float value){
        return new Vector3( vector.x - value, vector.y - value, vector.z - value );
    }
    
    /**
     * Multiplies corresponding coordinates of two vectors.
     * @param vector1 The first vector to multiply.
     * @param vector2 The second vector to multiply.
     * @return Returns a vector which coordinates are equal to multiplication of corresponding coordinates of the two specified vectors.
     */
    public static Vector3 Multiply(Vector3 vector1, Vector3 vector2){
        return new Vector3( vector1.x * vector2.x, vector1.y * vector2.y, vector1.z * vector2.z );
    }
    
    /**
     * Multiplies coordinates of the specified vector by the specified factor.
     * @param vector Vector to multiply coordinates of.
     * @param factor Factor to multiple coordinates of the specified vector by.
     * @return Returns new vector with all coordinates multiplied by the specified factor.
     */
    public static Vector3 Multiply(Vector3 vector, float factor){
        return new Vector3( vector.x * factor, vector.y * factor, vector.z * factor );
    }
    
    /**
     * Divides corresponding coordinates of two vectors.
     * @param vector1 The first vector to divide.
     * @param vector2 The second vector to divide.
     * @return Returns a vector which coordinates are equal to coordinates of the first vector divided by corresponding coordinates of the second vector.
     */
    public static Vector3 Divide(Vector3 vector1, Vector3 vector2){
        return new Vector3( vector1.x / vector2.x, vector1.y / vector2.y, vector1.z / vector2.z );
    }
    
    /**
     * Divides coordinates of the specified vector by the specified factor.
     * @param vector Vector to divide coordinates of.
     * @param factor Factor to divide coordinates of the specified vector by.
     * @return Returns new vector with all coordinates divided by the specified factor.
     */
    public static Vector3 Divide(Vector3 vector, float factor){
        return new Vector3( vector.x / factor, vector.y / factor, vector.z / factor );
    }
    
    /**
     * Normalizes the vector by dividing it’s all coordinates with the vector's norm.
     * @return Returns the value of vectors’ norm before normalization.
     */
    public float Normalize( ){
        float norm = (float) Math.sqrt( x * x + y * y + z * z );
        float invNorm = 1.0f / norm;

        x *= invNorm;
        y *= invNorm;
        z *= invNorm;

        return norm;
    }
    
    /**
     * Inverse the vector.
     * @return Returns a vector with all coordinates equal to 1.0 divided by the value of corresponding coordinate
     * in this vector (or equal to 0.0 if this vector has corresponding coordinate also set to 0.0).
     */
    public Vector3 Inverse( ){
        return new Vector3(
            ( x == 0 ) ? 0 : 1.0f / z,
            ( y == 0 ) ? 0 : 1.0f / y,
            ( z == 0 ) ? 0 : 1.0f / z );
    }
    
    /**
     * Calculate absolute values of the vector.
     * @return Returns a vector with all coordinates equal to absolute values of this vector's coordinates.
     */
    public Vector3 Abs(){
        return new Vector3( Math.abs( x ), Math.abs( y ), Math.abs( z ) );
    }
    
    /**
     * Calculates cross product of two vectors.
     * @param vector1 First vector to use for cross product calculation.
     * @param vector2 Second vector to use for cross product calculation.
     * @return Returns cross product of the two specified vectors.
     */
    public static Vector3 Cross( Vector3 vector1, Vector3 vector2 ){
        return new Vector3(
            vector1.y * vector2.z - vector1.z * vector2.y,
            vector1.z * vector2.x - vector1.x * vector2.z,
            vector1.x * vector2.y - vector1.y * vector2.x );
    }
    
    /**
     * Calculates dot product of two vectors.
     * @param vector1 First vector to use for dot product calculation.
     * @param vector2 Second vector to use for dot product calculation.
     * @return Returns dot product of the two specified vectors.
     */
    public static float Dot( Vector3 vector1, Vector3 vector2 ){
        return vector1.x * vector2.x + vector1.y * vector2.y + vector1.z * vector2.z;
    }
    
    /**
     * Converts the vector to a 4D vector.
     * 
     * <para>The method returns a 4D vector which has X, Y and Z coordinates equal to the
     * coordinates of this 3D vector and Vector.W coordinate set to 1.0.
     * 
     * @return Returns 4D vector which is an extension of the 3D vector.
     */
    public Vector4 toVector4( ){
        return new Vector4( x, y, z, 1 );
    }
    
    @Override
    public boolean equals(Object obj){
        if (obj.getClass().isAssignableFrom(Vector3.class)) {
            Vector3 v = (Vector3)obj;
            if ((this.x == v.x) && (this.y == v.y) && (this.z == v.z)) {
                return true;
            }
        }
        return false;
    }
}