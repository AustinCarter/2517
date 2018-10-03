import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
public class Title extends JPanel implements ActionListener, KeyListener
{
    tankgame space;
    GameEngine rc;
    InventoryView invv;
    Store store;
    Container cont;
    JFrame frame;
    Inventory inv = new Inventory("playerInv");
    Weapon2[] weapons = {null,null,null,null,null,null,null,null,null,null};
    boolean pause = false;
    double lastx, lasty, lasthp = 100, lastfhp = 100;
    int[] ammos = new int[4];
    boolean tsplash = true;
    boolean splash = true;
    boolean select = true;

    final int UP = 0;
    final int DOWN = 1;
    final int SPACE = 2;
    boolean[] keys = new boolean[3];

    Img splashimg = new Img("splash");
    Img start = new Img("start");
    Img continu = new Img("continue");
    Img load = new Img("LoadingScreen");
    
    String folder = "Inventory Stats";
    String code = "UTF-8";
    
    Quest currentQuest = new Collection("Spice It Up");
    
    

    String stNm;
    String tempCont;
    byte storOr;

    ArrayList<Planet> planets = new ArrayList<Planet>();
    ArrayList<Star> stars = new ArrayList<Star>();
    double x = 0, y = 0;

    public Title()
    {
        for(int i = 0;i<50000;i++)
        {
            double x = Math.random()*10000-5000;
            double y = Math.random()*10000-5000;
            double depth = Math.random()*10+3;
            stars.add(new Star(x,y,depth));
        }
        for(int i = 0;i<200;i++)
        {
            double x = Math.random()*10000-5000;
            double y = Math.random()*10000-5000;
            double depth = Math.random()*10+5;
            double r = Math.random()*1000;
            String name = "planet "+i;
            int red = (int)(Math.random()*128+128);
            int g = (int)(Math.random()*128+128);
            int b = (int)(Math.random()*128+128);
            Planet planet = new Planet(x,y,depth,r,name,new Color(red,g,b));
            planets.add(planet);
        }
        createWindow();
        javax.swing.Timer timer = new javax.swing.Timer(5, this);
        timer.start();
        inv.loadInv();
        makeLoadOut();
    }

    public void passAmmo()
    {
        rc.pistolAmmo = ammos[0];
        rc.energyCell = ammos[1];
        rc.shotgunAmmo = ammos[2];
        rc.rifleAmmo = ammos[3];
    }

    public void createWindow()
    {
        frame = new JFrame("2517");
        //DisplayMode.BIT_DEPTH_MULTI
        DisplayMode displayMode = new DisplayMode(800,600,32,60);
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = environment.getDefaultScreenDevice();
        device.setFullScreenWindow(frame);
        device.setDisplayMode(displayMode);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setBackground(Color.BLACK);
        frame.getContentPane().add(this, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.addKeyListener(this);
    }

    public void createTankWindow(double x, double y)
    {
        space = new tankgame(x,y);
        passAmmo();
        frame.setPreferredSize(new Dimension(800, 600));
        space.addMouseListener(space);
        space.addMouseMotionListener(space);
        frame.getContentPane().remove(rc);
        frame.getContentPane().add(space, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.addKeyListener(space);
    }

    public void start()
    {
        rc = new GameEngine("startingprison",this);
        rc.loadOut = weapons;
        passAmmo();
        frame.setPreferredSize(new Dimension(800, 600));
        frame.getContentPane().remove(this);
        frame.getContentPane().add(rc, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.addKeyListener(rc);
    }

    public void createRCWindow(String level)
    {
        rc = new GameEngine(level,this);
        rc.loadOut = weapons;
        passAmmo();
        frame.setPreferredSize(new Dimension(800, 600));
        frame.getContentPane().remove(space);
        frame.getContentPane().add(rc, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.addKeyListener(rc);
    }

    public void pause(Component e)
    {
        inv.loadInv();
        if(rc!=null)ammos = rc.getAmmoAmounts();
        inv.setAmmoAmounts(ammos);
        invv = new InventoryView();
        pause = true;
        frame.setPreferredSize(new Dimension(800,600));
        frame.setBackground(Color.LIGHT_GRAY);
        frame.getContentPane().remove(e);
        frame.getContentPane().add(invv, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.addMouseMotionListener(invv);
        frame.addMouseListener(invv);
        frame.addKeyListener(invv);
    }   

    public void spaceUnpause()
    {
        invv.save();    
        inv = invv.playerInv;
        makeLoadOut(); 
        frame.setPreferredSize(new Dimension(800, 600));
        space.addMouseListener(space);
        space.addMouseMotionListener(space);
        frame.getContentPane().remove(invv);
        pause = false;        
        frame.getContentPane().add(space, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.addKeyListener(space);
    }

    public void makeLoadOut()
    {
        inv.loadInv();
        weapons = inv.getLoadOut();
        ammos = inv.getAmmoAmounts();
    }

    public void setTempCot(String scnabkbr,String contain, int wTyp)
    {
        stNm = scnabkbr; //URL
        tempCont = contain; //Container Name
        storOr = (byte)wTyp;
    }

    public void look()
    {
        //System.out.println(storOr);
        inv.loadInv();
        inv.setAmmoAmounts(ammos);
        if(storOr == 0){
            cont = new Container(tempCont,stNm);
            pause = true;
            frame.setPreferredSize(new Dimension(800,600));
            frame.setBackground(Color.LIGHT_GRAY);
            frame.getContentPane().remove(rc);
            frame.getContentPane().add(cont, BorderLayout.CENTER);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
            frame.addMouseMotionListener(cont);
            frame.addMouseListener(cont);
            frame.addKeyListener(cont);
            //System.out.println("udbsfiweb");
        }
        else if (storOr == 1){
            store = new Store(tempCont,stNm); ////ADD THE stNm
            pause = true;
            frame.setPreferredSize(new Dimension(800,600));
            frame.setBackground(Color.LIGHT_GRAY);
            frame.getContentPane().remove(rc);
            frame.getContentPane().add(store, BorderLayout.CENTER);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
            frame.addMouseMotionListener(store);
            frame.addMouseListener(store);
            frame.addKeyListener(store);
            //System.out.println("sdfiuasboufdbas");
        }

    }

    public void rcUnpause()
    {
        invv.save();
        savePosition();
        inv = invv.playerInv;
        passAmmo();
        makeLoadOut();
        while(invv.heal-->0) rc.health+=25;
        if(rc.health>200)rc.health=200;
        rc.loadOut = weapons;
        frame.setPreferredSize(new Dimension(800, 600));
        frame.getContentPane().remove(invv);
        pause = false;        
        frame.getContentPane().add(rc, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.addKeyListener(rc);
    }

    public void exit()
    {
        if(cont != null)
        {
            cont.save();
            inv = cont.playerInv;
            frame.getContentPane().remove(cont);
        }
        else if(store != null)
        {
            store.save();
            inv = store.playerInv;
            frame.getContentPane().remove(store);
        }
        makeLoadOut();
        rc.loadOut = weapons;
        frame.setPreferredSize(new Dimension(800, 600));
        pause = false;        
        frame.getContentPane().add(rc, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.addKeyListener(rc);
    }

    public void keyTyped(KeyEvent key) {}

    public void keyPressed(KeyEvent key) { processKey(key.getKeyCode(), true); }

    public void keyReleased(KeyEvent key) { processKey(key.getKeyCode(), false); }

    public void actionPerformed(ActionEvent e)
    {
        if(splash)
        {
            if(keys[UP] || keys[DOWN])select=!select;
            if(keys[SPACE])
            {
                splash = false;
                if(select)start();
                else loadPosition();
            }
            keys[UP] = false;
            keys[DOWN] = false;
            x+=3;
            y+=1.5;
            repaint();
        }
        else
        {
            if(space != null && space.get != null)
            {
                inv.loadInv();
                inv.addItem(space.get);
                invv.save();
                inv.loadInv();
                space.get = null;
            }
            if(space != null && space.pause == true && pause == false)
            {
                pause(space);
            }
            if(rc != null && rc.paused == true && pause == false)
            {
                pause(rc);
            }
            if(rc != null && rc.look == true && pause == false)
            {
                look();
            }
            if(invv != null && invv.on == false && pause == true)
            {
                invv.save();
                inv.loadInv();
                if(space!=null)
                {
                    spaceUnpause();
                    space.pause = false;
                }
                if(rc!=null)
                {
                    rcUnpause();
                    rc.paused = false;
                }
                invv = null;
            }
            if((cont != null && cont.on == false )|| (store != null && store.on == false))
            {
                exit();
                rc.look = false;
                cont = null;
                store = null;
            }
            if(space != null && space.on == false)
            {
                createRCWindow(space.level);
                makeLoadOut();
                lastx = space.x;
                lasty = space.y;
                lasthp = space.tank.health;
                lastfhp = space.tank.fhealth;
                space = null;
            }
            else if(rc != null && rc.on == false)
            {
                createTankWindow(lastx,lasty);
                space.tank.health = lasthp;
                space.tank.fhealth = lastfhp;
                rc = null;
            }
        }
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0,0,800,600);
        for(Star star : stars)
        {
            star.draw(g,x,y);
        }
        for(Planet planet : planets)
        {
            planet.draw(g,x,y);
        }
        splashimg.draw(g);
        if(select)start.draw(g);
        else continu.draw(g);
        if(!splash)
        {
            load.draw(g);
        }
    }
    
    public void savePosition()
    {
        try{
            File dir = new File(folder);
            dir.mkdir();

            //System.out.println("SAVE");

            PrintWriter clear = new PrintWriter(folder + "/" + "position",code);
            clear.println("");
            clear.close();

            PrintWriter invWriter = new PrintWriter(folder + "/" + "position",code);
            
            invWriter.println(rc.levelName);
            invWriter.println(rc.posX);
            invWriter.println(rc.posY);
            
            invWriter.close();
            

        }catch(IOException e){}
    }
    
    public void loadPosition()
    {
        try{

            //File data = new File(folder "/ " + name);
            String fileDir = String.format("%s/%s",folder,"position");
            Scanner file = new Scanner(new File(fileDir));
           
            
            rc = new GameEngine(file.nextLine(),this);
            rc.posX = (float)(file.nextDouble());
            file.nextLine();
            rc.posY = (float)(file.nextDouble());
            
            
            

        }catch(IOException e){start();}
        passAmmo();
        frame.setPreferredSize(new Dimension(800, 600));
        frame.getContentPane().remove(this);
        frame.getContentPane().add(rc, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.addKeyListener(rc);
    }
    

    public void processKey(int code, boolean pressed)
    {
        switch(code)
        {
            case 38: keys[UP] = pressed; break;
            case 40: keys[DOWN] = pressed; break; 
            case 32: keys[SPACE] = pressed; break; 
        }
    }

    public static void main (String [] args)
    {
        new Title();
    }
}
