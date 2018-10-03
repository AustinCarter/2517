import java.awt.*;

public class Baddie
{
    // instance variables
    Img img;
    double x = 0; 
    double y = 0;
    int w = 0, h = 0;
    double angle = Math.random()*Math.PI*2;
    int speed = 0;
    double acc = .5;
    double vx, vy;
    double rx, ry;
    double health = 100;
    double fhealth = 100;
    int btimer = 0;
    boolean splode = false;
    int stimer = 0;
    int behavior = 0;
    double vx1,vy1,vx2,vy2,vx3,vy3,vx4,vy4,vx5,vy5;
    double x1=x,y1=y,x2=x,y2=y,x3=x,y3=y,x4=x,y4=y,x5=x,y5=y;
    boolean hit = false;

    public Baddie(double x, double y, double angle, int behavior)
    {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.behavior = behavior;
        if(behavior == 0)
        {
            speed = 2;
            img = new Img("freighter");
            health = 300;
            fhealth = 300;
        }
        else if(behavior == 1)
        {
            speed = 10;
            img = new Img("images");
        }
        else if(behavior == 2)
        {
            speed = 4;
            img = new Img("thicc");
            health = 150;
            fhealth = 150;
        }
        w = img.img.getIconWidth();
        h = img.img.getIconHeight();
    }

    public Baddie(double x, double y, double angle, int speed, double acc, Img img)
    {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.speed = speed;
        this.acc = acc;
        this.img = img;
    }

    public void splode()
    {
        splode = true;
        vx1=(Math.random()-.5);
        vy1=(Math.random()-.5);
        vx2=(Math.random()-.5);
        vy2=(Math.random()-.5);
        vx3=(Math.random()-.5);
        vy3=(Math.random()-.5);
        vx4=(Math.random()-.5);
        vy4=(Math.random()-.5);
        vx5=(Math.random()-.5);
        vy5=(Math.random()-.5);
        x1=x;
        y1=y;
        x2=x;
        y2=y;
        x3=x;
        y3=y;
        x4=x;
        y4=y;
        x5=x;
        y5=y;
        stimer = 0;
    }

    public void setX(int x)
    {
        this.x = x + w/2;
    }

    public void setY(int y)
    {
        this.y = y + h/2;
    }

    public double getX()
    {
        return x + w/2;
    }

    public double getY()
    {
        return y + h/2;
    }

    public double distTo(double x, double y)
    {
        double a = x - this.x + w/2;
        double b = y - this.y + h/2;
        return Math.sqrt(a*a + b*b);
    }

    public void forward()
    {
        if(vx/Math.cos(angle) != (speed*(health/100)))
        {
            vx += Math.cos(angle) * acc;
            vy += Math.sin(angle) * acc;
        }
        if(vx/Math.cos(angle)>(speed*(health/100)))vx = Math.cos(angle) * (speed*(health/fhealth));
        if(vy/Math.sin(angle)>(speed*(health/100)))vy = Math.sin(angle) * (speed*(health/fhealth));
    }

    public void reverse()
    {
        if(vx/Math.cos(angle) != -5)
        {
            vx -= Math.cos(angle) * acc;
            vy -= Math.sin(angle) * acc;
        }
        if(vx/Math.cos(angle)<-speed)vx = Math.cos(angle) * -speed;
        if(vy/Math.sin(angle)<-speed)vy = Math.sin(angle) * -speed;
    }

    public void update()
    {
        x += vx;
        y += vy;
        if(vx>.01)vx -= .01;
        if(vx<-.01)vx += .01;
        if(vy>.01)vy -= .01;
        if(vy<-.01)vy += .01;
        if(splode || hit)stimer++;
        if(health>30 && behavior == 1)
        {
            angle = -Math.atan2(rx-x, ry-y)+(Math.PI/2);
            if(distTo(rx,ry)>200)forward();
            else if(distTo(rx,ry)<150)reverse(); 
            else if(distTo(rx,ry)>150 && distTo(rx,ry)<200)
            {
                if(vx>.01)vx -= .3;
                if(vx<-.01)vx += .3;
                if(vy>.01)vy -= .3;
                if(vy<-.01)vy += .3;
            }
            btimer++;
        }
        else if((health>15 && behavior == 1) || (behavior == 2 && health<fhealth && health>15))
        {
            angle = -Math.atan2(rx-x, ry-y)-(Math.PI/2);
            forward();
            if(behavior == 2)btimer++;
        }
        else forward();
    }
    
    public void getHit(double bullet)
    {
        health -= bullet;
        hit = true;
        vx1=(Math.random()-.5);
        vy1=(Math.random()-.5);
        vx2=(Math.random()-.5);
        vy2=(Math.random()-.5);
        vx3=(Math.random()-.5);
        vy3=(Math.random()-.5);
        vx4=(Math.random()-.5);
        vy4=(Math.random()-.5);
        vx5=(Math.random()-.5);
        vy5=(Math.random()-.5);
        x1=x;
        y1=y;
        x2=x;
        y2=y;
        x3=x;
        y3=y;
        x4=x;
        y4=y;
        x5=x;
        y5=y;
        stimer = 0;
    }

    public void turnRight()
    {
        angle += .05;
    }

    public void turnLeft()
    {
        angle -= .05;
    }

    public void draw(Graphics gr, double rx, double ry)
    {
        Graphics2D g = (Graphics2D) gr;
        this.rx = rx;
        this.ry = ry;
        if(!splode)
        {
            img.rotate(angle);
            img.setPosition((int)x-(int)rx+400-w/2, (int)y-(int)ry+300-h/2);
            img.draw(g);
            if(hit)
            {
                x1 += vx1;
                y1 += vy1;
                x2 += vx2;
                y2 += vy2;
                x3 += vx3;
                y3 += vy3;
                x4 += vx4;
                y4 += vy4;
                x5 += vx5;
                y5 += vy5;
                g.setColor(Color.YELLOW);
                g.fillOval((int)x1-(int)rx+400-5,(int)y1-(int)ry+300-5,(int)(10*((50-(double)stimer)/50)),(int)(10*((50-(double)stimer)/50)));
                g.fillOval((int)x2-(int)rx+400-5,(int)y2-(int)ry+300-5,(int)(10*((50-(double)stimer)/50)),(int)(10*((50-(double)stimer)/50)));
                g.fillOval((int)x3-(int)rx+400-5,(int)y3-(int)ry+300-5,(int)(10*((50-(double)stimer)/50)),(int)(10*((50-(double)stimer)/50)));
                g.fillOval((int)x4-(int)rx+400-5,(int)y4-(int)ry+300-5,(int)(10*((50-(double)stimer)/50)),(int)(10*((50-(double)stimer)/50)));
                g.fillOval((int)x5-(int)rx+400-5,(int)y5-(int)ry+300-5,(int)(10*((50-(double)stimer)/50)),(int)(10*((50-(double)stimer)/50)));
            }
        }
        else
        {
            x1 += vx1;
            y1 += vy1;
            x2 += vx2;
            y2 += vy2;
            x3 += vx3;
            y3 += vy3;
            x4 += vx4;
            y4 += vy4;
            x5 += vx5;
            y5 += vy5;
            g.setColor(Color.YELLOW);
            g.fillOval((int)x1-(int)rx+400-5,(int)y1-(int)ry+300-5,(int)(10*((50-(double)stimer)/50)),(int)(10*((50-(double)stimer)/50)));
            g.fillOval((int)x2-(int)rx+400-5,(int)y2-(int)ry+300-5,(int)(10*((50-(double)stimer)/50)),(int)(10*((50-(double)stimer)/50)));
            g.fillOval((int)x3-(int)rx+400-5,(int)y3-(int)ry+300-5,(int)(10*((50-(double)stimer)/50)),(int)(10*((50-(double)stimer)/50)));
            g.fillOval((int)x4-(int)rx+400-5,(int)y4-(int)ry+300-5,(int)(10*((50-(double)stimer)/50)),(int)(10*((50-(double)stimer)/50)));
            g.fillOval((int)x5-(int)rx+400-5,(int)y5-(int)ry+300-5,(int)(10*((50-(double)stimer)/50)),(int)(10*((50-(double)stimer)/50)));
        }
    }
}
