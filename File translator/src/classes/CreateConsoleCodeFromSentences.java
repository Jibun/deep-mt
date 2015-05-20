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

public class CreateConsoleCodeFromSentences {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		InputStreamReader fr = new InputStreamReader(new FileInputStream("fichersDefinitius/test-set-utf8.en"), "UTF8");
		BufferedReader br = new BufferedReader(fr);
		
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("fichersDefinitius/scriptToParseSentencesInEnglish.sh"), "UTF-8"));
		
		String consoleCommand = "java -jar DepParsingService.jar model.eng.for.demo ssynt_svm.svm.model ssynt_labelling_svm.svm.model pathPatterns.txt PTB_train_SSynt.conll CoNLL2009-ST-English-train__out.conll ";
		String s;
		int i = 0;
		while ((s = br.readLine()) != null) {
			s = s.trim();
			//String resultString = s.replaceAll("\\B[ .]+|(\\d)\\.+", "$1 ");
			/*String resultString = s.replaceAll("(\\w+)([\\)\\(\'\\,\\.\\{\\}\\[\\]\\:\\;\\?\\!]+)(\\w+)", "$1 $2 $3");
			resultString = s.replaceAll("(\\w+)([\\)\\(\'\\,\\.\\{\\}\\[\\]\\:\\;\\?\\!]+)", "$1 $2");
			resultString = resultString.replaceAll("([\\)\\(\'\\,\\.\\{\\}\\[\\]\\:\\;\\?\\!]+)(\\w+)", "$1 $2");*/
			//resultString = resultString.replaceAll("([\\)\\(\']+)(\\w)", "$1 $2");
			if(!s.isEmpty()){
				if(i == 0)
					out.write(consoleCommand + "\"" + s + "\"" + " > englishSentencesParserOutput.txt ; ");
				else 
					out.write(consoleCommand + "\"" + s + "\"" + " >> englishSentencesParserOutput.txt ; ");
				i++;
				System.out.println(i);
			}
			
		}
		fr.close();
		out.close();
	}
}
