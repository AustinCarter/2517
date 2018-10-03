import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Scanner;
import java.util.ArrayList;
import javax.imageio.*;
import java.awt.image.*;
import java.awt.geom.AffineTransform;
import java.io.*;
import javax.swing.AbstractAction;

public class Editor extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener, MouseWheelListener
{
    final int WIDTH = 1000;
    final int HEIGHT = 641;
    final int editWidth = 640;
    final int editHeight = 640;
    final int EDIT_TILES = 0;
    final int EDIT_POINTS = 1;
    final int EDIT_SPRITES = 2;
    final int NOT_NPC = -1;

    int editState = EDIT_TILES;

    private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
    static String defaultName = "level1";
    String levelName;        // will change based on level to be loaded
    String mainFolder = "levels/";
    String dataFileName = "data";       // will not change
    String floorFileName = "floor";
    String groundFileName = "ground";
    String ceilingFileName = "ceiling";
    String actionFolder = "actionIcons";
    String spriteFolder = "spriteTextures";
    String tileFolder = "gameTextures";
    String actionFileName = "actionPoints";
    String spriteFileName = "sprites";
    String npcFileName = "npcs";
    String code = "UTF-8";

    int zoomIndex = 0;
    int[] zooms = {1,2,4,6,8};
    int zoom = zooms[zoomIndex]; // 1,2,4,6,8    
    int texSize = 64;
    int texMod = (texSize/zoom);

    boolean mouseOnGrid = false;

    int Ibox_x = editWidth+1;
    int Ibox_y = 0;

    int saveX = WIDTH - texSize;
    int saveY = HEIGHT - texSize;

    int offsetR = 0;
    int offsetC = 0;

    int cur_r;
    int cur_c;
    Point mouse = new Point(0,0);

    boolean DOWN, UP, LEFT, RIGHT;
    boolean keyToggle = false; // is a key being pressed/held?
    int key = 0;

    BufferedImage select;
    BufferedImage selector;    
    BufferedImage visible;
    BufferedImage edit;
    BufferedImage crossout;
    BufferedImage save;
    BufferedImage floorButton;
    BufferedImage groundButton;
    BufferedImage ceilingButton;

    BufferedImage sample = new BufferedImage(texSize,texSize,BufferedImage.TYPE_INT_RGB);
    int selectedTex = 6;
    int numTex = 14;
    BufferedImage[] textures = new BufferedImage[numTex];
    int numSprites = 13;
    BufferedImage[] sprites = new BufferedImage[numSprites];
    int numIcons = 6;
    BufferedImage[] icons = new BufferedImage[numIcons];
    int spriteIndex = 0;
    int iconIndex = 0;
    boolean floorVisible = true;
    boolean groundVisible = true;
    boolean ceilingVisible = true;
    boolean spriteVisible = true;
    boolean pointVisible = true;
    int selectedLevel = 0;
    int floor = 0;
    int ground = 1;
    int ceiling = 2;

    double playerX = 20;
    double playerY = 20;
    int skyboxIndex;
    int musicIndex;

    boolean placing = false; // if this variable is set true, then it means that you need to place the second actionpoint/sprite 
    // before you can do anything else
    // such as switch->door, ladder->ladder, 
    int tempPointR = 0; 
    int tempPointC = 0;
    int tempPointType = 0;

    boolean xySet = true; // will be false

    //////// Add array of Buffered Images/ Textures
    ///////// Add world and ceiling maps
    ///////// add tools to sidebar
    /////// Add main menu
    /////// 

    String up = "up";
    String down = "down";
    String left = "left";
    String right = "right";

    int levelNumSprites;
    double spriteOffset = 0.5;
    ArrayList<Sprite> levelSprites = new ArrayList<Sprite>();
    int actionPointNum;
    ArrayList<ActionPoint> levelPoints = new ArrayList<ActionPoint>();
    ArrayList<ActionPoint> deletePoints = new ArrayList<ActionPoint>();

    boolean aggressive; // are the level's npcs aggressive initially

    int mapWidth;
    int mapHeight;

    int[][] floorMap;
    int[][] worldMap;
    int[][] ceilingMap;

    public Editor(String name)
    {
        levelName = name;
        init();       
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        setBackground(new Color(0x99ccff));
        JPanel frame = new JPanel();
        frame.setFocusable(true);

        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);

        frame.addKeyListener(this);

        frame.setVisible(true);

        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("UP"), up);
        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("DOWN"), down);
        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("LEFT"), left);
        this.getInputMap(IFW).put(KeyStroke.getKeyStroke("RIGHT"), right);

        this.getActionMap().put(up, new upAction(this));
        this.getActionMap().put(down, new downAction(this));
        this.getActionMap().put(left, new leftAction(this));
        this.getActionMap().put(right, new rightAction(this));

        javax.swing.Timer timer = new javax.swing.Timer(5, this);
        timer.start();
    }

    public void init()
    {
        try{
            loadLevel(); // first load the level data

            //then load all the textures
            select = ImageIO.read(new File("select.png"));
            selector = ImageIO.read(new File("selector.png"));    
            visible = ImageIO.read(new File("visible2.png"));
            edit = ImageIO.read(new File("edit.png"));
            crossout = ImageIO.read(new File("crossout.png"));
            floorButton = ImageIO.read(new File("floorbutton.png"));
            groundButton = ImageIO.read(new File("groundbutton.png"));
            ceilingButton = ImageIO.read(new File("ceilingbutton.png"));
            save = ImageIO.read(new File("save.png"));

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
            textures[10] = ImageIO.read(new File(tileFolder+"/switchoff.png"));
            textures[11] = textures[10]; // skip 11
            textures[12] = ImageIO.read(new File(tileFolder+"/plankdoor.png"));
            textures[13] = ImageIO.read(new File(tileFolder+"/wooddoor.png"));

            //load icons
            icons[0] = ImageIO.read(new File(actionFolder+"/switchPoint.png")); 
            icons[1] = ImageIO.read(new File(actionFolder+"/portalPoint.png")); 
            icons[2] = ImageIO.read(new File(actionFolder+"/startPoint.png")); 
            icons[3] = ImageIO.read(new File(actionFolder+"/exitPoint.png")); 
            icons[4] = ImageIO.read(new File(actionFolder+"/containerPoint.png"));
            icons[5] = ImageIO.read(new File(actionFolder+"/doorPoint.png"));

            //load Sprites
            sprites[0] = ImageIO.read(new File(spriteFolder+"/barrel.png"));
            sprites[1] = ImageIO.read(new File(spriteFolder+"/pillar.png"));
            sprites[2] = ImageIO.read(new File(spriteFolder+"/spaceGuy.png"));
            sprites[3] = ImageIO.read(new File(spriteFolder+"/ladderup.png"));
            sprites[4] = ImageIO.read(new File(spriteFolder+"/ladderdown.png"));
            sprites[5] = ImageIO.read(new File(spriteFolder+"/armorAlien.png"));
            sprites[6] = ImageIO.read(new File(spriteFolder+"/spaceGuy2.png"));
            sprites[7] = ImageIO.read(new File(spriteFolder+"/chest.png"));
            sprites[8] = ImageIO.read(new File(spriteFolder+"/lamp.png"));
            sprites[9] = ImageIO.read(new File(spriteFolder+"/flowers.png"));
            sprites[10] = ImageIO.read(new File(spriteFolder+"/bush.png"));
            sprites[11] = ImageIO.read(new File(spriteFolder+"/shopkeep.png"));
            sprites[12] = ImageIO.read(new File(spriteFolder+"/walter.png"));
        }catch(IOException e){}
    }

    public int getNpcVal(int spriteArrayIndex)
    {
        //if the sprite is an NPC, return its type value. if not return zero
        int i = spriteArrayIndex;
        int[] returnMap = {NOT_NPC,NOT_NPC,0,NOT_NPC,NOT_NPC,1,2,NOT_NPC,NOT_NPC,NOT_NPC,NOT_NPC,NOT_NPC,NOT_NPC};
        return returnMap[i];
        //getting a -1 from this method means the Sprite is not an NPC
    }

    public int spriteAt(int r, int c)
    {
        for(Sprite s : levelSprites)
        {
            if((c == (int)(s.getX()))&&(r == (int)(s.getY())))
            {
                return levelSprites.indexOf(s);
            }
        }
        return(-1);
    }

    public int pointAt(int r, int c)
    {
        for(ActionPoint a : levelPoints)
        {
            if((c == a.get_c())&&(r == a.get_r()))
            {
                return levelPoints.indexOf(a);
            }
        }
        return(-1);
    }

    public void loadLevel() throws IOException
    {
        File data = new File(mainFolder+levelName+"/"+dataFileName);  ///// get level dimensions from the "data " file
        Scanner parse = new Scanner(data);
        mapWidth = parse.nextInt();
        mapHeight = parse.nextInt();
        skyboxIndex = parse.nextInt();
        musicIndex = parse.nextInt();
        playerX = parse.nextDouble();
        playerY = parse.nextDouble();
        levelNumSprites = parse.nextInt();
        actionPointNum = parse.nextInt();
        int aggNum = parse.nextInt();
        if(aggNum == 0){aggressive = false;}
        else if(aggNum == 1){aggressive = true;}

        floorMap = new int[mapHeight][mapWidth];            ////// initialize the level arrays
        worldMap = new int[mapHeight][mapWidth];
        ceilingMap = new int[mapHeight][mapWidth];

        Scanner parse1 = new Scanner(new File(mainFolder+levelName+"/"+floorFileName));  // read in the floor file
        parse1.useDelimiter("\\s*,\\s*");
        for(int c = 0; c< mapWidth; c++)
        {
            for(int r = 0; r< mapHeight; r++)
            {
                if(parse1.hasNextInt())
                {
                    floorMap[r][c] = parse1.nextInt();
                }                    
            }
        }

        Scanner parse2 = new Scanner(new File(mainFolder+levelName+"/"+groundFileName));  // read in the ground file
        parse2.useDelimiter("\\s*,\\s*");
        for(int c = 0; c< mapWidth; c++)
        {
            for(int r = 0; r< mapHeight; r++)
            {
                if(parse2.hasNextInt())
                {
                    worldMap[r][c] = parse2.nextInt();
                }                    
            }
        }

        Scanner parse3 = new Scanner(new File(mainFolder+levelName+"/"+ceilingFileName));  // read in the ceiling file
        parse3.useDelimiter("\\s*,\\s*");
        for(int c = 0; c< mapWidth; c++)
        {
            for(int r = 0; r< mapHeight; r++)
            {
                if(parse3.hasNextInt())
                {
                    ceilingMap[r][c] = parse3.nextInt();
                }                    
            }
        }

        // load sprites
        Scanner parse5 = new Scanner(new File(mainFolder+levelName+"/"+spriteFileName));
        parse5.useDelimiter("\\s*,\\s*");
        while(parse5.hasNextDouble())
        {
            double r = parse5.nextDouble();
            double c = parse5.nextDouble();
            int tex = parse5.nextInt();
            levelSprites.add(new Sprite(r,c,tex));
        }
        Scanner parse6 = new Scanner(new File(mainFolder+levelName+"/"+npcFileName));
        parse6.useDelimiter("\\s*,\\s*");
        while(parse6.hasNextDouble())
        {
            double r = parse6.nextDouble();
            double c = parse6.nextDouble();
            int tex = parse6.nextInt();
            int type = parse6.nextInt();
            levelSprites.add(new Npc(r,c,tex,type));
        }

        //load ActionPoints
        Scanner parse7 = new Scanner(new File(mainFolder+levelName+"/"+actionFileName));
        parse7.useDelimiter("\\s*,\\s*");
        for(int i = 0; i<actionPointNum;i++)
        {
            for(int b = 0; b<5;b++)
            {
                if(parse7.hasNextInt())
                {
                    int r1 = parse7.nextInt();
                    int c1 = parse7.nextInt(); 
                    int r2 = parse7.nextInt();
                    int c2 = parse7.nextInt();
                    int type = parse7.nextInt(); 
                    levelPoints.add(new ActionPoint(r1,c1,r2,c2,type));
                }
            }
        }
    }

    public void saveLevel() throws IOException
    {
        actionPointNum = levelPoints.size();
        levelNumSprites = levelSprites.size();

        int aggNum = aggressive ? 1 : 0; // ternary operator

        //update the data file        
        PrintWriter writer1 = new PrintWriter(mainFolder+levelName+"/"+ dataFileName,code);
        writer1.println(mapWidth);                                          
        writer1.println(mapHeight);
        writer1.println(skyboxIndex);
        writer1.println(musicIndex);
        writer1.println(playerX);
        writer1.println(playerY);
        writer1.println(levelNumSprites);
        writer1.println(actionPointNum);
        writer1.println(aggNum);
        writer1.close();
        //overwrite the three files for the new arrays
        PrintWriter writer = new PrintWriter(mainFolder+levelName+"/"+ floorFileName,code); 
        for(int c = 0; c< mapWidth; c++)
        {
            for(int r = 0; r< mapHeight; r++)
            {
                writer.print(floorMap[r][c] + ",");
            }
            writer.print("\n");
        }
        writer.close();

        PrintWriter writer2 = new PrintWriter(mainFolder+levelName+"/"+ groundFileName,code);
        for(int c = 0; c< mapWidth; c++)
        {
            for(int r = 0; r< mapHeight; r++)
            {
                writer2.print(worldMap[r][c] + ",");
            }
            writer2.print("\n");
        }
        writer2.close();

        PrintWriter writer3 = new PrintWriter(mainFolder+levelName+"/"+ ceilingFileName,code);
        for(int c = 0; c< mapWidth; c++)
        {
            for(int r = 0; r< mapHeight; r++)
            {
                writer3.print(ceilingMap[r][c] + ",");
            }
            writer3.print("\n");
        }
        writer3.close();

        // save sprites and NPC's
        PrintWriter writer5 = new PrintWriter(new File(mainFolder+levelName+"/"+spriteFileName));
        for(Sprite s: levelSprites)
        {
            if(s instanceof Npc != true)
            {
                double r = s.getX();
                double c = s.getY();
                int t = s.texture;
                writer5.println(r + "," + c + "," + t + ",");
            }
        }
        writer5.close();

        PrintWriter writer6 = new PrintWriter(new File(mainFolder+levelName+"/"+npcFileName));
        for(Sprite s: levelSprites)
        {
            if(s instanceof Npc)
            {
                Npc n = (Npc) s;
                double r = n.getX();
                double c = n.getY();
                int t = n.getTex();
                int t2 = n.type;
                writer6.println(r + "," + c + "," + t + "," + t2 + ",");
            }
        }
        writer6.close();

        //save ActionPoints
        PrintWriter writer7 = new PrintWriter(new File(mainFolder+levelName+"/"+actionFileName));
        for(ActionPoint a: levelPoints)
        {
            int r = a.get_r();
            int c = a.get_c();
            int or = a.getOpen_r();
            int oc = a.getOpen_c();
            int type = a.getType();
            writer7.println(r + "," + c + "," + or + "," + oc + "," + type + ",");
        }
        writer7.close();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        int texMod = (texSize/zoom);
        for(int x = 0; x <= editWidth; x+=texMod)
        {            
            g.drawLine(x,0,x,editWidth);
        }
        for(int y = 0; y <= editHeight; y+=texMod)
        {
            g.drawLine(0,y,editHeight,y);
        }        
        g.setColor(new Color(0x0099ff));
        g.fillRect(Ibox_x,Ibox_y,WIDTH-Ibox_x,HEIGHT);
        g.setColor(Color.WHITE);
        g.drawRect(Ibox_x,Ibox_y,WIDTH-Ibox_x,HEIGHT);

        int widthInTiles = editWidth/texMod;

        for(int r = offsetR; r < mapHeight; r++)
        {
            for(int c = offsetC; c < mapWidth; c++)
            {
                g.setColor(Color.WHITE);
                int actualR = (r-offsetR)*texMod;
                int actualC = (c-offsetC)*texMod;
                if((actualC < editWidth)&&(actualR < editHeight)) 
                {
                    if((floorVisible)&&(((worldMap[r][c] == 0)||!groundVisible)&&((ceilingMap[r][c] == 0)||!ceilingVisible)))
                    {
                        BufferedImage draw = new BufferedImage(texMod,texMod,BufferedImage.TYPE_INT_RGB);
                        Graphics g2 = draw.createGraphics();
                        g2.drawImage(textures[floorMap[r][c]], 0, 0, texMod, texMod, null);
                        g2.dispose();
                        g.drawImage(draw,actualC,actualR,this);
                    }
                    if(groundVisible)
                    {
                        if((worldMap[r][c] != 0)&&((ceilingMap[r][c] == 0)||!ceilingVisible))
                        {
                            BufferedImage draw = new BufferedImage(texMod,texMod,BufferedImage.TYPE_INT_RGB);
                            Graphics g2 = draw.createGraphics();
                            g2.drawImage(textures[worldMap[r][c]], 0, 0, texMod, texMod, null);
                            g2.dispose();
                            g.drawImage(draw,actualC,actualR,this);
                        }
                    }
                    if(ceilingVisible)
                    {
                        if(ceilingMap[r][c] != 0)
                        {
                            BufferedImage draw = new BufferedImage(texMod,texMod,BufferedImage.TYPE_INT_RGB);
                            Graphics g2 = draw.createGraphics();
                            g2.drawImage(textures[ceilingMap[r][c]], 0, 0, texMod, texMod, null);
                            g2.dispose();
                            g.drawImage(draw,actualC,actualR,this);
                        }
                    }

                }
            }
        }

        if(spriteVisible)
        {
            for(Sprite s: levelSprites)
            {
                int actualR = ((int)s.getY()-offsetR)*texMod;
                int actualC = ((int)s.getX()-offsetC)*texMod;                
                if((actualC < editWidth)&&(actualR < editHeight)) 
                {
                    BufferedImage draw = new BufferedImage(texMod,texMod,BufferedImage.TYPE_INT_RGB);
                    Graphics g2 = draw.createGraphics();
                    g2.drawImage(sprites[s.texture], 0, 0, texMod, texMod, null);
                    g2.dispose();
                    g.drawImage(draw,actualC,actualR,this);
                }
            }            
        }
        //draw ActionPoints here
        if(pointVisible)
        {
            if(xySet)
            {
                int actualR = ((int)playerY-offsetR)*texMod;
                int actualC = ((int)playerX-offsetC)*texMod;
                if((actualC < editWidth)&&(actualR < editHeight)) 
                {
                    BufferedImage draw = new BufferedImage(texMod,texMod,BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2 = draw.createGraphics();
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR,0.0f));
                    g2.fillRect(0,0,texMod,texMod);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                    g2.drawImage(icons[2], 0, 0, texMod, texMod, null);
                    g2.dispose();
                    g.drawImage(draw,actualC,actualR,this);
                }
            }
            for(ActionPoint a: levelPoints)
            {
                int actualR = ((int)a.get_r()-offsetR)*texMod;
                int actualC = ((int)a.get_c()-offsetC)*texMod;
                int actualR2 = ((int)a.getOpen_r()-offsetR)*texMod;
                int actualC2 = ((int)a.getOpen_c()-offsetC)*texMod;
                if(a.getType() == 0) // if it is a switch/door
                {
                    if((actualC < editWidth)&&(actualR < editHeight))
                    {
                        BufferedImage draw = new BufferedImage(texMod,texMod,BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g2 = draw.createGraphics();
                        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR,0.0f));
                        g2.fillRect(0,0,texMod,texMod);
                        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                        g2.drawImage(icons[0], 0, 0, texMod, texMod, null);
                        g2.dispose();
                        g.drawImage(draw,actualC,actualR,this);
                    }
                    if((actualC2 < editWidth)&&(actualR2 < editHeight)) // draw the door
                    {
                        BufferedImage draw = new BufferedImage(texMod,texMod,BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g2 = draw.createGraphics();
                        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR,0.0f));
                        g2.fillRect(0,0,texMod,texMod);
                        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                        g2.drawImage(icons[5], 0, 0, texMod, texMod, null);
                        g2.dispose();
                        g.drawImage(draw,actualC2,actualR2,this);
                    }
                }
                else if(a.getType() == 1) // if it is a ladder/portal
                {
                    if((actualC < editWidth)&&(actualR < editHeight))
                    {
                        BufferedImage draw = new BufferedImage(texMod,texMod,BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g2 = draw.createGraphics();
                        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR,0.0f));
                        g2.fillRect(0,0,texMod,texMod);
                        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                        g2.drawImage(icons[1], 0, 0, texMod, texMod, null);
                        g2.dispose();
                        g.drawImage(draw,actualC,actualR,this);
                    }
                    if((actualC2 < editWidth)&&(actualR2 < editHeight)) // draw the door
                    {
                        BufferedImage draw = new BufferedImage(texMod,texMod,BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g2 = draw.createGraphics();
                        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR,0.0f));
                        g2.fillRect(0,0,texMod,texMod);
                        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                        g2.drawImage(icons[1], 0, 0, texMod, texMod, null);
                        g2.dispose();
                        g.drawImage(draw,actualC2,actualR2,this);
                    }
                }
                else
                {
                    BufferedImage draw = new BufferedImage(texMod,texMod,BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2 = draw.createGraphics();
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR,0.0f));
                    g2.fillRect(0,0,texMod,texMod);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                    g2.drawImage(icons[a.getType()], 0, 0, texMod, texMod, null);
                    g2.dispose();
                    g.drawImage(draw,actualC,actualR,this);
                }

            }
        }

        if(mouseOnGrid)
        {
            g.setColor(Color.RED);
            g.drawRect(cur_c*texMod,cur_r*texMod,texMod,texMod);
        }

        /////////////////////////////////////////////////
        //// draw the toolbox here///////////////////////
        /////////////////////////////////////////////////
        int toolX = Ibox_x + (texSize/2);
        int toolY = Ibox_y + (texSize/2);
        //toolX + texSize;
        //toolY + texSize;
        int buttonWidth = 128;

        //draw tools for the floor level
        g.drawImage(floorButton,toolX,toolY,this);
        toolX += buttonWidth;
        g.drawImage(visible,toolX,toolY,this);
        if(!floorVisible)
            g.drawImage(crossout,toolX,toolY,this);
        toolX += texSize;
        g.drawImage(edit,toolX,toolY,this);
        if(selectedLevel == floor)
            g.drawImage(select,toolX,toolY,this);

        toolX = Ibox_x + (texSize/2);
        toolY += texSize;
        //draw tools for the ground level
        g.drawImage(groundButton,toolX,toolY,this);
        toolX += buttonWidth;
        g.drawImage(visible,toolX,toolY,this);
        if(!groundVisible)
            g.drawImage(crossout,toolX,toolY,this);
        toolX += texSize;
        g.drawImage(edit,toolX,toolY,this);
        if(selectedLevel == ground)
            g.drawImage(select,toolX,toolY,this);

        toolX = Ibox_x + (texSize/2);
        toolY += texSize;
        //draw tools for the ground level
        g.drawImage(ceilingButton,toolX,toolY,this);
        toolX += buttonWidth;
        g.drawImage(visible,toolX,toolY,this);
        if(!ceilingVisible)
            g.drawImage(crossout,toolX,toolY,this);
        toolX += texSize;
        g.drawImage(edit,toolX,toolY,this);
        if(selectedLevel == ceiling)
            g.drawImage(select,toolX,toolY,this);

        // draw the tile selection tool
        toolX = Ibox_x + (texSize/2);
        toolY += buttonWidth;
        g.drawImage(selector,toolX,toolY,this);
        toolX += texSize;
        //DRAW THE CURRENT SELECTED TILE HERE
        g.drawImage(textures[selectedTex],toolX,toolY,this);
        toolX += buttonWidth;
        g.drawImage(visible,toolX,toolY,this);
        toolX += texSize;
        g.drawImage(edit,toolX,toolY,this);// draw aother editIcon here
        if(editState== EDIT_TILES)
            g.drawImage(select,toolX,toolY,this);

        //draw the sprite selection tool 
        toolX = Ibox_x + (texSize/2);
        toolY += texSize;        
        g.drawImage(selector,toolX,toolY,this);
        toolX += texSize;
        //DRAW THE CURRENT SELECTED SPRITE HERE
        g.drawImage(sprites[spriteIndex],toolX,toolY,this);
        toolX += buttonWidth;
        g.drawImage(visible,toolX,toolY,this);
        if(!spriteVisible)
            g.drawImage(crossout,toolX,toolY,this);
        toolX += texSize;
        g.drawImage(edit,toolX,toolY,this);
        if(editState== EDIT_SPRITES)
            g.drawImage(select,toolX,toolY,this);

        //draw the actionPoint selection tool 
        toolX = Ibox_x + (texSize/2);
        toolY += texSize;
        g.drawImage(selector,toolX,toolY,this);
        toolX += texSize;
        //DRAW THE CURRENT SELECTED POINT TYPE HERE
        g.drawImage(icons[iconIndex],toolX,toolY,this);
        toolX += buttonWidth; 
        g.drawImage(visible,toolX,toolY,this);
        if(!pointVisible)
            g.drawImage(crossout,toolX,toolY,this);
        toolX += texSize;
        g.drawImage(edit,toolX,toolY,this);
        if(editState == EDIT_POINTS)
            g.drawImage(select,toolX,toolY,this);

        // draw the save Icon
        g.drawImage(save,saveX,saveY,this);
        
        g.drawString((cur_c+offsetC)+","+(cur_r+offsetR),20,20);

    }

    public void moveUp()
    {
        if(offsetR > 0)
            offsetR--;
    }

    public void moveDown()
    {
        offsetR++;
    }

    public void moveLeft()
    {
        if(offsetC > 0)
            offsetC--;
    }

    public void moveRight()
    {
        offsetC++;
    }

    public void mouseClicked(MouseEvent mouse) {}

    public void mousePressed(MouseEvent mouse) 
    {
        if(!placing)//if you are not placing an action point 
        {
            if((mouse.getY()<editWidth)&&(mouse.getX()<editHeight)) //if the click is within the edit area
            {
                cur_r = (int)(mouse.getY()/texMod);
                cur_c = (int)(mouse.getX()/texMod);
                mouseOnGrid = true;

                int actualR = cur_r + offsetR; // your actual coordinate on the map
                int actualC = cur_c + offsetC;

                if((actualR<mapHeight)&&(actualC<mapWidth))//if the click is on the map
                {
                    if(editState== EDIT_TILES) // if you are currently editing tiles
                    {
                        if(mouse.getButton()==2) // if the middle mouse button is clicked, select the first visible block
                        {
                            if(ceilingVisible && ceilingMap[actualR][actualC] !=0)
                            {
                                selectedTex = ceilingMap[actualR][actualC];
                            }
                            else if(groundVisible && worldMap[actualR][actualC] !=0)
                            {
                                selectedTex = worldMap[actualR][actualC];
                            }
                            else
                            {
                                selectedTex = floorMap[actualR][actualC];
                            }
                        }
                        if(selectedLevel == floor)
                        {                    
                            if(mouse.getButton()==3)
                            {
                                floorMap[actualR][actualC] = 0;
                            }
                            else
                                floorMap[actualR][actualC] = selectedTex;
                        }
                        else if(selectedLevel == ground)
                        {
                            if(mouse.getButton()==3)
                            {
                                worldMap[actualR][actualC] = 0;
                            }
                            else
                                worldMap[actualR][actualC] = selectedTex;
                        }
                        else if(selectedLevel == ceiling)
                        {
                            if(mouse.getButton()==3)
                            {
                                ceilingMap[actualR][actualC] = 0;
                            }
                            else
                                ceilingMap[actualR][actualC] = selectedTex;
                        }
                    }
                    else if(editState == EDIT_SPRITES) // if you are currently placing sprites
                    {
                        //write a method to check vacancy and add the ability to remove as well

                        if((spriteAt(actualR,actualC) == -1)&&(worldMap[actualR][actualC] == 0))
                        {
                            if(getNpcVal(spriteIndex) == -1)
                            {
                                if(spriteIndex == 7){levelSprites.add(new Sprite(actualC+spriteOffset,actualR+spriteOffset,spriteIndex));}
                                else{
                                    levelSprites.add(new Sprite(actualC+spriteOffset,actualR+spriteOffset,spriteIndex));}
                            }
                            else
                                levelSprites.add(new Npc(actualC+spriteOffset,actualR+spriteOffset,spriteIndex,getNpcVal(spriteIndex)));
                        }
                        else if((mouse.getButton()==3)&&(spriteAt(actualR,actualC) != -1))
                            levelSprites.remove(spriteAt(actualR,actualC));
                    }
                    else if(editState == EDIT_POINTS)
                    {
                        boolean blockHere = false;
                        boolean spriteHere= false;
                        boolean pointHere = false;
                        if(pointAt(actualR,actualC) != -1)
                            pointHere = true;
                        if(spriteAt(actualR,actualC) != -1)
                            spriteHere = true;  
                        if(worldMap[actualR][actualC] != 0)
                            blockHere = true;
                        if(iconIndex == 2)// the start point is not really an actionPoint
                        {
                            if(worldMap[actualR][actualC] == 0)
                            {
                                playerX = actualC;
                                playerY = actualR;
                            }
                        }
                        else if((mouse.getButton()==3))
                        {
                            // handle deleting actionPoints
                            deletePoint(actualR,actualC);
                        }
                        else if(mouse.getButton()==1)
                        {
                            if(iconIndex == 0 || iconIndex == 1)
                            {
                                tempPointR = actualR;
                                tempPointC = actualC;
                                tempPointType = iconIndex;
                                placing = true;
                            }
                            else
                                levelPoints.add(new ActionPoint(actualR,actualC,actualR,actualC,iconIndex));
                        }

                    }
                }
            }
            else
            {
                mouseOnGrid = false;
                int buttonWidth = 128;
                int toolX = Ibox_x + (texSize/2) + buttonWidth;
                int toolY = Ibox_y + (texSize/2);
                if((toolX<mouse.getX())&&(toolY<mouse.getY())) //if the click is on the level selection area
                {
                    if((mouse.getX()-toolX) < texSize)
                    {
                        if((mouse.getY()-toolY)<texSize) //visibility toggle buttons
                        {
                            floorVisible = !floorVisible;                       
                        }
                        else if((mouse.getY()-toolY)<(2*texSize))
                        {
                            groundVisible = !groundVisible;
                        }
                        else if((mouse.getY()-toolY)<(3*texSize))
                        {
                            ceilingVisible = !ceilingVisible;
                        }
                    }
                    else if((mouse.getX()-toolX) < (2*texSize))
                    {
                        if((mouse.getY()-toolY)<texSize) //selection buttons
                        {
                            selectedLevel = floor;
                            floorVisible = true;
                        }
                        else if((mouse.getY()-toolY)<(2*texSize))
                        {
                            selectedLevel = ground;
                            groundVisible = true;
                        }
                        else if((mouse.getY()-toolY)<(3*texSize))
                        {
                            selectedLevel = ceiling;
                            ceilingVisible = true;
                        }
                    }
                } 
                toolX = Ibox_x + (texSize/2);
                toolY = Ibox_y + (int)(texSize * 3.5);
                //operate the tile selector
                if((toolX<mouse.getX())&&(toolY<mouse.getY())&&(toolY+texSize<mouse.getY())&&(toolY+buttonWidth>mouse.getY())) // if the click is on the first selector
                {
                    if((mouse.getX()-toolX)<texSize)//hit the left button
                    {
                        if(selectedTex>0){
                            selectedTex--;
                            if(selectedTex == 11){selectedTex--;}
                        }
                    }
                    else if(((mouse.getX()-toolX)<(texSize*3))&&(mouse.getX()-toolX)>(texSize*2))//hit the right button
                    {
                        if(selectedTex<numTex-1){
                            selectedTex++;
                            if(selectedTex == 11){selectedTex++;}
                        }
                    }
                    else if(((mouse.getX()-toolX)>(texSize*4))&&((mouse.getX()-toolX)<(texSize*5)))
                    {
                        editState = EDIT_TILES;
                    }
                }  
                toolY+=texSize;
                if((toolX<mouse.getX())&&(toolY<mouse.getY())&&(toolY+texSize<mouse.getY())&&(toolY+buttonWidth>mouse.getY())) // if the click is on the first selector
                {
                    if((mouse.getX()-toolX)<texSize)//hit the left button
                    {
                        if(spriteIndex>0)
                            spriteIndex--;
                    }
                    else if(((mouse.getX()-toolX)<(texSize*3))&&(mouse.getX()-toolX)>(texSize*2))//hit the right button
                    {
                        if(spriteIndex<numSprites-1)
                            spriteIndex++;
                    }
                    else if(((mouse.getX()-toolX)>(texSize*3))&&((mouse.getX()-toolX)<(texSize*4)))
                    {
                        spriteVisible = !spriteVisible;
                    }
                    else if(((mouse.getX()-toolX)>(texSize*4))&&((mouse.getX()-toolX)<(texSize*5)))
                    {
                        editState = EDIT_SPRITES;
                    }
                }  
                toolY+=texSize;
                if((toolX<mouse.getX())&&(toolY<mouse.getY())&&(toolY+texSize<mouse.getY())&&(toolY+buttonWidth>mouse.getY())) // if the click is on the first selector
                {
                    if((mouse.getX()-toolX)<texSize)//hit the left button
                    {
                        if(iconIndex>0)
                            iconIndex--;
                    }
                    else if(((mouse.getX()-toolX)<(texSize*3))&&(mouse.getX()-toolX)>(texSize*2))//hit the right button
                    {
                        if(iconIndex<numIcons-2)//-2 instead of -1 to disallow the placing of doors
                            iconIndex++;
                    }
                    else if(((mouse.getX()-toolX)>(texSize*3))&&((mouse.getX()-toolX)<(texSize*4)))
                    {
                        pointVisible = !pointVisible;
                    }
                    else if(((mouse.getX()-toolX)>(texSize*4))&&((mouse.getX()-toolX)<(texSize*5)))
                    {
                        editState = EDIT_POINTS;
                    }
                }  

                //operate the save button
                if((mouse.getX()>saveX)&&(mouse.getY()>saveY))
                {
                    try{saveLevel();} catch(IOException e) {} //save the level if the save button is pressed
                }
            }
        }
        else //otherwise place the other actionPoint first
        {
            if((mouse.getY()<editWidth)&&(mouse.getX()<editHeight)) //if the click is within the edit area
            {
                cur_r = (int)(mouse.getY()/texMod);
                cur_c = (int)(mouse.getX()/texMod);
                mouseOnGrid = true;
                int actualR = cur_r + offsetR; // your actual coordinate on the map
                int actualC = cur_c + offsetC;

                if((actualR<mapHeight)&&(actualC<mapWidth))//if the click is on the map
                {
                    if((mouse.getButton()==3))
                    {
                        placing = false; // cancel the placing process
                    }
                    else 
                    {
                        boolean blockHere = false;
                        boolean spriteHere= false;
                        boolean pointHere = false;
                        if(pointAt(actualR,actualC) != -1)
                            pointHere = true;
                        if(spriteAt(actualR,actualC) != -1)
                            spriteHere = true;  
                        if(worldMap[actualR][actualC] != 0)
                            blockHere = true;
                        if(iconIndex == 0) // if you're placing a door
                        {
                            if(blockHere)
                            {
                                levelPoints.add(new ActionPoint(tempPointR,tempPointC,actualR,actualC,tempPointType)); // place switch and door
                                placing = false;
                            }                            
                        }
                        else if(iconIndex == 1) // if you're placing a ladder/portal
                        {
                            if(!blockHere)
                            {
                                levelPoints.add(new ActionPoint(tempPointR,tempPointC,actualR,actualC,tempPointType));
                                levelPoints.add(new ActionPoint(actualR,actualC,tempPointR,tempPointC,tempPointType));
                                placing = false;
                            }
                        }
                    }
                }
            }
        }

    }

    public void deletePoint(int r, int c)
    {
        for(ActionPoint a : levelPoints)
        {
            if((r == a.get_r())&&(c == a.get_c()))
                deletePoints.add(a);
            if((r == a.getOpen_r())&&(c == a.getOpen_c()))
                deletePoints.add(a);
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

    public void mouseWheelMoved(MouseWheelEvent mouse)
    {
        if(mouseOnGrid)
        {
            zoomIndex += mouse.getWheelRotation();
            if(zoomIndex<0)
                zoomIndex =0;
            if(zoomIndex>4)
                zoomIndex =4;           
        }
    }

    public void update()
    {

        texMod = (texSize/zoom);
        if((mouse.getY()<editWidth)&&(mouse.getX()<editHeight))
        {
            mouseOnGrid = true;
            cur_r = (int)(mouse.getY()/texMod);
            cur_c = (int)(mouse.getX()/texMod);
        }
        else
        {mouseOnGrid = false;}

        zoom = zooms[zoomIndex];

        for(ActionPoint a : deletePoints)
        {
            levelPoints.remove(a);
        }
    }

    public void keyTyped(KeyEvent key) {}

    public void keyPressed(KeyEvent key)
    {
        process(key.getKeyCode(),true);
    }

    public void keyReleased(KeyEvent key)
    {
        process(key.getKeyCode(),false);
    }

    public void process(int code, boolean pressed)
    {
        key = code;

        switch(code)
        {
            case 37: LEFT = pressed; break;
            case 38: UP = pressed; break;
            case 39: RIGHT = pressed; break;
            case 40: DOWN = pressed; break;
            default:
            //System.out.println(code);
        }

        if(pressed==false)
        {
            keyToggle = false;
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        update();
        repaint();
    }

    public static void main(String[] args)
    {
        new Editor(defaultName);
    }
}

class upAction extends AbstractAction
{
    private Editor myEditor;

    public upAction(Editor myEditor)
    {
        this.myEditor = myEditor;
    }

    public void actionPerformed(ActionEvent e) 
    {
        if(myEditor.offsetR > 0)
            myEditor.offsetR--;
    }

}

class downAction extends AbstractAction
{
    private Editor myEditor;

    public downAction(Editor myEditor)
    {
        this.myEditor = myEditor;
    }

    public void actionPerformed(ActionEvent e) 
    {
        myEditor.offsetR++;
    }

}

class leftAction extends AbstractAction
{
    private Editor myEditor;

    public leftAction(Editor myEditor)
    {
        this.myEditor = myEditor;
    }

    public void actionPerformed(ActionEvent e) 
    {
        if(myEditor.offsetC > 0)
            myEditor.offsetC--;
    }

}

class rightAction extends AbstractAction
{
    private Editor myEditor;

    public rightAction(Editor myEditor)
    {
        this.myEditor = myEditor;
    }

    public void actionPerformed(ActionEvent e) 
    {
        myEditor.offsetC++;
    }

}

