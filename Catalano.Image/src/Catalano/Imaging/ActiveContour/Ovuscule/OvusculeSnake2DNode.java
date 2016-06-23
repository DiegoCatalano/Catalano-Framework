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

import Catalano.Core.DoublePoint;

/**
 * This class is used to store the snake-defining parameters. It extends
 * the capabilities of the class <code>DoublePoint</code> by
 * additional state variables.
 * @author Diego Catalano
 */
public class OvusculeSnake2DNode extends DoublePoint {

    /**
     * Methods of the class <code>OvusculeSnake2DKeeper</code> are allowed
     * to modify <code>OvusculeSnake2DNode</code> objects only if their
     * <code>frozen</code> flag is set to <code>false</code>. 
     */
    public boolean frozen = false;
    
    /**
     * Methods of the class <code>OvusculeSnake2DKeeper</code> are allowed
     * to interactively display (as a cross) the nodes stored in
     * <code>OvusculeSnake2DNode</code> objects only if their
     * <code>hidden</code> flag is set to <code>false</code>. 
     */
    public boolean hidden = false;

    @Override
    public String toString () {
            return("[" + super.toString()
                    + ", frozen: " + frozen
                    + ", hidden: " + hidden
                    + "]"
            );
    }

    /**
     * This constructor builds a point that is initially neither frozen nor hidden.
     * @param x The horizontal coordinate.
     * @param y The vertical coordinate.
     */
    public OvusculeSnake2DNode (double x, double y) {
        super(x, y);
    }

    /**
     * This constructor builds a point with the given initial values.
     * @param x The horizontal coordinate.
     * @param y The vertical coordinate.
     * @param frozen Set to <code>false</code> to allow methods of the class
     * <code>OvusculeSnake2DKeeper</code> to modify this point. Set to
     * <code>true</code> otherwise.
     * @param hidden Set to <code>false</code> to allow methods of the class
     * <code>OvusculeSnake2DKeeper</code> to display this point. Set to
     * <code>true</code> otherwise.
     */
    public OvusculeSnake2DNode (double x, double y, boolean frozen, boolean hidden) {
        super(x, y);
        this.frozen = frozen;
        this.hidden = hidden;
    }
}