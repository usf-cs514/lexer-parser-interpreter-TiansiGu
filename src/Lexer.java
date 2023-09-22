import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class to build an array of Tokens from an input file
 * @author Tiansi Gu
 * @see Token
 * @see Parser
 */
public class Lexer {

    // Data Members
    private String buffer; //all the data in the given file
    private int index = 0; //index of a specific character when looping through the buffer
    private int lineNum = 1; //the current lineNum we are at when looping through the buffer

    // Constants, token types
    public static final String INTTOKEN="INT";
    public static final String IDTOKEN="ID";
    public static final String ASSMTTOKEN="ASSMT";
    public static final String PLUSTOKEN="PLUS";
    public static final String EOFTOKEN="EOF";
    public static final String UNKNOWN = "UNKNOWN";


    /**
     * Constructor
     * call getInput to get the file data into our buffer
     * @param fileName the file we open
     */
    public Lexer(String fileName) {
        getInput(fileName);
    }

    /**
     * Reads given file into the data member buffer
     * @param fileName name of file to parse
    */
    private void getInput(String fileName)  {
        try {
            Path filePath = Paths.get(fileName);
            byte[] allBytes = Files.readAllBytes(filePath);
            buffer = new String (allBytes);
        } catch (IOException e) {
            System.out.println ("You did not enter a valid file name in the run arguments.");
            System.out.println ("Please enter a string to be parsed:");
            Scanner scanner = new Scanner(System.in);
            buffer=scanner.nextLine();
            System.out.println(buffer);
        }
    }

    /**
     * Identify and return a single token in the buffer
     * @return value token: an object of @Token class
     */
    private Token getNextToken() {
        Token token;
        char ch = buffer.charAt(index);

        if (Character.isLetter(ch)) {
            token = getIdentifier();
        }
        else if (Character.isDigit(ch)) {
            token = getInteger();
        }
        else if (ch == '=') {
            token = new Token(ASSMTTOKEN, "=", lineNum);
            index++;
        }
        else if (ch == '+') {
            token = new Token(PLUSTOKEN, "+", lineNum);
            index++;
        }
        else {
            token = new Token(UNKNOWN, Character.toString(ch), lineNum);
            index++;
        }
        return token;
    }

    /**
     * A helper method called by getNextToken, when the char at index is a letter
     * @return an ID token
     */
    private Token getIdentifier() {
        int j = index;
        int len = buffer.length();
        while ( j < len && (Character.isLetter(buffer.charAt(j)) || Character.isDigit(buffer.charAt(j))) ) {
            j++;
        }
        String tokenVal = buffer.substring(index, j);
        Token token = new Token(IDTOKEN, tokenVal, lineNum);
        index = j;
        return token;
    }

    /**
     * A helper method called by getNextToken, when the char at index is a digit
     * @return an INT token
     */
    private Token getInteger() {
        int j = index;
        int len = buffer.length();
        while ( j < len && Character.isDigit(buffer.charAt(j))) {
            j++;
        }
        String tokenVal = buffer.substring(index, j);
        Token token = new Token(INTTOKEN, tokenVal, lineNum);
        index = j;
        return token;
    }

    /**
     * Return all the token in the file
     * @return ArrayList of Token
     */

    public ArrayList<Token> getAllTokens(){
        //TODO: place your code here for lexing file
        ArrayList<Token> tokenLst = new ArrayList<>();
        int len = buffer.length();
        while (index < len) {
            while (index < len && Character.isWhitespace(buffer.charAt(index))) {
                if (buffer.charAt(index) == '\n') {
                    lineNum++;
                }
                index++;
            }
            if (index < len) {
                Token token = getNextToken();
                tokenLst.add(token);
            }
        }
        tokenLst.add(new Token(EOFTOKEN, "-", lineNum));
        return tokenLst;
    }



    /**
     * Before your run this starter code
     * Select Run | Edit Configurations from the main menu.
     * In Program arguments add the name of file you want to test (e.g., test.txt)
     * @param args args[0]
     */
    public static void main(String[] args) {
        String fileName="";
        if (args.length==0) {
            System.out.println("You can test a different file by adding as an argument");
            System.out.println("See comment above main");
            System.out.println("For this run, test.txt used");
            fileName="test1.txt";
        } else {

            fileName=args[0];
        }
        Lexer lexer = new Lexer(fileName);
        // just print out the text from the file
        System.out.println(lexer.buffer);
        // here is where you'll call getAllTokens
        System.out.println(lexer.getAllTokens());
    }
}
	