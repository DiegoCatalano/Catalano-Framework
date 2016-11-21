/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Genetic.Optimization;

import Catalano.Core.DoubleRange;
import java.util.List;

/**
 *
 * @author Diego
 */
public interface IOptimization {
    public double[] Compute(IObjectiveFunction function, List<DoubleRange> boundConstraint);
    public double getError();
}