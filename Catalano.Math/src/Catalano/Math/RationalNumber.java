// Catalano Math Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
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

import java.math.BigInteger;

/**
 * Rational Number.
 * @author Diego Catalano
 */
public class RationalNumber {
    
    private BigInteger num;
    private BigInteger den;
    boolean alwaysFactorize = true;

    /**
     * Get Numerator.
     * @return Numerator.
     */
    public BigInteger getNumerator() {
        return num;
    }
    
    /**
     * Set Numerator.
     * @param numerator Numerator.
     */
    public void setNumerator(BigInteger numerator){
        this.num = numerator;
    }

    /**
     * Get Denominator.
     * @return Denominator.
     */
    public BigInteger getDenominator() {
        return den;
    }
    
    /**
     * Set Denominator.
     * @param denominator Denominator.
     */
    public void setDenominator(BigInteger denominator){
        this.den = denominator;
    }

    /**
     * Check if each operation is being factorized.
     * @return True if every operation is factorized, otherwise false.
     */
    public boolean isAlwaysFactorize() {
        return alwaysFactorize;
    }

    /**
     * Set if need to factorize the rational number for every operation.
     * @param alwaysFactorize True if need to factorize.
     */
    public void setAlwaysFactorize(boolean alwaysFactorize) {
        this.alwaysFactorize = alwaysFactorize;
    }
    
    /**
     * Initializes a new instance of the ComplexNumber class.
     * @param number Number.
     */
    public RationalNumber(int number){
        this.num = new BigInteger(String.valueOf(number));
        this.den = new BigInteger("1");
    }
    
    /**
     * Initializes a new instance of the ComplexNumber class.
     * @param numerator Numerator.
     * @param denominator Denominator.
     */
    public RationalNumber(int numerator, int denominator){
        this(new BigInteger(String.valueOf(numerator)), new BigInteger(String.valueOf(denominator)));
    }
    
    /**
     * Initializes a new instance of the ComplexNumber class.
     * @param number Number.
     */
    public RationalNumber(BigInteger number){
        this.num = number;
        this.den = new BigInteger("1");
    }

    /**
     * Initializes a new instance of the ComplexNumber class.
     * @param numerator Numerator.
     * @param denominator Denominator.
     */
    public RationalNumber(BigInteger numerator, BigInteger denominator) {
        this.num = numerator;
        this.den = denominator;
        
        if(denominator.compareTo(new BigInteger("0")) == 0)
            throw new IllegalArgumentException("The denominator must be different from zero.");
        
        if(denominator.compareTo(new BigInteger("0")) < 0){
            this.num = this.num.multiply(new BigInteger("-1"));
            this.den = this.den.multiply(new BigInteger("-1"));
        }
        
        if(alwaysFactorize)
            Factorize();
        
    }
    
    /**
     * Absolute value.
     * @param rn Rational number.
     * @return abs(rn).
     */
    public static RationalNumber Abs(RationalNumber rn){
        return new RationalNumber(rn.getNumerator().abs(), rn.getDenominator());
    }
    
    /**
     * Add operation.
     * Rn + Rn.
     * @param rn1 Rational Number.
     * @param rn2 Rational Number.
     * @return Result of add operation.
     */
    public static RationalNumber Add(RationalNumber rn1, RationalNumber rn2){
        RationalNumber temp = new RationalNumber(rn1.getNumerator(), rn1.getDenominator());
        temp.Add(rn2);
        return temp;
    }
    
    /**
     * Absolute value.
     */
    public void Abs(){
        this.num = num.abs();
    }
    
    /**
     * Add operation.
     * @param number Number.
     */
    public void Add(int number){
        Add(new RationalNumber(number));
    }
    
    /**
     * Add operation.
     * @param numerator Numerator.
     * @param denominator Denominator.
     */
    public void Add(int numerator, int denominator){
        Add(new RationalNumber(numerator, denominator));
    }
    
    /**
     * Add operation.
     * @param number Numerator.
     */
    public void Add(BigInteger number){
        Add(new RationalNumber(number));
    }
    
    /**
     * Add operation.
     * @param numerator Numerator.
     * @param denominator Denominator.
     */
    public void Add(BigInteger numerator, BigInteger denominator){
        Add(new RationalNumber(numerator, denominator));
    }
    
    /**
     * Add operation.
     * @param rn Rational number.
     */
    public void Add(RationalNumber rn){
      
      BigInteger denFinal = den.multiply(rn.getDenominator());
      BigInteger numerator1 = num.multiply(rn.getDenominator());
      BigInteger numerator2 = rn.getNumerator().multiply(den);
      
      this.num = numerator1.add(numerator2);
      this.den = denFinal;
      
      if(alwaysFactorize)
        Factorize();
        
    }
    
    /**
     * Divide operation.
     * Rn / Rn.
     * @param rn1 Rational Number.
     * @param rn2 Rational Number.
     * @return Result of divide operation.
     */
    public static RationalNumber Divide(RationalNumber rn1, RationalNumber rn2){
        RationalNumber temp = new RationalNumber(rn1.getNumerator(), rn1.getDenominator());
        temp.Divide(rn2);
        return temp;
    }
    
    /**
     * Divide operation.
     * @param number Number.
     */
    public void Divide(int number){
        Divide(new RationalNumber(number));
    }
    
    /**
     * Divide operation.
     * @param numerator Numerator.
     * @param denominator Denominator.
     */
    public void Divide(int numerator, int denominator){
        Divide(new RationalNumber(numerator, denominator));
    }
    
    /**
     * Divide operation.
     * @param number Number.
     */
    public void Divide(BigInteger number){
        Divide(new RationalNumber(number, new BigInteger("1")));
    }
    
    /**
     * Divide operation.
     * @param numerator Numerator.
     * @param denominator Denominator.
     */
    public void Divide(BigInteger numerator, BigInteger denominator){
        Divide(new RationalNumber(numerator, denominator));
    }
    
    /**
     * Divide operation.
     * @param rn Rational number.
     */
    public void Divide(RationalNumber rn){
        Multiply(rn.den, rn.num);
    }
    
    /**
     * Multiply operation.
     * Rn * Rn.
     * @param rn1 Rational Number.
     * @param rn2 Rational Number.
     * @return Result of multiply operation.
     */
    public static RationalNumber Multiply(RationalNumber rn1, RationalNumber rn2){
        RationalNumber temp = new RationalNumber(rn1.getNumerator(), rn1.getDenominator());
        temp.Multiply(rn2);
        return temp;
    }
    
    /**
     * Multiply operation.
     * @param number Number.
     */
    public void Multiply(int number){
        Multiply(new RationalNumber(number));
    }
    
    /**
     * Multiply operation.
     * @param numerator Numerator.
     * @param denominator Denominator.
     */
    public void Multiply(int numerator, int denominator){
        Multiply(new RationalNumber(numerator, denominator));
    }
    
    /**
     * Multiply operation.
     * @param number Number.
     */
    public void Multiply(BigInteger number){
        Multiply(new RationalNumber(number));
    }
    
    /**
     * Multiply operation.
     * @param numerator Numerator.
     * @param denominator Denominator.
     */
    public void Multiply(BigInteger numerator, BigInteger denominator){
        Multiply(new RationalNumber(numerator, denominator));
    }
    
    /**
     * Multiply operation.
     * @param rn Rational number.
     */
    public void Multiply(RationalNumber rn){
        this.num = num.multiply(rn.num);
        this.den = den.multiply(rn.den);
        
        if(den.compareTo(new BigInteger("0")) < 0){
            this.num = num.multiply(new BigInteger("-1"));
            this.den = den.multiply(new BigInteger("-1"));
        }
        
        if(alwaysFactorize)
            Factorize();
    }
    
    /**
     * Power of the number.
     * @param rn Rational number.
     * @param power Power expoent.
     * @return The result rn^power.
     */
    public static RationalNumber Pow(RationalNumber rn, int power){
        
        RationalNumber r = new RationalNumber(rn.getNumerator(), rn.getDenominator());
        for (int i = 1; i < power; i++) {
            r = RationalNumber.Multiply(r, rn);
        }
        
        return r;
        
    }
    
    /**
     * Power of the number.
     * @param power Power expoent.
     */
    public void pow(int power){
        
        RationalNumber rn = new RationalNumber(num, den);
        for (int i = 1; i < power; i++) {
            this.num = num.multiply(rn.num);
            this.den = den.multiply(rn.den);
        }
        
    }
    
    /**
     * Subtract operation.
     * Rn - Rn.
     * @param rn1 Rational Number.
     * @param rn2 Rational Number.
     * @return Result of subtract operation.
     */
    public static RationalNumber Subtract(RationalNumber rn1, RationalNumber rn2){
        RationalNumber temp = new RationalNumber(rn1.getNumerator(), rn1.getDenominator());
        temp.Subtract(rn2);
        return temp;
    }
    
    /**
     * Subtract operation.
     * @param number Number.
     */
    public void Subtract(int number){
        Subtract(new RationalNumber(number));
    }
    
    /**
     * Subtract operation.
     * @param numerator Numerator.
     * @param denominator Denominator.
     */
    public void Subtract(int numerator, int denominator){
        Subtract(new RationalNumber(numerator, denominator));
    }
    
    /**
     * Subtract operation.
     * @param number Number.
     */
    public void Subtract(BigInteger number){
        Subtract(new RationalNumber(number));
    }
    
    /**
     * Subtract operation.
     * @param numerator Numerator.
     * @param denominator Denominator.
     */
    public void Subtract(BigInteger numerator, BigInteger denominator){
        Subtract(new RationalNumber(numerator, denominator));
    }
    
    /**
     * Subtract operation.
     * @param rn Rational number.
     */
    public void Subtract(RationalNumber rn){
        
        BigInteger denFinal = den.multiply(rn.getDenominator());
        BigInteger numerator1 = num.multiply(rn.getDenominator());
        BigInteger numerator2 = rn.getNumerator().multiply(den);

        this.num = numerator1.subtract(numerator2);
        this.den = denFinal;

        if(alwaysFactorize)
          Factorize();
    }
    
    /**
     * Factorize the rational number.
     */
    public void Factorize(){
        BigInteger gcd = num.gcd(den);
        this.num = num.divide(gcd);
        this.den = den.divide(gcd);
    }
    
    /**
     * Swap the numerator with the denominator.
     */
    public void Swap(){
        BigInteger x = num;
        this.num = this.den;
        this.den = x;
    }
    
    /**
     * Convert to double.
     * @return Double value.
     */
    public double doubleValue(){
        return num.doubleValue() / den.doubleValue();
    }

    /**
     * Rational number representational.
     * Rn = numerator / denominator
     * @return Representation.
     */
    @Override
    public String toString() {
        return num.toString() + " / " + den.toString();
    }
    
}