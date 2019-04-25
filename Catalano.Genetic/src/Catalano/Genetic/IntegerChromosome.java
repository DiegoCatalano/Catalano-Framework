/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.Genetic;

import java.util.Random;

/**
 *
 * @author Diego Catalano
 */
public class IntegerChromosome extends ChromosomeBase{
    
    private int size;
    private int maxValue;
    
    private int[] values;

    public int getMaxValue() {
        return maxValue;
    }

    public IntegerChromosome(int size, int maxValue) {
        this.size = size;
        this.maxValue = maxValue;
        Generate();
    }

    @Override
    public Object getGene(int index) {
        return values[index];
    }

    @Override
    public void setGene(int index, Object gene) {
        values[index] = (Integer)gene;
    }

    @Override
    public void Generate() {
        
        Random rand = new Random();
        
        values = new int[size];
        for (int i = 0; i < size; i++) {
            values[i] = rand.nextInt(maxValue);
        }
        
    }

    @Override
    public IChromosome CreateNew() {
        return new IntegerChromosome(size, maxValue);
    }

    @Override
    public IChromosome Clone() {
        try{
            return (IntegerChromosome)super.clone();
        }
        catch(CloneNotSupportedException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public int getLength() {
        return size;
    }

    @Override
    public String toString() {
        
        String str = "";
        for (int i = 0; i < size; i++) {
            str += String.valueOf(values[i]);
        }
        
        return str;
        
    }
    
    
    
}