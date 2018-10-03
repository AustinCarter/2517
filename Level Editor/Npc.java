import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
/**
 * Npc Class for Enemies and the Like
 */
public class Npc extends Sprite
{
    // instance variables - replace the example below with your own
    double dx, dy;
    double speed;
    boolean alive;
    double collRadius; // collision radius
    int state;
    int damage;
    int health;
    int type;
    int currentTex; 
    int texWidth = 64;
    int texHeight = texWidth;
    String name;

    BufferedImage front;
    BufferedImage back;
    BufferedImage left;
    BufferedImage right;
    BufferedImage dead;
    ///types:
    // type 0 = spaceGuy
    // type 1 = armorAlien

    //states:
    // state 0 = stil or static
    // state 1 = walking
    // 

    public Npc(double x,double y,int texture,int type)
    {
        super(x,y,texture);
        this.type = type;
        defineAttributes(this.type);
        getTextures();
    }

    public void defineAttributes(int type)
    {
        switch(type)
        {
            case 0:
            alive = true;
            dx = -1;
            dy = 0;
            state = 1;
            speed = 0.08;
            health = 20;
            damage = 5;
            currentTex = 2;
            collRadius = 0.4;
            texture = 2;
            name = "spaceGuy";
            break;
            case 1:
            alive = true;
            dx = -1;
            dy = 0;
            state = 1;
            speed = 0.0;
            health = 60;
            damage = 10;
            currentTex = 2;
            collRadius = 0.6;
            texture = 5;
            name = "armorAlien";
            break;
            
            case 2:
            alive = true;
            dx = -1;
            dy = 0;
            state = 1;
            speed = 0.10;
            health = 50;
            damage = 10;
            currentTex = 2;
            collRadius = 0.4;
            texture = 6;
            name = "spaceGuy2";
            break;
        }

    }

    public void getTextures()
    {
        try{
            left = ImageIO.read(new File(name+"/Left.png"));
            right = ImageIO.read(new File(name+"/Right.png"));
            front = ImageIO.read(new File(name+"/Front.png"));
            back = ImageIO.read(new File(name+"/Back.png"));
            dead = ImageIO.read(new File(name+"/Dead.png"));
        } catch (IOException e) {}
    }
    
    public boolean isAlive()
    {
        return alive;
    }
    
    public void subtractHealth(int amount)
    {
        health -= amount;
    }
    
    public void kill()
    {
        left = dead;
        right = dead;
        front = dead;
        back = dead;
        alive = false;
        speed = 0.0;
        state = 0;
    }
    
    public BufferedImage front()
    {
        return front;
    }

    public BufferedImage back()
    {
        return back;
    }

    public BufferedImage left()
    {
        return left;
    }

    public BufferedImage right()
    {
        return right;
    }

    public double speed()
    {
        return speed;
    }

    public double dx()
    {
        return dx;
    }

    public double dy()
    {return dy;}

    public int getState()
    {
        return state;
    }

    public int getTex()
    {
        return currentTex;
    }

    public int getHealth()
    {
        return health;
    }

    public void turnLeft()
    {
        double tempx = dx;
        double tempy = dy;
        dx = -tempy;
        dy = tempx;
    }

    public void turnRight()
    {        
        double tempx = dx;
        double tempy = dy;
        dx = tempy;
        dy = -tempx;
    }
}
