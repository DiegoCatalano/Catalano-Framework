// Catalano Genetic Library
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

package Catalano.Genetic;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Random;

/**
 *
 * @author Diego
 */
public class BinaryChromossome implements Cloneable, Comparable<BinaryChromossome>{
    
    private final int nBits;
    private double fitness;
    
    private BigInteger value;
    
    public static BinaryChromossome Generate(int nBits){
        return Generate(nBits, 0);
    }
    
    public static BinaryChromossome Generate(int nBits, long seed){
        Random r = new Random();
        if(seed != 0) r.setSeed(seed);
        
        String data = "";
        for (int i = 0; i < nBits; i++) {
            data += r.nextInt(2);
        }
        
        return new BinaryChromossome(nBits, data);
    }

    public int getNumberOfBits() {
        return nBits;
    }

    public BigInteger getValue() {
        return value;
    }
    
    public void setValue(String value){
        setValue(value, 10);
    }
    
    public void setValue(String n, int radix){
        value = new BigInteger(n, radix);
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
    
    public BinaryChromossome(int nBits){
        this(nBits, "0");
    }
    
    public BinaryChromossome(int nBits, String bits){
        this(nBits, new BigInteger(bits, 2));
    }
    
    public BinaryChromossome(int nBits, BigInteger value){
        this.nBits = nBits;
        this.value = value;
    }    
    
    public BinaryChromossome(BinaryChromossome chromossome){
        this.nBits = chromossome.getNumberOfBits();
        this.value = chromossome.getValue();
    }
    
    public double toDouble(){
        return toDouble(10);
    }
    
    public double toDouble(int scale){
        return toDouble(0, 1, scale);
    }
    
    public double toDouble(double min, double max){
        return toDouble(min, max, 10);
    }
    
    public double toDouble(double min, double max, int scale){
        BigDecimal a = new BigDecimal(value);
        BigDecimal b = new BigDecimal(new BigInteger("2").pow(nBits).add(new BigInteger("1")));
        
        double n =  a.divide(b, scale, RoundingMode.HALF_UP).doubleValue();
        return Catalano.Math.Tools.Scale(0, 1, min, max, n);
    }
    
    public String toBinary(){
        
        String v = value.toString(2);
        int rest = nBits - v.length();
        if(rest == 0)
            return v;
        
        String pad = "";
        for (int i = 0; i < rest; i++) {
            pad += "0";
        }
        
        return pad + v;
        
    }

    @Override
    public String toString() {
        return toBinary();
    }

    @Override
    public int compareTo(BinaryChromossome o) {
        return Double.compare(o.getFitness(), fitness);
    }

    @Override
    public BinaryChromossome clone() {
        try{
            return (BinaryChromossome)super.clone();   
        }
        catch(CloneNotSupportedException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}