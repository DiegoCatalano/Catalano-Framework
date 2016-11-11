/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic.SwarmIntelligence.PSO;

/**
 * Location.
 * Search space.
 * @author Diego Catalano
 */
public class Location {
    
    private double[] location;

    /**
     * Get value.
     * @return Value.
     */
    public double[] getValue() {
        return location;
    }

    /**
     * Set value.
     * @param location value.
     */
    public void setValue(double[] location) {
        this.location = location;
    }

    /**
     * Initializes a new instance of the Location class.
     * @param location 
     */
    public Location(double[] location) {
        this.location = location;
    }
    
}