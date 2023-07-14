import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
public class vm {    
    public static final String INPUT_FILE_PATH = "StaticTest.vm";
    public static final String OUTPUT_FILE_PATH = "vmORIGINAL.asm";
    static File inputFile = new File("StaticTest.vm");
    // public static String popsegment;
    //main function
    public static void main(String[] args) {
        try {
            String[] lines = readFromFile(INPUT_FILE_PATH);
            String assemblypop = translate(lines);
            writeToFile(assemblypop, OUTPUT_FILE_PATH);
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
    

    //writing to file
    public static void writeToFile(String content, String filePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write(content);
        writer.close();
    }
    


//static hashmap
public LinkedHashMap<String, String> StaticMap(File vmfile) {
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


   
    //this is for static variables for pop
    public static String StaticValuepop(String PointerValue, File vmfile) {
        vm ob=new vm();
        LinkedHashMap<String, String> staticmap = new LinkedHashMap<String, String>();
        staticmap = ob.StaticMap(vmfile);
        String addr = staticmap.get(PointerValue);
        String output = "//SP--\n@SP\nM=M-1\nA=M\nD=M\n@" + addr+"\nA=M\nM=D";
        return output;
    }


    //pop function
    public static String pop(String segment, int value, File vmfile) {
        StringBuilder assemblypop = new StringBuilder();
        
        assemblypop.append("// pop ").append(segment).append(" ").append(value).append("\n");
        
        if (segment.equals("constant")) {
            assemblypop.append("@").append(value).append("\n");
            assemblypop.append("D=A\n");
        }
        else if(segment.equals("local")){
            assemblypop.append("@"+"LCL"+"\nD=M\n@"+value+"\nD=D+A\n@addr\nM=D\n@SP\nM=M-1\nA=M\nD=M\n@addr\nM=D");
        }
        else if(segment.equals("temp")){
            assemblypop.append("@5"+"\nD=A\n@"+value+"\nD=D+A\n@addr\nM=D\n@SP\nM=M-1\nA=M\nD=M\n@addr\nM=D");
        }
        else if(segment.equals("this")){
            assemblypop.append("@"+"THIS"+"\nD=M\n@"+value+"\nD=D+A\n@addr\nM=D\n@SP\nM=M-1\nA=M\nD=M\n@addr\nM=D");
        }
        else if(segment.equals("that")){
            assemblypop.append("@"+"THAT"+"\nD=M\n@"+value+"\nD=D+A\n@addr\nM=D\n@SP\nM=M-1\nA=M\nD=M\n@addr\nM=D");
        }
        else if(segment.equals("argument")){
            assemblypop.append("@"+"ARG"+"\nD=M\n@"+value+"\nD=D+A\n@addr\nM=D\n@SP\nM=M-1\nA=M\nD=M\n@addr\nM=D");
        }        
        else if(segment.equals("pointer")){
                if (value == 0) {
                    assemblypop.append("@SP\\n" + //
                            "M=M-1\\n" + //
                            "A=M\\n" + //
                            "D=M\\n" + //
                            "@THIS\\n" + //
                            "M=D");
                } else if (value == 1) {
                    assemblypop.append("@SP\\n" + //
                            "M=M-1\\n" + //
                            "A=M\\n" + //
                            "D=M\\n" + //
                            "@THAT\\n" + //
                            "M=D");
                } else {
                    throw new IllegalArgumentException("Invalid pointer index: " + value);
                }
        }
        else{
            if (segment.equals("static")){

                assemblypop.append(StaticValuepop(Integer.toString(value),inputFile));
            }
            
        }

        return assemblypop.toString();
    }

    //this is for the push static variable part
    public static String StaticValuepush(String PointerValue, File vmfile) {
        vm obj = new vm();
        LinkedHashMap<String, String> staticmap = new LinkedHashMap<String, String>();
        staticmap = obj.StaticMap(vmfile);
        String addr = staticmap.get(PointerValue);
        String output = "@" + addr + "\nD=M\n@SP\nA=M\nM=D\n//SP++\n@SP\nM=M+1";
        return output;
    }

    //push function
    public static String push(String segment, int value) {
        StringBuilder assemblypush = new StringBuilder();
        
        assemblypush.append("// push ").append(segment).append(" ").append(value).append("\n");
        
        if (segment.equals("constant")) {
            assemblypush.append("@").append(value).append("\n");
            assemblypush.append("D=A\n");
        }
        else if(segment.equals("local")){
            assemblypush.append("@" + "LCL" + "\nD=M\n@" + value +"\nD=D+A\n@addr\nM=D\nA=M\nD=M\n@SP\nA=M\nM=D" + "\n@SP\nM=M+1");
        }
        else if(segment.equals("temp")){
            assemblypush.append( "@5" + "\nD=A\n@" + value +"\nD=D+A\n@addr\nM=D\nA=M\nD=M\n@SP\nA=M\nM=D" + "\n@SP\nM=M+1");
        }
        else if(segment.equals("this")){
            assemblypush.append("@" + "THIS" + "\nD=M\n@" + value +"\nD=D+A\n@addr\nM=D\nA=M\nD=M\n@SP\nA=M\nM=D" + "\n@SP\nM=M+1");
        }
        else if(segment.equals("that")){
            assemblypush.append("@" + "THAT" + "\nD=M\n@" + value +"\nD=D+A\n@addr\nM=D\nA=M\nD=M\n@SP\nA=M\nM=D" + "\n@SP\nM=M+1");
        }
        else if(segment.equals("argument")){
            assemblypush.append("@" + "ARG" + "\nD=M\n@" + value +"\nD=D+A\n@addr\nM=D\nA=M\nD=M\n@SP\nA=M\nM=D" + "\n@SP\nM=M+1");
        }        
        else if(segment.equals("pointer")){
                if (value == 0) {
                    assemblypush.append("@THIS\nD=A\n@SP\nA=M\nM=D\n@SP\nM=M+1");
                } else if (value == 1) {
                    assemblypush.append("@THAT\nD=A\n@SP\nA=M\nM=D\n@SP\nM=M+1");
                } else {
                    throw new IllegalArgumentException("Invalid pointer index: " + value);
                }
        }
        else{
            if (segment.equals("static")){
                assemblypush.append(StaticValuepush(Integer.toString(value),inputFile));
            }
            
        }

        return assemblypush.toString();
    }






     //translator function
    public static String translate(String[] lines) {
        StringBuilder assemblypop = new StringBuilder();
        // int labelCounter = 0;
        
        for (String line : lines) {
            line = line.trim();
            
            if (line.isEmpty() || line.startsWith("//")) {
                continue; // Skip empty lines and comments
            }
            
            String[] parts = line.split("\\s+");
            String command = parts[0];
            
            if (command.equals("push")) {
                String pushsegment = parts[1];
                int value = Integer.parseInt(parts[2]);
                assemblypop.append(push(pushsegment, value)).append("\n");
            } else if (command.equals("pop") ) {
                String popsegment = parts[1];
                int value = Integer.parseInt(parts[2]);
                assemblypop.append(pop(popsegment, value,inputFile)).append("\n");
            } else if(command.equals("goto")){
                String labelname=parts[1];
                String output="@"+labelname+"\n0;JMP";
                assemblypop.append(output).append("\n");
            } else if(command.equals("label")){
                String labelname=parts[1];
                String output="("+labelname+")";
                assemblypop.append(output).append("\n");
        }
    }
        return assemblypop.toString();
    }
 
}