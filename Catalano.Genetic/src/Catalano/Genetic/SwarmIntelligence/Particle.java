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
public class Particle {
    
    private double fitness;
    private Velocity velocity;
    private Location location;

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public void setVelocity(Velocity velocity) {
        this.velocity = velocity;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Particle() {}

    public Particle(double fitness, Velocity velocity, Location location) {
        this.fitness = fitness;
        this.velocity = velocity;
        this.location = location;
    }
    
}