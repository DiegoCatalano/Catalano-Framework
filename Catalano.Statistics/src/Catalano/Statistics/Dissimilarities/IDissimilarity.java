/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Catalano.Statistics.Dissimilarities;

import Catalano.Math.Distances.IDistance;

/**
 *
 * @author Diego
 * @param <T>
 */
public interface IDissimilarity <T> extends IDistance<T>{

    @Override
    public double Compute(T u, T v);
    
}