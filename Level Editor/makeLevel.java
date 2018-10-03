import java.io.*;
import java.util.*;
/**
 * Makes a text file from an array and places it in a folder
 */
public class makeLevel
{
    // instance variables - replace the example below with your own

    String levelsFolder = "levels/";
    String folderName;
    String dataFileName = "data";
    String floorFileName = "floor";
    String groundFileName = "ground";
    String ceilingFileName = "ceiling";
    String actionFileName = "actionPoints";
    String spriteFileName = "sprites";
    String npcFileName = "npcs";
    String code = "UTF-8";

    int defFloor;
    int defCeil;
    int defBord;
    boolean covered;
    boolean aggressive;
    int music;
    int skybox; 
    int playerX = 0;
    int playerY = 0;
    int spriteNum = 0;
    int actionPointNum = 0;
    //more code will be added for sprites and actionPoints

    int mapWidth;
    int mapHeight;
    int[][] floorMap;
    int[][] worldMap;
    int[][] ceilingMap;

    public makeLevel(String name, int width, int height,int music,int skybox, int defFloor, int defCeil, int defBord,boolean covered,boolean aggressive)
    {        
        this.folderName = new String(levelsFolder+name);
        this.mapWidth = width;
        this.mapHeight = height;
        this.defFloor = defFloor;
        this.defCeil = defCeil;
        this.defBord = defBord;
        this.covered = covered;
        this.aggressive = aggressive;
        this.music = music;
        this.skybox = skybox;
        makeFile();
    }

    public void makeFile()
    {
        try{
            File dir = new File(folderName); // make the folder
            dir.mkdir();
            
            int aggNum = aggressive ? 1 : 0; // ternary operator
            
            PrintWriter writer1 = new PrintWriter(folderName+"/"+dataFileName); //make the "data"
            writer1.println(mapWidth);                                          //file 
            writer1.println(mapHeight);
            writer1.println(skybox);
            writer1.println(music);
            writer1.println(playerX);
            writer1.println(playerY);
            writer1.println(spriteNum);
            writer1.println(actionPointNum);
            writer1.println(aggNum);
            writer1.close();

            makeMap(mapWidth,mapHeight); // create the actual map

            //write each map to a text file
            PrintWriter writer = new PrintWriter( folderName+"/"+ floorFileName,code);
            for(int c = 0; c< mapWidth; c++)
            {
                for(int r = 0; r< mapHeight; r++)
                {
                    writer.print(floorMap[r][c] + ",");
                }
                writer.print("\n");
            }
            writer.close();
            PrintWriter writer2 = new PrintWriter( folderName+"/"+ groundFileName,code);
            for(int c = 0; c< mapWidth; c++)
            {
                for(int r = 0; r< mapHeight; r++)
                {
                    writer2.print(worldMap[r][c] + ",");
                }
                writer2.print("\n");
            }
            writer2.close();
            PrintWriter writer3 = new PrintWriter( folderName+"/"+ ceilingFileName,code);
            for(int c = 0; c< mapWidth; c++)
            {
                for(int r = 0; r< mapHeight; r++)
                {
                    writer3.print(ceilingMap[r][c] + ",");
                }
                writer3.print("\n");
            }
            writer3.close();

            PrintWriter writer4 = new PrintWriter( folderName+"/"+ spriteFileName,code);
            writer4.close();
            PrintWriter writer5 = new PrintWriter( folderName+"/"+ npcFileName,code);
            writer5.close();
            PrintWriter writer6 = new PrintWriter( folderName+"/"+ actionFileName,code);
            writer6.close();
        } catch(IOException e){}
    }

    public void makeMap(int width,int height)
    {
        floorMap = new int[height][width];
        worldMap = new int[height][width];
        ceilingMap = new int[height][width];
        for(int c = 0; c< width; c++)
        {
            for(int r = 0; r< height; r++)
            {
                floorMap[r][c] = defFloor;
                if(covered)
                {ceilingMap[r][c] = defCeil;}
                else
                    ceilingMap[r][c] = 0;
                if(r == 0)
                {
                    worldMap[r][c] = defBord;
                    ceilingMap[r][c] = defCeil;
                }
                if(c == 0){
                    worldMap[r][c] = defBord;
                    ceilingMap[r][c] = defCeil;
                }
                if(r == height-1)
                {
                    worldMap[r][c] = defBord;
                    ceilingMap[r][c] = defCeil;
                }
                if(c == width-1)
                {
                    worldMap[r][c] = defBord;
                    ceilingMap[r][c] = defCeil;
                }
            }

        }
    }

}
