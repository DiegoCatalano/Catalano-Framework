/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalano.Math.Distances;

/**
 *
 * @author Diego
 */
public class JaccardDistance implements IDistance{

    public JaccardDistance() {}

    @Override
    public double Compute(double[] x, double[] y) {
        double distance = 0;
        int intersection = 0, union = 0;

        for ( int i = 0; i < x.length; i++)
        {
            if ( ( x[i] != 0 ) || ( y[i] != 0 ) )
            {
                if ( x[i] == y[i] )
                {
                    intersection++;
                }

                union++;
            }
        }

        if ( union != 0 )
            distance = 1.0 - ( (double) intersection / (double) union );
        else
            distance = 0;

        return distance;
    }
}