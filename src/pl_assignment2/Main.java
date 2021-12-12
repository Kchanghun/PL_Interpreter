package pl_assignment2;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		Path path;
		//path=Paths.get(args[0]);
		path=Paths.get("/Users/errasi/Desktop/test.txt");
		Charset charset=Charset.forName("UTF-8");
		List<String> inputText=null;
		try {
			inputText=Files.readAllLines(path,charset);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		Interpreter interpreter = new Interpreter(inputText);
		
		String a="";
		if(a.equals("")) {
			System.out.println("adsf");
		}
		
		
		
	}

}
