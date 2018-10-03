import java.awt.*;

public class Planet
{
    double x = 0; 
    double y = 0;
    double depth = 0;
    double r = 0;
    String name;
    Color color = Color.GREEN;
    Img img;
    double spin = Math.random()*.01-.005;
    double angle = Math.random()*Math.PI;

    public Planet(double x, double y, double depth, double r, String name, Color color)
    {
        this.x = x;
        this.y = y;
        this.r = r;
        this.depth = depth;
        this.name = name;
        this.color = color;
    }

    public void draw(Graphics g, double rx, double ry)
    {
        if(img==null)
        {
            g.setColor(color);
            g.fillOval((int)(x-rx/depth-r/depth)+400,(int)(y-ry/depth-r/depth)+300,(int)(r*2/depth),(int)(r*2/depth));
        }
        else
        {
            angle+=spin;
            img.rotate(angle);
            img.setPosition((int)(x-rx/depth-r/depth)+400,(int)(y-ry/depth-r/depth)+300);
            img.draw(g,(int)(r*2/depth),(int)(r*2/depth));
        }
    }
}