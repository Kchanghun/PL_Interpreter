package pl_assignment2;
import java.util.*;
public class SymbolTable {
	HashMap<String,Integer> identifier4Variable=new HashMap<>();
	HashMap<String,Integer> identifier4Function=new HashMap<>();
	HashMap<String,Integer> reserved_word=new HashMap<>();
	HashMap<String,Integer> variableInCurrFunction=new HashMap<>();
	SymbolTable(){
		reserved_word.put("call", 1);
		reserved_word.put("variable", 1);
		reserved_word.put("print_ari", 1);
	}
}
