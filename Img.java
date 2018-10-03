import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.awt.Image;

public class Img
{
	JLabel label;
	Image imgimg;
    ImageIcon srcimg;
    ImageIcon img;
	int x, y;
	double angle = 0;
	
	public Img()
	{
		srcimg = null;
	    img = null;
		setPosition(0,0);
	}
	
	public Img(String filename)
	{
		this(filename,"png");
	}
	
	public Img(String filename, String filetype)
	{
		img = loadImage(filename + "." + filetype);
		srcimg = img;
        imgimg = img.getImage();
		setPosition(0,0);
	}
	
	public void rotate(double angle) 
	{
        int w = img.getIconWidth();
        int h = img.getIconHeight();
        int type = BufferedImage.TYPE_INT_ARGB;  // other options, see api
        BufferedImage image = new BufferedImage(h, w, type);
        Graphics2D g2 = image.createGraphics();
        double x = (h - w)/2.0;
        double y = (w - h)/2.0;
        this.angle = angle;
        AffineTransform at = AffineTransform.getTranslateInstance(x, y);
        at.rotate(this.angle, w/2.0, h/2.0);
        g2.drawImage(srcimg.getImage(), at, label);
        g2.dispose();
        img = new ImageIcon(image);
        imgimg = img.getImage();
    }
	
	public void setPosition(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void draw(Graphics g)
	{
		img.paintIcon(null,g,x,y);
	}
    
    public void draw(Graphics g, int w, int h)
    {
        g.drawImage(imgimg,x,y,w,h,null);
    }
	
	protected ImageIcon loadImage(String name)
	{
		java.net.URL url = this.getClass().getResource(name);
		return new ImageIcon(url);
	}
}