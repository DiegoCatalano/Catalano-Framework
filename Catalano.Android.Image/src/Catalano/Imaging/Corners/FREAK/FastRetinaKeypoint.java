// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2013
// diego.catalano at live.com
//
// Copyright © César Souza, 2009-2013
// cesarsouza at gmail.com
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

package Catalano.Imaging.Corners.FREAK;

/**
 * Fast Retina Keypoint (FREAK) point.
 * @author Diego Catalano
 */
public class FastRetinaKeypoint {
    
    /**
     * Gets or sets the x-coordinate of this point.
     */
    public double x,
    /**
     * Gets or sets the y-coordinate of this point.
     */
    y,
    /**
     * Gets or sets the scale of the point.
     */
    scale,
    /**
     * Gets or sets the orientation of this point in angles.
     */
    orientation;
    private byte[] descriptor;

    /**
     * Get the orientation of this point in angles.
     * @return Orientation.
     */
    public double getOrientation() {
        return orientation;
    }

    /**
     * Set the orientation of this point in angles.
     * @param orientation Orientation.
     */
    public void setOrientation(double orientation) {
        this.orientation = orientation;
    }

    /**
     * Get the scale of this point.
     * @return Scale.
     */
    public double getScale() {
        return scale;
    }

    /**
     * Set the scale of this point.
     * @param scale Scale.
     */
    public void setScale(double scale) {
        this.scale = scale;
    }

    /**
     * Gets the descriptor vector associated with this point.
     * @return Descriptor.
     */
    public byte[] getDescriptor() {
        return descriptor;
    }

    /**
     * Sets the descriptor vector associated with this point.
     * @param descriptor Descriptor.
     */
    public void setDescriptor(byte[] descriptor) {
        this.descriptor = descriptor;
    }

    /**
     * Initializes a new instance of the FastRetinaKeypoint class.
     * @param x The x-coordinate of the point in the image.
     * @param y The y-coordinate of the point in the image.
     */
    public FastRetinaKeypoint(double x, double y) {
        this.x = x;
        this.y = y;
    }
}