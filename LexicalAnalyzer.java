package CompConstructionPackage;

import java.util.*;

class LexicalAnalyzer {
 
	public static List<String> tokenize(String code, Automata dfa) {
	    List<String> tokens = new ArrayList<>();
	    System.out.println("🔍 DEBUG: Tokenizing input using DFA: " + code);

	    String[] words = code.split("\\s+"); //Splitting input by spaces

	    for (String word : words) {
	    	//if word ends with semi colon it is seperated from the word and used as token
	        if (word.endsWith(";")) {
	            tokens.add("TOKEN:" + word.substring(0, word.length() - 1)); // Adding word without ;
	            tokens.add("TOKEN:;"); // Adding semicolon as separate token
	            continue;  // after ; no need for further tokenizing this word
	        }
	        //Detecting output as special keyword and preventing merging with other tokens
	        if (word.equals("output")) {
	            tokens.add("KEYWORD:" + word);
	            continue; // Skipping further checking
	        }

	        // DFA Tokenization Process starting from q0
	        if (dfa.hasTransition("q0", word.charAt(0))) {
	            String currentState = dfa.getInitialState();
	            StringBuilder currentToken = new StringBuilder();
	            //We iterate through each char of word
	            for (char ch : word.toCharArray()) {
	            	//if there is a transition then current state is moved to next state
	                if (dfa.hasTransition(currentState, ch)) {
	                    currentState = dfa.getNextState(currentState, ch);
	                    currentToken.append(ch);
	                } else { //if no transition word processing stopped
	                    break;
	                }
	            }
	            //if final state reached Token created
	            if (dfa.isFinalState(currentState)) {
	                tokens.add("TOKEN:" + currentToken.toString());
	            }
	        }
	    }

	   // System.out.println("Generated Tokens using DFA: " + tokens);
	    return tokens;
	}



    public static String preprocessCode(String code) {
        StringBuilder filteredCode = new StringBuilder();
        boolean inMultiLineComment = false;

        String[] lines = code.split("\\r?\\n");//Splits the code line by line 

        for (String line : lines) {
            String trimmedLine = line.trim();//trimming spaces from each line

            // Start of a multi line comment
            if (trimmedLine.startsWith("/*")) {
                inMultiLineComment = true;
            }

            // Only processing non commented code
            if (!inMultiLineComment) {
                // Removing everything after single line comments 
                int singleLineCommentIndex = trimmedLine.indexOf("//");
                if (singleLineCommentIndex != -1) {
                    trimmedLine = trimmedLine.substring(0, singleLineCommentIndex).trim();
                }

                // Keeping valid code
                if (!trimmedLine.isEmpty()) {
                    filteredCode.append(trimmedLine).append("\n");
                }
            }

            // Ending of a multi line comment
            if (trimmedLine.endsWith("*/")) {
                inMultiLineComment = false;
            }
        }

        String finalCode = filteredCode.toString().trim();
       // System.out.println("Fixed Code After Preprocessing:\n" + finalCode); // Debugging print
        return finalCode;
    }

}
