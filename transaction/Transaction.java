
package transaction;

import java.util.*;
import java.util.regex.*;
import static javax.swing.JOptionPane.*;

public class Transaction { 
	public static class Value<T> {
		private T value;
		
		public void setValue(T value) {
			this.value = value;
		}
		
		public T getValue() {
			return this.value;
		}
	}
 	
	public static class Token<T> {
		private String token;
		private ArrayList<Value<T>> parameter;
		private int built_in_index;
		
		public Token() {
			this.parameter = new ArrayList<>();
		}
		
		public void setToken(String token) {
			this.token = token;
		}
		
		public String getToken() {
			return this.token;
		}
		
		public void addParameter(T value) {
			Value<T> _value = new Value<>();
			
			_value.setValue(value);
			
			this.parameter.add(_value);
		}
		
		public Value<T> getParameter(int index) {
			return this.parameter.get(index);
		}
		
		public ArrayList<Value<T>> getParameterAll() {
			return this.parameter;
		}
		
		public void setBuiltInIndex(int index) {
			this.built_in_index = index;
		}
		
		public int getBuiltInIndex() {
			return this.built_in_index;
		}
	}
	
	public String code;
	public ArrayList<Token> tokens;
	public Map<String, Value> stack;
	
	public Transaction() {
		
	}
	
	public Transaction(String code) {
		this.code = code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public Token getSetToken(String[] codes, int i) {
		switch(codes[i]) {
			case "Plus" :
				Token<Object> plus_token = new Token<>();
				
				plus_token.setToken(codes[i]);
				plus_token.addParameter(codes[++i]);
				plus_token.addParameter(codes[++i]);
				plus_token.setBuiltInIndex(i);
				
				return plus_token;
				
			case "Minus" :
				Token<Object> minus_token = new Token<>();
				
				minus_token.setToken(codes[i]);
				minus_token.addParameter(codes[++i]);
				minus_token.addParameter(codes[++i]); 
				minus_token.setBuiltInIndex(i);
				
				return minus_token;
				
			case "Print" :
				Token<Object> print_token = new Token<>();
				
				print_token.setToken(codes[i]);
				print_token.addParameter(codes[++i]); 
				print_token.setBuiltInIndex(i);
				
				return print_token;
				
			default :
				if(codes[i].replaceAll("[0-9a-zA-Z°¡-ÆR\n]", "").equals(",")) {
					Token<Token> var_token = new Token<>();
					
					if(Pattern.matches("^\\[[\\W\\w]+\\]$", codes[i + 1])) {
						Token<Object> token_value = new Token<>();
						
						token_value.setToken("");
						token_value.addParameter(codes[i + 1]);
						
						var_token.setToken(codes[i]);
						var_token.addParameter(token_value);
						var_token.setBuiltInIndex(++i);
						
						return var_token;
					}
					
					Token parameter_token;
					
					var_token.setToken(codes[i]);
					
					parameter_token = this.getSetToken(codes, ++i);
					
					var_token.addParameter(parameter_token);
					var_token.setBuiltInIndex(parameter_token.getBuiltInIndex());
					
					return var_token;
				}
				
				return null;
		}
	}
	
	public Object getStackVariableValue(String var_name) {
		Object message = null;
		
		if(Pattern.matches("^\\[[\\W\\w]+\\]$", var_name)) {
			String replaced_value = var_name.replaceAll("[\\[\\]]", "");
			Value stack_value = this.stack.get(replaced_value);
			
			if(stack_value.getValue() != null) {
				message = stack_value.getValue();
			
			} else {
				message = null;
			}
		
		} else {
			message = null;
		}
		
		return message;
	}
	
	public Value getTokenExecutionValue(Token token) {
		switch(token.getToken()) {
			case "Plus" :
				Value<Integer> plus_value = new Value<>();
				Object plus_param1_var = this.getStackVariableValue(token.getParameter(0).getValue().toString());
				Object plus_param2_var = this.getStackVariableValue(token.getParameter(1).getValue().toString());
				
				plus_value.setValue(0);
				
				if(plus_param1_var != null) {
					plus_value.setValue(plus_value.getValue() + Integer.parseInt(plus_param1_var.toString()));
				
				} else {
					plus_value.setValue(plus_value.getValue() + Integer.parseInt(token.getParameter(0).getValue().toString()));
				}
				
				if(plus_param2_var != null) {
					plus_value.setValue(plus_value.getValue() + Integer.parseInt(plus_param2_var.toString()));
				
				} else {
					plus_value.setValue(plus_value.getValue() + Integer.parseInt(token.getParameter(1).getValue().toString()));
				}
				
				return plus_value;
				
			case "Minus" :
				Value<Integer> minus_value = new Value<>();
				Object minus_param1_var = this.getStackVariableValue(token.getParameter(0).getValue().toString());
				Object minus_param2_var = this.getStackVariableValue(token.getParameter(1).getValue().toString());
				
				minus_value.setValue(0);
				
				if(minus_param1_var != null) {
					minus_value.setValue(minus_value.getValue() + Integer.parseInt(minus_param1_var.toString()));
				
				} else {
					minus_value.setValue(minus_value.getValue() + Integer.parseInt(token.getParameter(0).getValue().toString()));
				}
				
				if(minus_param2_var != null) {
					minus_value.setValue(minus_value.getValue() - Integer.parseInt(minus_param2_var.toString()));
				
				} else {
					minus_value.setValue(minus_value.getValue() - Integer.parseInt(token.getParameter(1).getValue().toString()));
				}
				
				return minus_value;
				
			case "Print" :
				Value<String> print_value = new Value<>();
				String var_name = token.getParameter(0).getValue().toString();
				
				print_value.setValue(var_name);
				
				showMessageDialog(null, this.getStackVariableValue(var_name));
				
				return print_value;
				
			default :
				if(token.getToken().replaceAll("[0-9a-zA-Z°¡-ÆR]", "").equals(",")) {
					Value put_value = this.getTokenExecutionValue((Token) token.getParameter(0).getValue()); 
					
					if(put_value == null) {
						put_value = new Value<>();
						Token temp_token = (Token) token.getParameter(0).getValue();
						
						put_value.setValue(this.getStackVariableValue(temp_token.getParameter(0).getValue().toString()));
					}
					
					this.stack.put(token.getToken().replace(",", ""), put_value);
				}
				
				return null;
		}
	}
	
	public ArrayList<Token> parse() {
		String[] codes = this.code.replaceAll("\n", "").split("\\s");
		System.out.println(Arrays.toString(codes));
		this.tokens = new ArrayList<>();
		int i = 0;
				
		while(i < codes.length) {
			if(!codes[i].trim().equals("")) {
				codes[i] = codes[i].trim();
				
				Token token = this.getSetToken(codes, i);
				
				this.tokens.add(token);
				
				i = token.getBuiltInIndex() + 1;
			
			
			} else {
				i += 1;
			}
		}
		
		return this.tokens;
	}
	
	public void execute() {
		this.stack = new HashMap<>();
		int i = 0;
		
		while(i < this.tokens.size()) {
			this.getTokenExecutionValue(this.tokens.get(i));
				
			i += 1;
		}
	}
	
//	public static void main(String[] args) {
//		Transaction trans = new Transaction("Result, Plus 2 1 Result1, Minus 2 1 Print [Result1]"); // test string
//		
//		trans.parse();
//		trans.execute();
//		
//		System.out.println(trans.stack.get("Result").getValue());
//		System.out.println(trans.stack.get("Result1").getValue());
//	}
}






