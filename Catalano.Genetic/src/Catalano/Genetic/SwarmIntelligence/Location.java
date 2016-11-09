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
public class Location {
    
    private double[] location;

    public double[] getValue() {
        return location;
    }

    public void setValue(double[] location) {
        this.location = location;
    }

    public Location(double[] location) {
        this.location = location;
    }
    
}