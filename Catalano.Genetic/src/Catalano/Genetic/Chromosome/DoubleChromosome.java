/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.Genetic.Chromosome;

import Catalano.Math.Tools;
import java.util.Random;

/**
 *
 * @author Diego Catalano
 */
public class DoubleChromosome extends ChromosomeBase{
    
    private final int size;
    private final double minValue;
    private final double maxValue;
    
    private double[] values;

    public DoubleChromosome(int size, double minValue, double maxValue) {
        this.size = size;
        this.minValue = minValue;
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
        
        values = new double[size];
        for (int i = 0; i < size; i++) {
            values[i] = Tools.Scale(0, 1, minValue, maxValue, rand.nextDouble());
        }
        
    }

    @Override
    public IChromosome CreateNew() {
        return new DoubleChromosome(size, minValue, maxValue);
    }

    @Override
    public IChromosome Clone() {
        try{
            return (DoubleChromosome)super.clone();
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