/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.Genetic.Mutation;

import Catalano.Genetic.Chromosome.DoubleChromosome;
import Catalano.Math.Tools;
import java.util.Random;

/**
 *
 * @author Diego
 */
public class MultiplierMutation implements IMutation<DoubleChromosome>{
    
    private double balancer;

    public double getBalancer() {
        return balancer;
    }

    public void setBalancer(double balancer) {
        this.balancer = balancer;
    }

    public MultiplierMutation() {
        this(0.5);
    }

    public MultiplierMutation(double balancer) {
        this.balancer = balancer;
    }

    @Override
    public DoubleChromosome Compute(DoubleChromosome chromossome) {
        
        Random rand = new Random();
        
        DoubleChromosome c = (DoubleChromosome)chromossome.Clone();
        
        int gene = rand.nextInt(c.getLength());
        if(rand.nextDouble() < balancer){
            double value = (Double)chromossome.getGene(gene) * rand.nextDouble();
            value = Tools.Clamp(value, chromossome.getMinValue(), chromossome.getMaxValue());

            c.setGene(gene, value);
        } else{
            double value = (Double)chromossome.getGene(gene) + rand.nextDouble();
            value = Tools.Clamp(value, chromossome.getMinValue(), chromossome.getMaxValue());

            c.setGene(gene, value);
        }

        return c;
    }
    
}