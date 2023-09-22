import java.util.ArrayList;
import java.util.HashMap;

public class ByteCodeInterpreter {
    private ArrayList<Integer> bytecode = new ArrayList<>();
    private ArrayList<Integer> memory;
    private int accumulator;
    private int mmSize;
    private boolean isEnoughMemory = true;
    //constants for the Bytecode commands
    public static final int LOAD = 0;
    public static final int LOADI = 1;
    public static final int STORE = 2;

    public ByteCodeInterpreter(int mmSize) {
        this.mmSize = mmSize;
        this.accumulator = 0;
        memory = new ArrayList<>(mmSize);
    }

    public void generate(int command, int operand) {
        bytecode.add(command);
        bytecode.add(operand);
    }

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
            idx += 2;
        }
        for (int i = usedmemory; i < mmSize; i++) {
            memory.add(0);
        }
    }

    private void runLOAD(int address) {
        if (address >= mmSize) {
            System.out.println("Run-time error: Address out of bounds");
            isEnoughMemory = false;
            return;
        }
        int idValue = memory.get(address);
        accumulator += idValue;
    }

    private void runLOADI(int number) {
        accumulator += number;
    }

    private void runSTORE() {
        if (memory.size() >= mmSize) {
            System.out.println("Run-time error: Address out of bounds");
            isEnoughMemory = false;
            return;
        }
        memory.add(accumulator);
        accumulator = 0;
    }

    public boolean getValid() {
        return isEnoughMemory;
    }

    public String toString() {
        String byteStr = "ByteCode:" + bytecode.toString();
        String mmStr = "Memory:" + memory.toString();
        return byteStr + "\n" + mmStr;
    }

}
