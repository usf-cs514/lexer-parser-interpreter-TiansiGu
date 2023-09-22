import java.util.HashMap;

public class IdTable {

    private HashMap<String, Integer> table = new HashMap<>();
    private int currentAddress = 0;

    public void add(Token token) {
        table.put(token.getValue(), currentAddress++);
    }

    public int getAddress(Token token) {
        int address = table.getOrDefault(token.getValue(), -1);
        return address;
    }

//    public int getIdValue(int address) {
//        int idValue = 0;
//        for (HashMap.Entry<String, Integer> entry: table.entrySet()) {
//            int add = entry.getValue();
//            if (add == address) {
//                Token id = entry.getKey();
//                idValue = Integer.valueOf(entry.getKey());
//                break;
//            }
//        }
//        return idValue;
//    }

    public String toString() {
        String str = "Symbol Table: {";
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
