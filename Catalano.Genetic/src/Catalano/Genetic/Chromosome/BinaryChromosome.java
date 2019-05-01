// Catalano Genetic Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2019
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

package Catalano.Genetic.Chromosome;

import java.math.BigInteger;
import java.util.Random;

/**
 * Binary Chromosome.
 * @author Diego Catalano
 */
public class BinaryChromosome extends ChromosomeBase{
    
    private int nBits;
    private BigInteger data;

    @Override
    public int getLength() {
        return nBits;
    }
    
    /**
     * Set Specified binary.
     * @param bits Binary.
     */
    public void setBinary(String bits){
        data = new BigInteger(bits, 2);
    }

    @Override
    public Object getGene(int index) {
        String bin = toBinary();
        return bin.charAt(index);
    }

    @Override
    public void setGene(int index, Object gene) {
        String bin = toBinary();
        
        StringBuilder str = new StringBuilder(bin);
        str.setCharAt(index, (Character)gene);
        
        this.data = new BigInteger(str.toString(), 2);
    }

    /**
     * Initializes a new instance of the BinaryChromosome class.
     * @param nBits Number of bits.
     */
    public BinaryChromosome(int nBits) {
        this.nBits = nBits;
        Generate();
    }
    
    /**
     * Initializes a new instance of the BinaryChromosome class.
     * @param nBits Number of bits
     * @param bits Specified bit.
     */
    public BinaryChromosome(int nBits, String bits) {
        this.nBits = nBits;
        this.data = new BigInteger(bits, 2);
    }

    @Override
    public void Generate() {
        Random r = new Random();
        
        String bin = "";
        for (int i = 0; i < nBits; i++) {
            bin += r.nextInt(2);
        }
        
        this.data = new BigInteger(bin, 2);
    }

    @Override
    public IChromosome CreateNew() {
        return new BinaryChromosome(nBits);
    }

    @Override
    public IChromosome Clone() {
        try{
            return (BinaryChromosome)super.clone();
        }
        catch(CloneNotSupportedException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    
    /**
     * Convert Binary Chromosome to binary representation.
     * @return Binary representation.
     */
    public String toBinary(){
        
        String v = data.toString(2);
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
    
}
