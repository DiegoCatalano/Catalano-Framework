/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Math;

import java.math.BigInteger;

/**
 *
 * @author Diego
 */
public class RationalNumber {
    
    private BigInteger num;
    private BigInteger den;

    public BigInteger getNumerator() {
        return num;
    }

    public BigInteger getDenominator() {
        return den;
    }
    
    public RationalNumber(int number){
        this.num = new BigInteger(String.valueOf(number));
        this.den = new BigInteger("1");
    }
    
    public RationalNumber(BigInteger number){
        this.num = number;
        this.den = new BigInteger("1");
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
        
        Factorize();
        
    }
    
    public void Multiply(int n){
        Multiply(new BigInteger(String.valueOf(n)), new BigInteger("1"));
    }
    
    public void Multiply(BigInteger numerator, BigInteger denominator){
        this.num = num.multiply(numerator);
        this.den = den.multiply(denominator);
        
        if(denominator.compareTo(new BigInteger("0")) < 0){
            this.num.multiply(new BigInteger("-1"));
            this.den.multiply(new BigInteger("-1"));
        }
        
        Factorize();
    }
    
    private void Factorize(){
        BigInteger gcd = num.gcd(den);
        this.num.divide(gcd);
        this.den.divide(gcd);
    }

    @Override
    public String toString() {
        return num.toString() + " / " + den.toString();
    }
    
}
