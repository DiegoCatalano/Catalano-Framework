// Catalano Math Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2019
// diego.catalano at live.com
//
// Copyright © Andrei Ciobanu, 2010-2019
// gnomemory at yahoo.com
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

package Catalano.Math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Polish Expression.
 * Is a mathematical notation in which operators precede their operands, in contrast to the more common infix notation.
 * 
 * Original implementation: http://andreinc.net/2010/10/05/converting-infix-to-rpn-shunting-yard-algorithm/
 * @author Diego Catalano
 */
public class PolishExpression {
    
    /**
     * Specifies the associativity.
     */
    public static enum Associativity{
        
        /**
         * Left association.
         */
        Left,
        
        /**
         * Right association.
         */
        Right
    }
    
    private static int LEFT = 0;
    private static int RIGHT = 1;
    
    private static final Map<String, int[]> OPERATORS = new HashMap<String, int[]>();
    static {
        // Map<"token", []{precendence, associativity}>
        OPERATORS.put("+", new int[] { 0, LEFT });
        OPERATORS.put("-", new int[] { 0, LEFT });
        OPERATORS.put("*", new int[] { 1, LEFT });
        OPERATORS.put("/", new int[] { 1, LEFT });
        OPERATORS.put("%", new int[] { 1, LEFT });
        OPERATORS.put("mod", new int[] { 1, LEFT });
        OPERATORS.put("abs", new int[] { 1, RIGHT });
        OPERATORS.put("ln", new int[] { 1, RIGHT });
        OPERATORS.put("sin", new int[] { 1, RIGHT });
        OPERATORS.put("cos", new int[] { 1, RIGHT });
        OPERATORS.put("sum", new int[] { 1, RIGHT });
        OPERATORS.put("^", new int[] { 2, RIGHT });
        OPERATORS.put("pow", new int[] { 2, RIGHT });
    }

    /**
     * Initializes a new instance of the PolishExpression class.
     */
    public PolishExpression() {}
    
    /**
     * Add a operator into polish expression.
     * @param symbol Symbol.
     * @param precedence Precedence.
     * @param associativity Associativity.
     */
    public void AddOperator(String symbol, int precedence, Associativity associativity){
        switch(associativity){
            case Left:
                OPERATORS.put(symbol, new int[] {precedence, 0});
            break;
            case Right:
                OPERATORS.put(symbol, new int[] {precedence, 1});
            break;
        }
    }
    
    /**
     * Evaluate the RPN expression.
     * @param rpn RPN expression.
     * @return The result from the expression.
     */
    public double Evaluate(String rpn){
        return Evaluate(rpn.split(" "));
    }
    
    /**
     * Evaluate the RPN expression.
     * @param rpnTokens RPN tokens.
     * @return The result from the expression.
     */
    public double Evaluate(String[] rpnTokens){
        
        Stack<Double> values = new Stack<>();
        
        double result = 0;
        for (int i = 0; i < rpnTokens.length; i++) {
            if(Tools.isNumeric(rpnTokens[i])){
                values.push(Double.parseDouble(rpnTokens[i]));
            } else{
                result = Calc(values, rpnTokens[i]);
            }
        }
        
        return result;
        
    }
    
    private static double Calc(Stack<Double> values, String op){
        
        switch (op) {
            case "+":
            {
                Double a = values.pop();
                Double b = values.pop();
                values.push(b+a);
                return values.peek();
            }
            case "-":
            {
                Double a = values.pop();
                Double b = values.pop();
                values.push(b-a);
                return values.peek();
            }
            case "*":
            {
                Double a = values.pop();
                Double b = values.pop();
                values.push(b*a);
                return values.peek();
            }
            case "/":
            {
                Double a = values.pop();
                Double b = values.pop();
                values.push(b/a);
                return values.peek();
            }
            case "^":
            {
                Double a = values.pop();
                Double b = values.pop();
                values.push(Math.pow(b, a));
                return values.peek();
            }
            case "pow":
            {
                Double a = values.pop();
                Double b = values.pop();
                values.push(Math.pow(b, a));
                return values.peek();
            }
            case "ln":
            {
                Double a = values.pop();
                values.push(Math.log(a));
                return values.peek();
            }
            case "exp":
            {
                Double a = values.pop();
                values.push(Math.exp(a));
                return values.peek();
            }
            case "sqrt":
            {
                Double a = values.pop();
                values.push(Math.sqrt(a));
                return values.peek();
            }
            case "sin":
            {
                Double a = values.pop();
                values.push(Math.sin(a));
                return values.peek();
            }
            case "cos":
            {
                Double a = values.pop();
                values.push(Math.cos(a));
                return values.peek();
            }
            case "tan":
            {
                Double a = values.pop();
                values.push(Math.tan(a));
                return values.peek();
            }
            default:
                break;
        }
        
        return 0;
        
    }
    
    /**
     * Convert Infix notation to RPN.
     * @param infixNotation Infix notation.
     * @return RPN.
     */
    public String toReversePolishNotation(String infixNotation){
        String[] tokens = toReversePolishNotation(infixNotation.split(" "));
        return Arrays.toString(tokens);
    }
    
    /**
     * Convert Infix notation to RPN.
     * @param inputTokens Infix tokens.
     * @return RPN.
     */
    public String[] toReversePolishNotation(String[] inputTokens) {
        ArrayList<String> out = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        // For all the input tokens [S1] read the next token [S2]
        for (String token : inputTokens) {
            if (isOperator(token)) {
                // If token is an operator (x) [S3]
                while (!stack.empty() && isOperator(stack.peek())) {
                    // [S4]
                    if ((isAssociative(token, LEFT) && cmpPrecedence(token, stack.peek()) <= 0) || (isAssociative(token, RIGHT) && cmpPrecedence(token, stack.peek()) < 0)) {
                        out.add(stack.pop()); 	// [S5] [S6]
                        continue;
                    }
                    break;
                }
                // Push the new operator on the stack [S7]
                stack.push(token);
            } else if (token.equals("(")) {
                stack.push(token); 	// [S8]
            } else if (token.equals(")")) {
                // [S9]
                while (!stack.empty() && !stack.peek().equals("(")) {
                    out.add(stack.pop()); // [S10]
                }
                stack.pop(); // [S11]
            } else {
                out.add(token); // [S12]
            }
        }
        while (!stack.empty()) {
            out.add(stack.pop()); // [S13]
        }
        
        String[] output = new String[out.size()];
        return out.toArray(output);
    }
    
    /**
     * Test if a certain is an operator .
     * @param token The token to be tested .
     * @return True if token is an operator . Otherwise False .
     */
    private static boolean isOperator(String token) {
        return OPERATORS.containsKey(token);
    }

    /**
     * Test the associativity of a certain operator token .
     * @param token The token to be tested (needs to operator).
     * @param type LEFT_ASSOC or RIGHT_ASSOC
     * @return True if the tokenType equals the input parameter type .
     */
    private static boolean isAssociative(String token, int type) {
        if (!isOperator(token)) {
            throw new IllegalArgumentException("Invalid token: " + token);
        }
        if (OPERATORS.get(token)[1] == type) {
            return true;
        }
        return false;
    }

    /**
     * Compare precendece of two operators.
     * @param token1 The first operator .
     * @param token2 The second operator .
     * @return A negative number if token1 has a smaller precedence than token2,
     * 0 if the precendences of the two tokens are equal, a positive number
     * otherwise.
     */
    private static int cmpPrecedence(String token1, String token2) {
        if (!isOperator(token1) || !isOperator(token2)) {
            throw new IllegalArgumentException("Invalied tokens: " + token1 + " " + token2);
        }
        return OPERATORS.get(token1)[0] - OPERATORS.get(token2)[0];
    }
    
}