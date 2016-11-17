// Catalano Genetic Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
//
//    This library is free software; you can redistribute it and/or
//    modify it under the terms of the GNU Lesser General Public
//    License as published by the Free Software Foundation; either
//    version 2.1 of the License, or (at your option) any later version.
//
//    This library is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//    Lesser General Public License for more details.
//
//    You should have received a copy of the GNU Lesser General Public
//    License along with this library; if not, write to the Free Software
//    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
//

package Catalano.Genetic.SwarmIntelligence.PSO;

import Catalano.Core.DoubleRange;
import Catalano.Genetic.SwarmIntelligence.BoundConstraint;
import Catalano.Genetic.SwarmIntelligence.IObjectiveFunction;
import Catalano.Math.Matrix;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Particle Swarm Optimization (PSO).
 * @author Diego Catalano
 */
public class ParticleSwarmOptimization {
    
    private int swarmSize;
    private int iterations;
    private long seed;
    
    private double[] pBest;
    private double[] fitness;
    
    private double gBest;
    private Location gBestLocation;
    
    private double w;
    private double C1;
    private double C2;
    private double W_UPPERBOUND = 1.0;
    private double W_LOWERBOUND = 0;
    
    private double minError = Double.MAX_VALUE;
    
    private Random random = new Random();
    
    private List<Location> pBestLocation = new ArrayList<Location>();
    private List<Particle> swarm = new ArrayList<Particle>();
    
    /**
     * Get minimum error of the function.
     * @return Minimum error found by PSO.
     */
    public double getError(){
        return minError;
    }

    /**
     * Initializes a new instance of the ParticleSwarmOptimization class.
     */
    public ParticleSwarmOptimization() {
        this(64,100);
    }
    
    /**
     * Initializes a new instance of the ParticleSwarmOptimization class.
     * @param swarm Number of swarms.
     * @param iterations Number of iterations.
     */
    public ParticleSwarmOptimization(int swarm, int iterations) {
        this(swarm, iterations, 1.5, 1.5, 0.9);
    }
    
    /**
     * Initializes a new instance of the ParticleSwarmOptimization class.
     * @param swarm Number of swarms.
     * @param iterations Number of iterations.
     * @param c1 Learning factor 1.
     * @param c2 Learning factor 2.
     * @param w Learning factor 0.
     */
    public ParticleSwarmOptimization(int swarm, int iterations, double c1, double c2, double w){
        this(swarm, iterations, c1, c2, w, 0);
    }
    
    /**
     * Initializes a new instance of the ParticleSwarmOptimization class.
     * @param swarm Number of swarms.
     * @param iterations Number of iterations.
     * @param c1 Learning factor 1.
     * @param c2 Learning factor 2.
     * @param w Learning factor 0.
     * @param seed Random seed.
     */
    public ParticleSwarmOptimization(int swarm, int iterations, double c1, double c2, double w, long seed){
        this.swarmSize = swarm;
        this.iterations = iterations;
        this.C1 = c1;
        this.C2 = c2;
        this.w = w;
        this.seed = seed;
        
        this.fitness = new double[swarmSize];
        this.pBest = new double[swarmSize];
    }
    
    /**
     * Optimize the function.
     * @param function Objective function.
     * @param constraint Constraint.
     * @return Best parameters.
     */
    public double[] Optimize(IObjectiveFunction function, BoundConstraint constraint){
        
        //Initialize the particle
        initializeSwarm(swarmSize, constraint, seed);
        
        //Update fitness for each swarm
        UpdateFitness(function);
        
        for (int i = 0; i < swarmSize; i++) {
            pBest[i] = fitness[i];
            pBestLocation.add(swarm.get(i).getLocation());
        }
        
        for (int i = 0; i < iterations; i++) {
            
            //1) Update pBest
            for(int s = 0; s < swarmSize; s++) {
                if(fitness[s] < pBest[s]) {
                    pBest[s] = fitness[s];
                    pBestLocation.set(s, swarm.get(s).getLocation());
                }
            }
            
            //2) Update gBest
            int bestParticleIndex = Matrix.MinIndex(fitness);
            if(i == 0 || fitness[bestParticleIndex] < gBest) {
                gBest = fitness[bestParticleIndex];
                gBestLocation = swarm.get(bestParticleIndex).getLocation();
            }
            
            //For each swarm
            for (int j = 0; j < swarmSize; j++) {
                double r1 = random.nextDouble();
                double r2 = random.nextDouble();

                Particle p = swarm.get(j);

                //Update velocity
                double[] newVelocity = new double[constraint.getLocationRange().size()];
                for (int k = 0; k < newVelocity.length; k++) {
                    newVelocity[k] = (w * p.getVelocity().getValue()[0]) + 
                                        (r1 * C1) * (pBestLocation.get(k).getValue()[0] - p.getLocation().getValue()[0]) +
                                        (r2 * C2) * (gBestLocation.getValue()[0] - p.getLocation().getValue()[0]);
                }
                
                Velocity vel = new Velocity(newVelocity);
                p.setVelocity(vel);

                //Update location
                double[] newLocation = new double[constraint.getLocationRange().size()];
                newLocation[0] = p.getLocation().getValue()[0] + newVelocity[0];
                newLocation[1] = p.getLocation().getValue()[1] + newVelocity[1];
                Location loc = new Location(newLocation);
                p.setLocation(loc);
                
                //Swap the swarm
                swarm.set(j, p);
            }
            
            minError = function.Compute(gBestLocation.getValue());
            UpdateFitness(function);
            
        }
        
        return gBestLocation.getValue();
        
    }
    
    private void initializeSwarm(int swarmSize, BoundConstraint constraint, long seed) {
        
        int size = constraint.getLocationRange().size();
        
        Random r = new Random();
        if(seed != 0) r.setSeed(seed);
        
        for(int i = 0; i < swarmSize; i++) {

            // randomize location inside a space defined in Problem Set
            double[] loc = new double[size];
            for (int j = 0; j < size; j++) {
                DoubleRange range = constraint.getLocationRange().get(j);
                loc[j] = range.getMin() + r.nextDouble() * (range.getMax() - range.getMin());
            }
            Location location = new Location(loc);

            // randomize velocity in the range defined in Problem Set
            double[] vel = new double[size];
            for (int j = 0; j < size; j++) {
                DoubleRange range = constraint.getVelocityRange().get(j);
                vel[j] = range.getMin() + r.nextDouble() * (range.getMax() - range.getMin());
            }
            Velocity velocity = new Velocity(vel);

            swarm.add(new Particle(location, velocity));
        }
    }
    
    private void UpdateFitness(IObjectiveFunction function){
        for (int i = 0; i < swarmSize; i++) {
            fitness[i] = function.Compute(swarm.get(i).getLocation().getValue());
        }
    }
    
}