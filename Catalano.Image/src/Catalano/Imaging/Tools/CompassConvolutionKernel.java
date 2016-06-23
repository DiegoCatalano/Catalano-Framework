// Catalano Imaging Library
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
package Catalano.Imaging.Tools;

/**
 * Compass Kernels for convolution filter.
 * @author Diego Catalano
 */
public final class CompassConvolutionKernel{

    /**
     * Don't let anyone instantiate this class.
     */
    private CompassConvolutionKernel() {}
    
    /**
     * Isotropic compass edge detection mask: North.
     */
    public static float[][] Isotropic_North = {
        {-1, 0, 1},
        {-1.41421356f, 0, 1.41421356f},
        {-1, 0, 1}};
    
    /**
     * Isotropic compass edge detection mask: Northwest.
     */
    public static float[][] Isotropic_Northwest = {
        {0, 1, 1.41421356f},
        {-1, 0, 1},
        {-1.41421356f, -1, 0}};
    
    /**
     * Isotropic compass edge detection mask: West.
     */
    public static float[][] Isotropic_West = {
        {1, 1.41421356f, 1},
        {0, 0, 0},
        {-1, -1.41421356f, -1}};
    
    /**
     * Isotropic compass edge detection mask: Southwest.
     */
    public static float[][] Isotropic_Southwest = {
        {1.41421356f, 1, 0},
        {1, 0, -1},
        {0, -1, -1.41421356f}};
    
    /**
     * Isotropic compass edge detection mask: South.
     */
    public static float[][] Isotropic_South = {
        {1, 0, -1},
        {1.41421356f, 0, -1.41421356f},
        {1, 0, -1}};
    
    /**
     * Isotropic compass edge detection mask: Southeast.
     */
    public static float[][] Isotropic_Southeast = {
        {0, -1, -1.41421356f},
        {1, 0, -1},
        {1.41421356f, 1, 0}};
    
    /**
     * Isotropic compass edge detection mask: East.
     */
    public static float[][] Isotropic_East = {
        {-1, -1.41421356f, -1},
        {0, 0, 0},
        {1, 1.41421356f, 1}};
    
    /**
     * Isotropic compass edge detection mask: Northeast.
     */
    public static float[][] Isotropic_Northeast = {
        {-1.41421356f, -1, 0},
        {-1, 0, 1},
        {0, 1, 1.41421356f}};
    
    /**
     * Kirsch compass edge detection mask: North.
     */
    public static int[][] Kirsch_North = {
        {-3, -3, 5},
        {-3, 0, 5},
        {-3, -3, 5}};
    
    /**
     * Kirsch compass edge detection mask: Northwest.
     */
    public static int[][] Kirsch_Northwest = {
        {-3, 5, 5},
        {-3, 0, 5},
        {-3, -3, -3}};
    
    /**
     * Kirsch compass edge detection mask: West.
     */
    public static int[][] Kirsch_West = {
        {5, 5, 5},
        {-3, 0, -3},
        {-3, -3, -3}};
    
    /**
     * Kirsch compass edge detection mask: Southwest.
     */
    public static int[][] Kirsch_Southwest = {
        {5, 5, -3},
        {5, 0, -3},
        {-3, -3, -3}};
    
    /**
     * Kirsch compass edge detection mask: South.
     */
    public static int[][] Kirsch_South = {
        {5, -3, -3},
        {5, 0, -3},
        {5, -3, -3}};
    
    /**
     * Kirsch compass edge detection mask: Southeast.
     */
    public static int[][] Kirsch_Southeast = {
        {-3, -3, -3},
        {5, 0, -3},
        {5, 5, -3}};
    
    /**
     * Kirsch compass edge detection mask: East.
     */
    public static int[][] Kirsch_East = {
        {-3, -3, -3},
        {-3, 0, -3},
        {5, 5, 5}};
    
    /**
     * Kirsch compass edge detection mask: Northeast.
     */
    public static int[][] Kirsch_Northeast = {
        {-3, -3, -3},
        {-3, 0, 5},
        {-3, 5, 5}};
    
    /**
     * Prewitt compass edge detection mask: North.
     */
    public static int[][] Prewitt_North = {
        {-1, 0, 1},
        {-1, 0, 1},
        {-1, 0, 1}};
    
    /**
     * Prewitt compass edge detection mask: Northwest.
     */
    public static int[][] Prewitt_Northwest = {
        {0, 1, 1},
        {-1, 0, 1},
        {-1, -1, 0}};
    
    /**
     * Prewitt compass edge detection mask: West.
     */
    public static int[][] Prewitt_West = {
        {1, 1, 1},
        {0, 0, 0},
        {-1, -1, -1}};
    
    /**
     * Prewitt compass edge detection mask: Southwest.
     */
    public static int[][] Prewitt_Southwest = {
        {1, 1, 0},
        {1, 0, -1},
        {0, -1, -1}};
    
    /**
     * Prewitt compass edge detection mask: South.
     */
    public static int[][] Prewitt_South = {
        {1, 0, -1},
        {1, 0, -1},
        {1, 0, -1}};
    
    /**
     * Prewitt compass edge detection mask: Southeast.
     */
    public static int[][] Prewitt_Southeast = {
        {0, -1, -1},
        {1, 0, -1},
        {1, 1, 0}};
    
    /**
     * Prewitt compass edge detection mask: East.
     */
    public static int[][] Prewitt_East = {
        {-1, -1, -1},
        {0, 0, 0},
        {1, 1, 1}};
    
    /**
     * Prewitt compass edge detection mask: Northeast.
     */
    public static int[][] Prewitt_Northeast = {
        {-1, -1, 0},
        {-1, 0, 1},
        {0, 1, 1}};
    
    /**
     * Robinson compass edge detection mask: North.
     */
    public static int[][] Robinson_North = {
        {-1, 0, 1},
        {-2, 0, 2},
        {-1, 0, 1}};
    
    /**
     * Robinson compass edge detection mask: Northwest.
     */
    public static int[][] Robinson_Northwest = {
        {0, 1, 2},
        {-1, 0, 1},
        {-2, -1, 0}};
    
    /**
     * Robinson compass edge detection mask: West.
     */
    public static int[][] Robinson_West = {
        {1, 2, 1},
        {0, 0, 0},
        {-1, -2, -1}};
    
    /**
     * Robinson compass edge detection mask: Southwest.
     */
    public static int[][] Robinson_Southwest = {
        {2, 1, 0},
        {1, 0, -1},
        {0, -1, -2}};
    
    /**
     * Robinson compass edge detection mask: South.
     */
    public static int[][] Robinson_South = {
        {1, 0, -1},
        {2, 0, -2},
        {1, 0, -1}};
    
    /**
     * Robinson compass edge detection mask: Southeast.
     */
    public static int[][] Robinson_Southeast = {
        {0, -1, -2},
        {1, 0, -1},
        {2, 1, 0}};
    
    /**
     * Robinson compass edge detection mask: East.
     */
    public static int[][] Robinson_East = {
        {-1, -2, -1},
        {0, 0, 0},
        {1, 2, 1}};
    
    /**
     * Robinson compass edge detection mask: Northeast.
     */
    public static int[][] Robinson_Northeast = {
        {-2, -1, 0},
        {-1, 0, 1},
        {0, 1, 2}};
    
    /**
     * Scharr compass edge detection mask: North.
     */
    public static int[][] Scharr_North = {
        {-1, 0, 1},
        {-3, 0, 3},
        {-1, 0, 1}};
    
    /**
     * Scharr compass edge detection mask: Northwest.
     */
    public static int[][] Scharr_Northwest = {
        {0, 1, 3},
        {-1, 0, 1},
        {-3, -1, 0}};
    
    /**
     * Scharr compass edge detection mask: West.
     */
    public static int[][] Scharr_West = {
        {1, 3, 1},
        {0, 0, 0},
        {-1, -3, -1}};
    
    /**
     * Scharr compass edge detection mask: Southwest.
     */
    public static int[][] Scharr_Southwest = {
        {3, 1, 0},
        {1, 0, -1},
        {0, -1, -3}};
    
    /**
     * Scharr compass edge detection mask: South.
     */
    public static int[][] Scharr_South = {
        {1, 0, -1},
        {3, 0, -3},
        {1, 0, -1}};
    
    /**
     * Scharr compass edge detection mask: Southeast.
     */
    public static int[][] Scharr_Southeast = {
        {0, -1, -3},
        {1, 0, -1},
        {3, 1, 0}};
    
    /**
     * Scharr compass edge detection mask: East.
     */
    public static int[][] Scharr_East = {
        {-1, -3, -1},
        {0, 0, 0},
        {1, 3, 1}};
    
    /**
     * Scharr compass edge detection mask: Northeast.
     */
    public static int[][] Scharr_Northeast = {
        {-3, -1, 0},
        {-1, 0, 1},
        {0, 1, 3}};
    
    /**
     * Sobel compass edge detection mask: North.
     */
    public static int[][] Sobel_North = {
        {-1, 0, 1},
        {-2, 0, 2},
        {-1, 0, 1}};
    
    /**
     * Sobel compass edge detection mask: Northwest.
     */
    public static int[][] Sobel_Northwest = {
        {0, 1, 2},
        {-1, 0, 1},
        {-2, -1, 0}};
    
    /**
     * Sobel compass edge detection mask: West.
     */
    public static int[][] Sobel_West = {
        {1, 2, 1},
        {0, 0, 0},
        {-1, -2, -1}};
    
    /**
     * Sobel compass edge detection mask: Southwest.
     */
    public static int[][] Sobel_Southwest = {
        {2, 1, 0},
        {1, 0, -1},
        {0, -1, -2}};
    
    /**
     * Sobel compass edge detection mask: South.
     */
    public static int[][] Sobel_South = {
        {1, 0, -1},
        {2, 0, -2},
        {1, 0, -1}};
    
    /**
     * Sobel compass edge detection mask: Southeast.
     */
    public static int[][] Sobel_Southeast = {
        {0, -1, -2},
        {1, 0, -1},
        {2, 1, 0}};
    
    /**
     * Sobel compass edge detection mask: East.
     */
    public static int[][] Sobel_East = {
        {-1, -2, -1},
        {0, 0, 0},
        {1, 2, 1}};
    
    /**
     * Sobel compass edge detection mask: Northeast.
     */
    public static int[][] Sobel_Northeast = {
        {-2, -1, 0},
        {-1, 0, 1},
        {0, 1, 2}};
}