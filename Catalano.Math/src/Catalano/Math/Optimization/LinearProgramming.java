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

import Catalano.Math.Matrix;
import java.util.Arrays;
import java.util.List;

/**
 * Linear Programming.
 * Two Phase Method.
 * 
 * @author Diego Catalano
 */
public class LinearProgramming {
    
    /**
     * The solution is infeasible.
     */
    public static final int INFEASIBLE = 0;
    
    /**
     * The solution exists.
     */
    public static final int OPTIMAL = 1;
    
    /**
     * The solution is unbounded.
     */
    public static final int UNBOUNDED = 2;
    
    /**
     * Objective of the function.
     */
    public enum Objective{
        
        /**
         * Minimize the function.
         */
        Minimize,
        
        /**
         * Maximize the function.
         */
        Maximize
    };
    
    private final static int SECOND_PHASE = -2;
    
    private double[] function;
    private Objective objective;
    
    private double[] r;
    private double solution;
    private boolean isInfinite;
    
    private int iterations;
    
    private List<Constraint> constraints;

    /**
     * Number of iterations required to be converged.
     * @return Number of iterations.
     */
    public int getIterations() {
        return iterations;
    }
    
    /**
     * Get the final solution.
     * @return Optimal objective value.
     */
    public double getSolution() {
        return solution;
    }

    /**
     * Get coefficients.
     * @return Coefficients.
     */
    public double[] getCoefficients() {
        return r;
    }

    /**
     * Linear Programming.
     * @param objective Objective.
     */
    public LinearProgramming(Objective objective) {
        this.objective = objective;
    }
    
    /**
     * Check if the solution is infinite.
     * @return True if exists one more solution, otherwise return false.
     */
    public boolean isInfinite(){
        return isInfinite;
    }
    
    /**
     * Solve the simplex problem.
     * @param function Function.
     * @param constraints List of constraints.
     * @return Status of the solution.
     */
    public int Solve(double[] function, List<Constraint> constraints){
        
        this.function = function;
        this.constraints = constraints;
        iterations = 0;
        isInfinite = false;
        
        //Create Tableau
        Tableau tableau = CreateTableau(function, constraints);
        
        double[][] tab = tableau.getTableau();
        double[] vars = tableau.getCoefVars();
        int[] basis = tableau.getBasis();
        
        int status = -1;
        
        while(true){
            
            iterations++;
            
            //Find column
            int q = findColumn(tab, vars, basis); //var in
            
            //Create another tableau, 2-phase
            if(q == SECOND_PHASE){
                tableau = CreateSecondTableau(tab, vars, basis);
                tab = tableau.getTableau();
                vars = tableau.getCoefVars();
                basis = tableau.getBasis();
            }
            
            //Check if the solution is unbounded
            if(q >= 0){
                if(isUnbounded(tab, q))
                    return 2;
            }
            
            if(q == -1){
                
                //Check if the solution is infeasible
                if(isInfeasible(tab, vars, basis))
                    return 0;
                
                //Optimal solution
                status = 1;
                break;
            }
            
            if(q >= 0){
                int p = findRow(tab, q, vars, basis); //var out

                //Change basis
                basis[p] = q;

                //Pivot element
                double pivot = tab[p][q];

                //Calculate new line
                for (int i = 0; i < tab[0].length; i++) {
                    tab[p][i] /= pivot;
                }

                //Calculate the rest of the tableau
                for (int i = 0; i < tab.length; i++) {
                    if(i != p){
                        pivot = tab[i][q];
                        for (int j = 0; j < tab[0].length; j++) {
                            tab[i][j] = tab[i][j] - pivot * tab[p][j];
                        }
                    }
                }
            }
        }
        
        //Compute the coefficients
        r = new double[vars.length];
        for (int i = 0; i < basis.length; i++) {
            r[basis[i]] = tab[i][tab[0].length - 1];
        }
        
        //Compute Z (The final solution)
        solution = 0;
        for (int i = 0; i < function.length; i++) {
            solution += function[i] * r[i];
        }
        
        if(objective == Objective.Minimize){
            for (int i = 0; i < function.length; i++) {
                function[i] *= -1;
            }
        }
        
        if(objective == Objective.Minimize)
            solution = -solution;
        
        r = Arrays.copyOf(r, function.length);
        return status;
        
    }
    
    private Tableau CreateSecondTableau(double[][] tableau, double[] vars, int[] basis){
        
        //Count how many artificial variables exists
        int c = 0;
        for (int i = 0; i < vars.length; i++) {
            if(vars[i] == -1) c++;
        }
        
        //Eliminate columns from tableau, vars and others.
        int[] index = new int[c];
        int idx = 0;
        for (int i = 0; i < vars.length; i++) {
            if(vars[i] == -1){
                index[idx] = i;
                idx++;
            }
        }
        
        tableau = Matrix.RemoveColumns(tableau, index);
        
        vars = new double[tableau[0].length];
        
        for (int i = 0; i < function.length; i++) {
            vars[i] = function[i];
        }
        
        return new Tableau(tableau, vars, basis);
        
    }
    
    private boolean isUnbounded(double[][] tableau, int q){
        int c = 0;
        for (int i = 0; i < tableau.length; i++) {
            if(tableau[i][q] <= 0) c++;
        }

        if(c == tableau.length) return true;
        
        return false;
    }
    
    private boolean isInfeasible(double[][] tableau, double[] artVariables, int[] basis){
        for (int i = 0; i < tableau.length; i++) {
            if((tableau[i][tableau[0].length - 1] > 0) && (artVariables[basis[i]] == -1)){
                return true;
            }
        }
        return false;
    }
    
    private int findColumn(double[][] tableau, double[] coef, int[] basis){

        //Calculate Z
        double[] z = new double[tableau[0].length - 1];
        for (int i = 0; i < tableau.length; i++) {
            for (int j = 0; j < tableau[0].length - 1; j++) {
                z[j] += tableau[i][j] * coef[basis[i]];
            }
        }
        
        //Calculate C-Z
        for (int i = 0; i < coef.length - 1; i++) {
            z[i] = z[i] - coef[i];
        }
        
        //Check if the solution is optimal
        boolean isOptimal = true;
        for (int i = 0; i < z.length; i++) {
            if(z[i] < 0)
                isOptimal = false;
        }
        
        //Check if Z right values is 0 too
        if(isOptimal){
            double sum = 0;
            for (int i = 0; i < tableau.length; i++) {
                sum += tableau[i][tableau[0].length - 1] * coef[basis[i]];
            }
            
            //Need to create the other tableau
            if(sum == 0){
                return -2;
            }
            
            //Check if the solution is infinite
            for (int i = 0; i < basis.length; i++) {
                coef[basis[i]] = Double.NaN;
            }
            
            int c = 0;
            for (int i = 0; i < z.length; i++)
                if(!Double.isNaN(coef[i]) && z[i] == 0) c++;
            
            
            if(c > 0) isInfinite = true;
            
            return -1;
            
        }
        
        if(isOptimal) return -1;
        
        return Matrix.MinIndex(z);
        
    }
    
    private int findRow(double[][] tableau, int q, double[] artVar, int[] basis){
        
        double min = Double.MAX_VALUE;
        double[] div = new double[tableau.length];
        for (int i = 0; i < div.length; i++) {
            if(tableau[i][q] > 0){
                div[i] = tableau[i][tableau[0].length - 1] / tableau[i][q];
                min = Math.min(min, div[i]);
            }
            else{
                div[i] = Double.MAX_VALUE;
            }
        }
        
        //Output variable draw
        //In the case if exists more than one same value of min, we need to choose artificial variable
        int c = 0;
        for (int i = 0; i < div.length; i++) {
            if(div[i] == min) c++;
        }
        
        if(c == 1) return Matrix.MinIndex(div);
        
        for (int i = 0; i < div.length; i++)
            if(div[i] == min && artVar[basis[i]] == -1)
                return i;
        
        
        return Matrix.MinIndex(div);
        
    }
    
    private Tableau CreateTableau(double[] function, List<Constraint> constraint){
        
        //Transform minimization problem in maximization
        if(objective == Objective.Minimize){
            for (int i = 0; i < function.length; i++) {
                function[i] *= -1;
            }
        }
        
        boolean hasArtificialVariable = false;
        int nBasic = 0;
        int nArtificial = 0;
        
        //Discover how many variables is necessary
        int vars = 0;
        for (Constraint c : constraint) {
            switch(c.getSymbol()){
                case LESS_THAN:
                    nBasic++;
                break;
                case EQUAL_TO:
                    nArtificial++;
                break;
                case GREATER_THAN:
                    nBasic++;
                    nArtificial++;
                break;
            }
        }
        
        vars = nBasic + nArtificial;
        if(nArtificial > 0) hasArtificialVariable = true;
        
        //Build our tableau
        double[][] tableau = new double[constraint.size()][function.length + vars + 1];
        
        int[] basis = new int[constraint.size()];
        for (int i = 0; i < basis.length; i++) {
            basis[i] = i;
        }
        
        //Write left side values
        for (int i = 0; i < constraint.size(); i++) {
            Constraint c = constraint.get(i);
            for (int j = 0; j < function.length; j++) {
                if(c.getRightSide() >= 0)
                    tableau[i][j] = c.getLeftSide()[j];
                else
                    tableau[i][j] = -c.getLeftSide()[j];
            }
        }
        
        //Write right side values
        for (int i = 0; i < constraint.size(); i++){
            Constraint c = constraint.get(i);
            if(c.getRightSide() >= 0)
                tableau[i][tableau[0].length - 1] = c.getRightSide();
            else
                tableau[i][tableau[0].length - 1] = -c.getRightSide();
        }
        
        //Coefficients
        double[] t = new double[function.length + vars + 1];
        
        if(!hasArtificialVariable){
            for (int i = 0; i < function.length; i++) {
                t[i] = function[i];
            }
        }
        
        //Initialize slack variables
        int movesSx = function.length;
        int movesAx = function.length + nBasic;
        for (int i = 0; i < constraint.size(); i++) {
            
            Constraint c = constraint.get(i);
            
            if(c.getRightSide() >= 0){
                switch(c.getSymbol()){
                    case LESS_THAN:
                        tableau[i][movesSx] = 1;
                        basis[i] = movesSx;

                        movesSx++;
                    break;
                    case EQUAL_TO:
                        tableau[i][movesAx] = 1;
                        basis[i] = movesAx;
                        t[movesAx] = -1;

                        movesAx++;
                    break;
                    case GREATER_THAN:
                        tableau[i][movesSx] = -1;
                        tableau[i][movesAx] = 1;
                        t[movesSx] = 0;
                        t[movesAx] = -1;
                        basis[i] = movesAx;

                        movesSx++;
                        movesAx++;
                    break;
                }
            }
            else{
                switch(c.getSymbol()){
                    case LESS_THAN:
                        tableau[i][movesSx] = -1;
                        tableau[i][movesAx] = 1;
                        t[movesSx] = 0;
                        t[movesAx] = -1;
                        basis[i] = movesAx;

                        movesSx++;
                        movesAx++;
                    break;
                    case EQUAL_TO:
                        tableau[i][movesAx] = 1;
                        basis[i] = movesAx;
                        t[movesAx] = -1;

                        movesAx++;
                    break;
                    case GREATER_THAN:
                        tableau[i][movesSx] = 1;
                        basis[i] = movesSx;

                        movesSx++;
                    break;
                }
            }
            
        }
        
        return new Tableau(tableau, t, basis);
        
    }
    
}