import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
/**
 * The Weapon2 class used for the player
 */
public class Weapon2
{
    int type; //weapons actual game value
    int kind; //classification of weapon
    String name; //Weapon2's ingame name, also corrosponds to its asset folder
    int ammo_type; //what ammo type the weapon feeds from
    int range;
    int magCap; //magazine capacity
    int mag; //current number of rounds in magazine
    boolean auto; // is the weapon fully automatic or not?
    int damage; // damage the weapon's projectiles carry;
    int reloadTime; // time the weapon takes to reload
    int sound; // what sound the gat makes when it blats
    
    // ammo type
    // 1 = pistol
    // 2 = energy
    // 3 = shotgun
    // 4 = intermediate
    // 5 = rifle
    
    
    int melee = 0;
    int pistol = 1;
    int shotgun = 2;
    int smg = 3;
    int rifle = 4;
        
    BufferedImage idle;
    BufferedImage[] shoot = new BufferedImage[4];
    BufferedImage[] reload;
    
    public Weapon2(int type)
    {
        this.type = type;
        defineAttributes(this.type);
    }
    
    public void defineAttributes(int type)
    {
        switch(type)
        {
            case 0:
            kind = melee;
            name = "Knife";
            range = 3;
            magCap = 0;
            auto = false;
            damage = 30;
            shoot = new BufferedImage[3];
            reloadTime = 0;
            sound = 0;
            ammo_type = 0;
            case 1:
            case 2:
            case 3:
            kind = pistol;
            name = "Glock";
            range = 1000;
            magCap = 12;
            auto = false;
            damage = 10;
            shoot = new BufferedImage[4];
            reloadTime = 7;
            sound = 0;
            ammo_type = 2;
            break;
            case 4:
            kind = pistol;
            name = "Mauser";
            range = 1000;
            magCap = 10;
            auto = false;
            damage = 15;
            shoot = new BufferedImage[3];
            reloadTime = 5;
            sound = 1;
            ammo_type = 1;
            break;
            
            case 5:
            case 6:
            case 7:
            kind = shotgun;
            name = "PumpShotgun";
            range = 30;
            magCap = 8;
            auto = false;
            damage = 5;
            shoot = new BufferedImage[3];
            reloadTime = 11;
            sound = 4;
            ammo_type = 3;
            break;
            
            case 8:
            case 9:
            case 10:
            kind = smg;
            name = "Suomi";
            range = 1000;
            magCap = 70;
            auto = true;
            damage = 7;
            shoot = new BufferedImage[2];
            reloadTime = 5;
            sound = 3;
            ammo_type = 1;
            break;
            
            case 11:
            kind = smg;
            name = "Agram";
            range = 1000;
            magCap = 25;
            auto = true;
            damage = 10;
            shoot = new BufferedImage[3];
            reloadTime = 5; 
            sound = 2;
            ammo_type = 2;
            break;
            
            case 12:
            case 13:
            kind = rifle;
            name = "HK51";
            range = 1000;
            magCap = 20;
            auto = false;
            damage = 25;
            shoot = new BufferedImage[3];
            reloadTime = 5;
            sound = 1;
            ammo_type = 5;
            break;
            
            case 14:
            kind = rifle;
            name = "Ak74";
            range = 1000;
            magCap = 30;
            auto = true;
            damage = 20;
            shoot = new BufferedImage[3];
            reloadTime = 5;
            sound = 2;
            ammo_type = 2;
            break;
            
            case 15:
            kind = rifle;
            name = "G11";
            range = 1000;
            magCap = 45;
            auto = true;
            damage = 60;
            shoot = new BufferedImage[3];
            reloadTime = 5;
            sound = 2;
            ammo_type = 2;
            default: 
        }
    }
    
    
    
    public void getTextures()
    {              
        try{
            idle = ImageIO.read(new File(name+"/idle.png"));
            for(int i = 0; i < this.shoot.length; i++)
            {
                int num = i+1;
                shoot[i] = ImageIO.read(new File(name+"/shoot"+num+".png"));
            }
        } catch (IOException e) {}
    }
    
    public BufferedImage idle()
    {
        return idle;
    }
    
}
