import java.io.*;
import java.util.*;
/**
 * Trying to get java to read in arrays from a data file, using commas as delimiters
 */
public class printFile
{

    String folderName = "planet1";
    String dataFileName = "data";
    String mapFileName = "map";
    String code = "UTF-8";
    int mapWidth;
    int mapHeight;
    int[][] worldMap;

    public void printFile(String[] args) throws IOException
    {        
        Scanner parse = new Scanner(new File(folderName+"/"+mapFileName));
        while(parse.hasNextLine())
        {
            System.out.println(parse.nextLine());
        }
        
    }

    public void readData()
    {

    }

    public void readMap(int width, int height)
    {

    }

}
