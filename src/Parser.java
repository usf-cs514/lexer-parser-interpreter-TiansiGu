import com.sun.management.HotSpotDiagnosticMXBean;

import java.util.*;

/**
 * Class to check and determine if the statements are valid, and displays an error if not.
 * @author Tiansi Gu
 * @see Lexer
 * @see Token
 * @see IdTable
 */

public class Parser {

    // Data Members
    private ArrayList<Token> tokenList; // the tokenList generated from “getAllTokens”
    private IdTable idTable = new IdTable(); //an IdTable object of the arguments that we are parsing, see class IdTable

    private int index = 0; //index of Tokens when looping through the tokenList

    private boolean valid; //if the statement(s) is/are valid so far

    private ByteCodeInterpreter interpreter; //an object of class ByteCodeInterpreter, used to generate bytecode of the arguments and interpret them

    /**
     * Constructor
     * Create a Lexer and places the results of “getAllTokens” into the @data member tokenList
     * Create a ByteCodeInterpreter with a limited memory 10
     * @param fileName the file we open
     */
    public Parser(String fileName) {
        Lexer lexer = new Lexer(fileName);
        this.tokenList = lexer.getAllTokens();
        this.valid = true;
        this.interpreter = new ByteCodeInterpreter(10);
    }

    /**
     * Parse the entire program, call parseAssignment within a loop
     */
    public void parseProgram() {
        while (index < tokenList.size() && this.valid) {
            parseAssignment();
        }
        if (this.valid && interpreter.getValid()) {
            System.out.println("Valid Program");
        } else {
            System.out.println("Invalid Program");
        }
    }

    /**
     * Parse a single assignment, it calls parseId, parseAssignmentOp, and parseExpression in descending order
     * Generate bytecode of the assignment at the same time
     */
    private void parseAssignment() {
        if (!valid) return;
        Token idToken = parseId();
        if (!valid) return;
        parseAssignOp();
        if (!valid) return;
        parseExpression();
        // Generate STORE bytecode, store value in Accumulator to the LHS's identifier
        if (valid) {
            int idAddress = idTable.getAddress(idToken);
            interpreter.generate(ByteCodeInterpreter.STORE, idAddress);
        }
    }

    /**
     * A helper function called in parseAssignment
     * Parses a single identifier, displays an error if it is not an identifier
     * @return token the token on the LHS
     */
    private Token parseId() {
        Token token = nextToken();
        if (token.getType().equals(Lexer.IDTOKEN)) {
            //System.out.print("get an id");
            if (idTable.getAddress(token) == -1) {
                idTable.add(token);
            }
            //System.out.println(table.getAddress(token));
        }
        else {
            valid = false;
            System.out.println("Error: Expecting identifier, line " + token.getLineNumber());
        }
        return token;
    }

    /**
     * A helper function called in parseAssignment
     * Parses a single assignment operator, displays an error if it is not an assignment operator
     */
    private void parseAssignOp() {
        Token token = nextToken();
        if (! token.getType().equals(Lexer.ASSMTTOKEN)) {
            valid = false;
            System.out.println("Error: Expecting assignment operator, line " + token.getLineNumber());
        }
    }

    /**
     * A helper function called in parseAssignment
     * Parses an expression (RHS), displays an error if it is an error
     */
    private void parseExpression() {
        Token t1 = nextToken();
        Token t2 = nextToken();
        while ((t1.getType().equals(Lexer.IDTOKEN) || t1.getType().equals(Lexer.INTTOKEN))){
            if (t1.getType().equals(Lexer.IDTOKEN) && idTable.getAddress(t1) == -1) {
                valid = false;
                System.out.println("Error: Identifier " + t1.getValue() + " not defined, line " + t1.getLineNumber());
                return;
            }

            // generate bytecode using "generate" in Class ByteCodeInterpreter
            if (t1.getType().equals(Lexer.IDTOKEN)) {
                int value = idTable.getAddress(t1);
                interpreter.generate(ByteCodeInterpreter.LOAD, value);
            }
            if (t1.getType().equals(Lexer.INTTOKEN)) {
                int value = Integer.valueOf(t1.getValue());
                interpreter.generate(ByteCodeInterpreter.LOADI, value);
            }

            if (t2.getType().equals(Lexer.PLUSTOKEN)) {
                t1 = nextToken();
                t2 = nextToken();
            } else{
                break;
            }
        }

        if ( ! t1.getType().equals(Lexer.IDTOKEN) && ! t1.getType().equals(Lexer.INTTOKEN) ) {
            valid = false;
            System.out.println("Error: Expecting identifier or integer, line " + t1.getLineNumber());
            return;
        }
        if (t2.getType().equals(Lexer.IDTOKEN)) {
            index -= 1;
            return;
        }

        if (! t2.getType().equals(Lexer.EOFTOKEN)) {
            valid = false;
            System.out.println("Error: Expecting identifier or add operator, line " + t2.getLineNumber());
        }
    }

    /**
     * Get the next token in "tokenList" and increments the index
     */
    private Token nextToken() {
        if (index < tokenList.size()) {
            return tokenList.get(index++);
        }
        return null;
    }

    /**
     * @override print out the tokenList and id table
     */
    public String toString() {
        String tokenListPrint = "Token List:" + tokenList.toString();
        String idTablePrint = "Symbol Table:" + idTable.toString();
        return tokenListPrint + "\n" + idTablePrint;
    }

    /**
     * The Entry of the entire project.
     * We get tokens, parse them, generate bytecodes, and finally interpret the assignments
     * @param args
     */
    public static void main(String[] args) {
        Parser parser = new Parser("testOutOfBounds.txt");
        parser.parseProgram();
        //System.out.println(parser);
        if (parser.valid) {
            parser.interpreter.run();
            System.out.println(parser);
            System.out.println(parser.interpreter);
        }
    }

}
