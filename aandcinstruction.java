import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class aandcinstruction {
    
    public static void main(String args[]){
        ArrayList<String> x=ReadFile("/home/akshaya/Documents/assembler/white_space_removed.asm");
        givetoaorc(x);
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

    private static void givetoaorc(ArrayList<String> x){
        for (String line:x){
            char firstelement=line.charAt(0);
            if (firstelement=='@'){
                ainstruction(line.substring(1,line.length()));

            // }
            // else{

            // }
        }
    
    }

    }
    private static String ainstruction(String k){
        
        return k;

    }

    
}
