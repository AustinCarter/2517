import java.awt.*;

public class Star
{
    double x = 0; 
    double y = 0;
    double depth = 0;

    public Star(double x, double y, double depth)
    {
        this.x = x;
        this.y = y;
        this.depth = depth;
    }

    public void draw(Graphics g, double rx, double ry)
    {
        g.setColor(Color.WHITE);
        g.drawLine((int)(x-rx/depth)+400,(int)(y-ry/depth)+300,(int)(x-rx/depth)+400,(int)(y-ry/depth)+300);
    }
}
