package classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;

public class LemaGetter {
	public static void main(String[] args) throws Exception {
		InputStreamReader fr = new InputStreamReader(new FileInputStream("assets/dsynt_final_output_en.conll"), "UTF8");
		BufferedReader br = new BufferedReader(fr);
		
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("assets/englishLemas.txt"), "UTF-8"));
		
		String s;
		int i = 0;
		HashSet<String> hs = new HashSet<String>();
		while ((s = br.readLine()) != null) {
			
			String[] wordDetails = s.split("\t");
			if(wordDetails != null && wordDetails.length >= 10){
				if(!hs.contains(wordDetails[1].toLowerCase())){
					hs.add(wordDetails[1].toLowerCase());
					out.write(wordDetails[1].toLowerCase()+"\n");
				}
				i++;
			}
			System.out.println("line " + i);
		}
		fr.close();
		out.close();
	}
}
