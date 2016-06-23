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
 * This interface encapsulates the number-crunching aspect of snakes.
 * @see OvusculeSnake2DKeeper
 * @see OvusculeSnake2DNode
 * @author Diego Catalano
 */
public interface IOvusculeSnake2D {

    /**
     * The purpose of this method is to compute the energy of the snake.
     * 
     * <para>This energy is usually made of three additive terms: 1) the image
     * energy, which gives the driving force associated to the data; 2) the
     * internal energy, which favors smoothness of the snake; and 3) the
     * constraint energy, which incorporates a priori knowledge. This
     * method is called repeatedly during the optimization of the snake. It
     * is <b>imperative</b> that this function be
     * <b>everywhere differentiable</b> with respect to the snake-defining
     * nodes.</para>
     * 
     * @return Return a number that should attain a minimal value when the
     * snake is optimal. Negative values are admissible.
     */
    public double energy ();

    /**
     * The purpose of this method is to compute the gradient of the snake
     * energy with respect to the snake-defining nodes.
     * 
     * <para>This method is called repeatedly during the optimization of the snake.
     * The optimization takes place under the control of the method
     * <code>OvusculeSnake2DKeeper.Optimize()</code>.
     * 
     * @return Return an array that contains the gradient values associated
     * to each node. They predict the variation of the energy for a
     * horizontal or vertical displacement of one pixel. The ordering of the
     * nodes must follow that of <code>getNodes()</code>. If
     * <code>null</code> is returned, the optimizer within the class
     * <code>OvusculeSnake2DKeeper</code> will attempt to estimate the
     * gradient by a finite-difference approach.
     * 
     * @see #getNodes
     * 
     */
    public DoublePoint[] getEnergyGradient ();

    /**
     * This method provides an accessor to the snake-defining nodes.
     * 
     * @return Return an array of subpixel node locations. It is expected
     * that the ordering of the nodes and the number of nodes does not
     * change during the lifetime of the snake.
     * 
     * @see #setNodes
     */
    public OvusculeSnake2DNode[] getNodes ();

    /**
     * The purpose of this method is to detemine what to draw on screen,
     * given the current configuration of nodes. This method is called
     * repeatedly during the user interaction provided by the method
     * <code>OvusculeSnake2DKeeper.interactAndOptimize()</code>. The origin
     * of coordinates lies at the top-left corner of the
     * <code>display</code> parameter. Collectively, the array of scales
     * forms the skin of the snake.
     * @return Return an array of <code>OvusculeSnake2DScale</code> objects.
     * Straight lines will be drawn between the apices of each polygon, in
     * the specified color. It is not necessary to maintain a constant
     * number of polygons in the array, or a constant number of apices in a
     * given polygon.
     * 
     * @see OvusculeSnake2DScale
     * 
     */
    public OvusculeSnake2DScale[] getScales ();

    /**
     * This method provides a mutator to the snake-defining nodes. It will
     * be called repeatedly by the method <code>OvusculeSnake2DKeeper.Optimize()</code>.
     * @param node Array of subpixel node locations.
     */
    public void setNodes (OvusculeSnake2DNode[] node);
}