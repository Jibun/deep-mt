import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		/*
		 * Lista de palabras en ingles y su traduccion al espanol dependiendo de su morfologia
		 */
		InputStreamReader dic = new InputStreamReader(new FileInputStream("assets/wiktionary.en-es"), "UTF8");
		
		BufferedReader br = new BufferedReader(dic);
		
		
		String s;
		int i = 0;
		HashMap<String, HashMap<String, ArrayList<String>>> hs = new HashMap<String, HashMap<String, ArrayList<String>>>();
		while ((s = br.readLine()) != null) {
			
			String[] wordDetails = s.split(" \\|\\|\\| ");
			if(wordDetails != null && wordDetails.length == 2){
				
				if(!hs.containsKey(wordDetails[0].trim())){
					HashMap<String, ArrayList<String>> tagLema = new HashMap<String, ArrayList<String>>();
					ArrayList<String> lemas = new ArrayList<String>();
					lemas.add(wordDetails[1].trim());
					tagLema.put("unknown", lemas);
					hs.put(wordDetails[0].trim(), tagLema);
				}else{
					HashMap<String, ArrayList<String>> tag = hs.get(wordDetails[0].trim());
					ArrayList<String> containedIn = new ArrayList<String>();
					for (Map.Entry<String, ArrayList<String>> entry : tag.entrySet()) {
				    	String key = entry.getKey();
				    	ArrayList<String> spanishLemas = entry.getValue();
				    	if(spanishLemas.contains(wordDetails[1].trim())){
				    		containedIn.add(key);
				    	}
					}
					if(containedIn.size() == 0){
						if(tag.containsKey("unknown")){
							ArrayList<String> lemas = tag.get("unknown");
							lemas.add(wordDetails[1].trim());
						}else{
							ArrayList<String> lemas = new ArrayList<String>();
							lemas.add(wordDetails[1].trim());
							tag.put("unknown", lemas);
						}
					}else{
						if(!containedIn.contains("unknown")){
							if(tag.containsKey("unknown")){
								ArrayList<String> lemas = tag.get("unknown");
								lemas.add(wordDetails[1].trim());
							}else{
								ArrayList<String> lemas = new ArrayList<String>();
								lemas.add(wordDetails[1].trim());
								tag.put("unknown", lemas);
							}
						}
					}
				}
				i++;
			}
		}
		dic.close();
		
		InputStreamReader dic1 = new InputStreamReader(new FileInputStream("assets/dic-raw1.txt"), "UTF8");
		BufferedReader br2 = new BufferedReader(dic1);
		i = 0;
		while ((s = br2.readLine()) != null) {
			
			String[] wordDetails = s.split("\t");
			if(wordDetails != null && wordDetails.length >= 2){
				String[] wordsPerDefinition = wordDetails[0].toLowerCase().split(" ");
				if(wordsPerDefinition.length == 1){
					String reconstructedCorrespondances =  wordDetails[1].replaceAll("\\(.+?\\)", "");
					String[] correspondances = reconstructedCorrespondances.split("; ");
					if(correspondances.length == 1) correspondances = reconstructedCorrespondances.split(", ");
					
					if(!hs.containsKey(wordDetails[0].toLowerCase())){
						HashMap<String, ArrayList<String>> tagLema = new HashMap<String, ArrayList<String>>();
						ArrayList<String> lemas = new ArrayList<String>();
						for(String c1 : correspondances){
							lemas.add(c1.trim());
						}
						tagLema.put("unknown", lemas);
						hs.put(wordDetails[0].toLowerCase(), tagLema);
						i++;
					}else{
						HashMap<String, ArrayList<String>> tag = hs.get(wordDetails[0].toLowerCase());
						for(String c1 : correspondances){
							ArrayList<String> containedIn = new ArrayList<String>();
							for (Map.Entry<String, ArrayList<String>> entry : tag.entrySet()) {
						    	String key = entry.getKey();
						    	ArrayList<String> spanishLemas = entry.getValue();
						    	if(spanishLemas.contains(c1.trim())){
						    		containedIn.add(key);
						    	}
							}
							if(containedIn.size() == 0){
								if(tag.containsKey("unknown")){
									ArrayList<String> lemas = tag.get("unknown");
									lemas.add(c1.trim());
								}else{
									ArrayList<String> lemas = new ArrayList<String>();
									lemas.add(c1.trim());
									tag.put("unknown", lemas);
								}
							}else{
								if(!containedIn.contains("unknown")){
									if(tag.containsKey("unknown")){
										ArrayList<String> lemas = tag.get("unknown");
										lemas.add(c1.trim());
									}else{
										ArrayList<String> lemas = new ArrayList<String>();
										lemas.add(c1.trim());
										tag.put("unknown", lemas);
									}
								}
							}
						}
					}
				}
			}
			
		}
		dic1.close();
		
		InputStreamReader dic3 = new InputStreamReader(new FileInputStream("assets/en_es_all.txt"), "UTF8");
		BufferedReader br4 = new BufferedReader(dic3);
		
		i = 0;
		while ((s = br4.readLine()) != null) {
			String[] wordDetails = s.split("\t");
			if(wordDetails != null && wordDetails.length == 3){
				wordDetails[0] =  wordDetails[0].toLowerCase().trim();
				wordDetails[2] =  wordDetails[2].toLowerCase().trim();
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
		dic3.close();
		
		InputStreamReader dic2 = new InputStreamReader(new FileInputStream("assets/dic-raw2revised.txt"), "UTF8");
		BufferedReader br3 = new BufferedReader(dic2);
		//BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("assets/dic-raw2revised.txt"), "UTF-8"));
		
		i = 0;
		while ((s = br3.readLine()) != null) {
//			s =  s.replaceAll("a/", "á");
//			s =  s.replaceAll("e/", "é");
//			s =  s.replaceAll("i/", "í");
//			s =  s.replaceAll("o/", "ó");
//			s =  s.replaceAll("u/", "ú");
//			s =  s.replaceAll("n~", "ñ");
//			s =  s.replaceAll(",-a", "");
//			s =  s.replaceAll("adj\\.", "[adjective]");
//			s =  s.replaceAll("\\(v\\)", "[verb]");
//			s =  s.replaceAll("\\(n\\)", "[noun]");
//			s =  s.replaceAll("\\(.+?\\)", "");
//			s =  s.replaceAll("/", ";");
//			s =  s.replaceAll("\tel ", "\t");
//			s =  s.replaceAll("\tla ", "\t");
//			s =  s.replaceAll("\tlos ", "\t");
//			s =  s.replaceAll("\tlas ", "\t");
//			out.write(s + "\n");
			
			String[] wordDetails = s.split("\t");
			if(wordDetails != null && wordDetails.length >= 2){
				wordDetails[0] =  wordDetails[0].toLowerCase().trim();
				String[] wordsPerDefinition = wordDetails[0].split(" ");
				if(wordsPerDefinition.length == 1){
					wordDetails[1] =  wordDetails[1].toLowerCase().trim();
					String[] correspondances = wordDetails[1].split("; ");
					if(correspondances.length == 1) correspondances = wordDetails[1].split(", ");
					String type = "unknown";
					for(int j = 0; j < correspondances.length; j++){
						Matcher m = Pattern.compile("\\[([^\\[]+)\\]").matcher(correspondances[j]);
					    if(m.find()) {
					       type = m.group(1);
					       
					       if(type.equals("article")){
					    	   type = "det";
					       }else if(type.equals("adverb")){
					    	   type = "adv";
					       }else if(type.equals("noun")){
					    	   type = "n";
					       }else if(type.equals("adjective")){
					    	   type = "adj";
					       }else if(type.equals("verb")){
					    	   type = "v";
					       }else if(type.equals("preposition")){
					    	   type = "prep";
					       }else if(type.equals("conjunction")){
					    	   type = "conj";
					       }else if(type.equals("pronoun")){
					    	   type = "pron";
					       }
					       
					       correspondances[j] = m.replaceAll("");
				    	}
					}  
					if(!hs.containsKey(wordDetails[0])){
						HashMap<String, ArrayList<String>> tagLema = new HashMap<String, ArrayList<String>>();
						ArrayList<String> lemas = new ArrayList<String>();
						for(String c1 : correspondances){
							lemas.add(c1.trim());
						}
						tagLema.put(type, lemas);
						hs.put(wordDetails[0], tagLema);
						i++;
					}else{
						HashMap<String, ArrayList<String>> tag = hs.get(wordDetails[0]);

						for(String c1 : correspondances){
							ArrayList<String> containedIn = new ArrayList<String>();
							for (Map.Entry<String, ArrayList<String>> entry : tag.entrySet()) {
						    	String key = entry.getKey();
						    	ArrayList<String> spanishLemas = entry.getValue();
						    	if(spanishLemas.contains(c1.trim())){
						    		containedIn.add(key);
						    	}
							}
							if(containedIn.size()==0){
								if(tag.containsKey(type)){
									ArrayList<String> lemas = tag.get(type);
									lemas.add(c1.trim());
								}else{
									ArrayList<String> lemas = new ArrayList<String>();
									lemas.add(c1.trim());
									tag.put(type, lemas);
								}
							}else{
								if(!containedIn.contains(type)){
									if(tag.containsKey(type)){
										ArrayList<String> lemas = tag.get(type);
										lemas.add(c1.trim());
									}else{
										ArrayList<String> lemas = new ArrayList<String>();
										lemas.add(c1.trim());
										tag.put(type, lemas);
									}
								}
							}
						}
					}
				}
			}
		}
		dic2.close();
		//out.close();
		
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("assets/dic-full-en-es.txt"), "UTF-8"));
		SortedSet<String> engWords = new TreeSet<String>(hs.keySet());
		for (String engWord : engWords) { 
			HashMap<String,ArrayList<String>> TagsPlusEsp = hs.get(engWord);
			SortedSet<String> tags = new TreeSet<String>(TagsPlusEsp.keySet());
			for (String tag : tags) { 
				ArrayList<String> spaWords = TagsPlusEsp.get(tag);
				Collections.sort(spaWords);
				for(String spaWord: spaWords){
					out.write(engWord + "\t" + tag + "\t" + spaWord + "\n");
				}
			}
		}
		out.close();
	}
	
	

}
