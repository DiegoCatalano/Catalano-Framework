// Catalano Statistics Library
// The Catalano Framework
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

package Catalano.Statistics.Kernels;

import java.io.Serializable;

/**
 * Kernel function interface.
 * <para> In Machine Learning and statistics, a Kernel is a function that returns
 * the value of the dot product between the images of the two arguments.</para>
 * @author Diego Catalano
 */
public interface IMercerKernel<T> extends Serializable {
    
    /**
     * The kernel function.
     * @param x Vector x in input space.
     * @param y Vector <c>y</c> in input space.
     * @return Dot product in feature (kernel) space.
     */
    public double Function(T x, T y);
}