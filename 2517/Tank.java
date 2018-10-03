import java.awt.*;

public class Tank
{
    // instance variables
    Img img = new Img("images");
    Img turret = new Img("turret");
    double x = 0; 
    double y = 0;
    double angle = 0;
    double tangle = 0;
    int speed = 7;
    double acc = .25;
    double vx, vy;
    double health = 100;
    double fhealth = 100;
    boolean t = false;

    /**
     * Constructor for objects of class Tank
     */
    public Tank(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public void setX(int x)
    {
        this.x = x + 16;
    }

    public void setY(int y)
    {
        this.y = y + 16;
    }

    public double getX()
    {
        return x + 16;
    }

    public double getY()
    {
        return y + 16;
    }

    public double distTo(double x, double y)
    {
        double a = x - this.x + 16;
        double b = y - this.y + 16;
        return Math.sqrt(a*a + b*b);
    }

    public void forward()
    {
        if(vx/Math.cos(angle) != 5)
        {
            if(vx/Math.cos(angle)<speed)vx += Math.cos(angle) * acc;
            if(vy/Math.sin(angle)<speed)vy += Math.sin(angle) * acc;
        }
    }

    public void reverse()
    {
        if(vx/Math.cos(angle) != -5)
        {
            if(vx/Math.cos(angle)>-speed)vx -= Math.cos(angle) * acc;
            if(vy/Math.sin(angle)>-speed)vy -= Math.sin(angle) * acc;
        }
    }

    public void update(int mousex, int mousey)
    {
        x += vx;
        y += vy;
        if(vx>.01)vx -= .01;
        if(vx<-.01)vx += .01;
        if(vy>.01)vy -= .01;
        if(vy<-.01)vy += .01;
        if(t)tangle = -Math.atan2(mousex-400,mousey-300)+(Math.PI/2);
        else tangle = angle;
    }

    public void turnRight()
    {
        angle += .05;
    }

    public void turnLeft()
    {
        angle -= .05;
    }

    public void draw(Graphics gr)
    {
        Graphics2D g = (Graphics2D) gr;
        img.rotate(angle);
        img.setPosition(400-16+(int)(vx*2), 300-16+(int)(vy*2));
        img.draw(g);
        if(t)
        {
            turret.rotate(tangle);
            turret.setPosition(400-16+(int)(vx*2), 300-16+(int)(vy*2));
            turret.draw(g);
        }
    }
}
