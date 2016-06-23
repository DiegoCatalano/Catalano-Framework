// Catalano Math Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
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

package Catalano.Math.Wavelets;

/**
 * Common interface for wavelets algorithms.
 * @author Diego Catalano
 */
public interface IWavelet {
    /**
     * 1-D Forward Discrete Wavelet Transform.
     * @param data Data.
     */
    public void Forward(double[] data);
    
    /**
     * 2-D Forward Discrete Wavelet Transform.
     * @param data Data.
     */
    public void Forward(double[][] data);
    
    /**
     * 1-D Backwad Discrete Wavelet Transform.
     * @param data Data.
     */
    public void Backward(double[] data);
    
    /**
     * 2-D Backward Discrete Wavelet Transform.
     * @param data Data.
     */
    public void Backward(double[][] data);
}