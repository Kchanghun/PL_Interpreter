package pl_assignment2;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		//Get Path of txt file
		Path path=Paths.get(args[0]);
		Charset charset=Charset.forName("UTF-8");
		List<String> inputText=null;
		try {
			inputText=Files.readAllLines(path,charset);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		//send all context of the txt file to Interpreter
		Interpreter interpreter = new Interpreter(inputText);
		
		//Syntax Check
		interpreter.reculsive_descent_parser();
		
		//if syntax error occurred, then exit program 
		if(interpreter.error) {
			System.out.println("Syntax Error");
			return;
		}
		
		//if syntax OK, then execute the txt code
		System.out.println("Syntax O.K.\n");
		interpreter.execute();
		
		
		 
		
	}

}
