/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic.SwarmIntelligence.PSO;

/**
 * Velocity.
 * @author Diego Catalano
 */
public class Velocity {
    
    private double[] velocity;

    /**
     * Get value.
     * @return Value.
     */
    public double[] getValue() {
        return velocity;
    }

    /**
     * Set value.
     * @param velocity Value.
     */
    public void setValue(double[] velocity) {
        this.velocity = velocity;
    }

    /**
     * Initializes a new instance of the Velocity class.
     * @param velocity 
     */
    public Velocity(double[] velocity) {
        this.velocity = velocity;
    }
    
}