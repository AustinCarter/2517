import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import java.awt.*;
public class textBox
{
    // instance variables - replace the example below with your own
    int width = 400;
    int height = 200;
    String text;
    public textBox(String text)
    {
        this.text = text;
    }

    public int nearestWhitespace(int index)
    {
        for(int i = index; i > 0; i--)
        {
            if(text.charAt(i) == ' '){return i;}
        }
        return 0;
    }

    public BufferedImage output()
    {
        BufferedImage output = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics2D g = output.createGraphics();
        g.setColor(Color.GRAY);
        g.fillRect(0,0,399,10);
        g.fillRect(0,0,10,199);
        g.fillRect(0,190,399,10);
        g.fillRect(390,0,10,199);

        int length = text.length();
        g.setColor(Color.GREEN);
        // text = "Somebody once told me the world is gonna roll me. I ain't the sharpest tool in the shed. She was looking kind of dumb with her finger and her thumb in  the shape of an 'L' on her forehead. Well the years start coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming and they don't stop coming";
        int line = 60;
        int x = 20;
        int y = 25;
        int i = 0;
        while(i<length)
        {
            int e = i + 60;
            if(e>length){e = length;}
            else{e = nearestWhitespace(e);}            
            g.drawString(text.substring(i,e),x,y);
            i = e+1;
            y+=10;
        }

        //g.drawString(text,x,y);
        
        return output;
    }
}
