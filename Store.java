import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

public class Store extends JPanel implements ActionListener,
MouseMotionListener, MouseListener, KeyListener
{

    Font impact = new Font("Impact", Font.BOLD, 35);
    Font dialog = new Font("Dialog", Font.BOLD, 12);

    Inventory playerInv  = new Inventory("playerInv");
    Inventory cargo= new Inventory("cargoHold",9,13);
    Inventory store;

    Rectangle charTab = new Rectangle(0,0,245,50);
    Rectangle cargoHold = new Rectangle(246,0,245,50);
    Rectangle deposit = new Rectangle(491,0,245,50);
    Rectangle saveButton = new Rectangle(736,0,50,50);
    Img saveImg = new Img("save");

    Scanner kybd = new Scanner(System.in);

    boolean character = false, buying = true;
    boolean save= false;
    boolean on = true;

    int size = 60;
    int invTopx = 0;
    int invTopy = 51;

    String leave = "leave";
    String URL;
    private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;

    int invR = playerInv.rows;
    int invC = playerInv.columns;
    int storeR,storeC;
    int cargoR = cargo.rows;
    int cargoC = cargo.columns;

    String [] displayStats;

    double priceChange;

    int selectR, selectC, lastSelectR, lastSelectC;

    boolean done = false;
    boolean swap = false;
    boolean sold = false;

    private int mousex,mousey,r,c;

    boolean [] key = new boolean[4];

    final static int UP = 0;
    final static int DOWN = 1;
    final static int LEFT = 2;
    final static int RIGHT = 3;

    public Store()
    {
        store = new Inventory("store");

        priceChange =  0.1;
        storeR = store.rows;
        storeC = store.columns;

        setPreferredSize(new Dimension(800,600));
        setBackground(Color.LIGHT_GRAY);
        JFrame frame = new JFrame("painter");
        frame.getContentPane().add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        addMouseMotionListener(this);
        addMouseListener(this);
        frame.addKeyListener(this);

        Timer timer = new Timer(5,this);
        timer.start();

        populateInv();
    }

    public Store(Inventory store)
    {
        this.store = store;

        priceChange = 0.1;

        storeR = store.rows;
        storeC = store.columns;

        setPreferredSize(new Dimension(800,600));
        setBackground(Color.LIGHT_GRAY);
        JFrame frame = new JFrame("painter");
        frame.getContentPane().add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        addMouseMotionListener(this);
        addMouseListener(this);
        frame.addKeyListener(this);

        Timer timer = new Timer(5,this);
        timer.start();

        populateInv();
    }

    public Store(String store, String URL)
    {
        this.store = new Inventory(store);

        priceChange = 0.1;

        storeR = this.store.rows;
        storeC = this.store.columns;

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

        
        this.URL = URL;
        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("SPACE"),leave);
        this.getActionMap().put(leave, new leave(this));

        populateInv();
    }

    public Store(String store, Double priceMod)
    {
        this.store = new Inventory(store);

        priceChange = priceMod;

        storeR = this.store.rows;
        storeC = this.store.columns;

        setPreferredSize(new Dimension(800,600));
        setBackground(Color.LIGHT_GRAY);
        JFrame frame = new JFrame("painter");
        frame.getContentPane().add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        addMouseMotionListener(this);
        addMouseListener(this);
        frame.addKeyListener(this);

        Timer timer = new Timer(5,this);
        timer.start();

        populateInv();
    }

    public void populateInv()
    {
        playerInv.loadInv();
        cargo.loadInv();
        loadInv();

        /* store.addItem(new Stackable("Guadium Spice",100));
        store.addItem(new Stackable("Polychromite",100));
        store.addItem(new Stackable("Ununseptium",100));
        store.addItem(new Stackable("Exotic Plant1",100));
        store.addItem(new Stackable("Exotic Plant2",100));
        store.addItem(new Stackable("Polemian Spice",100));

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
            drawStore(g);
        }

        g.setFont(impact);
        g.setColor(Color.GRAY.darker());

        g.drawString("Player",75,35);
        g.drawString("Cargo",321,35);
        g.drawString("Store",558,35);
    }

    public void drawStore(Graphics g)
    {
        g.setColor(Color.GRAY.darker());
        g.drawRect(491,0,245,50);

        for(int r = storeR - 1; r >= 0; r--)
        {
            for(int c = 0; c < storeC; c++)
            { 
                g.drawRect(invTopx + (c*size), invTopy + (r*size), size, size);
            }
        }

        for(int r = 0; r < storeR; r++)
        {
            for(int c = 0; c < storeC; c++)
            {
                if(store.checkItem(r,c))
                {                   
                    store.getItem(r,c).draw(g,invTopx + (c*size), invTopy + (r*size));
                }
            }
        }

        if(selectR >= 0 && selectC >= 0 && selectR < storeR  && selectC < storeC)
        {
            g.setColor(Color.BLUE);
            g.drawRect(invTopx + (selectC*size), invTopy + (selectR*size), size,size);
            shopArmorStatsDraw(g);
        }
        //drawAmmo(g);

        if(swap && selectR < storeR  && selectC < storeC && store.checkItem(lastSelectR,lastSelectC))
        {
            store.getItem(lastSelectR,lastSelectC).draw(g,mousex - 30,mousey - 30);
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

        if(swap && selectR < invR  && selectC < invC && playerInv.checkItem(lastSelectR,lastSelectC))
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
            int cost = (int)(playerInv.getItem(selectR,selectC).price - (playerInv.getItem(selectR,selectC).price * priceChange));
            //draw item stats
            displayStats = playerInv.getItem(selectR,selectC).getData();
            String name = "Name: " + displayStats[0];
            String damage = "Damage: " + displayStats[2];
            String RoF = "Rate of Fire: " + displayStats[3];
            String reload = "Reload Time: " + displayStats[4];
            String range = "Range: " + displayStats[5];
            String magSize = "Mag Size: " + displayStats[6];
            String pricing = "Price: " + cost;

            g.drawString(name,625, 101);
            g.drawString(damage,625, 151);
            g.drawString(RoF,625, 201);
            g.drawString(reload,625, 251);
            g.drawString(range,625, 301);
            g.drawString(magSize,625, 351);
            g.drawString(pricing,625,401);

        }  
        else if(playerInv.getItem(selectR,selectC) instanceof Armor)
        {
            int cost = (int)(playerInv.getItem(selectR,selectC).price - (playerInv.getItem(selectR,selectC).price * priceChange));
            //draw item stats
            displayStats = playerInv.getItem(selectR,selectC).getData();
            String name = "Name: " + displayStats[0];
            String life = "Bonus Life: " + displayStats[2];
            String absorption = "Absorption: " + displayStats[3];
            String pricing = "Price: " + cost;

            g.drawString(name,625, 101);
            g.drawString(life,625, 151);
            g.drawString(absorption,625, 201);
        }
        else if(playerInv.getItem(selectR,selectC) instanceof Stackable)
        {
            int cost = (int)(playerInv.getItem(selectR,selectC).price - (playerInv.getItem(selectR,selectC).price * priceChange));
            String pricing = "Price: " + cost;
            g.drawString(playerInv.getItem(selectR,selectC).getType(),675, 101);
            g.drawString(pricing,665,151);
        }

    }

    public void shopArmorStatsDraw(Graphics g)
    {
        g.setColor(Color.RED);
        if(!store.checkItem(selectR,selectC))
        {
            g.drawString("There is no item here",625, 101);
        }
        else if(store.getItem(selectR,selectC) instanceof Weapon)
        {
            int cost = (int)(store.getItem(selectR,selectC).price + (store.getItem(selectR,selectC).price * priceChange));
            //draw item stats
            displayStats = store.getItem(selectR,selectC).getData();
            String name = "Name: " + displayStats[0];
            String damage = "Damage: " + displayStats[2];
            String RoF = "Rate of Fire: " + displayStats[3];
            String reload = "Reload Time: " + displayStats[4];
            String range = "Range: " + displayStats[5];
            String magSize = "Mag Size: " + displayStats[6];
            String pricing = "Price: " + cost;

            g.drawString(name,625, 101);
            g.drawString(damage,625, 151);
            g.drawString(RoF,625, 201);
            g.drawString(reload,625, 251);
            g.drawString(range,625, 301);
            g.drawString(magSize,625, 351);
            g.drawString(pricing,625,401);

        }  
        else if(store.getItem(selectR,selectC) instanceof Armor)
        {
            int cost = (int)(store.getItem(selectR,selectC).price + (store.getItem(selectR,selectC).price * priceChange));
            //draw item stats
            displayStats = store.getItem(selectR,selectC).getData();
            String name = "Name: " + displayStats[0];
            String life = "Bonus Life: " + displayStats[2];
            String absorption = "Absorption: " + displayStats[3];
            String pricing = "Price: " + cost;

            g.drawString(name,625, 101);
            g.drawString(life,625, 151);
            g.drawString(absorption,625, 201);
        }
        else if(store.getItem(selectR,selectC) instanceof Stackable)
        {
            int cost = (int)(store.getItem(selectR,selectC).price + (store.getItem(selectR,selectC).price * priceChange));
            String pricing = "Price: " + cost;
            g.drawString(store.getItem(selectR,selectC).getType(),675, 101);
            g.drawString(pricing,665,151);
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

        if(swap && selectR < cargoR  && selectC < cargoC && cargo.checkItem(lastSelectR,lastSelectC))
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

    public void sell(Item e)
    {
        int sellCost = (int)(e.price - e.price * priceChange); 
        int buyCost = (int)(e.price + e.price * priceChange);
        sold = false;
        Item r;
        if(e instanceof Stackable)
            r = new Stackable(e.getType(),1);
        else if(e instanceof Weapon)
            r = new Weapon(e.getData());
        else if(e instanceof Armor)
            r = new Armor(e.getData());
        else
            r = e;
        if(buying && playerInv.getMoney() > sellCost)
        {
            playerInv.addItem(r);
            playerInv.pay(buyCost);
            sold = true;
        }
        else if(!buying)
        {
            store.addItem(r);
            playerInv.addMoney(sellCost);
            sold = true;
        }
    }

    public void sellAll(Item e)
    {
        int sellCost = (int)(e.price - e.price * priceChange); 
        int buyCost = (int)(e.price + e.price * priceChange);
        sold = false;
        if(buying && playerInv.getMoney() > sellCost * e.amount)
        {
            playerInv.addItem(e);
            playerInv.pay(buyCost * e.amount);
            sold = true;
        }
        else if(!buying)
        {
            store.addItem(e);
            playerInv.addMoney(sellCost * e.amount);
            sold = true;
        }       
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
        selectC = (int)(mousex/size);
        selectR = (int)((mousey-invTopy)/size);
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
                    if(playerInv.getItem(selectR,selectC) instanceof Stackable)
                    {
                        sell(playerInv.getItem(selectR,selectC));
                    }
                    else
                        sell(playerInv.getItem(selectR,selectC));
                    if(sold)playerInv.items[selectR][selectC].amount -= 1;
                    if(playerInv.items[selectR][selectC].amount == 0)
                        playerInv.removeItem(selectR,selectC);
                }
                if(!character && cargo.checkItem(selectR,selectC))
                {
                    if(cargo.getItem(selectR,selectC) instanceof Stackable)
                    {
                        sell(cargo.getItem(selectR,selectC));
                    }
                    else
                        sell(cargo.getItem(selectR,selectC));
                    if(sold)cargo.items[selectR][selectC].amount -= 1;
                    if(cargo.items[selectR][selectC].amount == 0)
                        cargo.removeItem(selectR,selectC);
                }
            }
            else if(buying && store.checkItem(selectR,selectC))
            {
                if(store.getItem(selectR,selectC) instanceof Stackable)
                {
                    sell(store.getItem(selectR,selectC));
                }
                else
                    sell(store.getItem(selectR,selectC));
                if(sold)store.items[selectR][selectC].amount -= 1;
                if(store.items[selectR][selectC].amount == 0)
                    store.removeItem(selectR,selectC);
            }
        }

        repaint();
    }

    public void mouseMoved(MouseEvent mouse){}

    public void mouseReleased(MouseEvent mouse)
    {
        mousex = mouse.getX();
        mousey = mouse.getY();
        selectC = (int)(mousex/size);
        selectR = (int)((mousey-invTopy)/size);

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
                    sellAll(playerInv.getItem(lastSelectR,lastSelectC));
                    if(sold)playerInv.removeItem(lastSelectR,lastSelectC);
                }
                else if(selectC > -1 && selectR > -1 && character && selectR < invR  && selectC < invC && lastSelectR < invR  && lastSelectC < invC 
                && playerInv.getItem(lastSelectR,lastSelectC) instanceof Stackable && playerInv.getItem(selectR,selectC) instanceof Stackable
                && playerInv.getItem(lastSelectR,lastSelectC).category ==  playerInv.getItem(selectR,selectC).category
                && !(lastSelectR == selectR && lastSelectC == selectC))
                {
                    Stackable a =  new Stackable(playerInv.getItem(selectR,selectC).getData());
                    Stackable s =   new Stackable(playerInv.getItem(lastSelectR,lastSelectC).getData());
                    a.merge(s);
                    playerInv.setItem(selectR,selectC,a);
                    playerInv.removeItem(lastSelectR,lastSelectC);
                }
                else if( character && selectR < invR  && selectC < invC && lastSelectR < invR  && lastSelectC < invC)
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
                    sellAll(cargo.getItem(lastSelectR,lastSelectC));
                    if(sold)cargo.removeItem(lastSelectR,lastSelectC);
                }
                else if(!character && selectC > -1 && selectR > -1 &&  selectR < cargoR  && selectC < cargoC && lastSelectR < cargoR  && lastSelectC < cargoC 
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

                else if( !character && selectR < cargoR  && selectC < cargoC && lastSelectR < cargoR  && lastSelectC < cargoC)
                    cargo.swapItems(selectR,selectC,lastSelectR,lastSelectC);
            }
        }
        else if(buying)
        {
            if(swap && charTab.contains(mousex,mousey))
            {
                sellAll(store.getItem(lastSelectR,lastSelectC));
                if(sold)store.removeItem(lastSelectR,lastSelectC);
            }
            else if(swap && cargoHold.contains(mousex,mousey))
            {
                sellAll(store.getItem(lastSelectR,lastSelectC));
                if(sold)store.removeItem(lastSelectR,lastSelectC);
            }
            else if(!character && selectR < storeR  && selectC > -1 && selectR > -1 &&  selectC < storeC && lastSelectR < storeR  && lastSelectC < storeC 
            && store.getItem(lastSelectR,lastSelectC) instanceof Stackable && store.getItem(selectR,selectC) instanceof Stackable
            && store.getItem(lastSelectR,lastSelectC).category ==  store.getItem(selectR,selectC).category
            && !(lastSelectR == selectR && lastSelectC == selectC))
            {
                Stackable a =  new Stackable(store.getItem(selectR,selectC).getData());
                Stackable s =   new Stackable(store.getItem(lastSelectR,lastSelectC).getData());
                a.merge(s);
                store.setItem(selectR,selectC,a);
                store.removeItem(lastSelectR,lastSelectC);
            }

            else if( !character && selectR < storeR  && selectC < storeC && lastSelectR < storeR  && lastSelectC < storeC)
                store.swapItems(selectR,selectC,lastSelectR,lastSelectC);
        }
        if(save)
        {
            playerInv.saveInv();
            cargo.saveInv();
            saveInv();
            save = false;
        }

        selectC = (int)(mousex/size);
        selectR = (int)((mousey-invTopy)/size);
        swap = false;
    }

    public void save()
    {
        playerInv.saveInv();
        cargo.saveInv();
        saveInv();
        //System.out.println("Store is Saving");
    }

    public void loadInv()
    {
      
        try{

            //File data = new File(folder "/ " + name);
            //String fileDir = String.format("%s/%s",store.folder,store.name);
            Scanner file = new Scanner(new File(URL));
            file.nextLine(); //skip the initial value
            store.rows = 7;
            store.columns = 10;
            if(file.hasNextLine()){
                file.nextLine(); // skip r c because we know what they are
                file.nextLine();

                //file.nextLine();

                String[] rc;

                store.loadStackables(file);

                while(file.hasNextLine())
                {
                    int iType = file.nextInt();
                    file.nextLine();
                    if(iType == 1) 
                    {
                        Weapon w = new Weapon(file.nextLine().split(","));
                        rc = file.nextLine().split(",");
                        store.setItem(Integer.parseInt(rc[0]),Integer.parseInt(rc[1]),w);
                    }
                    else if(iType == 2)
                    {
                        Armor a = new Armor(file.nextLine().split(","));
                        rc = file.nextLine().split(",");
                        store.setItem(Integer.parseInt(rc[0]),Integer.parseInt(rc[1]),a);
                    }
                    else if(iType == 3)
                    {
                        Stackable s = new Stackable(file.nextLine().split(","));
                        rc = file.nextLine().split(",");
                        store.setItem(Integer.parseInt(rc[0]),Integer.parseInt(rc[1]),s);
                    }
                }
            }
        }catch(IOException e){}
        
    }

    public void saveInv()
    {
        /*
        try{
            File dir = new File(store.folder);
            dir.mkdir();

            PrintWriter clear = new PrintWriter(store.folder + "/" + store.name,store.code);
            clear.println("");
            clear.close();

            PrintWriter invWriter = new PrintWriter(store.folder + "/" + store.name,store.code);
            invWriter.println(0);  // tell the program that it's a container
            invWriter.println(store.rows);
            invWriter.println(store.columns);

            for(int i = 0; i < 6; i++)
            {
                invWriter.println(store.stackables[i].toString());
            }

            for(int r =0; r < store.rows; r++)
            {
                for(int c = 0; c < store.columns; c++)
                {
                    if(store.checkItem(r,c))
                    {
                        invWriter.println(store.items[r][c].iType);
                        invWriter.println(store.items[r][c].toString());
                        invWriter.printf("%d,%d\n",r,c);
                    }
                }
            }
            invWriter.close();
            //System.out.println("saved");

        }catch(IOException e){}
        */
        try{
            //File dir = new File(store.folder);
            //dir.mkdir();

            //PrintWriter clear = new PrintWriter(store.folder + "/" + store.name,store.code);
            PrintWriter clear = new PrintWriter(URL,store.code);
            clear.println("");
            clear.close();

            //PrintWriter invWriter = new PrintWriter(store.folder + "/" + store.name,store.code);
            PrintWriter invWriter = new PrintWriter(URL,store.code);
            invWriter.println(1);  // tell the program that it's a store
            invWriter.println(store.rows);
            invWriter.println(store.columns);

            for(int i = 0; i < 6; i++)
            {
                invWriter.println(store.stackables[i].toString());
            }

            for(int r =0; r < store.rows; r++)
            {
                for(int c = 0; c < store.columns; c++)
                {
                    if(store.checkItem(r,c))
                    {
                        invWriter.println(store.items[r][c].iType);
                        invWriter.println(store.items[r][c].toString());
                        invWriter.printf("%d,%d\n",r,c);
                    }
                }
            }
            invWriter.close();
            //System.out.println("saved");

        }catch(IOException e){e.printStackTrace();}
    }

    public void keyTyped(KeyEvent key) {}

    public void keyPressed(KeyEvent key) {}

    public void keyReleased(KeyEvent key) {}

    public void actionPerformed(ActionEvent e)
    {        
        repaint();
    }
}
class leave extends AbstractAction
{
    private Store myInv;

    public leave(Store myInv)
    {
        this.myInv = myInv;
    }

    public void actionPerformed(ActionEvent e) 
    {
        myInv.on = false;
        //System.out.println("thank you");
    }

}
