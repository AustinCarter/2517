import java.io.*;
import java.util.*;
/**
 * First Attempt at making a folder with java
 * 
 * The program will now create a folder, create a file inside that folder, and write to it
 */
public class makeTestLevel
{
    // instance variables - replace the example below with your own

    String folderName = "level1";
    String fileName1 = "data";
    String fileName2 = "floor";
    String fileName3 = "ground";
    String fileName4 = "ceiling";
    String code = "UTF-8";
    int levelWidth = 30;
    
    

    public makeTestLevel()
    {
        makeFolder();
        makeFile();
    }

    public void makeFile()
    {
        try{
            PrintWriter writer = new PrintWriter( folderName+"/"+ fileName1,code);
            writer.println(levelWidth);
            writer.println(levelWidth);
            writer.close();
            PrintWriter writer2 = new PrintWriter( folderName+"/"+ fileName2,code);
            writer2.close();
            PrintWriter writer3 = new PrintWriter( folderName+"/"+ fileName3,code);
            writer3.close();
            PrintWriter writer4 = new PrintWriter( folderName+"/"+ fileName4,code);
            writer4.close();
        } catch(IOException e){}
    }

    public void makeFolder()
    {
        File dir = new File(folderName);
        dir.mkdir();
    }
}
