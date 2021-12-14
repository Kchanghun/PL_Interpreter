package pl_assignment2;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		//get path of txt file
		Path path=Paths.get(args[0]);
		Charset charset=Charset.forName("UTF-8");
		List<String> inputText=null;
		try {
			inputText=Files.readAllLines(path,charset);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		//send all context of the txt file to interpreter
		Interpreter interpreter = new Interpreter(inputText);
		
		//syntax check
		interpreter.reculsive_descent_parser();
		
		//if 
		if(interpreter.SyntaxError) {
			System.out.println("Syntax Error.\n");
			return;
		}
		else if(interpreter.DuplicateError) {
			return;
		}
		System.out.println("Syntax O.K.\n");

		interpreter.execute();
		
		
		 
		
	}

}
