import java.util.Scanner;
import java.io.*;
import java.util.*;

public class Weapon extends Item
{  
    String weaponClass;
    ArrayList<WeaponPrefix> prefixes = new ArrayList<WeaponPrefix>();
    int damage,RoF,reloadTime,range,magSize;
    boolean melee = false;
    Scanner file;
    String baseName;
    

    /**
     * Constructor for objects of class Weapon
     */
    public Weapon(String[] data)
    {
        this.data = data;
        weaponClass = data[0];
        baseName = data[1];
        damage = Integer.parseInt(data[2]);
        RoF = Integer.parseInt(data[3]);
        reloadTime =  Integer.parseInt(data[4]);
        range = Integer.parseInt(data[5]);
        magSize = Integer.parseInt(data[6]);
        price = Integer.parseInt(data[7]);
        invView = new Img(baseName);
        this.iType = 1;
		amount = 1;
        
        
        try{file = new Scanner(new File("weaponPrefixes.csv"));}
        catch(IOException E){}
        
        while(file.hasNextLine())
        {
            prefixes.add(new WeaponPrefix(file.nextLine().split(",")));
        }

        if(range <= 5)
            melee = true;
    }

    public void update(int tier)
    {
        int points = tier * 3;
        if(!melee)
        {
            while(points --> 0)
            {
                int choose = (int)(Math.random()*6);

                switch(choose)
                {
                    case 0:
                    damage ++;
                    data[2] = Integer.toString(damage);
                    break;
                    case 1:
                    reloadTime ++;
                    data[4] = Integer.toString(reloadTime);
                    break;
                    case 2:
                    magSize ++;
                    data[6] = Integer.toString(magSize);
                    break;
                    default:
                    break;
                }
            }
        }
        else 
        {
            while(points --> 0)
            {
                int choose = (int)(Math.random()*6);
                points = points * 3;

               if(choose < 3)
                    damage ++;
                    
            }
        }
        
        int choose = (int)(Math.random()*100) + 1;
        if(choose > 50)
            applyPrefix(tier);
    }
    
    public void applyPrefix(int tier)
    {
        int which = (int)(Math.random()*prefixes.size());
        WeaponPrefix p = prefixes.get(which);
        
        this.weaponClass = p.prefix + " " + weaponClass;
        this.damage += p.damage * tier;
        this.range += p.range;
        this.magSize += p.magSize * tier;
        
        data[0] = weaponClass;
        data[2] = Integer.toString(damage);
        data[5] = Integer.toString(range);
        data[6] = Integer.toString(magSize);
    }

    public String toString()
    {
        return String.format(weaponClass + "," + baseName +",%d,%d,%d,%d,%d,%d",damage,RoF,reloadTime,range,magSize,price);

    }
}
