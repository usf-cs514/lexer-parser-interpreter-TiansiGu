/**
 * Class of tokens
 * type: record a token's type, @See Lexer
 * value: a token's value
 * lineNumber: which line the token is in
 */
public class Token {
    private String type;
    private String value;
    private int lineNumber;

    /**
     * Constructor
     * @param type
     * @param value
     * @param lineNumber
     */
    public Token(String type, String value, int lineNumber) {
        this.type=type;
        this.value=value;
        this.lineNumber = lineNumber;
    }

    // "get" method for token's type
    public String getType() {return type;}

    // "get" method for token's value
    public String getValue() {
        return value;
    }

    // "get" method for token's line number
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * toString method overrides
     * @return a token's type and value (lineNumber is hidden)
     */
    public String toString(){
        return type+" "+value;
    }

}
