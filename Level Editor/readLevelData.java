import java.io.*;
import java.util.*;
/**
 * Trying to get java to read in arrays from a data file, using commas as delimiters
 */
public class readLevelData
{

    String folderName = "planet1";
    String dataFileName = "data";
    String mapFileName = "map";
    String map1Name = "map1";
    String map2Name = "map2";
    String code = "UTF-8";
    int mapWidth;
    int mapHeight;
    int[][] worldMap;

    public void readLevelData(String[] args)
    {        

        mapWidth = 0;
        mapHeight = 0;
        readData();
        worldMap = readMap(map2Name,mapWidth,mapHeight);
        //System.out.println(mapWidth);
        //System.out.println(mapHeight);
        
        for(int c = 0; c< mapWidth; c++)
        {
            for(int r = 0; r< mapHeight; r++)
            {
                System.out.print(worldMap[r][c] + ",");
            }
            System.out.print("\n");
        }        
        
    }

    public void readData()
    {
        try{
            File data = new File(folderName+"/"+dataFileName);
            Scanner parse = new Scanner(data);
            mapWidth = parse.nextInt();
            mapHeight = parse.nextInt();
        } catch(IOException e){}
    }

    public int[][] readMap(String name, int width, int height)
    {
        int[][] map = new int[width][height];
        try{
            Scanner parse = new Scanner(new File(folderName+"/"+name));
            parse.useDelimiter("\\s*,\\s*");
            for(int c = 0; c< width; c++)
            {
                for(int r = 0; r< height; r++)
                {
                    if(parse.hasNextInt())
                    {
                        map[r][c] = parse.nextInt();
                    }                    
                }
            }
        } catch(IOException e){}
        return map; 
    }

}
