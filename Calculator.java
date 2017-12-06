package com.amazonaws.lambda.demo;

import java.text.DecimalFormat;
import java.util.Stack;

public class Calculator {
	private Stack<Float> values;
	private Stack<Character> operators;
	
	Calculator (){
		values = new Stack<>();
		operators = new Stack<>();
	}
	
	public String evaluateExpression(String expression) throws UnsupportedOperationException {
		float result = 0;
		if (expression == null || expression.length() == 0) {
			return null;
		}
		try {
			result = calculateResult(expression);
		}
		catch (Exception e) {
			throw (e);
		}
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(3);
		df.format(result);
		return(String.valueOf(result));
	}
	
	private float calculateResult(String expression) throws UnsupportedOperationException {
		
		char[] tokens = expression.toCharArray();
		for (int i = 0; i < tokens.length; i++) {
			// space then move ahead
			if (tokens[i] == ' ') { continue; }
			
			// number push it in the stack of values
			if (tokens[i] >= '0' && tokens[i] <= '9') {
				// if there are more than one digit or decimal
				StringBuffer sbuf = new StringBuffer();
				while ((i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9') ||
						(i < tokens.length && tokens[i] == '.')) {
                    sbuf.append(tokens[i++]);
				}
				values.push(Float.valueOf(sbuf.toString()));
				if (i >= tokens.length ) { continue; }
			}
			// opening bracket push in operator
			if (tokens[i] == '('){
				operators.push(tokens[i]);
			}
			// closing brace solve the expression
			if (tokens[i] == ')'){
				while (operators.peek() != '(') {
					try {
						float result = evaluateResult(operators.pop(), values.pop(), values.pop());
						values.push(result);
					}
					catch (UnsupportedOperationException e) {
						throw (e);
					}
					
				}
				operators.pop();
			}
			
			// operator
			if (isOperator(tokens[i])) {
				while (!operators.isEmpty() && 
						hasPrecedence(tokens[i], operators.peek())) {
					float result = evaluateResult(operators.pop(), values.pop(), values.pop());
					values.push(result);
				}
				operators.push(tokens[i]);
			}
		}
		
		// if operators left
		while (!operators.isEmpty()) {
			float result = evaluateResult(operators.pop(), values.pop(), values.pop());
			values.push(result);
		}
		if (values.size() != 1) {
			throw new UnsupportedOperationException("Invalid expression");
		}
		return values.pop();
		
	}
	
	private float evaluateResult(char operator, float operand1, float operand2) 
			throws UnsupportedOperationException  {
		
		switch (operator)
        {
        case '+':
            return operand1 + operand2;
        case '-':
            return operand1 - operand2;
        case '*':
            return operand1 * operand2;
        case '/':
            if (operand2 == 0)
                throw new UnsupportedOperationException("Cannot divide by zero");
            return operand1 / operand2;
        default:
        	throw new UnsupportedOperationException(operator + " not supported");
        }
        
	}
	
	private boolean isOperator (char operator) {
		if(operator == '+' || operator == '-' ||
		   operator == '*' || operator == '/'){
		   return true;
		}
		return false;
	}
	
	// returns true if op2 has higher or same precedence than op1
	private boolean hasPrecedence(char op1, char op2) {

        if (op2 == '(' || op2 == ')')
            return false;
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-'))
            return false;
        else
            return true;
	}

}
