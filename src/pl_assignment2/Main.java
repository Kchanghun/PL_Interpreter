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
		if(args[0].equals("-v")) {
			path= Paths.get(args[1]);
		}
		else{
			path= Paths.get(args[0]);
		}
		Charset charset=Charset.forName("UTF-8");
		List<String> lines=null;
		try {
			lines=Files.readAllLines(path,charset);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		String context="";
		int[] sizeOfLine=new int[lines.size()];
		for(int i=0;i<lines.size();i++) {
			context+=lines.get(i)+" ";
			sizeOfLine[i]=context.length()-1;
		}
		
	}

}
