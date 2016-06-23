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

package Catalano.Math.Functions;

/**
 * Perlin noise function.
 * @author Diego Catalano
 */
public class PerlinNoise {
    private double initFrequency = 1.0;
    private double initAmplitude = 1.0;
    private double persistence = 0.65;
    private int octaves = 4;

    /**
     * Initializes a new instance of the PerlinNoise class.
     */
    public PerlinNoise() {}
    
    /**
     * Initializes a new instance of the PerlinNoise class.
     * @param octaves Number of octaves.
     * @param persistense Persistence value.
     */
    public PerlinNoise(int octaves, double persistense) {
        this.octaves = octaves;
        this.persistence = persistense;
    }

    /**
     * Initializes a new instance of the PerlinNoise class.
     * @param octaves Number of octaves.
     * @param persistence Persistence value.
     * @param initFrequency Initial frequency.
     * @param initAmplitude Initial amplitude.
     */
    public PerlinNoise(int octaves, double persistence, double initFrequency, double initAmplitude){
        this.octaves       = octaves;
        this.persistence   = persistence;
        this.initFrequency = initFrequency;
        this.initAmplitude = initAmplitude;
    }

    /**
     * Get initial amplitude.
     * @return Value.
     */
    public double getInitAmplitude() {
        return initAmplitude;
    }

    /**
     * Set initial amplitude.
     * @param initAmplitude Value.
     */
    public void setInitAmplitude(double initAmplitude) {
        this.initAmplitude = initAmplitude;
    }

    /**
     * Get initial frequency.
     * @return Value.
     */
    public double getInitFrequency() {
        return initFrequency;
    }

    /**
     * Set initial frequency.
     * @param initFrequency Value.
     */
    public void setInitFrequency(double initFrequency) {
        this.initFrequency = initFrequency;
    }

    /**
     * Get number of octaves.
     * @return Value.
     */
    public int getOctaves() {
        return octaves;
    }

    /**
     * Set number of octaves.
     * @param octaves Value.
     */
    public void setOctaves(int octaves) {
        this.octaves = octaves;
    }

    /**
     * Get persistence value.
     * @return Value.
     */
    public double getPersistence() {
        return persistence;
    }

    /**
     * Set persistence value.
     * @param persistence Value.
     */
    public void setPersistence(double persistence) {
        this.persistence = persistence;
    }
    
    /**
     * 1-D Perlin noise function.
     * @param x X Value.
     * @return Returns function's value at point x.
     */
    public double Function1D( double x ){
        double	frequency = initFrequency;
        double	amplitude = initAmplitude;
        double	sum = 0;

        // octaves
        for ( int i = 0; i < octaves; i++ )
        {
            sum += SmoothedNoise( x * frequency ) * amplitude;

            frequency *= 2;
            amplitude *= persistence;
        }
        return sum;
    }
    
    /**
     * 2-D Perlin noise function.
     * @param x X Value.
     * @param y Y Value.
     * @return Returns function's value at point xy.
     */
    public double Function2D( double x, double y ){
        double	frequency = initFrequency;
        double	amplitude = initAmplitude;
        double	sum = 0;

        // octaves
        for ( int i = 0; i < octaves; i++ )
        {
            sum += SmoothedNoise( x * frequency, y * frequency ) * amplitude;

            frequency *= 2;
            amplitude *= persistence;
        }
        return sum;
    }
    
    /**
     * Ordinary noise function.
     * @param x Value.
     * @return Value.
     */
    private double Noise( int x ){
        int n = ( x << 13 ) ^ x;

        return ( 1.0 - ( ( n * ( n * n * 15731 + 789221 ) + 1376312589 ) & 0x7fffffff ) / 1073741824.0 );
    }
    
    /**
     * Ordinary noise function.
     * @param x X Value.
     * @param y Y Value.
     * @return 
     */
    private double Noise( int x, int y ){
        int n = x + y * 57;
        n = ( n << 13 ) ^ n;

        return ( 1.0 - ( ( n * ( n * n * 15731 + 789221 ) + 1376312589 ) & 0x7fffffff ) / 1073741824.0 );
    }
    
    /**
     * Smoothed noise.
     * @param x Value.
     * @return Value.
     */
    private double SmoothedNoise( double x ){
        int		xInt = (int) x;
        double	xFrac = x - xInt;

        return CosineInterpolate( Noise( xInt ), Noise( xInt + 1 ), xFrac );
    }
    
    /**
     * Smoothed noise.
     * @param x X Value.
     * @param y Y Value.
     * @return Value.
     */
    private double SmoothedNoise( double x, double y ){
        int		xInt = (int) x;
        int		yInt = (int) y;
        double	xFrac = x - xInt;
        double	yFrac = y - yInt;

        // get four noise values
        double	x0y0 = Noise( xInt, yInt );
        double	x1y0 = Noise( xInt + 1, yInt );
        double	x0y1 = Noise( xInt, yInt + 1 );
        double	x1y1 = Noise( xInt + 1, yInt + 1 );

        // x interpolation
        double	v1 = CosineInterpolate( x0y0, x1y0, xFrac );
        double	v2 = CosineInterpolate( x0y1, x1y1, xFrac );
        // y interpolation
        return CosineInterpolate( v1, v2, yFrac );
    }
    
    /**
     * Cosine interpolation.
     * @param x1 X1 Value.
     * @param x2 X2 Value.
     * @param a Value.
     * @return Value.
     */
    private double CosineInterpolate( double x1, double x2, double a ){
        double f = ( 1 - Math.cos( a * Math.PI ) ) * 0.5;

        return x1 * ( 1 - f ) + x2 * f;
    }
}