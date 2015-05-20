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

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		InputStreamReader fr = new InputStreamReader(new FileInputStream("assets/lemasSustituidos.conll"), "UTF8");
		BufferedReader br = new BufferedReader(fr);
		
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("assets/lemasSustituidosOrdenado.conll"), "UTF-8"));
		
		String s;
		int i = 0;
		float specialCasesPerSentence = 0;
		SortedMap<Float, String> sm = new TreeMap<Float, String>();
		while ((s = br.readLine()) != null) {
			String[] wordDetails = s.split("\t");
			if(wordDetails != null && wordDetails.length >= 10){
				String[] feats = wordDetails[6].split("\\|");
				for(int j= 0; j < feats.length; j++){
					if(feats[j].startsWith("id0=")){
						int length = 0;
						boolean specialCase = true;
						if(feats[j].endsWith("_prosubj")){
							length = feats[j].length() - 8;
							specialCasesPerSentence += 0.1f;
						}else if(feats[j].endsWith("_proObj")){
							length = feats[j].length() - 7;
							specialCasesPerSentence += 0.1f;
						}else if(feats[j].endsWith("_bis")){
							length = feats[j].length() - 4;
							specialCasesPerSentence += 0.1f;
						}else if(feats[j].endsWith("bis")){
							length = feats[j].length() - 3;
							specialCasesPerSentence += 0.1f;
						}else{
							specialCase = false;
							length = feats[j].length();
						}
						Float order = (float) Integer.parseInt(feats[j].substring(4, length));
						if(specialCase)
							order += specialCasesPerSentence;
						//System.out.println(i + ": " + order);
						
						sm.put(order, s);
					}
				}
				/*out.write(wordDetails[0]+"\t"+ wordDetails[1] +"\t"+wordDetails[2]+
						"\t"+wordDetails[4]+"\t"+wordDetails[5]+"\t"+wordDetails[6]+
						"\t"+wordDetails[8]+"\t"+wordDetails[10]+"\t"+wordDetails[9]+"\t"+wordDetails[11]+"\n"); */
				i++;
			}else if (wordDetails != null && wordDetails.length == 1){
				specialCasesPerSentence = 0;
				reorder(sm, out);
				sm.clear();
				out.write("\n");
			}
			
		}
		fr.close();
		out.close();
	}
	
	public static void reorder(SortedMap<Float, String> sm, BufferedWriter out) throws IOException{
		int i = 1;
		HashMap<Integer, Integer> indexEquivalent = new HashMap<Integer, Integer>();  
		for (String line: sm.values()) {
			//System.out.println(i + ": " + line);
			indexEquivalent.put(Integer.parseInt(line.split("\t")[0]), i);
			i++;
	    }
		i=1;
		for (String line: sm.values()) {
			String[] wordDetails = line.split("\t");
			
			String head;
			if(wordDetails[8].matches("-?\\d+(\\.\\d+)?") && !wordDetails[8].equals("0")){
				head = indexEquivalent.get(Integer.parseInt(wordDetails[8])).toString();
			}else{
				head = wordDetails[8];
			}
				
			System.out.println(i+"\t"+ wordDetails[1] +"\t"+wordDetails[2]+
					"\t"+wordDetails[4]+"\t"+wordDetails[5]+"\t"+wordDetails[6]+
					"\t"+head+"\t"+wordDetails[10]+"\t"+"_"+"\t"+"_");
			
			/*out.write(i+"\t"+ wordDetails[1] +"\t"+wordDetails[2]+
					"\t"+wordDetails[4]+"\t"+wordDetails[5]+"\t"+wordDetails[6]+
					"\t"+head+"\t"+wordDetails[10]+"\t"+"_"+"\t"+"_"+"\n");*/
			out.write(i+"\t"+ wordDetails[1] +"\t"+wordDetails[2]+"\t"+
					wordDetails[3] +"\t"+wordDetails[4]+"\t"+wordDetails[5]+"\t"+wordDetails[6]+"\t"+
					wordDetails[7] +"\t"+ head + "\t" +wordDetails[9]+"\t"+wordDetails[10]+"\t"+wordDetails[11] +"\t"+
					wordDetails[12]+"\t"+wordDetails[13]+"\n");
			i++;
		}
	}
}
