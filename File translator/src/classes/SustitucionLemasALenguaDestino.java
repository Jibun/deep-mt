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
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class SustitucionLemasALenguaDestino {
	
	static boolean BASELINE = true;
	static String initialFile = "devSet/textoInglesParseadoDefinitivo.txt";
	static String endFile = "devSet/lemasSustituidosBaseline.conll";
	
	public static void reorderSentences() throws IOException {
		InputStreamReader fr = new InputStreamReader(new FileInputStream(initialFile), "UTF8");
		BufferedReader br = new BufferedReader(fr);
		
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("tmp/textoInglesOrdenado.conll"), "UTF-8"));
		
		String s;
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
						
						sm.put(order, s);
					}
				}
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
				
			out.write(i+"\t"+ wordDetails[1] +"\t"+wordDetails[2]+"\t"+
					wordDetails[3] +"\t"+wordDetails[4]+"\t"+wordDetails[5]+"\t"+wordDetails[6]+"\t"+
					wordDetails[7] +"\t"+ head + "\t" +wordDetails[9]+"\t"+wordDetails[10]+"\t"+wordDetails[11] +"\t"+
					wordDetails[12]+"\t"+wordDetails[13]+"\n");
			i++;
		}
	}
	
	public static void changeOrderSentecesEnglishSpanish() throws IOException{
		InputStreamReader fr = new InputStreamReader(new FileInputStream("tmp/textoInglesOrdenado.conll"), "UTF8");
		BufferedReader br = new BufferedReader(fr);
		
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("tmp/textoInglesOrdenCastellano.conll"), "UTF-8"));
		
		String s;
		int i = 0;
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
				
			out.write(i+"\t"+ wordDetails[1] +"\t"+wordDetails[2]+"\t"+
					wordDetails[3] +"\t"+wordDetails[4]+"\t"+wordDetails[5]+"\t"+wordDetails[6]+"\t"+
					wordDetails[7] +"\t"+ head + "\t" +wordDetails[9]+"\t"+wordDetails[10]+"\t"+wordDetails[11] +"\t"+
					wordDetails[12]+"\t"+wordDetails[13]+"\n");
			i++;
		}
	}
	
	public static String strJoin(String[] aArr, String sSep) {
	    StringBuilder sbStr = new StringBuilder();
	    for (int i = 0, il = aArr.length; i < il; i++) {
	        if (i > 0)
	            sbStr.append(sSep);
	        sbStr.append(aArr[i]);
	    }
	    return sbStr.toString();
	}
	
	
	public static void main(String[] args) throws Exception {
		
		reorderSentences();
		if(!BASELINE)changeOrderSentecesEnglishSpanish();
		
		/*
		 * Correspondencia entre los tags de los lemas en ingles y la morfologia de la palabra
		 */
		InputStreamReader fr0 = new InputStreamReader(new FileInputStream("assets/Correspondencia_tags_dels_lemes.txt"), "UTF8");
		BufferedReader br0 = new BufferedReader(fr0);
		
		String s;
		HashMap<String, String> tags = new HashMap<String, String>();
		while ((s = br0.readLine()) != null) {
			
			String[] wordDetails = s.split("\t");
			if(wordDetails != null && wordDetails.length >= 2){
				if(!tags.containsKey(wordDetails[0])){
					tags.put(wordDetails[0], wordDetails[1]);
				}
			}
		}
		fr0.close();
		
		
		/*
		 * Lista de palabras en ingles y su traduccion al espanol dependiendo de su morfologia
		 */
		InputStreamReader fr = new InputStreamReader(new FileInputStream("assets/dic-full-en-es.txt"), "UTF8");
		BufferedReader br = new BufferedReader(fr);
		
		int i = 0;
		/*HashMap<String, HashMap<String, String>> hs = new HashMap<String, HashMap<String, String>>();
		while ((s = br.readLine()) != null) {
			
			String[] wordDetails = s.split("\t");
			if(wordDetails != null && wordDetails.length >= 3){
				
				if(!hs.containsKey(wordDetails[0])){
					HashMap<String, String> tagLema = new HashMap<String, String>();
					tagLema.put(wordDetails[1], wordDetails[2]);
					hs.put(wordDetails[0], tagLema);
				}else{
					HashMap<String, String> tag = hs.get(wordDetails[0]);
					if(!tag.containsKey(wordDetails[1])){
						tag.put(wordDetails[1], wordDetails[2]);
					}
				}
				i++;
			}
			System.out.println("line " + i);
		}*/
		
		HashMap<String, HashMap<String, ArrayList<String>>> hs = new HashMap<String, HashMap<String, ArrayList<String>>>();
		i = 0;
		while ((s = br.readLine()) != null) {
			String[] wordDetails = s.split("\t");
			if(wordDetails != null && wordDetails.length == 3){
				/*wordDetails[0] =  wordDetails[0].toLowerCase().trim();
				wordDetails[2] =  wordDetails[2].toLowerCase().trim();*/
				if(!hs.containsKey(wordDetails[0])){
					HashMap<String, ArrayList<String>> tagLema = new HashMap<String, ArrayList<String>>();
					ArrayList<String> lemas = new ArrayList<String>();
					lemas.add(wordDetails[2]);
					tagLema.put(wordDetails[1], lemas);
					hs.put(wordDetails[0], tagLema);
				}else{
					HashMap<String, ArrayList<String>> tag = hs.get(wordDetails[0]);
					ArrayList<String> containedIn = new ArrayList<String>();
					for (Map.Entry<String, ArrayList<String>> entry : tag.entrySet()) {
				    	String key = entry.getKey();
				    	ArrayList<String> spanishLemas = entry.getValue();
				    	if(spanishLemas.contains(wordDetails[2])){
				    		containedIn.add(key);
				    	}
					}
					if(containedIn.size()==0){
						if(tag.containsKey(wordDetails[1])){
							ArrayList<String> lemas = tag.get(wordDetails[1]);
							lemas.add(wordDetails[2]);
						}else{
							ArrayList<String> lemas = new ArrayList<String>();
							lemas.add(wordDetails[2]);
							tag.put(wordDetails[1], lemas);
						}
					}else{
						if(!containedIn.contains(wordDetails[1])){
							if(tag.containsKey(wordDetails[1])){
								ArrayList<String> lemas = tag.get(wordDetails[1]);
								lemas.add(wordDetails[2]);
							}else{
								ArrayList<String> lemas = new ArrayList<String>();
								lemas.add(wordDetails[2]);
								tag.put(wordDetails[1], lemas);
							}
						}
					}
				}
			}
		}
		fr.close();
		
		/*for (Map.Entry<String, HashMap<String, String>> entry : hs.entrySet()) {
		    String english = entry.getKey();
		    HashMap<String, String> value = entry.getValue();
		    for (Map.Entry<String, String> entry2 : value.entrySet()) {
		    	String tag = entry2.getKey();
		    	String spanish = entry2.getValue();
		    	
		    	System.out.println(english + ", " + tag + "," + spanish);
		    }
		}*/
		
		/*
		 * Correspondencia entre los tags de los lemas en ingles y los tags en español, asi como los dpos y spos
		 */
		InputStreamReader fr1 = new InputStreamReader(new FileInputStream("assets/Correspondencia_tags_eng_es.txt"), "UTF8");
		BufferedReader br1 = new BufferedReader(fr1);
		
		i = 0;
		HashMap<String, HashMap<String, String>> tagsDposSpos = new HashMap<String, HashMap<String, String>>();
		while ((s = br1.readLine()) != null) {
			
			String[] wordDetails = s.split("\t");
			if(wordDetails != null && wordDetails.length >= 2){
				
				if(!tagsDposSpos.containsKey(wordDetails[0])){
					HashMap<String, String> tagLema = new HashMap<String, String>();
					if(wordDetails.length > 2)
						tagLema.put(wordDetails[1], wordDetails[2]);
					else
						tagLema.put(wordDetails[1], "");
					tagsDposSpos.put(wordDetails[0], tagLema);
				}else{
					HashMap<String, String> tag = tagsDposSpos.get(wordDetails[0]);
					if(!tag.containsKey(wordDetails[1])){
						if(wordDetails.length > 2)
							tag.put(wordDetails[1], wordDetails[2]);
						else
							tag.put(wordDetails[1], "");
					}
				}
				i++;
			}
		}
		fr1.close();
		
		for (Map.Entry<String, HashMap<String, String>> entry : tagsDposSpos.entrySet()) {
		    String english = entry.getKey();
		    HashMap<String, String> value = entry.getValue();
		    for (Map.Entry<String, String> entry2 : value.entrySet()) {
		    	String tag = entry2.getKey();
		    	String spanish = entry2.getValue();
		    	
		    	System.out.println(english + ", " + tag + "," + spanish);
		    }
		}
		
		/*
		 * Preposiciones list
		 * */
		List<String> prepositions = new ArrayList<String>();
		prepositions.add("a");
		prepositions.add("ante");
		prepositions.add("bajo");
		prepositions.add("cabe");
		prepositions.add("con");
		prepositions.add("contra");
		prepositions.add("de");
		prepositions.add("desde");
		prepositions.add("durante");
		prepositions.add("en");
		prepositions.add("entre");
		prepositions.add("hacia");
		prepositions.add("hasta");
		prepositions.add("mediante");
		prepositions.add("para");
		prepositions.add("por");
		prepositions.add("según");
		prepositions.add("sin");
		prepositions.add("so");
		prepositions.add("sobre");
		prepositions.add("tras");
		prepositions.add("versus");
		prepositions.add("vía");
		
		/*
		 * Sustitucion
		 */
		InputStreamReader fr2 = new InputStreamReader(new FileInputStream("tmp/textoInglesOrdenCastellano.conll"), "UTF8");
		BufferedReader br2 = new BufferedReader(fr2);
		
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(endFile), "UTF-8"));

		i = 0;
		String previousLema = "";
		String previousTag = "";
		boolean existentialThereFound = false;
		String existentialThereLine = "";
		ArrayList<String> frase = new ArrayList<String>();
		boolean whyLema = false;
		
		boolean modalToDelete = false;
		String modalToDeleteLine = "";
		String modalIndex = "";
		String modalReference = "";
		HashMap<String, String> deletedLineToNewReference = new HashMap<String, String>();
		
		int wordsFoundWithTheirTag, wordsFoundWithUnknownTag, wordsFoundWithOtherTag, wordsNotFound;
		wordsFoundWithTheirTag = wordsFoundWithUnknownTag = wordsFoundWithOtherTag = wordsNotFound = 0;
		ArrayList<String> uniqueWordsNotFound = new ArrayList<String>();
		while ((s = br2.readLine()) != null) {
			String[] wordDetails = s.split("\t");
			if(wordDetails != null && wordDetails.length >= 10){
				
				/*
				 * We get translatad word
				 * */
				boolean knownTag = false;
				if(tags.containsKey(wordDetails[4])){
					knownTag = true;
				}
				String nuevoLema = wordDetails[1];
				if(hs.containsKey(wordDetails[1])){
					HashMap<String, ArrayList<String>> tagLema;
					tagLema = hs.get(wordDetails[1]);
					if(knownTag){
						String tagsCorrespondanceString = tags.get(wordDetails[4]);
						String[] tagsCorrespondance = tagsCorrespondanceString.split("\\|");
						int j=0;
						boolean lemaFound = false;
						while(j < tagsCorrespondance.length && !lemaFound){
							if(tagLema.containsKey(tagsCorrespondance[j])){
								nuevoLema = tagLema.get(tagsCorrespondance[j]).get(0);
								lemaFound = true;
								wordsFoundWithTheirTag ++;
							}
							j++;
						}
						if(!lemaFound){
							if(tagLema.containsKey("unknown")){
								nuevoLema = tagLema.get("unknown").get(0);
								wordsFoundWithUnknownTag++;
							}else{
								nuevoLema = tagLema.values().iterator().next().get(0);
								wordsFoundWithOtherTag ++;
							}
						}
					}
				}else{
					wordsNotFound++;
					if(!uniqueWordsNotFound.contains(wordDetails[1]))
						uniqueWordsNotFound.add(wordDetails[1]);
				}
				
				/*
				 * We get the spanish POS and morphological features
				 * */
				String nuevoTag = wordDetails[4];
				String nuevoCol6 = wordDetails[6];
				String nuevoCol7 = wordDetails[7];
				if(tagsDposSpos.containsKey(wordDetails[4])){
					HashMap<String, String> tagLema;
					tagLema = tagsDposSpos.get(wordDetails[4]);
					for (Map.Entry<String, String> entry : tagLema.entrySet()) {
						nuevoTag = entry.getKey();
						if(!entry.getValue().equals("")){
							nuevoCol6 = entry.getValue();
							nuevoCol7 = nuevoCol6;
						}
					}
				}
				
				if(!BASELINE){
				
					/*
					 * IN can be preposition or conjunction
					 * */
					if(nuevoTag.equals("IN") && prepositions.contains(nuevoLema.toLowerCase())){
						nuevoCol6 = "dpos=Adv|spos=preposition";
						nuevoCol7 = nuevoCol6;
					}
					
					/*
					 * We find the person and number of VBP verbs (present non third person)
					 * */
					if(wordDetails[4].equals("VBP") && previousTag.equals("PRP")){
						switch(previousLema){
							case "i":
								nuevoCol6 += "|number=SG|person=1";
								break;
							case "you":
								nuevoCol6 += "|number=SG|person=2";
								break;
							case "he":
								nuevoCol6 += "|number=SG|person=3";
								break;
							case "she":
								nuevoCol6 += "|number=SG|person=3";
								break;
							case "it":
								nuevoCol6 += "|number=SG|person=3";
								break;
							case "we":
								nuevoCol6 += "|number=PL|person=1";
								break;
							case "they":
								nuevoCol6 += "|number=PL|person=3";
								break;
						}
						nuevoCol7 = nuevoCol6;
					}
					
					/*
					 * We find existential there, later we will replace it by haber if it is followed by the to be verb, if not we will remove it.
					 * */
					if(wordDetails[4].equals("EX") && wordDetails[1].equals("there")){
						existentialThereFound = true;
						existentialThereLine = s;
						continue;
					}
					
					/*
					 * We remove ' and s if they are of POS kind.
					 * */
					if(wordDetails[4].equals("POS") || wordDetails[1].equals("s")){
						deletedLineToNewReference.put(wordDetails[0], wordDetails[8]);
						continue;
					}
					
					/*
					 * We change why by por qué
					 * */
					if(wordDetails[4].equals("WRB") && wordDetails[1].equals("why")){
						nuevoLema = "por";
						nuevoTag = "IN";
						nuevoCol6 = "dpos=Adv|spos=preposition";
						nuevoCol7 = nuevoCol6;
						whyLema = true;
					}
					
					/*
					 * We change the modal verbs by their equivalent in Spanish, or we delete it and change the following verb accordingly
					 * */
					//guardar referencia del MD abans d'eliminar, perque la necesitara el verb seguent.
					//mirar seguent verb si fa referencia al modal
					if(wordDetails[4].equals("MD")){
						switch(wordDetails[1]){
							case "will":
							case "would":
							case "shall":
								modalToDelete = true;
								modalToDeleteLine = s;
								modalIndex = wordDetails[0];
								modalReference = wordDetails[8];
								continue;
							case "could":
							case "might":
								nuevoLema = "poder";
								nuevoTag = "VV";
								nuevoCol6 = "dpos=V|finiteness=FIN|mood=IND|spos=verb|tense=FUT";
								break;
							case "can":
							case "may":
								nuevoLema = "poder";
								nuevoTag = "VV";
								nuevoCol6 = "dpos=V|finiteness=FIN|mood=IND|spos=verb|tense=PRES";
								break;
							case "should":
								nuevoLema = "deber";
								nuevoTag = "VV";
								nuevoCol6 = "dpos=V|finiteness=FIN|mood=IND|spos=verb|tense=FUT";
								break;
							case "must":
							case "ought to":
								nuevoLema = "deber";
								nuevoTag = "VV";
								nuevoCol6 = "dpos=V|finiteness=FIN|mood=IND|spos=verb|tense=PRES";
								break;
						}
						if(previousTag.equals("PRP")){
							switch(previousLema){
								case "i":
									nuevoCol6 += "|number=SG|person=1";
									break;
								case "you":
									nuevoCol6 += "|number=SG|person=2";
									break;
								case "he":
									nuevoCol6 += "|number=SG|person=3";
									break;
								case "she":
									nuevoCol6 += "|number=SG|person=3";
									break;
								case "it":
									nuevoCol6 += "|number=SG|person=3";
									break;
								case "we":
									nuevoCol6 += "|number=PL|person=1";
									break;
								case "they":
									nuevoCol6 += "|number=PL|person=3";
									break;
							}
							nuevoCol7 = nuevoCol6;
						}
					}
					
					if(modalToDelete){
						if (wordDetails[4].equals("VB") || 
							wordDetails[4].equals("VBD") || 
							wordDetails[4].equals("VBG") ||
							wordDetails[4].equals("VBN") ||
							wordDetails[4].equals("VBP") ||
							wordDetails[4].equals("VBZ")){
	
							if(wordDetails[8].equals(modalIndex)){
								wordDetails[8] = modalReference;
								
								if(wordDetails[4].equals("VB")){
									nuevoCol6 = "finiteness=FIN|dpos=V|mood=IND|spos=verb|tense=FUT";
								}else{
									String[] morfFeatures = nuevoCol6.split("\\|");
									for(int z = 0; z < morfFeatures.length; z++){
										if(morfFeatures[z].contains("tense=")){
											morfFeatures[z] = "tense=FUT";
										}
									}
									nuevoCol6 = strJoin(morfFeatures, "|");
								}
								nuevoCol7 = nuevoCol6;
								
								deletedLineToNewReference.put(modalIndex, wordDetails[0]);
								
								modalToDelete = false;
								modalToDeleteLine = "";
								modalIndex = "";
								modalReference = "";
							}
							
						}
						
					}
					
					
					if(existentialThereFound){
						if (wordDetails[4].equals("VBZ") && wordDetails[1].equals("be")){
							nuevoLema = "haber";
							nuevoTag = "VH";
							nuevoCol6 = "dpos=V|finiteness=FIN|mood=IND|number=SG|person=3|spos=verb|tense=PRES";
							nuevoCol7 = nuevoCol6;
							
							//TODO REORDENAR LA FRASE
						}else{
							String[] existentialThereDetails = existentialThereLine.split("\t");
							String col6 = "dpos=V|finiteness=FIN|mood=IND|number=SG|person=3|spos=verb|tense=PRES";
									
							String[] features = existentialThereDetails[6].split("\\|");
							String idFeature = "";
							for(String feature : features){
								if(feature.contains("id0")) 
									idFeature = feature;
							}
							
							if(!idFeature.equals(""))
								col6 += "|"+idFeature;
							
							/*out.write(existentialThereDetails[0] +"\t"+ "haber" +"\t"+ "haber" +
									"\t"+ "haber" +"\t"+ "VH" +"\t"+ "VH" +"\t"+ col6 +
									"\t"+ "dpos=V|finiteness=FIN|mood=IND|number=SG|person=3|spos=verb|tense=PRES" +"\t"+ existentialThereDetails[8] +"\t"+"_"+"\t"+existentialThereDetails[10]+
									"\t"+existentialThereDetails[11]+"\t"+existentialThereDetails[12]+"\t"+existentialThereDetails[13]+"\n");*/
							frase.add(existentialThereDetails[0] +"\t"+ "haber" +"\t"+ "haber" +
									"\t"+ "haber" +"\t"+ "VH" +"\t"+ "VH" +"\t"+ col6 +
									"\t"+ "dpos=V|finiteness=FIN|mood=IND|number=SG|person=3|spos=verb|tense=PRES" +"\t"+ existentialThereDetails[8] +"\t"+"_"+"\t"+existentialThereDetails[10]+
									"\t"+existentialThereDetails[11]+"\t"+existentialThereDetails[12]+"\t"+existentialThereDetails[13]);
						}
						existentialThereFound = false;
					}
				
				}
				
				/*
				 * We get the id0 from the morphological features
				 * */
				String[] features = wordDetails[6].split("\\|");
				String idFeature = "";
				for(String feature : features){
					if(feature.contains("id0")) 
						idFeature = feature;
				}
				
				if(!idFeature.equals(""))
					nuevoCol6 += "|"+idFeature;
				
				/*out.write(wordDetails[0] +"\t"+ nuevoLema +"\t"+ nuevoLema +
						"\t"+ nuevoLema +"\t"+ nuevoTag +"\t"+ nuevoTag +"\t"+ nuevoCol6 +
						"\t"+ nuevoCol7 +"\t"+ wordDetails[8] +"\t"+"_"+"\t"+wordDetails[10]+
						"\t"+wordDetails[11]+"\t"+wordDetails[12]+"\t"+wordDetails[13]+"\n");*/
				frase.add(wordDetails[0] +"\t"+ nuevoLema +"\t"+ nuevoLema +
						"\t"+ nuevoLema +"\t"+ nuevoTag +"\t"+ nuevoTag +"\t"+ nuevoCol6 +
						"\t"+ nuevoCol7 +"\t"+ wordDetails[8] +"\t"+"_"+"\t"+wordDetails[10]+
						"\t"+wordDetails[11]+"\t"+wordDetails[12]+"\t"+wordDetails[13]);
				if(whyLema){
					frase.add(Integer.valueOf(wordDetails[0]) * -1 +"\t"+ "qué" +"\t"+ "qué" +
							"\t"+ "qué" +"\t"+ "WP" +"\t"+ "WP" +"\t"+ "dpos=N|number=SG|spos=interrogative_pronoun|" + idFeature + "_bis" +
							"\t"+ "dpos=N|number=SG|spos=interrogative_pronoun" +"\t"+ wordDetails[0] +"\t"+"_"+"\t"+ "II" +
							"\t"+ "II" +"\t"+"_"+"\t"+"_");
					whyLema = false;
				}
				
				previousLema = wordDetails[1];
				previousTag = wordDetails[4];
				
				i++;
			}else if (wordDetails != null && wordDetails.length == 1){
				previousLema = "";
				previousTag = "";
				reorderLines(frase, out, deletedLineToNewReference);
				frase.clear();
				deletedLineToNewReference.clear();
				out.write("\n");
			}
			
		}
		fr2.close();
		out.close();
		System.out.println("Words found by tag: " + wordsFoundWithTheirTag);
		System.out.println("Words found with UNKNOWN Tag: " + wordsFoundWithUnknownTag);
		System.out.println("Words found with OTHER tag: " + wordsFoundWithOtherTag);
		System.out.println("Words not found: " + wordsNotFound);
		
		
		BufferedWriter out2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("devSet/notFoundWords.txt"), "UTF-8"));
		for(String word: uniqueWordsNotFound){
			out2.write(word+"\n");
		}
		out2.close();

	}
	
	static void reorderLines(ArrayList<String> frase, BufferedWriter out, HashMap<String, String> deletedLineToNewReference) throws IOException{
		int i = 1;
		HashMap<Integer, Integer> indexEquivalent = new HashMap<Integer, Integer>();  
		for (String line: frase) {
			indexEquivalent.put(Integer.parseInt(line.split("\t")[0]), i);
			i++;
	    }
		i=1;
		
		for (String line: frase) {
			String[] wordDetails = line.split("\t");
			
			String head = "_";
			if(wordDetails[8].matches("-?\\d+(\\.\\d+)?") && !wordDetails[8].equals("0")){
				if(indexEquivalent.containsKey(Integer.parseInt(wordDetails[8])))
					head = indexEquivalent.get(Integer.parseInt(wordDetails[8])).toString();
				else if(deletedLineToNewReference.containsKey(wordDetails[8])){
					if(indexEquivalent.containsKey(Integer.parseInt(deletedLineToNewReference.get(wordDetails[8])))){
						head = indexEquivalent.get(Integer.parseInt(deletedLineToNewReference.get(wordDetails[8]))).toString();
					}
				}
			}else{
				head = wordDetails[8];
			}
				
			out.write(i+"\t"+ wordDetails[1] +"\t"+wordDetails[2]+"\t"+
					wordDetails[3] +"\t"+wordDetails[4]+"\t"+wordDetails[5]+"\t"+wordDetails[6]+"\t"+
					wordDetails[7] +"\t"+ head + "\t" +wordDetails[9]+"\t"+wordDetails[10]+"\t"+wordDetails[11] +"\t"+
					wordDetails[12]+"\t"+wordDetails[13]+"\n");
			i++;
		}
	}
}
