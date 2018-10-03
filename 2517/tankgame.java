import java.awt.*;
import java.util.*;
import java.io.*;
import javax.swing.AbstractAction;
import java.awt.event.*;
import javax.swing.*;
/**
 * Write a description of class tankgame here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class tankgame extends JPanel implements MouseListener, MouseMotionListener, KeyListener, ActionListener
{
    // instance variables
    final int TURNLEFT = 0;
    final int TURNRIGHT = 1;
    final int MOVEFORWARD = 2;
    final int MOVEBACK = 3;
    final int SHOOT = 4;
    final int ENTER = 5;
    final int INV = 6;
    boolean[] keys = new boolean[10];
    double x, y;
    double vx, vy;
    int timer = 2000;
    int btimer = 0;
    int spawn = 1000;
    int rate = 20;
    boolean hover = false;
    Planet hovered = null;
    boolean ton = true;
    boolean on = true;
    Point mouse = new Point(0,0);
    Item get = null;
    String level;
    int htimer = 0;
    double bullet = 10;
    Planet goal = null;
    double pangle = 0;
    Img pointer = new Img("pointer");
    boolean pause = false;
    int ptimer = 0;
    Img load = new Img("LoadingScreen");

    Generate gen = new Generate();
    Tank tank;
    ArrayList<Baddie> baddies = new ArrayList<Baddie>();
    ArrayList<Planet> planets = new ArrayList<Planet>();
    ArrayList<Star> stars = new ArrayList<Star>();
    ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    ArrayList<Item> items = new ArrayList<Item>();
    long currentTime = System.nanoTime();

    /**
     * Constructor for objects of class tankgame
     */
    public tankgame(double px, double py)
    {
        setBackground(Color.BLACK);
        try{gen.init();}catch(IOException e){}
        for(int i = 0;i<50000;i++)
        {
            double x = Math.random()*10000-5000;
            double y = Math.random()*10000-5000;
            double depth = Math.random()*10+3;
            stars.add(new Star(x,y,depth));
        }
        Planet planet1 = new Planet(0,0,7,600,"prison",new Color(128,128,255));
        planet1.img = new Img(planet1.name);
        planets.add(planet1);
        Planet planet4 = new Planet(300,-150,6,500,"polemia",new Color(255,128,128));
        planet4.img = new Img(planet4.name);
        planets.add(planet4);
        Planet planet2 = new Planet(500,300,4,410,"smalltown",new Color(128,255,128));
        planet2.img = new Img(planet2.name);
        planets.add(planet2);
        Planet planet3 = new Planet(-300,250,3,150,"castle",new Color(128,128,128));
        planet3.img = new Img(planet3.name);
        planets.add(planet3);
        Planet planet5 = new Planet(-250,-300,2,300,"space station",new Color(128,128,128));
        planet5.img = new Img(planet5.name);
        planets.add(planet5);
        x = px;
        y = py;
        tank = new Tank(x,y);
        javax.swing.Timer t = new javax.swing.Timer(5,this);
        t.start();
    }

    public void paintComponent(Graphics gr)
    {   
        super.paintComponent(gr);
        if(gr==null)return;
        Graphics2D g = (Graphics2D) gr;
        if(stars == null || planets == null) return;
        for(Star star : stars)
        {
            star.draw(g,x-(int)(vx*2),y-(int)(vy*2));
        }
        for(Planet planet : planets)
        {
            planet.draw(g,x-(int)(vx*2),y-(int)(vy*2));
        }
        if(hover)
        {
            g.setColor(Color.GREEN);
            g.drawOval((int)(hovered.x-(x-(int)(vx*2))/hovered.depth-hovered.r/hovered.depth)+400-5,(int)(hovered.y-(y-(int)(vy*2))/hovered.depth-hovered.r/hovered.depth)+300-5,(int)(hovered.r*2/hovered.depth)+10,(int)(hovered.r*2/hovered.depth)+10);
        }
        for(Item item : items)
        {
            item.sdraw(g,(int)x-(int)(vx*2),(int)y-(int)(vy*2));
        }
        for(Bullet bullet : bullets)
        {
            bullet.draw(g,x-(int)(vx*2),y-(int)(vy*2));
        }
        for(Baddie baddie : baddies)
        {
            baddie.draw(g,x-(int)(vx*2),y-(int)(vy*2));
        }
        if(tank != null)
            tank.draw(g);
        g.setColor(Color.GREEN);
        g.fillRect(0,557,(int)((double)tank.health/(double)tank.fhealth*800),5);
        if(hover)
        {
            g.setColor(Color.BLACK);
            g.drawString("Press space to "+hovered.name,21,20);
            g.drawString("Press space to "+hovered.name,19,20);
            g.drawString("Press space to "+hovered.name,20,21);
            g.drawString("Press space to "+hovered.name,20,19);
            g.setColor(Color.GREEN);
            g.drawString("Press space to "+hovered.name,20,20);
        }
        if(goal != null)
        {
            if(hovered!=null)
            {
                if(goal.x != hovered.x)
                {
                    pangle = -Math.atan2(goal.x-x, goal.y-y)+(Math.PI/2);
                    pointer.rotate(pangle);
                    pointer.setPosition(400-128,300-128);
                    pointer.draw(g);
                }
            }
            else
            {
                pangle = -Math.atan2(goal.x-x, goal.y-y)+(Math.PI/2);
                pointer.rotate(pangle);
                pointer.setPosition(400-128,300-128);
                pointer.draw(g);
            }
        }
        if(!ton)
        {
            load.draw(g);
            on = false;
        }
    }

    public void keyTyped(KeyEvent key) {}

    public void keyPressed(KeyEvent key) { processKey(key.getKeyCode(), true); }

    public void keyReleased(KeyEvent key) { processKey(key.getKeyCode(), false); }

    public void mouseClicked(MouseEvent mouse) {}

    public void mousePressed(MouseEvent mouse) {
        this.mouse = mouse.getPoint();
        if(tank.t)tank.tangle = -Math.atan2(this.mouse.x-400, this.mouse.y-300)+(Math.PI/2);
        else tank.tangle = tank.angle;
        if(btimer>rate)  
        {
            bullets.add(new Bullet(x,y,Math.cos(tank.tangle)*20,Math.sin(tank.tangle)*20,tank.tangle,false));
            btimer = 0;
        }
    }

    public void mouseReleased(MouseEvent mouse) {}

    public void mouseEntered(MouseEvent mouse) {}

    public void mouseExited(MouseEvent mouse) {}

    public void mouseDragged(MouseEvent mouse) {}

    public void mouseMoved(MouseEvent mouse)
    {
        this.mouse = mouse.getPoint();
    }

    public void actionPerformed(ActionEvent e)
    {
        if(!pause)
        {
            long newTime = System.nanoTime();
            long diff = newTime-currentTime;
            if(diff > 15000000){
                ptimer ++;
                if(keys[INV] && ptimer>10) 
                {
                    pause = true;
                    ptimer = 0;
                    return;
                }
                if(keys[TURNRIGHT]) tank.turnRight();
                if(keys[TURNLEFT]) tank.turnLeft();
                if(keys[MOVEFORWARD])   tank.forward();
                if(keys[MOVEBACK])  tank.reverse();

                tank.update(mouse.x,mouse.y);
                vx = tank.vx;
                vy = tank.vy;
                x = tank.getX();
                y = tank.getY();
                hover = false;
                for(Planet planet : planets)
                {
                    //((int)(x-rx/depth-r)+400,(int)(y-ry/depth-r)+300,(int)(r*2/depth),(int)(r*2/depth))
                    if(Math.abs(planet.x-x/planet.depth)<planet.r/planet.depth && Math.abs(planet.y-y/planet.depth)<planet.r/planet.depth)
                    {
                        hover = true;
                        hovered = new Planet(planet.x,planet.y,planet.depth,planet.r,planet.name,planet.color);
                        hovered.name = "enter "+planet.name;
                    }
                }
                for(Baddie baddie : baddies)
                {
                    if(Math.abs(baddie.x-x)<baddie.w+32 && Math.abs(baddie.y-y)<baddie.w+32 && baddie.health<30 && !baddie.splode)
                    {
                        hover = true;
                        hovered = new Planet(baddie.x,baddie.y,1,baddie.w+32,"board ship",Color.BLUE);
                    }
                }
                for(Item item : items)
                {
                    if(Math.abs(item.x-x)<64 && Math.abs(item.y-y)<64)
                    {
                        hover = true;
                        hovered = new Planet(item.x,item.y,1,64,"pick up "+item.getType(),Color.BLUE);
                    }
                }
                ArrayList<Item> pickup = new ArrayList<Item>();
                htimer++;
                if(htimer>20)
                {
                    if(hover && keys[ENTER] && hovered.name.contains("pick up"))
                    {
                        for(Item item : items)
                        {
                            if(Math.abs(item.x-x)<64 && Math.abs(item.y-y)<64)
                            {
                                pickup.add(item);
                            }
                        }
                        htimer = 0;
                    }
                    else if(hover && keys[ENTER] && hovered.name.contains("enter"))
                    {
                        tank.health = tank.fhealth;
                        for(Planet planet : planets)
                        {
                            if(Math.abs(planet.x-x/planet.depth)<planet.r/planet.depth && Math.abs(planet.y-y/planet.depth)<planet.r/planet.depth)
                            {
                                level = planet.name;
                            }
                        }
                        ton = false;
                        htimer = 0;
                    }
                    else if(hover && keys[ENTER] && hovered.name.contains("board"))
                    {
                        for(Baddie baddie : baddies)
                        {
                            if(Math.abs(baddie.x-x)<baddie.w+32 && Math.abs(baddie.y-y)<baddie.w+32 && baddie.health<30 && !baddie.splode)
                            {
                                if(baddie.behavior==1)level = "AnTest";
                                else if(baddie.behavior==0)level = "Ship1";
                                else level = "smallship";
                                ton = false;
                                htimer = 0;
                            }
                        }
                    }
                }
                for(Item item : pickup)
                {
                    get = item;
                    items.remove(item);
                }
                if(!hover)hovered = null;
                if(tank.t)tank.tangle = -Math.atan2(mouse.x-400, mouse.y-300)+(Math.PI/2);
                else tank.tangle = tank.angle;
                if(keys[SHOOT] && btimer>rate)  
                {
                    bullets.add(new Bullet(x,y,Math.cos(tank.tangle)*20,Math.sin(tank.tangle)*20,tank.tangle,false));
                    btimer = 0;
                }
                timer++;
                btimer++;
                if(timer>spawn)
                {
                    double angle = Math.random()*Math.PI;
                    int wall = (int)(Math.ceil(Math.random()*4));
                    double bx = Math.random()*1000-500, by = Math.random()*800-400;
                    if(wall==1)bx = -500;
                    else if(wall==2)by = -400;
                    else if(wall==3)bx = 500;
                    else if(wall==4)by = 400;
                    double choose = Math.random();
                    if(choose<.25)baddies.add(new Baddie(x+bx,y+by,angle,0));
                    else if(choose<.75)baddies.add(new Baddie(x+bx,y+by,angle,1));
                    else baddies.add(new Baddie(x+bx,y+by,angle,2));
                    timer = 0;
                }
                ArrayList<Baddie> hit = new ArrayList<Baddie>();
                ArrayList<Baddie> dies = new ArrayList<Baddie>();
                ArrayList<Bullet> bdies = new ArrayList<Bullet>();
                for(Baddie baddie : baddies)
                {
                    baddie.update();
                    if(!baddie.splode)
                    {
                        if(baddie.behavior == 1 && baddie.btimer>100 && baddie.distTo(x,y)<500)
                        {
                            bullets.add(new Bullet(baddie.x,baddie.y,Math.cos(baddie.angle)*20,Math.sin(baddie.angle)*20,baddie.angle,true));
                            baddie.btimer = 0;
                        }
                        else if(baddie.behavior == 2 && baddie.btimer>20)
                        {
                            bullets.add(new Bullet(baddie.x,baddie.y,Math.cos(baddie.angle)*20,Math.sin(baddie.angle)*20,baddie.angle,true));
                            bullets.add(new Bullet(baddie.x,baddie.y,Math.cos(baddie.angle+(Math.PI/6))*20,Math.sin(baddie.angle+(Math.PI/6))*20,baddie.angle+(Math.PI/6),true));
                            bullets.add(new Bullet(baddie.x,baddie.y,Math.cos(baddie.angle+(Math.PI/6*2))*20,Math.sin(baddie.angle+(Math.PI/6*2))*20,baddie.angle+(Math.PI/6*2),true));
                            bullets.add(new Bullet(baddie.x,baddie.y,Math.cos(baddie.angle+(Math.PI/6*3))*20,Math.sin(baddie.angle+(Math.PI/6*3))*20,baddie.angle+(Math.PI/6*3),true));
                            bullets.add(new Bullet(baddie.x,baddie.y,Math.cos(baddie.angle+(Math.PI/6*4))*20,Math.sin(baddie.angle+(Math.PI/6*4))*20,baddie.angle+(Math.PI/6*4),true));
                            bullets.add(new Bullet(baddie.x,baddie.y,Math.cos(baddie.angle+(Math.PI/6*5))*20,Math.sin(baddie.angle+(Math.PI/6*5))*20,baddie.angle+(Math.PI/6*5),true));
                            bullets.add(new Bullet(baddie.x,baddie.y,Math.cos(baddie.angle+(Math.PI/6*6))*20,Math.sin(baddie.angle+(Math.PI/6*6))*20,baddie.angle+(Math.PI/6*6),true));
                            bullets.add(new Bullet(baddie.x,baddie.y,Math.cos(baddie.angle+(Math.PI/6*7))*20,Math.sin(baddie.angle+(Math.PI/6*7))*20,baddie.angle+(Math.PI/6*7),true));
                            bullets.add(new Bullet(baddie.x,baddie.y,Math.cos(baddie.angle+(Math.PI/6*8))*20,Math.sin(baddie.angle+(Math.PI/6*8))*20,baddie.angle+(Math.PI/6*8),true));
                            bullets.add(new Bullet(baddie.x,baddie.y,Math.cos(baddie.angle+(Math.PI/6*9))*20,Math.sin(baddie.angle+(Math.PI/6*9))*20,baddie.angle+(Math.PI/6*9),true));
                            bullets.add(new Bullet(baddie.x,baddie.y,Math.cos(baddie.angle+(Math.PI/6*10))*20,Math.sin(baddie.angle+(Math.PI/6*10))*20,baddie.angle+(Math.PI/6*10),true));
                            bullets.add(new Bullet(baddie.x,baddie.y,Math.cos(baddie.angle+(Math.PI/6*11))*20,Math.sin(baddie.angle+(Math.PI/6*11))*20,baddie.angle+(Math.PI/6*11),true));
                            baddie.btimer = 0;
                        }
                    }
                    for(Baddie baddie2 : baddies)
                    {
                        if(Math.abs(baddie.x-baddie2.x)<baddie.w/2+baddie2.w/2 && Math.abs(baddie.y-baddie2.y)<baddie.h/2+baddie2.h/2 && !baddie.equals(baddie2))
                        {
                            double tx, ty;
                            tx = baddie.vx;
                            ty = baddie.vy;
                            baddie.vx = baddie2.vx;
                            baddie.vy = baddie2.vy;
                            baddie2.vx = tx;
                            baddie2.vy = ty;
                        }
                    }
                }
                for(Bullet bullet : bullets)
                {
                    bullet.update();
                    for(Baddie baddie : baddies)
                    {
                        if(bullet.bad && Math.abs(bullet.x-x)<16 && Math.abs(bullet.y-y)<16)
                        {
                            tank.vx += Math.cos(bullet.angle)*.25;
                            tank.vy += Math.sin(bullet.angle)*.25;
                            tank.health--;
                            bdies.add(bullet);
                        }
                        else if(!bullet.bad && Math.abs(bullet.x-baddie.x)<baddie.w/2 && Math.abs(bullet.y-baddie.y)<baddie.h/2 && !baddie.splode)
                        {
                            hit.add(baddie);
                            bdies.add(bullet);
                        }
                    }
                }
                for(Baddie baddie : hit)
                {
                    if(baddie.health>0)baddie.getHit(bullet);
                    else baddies.get(baddies.indexOf(baddie)).health=0;
                    for(Bullet bullet : bdies)
                    {
                        if(Math.abs(bullet.x-baddie.x)<baddie.w/2 && Math.abs(bullet.y-baddie.y)<baddie.h/2)
                        {
                            baddies.get(baddies.indexOf(baddie)).vx += bullet.vx/4/(baddie.w/32);
                            baddies.get(baddies.indexOf(baddie)).vy += bullet.vy/4/(baddie.w/32);
                        }
                    }
                    if(baddie.health<=0 && baddie.behavior != 0)
                    {
                        baddies.get(baddies.indexOf(baddie)).splode();
                        items.add(Generate.dropItem(0,(int)baddie.x,(int)baddie.y));
                    }
                }
                for(Baddie baddie : baddies)
                {
                    if(baddie.splode)
                    {
                        if(baddie.stimer>50)
                        {
                            dies.add(baddie);
                        }
                    }
                }
                for(Baddie baddie : dies)
                {
                    baddies.remove(baddie);
                }
                for(Bullet bullet : bullets)
                {
                    if(bullet.x<x-1000 || bullet.x>x+1000 || bullet.y<y-1000 || bullet.y>y+1000)bdies.add(bullet);
                }
                for(Bullet bullet : bdies)
                {
                    bullets.remove(bullet);
                }
                repaint();
                currentTime = newTime;
            }
        }
    }

    public void processKey(int code, boolean pressed)
    {
        switch(code)
        {
            case 'D': keys[TURNRIGHT] = pressed; break;
            case 'A': keys[TURNLEFT] = pressed; break; 
            case 'W': keys[MOVEFORWARD] = pressed; break;
            case 'S': keys[MOVEBACK] = pressed; break;
            case 39: keys[TURNRIGHT] = pressed; break;
            case 37: keys[TURNLEFT] = pressed; break; 
            case 38: keys[MOVEFORWARD] = pressed; break;
            case 40: keys[MOVEBACK] = pressed; break;
            case 'F': keys[SHOOT] = pressed; break;
            case 32: keys[ENTER] = pressed; break;
            case 'Q': keys[INV] = pressed; break;
        }
    }
}