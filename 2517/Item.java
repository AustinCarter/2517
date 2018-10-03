import java.awt.*;

public abstract class Item
{

    int iType; // 1 = weapon | 2 = armor | 3 = stackable
    String[] data;
    String baseName;
    Img invView = new Img("pistol");
    int category,amount = 1;
    int price;
    int x,y;

    public Item()
    {}
    
    /*public String toString()
    {
        String stats = "";
        
        for(String s: data)
            stats += s + " ";
            
        return stats;
         
    }*/
    public void remove(int amount)
    {
        this.amount -= amount;
    }
    public String[] getData()
    {
        return this.data;
    }
    public Img getImage()
    {
        return this.invView;
    }
    public void draw(Graphics g,int x, int y)
    {      
        invView.setPosition(x,y);
        invView.draw(g);
    }
    public void sdraw(Graphics g,int rx, int ry)
    {      
        invView.setPosition(x-rx+400-30,y-ry+300-30);
        invView.draw(g);
    }
    public String getType()
    {
        return this.data[0];
    } 
     public void setAmount(int amount)
    {
        this.amount = amount;
    }
    
    /*public void setPosition(int x, int y)
    {
       this.x = x; 
       this.y = y; 
    }
    public void draw(Graphics g)
	{
		invView.setPosition(x,y);
		invView.draw(g);
	}*/
}
