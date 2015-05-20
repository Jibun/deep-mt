package classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class LineFileSplitter {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		InputStreamReader fr = new InputStreamReader(new FileInputStream("ParsedLinesDeep/lemasSustituidosCastellanoFinal.conll"), "UTF8");
		BufferedReader br = new BufferedReader(fr);
		
		int i= 1;
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("ParsedLinesDeep/output_sentence_"+i+".txt"), "UTF-8"));
		String s;
		while ((s = br.readLine()) != null) {
			if(s.trim().length() == 0){
				out.close();
				i++;
				out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("ParsedLinesDeep/output_sentence_"+i+".txt"), "UTF-8"));
			}else{
				out.write(s);
				out.write("\n");
			}		
		}
		fr.close();
	}
}
