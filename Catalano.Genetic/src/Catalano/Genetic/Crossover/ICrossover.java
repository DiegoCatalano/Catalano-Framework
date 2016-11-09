/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic.Crossover;

import Catalano.Genetic.BinaryChromossome;
import java.util.List;

/**
 *
 * @author Diego
 */
public interface ICrossover {
    List<BinaryChromossome> Compute(List<BinaryChromossome> chromossomes);
}