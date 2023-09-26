import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that generate SIMPLE bytecode and interpret bytecode,
 * modifying memory which is represented with an array of Integers
 * Class methods "generate" and "run" are called in Parser's main
 * @author Tiansi Gu
 * @see Parser
 */
public class ByteCodeInterpreter {
    /**
     * Data Members
     */
    private ArrayList<Integer> bytecode = new ArrayList<>(); // bytecode generated from token list
    private ArrayList<Integer> memory; // main activation memory array
    private int accumulator;
    private int mmSize;
    private boolean isEnoughMemory = true; // will be set to false when an address outside of memory is referenced.

    //constants for the 3 Bytecode commands
    public static final int LOAD = 0;
    public static final int LOADI = 1;
    public static final int STORE = 2;

    /**
     * Constructor
     * @param mmSize specify the size of memory
     */
    public ByteCodeInterpreter(int mmSize) {
        this.mmSize = mmSize;
        this.accumulator = 0;
        memory = new ArrayList<>(mmSize);
    }

    /**
     * Adds the command and operand to the bytecode being generated
     * @param command  LOAD, LOADI, or STORE
     * @param operand  ID's Address, INT, or the value of accumulator
     */
    public void generate(int command, int operand) {
        bytecode.add(command);
        bytecode.add(operand);
    }

    /**
     * Interpret the code in bytecode, modifying memory
     */
    public void run() {
        int idx = 1;
        int usedmemory = 0;
        while (isEnoughMemory && idx < bytecode.size()) {
            int commandCode = bytecode.get(idx - 1);
            int operandCode = bytecode.get(idx);
            if (commandCode == LOAD) {
                runLOAD(operandCode);
            } else if (commandCode == LOADI) {
                runLOADI(operandCode);
            } else {
                runSTORE();
                usedmemory++;
            }
            idx += 2; //iterate in pairs
        }
//        for (int i = usedmemory; i < mmSize; i++) {
//            memory.add(0);
//        }
    }

    /**
     * Helper method for LOAD command
     * @param address
     */
    private void runLOAD(int address) {
        if (address >= mmSize) {
            System.out.println("Run-time error: Address out of bounds");
            isEnoughMemory = false;
            return;
        }
        int idValue = memory.get(address);
        accumulator += idValue;
    }

    /**
     * Helper method for LOADI command
     * @param number
     */
    private void runLOADI(int number) {
        accumulator += number;
    }

    /**
     * Helper method for STORE command
     */
    private void runSTORE() {
        if (memory.size() >= mmSize) {
            System.out.println("Run-time error: Address out of range");
            isEnoughMemory = false;
            return;
        }
        memory.add(accumulator);
        accumulator = 0;
    }

    /**
     * Providing public access to isEnoughMemory
     */
    public boolean getValid() {
        return isEnoughMemory;
    }


    /**
     * toString method overrides, @return bytecode and memory
     */
    public String toString() {
        String byteStr = "ByteCode:" + bytecode.toString();
        String mmStr = "Memory:" + memory.toString();
        return byteStr + "\n" + mmStr;
    }

}
