import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
/**
 * Npc Class for Enemies and the Like
 */
public class Npc extends Sprite
{
    // instance variables - replace the example below with your own
    boolean aggressive;
    boolean permStatic;
    float dx, dy;
    float speed;
    boolean alive;
    float collRadius; // collision radius
    int state;
    int damage;
    int health;
    int type;
    int currentTex; 
    int texWidth = 64;
    int texHeight = texWidth;
    int anState = 0;
    String name;

    private GameEngine myEngine;

    

    int anFrame = 0;
    
    int walkRight[] = {0,1,2,3,4};
    int walkLeft[] = {5,6,7,8,9};
    int walkFront[] = {10,11,12,13,14};
    int walkBack[] = {15,16,17,18,19};    
    int dead = 20;
    int hit = 21;
    int shoot = 22;
    // animation states
    // 0 = walking
    // 1 = getting hit
    // 2 = shooting

    ///types:
    // type 0 = spaceGuy
    // type 1 = armorAlien
    // type 2 = spaceGuy2
    // type 3 = spaceMerchant

    //states:
    // state 0 = stil or static
    // state 1 = walking
    // state 2 = chase
    // state 3 = dead

    int idle = 0;
    int walk = 1;
    int chase = 2;

    int sightRange = 20; // range at which the NPC can detect you

    float chaseSpeed;
    
    
    int anSwitch = 2; //
    int updates = 0;
    int switchInterval = 20;
    int shootTick = 0;
    int shootInterval = 20;
    int stopPoint = 3; //how close the NPC will get before it stops chasing
    float accVar = 0.18f; // discrepency in weapon accuracy

    public Npc(float x,float y,int texture,int type, GameEngine myEngine)
    {
        super(x,y,texture);
        this.myEngine = myEngine;
        this.type = type;
        defineAttributes(this.type);
    }

    public void defineAttributes(int type)
    {
        switch(type)
        {
            case 0:
            alive = true;
            dx = -1;
            dy = 0;
            state = 1;
            speed = 0.08f;
            health = 20;
            damage = 5;
            currentTex = 2;
            collRadius = 0.4f;
            name = "spaceGuy";
            break;

            case 1:
            alive = true;
            dx = -1;
            dy = 0;
            state = 1;
            speed = 0.03f;
            health = 60;
            damage = 10;
            currentTex = 2;
            collRadius = 0.6f;
            name = "armorAlien";
            break;

            case 2:
            alive = true;
            dx = -1;
            dy = 0;
            state = 1;
            speed = 0.10f;
            health = 50;
            damage = 10;
            currentTex = 2;
            collRadius = 0.4f;
            name = "spaceGuy2";
            break;
            
            case 3:
            alive = true;
            dx = -1;
            dy = 0;
            state = 1;
            speed = 0.00f;
            health = 100;
            damage = 0;
            currentTex = 2;
            collRadius = 0.4f;
            name = "spaceMerchant";
            break;
        }
        aggressive = myEngine.aggressive;
        //aggressive = false;
        //state = chase;
        chaseSpeed = speed*2;
    }

    public void update()
    {
        if(alive)
        {
            aggressive = myEngine.aggressive;
            double nCollSpeed = speed * 4;
            if(type == 3){state = idle;aggressive = false;}// merchant
            if(state == idle)
            {
                anFrame = 0;
                // don't move 
                // if aggressive look for the player, if player found switch to chase
                if(aggressive){findPlayer();}

                // if not aggressive, random chance to switch to walk                
                if(updates % switchInterval == 0)
                {
                    int range = (10) + 1; // 3 == range
                    int chance = (int)(Math.random() * range);
                    if(chance <= 4){state = walk; turnRandom();}

                }
            }            
            else if(state == walk)
            {
                // walk a step in the current direction
                // if wall is hit, turn left, right, or 180°
                // if aggressive look for the player, if player found switch to chase
                // if not aggressive, random chance to switch to idle
                if(aggressive){findPlayer();}

                if((myEngine.worldMap[(int)(x + dx * nCollSpeed)][(int)(getY())]) == (0))
                {
                    x += dx * speed;
                }
                else
                {
                    turnRandom();
                } // random chance

                if((myEngine.worldMap[(int)(x)][(int)(y + dy * nCollSpeed)]) == (0))
                {
                    y += dy * speed;
                }
                else
                {
                    turnRandom();
                } 
                if(updates % anSwitch == 0)
                {
                    if(anFrame == 4)//update frame
                    {anFrame = 0;}
                    else 
                    {anFrame++;}}

                if(updates % switchInterval == 0) // 
                {
                    int range = (6) + 1; // 3 == range
                    int chance = (int)(Math.random() * range);
                    if(chance == 5){turnRandom();}
                    if(chance == 2){state = idle;}
                }
            }
            else if(state == chase)
            {
                findPlayer();
                if(distPlayer()>stopPoint)
                {
                    if((myEngine.worldMap[(int)(x + dx * nCollSpeed)][(int)(getY())]) == (0))
                    {
                        x += dx * speed;
                    }
                    else
                    {
                        state = walk;
                    } // random chance
                    if((myEngine.worldMap[(int)(x)][(int)(y + dy * nCollSpeed)]) == (0))
                    {
                        y += dy * speed;
                    }
                    else
                    {
                        state = walk;
                    }
                    if(updates % anSwitch == 0)
                    {
                        if((anFrame == 4)) //update frame
                        {anFrame = 0;}
                        else 
                        {anFrame++;}
                    }
                }

                if(shootTick % shootInterval == 0)
                {
                    shoot();
                }
                shootTick++;
                // look for player, if wall is between npc and player or player is too far away, set state to walk
                // if player is found set current directions to direct vector path to player
                // fire a projectile with some degree of accuracy towards the player
                // if wall is hit, switch to walk and turn left right or 180
            }
        }
        updates++;
    }

    public double distPlayer()
    {
        double playerX = myEngine.posX; 
        double playerY = myEngine.posY; //get the vector between the player and NPC and then normalize it
        double tempX = playerX-x;
        double tempY = playerY-y;
        double vLength = Math.sqrt((tempX*tempX)+(tempY*tempY)); // actual length of the vector
        return vLength;
    }

    public void findPlayer()
    {
        //find vector between player and NPC
        // if:
        // -the vector is within a given range
        // -the vector does not intersect any walls
        // -the Npc does not have its back turned to the player
        // set the npc's dx and dy to face the player

        float playerX = myEngine.posX; 
        float playerY = myEngine.posY; //get the vector between the player and NPC and then normalize it
        float tempX = playerX-x;
        float tempY = playerY-y;
        float vLength = (float)Math.sqrt((tempX*tempX)+(tempY*tempY)); // actual length of the vector
        float ddx = tempX/vLength; // normalize        
        float ddy = tempY/vLength;

        if((vLength <= sightRange)&&(!wallInWay(ddx,ddy))&&(myEngine.chooseDirection(dx,dy,this) != 1)) // check if player is in range
        {
            if(state != chase)
                state = chase;
            dx = ddx;
            dy = ddy;
        }
        else
        {
            if(state == chase){turnRandom();} 

            state = walk;            
        }

    }
    
    public void lookAtPlayer()
    {
        float playerX = myEngine.posX; 
        float playerY = myEngine.posY; //get the vector between the player and NPC and then normalize it
        float tempX = playerX-x;
        float tempY = playerY-y;
        float vLength = (float)Math.sqrt((tempX*tempX)+(tempY*tempY)); // actual length of the vector
        dx = tempX/vLength; // normalize        
        dy = tempY/vLength;
    }
    
    public boolean wallInWay(double ddx, double ddy)
    {
        float speed2 = 0.01f;
        boolean collided = false;
        float tempX = x;
        float tempY = y;
        float px = (float)Math.floor(myEngine.posX);
        float py = (float)Math.floor(myEngine.posY);

        while(!collided)
        {
            if(myEngine.worldMap[(int)(tempX)][(int)(tempY)] != 0)
            {
                return true;
            }
            if((Math.floor(tempX) == px)&&(Math.floor(tempY) == py)){break;}
            tempX += speed2*ddx;
            tempY += speed2*ddy;
        }

        return collided;
    }

    public void shoot()
    {
        float abVar = accVar + accVar;
        float variance = (float)(Math.random() * abVar)-accVar;

        float sdx = (float)(dx * Math.cos(variance) - dy * Math.sin(variance));
        float sdy = (float)(dy * Math.sin(variance) + dy * Math.cos(variance)); 
        anState = 2;
        myEngine.bullets.add(new Bullet2(x,y,sdx,sdy,1000,damage,false));
    }

    public boolean isAlive()
    {
        return alive;
    }

    public void subtractHealth(int amount)
    {
        health -= amount;
    }

    public void kill()
    {
        alive = false;
        speed = 0.0f;
        state = 3;
    }

    public BufferedImage front()
    {
        if(!alive){return dead();}
        return myEngine.myAssets.output(type,walkFront[anFrame]);
    }

    public BufferedImage back()
    {
        if(!alive){return dead();}
        return myEngine.myAssets.output(type,walkBack[anFrame]);
    }

    public BufferedImage left()
    {
        if(!alive){return dead();}
        return myEngine.myAssets.output(type,walkLeft[anFrame]);
    }

    public BufferedImage right()
    {
        if(!alive){return dead();}
        return myEngine.myAssets.output(type,walkRight[anFrame]);
    }
    
    public BufferedImage hit()
    {return myEngine.myAssets.output(type,hit);}
    
    public BufferedImage dead()
    {return myEngine.myAssets.output(type,dead);}
    
    public BufferedImage shooting()
    {return myEngine.myAssets.output(type,shoot);}

    public float speed()
    {
        return speed;
    }

    public float dx()
    {
        return dx;
    }

    public float dy()
    {return dy;}

    public int getState()
    {
        return state;
    }

    public int getTex()
    {
        return currentTex;
    }

    public int getHealth()
    {
        return health;
    }

    public void turnAround()
    {
        turnLeft();
        turnLeft();
    }

    public void turnLeft()
    {
        float tempx = dx;
        float tempy = dy;
        dx = -tempy;
        dy = tempx;
    }

    public void turnRight()
    {        
        float tempx = dx;
        float tempy = dy;
        dx = tempy;
        dy = -tempx;
    }

    public void turnRandom()
    {
        int range = (3) + 1;
        int chance = (int)(Math.random() * range);
        if(chance == 1){turnAround();}
        if(chance == 2){turnLeft();}
        if(chance == 3){turnAround();}
    }
}
