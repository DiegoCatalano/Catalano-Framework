// Catalano Fuzzy Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2013
// diego.catalano at live.com
//
// Copyright © Andrew Kirillov, 2007-2008
// andrew.kirillov at gmail.com
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

package Catalano.Fuzzy;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements the centroid defuzzification method.
 * @author Diego Catalano
 */
public class CentroidDefuzzifier implements IDefuzzifier{

    // number of intervals to use in numerical approximation
    private int intervals;

    /**
     * Initializes a new instance of the CentroidDefuzifier class.
     * @param intervals Number of segments that the speech universe will be splited to perform the numerical approximation of the center of area.
     */
    public CentroidDefuzzifier(int intervals) {
        this.intervals = intervals;
    }
    
    @Override
    public float Defuzzify(FuzzyOutput fuzzyOutput, INorm normOperator) {
        // results and accumulators
        float weightSum = 0, membershipSum = 0;

        // speech universe
        float start = fuzzyOutput.getOutputVariable().getStart();
        float end = fuzzyOutput.getOutputVariable().getEnd();

        // increment
        float increment = ( end - start ) / this.intervals;

        // running through the speech universe and evaluating the labels at each point
        for ( float x = start; x < end; x += increment )
        {
            // we must evaluate x membership to each one of the output labels
            for ( FuzzyOutput.OutputConstraint oc : fuzzyOutput.getOutputList() )
            {
                // getting the membership for X and constraining it with the firing strength
                float membership = fuzzyOutput.getOutputVariable().GetLabelMembership(oc.getLabel(), x);
                float constrainedMembership = normOperator.Evaluate( membership, oc.getFiringStrength());

                weightSum += x * constrainedMembership;
                membershipSum += constrainedMembership;
            }
        }

        // if no membership was found, then the membershipSum is zero and the numerical output is unknown.
        if ( membershipSum == 0 )
            try {
            throw new Exception( "The numerical output in unavaliable. All memberships are zero." );
        } catch (Exception ex) {
            Logger.getLogger(CentroidDefuzzifier.class.getName()).log(Level.SEVERE, null, ex);
        }

        return weightSum / membershipSum;
    }
    
}
