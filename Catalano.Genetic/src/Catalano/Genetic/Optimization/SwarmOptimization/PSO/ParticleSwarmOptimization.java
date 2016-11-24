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

package Catalano.Genetic.Optimization.SwarmOptimization.PSO;

import Catalano.Core.DoubleRange;
import Catalano.Genetic.Optimization.IObjectiveFunction;
import Catalano.Genetic.Optimization.IOptimization;
import Catalano.Math.Random.Pcg32;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Particle Swarm Optimization (PSO).
 * Need implements news weight (w). http://www.ijmlc.org/vol5/535-C037.pdf
 * @author Diego Catalano
 */
public class ParticleSwarmOptimization implements IOptimization{
    
    private int swarmSize;
    private int iterations;
    private long seed;
    
    private double gBest = Double.MAX_VALUE;
    private double[] gBestLocation;
    
    private double w;
    private double C1;
    private double C2;
    
    private Pcg32 random = new Pcg32();
    
    private List<Particle> swarm = new ArrayList<Particle>();
    
    /**
     * Get minimum error of the function.
     * @return Minimum error found by PSO.
     */
    public double getError(){
        return gBest;
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
    }

    @Override
    public double[] Compute(IObjectiveFunction function, List<DoubleRange> boundConstraint) {
        List<DoubleRange> velocity = new ArrayList(boundConstraint.size());
        for (int i = 0; i < boundConstraint.size(); i++) {
            double v = 0.2 * (boundConstraint.get(i).getMax() - boundConstraint.get(i).getMin());
            velocity.add(new DoubleRange(-v, v));
        }
        
        return Compute(function, boundConstraint, velocity);
        
    }
    
    /**
     * Optimize the function.
     * @param function Objective function.
     * @param boundConstraint Constraint.
     * @param velocity Velocity.
     * @return Best parameters.
     */
    public double[] Compute(IObjectiveFunction function, List<DoubleRange> boundConstraint, List<DoubleRange> velocity){
        
        //Initialize the particles
        Initialize(swarmSize, boundConstraint, function, seed);
        
        for (int i = 0; i < iterations; i++) {
            
            //For each swarm
            for (int j = 0; j < swarmSize; j++) {

                Particle p = swarm.get(j);

                //Update velocity
                double[] newVelocity = new double[boundConstraint.size()];
                for (int k = 0; k < newVelocity.length; k++) {
                    newVelocity[k] = (w * p.getVelocity()[k]) + 
                                        (random.nextDouble() * C1) * (p.getBestLocation()[k] - p.getLocation()[k]) +
                                        (random.nextDouble() * C2) * (gBestLocation[k] - p.getLocation()[k]);
                }
                
                //Fix constraint
                for (int k = 0; k < newVelocity.length; k++) {
                    newVelocity[k] = newVelocity[k] < velocity.get(k).getMin() ? velocity.get(k).getMin() : newVelocity[k];
                    newVelocity[k] = newVelocity[k] > velocity.get(k).getMax() ? velocity.get(k).getMax() : newVelocity[k];
                }
                p.setVelocity(newVelocity);

                //Update location
                double[] newLocation = new double[boundConstraint.size()];
                for (int k = 0; k < newLocation.length; k++) {
                    newLocation[k] = p.getLocation()[k] + newVelocity[k];
                }
                
                //Fix constraint
                for (int k = 0; k < newLocation.length; k++) {
                    newLocation[k] = newLocation[k] < boundConstraint.get(k).getMin() ? boundConstraint.get(k).getMin() : newLocation[k];
                    newLocation[k] = newLocation[k] > boundConstraint.get(k).getMax() ? boundConstraint.get(k).getMax() : newLocation[k];
                }
                p.setLocation(newLocation);
                
                p.setFitness(function.Compute(newLocation));
                
                if(p.getFitness() < p.getBestFitness()){
                    p.setBestLocation(p.getLocation());
                    p.setBestFitness(p.getFitness());
                    
                    if(p.getBestFitness() < gBest){
                        gBestLocation = p.getBestLocation();
                        gBest = p.getBestFitness();
                    }
                }
                
                //Swap the swarm
                swarm.set(j, p);
                
            }
            
            //Dumping factor
            w *= 0.99;
            
        }
        
        return gBestLocation;
        
    }
    
    private void Initialize(int swarmSize, List<DoubleRange> location, IObjectiveFunction function, long seed) {
        
        int size = location.size();
        
        if(seed != 0) random.setSeed(seed);
        
        for(int i = 0; i < swarmSize; i++) {

            // randomize location inside the bound constraint.
            double[] loc = new double[size];
            for (int j = 0; j < size; j++) {
                DoubleRange range = location.get(j);
                loc[j] = range.getMin() + random.nextDouble() * (range.getMax() - range.getMin());
            }

            // Initialize velocity.
            double[] vel = new double[size];
            
            Particle p = new Particle();
            p.setLocation(loc);
            p.setBestLocation(loc);
            p.setVelocity(vel);
            p.setFitness(function.Compute(loc));
            p.setBestFitness(p.getFitness());
            
            if(p.getFitness() < gBest){
                gBest = p.getFitness();
                gBestLocation = p.getLocation();
            }

            swarm.add(p);
        }
    }
}