/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic;

import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author Diego
 */
public class BinaryChromosome extends ChromosomeBase{
    
    private int nBits;
    private BigInteger data;

    @Override
    public int getLength() {
        return nBits;
    }
    
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

    public BinaryChromosome(int nBits) {
        this.nBits = nBits;
        Generate();
    }
    
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
