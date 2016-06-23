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
 * Complex Number.
 * @author Diego Catalano
 */
public class ComplexNumber {
    
    /**
     * Real.
     */
    public double real = 0;
    /**
     * Imaginary.
     */
    public double imaginary = 0;

    /**
     * Initializes a new instance of the ComplexNumber class.
     */
    public ComplexNumber() {
        this(0,0);
    }

    /**
     * Initializes a new instance of the ComplexNumber class.
     * @param real Real.
     * @param imaginary Imaginary.
     */
    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }
    
    /**
     * Initializes a new instance of the ComplexNumber class.
     * @param z1 Complex Number.
     */
    public ComplexNumber(ComplexNumber z1){
        this.real = z1.real;
        this.imaginary = z1.imaginary;
    }
    
    /**
     * Get Magnitude value of the complex number.
     * @return Magnitude.
     */
    public double getMagnitude(){
        return Math.sqrt(real*real + imaginary*imaginary);
    }
    
    /**
     * Get Squared magnitude value of the complex number.
     * @return squared magnitude.
     */
    public double getSquaredMagnitude(){
        return real*real + imaginary*imaginary;
    }
    
    /**
     * Get Phase value of the complex number.
     * @return Phase value.
     */
    public double getPhase(){
        return Math.atan2(imaginary,real);
    }
    
    /**
     * Get real part from the complex numbers.
     * @param cn Complex numbers.
     * @return Real part.
     */
    public static double[] getReal(ComplexNumber[] cn){
        double[] n = new double[cn.length];
        for (int i = 0; i < n.length; i++) {
            n[i] = cn[i].real;
        }
        return n;
    }
    
    /**
     * Get imaginary part from the complex numbers.
     * @param cn Complex numbers.
     * @return Imaginary part.
     */
    public static double[] getImaginary(ComplexNumber[] cn){
        double[] n = new double[cn.length];
        for (int i = 0; i < n.length; i++) {
            n[i] = cn[i].imaginary;
        }
        return n;
    }
    
    /**
     * Get real part from the complex numbers.
     * @param cn Complex numbers.
     * @return Real part.
     */
    public static double[][] getReal(ComplexNumber[][] cn){
        double[][] n = new double[cn.length][cn[0].length];
        for (int i = 0; i < n.length; i++) {
            for (int j = 0; j < n[0].length; j++) {
                n[i][j] = cn[i][j].real;
            }
        }
        return n;
    }
    
    /**
     * Get imaginary part from the complex numbers.
     * @param cn Complex numbers.
     * @return Imaginary part.
     */
    public static double[][] getImaginary(ComplexNumber[][] cn){
        double[][] n = new double[cn.length][cn[0].length];
        for (int i = 0; i < n.length; i++) {
            for (int j = 0; j < n[0].length; j++) {
                n[i][j] = cn[i][j].imaginary;
            }
        }
        return n;
    }
    
    /**
     * Swap values between real and imaginary.
     * @param z1 Complex number.
     */
    public static void Swap(ComplexNumber z1){
        double t = z1.real;
        z1.real = z1.imaginary;
        z1.imaginary = t;
    }
    
    /**
     * Swap values between real and imaginary.
     * @param z Complex number.
     */
    public static void Swap(ComplexNumber[] z){
        for (int i = 0; i < z.length; i++) {
            z[i] = new ComplexNumber(z[i].imaginary, z[i].real);
        }
    }
    
    /**
     * Swap values between real and imaginary.
     * @param z Complex number.
     */
    public static void Swap(ComplexNumber[][] z){
        for (int i = 0; i < z.length; i++) {
            for (int j = 0; j < z[0].length; j++) {
                z[i][j] = new ComplexNumber(z[i][j].imaginary, z[i][j].real);
            }
        }
    }
    
    /**
     * Absolute value of complex number.
     * Same result like magnitude.
     * @param z Complex Number.
     * @return Absolute number.
     */
    public static double Abs(ComplexNumber z){
        return Magnitude(z);
    }
    
    /**
     * Absolute value of complex number.
     * @param z Complex Numbers.
     * @return Absolute number.
     */
    public static double[] Abs(ComplexNumber[] z){
        double[] values = new double[z.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = z[i].getMagnitude();
        }
        return values;
    }
    
    /**
     * Absolute value of complex number.
     * @param z Complex numbers.
     * @return Absolute number.
     */
    public static double[][] Abs(ComplexNumber[][] z){
        double[][] values = new double[z.length][z[0].length];
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[0].length; j++) {
                values[i][j] = z[i][j].getMagnitude();
            }
        }
        return values;
    }
    
    /**
     * Adds two complex numbers.
     * @param z1 Complex Number.
     * @param z2 Complex Number.
     * @return Returns new ComplexNumber instance containing the sum of specified complex numbers.
     */
    public static ComplexNumber Add(ComplexNumber z1, ComplexNumber z2){
        return new ComplexNumber(z1.real + z2.real, z1.imaginary + z2.imaginary);
    }
    
    /**
     * Adds the complex number with a scalar value.
     * @param z1 Complex Number.
     * @param scalar Scalar value.
     * @return Returns new ComplexNumber instance containing the add of specified complex number with scalar value.
     */
    public static ComplexNumber Add(ComplexNumber z1, double scalar){
        return new ComplexNumber(z1.real + scalar, z1.imaginary);
    }
    
    /**
     * Adds scalar value to a complex number.
     * @param scalar Scalar value.
     */
    public void Add(double scalar){
        this.real += scalar;
    }
    
    /**
     * Subtract two complex numbers.
     * @param z1 Complex Number.
     * @param z2 Complex Number.
     * @return Returns new ComplexNumber instance containing the subtract of specified complex numbers.
     */
    public static ComplexNumber Subtract(ComplexNumber z1, ComplexNumber z2){
        return new ComplexNumber(z1.real - z2.real, z1.imaginary - z2.imaginary);
    }
    
    /**
     * Subtract a complex number.
     * @param z1 Complex Number.
     * @param scalar Scalar value.
     * @return Returns new ComplexNumber instance containing the subtract of specified complex number with a scalar value.
     */
    public static ComplexNumber Subtract(ComplexNumber z1, double scalar){
        return new ComplexNumber(z1.real - scalar, z1.imaginary);
    }
    
    /**
     * Subtracts scalar value to a complex number.
     * @param scalar Scalar value.
     */
    public void Subtract(double scalar){
        this.real -= scalar;
    }
    
    /**
     * Magnitude of complex number.
     * @param z Complex number.
     * @return Magnitude of complex number.
     */
    public static double Magnitude(ComplexNumber z){
        return Math.sqrt(z.real*z.real + z.imaginary*z.imaginary);
    }
    
    /**
     * Multiply two complex numbers.
     * @param z1 Complex Number.
     * @param z2 Complex Number.
     * @return Returns new ComplexNumber instance containing the multiply of specified complex numbers.
     */
    public static ComplexNumber Multiply(ComplexNumber z1, ComplexNumber z2){
        double z1R = z1.real, z1I = z1.imaginary;
        double z2R = z2.real, z2I = z2.imaginary;
        
        return new ComplexNumber(z1R * z2R - z1I * z2I, z1R * z2I + z1I * z2R);
    }
    
    /**
     * Multiply scalar value to a complex number.
     * @param z1 Complex Number.
     * @param scalar Scalar value.
     * @return Returns new ComplexNumber instance containing the multiply of specified complex number with the scalar value.
     */
    public static ComplexNumber Multiply(ComplexNumber z1, double scalar){
        return new ComplexNumber(z1.real*scalar, z1.imaginary*scalar);
    }
    
    /**
     * Multiplys scalar value to a complex number.
     * @param scalar Scalar value.
     */
    public void Multiply(double scalar){
        this.real *= scalar;
        this.imaginary *= scalar;
    }
    
    /**
     * Divide two complex numbers.
     * @param z1 Complex Number.
     * @param z2 Complex Number.
     * @return Returns new ComplexNumber instance containing the divide of specified complex numbers.
     */
    public static ComplexNumber Divide(ComplexNumber z1, ComplexNumber z2){
        
         ComplexNumber conj = ComplexNumber.Conjugate(z2);
         
         double a = z1.real * conj.real + ((z1.imaginary * conj.imaginary) * -1);
         double b = z1.real * conj.imaginary + (z1.imaginary * conj.real);
         
         double c = z2.real * conj.real + ((z2.imaginary * conj.imaginary) * -1);
         
         return new ComplexNumber(a / c, b / c);
    }
    
    /**
     * Divides scalar value to a complex number.
     * @param z1 Complex Number.
     */
    public void Divide(ComplexNumber z1){
        ComplexNumber conj = ComplexNumber.Conjugate(z1);
        
        double a = this.real * conj.real + ((this.imaginary * conj.imaginary) * -1);
        double b = this.real * conj.imaginary + (this.imaginary * conj.real);
         
        double c = z1.real * conj.real + ((z1.imaginary * conj.imaginary) * -1);
        
        this.real = a / c;
        this.imaginary = b / c;
    }
    
    /**
     * Divides scalar value to a complex number.
     * @param z1 Complex Number.
     * @param scalar Scalar value.
     * @return Returns new ComplexNumber instance containing the divide of specified complex number with the scalar value.
     */
    public static ComplexNumber Divide(ComplexNumber z1, double scalar){
        return new ComplexNumber(z1.real / scalar, z1.imaginary / scalar);
    }
    
    /**
     * Divides scalar value to a complex number.
     * @param scalar Scalar value.
     */
    public void Divide(double scalar){
        
        if (scalar == 0){
            try {
                throw new ArithmeticException("Can not divide by zero.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        this.real /= scalar;
        this.imaginary /= scalar;
    }
    
    /**
     * Calculate power of a complex number.
     * @param z1 Complex Number.
     * @param n Power.
     * @return Returns a new complex number containing the power of a specified number.
     */
    public static ComplexNumber Pow(ComplexNumber z1, double n){
        
        double norm = Math.pow(z1.getMagnitude(), n);
        double angle = 360 - Math.abs(Math.toDegrees(Math.atan(z1.imaginary/z1.real)));
        
        double common = n * angle;
        
        double r = norm * Math.cos(Math.toRadians(common));
        double i = norm * Math.sin(Math.toRadians(common));
        
        return new ComplexNumber(r, i);
        
    }
    
    /**
     * Calculate power of a complex number.
     * @param n Power.
     */
    public void Pow(double n){
        double norm = Math.pow(getMagnitude(), n);
        double angle = 360 - Math.abs(Math.toDegrees(Math.atan(this.imaginary/this.real)));
        
        double common = n * angle;
        
        this.real = norm * Math.cos(Math.toRadians(common));
        this.imaginary = norm * Math.sin(Math.toRadians(common));
    }
    
    /**
     * Calculates natural (base <b>e</b>) logarithm of a complex number.
     * @param z1 Complex Number instance.
     * @return Returns new ComplexNumber instance containing the natural logarithm of the specified complex number.
     */
    public static ComplexNumber Log(ComplexNumber z1){
        ComplexNumber result = new ComplexNumber();

        if ( ( z1.real > 0.0 ) && ( z1.imaginary == 0.0 ) ){
            result.real = Math.log( z1.real );
            result.imaginary = 0.0;
        }
        else if ( z1.real == 0.0 ){
            if ( z1.imaginary > 0.0 ){
                result.real = Math.log( z1.imaginary );
                result.imaginary = Math.PI / 2.0;
            }
            else{
                result.real = Math.log( -( z1.imaginary ) );
                result.imaginary = -Math.PI / 2.0;
            }
        }
        else{
            result.real = Math.log( z1.getMagnitude() );
            result.imaginary = Math.atan2( z1.imaginary, z1.real );
        }

        return result;
    }
    
    /**
     * Calculates exponent (e raised to the specified power) of a complex number.
     * @param z1 A Complex Number instance.
     * @return Returns new ComplexNumber instance containing the exponent of the specified complex number.
     */
    public static ComplexNumber Exp(ComplexNumber z1){
        ComplexNumber x,y;
        x = new ComplexNumber(Math.exp(z1.real),0.0);
        y = new ComplexNumber(Math.cos(z1.imaginary),Math.sin(z1.imaginary));
        
        return Multiply(x, y);
    }
    
    /**
     * Calculates Sine value of the complex number.
     * @param z1 A Complex Number instance.
     * @return Returns new ComplexNumber instance containing the Sine value of the specified complex number.
     */
    public static ComplexNumber Sin(ComplexNumber z1){
        ComplexNumber result = new ComplexNumber();

        if ( z1.imaginary == 0.0 )
        {
            result.real = Math.sin( z1.real );
            result.imaginary = 0.0;
        }
        else
        {
            result.real = Math.sin( z1.real ) * Math.cosh( z1.imaginary );
            result.imaginary = Math.cos( z1.real ) * Math.sinh( z1.imaginary );
        }

        return result;
    }
    
    /**
     * Calculates Cosine value of the complex number.
     * @param z1 A ComplexNumber instance.
     * @return Returns new ComplexNumber instance containing the Cosine value of the specified complex number.
     */
    public static ComplexNumber Cos(ComplexNumber z1){
        ComplexNumber result = new ComplexNumber();

        if ( z1.imaginary == 0.0 )
        {
            result.real = Math.cos( z1.real );
            result.imaginary = 0.0;
        }
        else
        {
            result.real = Math.cos( z1.real ) * Math.cosh( z1.imaginary );
            result.imaginary = -Math.sin( z1.real ) * Math.sinh( z1.imaginary );
        }

        return result;
    }
    
    /**
     * Calculates Tangent value of the complex number.
     * @param z1 A ComplexNumber instance.
     * @return Returns new ComplexNumber instance containing the Tangent value of the specified complex number.
     */
    public static ComplexNumber Tan(ComplexNumber z1){
        ComplexNumber result = new ComplexNumber();

        if ( z1.imaginary == 0.0 )
        {
            result.real = Math.tan( z1.real );
            result.imaginary = 0.0;
        }
        else
        {
            double real2 = 2 * z1.real;
            double imag2 = 2 * z1.imaginary;
            double denom = Math.cos( real2 ) + Math.cosh( real2 );

            result.real = Math.sin( real2 ) / denom;
            result.imaginary = Math.sinh( imag2 ) / denom;
        }

        return result;
    }
    
    /**
     * Conjugate this complex number.
     */
    public void Conjugate(){
        this.imaginary *= - 1;
    }
    
    /**
     * Conjugate a complex number.
     * @param z1 Complex number.
     * @return Returns new ComplexNumber instance containing the conjugate of the specified complex number.
     */
    public static ComplexNumber Conjugate(ComplexNumber z1){
        return new ComplexNumber(z1.real, z1.imaginary * - 1);
    }

    @Override
    public String toString() {
        if (this.imaginary >= 0)
            return this.real + " +" + this.imaginary + "i";
        return this.real + " " + this.imaginary + "i";
    }
}