package classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;

public class Tokenizer {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		InputStreamReader fr = new InputStreamReader(new FileInputStream("assets/spanish.rest.plaintext"), "UTF8");
		BufferedReader br = new BufferedReader(fr);
		
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("assets/spanishPlaintextTokenizedRest.txt"), "UTF-8"));
		
		String s;
		int i = 0;
		while ((s = br.readLine()) != null) {
			s = s.trim();

			String resultString = s.replaceAll("(\\w+)([\\)\\(\'\\,\\.\\{\\}\\[\\]\\:\\;\\?\\¿\\!\\¡\\\"]+)(\\w+)", "$1 $2 $3");
			resultString = s.replaceAll("([\\p{L}0-9]+)([\\)\\(\'\\,\\.\\{\\}\\[\\]\\:\\;\\?\\¿\\!\\¡\\\"]+)", "$1 $2");
			resultString = resultString.replaceAll("([\\)\\(\'\\,\\.\\{\\}\\[\\]\\:\\;\\?\\¿\\!\\¡\\\"]+)(\\w+)", "$1 $2");

			if(!s.isEmpty()){
					out.write(resultString + "\n");
				i++;
				System.out.println(i);
			}
			
		}
		fr.close();
		out.close();
	}
}
