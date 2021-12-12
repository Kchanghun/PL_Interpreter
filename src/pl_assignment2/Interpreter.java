package pl_assignment2;
import java.util.*;
interface Token{
	int EOF=0;
	int start=1;
	int functions=2;
	int function_body=3;
	int var_definitions=4;
	int var_definition=5;
	int var_list=6;
	int statements=7;
	int statement=8;
	int identifier=9;
	int semicolon=10;
	int left_brace=11;
	int right_brace=12;
	int comma=13;
}
interface KindOfID{
	int variable=0;
	int function=1;
	int not_definition=2;
}
public class Interpreter extends SymbolTable{
	List<String> input=new ArrayList<>();
	Stack<String> RuntimeStack=new Stack<>();	//Run time stack
	HashMap<String,Integer> AR_size=new HashMap<>();	//함수별 ARI size 저장
	int beginFrom=0;	//실행을위해 main함수 시작 index 저
	int nextToken;
	String lexeme;
	boolean error=false;
	int splitter=0;
	int analyze_line=0;
	Interpreter(List<String> inputText){
		super();
		input=inputText;
	
	}
	public void lexical() {
		//set lexeme & set nextToken
		lexeme="";
		char[] currLine;
		for(;analyze_line<input.size();analyze_line++) {
			currLine=input.get(analyze_line).toCharArray();
			for(;splitter<currLine.length;splitter++) {
				if(currLine[splitter]==',') {
					splitter+=1;
					lexeme=",";
					nextToken=Token.comma;
					return;
				}
				else if(currLine[splitter]==';') {
					splitter+=1;
					lexeme=";";
					nextToken=Token.semicolon;
					return;
				}
				else if(currLine[splitter]=='{') {
					splitter+=1;
					lexeme="{";
					nextToken=Token.left_brace;
					return;
				}
				else if(currLine[splitter]=='}') {
					splitter+=1;
					lexeme="}";
					nextToken=Token.right_brace;
					return;
				}
				else if()
				
			}
			splitter=0;
		}
	}
	public void reculsive_descent_parser() {
		lexical();
		start();
	}
	public void start() {
		functions();
	}
	public void functions() {
		function();
		variableInCurrFunction.clear();
		if(nextToken>0) {
			functions();
		}
	}
	public void function() {
		identifier(KindOfID.function);
		left_brace();
		function_body();
		right_brace();
	}
	public void function_body() {
		if(nextToken==Token.var_definitions) {
			var_definitions();
		}
		statements();
	}
	public void var_definitions() {
		var_definition();
		if(nextToken==Token.var_definitions) {
			var_definitions();
		}
	}
	public void var_definition() {
		lexical();
		var_list();
		semicolon();
	}
	public void var_list() {
		identifier(KindOfID.variable);
		if(nextToken==Token.comma) {
			comma();
			var_list();
		}
	}
	public void statements() {
		statement();
		if(nextToken!=Token.right_brace) {
			statements();
		}
	}
	public void statement() {
		if(lexeme.equals("call")) {
			lexical();
			identifier(KindOfID.not_definition);
		}
		else if(lexeme.equals("print_ari")) {
			lexical();
		}
		else {
			identifier(KindOfID.not_definition);
		}
		semicolon();
	}
	public void identifier(int which_identifier) {
		switch(which_identifier) {
		case KindOfID.variable:
			check_duplicate(lexeme,which_identifier);
			identifier4Variable.put(lexeme, 1);
			variableInCurrFunction.put(lexeme, 1);
			break;
		case KindOfID.function:
			check_duplicate(lexeme,which_identifier);
			identifier4Function.put(lexeme, 1);
			break;
		case KindOfID.not_definition:
			//not save to Symbol Table
		}
		lexical();
	}
	public void semicolon() {
		if(!lexeme.equals(";")) {
			error=true;
			System.out.println("Wrong Grammer");
		}
		lexical();
	}
	public void left_brace() {
		if(!lexeme.equals("{")) {
			error=true;
			System.out.println("Wrong Grammer");
		}
		lexical();
	}
	public void right_brace() {
		if(!lexeme.equals("}")) {
			error=true;
			System.out.println("Wrong Grammer");
		}
		lexical();
	}
	public void comma() {
		if(!lexeme.equals(",")) {
			error=true;
			System.out.println("Wrong Grammer");
		}
		lexical();
	}
	public void check_duplicate(String lexeme,int which_identifier) {
		if(!error) {
			if(which_identifier==KindOfID.variable) {
				if(variableInCurrFunction.containsKey(lexeme)) {
					error=true;
					System.out.println("Duplicate declaration of the identifier: "+lexeme);
					return;
				}
				else if(identifier4Function.containsKey(lexeme)) {
					error=true;
					System.out.println("Duplicate declaration of the identifier or the function name: "+lexeme);
					return;
				}
			}
			else if(which_identifier==KindOfID.function) {
				if(identifier4Variable.containsKey(lexeme)) {
					error=true;
					System.out.println("Duplicate declaration of the identifier or the function name: "+lexeme);
					return;
				}
				else if(identifier4Function.containsKey(lexeme)) {
					error=true;
					System.out.println("Duplicate declaration of the function name: "+lexeme);
					return;
				}
			}
		}
	}
	public void execute() {
		
	}
}
