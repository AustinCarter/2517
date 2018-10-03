import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Scanner;
import javax.imageio.*;
import java.awt.CardLayout;
import java.awt.image.*;
import java.awt.geom.AffineTransform;
import java.io.*;

public class Shell extends JPanel implements ActionListener, MouseListener, MouseMotionListener, MouseWheelListener
{
    final int WIDTH = 1000;
    final int HEIGHT = 641;

    String levelName = "";        // will change based on level to be loaded
    String dataFileName = "data";       // will not change
    String floorFileName = "floor";
    String groundFileName = "ground";
    String ceilingFileName = "ceiling";
    String code = "UTF-8";

    JPanel cards;

    Point mouse = new Point(0,0);

    boolean DOWN, UP, LEFT, RIGHT;
    boolean keyToggle = false; // is a key being pressed/held?
    int key = 0;

    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);

    public Shell()
    {

        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        JFrame frame = new JFrame("Shell");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel card1 = new Menu(this);
        cardPanel.add(card1,"Menu");
        cardPanel.setVisible(true);
        //create the cardLayout

        add(cardPanel, BorderLayout.NORTH);

        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);

        frame.getContentPane().add(this);

        frame.pack();
        frame.setVisible(true);

        javax.swing.Timer timer = new javax.swing.Timer(5, this);
        timer.start();
    }

    public void init()
    {

    }    

    public void mouseReleased(MouseEvent mouse) {}

    public void mouseEntered(MouseEvent mouse) {}

    public void mouseExited(MouseEvent mouse) {}

    public void mouseDragged(MouseEvent mouse) {} 

    public void mouseClicked(MouseEvent mouse){}

    public void mousePressed(MouseEvent mouse) 
    {

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
    }

    public void update()
    {

        //cardLayout.next(cardPanel);

    }

    public void loadLevel()
    {
        JPanel card3 = new Editor(levelName);
        card3.setFocusable(true);  
        cardPanel.add(card3,"Editor");
        cardLayout.show(cardPanel,"Editor");
    }
    
    public void newLevel()
    {
        new newLevel(this);
    }

    public void setLevelName(String name)
    {        
        levelName = name;
    }

    public void actionPerformed(ActionEvent e)
    {
        update();
        repaint();
    }

    public static void main(String[] args)
    {
        Shell Shell = new Shell();
        JFrame frame = new JFrame();
        //EXIT_ON_CLOSE = 3
        frame.setDefaultCloseOperation(3);
        frame.setTitle("Shell");
        frame.getContentPane().add(Shell, BorderLayout.CENTER);
        frame.setSize(800,600);
        frame.setVisible(true);
    }
}