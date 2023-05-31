import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.FileWriter;

public class symbols{
    public static void main(String args[]){
        
        System.out.println("Enter file path: ");        
        Scanner scObj = new Scanner(System.in);
        String filePath = scObj.nextLine();
        ArrayList<String> x=ReadFile(filePath);
        WriteSymbols(x);
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
    private static void WriteSymbols(ArrayList<String> x){
        
        try{
            ArrayList<String> allsymbols = new ArrayList<>();
            File file = new File("/home/akshaya/Documents/assembler/symbolsandvariables.asm");
            FileWriter myWriter = new FileWriter(file);
            for (String line : x) {
                char st=line.charAt(0);
                String m="";
                if(st=='@'){
                m=line.substring(1,line.length());}
                else if(st=='('){
                m=line.substring(1,line.length()-1);
                }
                if((st=='@') || st=='('  ){
                    if(!allsymbols.contains(m)){
                    allsymbols.add(m);
                    myWriter.write(m);
                    myWriter.write(System.lineSeparator());
                    }
                }
             
            }
            for (String i : allsymbols) {
                System.out.println(i);
                
            }
            myWriter.close();
            
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }



    }
}