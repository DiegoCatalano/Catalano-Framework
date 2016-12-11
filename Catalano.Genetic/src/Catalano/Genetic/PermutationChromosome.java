/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic;

import Catalano.Core.ArraysUtil;
import Catalano.Math.Matrix;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diego
 */
public class PermutationChromosome extends ChromosomeBase {
    
    private int[] data;
    
    @Override
    public int getLength() {
        return data.length;
    }

    @Override
    public Object getGene(int index) {
        return data[index];
    }

    @Override
    public void setGene(int index, Object gene) {
        this.data[index] = (Integer)gene;
    }

    public PermutationChromosome(int lenght) {
        this.data = Matrix.Indices(0, lenght);
        ArraysUtil.Shuffle(data);
    }
    
    public PermutationChromosome(int[] data){
        this.data = data;
    }

    @Override
    public void Generate() {
        
        data = Matrix.Indices(0, data.length);
        ArraysUtil.Shuffle(data);
        
    }

    @Override
    public IChromosome CreateNew() {
        return new PermutationChromosome(data.length);
    }

    @Override
    public IChromosome Clone() {
        return new PermutationChromosome(Arrays.copyOf(data, data.length));
    }
    
}