import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.FileWriter;

// demo code

public class assemblerfinal {

        public static void main(String[] args) throws IOException {
            nowhitespace();//remove white spaces
            
            LinkedHashMap<String, Integer> x;//symboltable
            LinkedHashMap<String, Integer> y ;
            x=predefined();
            y=symbolfile(x);
            writetofile(y);
            LinkedHashMap<String, String> z ;
            z=cinstructiontable();
            givetoaorc(y,z);//a and c instruction
        }

    private static void nowhitespace() {
        System.out.println("Enter file path: ");        
            Scanner scObj = new Scanner(System.in);
            String filePath = scObj.nextLine();
            ArrayList<String> x=ReadFile(filePath);
            WriteFile(x);
    }

    private static ArrayList<String> ReadFile(String filepath) {
        ArrayList<String> lines = new ArrayList<>();
        File file = new File(filepath);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
    private static void WriteFile(ArrayList<String> x){
        try{
            File file = new File("/home/akshaya/Documents/assembler/white_space_removed.asm");
            FileWriter myWriter = new FileWriter(file);
            for (String line : x) {
                String j=line.trim();
                if(j.startsWith("//")||j.isBlank()){
                    continue;
                }
                
                if(j.contains("//"))
                {
                    int k=j.indexOf("//");
                    String l=j.substring(0,k);
                    myWriter.write(l.replaceAll(" ", ""));
                    myWriter.write(System.lineSeparator());
                }
                else{
                myWriter.write(j.replaceAll(" ", ""));
                myWriter.write(System.lineSeparator());
     
                }
            }
                
            
            myWriter.close();
        }
    
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }
    private static LinkedHashMap<String,Integer> predefined(){
        LinkedHashMap<String,Integer> pre=new LinkedHashMap<String,Integer>();
        pre.put("R0",0);
        pre.put("R1",1);
        pre.put("R2",2);
        pre.put("R3",3);
        pre.put("R4",4);
        pre.put("R5",5);
        pre.put("R6",6);
        pre.put("R7",7);
        pre.put("R8",8);
        pre.put("R9",9);
        pre.put("R10",10);
        pre.put("R11",11);
        pre.put("R12",12);
        pre.put("R13",13);
        pre.put("R14",14);
        pre.put("R15",15);
        pre.put("SCREEN",16384);
        pre.put("KBD",24576);
        pre.put("SP",0);
        pre.put("LCL",1);
        pre.put("ARG",2);
        pre.put("THIS",3);
        pre.put("THAT",4);
        return pre;

    }
    
    private static LinkedHashMap<String,Integer> symbolfile(LinkedHashMap<String,Integer> z) throws IOException{
        try (BufferedReader reader = new BufferedReader(new FileReader("white_space_removed.asm"))) {
            String line;
            int j=1;
            int k=1;

            while ((line = reader.readLine()) != null) {   
                    if(line.startsWith("(")){
                    
                        z.put(line.substring(1,line.length()-1),j-k);
                        k+=1;
                    }
                    j+=1;
                    
            }
            reader.close();
            try (BufferedReader r = new BufferedReader(new FileReader("white_space_removed.asm"))) {
            String l;
            int count=16;
            while ((l = r.readLine()) != null) {
                if(!z.containsKey(l.substring(1,l.length())) && l.startsWith("@") && !Character.isDigit(l.charAt(1))){
                    z.put(l.substring(1,l.length()),count);
                    count+=1;
                }   
            }

            }
            
             catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return z;
    }
    }

    private static void writetofile(LinkedHashMap<String,Integer> u) throws IOException{
        File file = new File("/home/akshaya/Documents/assembler/symboltable.asm");
            FileWriter myWriter = new FileWriter(file);
            for(Map.Entry<String,Integer> hi:u.entrySet()){
                String k=hi.getKey();
                int v=hi.getValue();
                myWriter.write(k+" : "+v);
                myWriter.write(System.lineSeparator());
            }
            myWriter.close();


    }
    private static LinkedHashMap<String,String> cinstructiontable(){
            LinkedHashMap<String,String> cinsttable=new LinkedHashMap<String,String>();
            cinsttable.put("NULL", "000");
            cinsttable.put("M", "001");
            cinsttable.put("D", "010");
            cinsttable.put("MD", "011");
            cinsttable.put("DM", "011");
            cinsttable.put("A", "100");
            cinsttable.put("AM", "101");
            cinsttable.put("AD", "110");
            cinsttable.put("AMD", "111");
            cinsttable.put("0", "0101010");
            cinsttable.put("1", "0111111");
            cinsttable.put("-1", "0111010");
            cinsttable.put("D", "0001100");
            cinsttable.put("A", "0110000");
            cinsttable.put("M", "1110000");
            cinsttable.put("!D", "0001101");
            cinsttable.put("!A", "0110001");
            cinsttable.put("!M", "1110001");
            cinsttable.put("-D", "0001111");
            cinsttable.put("-A", "0110011");
            cinsttable.put("-M", "1110011");
            cinsttable.put("D+1", "0011111");
            cinsttable.put("A+1", "0110111");
            cinsttable.put("M+1", "1110111");
            cinsttable.put("D-1", "0001110");
            cinsttable.put("A-1", "0110010");
            cinsttable.put("M-1", "1110010");
            cinsttable.put("D+A", "0000010");
            cinsttable.put("D+M", "1000010");
            cinsttable.put("D-A", "0010011");
            cinsttable.put("D-M", "1010011");
            cinsttable.put("A-D", "0000111");
            cinsttable.put("M-D", "1000111");
            cinsttable.put("D&A", "0000000");
            cinsttable.put("D&M", "1000000");
            cinsttable.put("D|A", "0010101");
            cinsttable.put("D|M", "1010101");
            cinsttable.put("NULL", "000");
            cinsttable.put("JGT", "001");
            cinsttable.put("JEQ", "010");
            cinsttable.put("JGE", "011");
            cinsttable.put("JLT", "100");
            cinsttable.put("JNE", "101");
            cinsttable.put("JLE", "110");
            cinsttable.put("JMP", "111");
            return cinsttable;

    }
  
    
        
    private static void givetoaorc(LinkedHashMap<String, Integer> y,LinkedHashMap<String, String> z) throws IOException{
        ArrayList<String> x=ReadFile("/home/akshaya/Documents/assembler/white_space_removed.asm");
        File file = new File("/home/akshaya/Documents/assembler/finalasmfile.asm");
        FileWriter myWriter = new FileWriter(file);
        for (String line:x){
            char firstelement=line.charAt(0);
            if (firstelement=='@'){
                String k=line.substring(1,line.length());
                if(Character.isDigit(k.charAt(0))){
                    Long no=Long.parseLong(k);
                    String binaryno=Long.toBinaryString(no);
                    while(binaryno.length()<15 ){
                        binaryno="0"+binaryno;
                    }
                    myWriter.write("0"+binaryno);
                    myWriter.write(System.lineSeparator());

                }
                else{
                    Integer h=y.get(k);
                    String binaryno=Integer.toBinaryString(h);
                    while(binaryno.length()<15 ){
                        binaryno="0"+binaryno;
                    }
                    myWriter.write("0"+binaryno);
                    myWriter.write(System.lineSeparator());
                }
            
            }
            else{//c instruction
                List<String> a1 = Arrays.asList("M", "!M", "-M", "M+1", "M-1", "D+M", "D-M", "M-D", "D&M", "D|M");
            String toWrite = "";
            
            if (line.contains("=") && line.contains(";")) {
                String[] whole = line.split("=");
                String dest = whole[0];
                String[] withoutEqual = whole[1].split(";");
                String comp = withoutEqual[0];
                String jump = withoutEqual[1];

                if (a1.contains(comp)) {
                    toWrite = "1111" + z.get(comp);
                } else {
                    toWrite = "1110" + z.get(comp);
                }
                
                toWrite += z.get(dest);
                toWrite += z.get(jump);
                
            } else if (line.contains("=")) {
                String[] whole = line.split("=");
                String dest = whole[0];
                String comp = whole[1];
                
                if (a1.contains(comp)) {
                    toWrite = "1111" + z.get(comp);
                } else {
                    toWrite = "1110" + z.get(comp);
                }
                
                toWrite += z.get(dest);
                toWrite += "000";
            } else if (line.contains(";")) {
                String[] whole = line.split(";");
                String comp = whole[0];
                String jump = whole[1];
                
                if (a1.contains(comp)) {
                    toWrite = "1111" + z.get(comp);
                } else {
                    toWrite = "1110" + z.get(comp);
                }
                
                toWrite += "000";
                toWrite += z.get(jump);
            }
            
            myWriter.write(toWrite);
            myWriter.write(System.lineSeparator());
        }  
    
    
}

myWriter.close();

}

}