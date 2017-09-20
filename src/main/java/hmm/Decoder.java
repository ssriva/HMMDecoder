package hmm;

import java.util.ArrayList;

public class Decoder {

	public Decoder() {
		// TODO Auto-generated constructor stub
	}

	public static String[] decode(ArrayList<Token> sentence){
		
		String[] states = {"I-LOC","I-MISC","I-ORG","I-PER","B-LOC","B-MISC","B-ORG","B-PER","O"};
		
		
		double[][] vals = new double[states.length][sentence.size()];
		int[][] backPtr = new int[states.length][sentence.size()];
		
		/*First token*/
		for(int j=0;j<states.length;j++){
			vals[j][1] = FeatureWeights.stateScore(sentence, 1, states[j], "<START>");
			backPtr[j][1] = -1;
			//System.out.println("Posn: 1"+"State:"+states[j]+"PrevState:"+"<START>"+" Value:" + vals[j][1]);
		}
		
		/*Intermediate tokens*/
		for(int i=2;i<=sentence.size()-2;i++){
			{
				for(int j=0;j<states.length;j++){
					vals[j][i] = FeatureWeights.stateScore(sentence, i, states[j], states[0]) + vals[0][i-1];
					backPtr[j][i] = 0;
					for(int k=1;k<states.length;k++){
						if(FeatureWeights.stateScore(sentence, i, states[j], states[k]) + vals[k][i-1] > vals[j][i]){
							vals[j][i] = FeatureWeights.stateScore(sentence, i, states[j], states[k]) + vals[k][i-1];
							backPtr[j][i] = k;
						}
					}
					//System.out.println("Posn: "+ i +"State:"+states[j]+"PrevState:"+states[backPtr[j][i]]+" Value:" + vals[j][i]);
				}
			}
		}
		
		/*Stop token*/
		double finalv;
		{
			int i= sentence.size()-1;
			finalv = FeatureWeights.stateScore(sentence, i, "<STOP>", states[0]) + vals[0][i-1];
			backPtr[0][i] = 0;
			for(int k=1;k<states.length;k++){
				if(FeatureWeights.stateScore(sentence, i, "<STOP>", states[k]) + vals[k][i-1] > finalv){
					finalv = FeatureWeights.stateScore(sentence, i, "<STOP>", states[k]) + vals[k][i-1];
					backPtr[0][i] = k;
				}
			}	
		}
		
		//Retrace backpointers from backPtr[0][sentence.size()-1];
		String[] optimalTags = new String[sentence.size()];
		optimalTags[0]="<START>";
		optimalTags[sentence.size()-1]="<STOP>";
		int l=0;
		
		//FeatureWeights.verbose=true;
		for(int i=sentence.size()-1; i>=2; i--){
			optimalTags[i-1] = states[backPtr[l][i]];
			l = backPtr[l][i];
			FeatureWeights.stateScore(sentence, i, optimalTags[i], optimalTags[i-1]);
			//System.out.println("L is"+l+states[l]);
		}
		FeatureWeights.stateScore(sentence, 1, optimalTags[1], "<START>");
		FeatureWeights.verbose=false;
					
		return optimalTags;
	}
}
