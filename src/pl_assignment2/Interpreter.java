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
	int nextToken;
	String lexeme;
	boolean SyntaxError=false;
	boolean DuplicateError=false;
	int splitter=0;
	int analyze_line=0;
	
	Stack<String> RuntimeStack=new Stack<>();	//Run time stack
	HashMap<String,Integer> AR_size=new HashMap<>();
	String currFunction;
	HashMap<String,Integer> beginFrom=new HashMap<>();
	HashMap<String,Integer> bottomOf=new HashMap<>();
	
	Interpreter(List<String> inputText){
		super();
		input=inputText;
	
	}
	//set lexeme & set nextToken
	public void lexical() {
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
				else if((currLine[splitter]>64&&currLine[splitter]<91)||(currLine[splitter]>96&&currLine[splitter]<123)||currLine[splitter]==95) {
					nextToken=Token.identifier;
					lexeme+=currLine[splitter];
					splitter+=1;
					while((splitter<currLine.length)&&((splitter<currLine.length)&&(currLine[splitter]>64&&currLine[splitter]<91)||(currLine[splitter]>96&&currLine[splitter]<123)||currLine[splitter]==95
							||(currLine[splitter]>47&&currLine[splitter]<58))) {
						lexeme+=currLine[splitter];
						splitter+=1;
					}
					if(lexeme.equals("variable")) {
						nextToken=Token.var_definitions;
					}
					return;
				}
				else if(currLine[splitter]<33) {
					//ignore White space
					continue;
				}
			}
			splitter=0;
		}
		nextToken=Token.EOF;
		return;
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
			beginFrom.put(lexeme, analyze_line);
			break;
		case KindOfID.not_definition:
			//not save to Symbol Table
		}
		lexical();
	}
	public void semicolon() {
		if(!lexeme.equals(";")) {
			SyntaxError=true;
		}
		lexical();
	}
	public void left_brace() {
		if(!lexeme.equals("{")) {
			SyntaxError=true;
		}
		lexical();
	}
	public void right_brace() {
		if(!lexeme.equals("}")) {
			SyntaxError=true;
		}
		lexical();
	}
	public void comma() {
		if(!lexeme.equals(",")) {
			SyntaxError=true;
		}
		lexical();
	}
	public void check_duplicate(String lexeme,int which_identifier) {
		if((!DuplicateError)&&(!SyntaxError)) {
			if(which_identifier==KindOfID.variable) {
				if(variableInCurrFunction.containsKey(lexeme)) {
					DuplicateError=true;
					System.out.println("Duplicate declaration of the identifier: "+lexeme);
					return;
				}
				else if(identifier4Function.containsKey(lexeme)) {
					DuplicateError=true;
					System.out.println("Duplicate declaration of the identifier or the function name: "+lexeme);
					return;
				}
				else if(reserved_word.containsKey(lexeme)) {
					DuplicateError=true;
					System.out.println("Identifier cannot be same with Reserved Word");
					return;
				}
			}
			else if(which_identifier==KindOfID.function) {
				if(identifier4Variable.containsKey(lexeme)) {
					DuplicateError=true;
					System.out.println("Duplicate declaration of the identifier or the function name: "+lexeme);
					return;
				}
				else if(identifier4Function.containsKey(lexeme)) {
					DuplicateError=true;
					System.out.println("Duplicate declaration of the function name: "+lexeme);
					return;
				}
				else if(reserved_word.containsKey(lexeme)) {
					DuplicateError=true;
					System.out.println("Function name cannot be same with Reserved Word");
					return;
				}
			}
		}
	}
	public void execute() {
		String prevLexeme="";
		analyze_line=beginFrom.get("main");
		nextToken=Token.identifier;
		int variable_count=0;
		int where2return=0;
		HashMap<String,Integer>var_definition_count=new HashMap<>();
		
		bottomOf.put("main", 0);
		
		while(nextToken!=Token.EOF) {
			prevLexeme=lexeme;
			lexical();
			if(lexeme.equals("{")) {
				currFunction=prevLexeme;
				var_definition_count.put(currFunction, 0);
			}
			else if(lexeme.equals("}")) {
				//function exit
				if(currFunction.equals("main")) {
					nextToken=Token.EOF;
					continue;
				}
				String[] returnTo=RuntimeStack.get(bottomOf.get(currFunction)).split(":");
				
				analyze_line=beginFrom.get(returnTo[0])+var_definition_count.get(returnTo[0])+Integer.parseInt(returnTo[1])+1;
			
				for(int i=0;i<AR_size.get(currFunction);i++) {
					RuntimeStack.pop();
				}
				AR_size.remove(currFunction);
				
				splitter=0;
				currFunction=returnTo[0];
			}
			else if(lexeme.equals("call")) {
				lexical();
				
				if(!identifier4Function.containsKey(lexeme)) {
					//Error!
					System.out.println("Call to undefined function: "+lexeme);
					break;
				}
				
				where2return=analyze_line-beginFrom.get(currFunction)-var_definition_count.get(currFunction);
				RuntimeStack.push(currFunction+":"+where2return);
				bottomOf.put(lexeme, RuntimeStack.size()-1);
				RuntimeStack.push(""+bottomOf.get(currFunction));
				
				variable_count=0;
				analyze_line=beginFrom.get(lexeme);
				splitter=0;
			}
			else if(lexeme.equals("variable")) {
				
				var_definition_count.put(currFunction, var_definition_count.get(currFunction)+1);
			
				while(nextToken!=Token.semicolon) {
					lexical();
					if(nextToken!=Token.comma&&nextToken!=Token.semicolon) {
						RuntimeStack.push(lexeme);
						variable_count+=1;
					}
				}
				
				if(currFunction.equals("main")) {
					AR_size.put(currFunction, variable_count);
				}
				else {
					AR_size.put(currFunction, variable_count+2);
				}
				
			}
			else if(lexeme.equals("print_ari")) {
				print_ari();
			}
			else if(identifier4Variable.containsKey(lexeme)) {
				reference_variable(lexeme,currFunction);
				lexical();
			}
			else if((!identifier4Function.containsKey(lexeme))&&(nextToken==Token.identifier)) {
				//Error!
				System.out.println("Reference to undefined variable: "+lexeme);
				break;
			}
		}
	}
	public void print_ari() {
		int indexOfStack=RuntimeStack.size()-1;
		for(Map.Entry<String,Integer> entry:sortReverse(AR_size).entrySet()) {
			System.out.print(entry.getKey()+": ");
			for(int i=entry.getValue();i>0;i--) {
				if(indexOfStack!=(RuntimeStack.size()-1)) {
					System.out.print("\t");
				}
				if(i>2||entry.getKey().equals("main")) {
					System.out.println("Local variable: "+RuntimeStack.get(indexOfStack));
				}
				else if(i==2) {
					System.out.println("Dynamic Link: "+RuntimeStack.get(indexOfStack));
				}
				else if(i==1) {
					System.out.println("Return Address: "+RuntimeStack.get(indexOfStack));
				}
				indexOfStack-=1;
			}
		}
		System.out.println();
	}
	public void reference_variable(String var_name,String currFunction) {
		String fromFunction=currFunction;
		int link_count=0;
		int local_offset=0;
		int linkTo=bottomOf.get(currFunction);
		boolean find=false;
		while(!find) {
			if(RuntimeStack.get(linkTo+local_offset).equals(var_name)) {
				find=true;
			}
			else {
				if(AR_size.get(currFunction)==(local_offset+1)) {
					//move link
					if(currFunction.equals("main")) {
						//Error!
						System.out.println("Cannot Reference variable: "+var_name);
						break;
					}
					link_count+=1;
					linkTo=Integer.parseInt(RuntimeStack.get(bottomOf.get(currFunction)+1));
					
					String[] getFunctionName=RuntimeStack.get(bottomOf.get(currFunction)).split(":");
					currFunction=getFunctionName[0];
					local_offset=0;
				}
				else {
					local_offset+=1;
				}
			}
		}
		if(find) {
			System.out.println(fromFunction+":"+var_name+" => "+link_count+", "+local_offset+"\n");
		}
	}
	public LinkedHashMap<String,Integer> sortReverse(HashMap<String,Integer> AR_size){
		List<Map.Entry<String, Integer>> entries=new LinkedList<>(AR_size.entrySet());
		Collections.sort(entries,(o1,o2)->-1);
		
		LinkedHashMap<String,Integer> result=new LinkedHashMap<>();
		for(Map.Entry<String, Integer>entry:entries) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
}
