
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
public class Sprite
{
    // instance variables - replace the example below with your own
    double dx = 0;
    double dy = 0;
    double speed = 0;
    double collRadius = 0.5;
    double x; //x and y location
    double y;
    boolean alive = false;
    int health = 5;
    int texture; //index of the texture
    int state = 0;
    int texWidth = 64;
    int texHeight = texWidth;

    BufferedImage front = new BufferedImage(texWidth,texHeight,BufferedImage.TYPE_INT_RGB);
    BufferedImage back = new BufferedImage(texWidth,texHeight,BufferedImage.TYPE_INT_RGB);
    BufferedImage left = new BufferedImage(texWidth,texHeight,BufferedImage.TYPE_INT_RGB);
    BufferedImage right = new BufferedImage(texWidth,texHeight,BufferedImage.TYPE_INT_RGB);
    BufferedImage dead = new BufferedImage(texWidth,texHeight,BufferedImage.TYPE_INT_RGB);

    public Sprite(double x,double y,int texture)
    {
        this.x = x;
        this.y = y;
        this.texture = texture;
    }
    
    public boolean isAlive()
    {
        return alive;
    }
    
    public void kill()
    {}

    public int getHealth()
    {
        return health;
    }

    public void subtractHealth(int amount)
    {
        health -= amount;
    }

    public BufferedImage dead()
    {
        return dead;
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
        return texture;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public void turnLeft()
    {        
        double tempx = dx;
        double tempy = dy;
        dx = tempy;
        dy = -tempx;
    }

    public void turnRight()
    {        
        double tempx = dx;
        double tempy = dy;
        dx = tempy;
        dy = -tempx;
    }
}
