// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright (c) 2006-2017 Wilhelm Burger, Mark J. Burge. All rights reserved.
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
// Original license from Wilhelm Burguer, Mark J. Burge.
//
//    Redistribution and use in source and binary forms, with or without
//    modification, are permitted provided that the following conditions are met: 
//
//    1. Redistributions of source code must retain the above copyright notice,  this
//       list of conditions and the following disclaimer. 
//    2. Redistributions in binary form must reproduce the  above  copyright  notice,
//       this list of conditions and the following disclaimer  in  the  documentation
//       and/or other materials provided with the distribution. 
//
//    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
//    ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,  THE  IMPLIED
//    WARRANTIES  OF  MERCHANTABILITY  AND  FITNESS  FOR A  PARTICULAR  PURPOSE   ARE 
//    DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
//    ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL  DAMAGES
//    (INCLUDING, BUT NOT LIMITED TO,  PROCUREMENT OF SUBSTITUTE GOODS  OR  SERVICES;
//    LOSS OF USE, DATA, OR PROFITS; OR  BUSINESS INTERRUPTION)  HOWEVER  CAUSED  AND
//    ON ANY THEORY OF LIABILITY, WHETHER IN  CONTRACT,  STRICT  LIABILITY,  OR  TORT
//    (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  OF  THIS
//    SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
//
//    The views and conclusions contained in the software and documentation are those
//    of the authors and should not be interpreted as representing official policies, 
//    either expressed or implied, of the FreeBSD Project.

package Catalano.Imaging.Tools;

import Catalano.Core.IntPoint;
import java.util.List;

public class Contour implements Comparable<Contour> {
	
    private int label;
    private List<IntPoint> points;

    /**
     * Get the label.
     * @return Label.
     */
    public int getLabel() {
        return label;
    }

    /**
     * Get points.
     * @return Points.
     */
    public List<IntPoint> getPoints() {
        return points;
    }

    /**
     * Get points as array.
     * @return Points.
     */
    public IntPoint[] getPointArray() {
        return points.toArray(new IntPoint[0]);
    }

    /**
     * Initializes a new instance of the Contour class.
     * @param label Label.
     * @param points List of points.
     */
    public Contour (int label, List<IntPoint> points) {
        this.label = label;
        this.points = points;
    }

    /**
     * Get the length of the contour.
     * @return Length.
     */
    public int getLength() {
        return points.size();
    }

    @Override
    public int compareTo(Contour c2) {
        return c2.points.size() - this.points.size();
    }
}