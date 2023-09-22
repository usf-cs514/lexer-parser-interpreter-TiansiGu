import java.util.HashMap;

/**
 * A class tracks identifiers' and stores address
 */
public class IdTable {

    /**
     * Date Members
     */
    private HashMap<String, Integer> table = new HashMap<>(); // keys-identifiers; values-the address in memory where the identifier will be stored
    private int currentAddress = 0;

    /**
     * Add an identifier to the hashmap table
     * @param token
     */
    public void add(Token token) {
        table.put(token.getValue(), currentAddress++);
    }

    /**
     * Get the address associated with an id, or -1 if not found
     * @param token the Token that we want to look up its address
     * @return the address in which an id will be stored
     */
    public int getAddress(Token token) {
        int address = table.getOrDefault(token.getValue(), -1);
        return address;
    }

    /**
     * toString method overrides, prints out id table in a sequential format (like:{xyz=0, zz12=1})
     * @return
     */
    public String toString() {
        String str = "{";
        int size = table.size();
        int count = 0;
        for (HashMap.Entry<String, Integer> entry: table.entrySet()) {
            count++;
            String id = entry.getKey();
            int add = entry.getValue();
            str += id + "=" + add;
            if (count < size) {
                str += ", ";
            }
        }
        str += "}";
        return str;
    }

}
