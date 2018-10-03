import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Scanner;
import javax.imageio.*;
import java.awt.CardLayout;
import java.awt.image.*;
import java.awt.geom.AffineTransform;
import java.io.*;

public class Menu extends JPanel implements ActionListener, MouseListener, MouseMotionListener, MouseWheelListener
{
    final int WIDTH = 1000;
    final int HEIGHT = 641;
    
    private Shell myShell;
    
    String chosenName = "level2";    //(default)

    String code = "UTF-8";

    int bwidth = 300;
    int bheight = 40;
    int bx = (WIDTH/2) - (bwidth/2);

    BufferedImage button1 = new BufferedImage(bwidth,bheight,BufferedImage.TYPE_INT_RGB);
    BufferedImage button2 = new BufferedImage(bwidth,bheight,BufferedImage.TYPE_INT_RGB);

    String text1 = "New Level";
    String text2 = "Load Level";

    boolean newLevel = false;
    boolean loadLevel = false;

    Point mouse = new Point(0,0);

    public Menu(Shell myShell)
    {
        this.myShell = myShell;
        init();
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        //JFrame frame = new JFrame("Menu");
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(new Color(0x99ccff));

        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);

        //frame.getContentPane().add(this);

        //frame.pack();
        //frame.setVisible(true);

        javax.swing.Timer timer = new javax.swing.Timer(5, this);
        timer.start();
    }

    public void init()
    {
        Graphics b1 = button1.getGraphics();
        b1.setColor(Color.GRAY.brighter());
        b1.fillRect(0,0,bwidth,bheight);
        b1.setColor(Color.BLACK);
        b1.setFont(new Font("Bank Gothic", Font.BOLD, 18));
        b1.drawString(text1,100,26);

        Graphics b2 = button2.getGraphics();
        b2.setColor(Color.GRAY.brighter());
        b2.fillRect(0,0,bwidth,bheight);
        b2.setColor(Color.BLACK);
        b2.setFont(new Font("Bank Gothic", Font.BOLD, 18));
        b2.drawString(text2,100,26);

    }    

    public boolean newLevelChosen()
    {
        return newLevel;
    }

    public boolean loadLevelChosen()
    {
        return loadLevel;
    }

    public void mouseReleased(MouseEvent mouse) {}

    public void mouseEntered(MouseEvent mouse) {}

    public void mouseExited(MouseEvent mouse) {}

    public void mouseDragged(MouseEvent mouse) {} 

    public void mouseClicked(MouseEvent mouse){}

    public void mousePressed(MouseEvent mouse) 
    {
        
        if((mouse.getX()>bx)&&(mouse.getX() < bx+bwidth))
        {
            if((mouse.getY()>300)&&(mouse.getY()<300+bheight))
            {
                myShell.newLevel();
            }
            else if((mouse.getY()>350)&&(mouse.getY()<350+bheight))
            {
                levelChooser choose = new levelChooser();
                if(choose.fileSelected())
                {
                    //System.out.println("This Worked");
                    chosenName = choose.selectedFile();
                    loadLevel = true;
                }
            }
        }
        
        if(loadLevel)
        {
            myShell.setLevelName(chosenName);
            myShell.loadLevel();
        }
        
    }

    public void mouseMoved(MouseEvent mouse)
    {
        this.mouse = mouse.getPoint();
    }

    public void mouseWheelMoved(MouseWheelEvent mouse)
    {

    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(button1,bx,300,null);
        g.drawImage(button2,bx,350,null);
    }

    public void update()
    {

    }

    public void actionPerformed(ActionEvent e)
    {
        update();
        repaint();
    }
    /*
    public static void main(String[] args)
    {
        new Menu();
    }
    */
}