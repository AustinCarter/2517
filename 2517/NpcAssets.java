import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
/**
 * Unproffesional code by thomas schwalen
 * Written March 1st 2016
 * Attempts to reduce memory consumption by loading fewer images.
 * The programming equivalent of sin written for the sake of optimization
 */
public class NpcAssets
{
    BufferedImage[] spaceGuy = new BufferedImage[23];   // 0
    BufferedImage[] armorAlien = new BufferedImage[23]; // 1
    BufferedImage[] spaceGuy2 = new BufferedImage[23];  // 2  

    
    BufferedImage error;

    public NpcAssets()
    {
        try{loadTex();}catch(IOException e){}
    }

    public void loadTex() throws IOException
    {
        //error = ImageIO.read(new File("portal.png"));
        
        String name = "armorAlien";
        //////////////////////load the first array        
        armorAlien[0] = ImageIO.read(new File(name+"/Right.png"));
        armorAlien[1] = ImageIO.read(new File(name+"/Right1.png"));
        armorAlien[2] = ImageIO.read(new File(name+"/Right2.png"));
        armorAlien[3] = ImageIO.read(new File(name+"/Right3.png"));
        armorAlien[4] = ImageIO.read(new File(name+"/Right4.png"));

        armorAlien[5] = ImageIO.read(new File(name+"/Left.png"));
        armorAlien[6] = ImageIO.read(new File(name+"/Left1.png"));
        armorAlien[7] = ImageIO.read(new File(name+"/Left2.png"));
        armorAlien[8] = ImageIO.read(new File(name+"/Left3.png"));
        armorAlien[9] = ImageIO.read(new File(name+"/Left4.png"));

        armorAlien[10] = ImageIO.read(new File(name+"/Front.png"));
        armorAlien[11] = ImageIO.read(new File(name+"/Front1.png"));
        armorAlien[12] = ImageIO.read(new File(name+"/Front2.png"));
        armorAlien[13] = ImageIO.read(new File(name+"/Front3.png"));
        armorAlien[14] = ImageIO.read(new File(name+"/Front4.png"));

        armorAlien[15] = ImageIO.read(new File(name+"/Back.png"));
        armorAlien[16] = ImageIO.read(new File(name+"/Back1.png"));
        armorAlien[17] = ImageIO.read(new File(name+"/Back2.png"));
        armorAlien[18] = ImageIO.read(new File(name+"/Back3.png"));
        armorAlien[19] = ImageIO.read(new File(name+"/Back4.png"));

        armorAlien[20] = ImageIO.read(new File(name+"/Dead.png"));
        armorAlien[21] = ImageIO.read(new File(name+"/hit.png"));
        armorAlien[22] = ImageIO.read(new File(name+"/shoot.png"));

        name = "spaceGuy";

        spaceGuy[0] = ImageIO.read(new File(name+"/Right.png"));
        spaceGuy[1] = ImageIO.read(new File(name+"/Right1.png"));
        spaceGuy[2] = ImageIO.read(new File(name+"/Right2.png"));
        spaceGuy[3] = ImageIO.read(new File(name+"/Right3.png"));
        spaceGuy[4] = ImageIO.read(new File(name+"/Right4.png"));

        spaceGuy[5] = ImageIO.read(new File(name+"/Left.png"));
        spaceGuy[6] = ImageIO.read(new File(name+"/Left1.png"));
        spaceGuy[7] = ImageIO.read(new File(name+"/Left2.png"));
        spaceGuy[8] = ImageIO.read(new File(name+"/Left3.png"));
        spaceGuy[9] = ImageIO.read(new File(name+"/Left4.png"));

        spaceGuy[10] = ImageIO.read(new File(name+"/Front.png"));
        spaceGuy[11] = ImageIO.read(new File(name+"/Front1.png"));
        spaceGuy[12] = ImageIO.read(new File(name+"/Front2.png"));
        spaceGuy[13] = ImageIO.read(new File(name+"/Front3.png"));
        spaceGuy[14] = ImageIO.read(new File(name+"/Front4.png"));

        spaceGuy[15] = ImageIO.read(new File(name+"/Back.png"));
        spaceGuy[16] = ImageIO.read(new File(name+"/Back1.png"));
        spaceGuy[17] = ImageIO.read(new File(name+"/Back2.png"));
        spaceGuy[18] = ImageIO.read(new File(name+"/Back3.png"));
        spaceGuy[19] = ImageIO.read(new File(name+"/Back4.png"));

        spaceGuy[20] = ImageIO.read(new File(name+"/Dead.png"));
        spaceGuy[21] = ImageIO.read(new File(name+"/hit.png"));
        spaceGuy[22] = ImageIO.read(new File(name+"/shoot.png"));

        name = "spaceGuy2";
        spaceGuy2[0] = ImageIO.read(new File(name+"/Right.png"));
        spaceGuy2[1] = ImageIO.read(new File(name+"/Right1.png"));
        spaceGuy2[2] = ImageIO.read(new File(name+"/Right2.png"));
        spaceGuy2[3] = ImageIO.read(new File(name+"/Right3.png"));
        spaceGuy2[4] = ImageIO.read(new File(name+"/Right4.png"));

        spaceGuy2[5] = ImageIO.read(new File(name+"/Left.png"));
        spaceGuy2[6] = ImageIO.read(new File(name+"/Left1.png"));
        spaceGuy2[7] = ImageIO.read(new File(name+"/Left2.png"));
        spaceGuy2[8] = ImageIO.read(new File(name+"/Left3.png"));
        spaceGuy2[9] = ImageIO.read(new File(name+"/Left4.png"));

        spaceGuy2[10] = ImageIO.read(new File(name+"/Front.png"));
        spaceGuy2[11] = ImageIO.read(new File(name+"/Front1.png"));
        spaceGuy2[12] = ImageIO.read(new File(name+"/Front2.png"));
        spaceGuy2[13] = ImageIO.read(new File(name+"/Front3.png"));
        spaceGuy2[14] = ImageIO.read(new File(name+"/Front4.png"));

        spaceGuy2[15] = ImageIO.read(new File(name+"/Back.png"));
        spaceGuy2[16] = ImageIO.read(new File(name+"/Back1.png"));
        spaceGuy2[17] = ImageIO.read(new File(name+"/Back2.png"));
        spaceGuy2[18] = ImageIO.read(new File(name+"/Back3.png"));
        spaceGuy2[19] = ImageIO.read(new File(name+"/Back4.png"));

        spaceGuy2[20] = ImageIO.read(new File(name+"/Dead.png"));
        spaceGuy2[21] = ImageIO.read(new File(name+"/hit.png"));
        spaceGuy2[22] = ImageIO.read(new File(name+"/shoot.png"));
    }

    public BufferedImage output(int type, int index)
    {
        switch(type)
        {
            case 0:
                return spaceGuy[index];
            case 1:
                return armorAlien[index];
            case 2:
                return spaceGuy2[index];
        }
        return error;
    }
}
