// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
// Copyright © Philippe Thevenaz, 2011
// philippe.thevenaz at epfl.ch
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

package Catalano.Imaging.ActiveContour.Ovuscule;


import Catalano.Imaging.Color;

/**
 * Used to store the scales that are used to draw the skin of the snake.
 * @author Diego Catalano
 */
public class OvusculeSnake2DScale {
    
    int[] xpoints;
    int[] ypoints;
    int npoints;

    /**
     * This is the color with which this scale will be drawn if it is part
     * of the optimal skin found so far in the course of the optimization.
     */
    public Color bestAttemptColor = null;

    /**
     * This is the color with which this scale will be drawn if it is part
     * of the skin being currently examined in the course of the
     * optimization.
     */
    public Color currentAttemptColor = null;

    /**
     * This flag is used to determine how to draw the outline of this scale.
     * If it is set to <code>true</code>, then the first and the last point
     * of the polygon are joined. Otherwise, if it is set to
     * <code>false</code>, then the first and the last point of the polygon
     * are not joined. A closed scale can be filled.
     */
    public boolean closed = true;

    /**
     * This flag is used to determine how to draw the interior of this
     * scale. If it is set to <code>true</code>, then the scale is filled.
     * Otherwise, if it is set to <code>false</code>, then only the outline
     * of the scale is drawn. The status of this flag is honored only when
     * the scale is closed, as indicated by the <code>closed</code> flag.
     */
    public boolean filled = false;

    public OvusculeSnake2DScale () {}

    public OvusculeSnake2DScale (final Color bestAttemptColor, final Color currentAttemptColor, final boolean closed, final boolean filled) {
        this.bestAttemptColor = bestAttemptColor;
        this.currentAttemptColor = currentAttemptColor;
        this.closed = closed;
        this.filled = filled;
    }


    @Override
    public String toString () {
	return("[" + super.toString()
		+ ", bestAttemptColor: " + bestAttemptColor
		+ ", currentAttemptColor: " + currentAttemptColor
		+ ", closed: " + closed
		+ ", filled: " + filled
		+ "]"
	);
    }
}