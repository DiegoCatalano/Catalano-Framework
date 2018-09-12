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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

/**
 * Mixed Integer Linear Programming.
 * Branch and Bound method.
 * 
 * @author Diego Catalano
 */
public class MixedIntegerLinearProgramming {
    
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
    
    private double tolL = 1e-5;
    private double tolU = 1D - 1e-5;
    private Objective objective;
    
    private LinearProgramming simplex;
    private int maxIteration = 100;
    
    private int[] type;
    
    private Solution sol;

    /**
     * Set type.
     * 1 means integer, 0 means double.
     * 
     * @param type Type.
     */
    public void setType(int[] type) {
        this.type = type;
    }
    
    /**
     * Get the coefficients.
     * @return Coefficients.
     */
    public double[] getCoefficients(){
        return sol.coef;
    }
    
    /**
     * Get the solution.
     * @return Solution.
     */
    public double getSolution(){
        return sol.z;
    }

    /**
     * Initialize a new instance of the MixedIntegerLinearProgramming class.
     * @param objective Objective.
     */
    public MixedIntegerLinearProgramming(Objective objective) {
        this(objective, 1E-5);
    }
    
    /**
     * Initialize a new instance of the MixedIntegerLinearProgramming class.
     * @param objective Objective.
     * @param tol Tolerance for round the integer.
     */
    public MixedIntegerLinearProgramming(Objective objective, double tol){
        this.objective = objective;
        this.tolL = tol;
        this.tolU = 1D - tol;
    }
    
    /**
     * Solve the problem.
     * @param function Function.
     * @param constraint List of constraints.
     * @return Status of the MILP.
     */
    public int Solve(double[] function, List<Constraint> constraint){
        
        int iter = 0;
        
        if(type == null)
            throw new IllegalArgumentException("The data type must be definied.");
        
        
        //Calculate node 0
        double value = Double.NaN;
        if(objective == Objective.Maximize){
            simplex = new LinearProgramming(LinearProgramming.Objective.Maximize);
        }
        else{
            simplex = new LinearProgramming(LinearProgramming.Objective.Minimize);
        }
        
        //Initial solution
        int status = simplex.Solve(function, constraint);
        if(status == 1){
            boolean c = CheckSolution(simplex.getCoefficients(), function.length);
            if(c){
                return 1;
            }
            else{
                
                //Initial bb
                Stack<List<Constraint>> stack = new Stack<List<Constraint>>();
                BranchAndBound(stack, constraint, simplex.getCoefficients(), function.length);
                
                List<Solution> solutions = new ArrayList<Solution>();
                
                while(stack.size() > 0){
                    
                    if(iter == maxIteration) break;
                    iter++;
                    
                    List<Constraint> p = stack.pop();
                    int s = simplex.Solve(function, p);
                    
                    if(s == 1){
                        
                        boolean b = CheckSolution(simplex.getCoefficients(), function.length);
                        
                        if(objective == Objective.Maximize){
                            if(b){
                                solutions.add(new Solution(simplex.getCoefficients(), simplex.getSolution()));
                                
                                if(Double.isNaN(value)){
                                    value = simplex.getSolution();
                                }
                                
                                value = Math.max(value, simplex.getSolution());
                                
                            }
                            else{
                                if(Double.isNaN(value))
                                    BranchAndBound(stack, p, simplex.getCoefficients(), function.length);
                                
                                if(Double.isNaN(value) == false && simplex.getSolution() > value){
                                    BranchAndBound(stack, p, simplex.getCoefficients(), function.length);
                                }
                            }
                        }
                        else{
                            if(b){
                                solutions.add(new Solution(simplex.getCoefficients(), simplex.getSolution()));
                                
                                if(Double.isNaN(value)){
                                    value = simplex.getSolution();
                                }
                                
                                value = Math.min(value, simplex.getSolution());
                                
                            }
                            else{
                                if(Double.isNaN(value))
                                    BranchAndBound(stack, p, simplex.getCoefficients(), function.length);
                                
                                if(Double.isNaN(value) == false && simplex.getSolution() < value){
                                    BranchAndBound(stack, p, simplex.getCoefficients(), function.length);
                                }
                            }
                        }
                    }
                }
                
                if(solutions.isEmpty()) return 0;
                
                if(objective == Objective.Maximize){
                    Collections.sort(solutions, new Comparator<Solution>() {
                        @Override
                        public int compare(Solution o1, Solution o2) {
                            return Double.compare(o2.z, o1.z);
                        }
                    });
                }
                else{
                    Collections.sort(solutions, new Comparator<Solution>() {
                        @Override
                        public int compare(Solution o1, Solution o2) {
                            return Double.compare(o1.z, o2.z);
                        }
                    });
                }
                
                sol = solutions.get(0);
                
                return 1;
                
            }
        }
        
        return status;
        
    }
    
    private void BranchAndBound(Stack<List<Constraint>> stack, List<Constraint> constraint, double[] solution, int n){
        
        int index = GetUpperValue(solution, n);
        
        double lower = Math.floor(solution[index]);
        double upper = Math.ceil(solution[index]);
        
        double[] v = new double[n];
        v[index] = 1;
        
        List<Constraint> c1 = new ArrayList<Constraint>(constraint);
        c1.add(new Constraint(v, Constraint.Symbol.GREATER_THAN, upper));
        
        List<Constraint> c2 = new ArrayList<Constraint>(constraint);
        c2.add(new Constraint(v, Constraint.Symbol.LESS_THAN, lower));
        
        stack.add(c1);
        stack.add(c2);
        
    }
    
    private int GetUpperValue(double[] solution, int n){
        
        double min = -Double.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < n; i++) {
            if(solution[i] % 1 != 0){
                if(solution[i] > min){
                    min = Math.max(min, solution[i]);
                    index = i;
                }
            }

        }
        
        return index;
        
    }
    
    private boolean CheckSolution(double[] solution, int n){
        
        for (int i = 0; i < n; i++) {
            if(type[i] == 1){
                if(isInteger(solution[i]) == false)
                    return false;
            }

        }
        
        return true;
        
    }
    
    /**
     * Check if the value is integer within a certain tolerance.
     * @param value Value.
     * @return True if the value is integer, otherwise false.
     */
    private boolean isInteger(double value){
        double d = Math.abs(Math.floor(value) - value);
        if(d < tolL) return true;
        if(d - tolU >= 0) return true;
        return d < tolL;
    }
        
    class Solution {
        private double[] coef;
        private double z;

        public Solution(double[] coef, double z) {
            this.coef = coef;
            this.z = z;
        }
        
    }
}