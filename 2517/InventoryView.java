import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Scanner;
import java.util.ArrayList;

public class InventoryView extends JPanel implements ActionListener,
MouseMotionListener, MouseListener, KeyListener
{
    private int mousex,mousey,r,c;

    Font impact = new Font("Impact", Font.BOLD, 35);
    Font dialog = new Font("Dialog", Font.BOLD, 12);

    Inventory playerInv  = new Inventory("playerInv");
    Inventory cargo= new Inventory("cargoHold",9,13);

    boolean character = true;
    boolean save= false;
    int heal = 0;
    String[] displayStats;

    Rectangle charTab = new Rectangle(0,0,368,80);
    Rectangle cargoHold = new Rectangle(369,0,368,80);
    Rectangle saveButton = new Rectangle(736,0,50,80);

    int invTopx = 0;
    int invTopy = 51;
    int invR = playerInv.rows;
    int invC = playerInv.columns;
    int cargoR = cargo.rows;
    int cargoC = cargo.columns;

    int selectR, selectC;

    int lastSelectR, lastSelectC;
    int size = 60;

    /*final static int UP = 0;
    final static int DOWN = 1;
    final static int LEFT = 2;
    final static int RIGHT = 3;*/

    String up = "up";
    String down = "down";
    String left = "left";
    String right = "right";
    String one = "one";
    String two = "two";
    String three = "three";
    String four = "four";
    String five = "five";
    String six = "six";
    String seven = "seven";
    String eight = "eight";
    String nine = "nine";
    String zero = "zero";
    String q = "q";

    private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;

    boolean done = false;
    boolean swap = false;
    //boolean added = false;
    //final static int NUMBER = 4; 

    Img saveImg = new Img("save");
    boolean on = true;

    boolean [] key = new boolean[4];
    public InventoryView()
    {

        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("UP"), up);
        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("DOWN"), down);
        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("LEFT"), left);
        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("RIGHT"), right);
        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("1"),one);
        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("2"),two);
        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("3"),three);
        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("4"),four);
        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("5"),five);
        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("6"),six);
        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("7"),seven);
        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("8"),eight);
        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("9"),nine);
        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("0"),zero);
        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("Q"),q);

        this.getActionMap().put(up, new upAction(this));
        this.getActionMap().put(down, new downAction(this));
        this.getActionMap().put(left, new leftAction(this));
        this.getActionMap().put(right, new rightAction(this));
        this.getActionMap().put(one, new toolbarOne(this));
        this.getActionMap().put(two, new toolbarTwo(this));
        this.getActionMap().put(three, new toolbarThree(this));
        this.getActionMap().put(four, new toolbarFour(this));
        this.getActionMap().put(five, new toolbarFive(this));
        this.getActionMap().put(six, new toolbarSix(this));
        this.getActionMap().put(seven, new toolbarSeven(this));
        this.getActionMap().put(eight, new toolbarEight(this));
        this.getActionMap().put(nine, new toolbarNine(this));
        this.getActionMap().put(zero, new toolbarZero(this));
        this.getActionMap().put(q, new quit(this));

        Timer timer = new Timer(5,this);
        timer.start();

        populateInv();

    }

    public void paintComponent(Graphics g)
    {

        super.paintComponent(g);

        g.setFont(dialog);

        g.setColor(Color.BLUE);
        String coords = String.format("(%d,%d)", mousex, mousey);
        g.drawString(coords,20,20);
        String rowcol = String.format("(%d,%d)",c,r);
        g.drawString(rowcol,20,40);

        g.setColor(Color.CYAN);
        g.fillRect(0,0,375,50);
        g.setColor(Color.MAGENTA);
        g.fillRect(376,0,375,50);
        saveImg.setPosition(751,0);
        saveImg.draw(g);

        /*g.setColor(Color.BLUE);
        String select = String.format("%d,%d  %d,%d",selectC,selectR,lastSelectC,lastSelectR);
        g.drawString(select,10,10);*/

        g.setColor(Color.DARK_GRAY);
        if(character)
        {
            drawChar(g); 
        }
        else
        {
            drawCargo(g);
        }

        g.setFont(impact);
        g.setColor(Color.GRAY.darker());

        g.drawString("Character", 125, 35);
        g.drawString("Cargo Hold", 500 ,35);

    }

    public void drawChar(Graphics g)
    {
        g.drawRect(599,51,200,420);
        g.drawRect(0,0,375,50);   

        g.setColor(Color.GRAY.darker());
        g.drawLine(180,471,180,600);

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
        drawAmmo(g);

        if(swap && selectR < invR  && selectC < invC && playerInv.checkItem(lastSelectR,lastSelectC))
        {
            playerInv.getItem(lastSelectR,lastSelectC).draw(g,mousex - 30,mousey - 60);
        } 

    }

    public void drawCargo(Graphics g)
    {
        g.drawRect(376,0,375,50);

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

    public void drawAmmo(Graphics g)
    {
        //draw resources
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

    public void populateInv()
    {
        /*String[] Agram = new String[]{"Agram","Agram","14","3","5","60","24","7500"};
        String[] Mauser = new String[]{"Mauser","Mauser","9","2","2","100","10","8000"};

        Weapon agram = new Weapon(Agram);
        agram.update(3);
        Weapon mauser = new Weapon(Mauser);
        mauser.update(2);
        playerInv.addItem(agram);
        playerInv.addItem(mauser);
        for(int r = 0; r < invR-3; r++)
        {
        for(int c = 0; c < invC-3; c++)
        {
        playerInv.addItem(new Weapon(new String[]{"Agram","Agram","14","3","5","60","24","7500"}));
        playerInv.addItem(new Weapon(new String[]{"Mauser","Mauser","9","2","2","100","10","8000"}));
        }
        }
         */
        playerInv.loadInv();
        cargo.loadInv();

    }

    public void mouseDragged(MouseEvent mouse)
    {
        swap = true;

        mousex = mouse.getX();
        mousey = mouse.getY();

        repaint();
    }

    public void save()
    {
        playerInv.saveInv();
        cargo.saveInv();
        //System.out.println("Inv View is Saving");
    }

    public Weapon2[] getLoadOut()
    {
        return playerInv.getLoadOut();
    }

    public void mouseEntered(MouseEvent e){}

    public void mouseExited(MouseEvent e){}

    public void mouseClicked(MouseEvent e){}

    public void mousePressed(MouseEvent mouse)
    {

        mousex = mouse.getX();
        mousey = mouse.getY();
        selectC = (int)((mousex)/size);
        selectR = (int)((mousey-invTopy -30)/size);
        lastSelectR = selectR;
        lastSelectC = selectC;

        //added = false;
        if(SwingUtilities.isRightMouseButton(mouse))
        {
            //System.out.println("healing trying");
            if(character && playerInv.getItem(selectR,selectC).baseName.equals("Health"))
            {
                playerInv.getItem(selectR,selectC).remove(1);
                heal ++;
                //System.out.println("healing suceeded");
            }
            else if(!character && cargo.getItem(selectR,selectC).baseName.equals("Health"))
            {
                cargo.getItem(selectR,selectC).remove(1);
                heal ++;
            }
        }

        if(charTab.contains(mousex,mousey))
        {
            character = true;
        }
        else if(cargoHold.contains(mousex,mousey))
        {
            character = false;
        }
        else if(saveButton.contains(mousex,mousey))
        {
            save = true;
        }

        repaint();
    }

    public void mouseReleased(MouseEvent mouse)
    {
        mousex = mouse.getX();
        mousey = mouse.getY();
        selectC = (int)((mousex)/size);
        selectR = (int)((mousey-invTopy -30)/size);

        if(character)
        {
            if( swap && cargoHold.contains(mousex,mousey))
            {
                cargo.addItem(playerInv.getItem(lastSelectR,lastSelectC));
                playerInv.removeItem(lastSelectR,lastSelectC);
                //added = true;
            }
            else if(character && selectR < invR  && selectC < invC && lastSelectR < invR  && lastSelectC < invC 
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
        else
        {

            if(swap && charTab.contains(mousex,mousey))
            {
                playerInv.addItem(cargo.getItem(lastSelectR,lastSelectC));
                cargo.removeItem(lastSelectR,lastSelectC);
            }
            else if(!character && selectR < cargoR  && selectC < cargoC && lastSelectR < cargoR  && lastSelectC < cargoC 
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
        if(save)
        {
            playerInv.saveInv();
            cargo.saveInv();
            save = false;
        }

        selectC = (int)((mousex)/size);
        selectR = (int)((mousey-invTopy -30)/size);
        swap = false;
    }

    public void mouseMoved(MouseEvent mouse)
    {
        mousex = mouse.getX();
        mousey = mouse.getY();

        repaint();
    }

    public void keyTyped(KeyEvent key) {}

    public void keyPressed(KeyEvent key)  {}

    public void keyReleased(KeyEvent key)
    {  
        done = false;
    }

    public void actionPerformed(ActionEvent e)
    {        
        repaint();
    }

    public static void main (String [] args)
    {
        new InventoryView();
    }
}

class upAction extends AbstractAction
{
    private InventoryView myInv;

    public upAction(InventoryView myInv)
    {
        this.myInv = myInv;
    }

    public void actionPerformed(ActionEvent e) 
    {
        myInv.selectR --;
    }

}

class downAction extends AbstractAction
{
    private InventoryView myInv;

    public downAction(InventoryView myInv)
    {
        this.myInv = myInv;
    }

    public void actionPerformed(ActionEvent e) 
    {
        myInv.selectR ++;
    }

}

class leftAction extends AbstractAction
{
    private InventoryView myInv;

    public leftAction(InventoryView myInv)
    {
        this.myInv = myInv;
    }

    public void actionPerformed(ActionEvent e) 
    {
        myInv.selectC --;
    }

}

class rightAction extends AbstractAction
{
    private InventoryView myInv;

    public rightAction(InventoryView myInv)
    {
        this.myInv = myInv;
    }

    public void actionPerformed(ActionEvent e) 
    {
        myInv.selectC ++;
    }

}

class toolbarOne extends AbstractAction
{
    private InventoryView myInv;

    public toolbarOne(InventoryView myInv)
    {
        this.myInv = myInv;
    }

    public void actionPerformed(ActionEvent e) 
    {
        myInv.selectC = 0;
        myInv.selectR = 0;
    }

}

class toolbarTwo extends AbstractAction
{
    private InventoryView myInv;

    public toolbarTwo(InventoryView myInv)
    {
        this.myInv = myInv;
    }

    public void actionPerformed(ActionEvent e) 
    {
        myInv.selectC = 1;
        myInv.selectR = 0;
    }

}

class toolbarThree extends AbstractAction
{
    private InventoryView myInv;

    public toolbarThree(InventoryView myInv)
    {
        this.myInv = myInv;
    }

    public void actionPerformed(ActionEvent e) 
    {
        myInv.selectC = 2;
        myInv.selectR = 0;
    }

}

class toolbarFour extends AbstractAction
{
    private InventoryView myInv;

    public toolbarFour(InventoryView myInv)
    {
        this.myInv = myInv;
    }

    public void actionPerformed(ActionEvent e) 
    {
        myInv.selectC = 3;
        myInv.selectR = 0;
    }

}

class toolbarFive extends AbstractAction
{
    private InventoryView myInv;

    public toolbarFive(InventoryView myInv)
    {
        this.myInv = myInv;
    }

    public void actionPerformed(ActionEvent e) 
    {
        myInv.selectC = 4;
        myInv.selectR = 0;
    }

}

class toolbarSix extends AbstractAction
{
    private InventoryView myInv;

    public toolbarSix(InventoryView myInv)
    {
        this.myInv = myInv;
    }

    public void actionPerformed(ActionEvent e) 
    {
        myInv.selectC = 5;
        myInv.selectR = 0;
    }

}

class toolbarSeven extends AbstractAction
{
    private InventoryView myInv;

    public toolbarSeven(InventoryView myInv)
    {
        this.myInv = myInv;
    }

    public void actionPerformed(ActionEvent e) 
    {
        myInv.selectC = 6;
        myInv.selectR = 0;
    }

}

class toolbarEight extends AbstractAction
{
    private InventoryView myInv;

    public toolbarEight(InventoryView myInv)
    {
        this.myInv = myInv;
    }

    public void actionPerformed(ActionEvent e) 
    {
        myInv.selectC = 7;
        myInv.selectR = 0;
    }

}

class toolbarNine extends AbstractAction
{
    private InventoryView myInv;

    public toolbarNine(InventoryView myInv)
    {
        this.myInv = myInv;
    }

    public void actionPerformed(ActionEvent e) 
    {
        myInv.selectC = 8;
        myInv.selectR = 0;
    }

}

class toolbarZero extends AbstractAction
{
    private InventoryView myInv;

    public toolbarZero(InventoryView myInv)
    {
        this.myInv = myInv;
    }

    public void actionPerformed(ActionEvent e) 
    {
        myInv.selectC = 9;
        myInv.selectR = 0;
    }

}

class quit extends AbstractAction
{
    private InventoryView myInv;

    public quit(InventoryView myInv)
    {
        this.myInv = myInv;
    }

    public void actionPerformed(ActionEvent e) 
    {
        myInv.save();
        myInv.on = false;
    }

}

