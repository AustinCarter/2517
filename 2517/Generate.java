import java.util.Scanner;
import java.io.*;
import java.util.*;

public class Generate
{
    /*
     * Algorithim steps
     * Find Enemy loot class
     * Decide Base Item
     * Decide modifiers/type of modifier
     */

    static String[][] mobStats;
    static String[][][] TC; //loot table

    /*public static void main(String [] args) throws IOException
    {
    init();
    int tier = (int)(Math.random() * 4);
    System.out.println(dropItem(tier));
    }*/

    public static void init() throws IOException
    {
        /*Scanner file = new Scanner(new File("monsterStats.csv"));
        int go = file.nextInt();
        file.nextLine();
        mobStats = new String[go][4];
        for(int i = 0; i < go; i++)
        {
        mobStats[i] = file.nextLine().split(",");
        }*/

        Scanner file = new Scanner(new File("lootTable.csv"));
        int tiers = file.nextInt();
        int go = file.nextInt();
        file.nextLine();

        TC = new String[tiers][go][8];

        for(int i = 0; i < tiers; i++)
        {
            for(int j = 0; j < go; j++)
            {
                TC[i][j] = file.nextLine().split(",");       
            }    
            try{
                go = file.nextInt();
                file.nextLine();
            }catch(NoSuchElementException E){}
        }
    }

    static Item dropItem(int tier, int x, int y)
    {
        boolean weapon = true; 
        int choose;

        choose = (int)(Math.random() * TC[tier].length);

        Weapon wDrop;

        wDrop = new Weapon(TC[tier][choose]);
        wDrop.x = x;
        wDrop.y = y;
        //System.out.println(wDrop.toString());
        wDrop.update(tier + 1);
        return wDrop;

        //drop.update(tier);
        //System.out.printf("%d,%d \n", tier, choose);
        //return drop;
    }

}
