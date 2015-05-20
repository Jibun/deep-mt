package classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class DependencyColumnReplacement {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		InputStreamReader fr = new InputStreamReader(new FileInputStream("testSet/lemasSustituidosDependency.conll"), "UTF8");
		BufferedReader br = new BufferedReader(fr);
		
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("testSet/transferDone.conll"), "UTF-8"));
		
		String s;
		int i = 0;
		while ((s = br.readLine()) != null) {
			String[] wordDetails = s.split("\t");
			if(wordDetails != null && wordDetails.length >= 10){
				out.write(wordDetails[0]+"\t"+ wordDetails[1] +"\t"+wordDetails[2]+
						"\t"+wordDetails[3]+"\t"+wordDetails[4]+"\t"+wordDetails[5]+"\t"+wordDetails[6]+
						"\t"+wordDetails[7]+"\t"+wordDetails[9]+"\t"+"_"+"\t"+wordDetails[11]+
						"\t"+wordDetails[11]+"\t"+wordDetails[12]+"\t"+wordDetails[13]+"\n");
				i++;
			}else if (wordDetails != null && wordDetails.length == 1){
				out.write("\n");
			}
			
		}
		fr.close();
		out.close();
	}
}