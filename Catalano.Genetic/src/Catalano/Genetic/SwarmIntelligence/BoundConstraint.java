/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic.SwarmIntelligence;

import Catalano.Core.DoubleRange;
import java.util.List;

/**
 *
 * @author Diego
 */
public class BoundConstraint {
    
    private List<DoubleRange> locationRange;
    private List<DoubleRange> velocityRange;

    public List<DoubleRange> getLocationRange() {
        return locationRange;
    }

    public void setLocationRange(List<DoubleRange> range) {
        this.locationRange = range;
    }

    public List<DoubleRange> getVelocityRange() {
        return velocityRange;
    }

    public void setVelocityRange(List<DoubleRange> velocityRange) {
        this.velocityRange = velocityRange;
    }

    public BoundConstraint(List<DoubleRange> locationRange, List<DoubleRange> velocityRange) {
        this.locationRange = locationRange;
        this.velocityRange = velocityRange;
    }
    
}