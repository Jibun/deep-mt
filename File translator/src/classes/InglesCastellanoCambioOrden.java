package classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;

public class InglesCastellanoCambioOrden {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		InputStreamReader fr = new InputStreamReader(new FileInputStream("assets/lemasSustituidosOrdenado.conll"), "UTF8");
		BufferedReader br = new BufferedReader(fr);
		
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("assets/lemasSustituidosOrdenCambiado.conll"), "UTF-8"));
		
		String s;
		int i = 0;
		float specialCasesPerSentence = 0;
		ArrayList<String> sm = new ArrayList<String>();
		ArrayList<String> tags = new ArrayList<String>();
		while ((s = br.readLine()) != null) {
			String[] wordDetails = s.split("\t");
			if(wordDetails != null && wordDetails.length >= 10){
				sm.add(s);
				tags.add(i, wordDetails[5]);
				if(i > 0){
					if((wordDetails[5].equals("NN") || wordDetails[5].equals("NNS")) 
							&& tags.get(i-1).equals("JJ"))
						swap(sm, i, i-1);
					
					if((wordDetails[5].equals("PRP") || wordDetails[5].equals("PRP$"))
							&& (tags.get(i-1).equals("VB") || tags.get(i-1).equals("VBD")|| tags.get(i-1).equals("VBG")|| 
									tags.get(i-1).equals("VBP")|| tags.get(i-1).equals("VBN")|| tags.get(i-1).equals("VBZ"))){
						if(i > 1){
							if(!tags.get(i-2).equals("MD") && !tags.get(i-1).equals("VB") && !tags.get(i-1).equals("VBD") 
									&& !tags.get(i-1).equals("VBG") && !tags.get(i-1).equals("VBP") 
									&& !tags.get(i-1).equals("VBN") && !tags.get(i-1).equals("VBZ")){
								swap(sm, i, i-1);
							}
						}else{
							swap(sm, i, i-1);
						}
					}
					
				}
				
				i++;
			}else if (wordDetails != null && wordDetails.length == 1){
				i=0;
				specialCasesPerSentence = 0;
				reorder(sm, out);
				sm.clear();
				out.write("\n");
			}
			
		}
		fr.close();
		out.close();
	}
	
	public static void swap(ArrayList<String> sm, int position1, int position2){
		String sentence = sm.get(position2);
		sm.set(position2, sm.get(position1));
		sm.set(position1, sentence);
	}
	
	public static void reorder(ArrayList<String> sm, BufferedWriter out) throws IOException{
		int i = 1;
		HashMap<Integer, Integer> indexEquivalent = new HashMap<Integer, Integer>();  
		for (String line: sm) {
			System.out.println(i + ": " + line);
			indexEquivalent.put(Integer.parseInt(line.split("\t")[0]), i);
			i++;
	    }
		i=1;
		for (String line: sm) {
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
