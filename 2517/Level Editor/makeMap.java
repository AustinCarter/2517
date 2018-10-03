import java.io.*;
import java.util.*;
/**
 * Makes a text file from an array and places it in a folder
 */
public class makeMap
{
    // instance variables - replace the example below with your own

    String folderName = "planet1";
    String fileName = "map2";
    String code = "UTF-8";

    int mapWidth = 10;
    int mapHeight = 10;
    int[][] map = {
            {1,2,3,4,1,2,3,4,6,7},
            {1,2,3,4,1,2,3,4,6,7},
            {1,2,3,4,1,2,3,4,6,7},
            {1,2,3,4,1,2,3,4,6,7},
            {1,2,3,4,1,2,3,4,6,7},
            {1,2,3,4,1,2,3,4,6,7},
            {1,2,3,4,1,2,3,4,6,7},
            {1,2,3,4,1,2,3,4,6,7},
            {1,2,3,4,1,2,3,4,6,7},
            {1,2,3,4,1,2,3,4,6,7},
        };

    public makeMap()
    {
        makeFile();
    }

    public void makeFile()
    {
        try{
            PrintWriter writer = new PrintWriter( folderName+"/"+ fileName,code);
            for(int c = 0; c< mapWidth; c++)
            {
                for(int r = 0; r< mapHeight; r++)
                {
                    writer.print(map[r][c] + ",");
                }
                writer.print("\n");
            }
            writer.close();
        } catch(IOException e){}
    }

}
