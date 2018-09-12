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
 * Constraint for Linear and Mixed Integer Linear Programming.
 * 
 * @author Diego Catalano
 */
public class Constraint {
    
    /**
     * Represents symbol in the constraint.
     */
    public enum Symbol{
        LESS_THAN,
        EQUAL_TO,
        GREATER_THAN
    }
    
    private Symbol symbol;
    
    private double[] leftSide;
    private double rightSide;

    /**
     * Get left side values.
     * @return Left side values.
     */
    public double[] getLeftSide() {
        return leftSide;
    }

    /**
     * Get right side values.
     * @return Right side values.
     */
    public double getRightSide() {
        return rightSide;
    }

    /**
     * Get symbol.
     * @return Symbol.
     */
    public Symbol getSymbol() {
        return symbol;
    }
    
    /**
     * Initialize a new instance of the Constraint class.
     * @param leftSide Left side values.
     * @param symbol Symbol.
     * @param rightSide Right side values.
     */
    public Constraint(double[] leftSide, Symbol symbol, double rightSide){
        this(leftSide, rightSide, symbol);
    }

    /**
     * Initialize a new instance of the Constraint class.
     * @param leftSide Left side values.
     * @param rightSide Right side values.
     * @param symbol Symbol.
     */
    public Constraint(double[] leftSide, double rightSide, Symbol symbol) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        this.symbol = symbol;
    }
    
}
