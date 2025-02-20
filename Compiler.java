
package CompConstructionPackage;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class Compiler {

    public static void main(String[] args) throws Exception {
        Automata automata = new Automata();
        //We recognize lower case letters as identifiers
        //DFA starts from q0 and if we get a lower case letter it moves to state IDENTIFIER
        //After this all lower case letters remain in this state(self loop)
        for (char c = 'a'; c <= 'z'; c++) {
            automata.addTransition("q0", c, "IDENTIFIER");
            automata.addTransition("IDENTIFIER", c, "IDENTIFIER"); // Allow multi-character identifiers
        }
        //We recognize digits as identifiers
        //DFA starts from q0 and if we get digit it moves to state Integer
        //After this all digits remain in this state(self loop)
        //if dfa is in decimal and reads anotther digit then remains in the same state
        for (char c = '0'; c <= '9'; c++) {
            automata.addTransition("q0", c, "INTEGER");
            automata.addTransition("INTEGER", c, "INTEGER");
            automata.addTransition("DECIMAL", c, "DECIMAL");
        }
       
     

        // if we get decimal in INTEGER state we go to DECIMAL state
        automata.addTransition("INTEGER", '.', "DECIMAL");

        // DFA transitions for mathematical Operators and punctuations and send them all to OPERATOR STATE
        automata.addTransition("q0", '=', "OPERATOR");
        automata.addTransition("q0", '+', "OPERATOR");
        automata.addTransition("q0", '-', "OPERATOR");
        automata.addTransition("q0", '*', "OPERATOR");
        automata.addTransition("q0", '/', "OPERATOR");
        automata.addTransition("q0", '%', "OPERATOR");
        automata.addTransition("q0", '^', "OPERATOR");
        automata.addTransition("q0", ';', "OPERATOR");

        // Ensuring operators are correctly handled after identifiers/numbers
        automata.addTransition("IDENTIFIER", ';', "OPERATOR");
        automata.addTransition("INTEGER", ';', "OPERATOR");
        automata.addTransition("DECIMAL", ';', "OPERATOR");
        automata.addTransition("BOOLEAN", ';', "OPERATOR");
        
        automata.addTransition("IDENTIFIER", '+', "OPERATOR");
        automata.addTransition("INTEGER", '+', "OPERATOR");
        automata.addTransition("DECIMAL", '+', "OPERATOR");

        automata.addTransition("INTEGER", '^', "OPERATOR");
        automata.addTransition("INTEGER", '%', "OPERATOR");
        automata.addTransition("IDENTIFIER", '%', "OPERATOR");
        automata.addTransition("INTEGER", '*', "OPERATOR");
        automata.addTransition("DECIMAL", '*', "OPERATOR");


        automata.setInitialState("q0");//q0 is initial state
        //All these are valid final states
        automata.addFinalState("IDENTIFIER");
        automata.addFinalState("INTEGER");
        automata.addFinalState("DECIMAL");
        automata.addFinalState("OPERATOR");

        automata.displayTransitionTable();
        //Reading file line by line and storing in codebuilder
        File file = new File("C:\\Users\\sherr\\eclipse-workspace\\CompilerConstructionProj\\myprogram.txt");
        Scanner scanner = new Scanner(file);
        StringBuilder codeBuilder = new StringBuilder();
        int lineNumber = 1;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
           // System.out.println("Reading Line " + lineNumber + ": " + line);
            codeBuilder.append(line).append("\n");
            lineNumber++;
        }
        scanner.close();

        // we call Preprocess function of Lexical Analyzer to remove comments from code 
        String code = LexicalAnalyzer.preprocessCode(codeBuilder.toString());

        System.out.println("Code After Preprocessing:\n" + code);
        //Tokenizing the Preprocessed code
        List<String> tokens = LexicalAnalyzer.tokenize(code, automata);

        // Displaying  Tokens
        System.out.println("Tokens After Tokenization: " + tokens);

        SymbolTable symbolTable = new SymbolTable();

        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);

            // Identifing all the  variables from the tokens
            if (token.startsWith("TOKEN:")) {
                String varName = token.split(":")[1];//eg TOKEN:a so token.split function ignores everything before : to give a

                // Checking if next token is assignment op
                if (i + 2 < tokens.size() && tokens.get(i + 1).equals("TOKEN:=")) {
                    String assignedValue = tokens.get(i + 2);
                    String varType = "unknown";

                    // Detecting variable type
                    if (assignedValue.startsWith("TOKEN:")) {
                        String value = assignedValue.split(":")[1];

                        if (value.matches("-?\\d+")) { // Integer 
                            varType = "integer";
                        } else if (value.matches("-?\\d+\\.\\d+")) { // Decimal 
                            varType = "decimal";
                        } else if (value.equals("true") || value.equals("false")) { // Boolean 
                            varType = "boolean";
                        } else { // If assigned from another variable, look it up
                            varType = symbolTable.lookup(value);
                        }
                    }

                    // Adding  variable with detected type to the Symbol Table hashmap
                    symbolTable.addEntry(varName, varType, true);
                }
            }
        }



        // Displaying Symbol Table
        System.out.println("Final Symbol Table:");
        symbolTable.displayTable();
    

    }
}



