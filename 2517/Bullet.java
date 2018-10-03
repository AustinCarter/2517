import java.awt.*;
public class Bullet
{
    double x, y, vx, vy, angle;
    boolean bad;
    Img bullet = new Img("bullet");
    public Bullet(double x, double y, double vx, double vy, double angle, boolean bad)
    {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.angle = angle;
        this.bad = bad;
    }

    public void update()
    {
        x+=vx;
        y+=vy;
    }
    
    public void draw(Graphics g, double rx, double ry)
    {
        bullet.rotate(angle);
        bullet.setPosition((int)(x-rx+400-2), (int)(y-ry+300-2));
        bullet.draw(g);
    }
}
