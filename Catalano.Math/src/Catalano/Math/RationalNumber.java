// Catalano Math Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2015
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

    public BigInteger getNumerator() {
        return num;
    }

    public BigInteger getDenominator() {
        return den;
    }

    public boolean isAlwaysFactorize() {
        return alwaysFactorize;
    }

    public void setAlwaysFactorize(boolean alwaysFactorize) {
        this.alwaysFactorize = alwaysFactorize;
    }
    
    public RationalNumber(int number){
        this.num = new BigInteger(String.valueOf(number));
        this.den = new BigInteger("1");
    }
    
    public RationalNumber(BigInteger number){
        this.num = number;
        this.den = new BigInteger("1");
    }
    
    public RationalNumber(int numerator, int denominator){
        this(new BigInteger(String.valueOf(numerator)), new BigInteger(String.valueOf(denominator)));
    }

    public RationalNumber(BigInteger numerator, BigInteger denominator) {
        this.num = numerator;
        this.den = denominator;
        
        if(denominator.compareTo(new BigInteger("0")) == 0)
            throw new IllegalArgumentException("The denominator must be different from zero.");
        
        if(denominator.compareTo(new BigInteger("0")) < 0){
            this.num.multiply(new BigInteger("-1"));
            this.den.multiply(new BigInteger("-1"));
        }
        
        if(alwaysFactorize)
            Factorize();
        
    }
    
    public static RationalNumber Add(RationalNumber rn1, RationalNumber rn2){
        RationalNumber temp = new RationalNumber(rn1.getNumerator(), rn1.getDenominator());
        temp.Add(rn2);
        return temp;
    }
    
    public void Add(int n){
        Add(new RationalNumber(n));
    }
    
    public void Add(int numerator, int denominator){
        Add(new RationalNumber(numerator, denominator));
    }
    
    public void Add(BigInteger numerator){
        Add(new RationalNumber(numerator));
    }
    
    public void Add(BigInteger numerator, BigInteger denominator){
        Add(new RationalNumber(numerator, denominator));
    }
    
    public void Add(RationalNumber rn){
      
      BigInteger denFinal = den.multiply(rn.getDenominator());
      BigInteger numerator1 = num.multiply(rn.getDenominator());
      BigInteger numerator2 = rn.getNumerator().multiply(den);
      
      this.num = numerator1.add(numerator2);
      this.den = denFinal;
      
      if(alwaysFactorize)
        Factorize();
        
    }
    
    public static RationalNumber Divide(RationalNumber rn1, RationalNumber rn2){
        RationalNumber temp = new RationalNumber(rn1.getNumerator(), rn1.getDenominator());
        temp.Divide(rn2);
        return temp;
    }
    
    public void Divide(int n){
        Divide(new RationalNumber(n));
    }
    
    public void Divide(int numerator, int denominator){
        Divide(new RationalNumber(numerator, denominator));
    }
    
    public void Divide(BigInteger n){
        Divide(new RationalNumber(n, new BigInteger("1")));
    }
    
    public void Divide(BigInteger numerator, BigInteger denominator){
        Divide(new RationalNumber(numerator, denominator));
    }
    
    public void Divide(RationalNumber rn){
        Multiply(rn.den, rn.num);
    }
    
    public static RationalNumber Multiply(RationalNumber rn1, RationalNumber rn2){
        RationalNumber temp = new RationalNumber(rn1.getNumerator(), rn1.getDenominator());
        temp.Multiply(rn2);
        return temp;
    }
    
    public void Multiply(int n){
        Multiply(new RationalNumber(n));
    }
    
    public void Multiply(int numerator, int denominator){
        Multiply(new RationalNumber(numerator, denominator));
    }
    
    public void Multiply(BigInteger numerator){
        Multiply(new RationalNumber(numerator));
    }
    
    public void Multiply(BigInteger numerator, BigInteger denominator){
        Multiply(new RationalNumber(numerator, denominator));
    }
    
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
    
    public static RationalNumber Subtract(RationalNumber rn1, RationalNumber rn2){
        RationalNumber temp = new RationalNumber(rn1.getNumerator(), rn1.getDenominator());
        temp.Subtract(rn2);
        return temp;
    }
    
    public void Subtract(int n){
        Subtract(new RationalNumber(n));
    }
    
    public void Subtract(int numerator, int denominator){
        Subtract(new RationalNumber(numerator, denominator));
    }
    
    public void Subtract(BigInteger numerator){
        Subtract(new RationalNumber(numerator));
    }
    
    public void Subtract(BigInteger numerator, BigInteger denominator){
        Subtract(new RationalNumber(numerator, denominator));
    }
    
    public void Subtract(RationalNumber rn){
        
        BigInteger denFinal = den.multiply(rn.getDenominator());
        BigInteger numerator1 = num.multiply(rn.getDenominator());
        BigInteger numerator2 = rn.getNumerator().multiply(den);

        this.num = numerator1.subtract(numerator2);
        this.den = denFinal;

        if(alwaysFactorize)
          Factorize();
    }
    
    private void Factorize(){
        BigInteger gcd = num.gcd(den);
        this.num = num.divide(gcd);
        this.den = den.divide(gcd);
    }
    
    public void Swap(){
        BigInteger x = num;
        this.num = this.den;
        this.den = x;
    }
    
    public double doubleValue(){
        return num.doubleValue() / den.doubleValue();
    }

    @Override
    public String toString() {
        return num.toString() + " / " + den.toString();
    }
    
}