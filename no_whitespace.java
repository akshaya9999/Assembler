import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.FileWriter;

public class no_whitespace
{
        public static void main(String[] args) {
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
}

    
