package hmm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class HMMTagger {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		if(args.length<1){
			System.err.println("Incorrect usage.");
			System.err.println("USAGE: exec inputFile");
			return;
		}
		String inputFile = args[0];
		String outputFile = inputFile+".annotated";
		//outputFile = "/Users/shashans/Documents/Courses/LSP/Assignments/Assign_1/hw1_files/test.annotated";
		
		{
			BufferedReader br;
			
			try {
				br = new BufferedReader(new FileReader(inputFile));
				PrintWriter writer = new PrintWriter(outputFile, "UTF-8");
				
				String line;
				ArrayList<Token> sentence = new ArrayList<Token>();
				sentence.add(new Token("<START>"));
				
				while ( (line=br.readLine()) != null ) {
					line = line.trim();
					String[] toks = line.split(" ");    //Get individual tokens by splitting on spaces
					
					if(toks.length==4){
						Token tok = new Token(toks[0],toks[1],toks[2],toks[3]);
						sentence.add(tok);
					}
					else if(line.equals("")){
						sentence.add(new Token("<STOP>"));
						for(int i=0;i<sentence.size();i++){
							System.out.print(sentence.get(i).getWord()+" ");
						}
						System.out.println();
						
						String[] optimalTags = Decoder.decode(sentence);
						
						for(int i=0;i<sentence.size();i++){
							System.out.print(optimalTags[i]+" ");
						}
						System.out.println();

						for(int i=1;i<=sentence.size()-2;i++){
							Token t = sentence.get(i);
							writer.println(t.getWord()+" "+t.getPos()+" "+t.getChuckTag()+" "+t.getLabel()+" "+optimalTags[i]);
						}
						writer.println();
						
						sentence.clear();
						sentence.add(new Token("<START>"));
						//break;
					}
					else
						System.out.println("Incorrect number of tokens : "+ line);
				}
				br.close();
				writer.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	}

}
