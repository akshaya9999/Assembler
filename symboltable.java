import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class symboltable {
    public static void main(String args[])throws IOException{
        LinkedHashMap<String, Integer> x;
        LinkedHashMap<String, Integer> y ;
        x=predefined();
        y=symbolfile(x);
        writetofile(y);
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
            int count=16;
            while ((line = reader.readLine()) != null) {
                    if(!z.containsKey(line) && line.startsWith("@")){
                        z.put(line.substring(1,line.length()),count);
                        count+=1;
                    }    
                    if(!z.containsKey(line) && line.startsWith("(")){
                        z.put(line.substring(1,line.length()-1),j+1);
                    }
                    j+=1;

            }
            }
            
             catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return z;
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
   
    
}
