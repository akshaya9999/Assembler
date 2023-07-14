import java.io.*;
import java.util.*;

public class cinstruction {
    private static final Map<String, String> symbolTable = new HashMap<>();
    private static final Map<String, String> comp = new HashMap<>();
    private static final Map<String, String> dest = new HashMap<>();
    private static final Map<String, String> jump = new HashMap<>();

    static {
        symbolTable.put("R0", "0");
        symbolTable.put("R1", "1");
        symbolTable.put("R2", "2");
        symbolTable.put("R3", "3");
        symbolTable.put("R4", "4");
        symbolTable.put("R5", "5");
        symbolTable.put("R6", "6");
        symbolTable.put("R7", "7");
        symbolTable.put("R8", "8");
        symbolTable.put("R9", "9");
        symbolTable.put("R10", "10");
        symbolTable.put("R11", "11");
        symbolTable.put("R12", "12");
        symbolTable.put("R13", "13");
        symbolTable.put("R14", "14");
        symbolTable.put("R15", "15");
        symbolTable.put("SP", "0");
        symbolTable.put("LCL", "1");
        symbolTable.put("ARG", "2");
        symbolTable.put("THIS", "3");
        symbolTable.put("THAT", "4");
        symbolTable.put("SCREEN", "16384");
        symbolTable.put("KBD", "24576");

        comp.put("0", "0101010");
        comp.put("1", "0111111");
        comp.put("-1", "0111010");
        comp.put("D", "0001100");
        comp.put("A", "0110000");
        comp.put("!D", "0001101");
        comp.put("!A", "0110001");
        comp.put("-D", "0001111");
        comp.put("-A", "0110011");
        comp.put("D+1", "0011111");
        comp.put("A+1", "0110111");
        comp.put("D-1", "0001110");
        comp.put("A-1", "0110010");
        comp.put("D+A", "0000010");
        comp.put("D-A", "0010011");
        comp.put("A-D", "0000111");
        comp.put("D&A", "0000000");
        comp.put("D|A", "0010101");

        comp.put("M", "1110000");
        comp.put("!M", "1110001");
        comp.put("-M", "1110011");
        comp.put("M+1", "1110111");
        comp.put("M-1", "1110010");
        comp.put("D+M", "1000010");
        comp.put("D-M", "1010011");
        comp.put("M-D", "1000111");
        comp.put("D&M", "1000000");
        comp.put("D|M", "1010101");

        dest.put("null", "000");
        dest.put("M", "001");
        dest.put("D", "010");
        dest.put("MD", "011");
        dest.put("A", "100");
        dest.put("AM", "101");
        dest.put("AD", "110");
        dest.put("AMD", "111");

        jump.put("null", "000");
        jump.put("JGT", "001");
        jump.put("JEQ", "010");
        jump.put("JGE", "011");
        jump.put("JLT", "100");
        jump.put("JNE", "101");
        jump.put("JLE", "110");
        jump.put("JMP", "111");
    }

    public static void main(String[] args) {
        String inputFile = "AssemblyCode.asm";
        String outputFile = "Assembler.Hack";

        try {
            firstPass(inputFile);
            secondPass(inputFile, outputFile);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }

        System.out.println("Output file: " + outputFile);
    }

    private static void firstPass(String inputFile) throws IOException {
        int ln= 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = clearline(line);
                if (line.isEmpty()) {
                    continue;
                    }

                if (line.startsWith("(")) {
                    String symbol = line.substring(1, line.length() - 1);
                    symbolTable.put(symbol, String.valueOf(ln));
                } else {
                    ln++;
                }
            }
        }
    }

    private static void secondPass(String inputFile, String outputFile) throws IOException {
        int n = 16;
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
             String line;
             while ((line = reader.readLine()) != null) {
                line = clearline(line);
                if (line.isEmpty() || line.startsWith("(")) {
                    continue;
                }

                if (line.startsWith("@")) {
                    String symbol = line.substring(1);
                    String value;
                    if (Character.isDigit(symbol.charAt(0))) {
                        value = symbol;
                        } 
                    else {
                        if (!symbolTable.containsKey(symbol)) {
                            symbolTable.put(symbol, String.valueOf(n));
                            n++;
                        }
                        value = symbolTable.get(symbol);
                        }
                    String binaryValue = toBinary(value);
                    writer.write(binaryValue);
                    writer.newLine();
                } else {
                    String binaryInstruction = "111";
                    String compCode = comp(line);
                    String destCode = dest(line);
                    String jumpCode = jump(line);
                    binaryInstruction += compCode + destCode + jumpCode;
                    writer.write(binaryInstruction);
                    writer.newLine();
                }
            }
        }
    }

    private static String clearline(String line) {
        line=line.replaceAll(" ", "");
        int Index = line.indexOf("/");
        if (Index != -1) {
            line = line.substring(0,Index);
        }
        return line;
    }

    private static String toBinary(String value) {
        int intValue = Integer.parseInt(value);
        String binaryValue = Integer.toBinaryString(intValue);
        return String.format("%16s", binaryValue).replace(' ', '0');
    }

    private static String comp(String line) {
        int compIndex = line.indexOf("=");
        int jumpIndex = line.indexOf(";");
        if (compIndex != -1) {
            String compval = line.substring(compIndex + 1);
            return comp.get(compval);
        } else if (jumpIndex != -1) {
            String compval = line.substring(0, jumpIndex);
            return comp.get(compval);
        }
        return null;
    }

    private static String dest(String line) {
        int destIndex = line.indexOf("=");
        if (destIndex != -1) {
            String destval = line.substring(0, destIndex);
            return dest.get(destval);
        }
        return dest.get("null");
    }

    private static String jump(String line) {
        int jumpIndex = line.indexOf(";");
        if (jumpIndex != -1) {
            String jumpval = line.substring(jumpIndex + 1);
            return jump.get(jumpval);
        }
        return jump.get("null");
    }
}