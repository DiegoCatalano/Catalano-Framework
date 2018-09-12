// Catalano Math Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2018
// diego.catalano at live.com
//
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

package Catalano.Math.Optimization;

/**
 * Tableau Standard Form for Simplex.
 * @author Diego Catalano
 */
public class Tableau {
    
    private double[][] tableau;
    private double[] coefVars;
    private int[] basis;

    /**
     * Get Basis.
     * @return Basis.
     */
    public int[] getBasis() {
        return basis;
    }

    /**
     * Get the tableau.
     * @return Tableau.
     */
    public double[][] getTableau() {
        return tableau;
    }

    /**
     * Set the tableau.
     * @param tableau Tableau.
     */
    public void setTableau(double[][] tableau) {
        this.tableau = tableau;
    }

    /**
     * Get the coefficients from original variables.
     * @return Coefficients from the original variables.
     */
    public double[] getCoefVars() {
        return coefVars;
    }

    /**
     * Set the coefficients of the variables.
     * @param coefVars Coefficents.
     */
    public void setCoefVars(double[] coefVars) {
        this.coefVars = coefVars;
    }

    /**
     * Initialize a new instance of the Tableau class.
     * @param tableau Tableau.
     * @param coefVars Coefficients.
     * @param basis Basis.
     */
    public Tableau(double[][] tableau, double[] coefVars, int[] basis) {
        this.tableau = tableau;
        this.coefVars = coefVars;
        this.basis = basis;
    }
}
