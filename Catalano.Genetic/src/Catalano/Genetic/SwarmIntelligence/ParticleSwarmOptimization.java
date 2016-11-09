/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic.SwarmIntelligence;

import Catalano.Core.DoubleRange;
import Catalano.Math.Matrix;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Diego
 */
public class ParticleSwarmOptimization {
    
    private int swarmSize;
    private int iterations;
    private long seed;
    
    private double[] pBest;
    private double[] fitness;
    
    private double gBest;
    private Location gBestLocation;
    
    private double C1;
    private double C2;
    private double W_UPPERBOUND = 1.0;
    private double W_LOWERBOUND = 0;
    
    private Random random = new Random();
    
    private List<Location> pBestLocation = new ArrayList<Location>();
    private List<Particle> swarm = new ArrayList<Particle>();

    public ParticleSwarmOptimization() {
        this(64,100);
    }
    
    public ParticleSwarmOptimization(int swarm, int iterations) {
        this(swarm, iterations, 1.5, 1.5);
    }
    
    public ParticleSwarmOptimization(int swarm, int iterations, double c1, double c2){
        this(swarm, iterations, c1, c2, 0);
    }
    
    public ParticleSwarmOptimization(int swarm, int iterations, double c1, double c2, long seed){
        this.swarmSize = swarm;
        this.iterations = iterations;
        this.C1 = c1;
        this.C2 = c2;
        this.seed = seed;
        
        this.fitness = new double[swarmSize];
        this.pBest = new double[swarmSize];
    }
    
    public double[] Optimize(IFitness function, BoundConstraint constraint){
        
        //Initialize the particle
        initializeSwarm(swarmSize, constraint, seed);
        
        //Update fitness for each swarm
        UpdateFitness(function);
        
        for (int i = 0; i < swarmSize; i++) {
            pBest[i] = fitness[i];
            pBestLocation.add(swarm.get(i).getLocation());
        }
        
        double w = 0;
        double err = 0;
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
            
            w = W_UPPERBOUND - (((double) i) / (double)iterations) * (W_UPPERBOUND - W_LOWERBOUND);
            
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
            
            err = function.Compute(gBestLocation.getValue());
            
            System.out.println("ITERATION " + i + ": ");
            System.out.println("     Best X: " + gBestLocation.getValue()[0]);
            System.out.println("     Best Y: " + gBestLocation.getValue()[1]);
            System.out.println("     Value: " + err);
            
            UpdateFitness(function);
            
        }
        
        return gBestLocation.getValue();
        
    }
    
    private void initializeSwarm(int swarmSize, BoundConstraint constraint, long seed) {
        Particle p;
        int size = constraint.getLocationRange().size();
        
        Random r = new Random();
        if(seed != 0) r.setSeed(seed);
        
        for(int i = 0; i < swarmSize; i++) {
            p = new Particle();

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

            p.setLocation(location);
            p.setVelocity(velocity);
            swarm.add(p);
        }
    }
    
    private void UpdateFitness(IFitness function){
        for (int i = 0; i < swarmSize; i++) {
            fitness[i] = function.Compute(swarm.get(i).getLocation().getValue());
        }
    }
    
}