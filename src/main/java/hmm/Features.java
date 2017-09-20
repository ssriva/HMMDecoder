package hmm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Features {

	public Features() {
		// TODO Auto-generated constructor stub
	}

	static HashMap<String, ArrayList<String> > hmap = new HashMap<String, ArrayList<String> >();

	static{
		try {
			
			String gazetteer = "/Users/shashans/Documents/Courses/LSP/Assignments/Assign_1/hw1_files/gazetteer.txt";
			BufferedReader br = new BufferedReader(new FileReader(gazetteer));

			String line="";
			while ((line=br.readLine())!=null) {
				line = line.trim();
			    String[] tok = line.split(" ");
			    for(int i=1;i<tok.length;i++){
			    	if(hmap.containsKey(tok[i])){
			    		ArrayList<String> arrList = new ArrayList<String>(hmap.get(tok[i])); 	
				    	if(!arrList.contains(tok[0]))
				    		arrList.add(tok[0].trim());
			    		hmap.put(tok[i], arrList);
			    		//System.out.println("Putting in gazmap:"+tok[i]);
			    	}
			    	else{
			    		ArrayList<String> arrList = new ArrayList<String>();	
				    	arrList.add(tok[0].trim());
			    		hmap.put(tok[i], arrList);
			    		//System.out.println("Putting in gazmap:"+tok[i]);
			    	}	
			    }
			}
			br.close();
		} catch (Exception e) {
			System.err.println(e+"Could not open gazetteer txt file.");
		}
	}
	
	public static ArrayList<String> generate( ArrayList<Token> sentence, int index, String state, String prevstate){
		
		ArrayList<String> featureVec = new ArrayList<String>();
		
		//Current Word
		String word_i= getWord(sentence, index, "Wi=", state);
		featureVec.add(word_i+":Ti="+state);
		String lower_i= getLowercasedWord(sentence, index,"Oi=", state);
		featureVec.add(lower_i+":Ti="+state);
		String pos_i= getPOS(sentence, index, "Pi=",state);
		featureVec.add(pos_i+":Ti="+state);
		String shape_i=getShape(sentence, index, "Si=", state);
		featureVec.add(shape_i+":Ti="+state);
		
		//For tokens except start state, add features depending on previous state
		if(index>0){
			String word_iM=getWord(sentence, index-1, "Wi-1=",state);
			featureVec.add(word_iM+":Ti="+state);
			String lower_iM=getLowercasedWord(sentence, index-1, "Oi-1=", state);
			featureVec.add(lower_iM+":Ti="+state);
			String pos_iM=getPOS(sentence, index-1, "Pi-1=", state);
			featureVec.add(pos_iM+":Ti="+state);
			String shape_iM=  getShape(sentence, index-1, "Si-1=", state);
			featureVec.add(shape_iM+":Ti="+state);
			
			//16 features based on previous token
			featureVec.add(word_i+":"+word_iM+":Ti="+state);
			featureVec.add(word_i+":"+lower_iM+":Ti="+state);
			featureVec.add(word_i+":"+pos_iM+":Ti="+state);
			featureVec.add(word_i+":"+shape_iM+":Ti="+state);			
			featureVec.add(lower_i+":"+word_iM+":Ti="+state);
			featureVec.add(lower_i+":"+lower_iM+":Ti="+state);
			featureVec.add(lower_i+":"+pos_iM+":Ti="+state);
			featureVec.add(lower_i+":"+shape_iM+":Ti="+state);
			featureVec.add(pos_i+":"+word_iM+":Ti="+state);
			featureVec.add(pos_i+":"+lower_iM+":Ti="+state);
			featureVec.add(pos_i+":"+pos_iM+":Ti="+state);
			featureVec.add(pos_i+":"+shape_iM+":Ti="+state);
			featureVec.add(shape_i+":"+word_iM+":Ti="+state);
			featureVec.add(shape_i+":"+lower_iM+":Ti="+state);
			featureVec.add(shape_i+":"+pos_iM+":Ti="+state);
			featureVec.add(shape_i+":"+shape_iM+":Ti="+state);
			
			//4 features based on previous token and previous state
			featureVec.add("Ti-1="+prevstate+":Ti="+state);
			featureVec.add(word_iM+":"+"Ti-1="+prevstate+":Ti="+state);
			featureVec.add(lower_iM+":"+"Ti-1="+prevstate+":Ti="+state);
			featureVec.add(pos_iM+":"+"Ti-1="+prevstate+":Ti="+state);
			featureVec.add(shape_iM+":"+"Ti-1="+prevstate+":Ti="+state);
		}
		
		//For tokens except stop state, add features depending on next state
		if(index<=sentence.size()-2){
			String word_iP=getWord(sentence, index+1, "Wi+1=", state);
			featureVec.add(word_iP+":Ti="+state);
			String lower_iP=getLowercasedWord(sentence, index+1, "Oi+1=",state);
			featureVec.add(lower_iP+":Ti="+state);
			String pos_iP=getPOS(sentence, index+1, "Pi+1=",state);
			featureVec.add(pos_iP+":Ti="+state);
			String shape_iP=getShape(sentence, index+1, "Si+1=", state);
			featureVec.add(shape_iP+":Ti="+state);
			
			//16 features based on net token
			featureVec.add(word_i+":"+word_iP+":Ti="+state);
			featureVec.add(word_i+":"+lower_iP+":Ti="+state);
			featureVec.add(word_i+":"+pos_iP+":Ti="+state);
			featureVec.add(word_i+":"+shape_iP+":Ti="+state);	
			featureVec.add(lower_i+":"+word_iP+":Ti="+state);
			featureVec.add(lower_i+":"+lower_iP+":Ti="+state);
			featureVec.add(lower_i+":"+pos_iP+":Ti="+state);
			featureVec.add(lower_i+":"+shape_iP+":Ti="+state);
			featureVec.add(pos_i+":"+word_iP+":Ti="+state);
			featureVec.add(pos_i+":"+lower_iP+":Ti="+state);
			featureVec.add(pos_i+":"+pos_iP+":Ti="+state);
			featureVec.add(pos_i+":"+shape_iP+":Ti="+state);
			featureVec.add(shape_i+":"+word_iP+":Ti="+state);
			featureVec.add(shape_i+":"+lower_iP+":Ti="+state);
			featureVec.add(shape_i+":"+pos_iP+":Ti="+state);
			featureVec.add(shape_i+":"+shape_iP+":Ti="+state);
			
			//4 features based on next token and previous tag and current state
			featureVec.add(word_iP+":"+"Ti-1="+prevstate+":Ti="+state);
			featureVec.add(lower_iP+":"+"Ti-1="+prevstate+":Ti="+state);
			featureVec.add(pos_iP+":"+"Ti-1="+prevstate+":Ti="+state);
			featureVec.add(shape_iP+":"+"Ti-1="+prevstate+":Ti="+state);
		}
		
		//4 features based on current token, previous state and current state
		featureVec.add(word_i+":"+"Ti-1="+prevstate+":Ti="+state);
		featureVec.add(lower_i+":"+"Ti-1="+prevstate+":Ti="+state);
		featureVec.add(pos_i+":"+"Ti-1="+prevstate+":Ti="+state);
		featureVec.add(shape_i+":"+"Ti-1="+prevstate+":Ti="+state);
		
		//8. Prefix features
		if(!(state.matches("<START>")||(state.matches("<STOP>")))){
			for(int i=1; i<=4;i++){
				if(word_i.length()-3>=i){
					featureVec.add("PREi="+word_i.substring(3, i+3)+":Ti="+ state);
				}
			}
		}
		else 
			featureVec.add("PREi="+state+":Ti="+ state);
		
		//Found in gazetteer in same role?
		{
			Boolean found=false;
			String w = word_i.substring(3);
			//System.out.println("Searching in gazmap:"+w);
			if(hmap.containsKey(w)){
				ArrayList<String> aL = hmap.get(w);
				for(int i=0;i<aL.size();i++){
					//System.out.println("Found in gazetteer:"+word_i+" "+state+" "+aL.get(i)+" "+state.contains(aL.get(i)));
					if(state.contains(aL.get(i).trim())){
						//System.out.println("Found in gazetteer:"+word_i+" "+state+" "+aL.get(i));
						featureVec.add("GAZi=True:"+"Ti="+state);
						found = true;
					}
				}
			}
			if(!found){
				featureVec.add("GAZi=False:"+"Ti="+state);
			}
		}
		
		
		//First char capital
		if((state.matches("<START>")||(state.matches("<STOP>"))))
			featureVec.add("CAPi=False"+":Ti="+state);
		else if(word_i.charAt(3)>='A' && word_i.charAt(3)<='Z')
			featureVec.add("CAPi=True"+":Ti="+state);
		//else if(Character.isAlphabetic(word_i.charAt(0)) && Character.isUpperCase(word_i.charAt(0)))
		//	featureVec.add("CAPi=True"+":Ti="+state);
		else
			featureVec.add("CAPi=False"+":Ti="+state);
		
		
		//Word position
		featureVec.add("POSi="+index+":Ti="+state);
		
		
		
		return featureVec;
	}

	private static String getShape(ArrayList<Token> sentence, int index, String symbol, String state) {
		
		if(index==0) return symbol+"<START>";
		if(index==sentence.size()-1) return symbol+"<STOP>";
		
		// Shape of string based on Lowercase, uppercase,Digits in it
		String word = sentence.get(index).getWord();
		String shapeOfString ="";
		for(int i=0;i<word.length();i++){
			if(Character.isUpperCase(word.charAt(i)))
				shapeOfString= shapeOfString+"A";
			else if(Character.isLowerCase(word.charAt(i)))
				shapeOfString= shapeOfString+"a";
			else if(Character.isDigit(word.charAt(i)))
				shapeOfString= shapeOfString+"d";
			else
				shapeOfString= shapeOfString+word.charAt(i);
		}
		return symbol+shapeOfString;
	}

	private static String getPOS(ArrayList<Token> sentence, int index, String symbol, String state) {
		if(index==0) return symbol+"<START>";
		if(index==sentence.size()-1) return symbol+"<STOP>";
		return symbol+sentence.get(index).getPos();
	}

	private static String getLowercasedWord(ArrayList<Token> sentence, int index, String symbol, String state) {
		if(index==0) return symbol+"<START>";
		if(index==sentence.size()-1) return symbol+"<STOP>";
		return symbol+sentence.get(index).getWord().toLowerCase();
	}

	private static String getWord(ArrayList<Token> sentence, int index, String symbol, String state) {
		if(index==0) return symbol+"<START>";
		if(index==sentence.size()-1) return symbol+"<STOP>";
		return symbol+sentence.get(index).getWord();
	}
	
}
