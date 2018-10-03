import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

public class Container extends JPanel implements ActionListener,
MouseMotionListener, MouseListener, KeyListener
{

    Font impact = new Font("Impact", Font.BOLD, 35);
    Font dialog = new Font("Dialog", Font.BOLD, 12);

    Inventory playerInv  = new Inventory("playerInv");
    Inventory cargo= new Inventory("cargoHold",9,13);
    Inventory container;

    Rectangle charTab = new Rectangle(0,0,245,80);
    Rectangle cargoHold = new Rectangle(246,0,245,80);
    Rectangle deposit = new Rectangle(491,0,245,80);
    Rectangle saveButton = new Rectangle(736,0,50,80);
    Img saveImg = new Img("save");

    boolean character = false, buying = true;
    boolean save= false;

    int size = 60;
    int invTopx = 0;
    int invTopy = 51;

    int invR = playerInv.rows;
    int invC = playerInv.columns;
    int containerR,containerC;
    int cargoR = cargo.rows;
    int cargoC = cargo.columns;

    String [] displayStats;

    int selectR, selectC, lastSelectR, lastSelectC;

    boolean done = false;
    boolean swap = false;

    private int mousex,mousey,r,c;

    boolean [] key = new boolean[4];

    String URL;

    final static int UP = 0;
    final static int DOWN = 1;
    final static int LEFT = 2;
    final static int RIGHT = 3;
    boolean on = true;
    String exit = "exit";
    private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;

    public Container()
    {
        container = new Inventory("container");

        containerR = container.rows;
        containerC = container.columns;

        Timer timer = new Timer(5,this);
        timer.start();

        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("SPACE"),exit);
        this.getActionMap().put(exit, new exit(this));

        populateInv();
    }

    public Container(Inventory container)
    {
        this.container = container;

        containerR = container.rows;
        containerC = container.columns;

        setPreferredSize(new Dimension(800,600));
        setBackground(Color.LIGHT_GRAY);
        //JFrame frame = new JFrame("painter");
        //frame.getContentPane().add(this);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.pack();
        //frame.setVisible(true);

        addMouseMotionListener(this);
        addMouseListener(this);
        //frame.addKeyListener(this);

        Timer timer = new Timer(5,this);
        timer.start();

        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("SPACE"),exit);
        this.getActionMap().put(exit, new exit(this));

        populateInv();
    }

    public Container(String container)
    {
        this.container = new Inventory(container);

        containerR = this.container.rows;
        containerC = this.container.columns;

        setPreferredSize(new Dimension(800,600));
        setBackground(Color.LIGHT_GRAY);
        //JFrame frame = new JFrame("painter");
        //frame.getContentPane().add(this);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.pack();
        //frame.setVisible(true);

        addMouseMotionListener(this);
        addMouseListener(this);
        //frame.addKeyListener(this);

        Timer timer = new Timer(5,this);
        timer.start();

        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("SPACE"),exit);
        this.getActionMap().put(exit, new exit(this));

        populateInv();
    }

    public Container(String container, String URL)
    {
        this.container = new Inventory(container);

        containerR = this.container.rows;
        containerC = this.container.columns;

        setPreferredSize(new Dimension(800,600));
        setBackground(Color.LIGHT_GRAY);
        //JFrame frame = new JFrame("painter");
        //frame.getContentPane().add(this);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.pack();
        //frame.setVisible(true);

        addMouseMotionListener(this);
        addMouseListener(this);
        //frame.addKeyListener(this);

        Timer timer = new Timer(5,this);
        timer.start();

        //this.container.folder = URL;
        this.URL = URL;
        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("SPACE"),exit);
        this.getActionMap().put(exit, new exit(this));

        populateInv();
    }

    public void populateInv()
    {
        playerInv.loadInv();
        cargo.loadInv();
        loadInv();

        /*container.addItem(new Stackable("Guadium Spice",100));
        container.addItem(new Stackable("Polychromite",100));
        container.addItem(new Stackable("Ununseptium",100));
        container.addItem(new Stackable("Exotic Plant1",100));
        container.addItem(new Stackable("Exotic Plant2",100));
        container.addItem(new Stackable("Polemian Spice",100));

        String[] HK51 = new String[]{"HK51","HK51","15","2","4","130","10","13000"};
        String[] glock = new String[]{"Glock","Glock","5","2","3","80","8","7500"};
        String[] ak = new String[]{"AK","AK","24","1","4","180","22","22500"};
        cargo.addItem(new Weapon(HK51));
        cargo.addItem(new Weapon(glock));
        cargo.addItem(new Weapon(ak));
         */
    }

    public void paintComponent (Graphics g)
    {
        super.paintComponent(g);

        g.setFont(dialog);

        g.setColor(Color.GRAY.brighter());
        g.fillRect(0,0,245,50);
        g.setColor(Color.CYAN);
        g.fillRect(246,0,245,50);
        g.setColor(Color.MAGENTA);
        g.fillRect(491,0,245,50);

        saveImg.setPosition(736,0);
        saveImg.draw(g);
        g.setColor(Color.GRAY.darker());

        if(!buying)
        {
            if(character)
            {
                drawChar(g); 
            }
            else
            {
                drawCargo(g);
            }

        }
        else
        {
            drawContainer(g);
        }

        g.setFont(impact);
        g.setColor(Color.GRAY.darker());

        g.drawString("Player",75,35);
        g.drawString("Cargo",321,35);
        g.drawString("Container",548,35);
    }

    public void drawContainer(Graphics g)
    {
        g.setColor(Color.GRAY.darker());
        g.drawRect(491,0,245,50);

        for(int r = containerR - 1; r >= 0; r--)
        {
            for(int c = 0; c < containerC; c++)
            { 
                g.drawRect(invTopx + (c*size), invTopy + (r*size), size, size);
            }
        }

        for(int r = 0; r < containerR; r++)
        {
            for(int c = 0; c < containerC; c++)
            {
                if(container.checkItem(r,c))
                {                   
                    container.getItem(r,c).draw(g,invTopx + (c*size), invTopy + (r*size));
                }
            }
        }

        if(selectR >= 0 && selectC >= 0 && selectR < containerR  && selectC < containerC)
        {
            g.setColor(Color.BLUE);
            g.drawRect(invTopx + (selectC*size), invTopy + (selectR*size), size,size);
            shopArmorStatsDraw(g);
        }
        //drawAmmo(g);

        if(swap && selectR > -1 && selectC >-1 && selectR < containerR  && selectC < containerC && container.checkItem(lastSelectR,lastSelectC))
        {
            container.getItem(lastSelectR,lastSelectC).draw(g,mousex - 30,mousey - 30);
        } 
    }

    public void drawChar(Graphics g)
    {  
        g.setColor(Color.GRAY.darker());
        g.drawRect(0,0,245,50);

        for(int r = invR - 1; r >= 0; r--)
        {
            for(int c = 0; c < invC; c++)
            {
                if(r == 0)
                {g.setColor(Color.RED);}
                else
                    g.setColor(Color.GRAY.darker());
                g.drawRect(invTopx + (c*size), invTopy + (r*size), size, size);
            }
        }

        for(int r = 0; r < invR; r++)
        {
            for(int c = 0; c < invC; c++)
            {
                if(playerInv.checkItem(r,c))
                {                   
                    playerInv.getItem(r,c).draw(g,invTopx + (c*size), invTopy + (r*size));
                }
            }
        }

        if(selectR >= 0 && selectC >= 0 && selectR < invR  && selectC < invC)
        {
            g.setColor(Color.BLUE);
            g.drawRect(invTopx + (selectC*size), invTopy + (selectR*size), size,size);
            armorStatsDraw(g);
        }
        //drawAmmo(g);

        if(selectC > -1 && selectR > -1 && swap && selectR < invR  && selectC < invC && playerInv.checkItem(lastSelectR,lastSelectC))
        {
            playerInv.getItem(lastSelectR,lastSelectC).draw(g,mousex - 30,mousey - 30);
        } 
        drawAmmo(g);

    }

    public void armorStatsDraw(Graphics g)
    {
        g.setColor(Color.RED);
        if(!playerInv.checkItem(selectR,selectC))
        {
            g.drawString("There is no item here",625, 101);
        }
        else if(playerInv.getItem(selectR,selectC) instanceof Weapon)
        {
            //draw item stats
            displayStats = playerInv.getItem(selectR,selectC).getData();
            String name = "Name: " + displayStats[0];
            String damage = "Damage: " + displayStats[2];
            String RoF = "Rate of Fire: " + displayStats[3];
            String reload = "Reload Time: " + displayStats[4];
            String range = "Range: " + displayStats[5];
            String magSize = "Mag Size: " + displayStats[6];

            g.drawString(name,625, 101);
            g.drawString(damage,625, 151);
            g.drawString(RoF,625, 201);
            g.drawString(reload,625, 251);
            g.drawString(range,625, 301);
            g.drawString(magSize,625, 351);

        }  
        else if(playerInv.getItem(selectR,selectC) instanceof Armor)
        {
            //draw item stats
            displayStats = playerInv.getItem(selectR,selectC).getData();
            String name = "Name: " + displayStats[0];
            String life = "Bonus Life: " + displayStats[2];
            String absorption = "Absorption: " + displayStats[3];

            g.drawString(name,625, 101);
            g.drawString(life,625, 151);
            g.drawString(absorption,625, 201);
        }
        else if(playerInv.getItem(selectR,selectC) instanceof Stackable)
        {
            g.drawString(playerInv.getItem(selectR,selectC).getType(),675, 101);
        }

    }

    public void shopArmorStatsDraw(Graphics g)
    {
        g.setColor(Color.RED);
        if(!container.checkItem(selectR,selectC))
        {
            g.drawString("There is no item here",625, 101);
        }
        else if(container.getItem(selectR,selectC) instanceof Weapon)
        {
            //draw item stats
            displayStats = container.getItem(selectR,selectC).getData();
            String name = "Name: " + displayStats[0];
            String damage = "Damage: " + displayStats[2];
            String RoF = "Rate of Fire: " + displayStats[3];
            String reload = "Reload Time: " + displayStats[4];
            String range = "Range: " + displayStats[5];
            String magSize = "Mag Size: " + displayStats[6];

            g.drawString(name,625, 101);
            g.drawString(damage,625, 151);
            g.drawString(RoF,625, 201);
            g.drawString(reload,625, 251);
            g.drawString(range,625, 301);
            g.drawString(magSize,625, 351);

        }  
        else if(container.getItem(selectR,selectC) instanceof Armor)
        {
            //draw item stats
            displayStats = container.getItem(selectR,selectC).getData();
            String name = "Name: " + displayStats[0];
            String life = "Bonus Life: " + displayStats[2];
            String absorption = "Absorption: " + displayStats[3];

            g.drawString(name,625, 101);
            g.drawString(life,625, 151);
            g.drawString(absorption,625, 201);
        }
        else if(container.getItem(selectR,selectC) instanceof Stackable)
        {
            g.drawString(container.getItem(selectR,selectC).getType(),675, 101);
        }

    }

    public void drawCargo(Graphics g)
    {
        g.drawRect(246,0,245,50);

        for(int r = 0; r < cargoR; r++)
        {
            for(int c = 0; c < cargoC; c++)
            {
                g.setColor(Color.GRAY.darker());
                g.drawRect(invTopx + (c*size), invTopy + (r*size), size, size);
                if(cargo.checkItem(r,c))
                {
                    cargo.getItem(r,c).draw(g,invTopx + (c*size), invTopy + (r*size));
                }
            }
        }

        if(selectR < cargoR  && selectC < cargoC)
        {
            g.setColor(Color.BLUE);
            g.drawRect(invTopx + (selectC*size), invTopy + (selectR*size), size,size);
            if(selectR >= 0 && selectC >= 0 && cargo.checkItem(selectR,selectC))
            {
                g.setColor(Color.RED);
                g.drawString(cargo.getItem(selectR,selectC).getData()[0],50,580);
            }
            // armorStatsDraw(g);
        }

        if(swap && selectR > -1 && selectC >-1 && selectR < cargoR  && selectC < cargoC && cargo.checkItem(lastSelectR,lastSelectC))
        {
            cargo.getItem(lastSelectR,lastSelectC).draw(g,mousex - 30,mousey - 30);
        }

    }

    public void drawAmmo(Graphics g)
    {
        //draw resources
        g.setColor(Color.RED);
        displayStats = playerInv.getStackableAmounts();
        String money = "Money: " + displayStats[0];
        String pistol = "Pistol Ammo: " + displayStats[1];
        String energy = "Energy Cells: " + displayStats[2];
        String shotgun = "Shotgun Shells: " + displayStats[3];
        String intermidiate = "Intermidiate Rounds: " + displayStats[4];
        String rifle = "Rifle Ammo: " + displayStats[5];
        g.drawString(money,10,495);
        g.drawString(pistol,10,515);
        g.drawString(energy,10,535);
        g.drawString(shotgun,10,555);
        g.drawString(intermidiate,10,575);
        g.drawString(rifle,10,595);
    }

    public void mouseDragged(MouseEvent mouse)
    {
        swap = true;

        mousex = mouse.getX();
        mousey = mouse.getY();

        repaint();
    }

    public void mouseEntered(MouseEvent e){}

    public void mouseExited(MouseEvent e){}

    public void mouseClicked(MouseEvent e){}

    public void mousePressed(MouseEvent mouse)
    {

        mousex = mouse.getX();
        mousey = mouse.getY();
        selectC = (int)((mousex)/size);
        selectR = (int)((mousey-invTopy-30)/size);
        lastSelectR = selectR;
        lastSelectC = selectC;

        //added = false;

        if(charTab.contains(mousex,mousey))
        {
            character = true;
            buying = false;
        }
        else if(cargoHold.contains(mousex,mousey))
        {
            character = false;
            buying = false;
        }
        else if(deposit.contains(mousex,mousey))
        {
            buying = true;
            character = false;
        }
        else if(saveButton.contains(mousex,mousey))
        {
            save = true;
        }

        if(SwingUtilities.isRightMouseButton(mouse))
        {
            if(!buying)
            {
                if(character && playerInv.checkItem(selectR,selectC))
                {
                    container.addItem(playerInv.getItem(selectR,selectC));
                    playerInv.removeItem(selectR,selectC);
                }
                if(!character && cargo.checkItem(selectR,selectC))
                {
                    container.addItem(cargo.getItem(selectR,selectC));
                    cargo.removeItem(selectR,selectC);
                }
            }
            else if(buying && container.checkItem(selectR,selectC))
            {
                playerInv.addItem(container.getItem(selectR,selectC));
                container.removeItem(selectR,selectC);
            }
        }

        repaint();
    }

    public void mouseMoved(MouseEvent mouse){}

    public void mouseReleased(MouseEvent mouse)
    {
        mousex = mouse.getX();
        mousey = mouse.getY();
        selectC = (int)((mousex)/size);
        selectR = (int)((mousey-invTopy-30)/size);

        if(!buying)
        {
            if(character)
            {
                if( swap && cargoHold.contains(mousex,mousey))
                {
                    cargo.addItem(playerInv.getItem(lastSelectR,lastSelectC));
                    playerInv.removeItem(lastSelectR,lastSelectC);
                    //added = true;
                }
                else if(swap && deposit.contains(mousex,mousey))
                {
                    container.addItem(playerInv.getItem(lastSelectR,lastSelectC));
                    playerInv.removeItem(lastSelectR,lastSelectC);
                }
                else if(character && selectR > -1 && selectC >-1 && selectR < invR  && selectC < invC && lastSelectR < invR  && lastSelectC < invC 
                && selectR > -1 && selectC >-1 && playerInv.getItem(lastSelectR,lastSelectC) instanceof Stackable && playerInv.getItem(selectR,selectC) instanceof Stackable
                && playerInv.getItem(lastSelectR,lastSelectC).category ==  playerInv.getItem(selectR,selectC).category
                && !(lastSelectR == selectR && lastSelectC == selectC))
                {
                    Stackable a =  new Stackable(playerInv.getItem(selectR,selectC).getData());
                    Stackable s =   new Stackable(playerInv.getItem(lastSelectR,lastSelectC).getData());
                    a.merge(s);
                    playerInv.setItem(selectR,selectC,a);
                    playerInv.removeItem(lastSelectR,lastSelectC);
                }
                else if( selectR > -1 && selectC >-1 && character && selectR < invR  && selectC < invC && lastSelectR < invR  && lastSelectC < invC)
                    playerInv.swapItems(selectR,selectC,lastSelectR,lastSelectC);
            }
            else if(!character)
            {

                if(swap && charTab.contains(mousex,mousey))
                {
                    playerInv.addItem(cargo.getItem(lastSelectR,lastSelectC));
                    cargo.removeItem(lastSelectR,lastSelectC);
                }
                else if(swap && deposit.contains(mousex,mousey))
                {
                    container.addItem(cargo.getItem(lastSelectR,lastSelectC));
                    cargo.removeItem(lastSelectR,lastSelectC);
                }
                else if(!character && selectR > -1 && selectC >-1 && selectR < cargoR  && selectC < cargoC && lastSelectR < cargoR  && lastSelectC < cargoC 
                && cargo.getItem(lastSelectR,lastSelectC) instanceof Stackable && cargo.getItem(selectR,selectC) instanceof Stackable
                && cargo.getItem(lastSelectR,lastSelectC).category ==  cargo.getItem(selectR,selectC).category
                && !(lastSelectR == selectR && lastSelectC == selectC))
                {
                    Stackable a =  new Stackable(cargo.getItem(selectR,selectC).getData());
                    Stackable s =   new Stackable(cargo.getItem(lastSelectR,lastSelectC).getData());
                    a.merge(s);
                    cargo.setItem(selectR,selectC,a);
                    cargo.removeItem(lastSelectR,lastSelectC);
                }

                else if( !character && selectR > -1 && selectC >-1 &&selectR < cargoR  && selectC < cargoC && lastSelectR < cargoR  && lastSelectC < cargoC)
                    cargo.swapItems(selectR,selectC,lastSelectR,lastSelectC);
            }
        }
        else if(buying)
        {

            if(swap && charTab.contains(mousex,mousey))
            {
                playerInv.addItem(container.getItem(lastSelectR,lastSelectC));
                container.removeItem(lastSelectR,lastSelectC);
            }
            else if(swap && cargoHold.contains(mousex,mousey))
            {
                cargo.addItem(container.getItem(lastSelectR,lastSelectC));
                container.removeItem(lastSelectR,lastSelectC);
            }
            else if(!character && selectR > -1 && selectC >-1 && selectR < containerR  && selectC < containerC && lastSelectR < containerR  && lastSelectC < containerC 
            && container.getItem(lastSelectR,lastSelectC) instanceof Stackable && container.getItem(selectR,selectC) instanceof Stackable
            && container.getItem(lastSelectR,lastSelectC).category ==  container.getItem(selectR,selectC).category
            && !(lastSelectR == selectR && lastSelectC == selectC))
            {
                Stackable a =  new Stackable(container.getItem(selectR,selectC).getData());
                Stackable s =   new Stackable(container.getItem(lastSelectR,lastSelectC).getData());
                a.merge(s);
                container.setItem(selectR,selectC,a);
                container.removeItem(lastSelectR,lastSelectC);
            }

            else if( !character && selectR > -1 && selectC >-1 && selectR < containerR  && selectC < containerC && lastSelectR < containerR  && lastSelectC < containerC)
                container.swapItems(selectR,selectC,lastSelectR,lastSelectC);

        }
        if(save)
        {
            playerInv.saveInv();
            cargo.saveInv();
            saveInv();
            save = false;
        }

        selectC = (int)((mousex)/size);
        selectR = (int)((mousey-invTopy-30)/size);
        swap = false;
    }

    public void save()
    {
        playerInv.saveInv();
        cargo.saveInv();
        saveInv();
        //System.out.println("Container is Saving");
    }

    public void keyTyped(KeyEvent key) {}

    public void keyPressed(KeyEvent key) {}

    public void keyReleased(KeyEvent key) 
    {
        done = false;
    }

    public void actionPerformed(ActionEvent e)
    {        
        repaint();
    }

    public void loadInv()
    {
        try{

            //File data = new File(folder "/ " + name);
            //String fileDir = String.format("%s/%s",container.folder,container.name);
            Scanner file = new Scanner(new File(URL));
            file.nextLine(); //skip the initial value
            container.rows = 7;
            container.columns = 10;
            file.nextLine(); // skip r c because we know what they are
            file.nextLine();
            if(file.hasNextLine()){

                //file.nextLine();
                String[] rc;
                container.loadStackables(file);
                while(file.hasNextLine())
                {
                    int iType = file.nextInt();
                    file.nextLine();
                    if(iType == 1) 
                    {
                        Weapon w = new Weapon(file.nextLine().split(","));
                        rc = file.nextLine().split(",");
                        container.setItem(Integer.parseInt(rc[0]),Integer.parseInt(rc[1]),w);
                    }
                    else if(iType == 2)
                    {
                        Armor a = new Armor(file.nextLine().split(","));
                        rc = file.nextLine().split(",");
                        container.setItem(Integer.parseInt(rc[0]),Integer.parseInt(rc[1]),a);
                    }
                    else if(iType == 3)
                    {
                        Stackable s = new Stackable(file.nextLine().split(","));
                        rc = file.nextLine().split(",");
                        container.setItem(Integer.parseInt(rc[0]),Integer.parseInt(rc[1]),s);
                    }
                }
            }
        }catch(IOException e){}
    }

    public void saveInv()
    {
        //System.out.println("trying");
        try{
            //File dir = new File(container.folder);
            //dir.mkdir();

            //PrintWriter clear = new PrintWriter(container.folder + "/" + container.name,container.code);
            PrintWriter clear = new PrintWriter(URL,container.code);
            clear.println("");
            clear.close();

            //PrintWriter invWriter = new PrintWriter(container.folder + "/" + container.name,container.code);
            PrintWriter invWriter = new PrintWriter(URL,container.code);
            invWriter.println(0);  // tell the program that it's a container
            invWriter.println(container.rows);
            invWriter.println(container.columns);

            for(int i = 0; i < 6; i++)
            {
                invWriter.println(container.stackables[i].toString());
            }

            for(int r =0; r < container.rows; r++)
            {
                for(int c = 0; c < container.columns; c++)
                {
                    if(container.checkItem(r,c))
                    {
                        invWriter.println(container.items[r][c].iType);
                        invWriter.println(container.items[r][c].toString());
                        invWriter.printf("%d,%d\n",r,c);
                    }
                }
            }
            invWriter.close();
            //System.out.println("saved");

        }catch(IOException e){e.printStackTrace();}
    }
}

class exit extends AbstractAction
{
    private Container myInv;

    public exit(Container myInv)
    {
        this.myInv = myInv;
    }

    public void actionPerformed(ActionEvent e) 
    {
        myInv.on = false;
    }

}