package com.amazonaws.lambda.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;


public class LambdaFunctionHandler implements RequestHandler<Object, String> {

    @Override
    public String handleRequest(Object input, Context context) {
        context.getLogger().log("Input: " + input);
        String obj = calculateResult(input.toString(), context);
        
        return obj;
    }
    
    private String calculateResult(String input, Context context) {
    	if (input == null) return null;

    	String result = null;
        try {
			
			Calculator c = new Calculator();
			result = c.evaluateExpression(input);
			context.getLogger().log("Result value = " + result);
			
			
		} catch (Exception e) {
			context.getLogger().log("Excpetion in expression evaluation : " + e);
		}
		
        return result;
        
        
    }

}
