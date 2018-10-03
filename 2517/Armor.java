
import java.util.Scanner;
import java.io.*;
import java.util.*;

class Armor extends Item
{
    // instance variables - replace the example below with your own

    String armorClass;
    int shielding, health, absorption;
    ArrayList<ArmorPrefix> prefixes = new ArrayList<ArmorPrefix>();
    Scanner file;
    String baseName;
    Img invView;

    /**
     * Constructor for objects of class Armor
     */
    public Armor(String[] data)
    {
        this.data = data;
        armorClass = data[0];
        baseName = data[1];
        shielding = Integer.parseInt(data[2]);
        health = Integer.parseInt(data[3]);
        absorption =  Integer.parseInt(data[4]);
        price = Integer.parseInt(data[5]);
        amount = 1;
        
        this.iType = 2;
        
        try{file = new Scanner(new File("armorPrefixes.csv"));}
        catch(IOException E){}
        
        while(file.hasNextLine())
        {
            prefixes.add(new ArmorPrefix(file.nextLine().split(",")));
        }
    }
    public void update(int tier)
    {
       int points = tier * 3;
        
        while(points --> 0)
        {
            int choose = (int)(Math.random()*3);
            
            switch(choose)
            {
                case 0:
                    shielding ++;
                    break;
                case 1:
                    health ++;
                    break;
                case 2:
                    absorption += 2;
                    break;
            }
        }
        
       int choose = (int)(Math.random()*100) + 1;
        if(choose > 50)
            applyPrefix(tier);
    }
    
    public void applyPrefix(int tier)
    {
        int which = (int)(Math.random()*prefixes.size());
        ArmorPrefix p = prefixes.get(which);
        
        this.armorClass = p.prefix + " " + armorClass;
        this.shielding += p.shielding * tier;
        this.health += p.health * tier;
        this.absorption += p.absorption * tier;
        
        
    }
    
    public String toString()
    {
        return String.format(armorClass + "," + baseName + ",%d,%d,%d,%d",shielding, health, absorption,price);

    }

}
