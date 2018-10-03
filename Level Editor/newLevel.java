import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Scanner;
import javax.imageio.*;
import java.awt.CardLayout;
import java.awt.image.*;
import java.awt.geom.AffineTransform;
import java.io.*;
import javax.swing.SwingConstants;

public class newLevel extends JPanel implements ActionListener, MouseListener, MouseMotionListener, MouseWheelListener
{
    final int WIDTH = 1000;
    final int HEIGHT = 641;

    final int INDEX_LEFT = 0;
    final int INDEX_RIGHT = 1;

    private Shell myShell;
    String code = "UTF-8";
    String tileFolder = "gameTextures";
    String skyboxFolder = "skyboxes/";

    String levelName;
    int mapWidth = 0;
    int mapHeight = 0;

    int floorIndex = 0;
    int borderIndex = 0;
    int ceilingIndex = 0;

    int musicIndex = 0;
    int skyIndex = 0;
    boolean isCovered = false;

    int doorIndex = 9;
    int texNum = 10;
    BufferedImage[] textures = new BufferedImage[texNum]; 

    int skyboxIndex = 0;
    int skyboxNum = 5;
    BufferedImage[] skybox = new BufferedImage[skyboxNum];

    boolean isAggressive;

    int musicNum = 15;
    String[] music = {"Song1","Song2","Song3","Song4","Song5","Song6","Song7","Song8","Song9","Song10","Song11","Song12","Song13","Song14","Song15",};

    // All the GUI business
    Point mouse = new Point(0,0);
    JTextField widthField = new JTextField("50",3);    
    JTextField heightField = new JTextField("50",3);
    JTextField nameField = new JTextField("NewLevel",20);

    JLabel widthText = new JLabel("Map Width:",SwingConstants.CENTER);
    JLabel heightText = new JLabel("Map Height:",SwingConstants.CENTER);
    JLabel floorTexText = new JLabel("Default Floor Texture:",SwingConstants.CENTER);
    JLabel ceilTexText = new JLabel("Default Ceiling Texture:",SwingConstants.CENTER);
    JLabel musicText = new JLabel("Level Music:",SwingConstants.CENTER);
    JLabel skyboxText = new JLabel("Skybox:",SwingConstants.CENTER);
    JLabel borderTexText = new JLabel("Default Border Texture:",SwingConstants.CENTER);
    JLabel borderTexLabel = new JLabel("Default Border Texture:",SwingConstants.CENTER);
    JLabel floorTexLabel = new JLabel("Texture Here",SwingConstants.CENTER);   
    JLabel musicLabel = new JLabel("Song Name",SwingConstants.CENTER);
    JLabel nameLabel = new JLabel("Level Name:",SwingConstants.CENTER);
    JLabel ceilTexLabel = new JLabel("Default Ceiling Texture:",SwingConstants.CENTER);
    JLabel skyboxLabel = new JLabel("Skybox",SwingConstants.CENTER);

    JButton floorLeft = new JButton();
    JButton floorRight = new JButton();
    JButton ceilLeft = new JButton();
    JButton ceilRight = new JButton();
    JButton musicLeft = new JButton();
    JButton musicRight = new JButton();
    JButton skyboxLeft = new JButton();
    JButton skyboxRight = new JButton();
    JButton borderLeft = new JButton();
    JButton borderRight = new JButton();
    JButton createLevel = new JButton("Create New Level");
    JCheckBox covered = new JCheckBox("Initially Covered");
    JCheckBox aggressive = new JCheckBox("Agressive Population");

    public newLevel(Shell myShell)
    {
        this.myShell = myShell;
        init();

        borderTexLabel = new JLabel(new ImageIcon(textures[borderIndex]),SwingConstants.CENTER);
        ceilTexLabel = new JLabel(new ImageIcon(textures[ceilingIndex]),SwingConstants.CENTER);    
        floorTexLabel = new JLabel(new ImageIcon(textures[floorIndex]),SwingConstants.CENTER);
        skyboxLabel = new JLabel(new ImageIcon(skybox[skyboxIndex]),SwingConstants.CENTER);
        try {
            Image left = ImageIO.read(new File("buttonLeft.png"));
            floorLeft.setIcon(new ImageIcon(left));
            ceilLeft.setIcon(new ImageIcon(left));
            musicLeft.setIcon(new ImageIcon(left));
            skyboxLeft.setIcon(new ImageIcon(left));
            borderLeft.setIcon(new ImageIcon(left));
            Image right = ImageIO.read(new File("buttonRight.png"));
            floorRight.setIcon(new ImageIcon(right));
            ceilRight.setIcon(new ImageIcon(right));
            musicRight.setIcon(new ImageIcon(right));
            skyboxRight.setIcon(new ImageIcon(right));
            borderRight.setIcon(new ImageIcon(right));
        } catch (IOException e) {}

        floorLeft.addActionListener(this);
        floorRight.addActionListener(this);
        ceilLeft.addActionListener(this);
        ceilRight.addActionListener(this);
        borderLeft.addActionListener(this);
        borderRight.addActionListener(this);
        musicLeft.addActionListener(this);
        musicRight.addActionListener(this);
        skyboxLeft.addActionListener(this);
        skyboxRight.addActionListener(this);

        JPanel panel1 = new JPanel(new GridLayout(0, 2, 10, 10));
        panel1.add(widthText);
        panel1.add(heightText);
        JPanel panel2 = new JPanel(new GridLayout(0, 2, 10, 10));
        panel2.add(widthField);        
        panel2.add(heightField);        
        JPanel panel3 = new JPanel(new GridLayout(0, 4, 10, 10));
        panel3.add(floorTexText);
        panel3.add(floorLeft);
        panel3.add(floorTexLabel);
        panel3.add(floorRight);
        JPanel panel4 = new JPanel(new GridLayout(0, 4, 10, 10));
        panel4.add(ceilTexText);
        panel4.add(ceilLeft);
        panel4.add(ceilTexLabel);
        panel4.add(ceilRight);
        JPanel panel5 = new JPanel(new GridLayout(0, 4, 10, 10));
        panel5.add(musicText);
        panel5.add(musicLeft);
        panel5.add(musicLabel);
        panel5.add(musicRight);
        JPanel panel6 = new JPanel(new GridLayout(0, 4, 10, 10));
        panel6.add(skyboxText);
        panel6.add(skyboxLeft);
        panel6.add(skyboxLabel);
        panel6.add(skyboxRight);
        JPanel panel7 = new JPanel(new GridLayout(0, 4, 10, 10));
        panel7.add(borderTexText);
        panel7.add(borderLeft);
        panel7.add(borderTexLabel);
        panel7.add(borderRight);
        JPanel panel8 = new JPanel(new GridLayout(0, 5, 10, 10));
        panel8.add(covered);
        panel8.add(aggressive);
        panel8.add(nameLabel);
        panel8.add(nameField);
        panel8.add(createLevel);

        final JFrame frame = new JFrame("Create New Level");
        frame.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(8, 1, 10, 10));
        frame.setBackground(new Color(0x99ccff));
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        frame.add(panel1);
        frame.add(panel2);
        frame.add(panel7);
        frame.add(panel3);
        frame.add(panel4);
        frame.add(panel6);
        frame.add(panel5);
        frame.add(panel8);
        frame.pack();
        frame.setVisible(true);

        createLevel.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {                   
                    createNewLevel();
                    frame.dispose();
                }
            });

        javax.swing.Timer timer = new javax.swing.Timer(5, this);
        timer.start();
    }

    public void init()
    {
        try{
            //then load all the textures
            textures[0] = ImageIO.read(new File(tileFolder+"/grass.png"));
            textures[1] = ImageIO.read(new File(tileFolder+"/greystone.png"));
            textures[2] = ImageIO.read(new File(tileFolder+"/bluestone.png"));
            textures[3] = ImageIO.read(new File(tileFolder+"/mossy.png"));
            textures[4] = ImageIO.read(new File(tileFolder+"/purplestone.png"));
            textures[5] = ImageIO.read(new File(tileFolder+"/redbrick.png"));
            textures[6] = ImageIO.read(new File(tileFolder+"/wood.png"));
            textures[7] = ImageIO.read(new File(tileFolder+"/plank.png"));
            textures[8] = ImageIO.read(new File(tileFolder+"/colorstone.png"));
            textures[9] = ImageIO.read(new File(tileFolder+"/door.png"));    

            //load Skyboxes
            skybox[0] = ImageIO.read(new File(skyboxFolder+"/greySky.png"));
            skybox[1] = ImageIO.read(new File(skyboxFolder+"/mountains.png"));
            skybox[2] = ImageIO.read(new File(skyboxFolder+"/mountains2.png"));
            skybox[3] = ImageIO.read(new File(skyboxFolder+"/forest.png"));
            skybox[4] = ImageIO.read(new File(skyboxFolder+"/citySky.png"));
        }catch(IOException e){}

        // prep the skyboxes
        for(int i = 0; i < skyboxNum; i++)
        {
            BufferedImage temp = skybox[i].getSubimage(0,0,1200,300);
            BufferedImage temp2 = new BufferedImage(256,64,BufferedImage.TYPE_INT_RGB);
            Graphics scale = temp2.getGraphics();
            scale.drawImage(temp,0,0,256,64,null);
            scale.dispose();
            skybox[i] = temp2;
        }
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
        floorTexLabel.setIcon(new ImageIcon(textures[floorIndex]));
        ceilTexLabel.setIcon(new ImageIcon(textures[ceilingIndex]));
        borderTexLabel.setIcon(new ImageIcon(textures[borderIndex]));
        skyboxLabel.setIcon(new ImageIcon(skybox[skyboxIndex]));
        musicLabel.setText(music[musicIndex]);
        isCovered = covered.isSelected();
        isAggressive = aggressive.isSelected();
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == floorRight)
        {
            floorIndex = moveTexIndex(floorIndex,INDEX_RIGHT);
        }
        if(e.getSource() == floorLeft)
        {
            floorIndex = moveTexIndex(floorIndex,INDEX_LEFT);
        }
        if(e.getSource() == ceilRight)
        {
            ceilingIndex = moveTexIndex(ceilingIndex,INDEX_RIGHT);
        }
        if(e.getSource() == ceilLeft)
        {
            ceilingIndex = moveTexIndex(ceilingIndex,INDEX_LEFT);
        }
        if(e.getSource() == borderRight)
        {
            borderIndex = moveTexIndex(borderIndex,INDEX_RIGHT);
        }
        if(e.getSource() == borderLeft)
        {
            borderIndex = moveTexIndex(borderIndex,INDEX_LEFT);
        }
        if(e.getSource() == musicRight)
        {
            musicIndex = moveMusicIndex(musicIndex,INDEX_RIGHT);
        }
        if(e.getSource() == musicLeft)
        {
            musicIndex = moveMusicIndex(musicIndex,INDEX_LEFT);
        }
        if(e.getSource() == skyboxRight)
        {
            skyboxIndex = moveSkyIndex(skyboxIndex,INDEX_RIGHT);
        }
        if(e.getSource() == skyboxLeft)
        {
            skyboxIndex = moveSkyIndex(skyboxIndex,INDEX_LEFT);
        }
        update();
        repaint();
    }

    public void createNewLevel()
    {
        int width = Integer.parseInt(widthField.getText());
        int height = Integer.parseInt(heightField.getText());
        isCovered = covered.isSelected();
        isAggressive = aggressive.isSelected();
        levelName = nameField.getText();
        if(borderIndex == 0) borderIndex = 1;
        if(ceilingIndex == 0) ceilingIndex = 1;
        new makeLevel(levelName,width,height,musicIndex,skyIndex,floorIndex,ceilingIndex,borderIndex,isCovered,isAggressive);
        myShell.setLevelName(levelName);
        myShell.loadLevel();
    }

    public int moveTexIndex(int i,int dir)
    {
        int returnI = i;
        if(dir == INDEX_RIGHT)
        {
            if(i == 8)
            {
                returnI = 0;
            }
            else
                returnI++;
        }
        else if(dir == INDEX_LEFT)
        {
            if(i == 0)
            {
                returnI = 8;
            }
            else
                returnI--;
        }
        return returnI;
    }

    public int moveMusicIndex(int i,int dir)
    {
        int returnI = i;
        if(dir == INDEX_RIGHT)
        {
            if(i == musicNum-1)
            {
                returnI = 0;
            }
            else
                returnI++;
        }
        else if(dir == INDEX_LEFT)
        {
            if(i == 0)
            {
                returnI = musicNum-1;
            }
            else
                returnI--;
        }
        return returnI;
    }

    public int moveSkyIndex(int i,int dir)
    {
        int returnI = i;
        if(dir == INDEX_RIGHT)
        {
            if(i == skyboxNum-1)
            {
                returnI = 0;
            }
            else
                returnI++;
        }
        else if(dir == INDEX_LEFT)
        {
            if(i == 0)
            {
                returnI = skyboxNum-1;
            }
            else
                returnI--;
        }
        return returnI;
    }

}