// Catalano Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2015
// diego.catalano at live.com
//
// Copyright © Peter Kovesi, 2002
// pk at csse uwa edu au
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

package Catalano.Imaging.Filters;

import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.IBaseInPlace;
import Catalano.Math.Matrix;

/**
 * Perona-Malik Anisotropic diffusion.
 * 
 * <p>  Is a technique aiming at reducing image noise without removing significant parts
 * of the image content, typically edges, lines or other details that
 * are important for the interpretation of the image. </p>
 * 
 * @author Diego Catalano
 */
public class PeronaMalikAnisotropicDiffusion implements IBaseInPlace{
    
    /**
     * Diffusion equation.
     */
    public static enum Diffusion {
        /**
         * Favors high-contrast edges over low-contrast ones.
         */
        HighContrastEdges,
        
        /**
         * Favors wide regions over smaller ones.
         */
        WideRegions
    };
    
    private int iterations;
    private double kappa;
    private double lambda;
    private Diffusion diffusion;

    /**
     * Get number of iterations.
     * @return Number of iterations.
     */
    public int getIterations() {
        return iterations;
    }

    /**
     * Set number of iterations.
     * @param iterations Number of iterations.
     */
    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    /**
     * Get kappa value.
     * @return Kappa value,
     */
    public double getKappa() {
        return kappa;
    }

    /**
     * Set kappa value,
     * @param kappa Kappa value.
     */
    public void setKappa(double kappa) {
        this.kappa = kappa;
    }

    /**
     * Get lambda value.
     * @return Lambda value.
     */
    public double getLambda() {
        return lambda;
    }

    /**
     * Set lambda value,
     * @param lambda Lambda value.
     */
    public void setLambda(double lambda) {
        this.lambda = lambda;
    }

    /**
     * Get diffusion equation.
     * @return Diffusion equation.
     */
    public Diffusion getDiffusion() {
        return diffusion;
    }

    /**
     * Set diffusion equation.
     * @param diffusion Diffusion equation.
     */
    public void setDiffusion(Diffusion diffusion) {
        this.diffusion = diffusion;
    }

    /**
     * Initializes a new instance of the PeronaMalikAnisotropicDiffusion class.
     */
    public PeronaMalikAnisotropicDiffusion() {
        this(20);
    }
    
    /**
     * Initializes a new instance of the PeronaMalikAnisotropicDiffusion class.
     * @param iterations Number of iterations.
     */
    public PeronaMalikAnisotropicDiffusion(int iterations) {
        this(iterations,10,0.25,Diffusion.HighContrastEdges);
    }

    /**
     * Initializes a new instance of the PeronaMalikAnisotropicDiffusion class.
     * @param iterations Number of iterations.
     * @param kappa Controls conduction as a function of gradient.
     * @param lambda Controls the speed of diffusion.
     * @param diffusion Diffusion equation.
     */
    public PeronaMalikAnisotropicDiffusion(int iterations, double kappa, double lambda, Diffusion diffusion) {
        this.iterations = iterations;
        this.kappa = kappa;
        this.lambda = lambda;
        this.diffusion = diffusion;
    }
    
    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        if(fastBitmap.isGrayscale()){
            
            double[][] diff = new double[fastBitmap.getHeight()][fastBitmap.getWidth()];
            fastBitmap.toArrayGray(diff);
            
            for (int i = 0; i < iterations; i++) {
                
                //North diff
                double[][] deltaN = new double[diff.length][diff[0].length];
                for (int y = 0; y < deltaN[0].length; y++) {
                    deltaN[0][y] = -diff[0][y];
                }
                for (int x = 1; x < deltaN.length; x++) {
                    for (int y = 0; y < deltaN[0].length; y++) {
                        deltaN[x][y] = diff[x-1][y] - diff[x][y];
                    }
                }
                
                //South diff
                double[][] deltaS = new double[diff.length][diff[0].length];
                for (int x = 0; x < deltaS.length - 1; x++) {
                    for (int y = 0; y < deltaS[0].length; y++) {
                        deltaS[x][y] = diff[x+1][y] - diff[x][y];
                    }
                }
                for (int y = 0; y < deltaS[0].length; y++) {
                    deltaS[deltaS.length - 1][y] = -diff[deltaS.length - 1][y];
                }
                
                //East diff
                double[][] deltaE = new double[diff.length][diff[0].length];
                for (int x = 0; x < deltaE.length; x++) {
                    for (int y = 0; y < deltaE[0].length - 1; y++) {
                        deltaE[x][y] = diff[x][y+1] - diff[x][y];
                    }
                }
                for (int x = 0; x < deltaE.length; x++) {
                    deltaE[x][deltaE[0].length - 1] = -diff[x][deltaE[0].length - 1];
                }
                
                //West diff
                double[][] deltaW = new double[diff.length][diff[0].length];
                for (int x = 0; x < deltaW.length; x++) {
                    for (int y = 1; y < deltaW[0].length; y++) {
                        deltaW[x][y] = diff[x][y-1] - diff[x][y];
                    }
                }
                for (int x = 0; x < deltaW.length; x++) {
                    deltaW[x][0] = -diff[x][0];
                }
                
                if(diffusion == Diffusion.HighContrastEdges){
                    for (int x = 0; x < diff.length; x++) {
                        for (int y = 0; y < diff[0].length; y++) {
                            double cN = Math.exp(-Math.pow(deltaN[x][y] / kappa, 2));
                            double cS = Math.exp(-Math.pow(deltaS[x][y] / kappa, 2));
                            double cE = Math.exp(-Math.pow(deltaE[x][y] / kappa, 2));
                            double cW = Math.exp(-Math.pow(deltaW[x][y] / kappa, 2));
                            diff[x][y] = diff[x][y] + lambda * (deltaN[x][y]*cN + deltaS[x][y]*cS + deltaE[x][y]*cE + deltaW[x][y]*cW);
                        }
                    }
                }
                else{
                    for (int x = 0; x < diff.length; x++) {
                        for (int y = 0; y < diff[0].length; y++) {
                            double cN = 1 / (1 + Math.pow((deltaN[x][y] / kappa),2));
                            double cS = 1 / (1 + Math.pow((deltaS[x][y] / kappa),2));
                            double cE = 1 / (1 + Math.pow((deltaE[x][y] / kappa),2));
                            double cW = 1 / (1 + Math.pow((deltaW[x][y] / kappa),2));
                            diff[x][y] = diff[x][y] + lambda * (deltaN[x][y]*cN + deltaS[x][y]*cS + deltaE[x][y]*cE + deltaW[x][y]*cW);
                        }
                    }
                }
            }
            
            //Just clamp the values [0..255]
            double min = Matrix.Min(diff);
            double max = Matrix.Max(diff);
            for (int i = 0; i < diff.length; i++) {
                for (int j = 0; j < diff[0].length; j++) {
                    fastBitmap.setGray(i, j, (int)Catalano.Math.Tools.Scale(min, max, 0, 255, diff[i][j]));
                }
            }
        }
    }
}