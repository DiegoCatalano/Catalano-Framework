// Catalano Statistics Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © AForge.NET, 2007-2013
// contacts@aforgenet.com
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

package Catalano.Statistics;

import Catalano.Core.FloatRange;

/**
 * Histogram for continuous random values.
 * @author Diego Catalano
 */
public class ContinuousHistogram {
    private int[] values;
    private FloatRange range;

    private float mean;
    private float stdDev;
    private float median;
    private float min;
    private float max;
    private int   total;

    /**
     * Values of the histogram.
     * @return
     */
    public int[] getValues() {
        return values;
    }

    /**
     * Mean value.
     * @return
     */
    public float getMean() {
        return mean;
    }

    /**
     * Standart Deviation value.
     * @return
     */
    public float getStdDev() {
        return stdDev;
    }

    /**
     * Median value.
     * @return
     */
    public float getMedian() {
        return median;
    }

    /**
     * Minimum value.
     * @return
     */
    public float getMin() {
        return min;
    }

    /**
     * Maximum value.
     * @return
     */
    public float getMax() {
        return max;
    }

    /**
     * Initializes a new instance of the ContinuousHistogram class.
     * @param values Values of the histogram.
     * @param range Range of random values.
     */
    public ContinuousHistogram(int[] values, FloatRange range){
        this.values = values;
        this.range = range;
        Update();
    }
    
    /**
     * Get range around median containing specified percentage of values.
     * @param percent Values percentage around median.
     * @return Returns the range which containes specifies percentage of values.
     */
    public FloatRange getRange(float percent){
        int min, max, hits;
        int h = (int) ( total * ( percent + ( 1 - percent ) / 2 ) );
        int n = values.length;
        int nM1 = n - 1;

        // skip left portion
        for ( min = 0, hits = total; min < n; min++ )
        {
            hits -= values[min];
            if ( hits < h )
                break;
        }
        // skip right portion
        for ( max = nM1, hits = total; max >= 0; max-- )
        {
            hits -= values[max];
            if ( hits < h )
                break;
        }
        // return range between left and right boundaries
        return new FloatRange(((float)min / nM1 ) * range.length() + range.getMin(),((float) max / nM1 ) * range.length() + range.getMin());
    }
    
    /**
     * Update statistical value of the histogram.
     */
    public void Update(){
        int hits;
        int i, n = values.length;
        int nM1 = n - 1;

        float rangeLength = range.length();
        float rangeMin = range.getMin();

        max    = 0;
        min    = n;
        mean   = 0;
        stdDev = 0;
        total  = 0;

        double sum = 0;

        // calculate mean, min, max
        for ( i = 0; i < n; i++ )
        {
            hits = values[i];

            if ( hits != 0 )
            {
                // max
                if ( i > max )
                    max = i;
                // min
                if ( i < min )
                    min = i;
            }

            // accumulate total value
            total += hits;
            // accumulate mean value
            sum += ( ( (double) i / nM1 ) * rangeLength + rangeMin ) * hits;
        }

        if ( total != 0 )
        {
            mean = (float) ( sum / total );
        }

        min = ( min / nM1 ) * rangeLength + rangeMin;
        max = ( max / nM1 ) * rangeLength + rangeMin;

        // calculate standard deviation
        sum = 0;
        double diff;

        for ( i = 0; i < n; i++ )
        {
            hits = values[i];
            diff = ( ( (double) i / nM1 ) * rangeLength + rangeMin ) - mean;
            sum += diff * diff * hits;
        }

        if ( total != 0 )
        {
            stdDev = (float) Math.sqrt( sum / total );
        }

        // calculate median
        int m, halfTotal = total / 2;

        for ( m = 0, hits = 0; m < n; m++ )
        {
            hits += values[m];
            if ( hits >= halfTotal )
                break;
        }
        median = ( (float) m / nM1 ) * rangeLength + rangeMin;
    }
    
}
