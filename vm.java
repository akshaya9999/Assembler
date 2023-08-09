import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;

public class vm {
    public static final String input_filename = "StaticTest.vm";
    public static final String output_filename = "vmORIGINAL.asm";
    static File inputFile = new File(input_filename);

    public static void main(String[] args) {
        try {
            String[] lines = readFromFile(input_filename);  //method to get a lines as list from input file 
            String assemblyOutput = convertToAsm(lines);       //method to translate and convert to asm
            writeToFile(assemblyOutput, output_filename);      //method to write to output file
            System.out.println("Translation completed successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    public static String[] readFromFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line.trim()).append("\n");
        }
        reader.close();
        return content.toString().split("\n");
    }

    public static void writeToFile(String content, String filePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write(content);
        writer.close();
    }

    public LinkedHashMap<String, String> staticVars(File vmfile) {   //to get the static variables
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(vmfile));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("static") && !line.contains("//")) {
                    String pointerValue = line.split("static")[1].trim();
                    if (!map.containsKey(pointerValue)) {
                        map.put(pointerValue, Integer.toString(map.size() + 16));
                    }
                }
            }
            br.close();
            return map;
        } catch (Exception e) {
            System.out.println("ERROR");
            return map;
        }
    }

    public static String handleStaticPop(String pointerValue, File vmfile) {    //to handle the pop funtion of static variables
        vm ob = new vm();
        LinkedHashMap<String, String> staticMap = new LinkedHashMap<>();
        staticMap = ob.staticVars(vmfile);
        String addr = staticMap.get(pointerValue);
        String output = "//SP--\n@SP\nM=M-1\nA=M\nD=M\n@" + addr + "\nA=M\nM=D";
        return output;
    }

    public static String handlePop(String segment, int value, File vmfile) {    //to handle the pop function
        StringBuilder assemblyOutput = new StringBuilder();

        assemblyOutput.append("// pop ").append(segment).append(" ").append(value).append("\n");

        if (segment.equals("constant")) {
            assemblyOutput.append("@").append(value).append("\n");
            assemblyOutput.append("D=A\n");
        } 
        else if (segment.equals("local")) {
            assemblyOutput.append(
                                "@" + //
                                "LCL" + //
                                "\nD=M\n@" + //
                                value + //
                                "\nD=D+A\n@addr\nM=D\n@SP\nM=M-1\nA=M\nD=M\n@addr\nM=D"
                                );
        } 
        else if (segment.equals("temp")) {
            assemblyOutput.append(
                                "@5" + //
                                "\nD=A\n@" + //
                                value + //
                                "\nD=D+A\n@addr\nM=D\n@SP\nM=M-1\nA=M\nD=M\n@addr\nM=D"
                                );
        } 
        else if (segment.equals("this")) {
            assemblyOutput.append(
                                "@" + //
                                "THIS" + //
                                "\nD=M\n@" + //
                                value + //
                                "\nD=D+A\n@addr\nM=D\n@SP\nM=M-1\nA=M\nD=M\n@addr\nM=D"
                                );
        } 
        else if (segment.equals("that")) {
            assemblyOutput.append(
                                "@" + //
                                "THAT" + //
                                "\nD=M\n@" + //
                                value + //
                                "\nD=D+A\n@addr\nM=D\n@SP\nM=M-1\nA=M\nD=M\n@addr\nM=D"
                                );
        } 
        else if (segment.equals("argument")) {
            assemblyOutput
                    .append(    
                                "@" + //
                                "ARG" + //
                                "\nD=M\n@" + //
                                value + //
                                "\nD=D+A\n@addr\nM=D\n@SP\nM=M-1\nA=M\nD=M\n@addr\nM=D"
                                );
        } 
        else if (segment.equals("pointer")) {
            if (value == 0) {
                assemblyOutput.append(
                                "@SP\\n" + //
                                "M=M-1\\n" + //
                                "A=M\\n" + //
                                "D=M\\n" + //
                                "@THIS\\n" + //
                                "M=D");
            } 
            else if (value == 1) {
                assemblyOutput.append(
                                "@SP\\n" + //
                                "M=M-1\\n" + //
                                "A=M\\n" + //
                                "D=M\\n" + //
                                "@THAT\\n" + //
                                "M=D");
            } 
            else {
                throw new IllegalArgumentException("Invalid pointer index: " + value);
            }
        } 
        else {
            if (segment.equals("static")) {
                assemblyOutput.append(handleStaticPop(Integer.toString(value), inputFile));
            }
        }

        return assemblyOutput.toString();
    }

    public static String handleStaticPush(String pointerValue, File vmfile) {         //to handle push funtion of static variables
        vm obj = new vm();
        LinkedHashMap<String, String> staticMap = new LinkedHashMap<String, String>();
        staticMap = obj.staticVars(vmfile);
        String addr = staticMap.get(pointerValue);
        String output = "@" + addr + "\nD=M\n@SP\nA=M\nM=D\n//SP++\n@SP\nM=M+1";
        return output;
    }

    public static String handlePush(String segment, int value) {                      //to handle push funtions
        StringBuilder assemblyPush = new StringBuilder();

        assemblyPush.append("// push ").append(segment).append(" ").append(value).append("\n");

        if (segment.equals("constant")) {
            assemblyPush.append("@").append(value).append("\n");
            assemblyPush.append("D=A\n");
        } 
        else if (segment.equals("local")) {
            assemblyPush.append(
                                "@" + //
                                "LCL" + //
                                "\nD=M\n@" + //
                                value + //
                                "\nD=D+A\n@addr\nM=D\nA=M\nD=M\n@SP\nA=M\nM=D" + //
                                "\n@SP\nM=M+1"
                                );
        } 
        else if (segment.equals("temp")) {
            assemblyPush.append(
                                "@5" + //
                                "\nD=A\n@" + //
                                value + //
                                "\nD=D+A\n@addr\nM=D\nA=M\nD=M\n@SP\nA=M\nM=D" + //
                                "\n@SP\nM=M+1"
                                );
        } 
        else if (segment.equals("this")) {
            assemblyPush.append(
                                "@" + //
                                "THIS" + //
                                "\nD=M\n@" + //
                                value + //
                                "\nD=D+A\n@addr\nM=D\nA=M\nD=M\n@SP\nA=M\nM=D" + //
                                "\n@SP\nM=M+1"
                                );
        } 
        else if (segment.equals("that")) {
            assemblyPush.append(
                                "@" + //
                                "THAT" + //
                                "\nD=M\n@" + //
                                value + //
                                "\nD=D+A\n@addr\nM=D\nA=M\nD=M\n@SP\nA=M\nM=D"+ //
                                "\n@SP\nM=M+1"
                                );
        } 
        else if (segment.equals("argument")) {
            assemblyPush.append(
                                "@" + //
                                "ARG" + //
                                "\nD=M\n@" + //
                                value + //
                                "\nD=D+A\n@addr\nM=D\nA=M\nD=M\n@SP\nA=M\nM=D" + //
                                "\n@SP\nM=M+1"
                                );
        } 
        else if (segment.equals("pointer")) {
            
            if (value == 0) {
                assemblyPush.append("@THIS\nD=A\n@SP\nA=M\nM=D\n@SP\nM=M+1");
            } 
            else if (value == 1) {
                assemblyPush.append("@THAT\nD=A\n@SP\nA=M\nM=D\n@SP\nM=M+1");
            } 
            else {
                throw new IllegalArgumentException("Invalid pointer index: " + value);
            }
        } 
        else {
            if (segment.equals("static")) {
                assemblyPush.append(handleStaticPush(Integer.toString(value), inputFile));
            }
        }

        return assemblyPush.toString();
    }

    public static String convertToAsm(String[] lines) {          //main method that calls all the individual conversion methods
        StringBuilder assemblyOutput = new StringBuilder();

        for (String line : lines) {
            line = line.trim();

            if (line.isEmpty() || line.startsWith("//")) {
                continue; // Skip empty lines and comments
            }

            String[] parts = line.split("\\s+");
            String command = parts[0];

            if (command.equals("push")) {
                String pushSegment = parts[1];
                int value = Integer.parseInt(parts[2]);
                assemblyOutput.append(handlePush(pushSegment, value)).append("\n");

            } 
            else if (command.equals("pop")) {
                String popSegment = parts[1];
                int value = Integer.parseInt(parts[2]);
                assemblyOutput.append(handlePop(popSegment, value, inputFile)).append("\n");

            } 
            else if (command.equals("goto")) {
                String labelName = parts[1];
                String output = "@" + labelName + "\n0;JMP";
                assemblyOutput.append(output).append("\n");

            } 
            else if (command.equals("label")) {
                String labelName = parts[1];
                String output = "(" + labelName + ")";
                assemblyOutput.append(output).append("\n");
            }
        }
        return assemblyOutput.toString();
    }
}
