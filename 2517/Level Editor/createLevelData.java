import java.io.*;
import java.util.*;
/**
 * First Attempt at making a folder with java
 * 
 * The program will now create a folder, create a file inside that folder, and write to it
 */
public class createLevelData
{
    // instance variables - replace the example below with your own

    String folderName = "testFolder";
    String fileName = "testFile";
    String code = "UTF-8";
    
    

    public createLevelData()
    {
        makeFolder();
        makeFile();
    }

    public void makeFile()
    {
        try{
            PrintWriter writer = new PrintWriter( folderName+"/"+ fileName,code);
            writer.println("Somebody once told me the world is gonna roll me");
            writer.println("I ain't the sharpest tool in the shed");
            writer.close();
        } catch(IOException e){}
    }

    public void makeFolder()
    {
        File dir = new File(folderName);
        dir.mkdir();
    }
}
