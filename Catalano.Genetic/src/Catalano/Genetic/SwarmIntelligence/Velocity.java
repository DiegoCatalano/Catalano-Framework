/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic.SwarmIntelligence;

/**
 *
 * @author Diego
 */
public class Velocity {
    
    private double[] velocity;

    public double[] getValue() {
        return velocity;
    }

    public void setValue(double[] velocity) {
        this.velocity = velocity;
    }

    public Velocity(double[] velocity) {
        this.velocity = velocity;
    }
    
}