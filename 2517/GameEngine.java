import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.*;
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///Thomas Schwalen 2015
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
public class GameEngine extends JPanel implements ActionListener, KeyListener
{
    final int SCREENWIDTH = 800;
    final int SCREENHEIGHT = 600;

    final int door = 0;
    final int ladder = 1;

    final int baseHealth = 200;

    int health = 200;

    float playerColRadius = 0.4f;

    private Title myTitle;
    NpcAssets myAssets;

    String mainFolder = "levels/";
    String levelName = "Prison";
    String dataFileName = "data";
    String floorFileName = "floor";
    String ceilingFileName = "ceiling";
    String groundFileName = "ground";
    String actionFileName = "actionPoints";
    String spriteFileName = "sprites";
    String skyFolder = "skyboxes/";
    String npcFileName = "npcs";
    String[] songs = {"Lines_of_Code.wav","Deeper.wav","KingOfTheDesert.wav","Defiance.wav","Alexandre_Navarro_-_01_-_Lost_Cities",
            "ReignOfAnarchy.wav","Retribution.wav","low-fi.wav","Boarding_Party.wav","ambient_menu.wav","track-1.wav","crazy-space.wav"};
    //squid

    int mapHeight = 24;
    int mapWidth = 24;
    int h = SCREENHEIGHT;

    int texWidth = 64;
    int texHeight = texWidth;

    float baseSpeed = 0.20f;

    float collSpeed = (float)(baseSpeed * 4);

    float moveSpeed = baseSpeed;
    float rotSpeed = 0.16f;  ///spped at which the player rotates
    //predone trig functions for optimization
    float cNegRot = (float)Math.cos(-rotSpeed);
    float sNegRot = (float)Math.sin(-rotSpeed);
    float cPosRot = (float)Math.cos(rotSpeed);
    float sPosRot = (float)Math.sin(rotSpeed);

    double Ninety = (float)Math.toDegrees((Math.PI/2));
    double OneEighty = (float)Math.toDegrees((2*Math.PI)/2);
    double TwoSeventy = (float)Math.toDegrees((3*Math.PI)/2);
    /////////////////////////////////////////////////////
    //
    BufferedImage buffer = new BufferedImage(SCREENWIDTH,SCREENHEIGHT,BufferedImage.TYPE_INT_RGB);
    Graphics2D screen = buffer.createGraphics();

    BufferedImage tex1 = new BufferedImage(texWidth,texHeight,BufferedImage.TYPE_INT_RGB);

    BufferedImage clear;
    BufferedImage hud;

    /////
    int skyWidth = 3200;
    int skyTemp = SCREENWIDTH/2;
    int skyHeight = SCREENHEIGHT/2;

    //there will be an array for music and the skybox

    BufferedImage skybox = new BufferedImage(skyWidth,skyHeight,BufferedImage.TYPE_INT_RGB);
    BufferedImage sky = new BufferedImage(SCREENWIDTH,skyHeight,BufferedImage.TYPE_INT_RGB);
    BufferedImage skyClear = new BufferedImage(SCREENWIDTH,skyHeight,BufferedImage.TYPE_INT_RGB);
    /////

    //int number of textures
    BufferedImage[] texture = new BufferedImage[13];

    //int number of sprite textures
    BufferedImage[] spriteTex = new BufferedImage[13];

    //Skyboxes
    String[] skyboxes = {skyFolder+"greySky.png",skyFolder+"mountains.png",skyFolder+"mountains2.png",skyFolder+"forest.png",skyFolder+"citySky.png"};

    int ceilingMap[][];

    int floorMap[][];

    int worldMap[][];

    int numSprites;

    boolean aggressive;

    ArrayList<Sprite> sprite = new ArrayList<Sprite>();

    //1D zbuffer
    float ZBuffer[] = new float[SCREENWIDTH];

    //arrays used to sort the sprites
    int spriteOrder[];
    float spriteDistance[];

    // action point arrayList
    ArrayList<ActionPoint> actionPoints = new ArrayList<ActionPoint>(); // action points are places on the map the player can interact with

    //player variables
    float posX = 20, posY = 12; // player position
    float dirX = -1, dirY = 0; //beginning directional vector
    float planeX = 0, planeY = 0.66f; //the 2d raycaster version of camera plane

    boolean UP, RIGHT, LEFT, DOWN, SPACE, FIRE, ONE,TWO,THREE,FOUR,FIVE,SIX,SEVEN,EIGHT,NINE,ZERO,DRAW,STRAFE,RELOAD,INVENT,PAUSE;
    boolean fireToggle = false;
    boolean spaceToggle = false;
    boolean drawToggle = false;
    boolean pauseToggle = false;
    boolean paused = false;
    int key = 0;
    //
    // code and data for enemies/NPC's

    //Weapon2 data will be taken from the inventory
    Weapon2[] loadOut = new Weapon2[10];

    int currentIndex = 0;
    Weapon2 currentWeapon2 = loadOut[currentIndex];
    int gunYoffset = 0;
    boolean drawn = true; //is the weapon out?
    boolean switching = false;
    int nextWeapon2 = 0;
    boolean reloading = false; // is the weapon reloading
    boolean firing = false; // if the firing animation is playing
    int frameIndex = 0; // which frame of the firing animation are you at now

    boolean on = true;
    int ptimer = 0;
    boolean look = false;
    //read data variables
    int skyboxIndex;
    int musicIndex;
    int actionPointNum;
    //arraylist of bullets
    ArrayList<Bullet2> bullets = new ArrayList<Bullet2>();
    ArrayList<Bullet2> spentBullets = new ArrayList<Bullet2>();

    String soundFolder = "Sounds/";
    SoundEffect hitSwitch = new SoundEffect(soundFolder + "CLICK09.wav");
    SoundEffect secretButton = new SoundEffect(soundFolder + "computer.wav");
    SoundEffect laserGat = new SoundEffect(soundFolder + "lazer.wav");
    SoundEffect laserGat2 = new SoundEffect(soundFolder + "Laser1.wav");
    SoundEffect realGat = new SoundEffect(soundFolder + "GunShot3.wav");
    SoundEffect autoGat = new SoundEffect(soundFolder + "GUNAUTO1.wav");
    SoundEffect shotGat = new SoundEffect(soundFolder + "shotGun.wav");
    SoundEffect[] gunSounds = {laserGat,realGat,laserGat2,autoGat,shotGat};
    Clip clip;

    // amounts of ammo types
    int pistolAmmo, energyCell, shotgunAmmo, rifleAmmo;

    //
    boolean SPRINT = false;
    //
    int currentMagAmount;
    boolean maginit = false;
    long currentTime = System.nanoTime();
    boolean ready = false;
    

    Collection spice = new Collection("Spice It Up");
    boolean draw = false;
    Inventory playerInv = new Inventory("player");

    textBox active;
    public GameEngine(String name, Title title)
    {
        this.levelName = name;
        this.myTitle = title;
        this.myAssets = new NpcAssets();
        loadOut = myTitle.weapons;
        gunSounds[3].changeVolume(5.0f);
        try{
            loadLevel();
        } catch (IOException e){}
        init();
        javax.swing.Timer timer = new javax.swing.Timer(5, this);
        timer.start();        
        //get and start music

        try{
            AudioInputStream audioInputStream =
                AudioSystem.getAudioInputStream(
                    this.getClass().getResource(soundFolder + songs[musicIndex]));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volume.setValue(-8.0f);
            Thread.sleep(10000);
            //clip.start();
        }
        catch(Exception ex){}

        ready = true;
    }

    public void loadLevel() throws IOException
    {
        File data = new File(mainFolder+levelName+"/"+dataFileName);  ///// get level dimensions from the "data " file
        Scanner parse = new Scanner(data);
        mapWidth = parse.nextInt();
        mapHeight = parse.nextInt();
        skyboxIndex = parse.nextInt();
        musicIndex = parse.nextInt();
        //posX = parse.nextdouble();
        //posY = parse.nextdouble();
        posY = (float)parse.nextDouble();
        posX = (float)parse.nextDouble();
        numSprites = parse.nextInt();
        actionPointNum = parse.nextInt();
        int aggNum = parse.nextInt();
        aggressive = (aggNum == 1) ? true:false; //another wonderful ternary operator
        /// done reading level data

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

        Scanner parse4 = new Scanner(new File(mainFolder+levelName+"/"+actionFileName)); //read actionPoints from 
        parse4.useDelimiter("\\s*,\\s*");
        for(int i = 0; i<actionPointNum;i++)
        {
            if(parse4.hasNextInt())
            {
                int r1 = parse4.nextInt();
                int c1 = parse4.nextInt(); 
                int r2 = parse4.nextInt();
                int c2 = parse4.nextInt();
                int type = parse4.nextInt(); 
                actionPoints.add(new ActionPoint(r1,c1,r2,c2,type));
            }
        }

        Scanner parse5 = new Scanner(new File(mainFolder+levelName+"/"+spriteFileName));
        parse5.useDelimiter("\\s*,\\s*");
        int spriteLoad = numSprites;
        while(parse5.hasNextDouble())
        {
            float r = (float)parse5.nextDouble();
            float c = (float)parse5.nextDouble();
            int tex = parse5.nextInt();
            sprite.add(new Sprite(r,c,tex));
        }
        Scanner parse6 = new Scanner(new File(mainFolder+levelName+"/"+npcFileName));
        parse6.useDelimiter("\\s*,\\s*");
        while(parse6.hasNextDouble())
        {
            float r = (float)parse6.nextDouble();
            float c = (float)parse6.nextDouble();
            int tex = parse6.nextInt();
            int type = parse6.nextInt();
            sprite.add(new Npc(r,c,tex,type,this));
        }

        for(ActionPoint a : actionPoints)
        {
            if(a.getType() == 0)// if the point is a switch/door
            {
                a.setSaveTex(worldMap[a.open_r][a.open_c]);
            }
        }
        spriteOrder = new int[numSprites];
        spriteDistance = new float[numSprites];
    }

    public void setMagAmount()
    {
        if(getGatAmount() < loadOut[currentIndex].magCap){currentMagAmount = getGatAmount();}
        else{currentMagAmount = loadOut[currentIndex].magCap;}
    }

    public void getLoadOut(Weapon2[] loadOut)
    {
        this.loadOut = loadOut;
    }

    public void combSort(int[] order, float[] dist, int amount)
    {
        int gap = amount;
        boolean swapped = false;
        while(gap > 1 || swapped)
        {
            //shrink factor 1.3
            gap = (gap * 10) / 13;
            if(gap == 9 || gap == 10) gap = 11;
            if (gap < 1) gap = 1;
            swapped = false;
            for (int i = 0; i < amount - gap; i++)
            {
                int j = i + gap;
                if (dist[i] < dist[j])
                {
                    float t = dist[i];
                    dist[i] = dist[j];
                    dist[j] = t;                    
                    int t2 = order[i];
                    order[i] = order[j];
                    order[j] = t2;
                    swapped = true;
                }
            }
        }

    }

    public void init()
    {
        //get images and fill texture array
        try{
            texture[0] = ImageIO.read(new File("greystone.png"));
            texture[1] = ImageIO.read(new File("bluestone.png"));
            texture[2] = ImageIO.read(new File("mossy.png"));
            texture[3] = ImageIO.read(new File("purplestone.png"));
            texture[4] = ImageIO.read(new File("redbrick.png"));
            texture[5] = ImageIO.read(new File("wood.png"));
            texture[6] = ImageIO.read(new File("plank.png"));
            texture[7] = ImageIO.read(new File("colorstone.png"));
            texture[8] = ImageIO.read(new File("door.png"));
            texture[9] = ImageIO.read(new File("switchOff.png"));
            texture[10] = ImageIO.read(new File("switchOn.png")); //most of the extra images will be moved to the array later
            texture[11] = ImageIO.read(new File("plankdoor.png"));
            texture[12] = ImageIO.read(new File("wooddoor.png"));

            spriteTex[0] = ImageIO.read(new File("barrel.png"));
            spriteTex[1] = ImageIO.read(new File("pillar.png"));
            spriteTex[2] = ImageIO.read(new File("spaceGuyleft.png"));
            spriteTex[3] = ImageIO.read(new File("ladderup.png"));
            spriteTex[4] = ImageIO.read(new File("ladderdown.png"));
            spriteTex[5] = ImageIO.read(new File("chest.png"));
            spriteTex[6] = ImageIO.read(new File("chest.png"));
            spriteTex[7] = ImageIO.read(new File("chest.png"));
            spriteTex[8] = ImageIO.read(new File("lamp.png"));
            spriteTex[9] = ImageIO.read(new File("flowers.png"));
            spriteTex[10] = ImageIO.read(new File("bush.png"));
            spriteTex[11] = ImageIO.read(new File("shopkeep.png"));
            spriteTex[12] = ImageIO.read(new File("walter.png"));

            skybox = ImageIO.read(new File(skyboxes[skyboxIndex]));

            clear = ImageIO.read(new File("clear.png"));
        } catch (IOException e) {}

    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if(loadOut[currentIndex] != null){
        if(maginit == false){setMagAmount(); maginit = true;}}

        makeSky();

        render();
        g.drawImage(buffer,0,0,this);

        if(loadOut[currentIndex] != null)
        {
            if(reloading == true && gunYoffset == 0){drawn = false; switching = true;}
            if((firing)&&(drawn))
            {
                if(frameIndex != currentWeapon2.shoot.length)
                {
                    hud = currentWeapon2.shoot[frameIndex];
                    frameIndex++;
                }
                else
                {
                    frameIndex = 0;
                    firing = false;
                }
            }
            else
            {try{hud = currentWeapon2.idle();}catch(Exception e){}
            }

            int drawMove = 55;
            int drawBottom = 280;
            if((switching)&&!(drawn)&&(gunYoffset>drawBottom)) 
            {
                if(!reloading) {                
                    currentIndex = nextWeapon2;
                    currentWeapon2 = loadOut[currentIndex];
                } else {reloading = false;}
                maginit = false;
                switching = false;
                drawn = true;
            }
            else if((drawn)&&(switching))
            {
                drawn = false;                
            }

            if((drawn)&&(gunYoffset>0))
            {
                gunYoffset -= drawMove;
            }
            if((!drawn)&&(gunYoffset<drawBottom))
            {
                gunYoffset += drawMove;
            }

            g.drawImage(hud,0,gunYoffset,this);
        }
        int healthBarOffset = 20;
        int hbX = healthBarOffset;
        int hbY = 540;
        int healthBarLength = 160;
        int healthBarHeight = 12;
        double scaleFactor = (double)health / (double)baseHealth;
        int realHbLength = (int)(scaleFactor * healthBarLength);
        g.setColor(Color.RED);
        g.drawRect(hbX,hbY,healthBarLength,healthBarHeight);
        g.fillRect(hbX,hbY,realHbLength,healthBarHeight);
        Font gatteries = new Font("Dialogue", Font.PLAIN, 32);    
        String pewpew = "0/0";
        if(loadOut[currentIndex] != null){
        pewpew = String.format("%d/%d",currentMagAmount,loadOut[currentIndex].magCap);}
        if(switching || !maginit){pewpew = String.format("%d/%d",0,0);}
        g.setFont(gatteries);
        g.setColor(Color.BLACK);
        int xx = 20;
        int yy = 530;
        g.drawString(pewpew, xx+1,yy+1);
        g.drawString(pewpew, xx+1, yy-1);
        g.drawString(pewpew, xx-1, yy+1);
        g.drawString(pewpew, xx-1, yy-1);
        g.setColor(Color.WHITE);
        g.drawString(pewpew,20,530);
        
        if(draw)
            g.drawImage(spice.output(),200,100,this);
    }

    public int getGatAmount()
    {
        int at = loadOut[currentIndex].ammo_type;
        if(at == 1)
        {
            return pistolAmmo;
        }
        if(at == 2)
        {
            return energyCell;
        }
        if(at == 3)
        {
            return shotgunAmmo;
        }
        if(at == 5)
        {
            return rifleAmmo;
        }
        return 0;
    }

    public void decGatAmount()
    {
        int at = loadOut[currentIndex].ammo_type;
        if(at == 1)
        {
            pistolAmmo--;
        }
        else if(at == 2)
        {
            energyCell--;
        }
        else if(at == 3)
        {
            shotgunAmmo--;
        }
        else if(at == 5)
        {
            rifleAmmo--;
        }
    }

    public void render()
    {
        //begin raycasting

        //clear buffer here
        screen.setColor(new Color(0x000000));
        screen.fillRect(0,300,SCREENWIDTH,300);
        screen.drawImage(sky,0,0,null);
        secondLevel();

        for(int x = 0; x < SCREENWIDTH; x++)
        {
            //find position and direction of the ray
            float cameraX = 2 * x /(float)(SCREENWIDTH) - 1;//cameraX is the x-coordinate on the camera plane that the current x-coordinate of the screen represents
            float rayPosX = posX;
            float rayPosY = posY;
            float rayDirX = dirX + planeX * cameraX;
            float rayDirY = dirY + planeY * cameraX;

            //which box of the map we're in  
            int mapX = (int)(rayPosX);
            int mapY = (int)(rayPosY);

            //length of ray from current position to next x or y-side
            float sideDistX;
            float sideDistY;

            //length of ray from one x or y-side to next x or y-side
            float deltaDistX = (float)Math.sqrt(1 + (rayDirY * rayDirY) / (rayDirX * rayDirX));
            float deltaDistY = (float)Math.sqrt(1 + (rayDirX * rayDirX) / (rayDirY * rayDirY));
            float perpWallDist = 0;

            //what direction to step in x or y-direction (either +1 or -1)
            int stepX;
            int stepY;

            int hit = 0; //was there a wall hit?
            int side = 0; //was a NS or a EW wall hit?, only declared here to avoid compile error

            //

            //calculate step and initial sideDist
            if (rayDirX < 0)
            {
                stepX = -1;
                sideDistX = (rayPosX - mapX) * deltaDistX;
            }
            else
            {
                stepX = 1;
                sideDistX = (float)((mapX + 1.0 - rayPosX) * deltaDistX);
            }
            if (rayDirY < 0)
            {
                stepY = -1;
                sideDistY = (rayPosY - mapY) * deltaDistY;
            }
            else
            {
                stepY = 1;
                sideDistY = (float)((mapY + 1.0 - rayPosY) * deltaDistY);
            }
            //perform DDA
            while (hit == 0)
            {
                //jump to next map square, OR in x-direction, OR in y-direction
                if (sideDistX < sideDistY)
                {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0;
                }
                else
                {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1;
                }
                //Check if ray has hit a wall
                if (worldMap[mapX][mapY] > 0) {
                    hit = 1;
                } 
            }
            int lineHeight = 0;
            int drawStart = 0;
            int drawEnd = 0;
            float wallX = 0;
            boolean door;
            //the rest is the same
            //Calculate distance projected on camera direction (oblique distance will give fisheye effect!)
            if (side == 0)
                perpWallDist = Math.abs((mapX - rayPosX + (1 - stepX) * 0.5f) / rayDirX);
            else
                perpWallDist = Math.abs((mapY - rayPosY + (1 - stepY) * 0.5f) / rayDirY);

            //Calculate height of line to draw on screen
            lineHeight = Math.abs((int)(h/ perpWallDist));

            //calculate lowest and highest pixel to fill in current stripe
            drawStart = (int)(-lineHeight * 0.5f + h * 0.5f);
            if(drawStart < 0)drawStart = 0;
            drawEnd = (int)(lineHeight * 0.5f + h * 0.5f);
            if(drawEnd >= h)drawEnd = h - 1;

            //texturing calculations
            int texNum = worldMap[mapX][mapY] - 1; //1 subtracted from it so that texture 0 can be used!

            //calculate value of wallX
            //where exactly the wall was hit, probably need to define initially 
            if (side == 0) wallX = rayPosY + perpWallDist * rayDirY;
            else       wallX = rayPosX + perpWallDist * rayDirX;
            wallX -= Math.floor((wallX));

            //x coordinate on the texture
            int texX = (int)(wallX * (float)(texWidth));
            if(side == 0 && rayDirX > 0) texX = texWidth - texX - 1;
            if(side == 1 && rayDirY < 0) texX = texWidth - texX - 1;

            int color = 0x000000;
            for(int y = drawStart; y<drawEnd; y++)
            {
                int d = y * 256 - h * 128 + lineHeight * 128;  //
                int texY = ((d * texHeight) / lineHeight) / 256;

                //make color darker for y-sides: R, G and B byte each divided through two with a "shift" and an "and"
                //probably gonna have to rewrite most of the color picking section
                try{color = texture[texNum].getRGB(texX,texY);} catch(Exception e){}                        

                if(side == 1) color = ((color >> 1) & 8355711);
                buffer.setRGB(x,y,color);
            } 

            ZBuffer[x] = perpWallDist; // use perpendicular distance
            //FLOOR CASTING
            float floorXWall, floorYWall; //x, y position of the floor texel at the bottom of the wall
            //4 different wall directions possible
            if(side == 0 && rayDirX > 0)
            {
                floorXWall = mapX;
                floorYWall = mapY + wallX;
            }
            else if(side == 0 && rayDirX < 0)
            {
                floorXWall = mapX + 1.0f;
                floorYWall = mapY + wallX;
            }
            else if(side == 1 && rayDirY > 0)
            {
                floorXWall = mapX + wallX;
                floorYWall = mapY;
            }
            else
            {
                floorXWall = mapX + wallX;
                floorYWall = mapY + 1.0f;
            } 

            BufferedImage floorTex = new BufferedImage(texWidth,texHeight,BufferedImage.TYPE_INT_RGB);
            BufferedImage ceilTex = new BufferedImage(texWidth,texHeight,BufferedImage.TYPE_INT_RGB);
            float distWall, distPlayer, currentDist;  

            distWall = perpWallDist;
            distPlayer = 0.0f;

            if (drawEnd < 0) drawEnd = h;

            for(int y = drawEnd + 1; y < h; y++)
            {
                currentDist = h / (2.0f * y - h); //you could make a small lookup table for this instead

                float weight = (currentDist - distPlayer) / (distWall - distPlayer);

                float currentFloorX = weight * floorXWall + (1.0f - weight) * posX;
                float currentFloorY = weight * floorYWall + (1.0f - weight) * posY;

                //temporary texture picking 
                if(floorMap[(int)(currentFloorX)][(int)(currentFloorY)]!=0)
                {
                    floorTex = texture[(floorMap[(int)(currentFloorX)][(int)(currentFloorY)])-1];
                }
                else
                    floorTex = texture[0];

                if(ceilingMap[(int)(currentFloorX)][(int)(currentFloorY)]!= 0)
                {
                    ceilTex = texture[(ceilingMap[(int)(currentFloorX)][(int)(currentFloorY)])-1];
                }
                else
                    ceilTex = clear;

                int floorTexX, floorTexY;
                floorTexX = (int)(currentFloorX * texWidth) % texWidth;
                floorTexY = (int)(currentFloorY * texHeight) % texHeight;

                int floorColor = (floorTex.getRGB(floorTexX,floorTexY) >> 1) & 8355711;
                //floor
                if(ceilingMap[(int)(currentFloorX)][(int)(currentFloorY)]!= 0)
                {
                    buffer.setRGB(x,y,(floorColor >> 1) & 8355711);
                }
                else
                    buffer.setRGB(x,y,floorColor);
                //ceiling (symmetrical!)
                if(ceilTex != clear)
                    buffer.setRGB(x,h - y,ceilTex.getRGB(floorTexX,floorTexY));
            }
        }

        //SPRITE CASTING
        //sort sprites from far to close
        for(int i = 0; i < numSprites; i++)
        {
            spriteOrder[i] = i;
            spriteDistance[i] = (float)((posX - sprite.get(i).getX()) * (posX - sprite.get(i).getX()) + (posY - sprite.get(i).getY()) * (posY - sprite.get(i).getY())); //sqrt not taken, unneeded
        }

        combSort(spriteOrder, spriteDistance, numSprites);

        //after sorting the sprites, do the projection and draw them
        for(int i = 0; i < numSprites; i++)
        {
            //translate sprite position to relative to camera
            float spriteX = sprite.get(spriteOrder[i]).getX() - posX;
            float spriteY = sprite.get(spriteOrder[i]).getY() - posY;

            float invDet = 1.0f / (planeX * dirY - dirX * planeY); //required for correct matrix multiplication

            float transformX = invDet * (dirY * spriteX - dirX * spriteY);
            float transformY = invDet * (-planeY * spriteX + planeX * spriteY); //this is actually the depth inside the screen, that what Z is in 3D       

            int spriteScreenX = (int)((SCREENWIDTH * 0.5f) * (1 + transformX / transformY));

            //calculate height of the sprite on screen
            int spriteHeight = Math.abs((int)(h / (transformY))); //using "transformY" instead of the real distance prevents fisheye
            //calculate lowest and highest pixel to fill in current stripe
            int drawStartY = (int)(-spriteHeight * 0.5f + h * 0.5f);
            if(drawStartY < 0) drawStartY = 0;
            int drawEndY = (int)(spriteHeight * 0.5f + h * 0.5f);
            if(drawEndY >= h) drawEndY = h - 1;

            //calculate width of the sprite
            int spriteWidth = Math.abs((int)(h / (transformY)));
            int drawStartX = (int)(-spriteWidth * 0.5f + spriteScreenX);
            if(drawStartX < 0) drawStartX = 0;
            int drawEndX = (int)(spriteWidth * 0.5f + spriteScreenX);
            if(drawEndX >= SCREENWIDTH) drawEndX = SCREENWIDTH - 1;

            //loop through every vertical stripe of the sprite on screen
            for(int stripe = drawStartX; stripe < drawEndX; stripe++)
            {
                int texX = (int)(256 * (stripe - (-spriteWidth * 0.5f + spriteScreenX)) * texWidth / spriteWidth) / 256;

                if(transformY > 0 && stripe > 0 && stripe < SCREENWIDTH && transformY < ZBuffer[stripe]) 
                    for(int y = drawStartY; y < drawEndY; y++) //for every pixel of the current stripe
                    {
                        int d = (y) * 256 - h * 128 + spriteHeight * 128; //256 and 128 factors to avoid doubles
                        int texY = ((d * texHeight) / spriteHeight) / 256;
                        int color = 0x000000;
                        if(sprite.get(spriteOrder[i]) instanceof Npc)
                        {
                            Npc npc = (Npc) sprite.get(spriteOrder[i]);
                            int spriteDir = chooseDirection(npc.dx(),npc.dy(),npc);
                            BufferedImage temp = npc.right();
                            if(npc.anState == 0)// if the animation state is walking
                            {
                                if(spriteDir == 0)
                                {temp = npc.front();}
                                else if(spriteDir == 1)
                                {temp = npc.back();}
                                else if(spriteDir == 2)
                                {temp = npc.left();}
                                else if(spriteDir == 3)
                                    temp = npc.right();
                            }
                            else if(npc.anState == 1) // getting hit
                            {
                                temp = npc.hit();
                            }
                            else if(npc.anState == 2) // shooting
                            {
                                temp = npc.shooting();
                            }
                            else if(npc.anState == 3) // shooting
                            {
                                temp = npc.dead();
                            }
                            try{color = temp.getRGB(texX,texY);}catch(ArrayIndexOutOfBoundsException a){}
                        }
                        else
                            try{color = spriteTex[sprite.get(spriteOrder[i]).getTex()].getRGB(texX,texY);}catch(ArrayIndexOutOfBoundsException a){} //get current color from the texture
                        //I'm getting errors in the above statement and I'm not sure why. I may just need to write a catch statement if all else fails
                        if((color & 0x00FFFFFF) != 0) buffer.setRGB(stripe,y,color); //paint pixel if it isn't black, black is the invisible color
                    }
            }
        }
    }

    public void secondLevel()
    {
        int secondLevelOffset;
        int lastCell = 0; //last cell, if 0, cell is visible
        for(int x = 0; x < SCREENWIDTH; x++)
        {
            //make a stack of walls
            Stack<Stripe> walls = new Stack<Stripe>();

            //find position and direction of the ray
            float cameraX = 2 * x /(float)(SCREENWIDTH) - 1;//cameraX is the x-coordinate on the camera plane that the current x-coordinate of the screen represents
            float rayPosX = posX;
            float rayPosY = posY;
            float rayDirX = dirX + planeX * cameraX;
            float rayDirY = dirY + planeY * cameraX;

            //which box of the map we're in  
            int mapX = (int)(rayPosX);
            int mapY = (int)(rayPosY);

            //length of ray from current position to next x or y-side
            float sideDistX;
            float sideDistY;

            //length of ray from one x or y-side to next x or y-side
            float deltaDistX = (float)Math.sqrt(1 + (rayDirY * rayDirY) / (rayDirX * rayDirX));
            float deltaDistY = (float)Math.sqrt(1 + (rayDirX * rayDirX) / (rayDirY * rayDirY));
            float perpWallDist;

            //what direction to step in x or y-direction (either +1 or -1)
            int stepX;
            int stepY;

            int hit = 0; //was there a wall hit?
            int side = 0; //was a NS or a EW wall hit?, only declared here to avoid compile error

            //calculate step and initial sideDist
            if (rayDirX < 0)
            {
                stepX = -1;
                sideDistX = (rayPosX - mapX) * deltaDistX;
            }
            else
            {
                stepX = 1;
                sideDistX = (mapX + 1.0f - rayPosX) * deltaDistX;
            }
            if (rayDirY < 0)
            {
                stepY = -1;
                sideDistY = (rayPosY - mapY) * deltaDistY;
            }
            else
            {
                stepY = 1;
                sideDistY = (mapY + 1.0f - rayPosY) * deltaDistY;
            }

            //perform DDA
            while (hit == 0)
            {
                boolean visible = true; //determines if the strip is visible or not

                //jump to next map square, OR in x-direction, OR in y-direction
                if (sideDistX < sideDistY)
                {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0;
                }
                else
                {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1;
                }
                //Check if ray has hit a wall
                //for the ceiling, keep casting if there is no wall beneath
                if(lastCell != 0)   visible = false; //if the previous casted cell is '0', then the stripe will be visible
                if((mapX==(int)(posX)) && (mapY==(int)(posY))) visible = false; //if the player is underneath, the cell will not be visible

                if (ceilingMap[mapX][mapY] > 0) 
                {
                    if(worldMap[mapX][mapY] > 0)    
                    {
                        if(visible)
                        {
                            walls.push(new Stripe(stepX,stepY,mapX,mapY,side));
                        }
                        hit = 1;//if there is a wall beneath, go ahead and stop casting
                    }
                    else
                    {                        
                        if(visible)
                        {
                            walls.push(new Stripe(stepX,stepY,mapX,mapY,side));//if there is not a wall beneath, add it but keep casting
                        }                           
                    }
                }
                lastCell = ceilingMap[mapX][mapY];
                // if the ceiling ray goes through at least one clear area, the wall stripe will not be hidden
            } 
            int lineHeight = 0;
            int drawStart = 0;
            int drawEnd = 0;
            int drawNum = 0;
            int texNum = 0;
            float wallX = 0;

            while(!walls.empty())
            {
                stepX = walls.peek().stepX; //set stepX and stepY to their Correct Values
                stepY = walls.peek().stepY;
                mapX = walls.peek().mapX;         //set mapX and mapY to their Correct Values
                mapY = walls.peek().mapY;
                side = walls.peek().side;                          //side side to its correct value

                //the rest is the same
                //Calculate distance projected on camera direction (oblique distance will give fisheye effect!)
                if (side == 0)
                    perpWallDist = Math.abs((mapX - rayPosX + (1 - stepX) * 0.5f) / rayDirX);
                else
                    perpWallDist = Math.abs((mapY - rayPosY + (1 - stepY) * 0.5f) / rayDirY);

                //Calculate height of line to draw on screen
                lineHeight = Math.abs((int)(h/ perpWallDist));

                //calculate lowest and highest pixel to fill in current stripe
                drawStart = (int)(-lineHeight * 0.5f + h * 0.5f);
                if(drawStart < 0)drawStart = 0;
                drawEnd = (int)(lineHeight * 0.5f + h * 0.5f);
                if(drawEnd >= h)drawEnd = h - 1;

                //texturing calculations
                texNum = ceilingMap[mapX][mapY] - 1; //1 subtracted from it so that texture 0 can be used!

                //calculate value of wallX
                //where exactly the wall was hit, probably need to define initially 
                if (side == 1) wallX = rayPosX + ((mapY - rayPosY + (1 - stepY) * 0.5f) / rayDirY) * rayDirX;
                else       wallX = rayPosY + ((mapX - rayPosX + (1 - stepX) / 2) / rayDirX) * rayDirY;
                wallX -= Math.floor((wallX));

                //x coordinate on the texture
                int texX = (int)(wallX * (float)(texWidth));
                if(side == 0 && rayDirX > 0) texX = texWidth - texX - 1;
                if(side == 1 && rayDirY < 0) texX = texWidth - texX - 1;

                int color = 0x000000;
                secondLevelOffset = lineHeight-1;
                for(int y = drawStart; y<drawEnd; y++)
                {
                    int d = y * 256 - h * 128 + lineHeight * 128;  //256 and 128 factors to avoid doubles
                    int texY = ((d * texHeight) / lineHeight) / 256;
                    //color = new Color(texture[texNum].getRGB(texX,texHeight * texY));
                    try{color = texture[texNum].getRGB(texX,texY);}catch(ArrayIndexOutOfBoundsException a){}
                    //make color darker for y-sides: R, G and B byte each divided through two with a "shift" and an "and"
                    if(side == 1) color = ((color >> 1) & 8355711);
                    if(y-secondLevelOffset > -1)
                    {buffer.setRGB(x,y-secondLevelOffset,color);
                    }                        
                } 
                walls.pop();

            }

        }
    }

    public void update()
    {
        //player movement

        if(on)
        {
            if((PAUSE)&&(!pauseToggle)&&ptimer>5)
            {
                paused = !paused;
                pauseToggle = true;
                ptimer = 0;
            }

            if((!paused))//
            {
                if(!look)ptimer++;
                gameUpdate();
            }
        }
        else
        {
            clip.stop();
        }
    }

    public void updateLoadout()
    {
        myTitle.makeLoadOut();
        loadOut = myTitle.weapons;
    }

    public void gameUpdate()
    {

        playerUpdate();
        //update NPC's
        int stateWalk = 1;

        for(Sprite n : sprite)
        {
            if(n instanceof Npc) //checks if the index is an NPC or Just a static Sprite
            {
                Npc npc = (Npc) n;      
                if(npc.anState != 0)
                    npc.anState = 0;
                if(npc.getHealth() <= 0)
                {
                    npc.kill();
                }
                npc.update();
                n = npc;
            }
        }   

        //update bullets
        for(Bullet2 b : bullets) // for every bullet
        {
            for(int i = 0; i < 10; i++) //update 10 times to keep accuracy and speed
            {
                // IF THE BULLET IS FRIENDLY, add code for enemy bullets later
                if(b.friendly == true)
                {
                    for(Sprite n : sprite)
                    {
                        if(n instanceof Npc) //check if the bullet has struck an enemy
                        {
                            Npc npc = (Npc) n;
                            if((distance(b.x,b.y,npc.getX(),npc.getY()) <= npc.collRadius)&&(npc.isAlive()))
                            {
                                npc.subtractHealth(b.damage);
                                npc.anState = 1; // set the animation state to being hit
                                npc.lookAtPlayer();//look at the player
                                if(npc.aggressive == false)
                                {aggressive = true;}                                
                                b.setSpeed(0.0f); //temporary solution
                                b.x = 0.0f;
                                b.y = 0.0f;
                            }
                            n = npc;
                        }

                    }
                    if(worldMap[(int)b.x][(int)b.y] != 0)
                    {
                        spentBullets.add(b);
                        i = 10;
                    }
                }
                else
                {
                    if(distance(b.x,b.y,posX,posY) <= playerColRadius)
                    {
                        health -= b.damage;
                        b.x = 0.0f;
                        b.y = 0.0f;
                        spentBullets.add(b);
                    }
                }
                b.update();
                if(b.age>=b.range){spentBullets.add(b);  break;}               
            }

        }

        for(Bullet2 b : spentBullets)
        {
            bullets.remove(b);
        }

    }

    public void playerUpdate()
    {
        //System.out.println(dirX +","+dirY);

        boolean moved = false;
        //cycle through your action bar in the most ineffecient way possible
        // switiching weapons will also unholster your weapon
        if((ZERO)&&(currentIndex!=0)) {nextWeapon2 = 0; switching = true;} 
        if((ONE)&&(currentIndex!=1)) {nextWeapon2 = 1; switching = true;}
        if((TWO)&&(currentIndex!=2)) {nextWeapon2 = 2; switching = true;}
        if((THREE)&&(currentIndex!=3)) {nextWeapon2 = 3; switching = true;}
        if((FOUR)&&(currentIndex!=4)) {nextWeapon2 = 4; switching = true;}
        if((FIVE)&&(currentIndex!=5)) {nextWeapon2 = 5;switching = true;}
        if((SIX)&&(currentIndex!=6)) {nextWeapon2 = 6; switching = true;}
        if((SEVEN)&&(currentIndex!=7)) {nextWeapon2 = 7; switching = true;}
        if((EIGHT)&&(currentIndex!=8)) {nextWeapon2 = 8; switching = true;}
        if((NINE)&&(currentIndex!=9)) {nextWeapon2 = 9; switching = true;}
        if(loadOut[currentIndex] == null) {drawn = false;}
        if(loadOut[nextWeapon2] == null) {switching = false;}
        currentWeapon2 = loadOut[currentIndex];
        if(loadOut[currentIndex] != null)currentWeapon2.getTextures();
        // move foreward if there is no wall

        if(RELOAD)
        {
            reloading = true;
            //spice.checkComplete();
        }

        //reset speed
        moveSpeed = baseSpeed;
        if(SPRINT)
        {
            moveSpeed = 0.36f;
        }

        //we're gonna replace spinting with actions because I don't feel like handling new keys right now
        if((SPACE)&&(!spaceToggle))
        {
            interactionHandler();
            spaceToggle = true;
        }

        if(loadOut[currentIndex] != null){
            if((FIRE)&&(!fireToggle)&&(!firing)&&(gunYoffset == 0)&& drawn && (currentMagAmount != 0))
            {            
                gunSounds[currentWeapon2.sound].play();
                if(currentWeapon2.auto)
                    gunSounds[currentWeapon2.sound].setLooping(true);
                if(currentWeapon2.kind == 2)// shotgun
                {
                    fireShotgun();
                }
                else
                    bullets.add(new Bullet2(posX,posY,dirX,dirY,currentWeapon2.range,currentWeapon2.damage,true));
                firing = true;
                currentMagAmount--;
                decGatAmount();
                if(!currentWeapon2.auto)
                    fireToggle = true;
            }
            else if((FIRE)&&(!drawn))
                drawn = true;
            if(!firing && currentWeapon2.auto == true)
            {   
                try{
                    gunSounds[currentWeapon2.sound].stop();}catch(Exception e){}}
        }    

        if((DRAW)&&(!drawToggle))
        {
            drawn = !drawn;
            drawToggle = true;
        }

        if (UP)
        {
            if((worldMap[(int)(posX + dirX * collSpeed)][(int)(posY)]) == (0)) posX += dirX * moveSpeed;
            if((worldMap[(int)(posX)][(int)(posY + dirY * collSpeed)]) == (0)) posY += dirY * moveSpeed;
        }
        //move backward if there is no wall
        if (DOWN)
        {
            if((worldMap[(int)(posX - dirX * collSpeed)][(int)(posY)]) == (0)) posX -= dirX * moveSpeed;
            if((worldMap[(int)(posX)][(int)(posY - dirY * collSpeed)]) == (0)) posY -= dirY * moveSpeed;
        }
        double strafeSpeed = 2*(moveSpeed/3);
        //rotate to the right   
        if (RIGHT)
        {
            //both camera direction and camera plane must be rotated
            ////////////////////////////////////////
            ////may need to create trig values beforehand depending on how slow java does trig
            ////
            ////////////////////////////////////////
            if(STRAFE)
            {
                if((worldMap[(int)(posX + dirY * collSpeed)][(int)(posY)]) == (0)) posX += dirY * strafeSpeed;
                if((worldMap[(int)(posX)][(int)(posY - dirX * collSpeed)]) == (0)) posY += -dirX * strafeSpeed;
            }
            else
            {
                float oldDirX = dirX;
                dirX = dirX * cNegRot - dirY * sNegRot;
                dirY = oldDirX * sNegRot + dirY * cNegRot;
                float oldPlaneX = planeX;
                planeX = planeX * cNegRot - planeY * sNegRot;
                planeY = oldPlaneX * sNegRot + planeY * cNegRot;
            }
        }
        //rotate to the left
        if (LEFT)
        {
            //both camera direction and camera plane must be rotated
            if(STRAFE)
            {
                if((worldMap[(int)(posX - dirY * collSpeed)][(int)(posY)]) == (0)) posX += -dirY * strafeSpeed;
                if((worldMap[(int)(posX)][(int)(posY + dirX * collSpeed)]) == (0)) posY += dirX * strafeSpeed;
            }
            else
            {

                float oldDirX = dirX;
                dirX = dirX * cPosRot - dirY * sPosRot;
                dirY = oldDirX * sPosRot + dirY * cPosRot;
                float oldPlaneX = planeX;
                planeX = planeX * cPosRot - planeY * sPosRot;
                planeY = oldPlaneX * sPosRot + planeY * cPosRot;
            }
        }

    }

    public void fireShotgun()
    {

        for(int i = 0; i < 6; i++)
        {
            float variance = (float)((Math.ceil(Math.random() * 16)-8)/100); // value between -0.08 and 0.08
            //System.out.println(variance);
            float bulletX = (float)(dirX * Math.cos(variance) - dirY * Math.sin(variance));
            float bulletY = (float)(dirX * Math.sin(variance) + dirY * Math.cos(variance));
            bullets.add(new Bullet2(posX,posY,bulletX,bulletY,currentWeapon2.range,currentWeapon2.damage,true));
        }

    }

    public void makeSky()
    {
        sky = skyClear;
        double angle = Math.toDegrees(Math.atan2(dirY,dirX));
        if(angle<0) angle = (angle+360);
        int initialX = (int)((angle/360)*skyWidth);
        initialX = skyWidth-initialX;
        if(initialX+800>skyWidth)
        {
            //x+width is greater than the skybox width
            int sky1X = initialX;
            int sky1Width = skyWidth-sky1X;
            int sky2X = 0;
            int sky2Width = SCREENWIDTH-sky1Width;
            Graphics2D skyG = sky.createGraphics();
            BufferedImage sky1 = skybox.getSubimage(sky1X,0,sky1Width,skyHeight);
            BufferedImage sky2 = skybox.getSubimage(sky2X,0,sky2Width,skyHeight);
            skyG.drawImage(sky1,0,0,null);
            skyG.drawImage(sky2,sky1Width,0,null);
            skyG.dispose();
        }
        else
        {
            sky = skybox.getSubimage(initialX,0,SCREENWIDTH,skyHeight);
        }

    }

    public void interactionHandler()
    {
        double checkAhead = 0.75;
        int r1 = (int)(posX + dirX * checkAhead); //coordinates of the far tile to check
        int c1 = (int)(posY + dirY * checkAhead);
        int r2 = (int)(posY);                       //coordinates of the close tile to check
        int c2 = (int)(posX);

        //System.out.println("Handling!");

        getActionPoint(r1,c1);
        //getActionPoint(r2,c2);
    }

    public void getActionPoint(int r,int c)
    {
        for(ActionPoint p : actionPoints)
        {
            //System.out.println("Checking!");
            if(p.get_r() == r && p.get_c() == c) 
            {
                if(p.getType() == door) // do this if it is a door
                {
                    if(!p.isOn())
                    {
                        if(worldMap[p.get_r()][p.get_c()] == 10)
                            worldMap[p.get_r()][p.get_c()] = 11; //change the switch texture to off
                        //playSound
                        hitSwitch.play();
                        openDoor(p.getOpen_r(),p.getOpen_c());
                    }
                    else
                    {
                        if(worldMap[p.get_r()][p.get_c()] == 11)
                            worldMap[p.get_r()][p.get_c()] = 10;
                        closeDoor(p.getOpen_r(),p.getOpen_c(),p);
                    }
                    p.switchSetting();
                }
                else if(p.getType() == ladder)
                {
                    teleport(p.getOpen_r(),p.getOpen_c());
                }
                else if(p.getType() == 3) // exit type
                {
                    on = false;
                }
                else if(p.getType() == 4 && ptimer>10) // container/store
                {
                    ptimer = 0;
                    try{
                        Scanner getType = new Scanner(new File(mainFolder+levelName+"/store"+p.getOpen_r()));
                        int typ = 0;
                        try{typ = getType.nextInt();} catch(Exception e){}                       
                        myTitle.setTempCot(mainFolder+levelName+"/store"+p.getOpen_r(),"store"+p.getOpen_r(),typ);
                        look = true;
                    } catch(IOException e){}

                }
                else if(p.getType() == 5)
                {
                    spice.checkComplete();
                    //active = spice.output();
                    draw = !draw;
                    playerInv = spice.playerInv;
                    //System.out.println("I am questing it up muddatruka");
                }
            }
        }
    }

    public int[] getAmmoAmounts()
    {
        int[] ammoAmounts = new int[4];
        ammoAmounts[0] = pistolAmmo;
        ammoAmounts[1] = energyCell;
        ammoAmounts[2] = shotgunAmmo;
        ammoAmounts[3] = rifleAmmo;

        return ammoAmounts;
    }

    public int chooseDirection(double ddx, double ddy, Npc n) // this is for choosing the image of the sprite to be displayed
    {
        int front = 0;
        int back = 1;
        int left = 2;
        int right = 3;
        int dx = (int)Math.round(ddx); 
        int dy = (int)Math.round(ddy);

        double sig = Math.max(Math.abs(dirX),Math.abs(dirY));
        int px = (int)Math.round(dirX); 
        int py = (int)Math.round(dirY);

        int chase = 2;

        if(n.state == 2)
            return front;

        if(sig == Math.abs(dirY)) //if the player is facing up or down
        {
            px = 0;
            if(px == dx)
            {
                if(py == dy)
                {
                    return back;
                }
                else
                    return front;
            }
            else if(py==dx)
            {
                return right;
            }
            else
                return left;
        }
        else if(sig == Math.abs(dirX)) // if the player is facing left or right
        {
            py = 0;
            if(py == dy)
            {
                if(px == dx)
                {
                    return back;
                }
                else
                    return front;
            }
            else if(px == dy)
            {
                return left;
            }
            else
                return right;
        }
        else
            return right;

    }

    public void teleport(int r, int c)
    {
        posX = (float)(r) + 0.5f; // offset to avoid spawning inside blocks
        posY = (float)(c) + 0.5f; // r and c revesred for some reason
    }

    public void openDoor(int r, int c)
    {
        worldMap[r][c] = 0;
        //System.out.println("Opened!");
    }

    public void closeDoor(int r, int c, ActionPoint p)
    {
        worldMap[r][c] = p.getSaveTex();
    }

    public void actionPerformed(ActionEvent e)
    {
        update();

        long newTime = System.nanoTime();
        long diff = newTime-currentTime;
        if(diff > 15000000){
            //update();
            repaint();
            currentTime = newTime;
        }

        //repaint();
    }

    public float distance(float x1, float y1, float x2, float y2)
    {
        float xdif = x2-x1;
        float ydif = y2-y1;
        return (float)Math.sqrt((xdif*xdif)+(ydif*ydif));
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
            case 81: 
            PAUSE = pressed;
            if(pressed == false){pauseToggle = false;}
            break;
            case 37: LEFT = pressed; break;
            case 39: RIGHT = pressed; break;
            case 38: UP = pressed; break;
            case 40: DOWN = pressed; break;
            case 32: 
            SPACE = pressed;
            if(pressed == false){spaceToggle = false;}
            break;
            case 70:
            FIRE = pressed;
            if(pressed == false){fireToggle = false;}            
            break;
            case 65:
            STRAFE = pressed;
            break;
            case 68:
            DRAW = pressed;
            if(pressed == false){drawToggle = false;}
            break;
            case 69:
            INVENT = pressed;
            break;
            case 82:
            RELOAD = pressed;
            break;
            case 83:
            SPRINT = pressed;
            break;
            case 48: ZERO = pressed;
            break;
            case 49: ONE= pressed;
            break;
            case 50: TWO= pressed;
            break;
            case 51: THREE = pressed;
            break;
            case 52: FOUR = pressed;
            break;
            case 53: FIVE= pressed;
            break;
            case 54: SIX= pressed;
            break;
            case 55: SEVEN= pressed;
            break;
            case 56: EIGHT= pressed;
            break;
            case 57: NINE= pressed;
            break;

            case 96: ZERO = pressed;
            break;
            case 97: ONE= pressed;
            break;
            case 98: TWO= pressed;
            break;
            case 99: THREE = pressed;
            break;
            case 100: FOUR = pressed;
            break;
            case 101: FIVE= pressed;
            break;
            case 102: SIX= pressed;
            break;
            case 103: SEVEN= pressed;
            break;
            case 104: EIGHT= pressed;
            break;
            case 105: NINE= pressed;
            break;

            //default:
            //System.out.println(code);
        }

    }

    public static void main(String[] args)
    {
        new GameEngine("Arena",new Title());
    }
}