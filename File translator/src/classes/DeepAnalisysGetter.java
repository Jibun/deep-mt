package classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class DeepAnalisysGetter {
	public static void main(String[] args) throws Exception {
		InputStreamReader fr = new InputStreamReader(new FileInputStream("assets/englishSentencesParserOutput.txt"), "UTF8");
		BufferedReader br = new BufferedReader(fr);
		
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("assets/textoInglesParseadoDefinitivo.txt"), "UTF-8"));
		
		String s;
		int i = 0;
		boolean contando = false;
		boolean encontrando = false;
		while ((s = br.readLine()) != null) {
			if(encontrando && s.trim().length() == 0){
				encontrando = false;
				out.write("\n");
			}
			
			if(s.trim().equals("----------") && !encontrando){
				contando = true;
				i = 0;
			}
			
			if(contando && i > 14){
				contando = false;
				encontrando = true;
			}
			
			if(contando){
				i++;
			}
			
			if(encontrando){
				out.write(s + "\n");
			}
		}
		fr.close();
		out.close();
	}
}
