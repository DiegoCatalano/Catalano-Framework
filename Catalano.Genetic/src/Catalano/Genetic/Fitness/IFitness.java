/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic.Fitness;

import Catalano.Genetic.BinaryChromossome;

/**
 *
 * @author Diego
 */
public interface IFitness {
    double Compute(BinaryChromossome chromossome);
}