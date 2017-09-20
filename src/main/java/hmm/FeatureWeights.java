package hmm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class FeatureWeights {

	public FeatureWeights() {
		// TODO Auto-generated constructor stub
	}
	
	public static Boolean verbose = false;
	static HashMap<String, Double > hmap = new HashMap<String, Double >();
	static{
		String line="";
		try {
			
			String weightfile = "/Users/shashans/Documents/Courses/LSP/Assignments/Assign_1/hw1_files/weights";
			BufferedReader br = new BufferedReader(new FileReader(weightfile));

			while ((line=br.readLine())!=null) {
				line=line.trim();
			    String[] tok = line.split(" ");
			    if(tok.length>1){
			    	hmap.put(tok[0], Double.parseDouble(tok[1]));
				}
			}
			br.close();
		} catch (Exception e) {
			System.err.println(line);
			System.err.println(e);
		}
	}
	
	public static double stateScore(ArrayList<Token> sentence, int index, String state, String prevstate){
		
		/*Build FeatureVector*/
		ArrayList<String> featureVec = new ArrayList<String>();
		featureVec.addAll(Features.generate(sentence, index, state, prevstate) );
		
		/*Calculate w.f(x,y,z) */
		double val = 0;
		for(String s:featureVec){
			if(hmap.containsKey(s)){
				if(verbose)
					System.out.println(s); //+" Weight:"+hmap.get(s));
				val += hmap.get(s);
			}
			else{
				if(verbose)
					System.out.println(s);
			}
		}
	
		return val;
	}
	
}
