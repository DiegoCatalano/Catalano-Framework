/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic.SwarmIntelligence.PSO;

/**
 * Particle.
 * @author Diego Catalano
 */
public class Particle {
    
    private double fitness;
    private Velocity velocity;
    private Location location;

    /**
     * Get fitness value.
     * @return Fitness value.
     */
    public double getFitness() {
        return fitness;
    }

    /**
     * Set fitness value.
     * @param fitness Fitness value.
     */
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    /**
     * Get velocity.
     * @return Velocity value.
     */
    public Velocity getVelocity() {
        return velocity;
    }

    /**
     * Set velocity.
     * @param velocity Velocity.
     */
    public void setVelocity(Velocity velocity) {
        this.velocity = velocity;
    }

    /**
     * Get location.
     * @return Location value.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Set location.
     * @param location Set location value.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Initializes a new instance of the Particle class.
     */
    public Particle() {}
    
    /**
     * Initializes a new instance of the Particle class.
     * @param location Location value.
     * @param velocity Velocity value.
     */
    public Particle(Location location, Velocity velocity) {
        this(location, velocity, 0);
    }

    /**
     * Initializes a new instance of the Particle class.
     * @param location Location value.
     * @param velocity Velocity value.
     * @param fitness Fitness value.
     */
    public Particle(Location location, Velocity velocity, double fitness) {
        this.location = location;
        this.velocity = velocity;
        this.fitness = fitness;
    }
    
}