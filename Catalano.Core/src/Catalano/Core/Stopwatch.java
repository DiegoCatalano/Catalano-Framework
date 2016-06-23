// Catalano Core Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
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

package Catalano.Core;

/**
 * Provides a set of methods and properties that you can use to accurately measure elapsed time.
 * @author Diego Catalano
 */
public class Stopwatch {
    
    private Long t1,t2,t3;

    /**
     * Initializes a new instance of the Stopwatch class.
     */
    public Stopwatch() {
    }
    
    /**
     * Start meansuring elapsed time for an interval.
     */
    public void Start(){
        t1 = System.currentTimeMillis();
    }
    
    /**
     * Stops meansuring elapsed time for an inverval.
     */
    public void Stop(){
        t2 = System.currentTimeMillis();
        t3 = t2-t1;
        Reset();
    }
    
    /**
     * Stops time interval measurement and resets the elapsed time to zero.
     */
    public void Reset(){
        t1 = t2 = 0L;
    }
    
    /**
     * Stops time interval measurement, resets the elapsed time to zero, and starts meansuring elapsed time.
     */
    public void Restart(){
        Reset();
        t1 = System.currentTimeMillis();
    }
    
    /**
     * Gets the total elapsed time measured by the current instance, in milliseconds.
     * @return Milliseconds.
     */
    public Long ElapsedMilliseconds(){
        return t3;
    }
    
    /**
     * Gets a value indicating whether the timer is running.
     * @return Status.
     */
    public boolean isRunning(){
        return t1 != 0;
    }
}