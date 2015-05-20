package classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class Main2 {

	/**
	 * @param args
	 */
	public static void main2(String[] args) throws Exception {
		// FileReader fr = new
		// FileReader("assets/dsynt_final_output_post_es.conll");

		InputStreamReader fr = new InputStreamReader(new FileInputStream("assets/dsynt_final_output_post_es.conll"), "UTF8");
		BufferedReader br = new BufferedReader(fr);
		
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("assets/resultat.conll"), "UTF-8"));
		
		Translate.setClientId("jibunSimpleTranslator");
		Translate.setClientSecret("MoRhSekJ7WeqvxawxJb5TsJGz07YSUqTFMQn4I0d2dc=");
		
		String s;
		int i = 0;
		while ((s = br.readLine()) != null) {
			String[] wordDetails = s.split("\t", 3);
			if(wordDetails != null && wordDetails.length > 1){
				String word = wordDetails[1];
				String translatedText = Translate.execute(word, Language.SPANISH, Language.ENGLISH);
				String[] firstTranslatedWord = translatedText.split(" ");
				String finalWord = firstTranslatedWord[0].toLowerCase();
				System.out.println(i + ": " + word + " " + finalWord);
				out.write(wordDetails[0]+"\t"+ finalWord +"\t"+wordDetails[2]+"\n"); 
				i++;
			}
			
		}
		fr.close();
		out.close();
	}
}
