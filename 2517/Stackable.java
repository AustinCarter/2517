import java.awt.*;

public class Stackable extends Item
{
    String type;
    //String baseName = "Pistol Ammo";

    public Stackable(String [] data)
    {
        this.data = data;
        type = data[0];
        this.amount = Integer.parseInt(data[1]);
        setCategory();
        invView = new Img(type);
        this.iType = 3;
        baseName = type;
    }

    public Stackable(String type, int amount)
    {
        this.data = new String[] {type,"" + amount};
        this.type = type;
        this.amount = amount;
        setCategory();
        invView = new Img(type);
        this.iType = 3;
        baseName = type;
    }

    public void add(int amount)
    {
        this.amount += amount;
    }

    public void remove(int amount)
    {
        this.amount -= amount;
    }

    public void merge(Stackable s)
    {
        add(s.getAmount());
    }

    public boolean sameType(Stackable s)
    {
        if(this.type == s.type)
            return true;
        return false;
    }

    public void draw(Graphics g,int x, int y)
    {      
        invView.setPosition(x,y);
        invView.draw(g);
        
        g.setFont(new Font("Dialog", Font.BOLD, 12));
        g.setColor(Color.BLACK);
        g.drawString("" + amount, x + 40, y + 51);
        g.drawString("" + amount, x + 40, y + 49);
        g.drawString("" + amount, x + 41, y + 50);
        g.drawString("" + amount, x + 39, y + 50);
        g.setColor(Color.WHITE);
        g.drawString("" + amount, x + 40, y + 50);
    }

    public void setCategory()
    {
        if(type.equals("Money")){category = 0;}
        else if(type.equals("Pistol")){category = 1; price = 10;}
        else if(type.equals("Energy")){category = 2; price = 35;}
        else if(type.equals("Shotgun")){category = 3; price = 25;}
        else if(type.equals("Intermidiate")){category = 4; price = 15;}
        else if(type.equals("Rifle")){category = 5; price = 15;}
        else if(type.equals("Ununseptium")){category = 6; price = 2500;}
        else if(type.equals("Solar Cell")){category = 7; price = 24000;}
        else if(type.equals("Polychromite")){category = 8; price = 1250;}
        else if(type.equals("Polemian")){category = 9; price = 2500;}
        else if(type.equals("Guadium")){category = 10; price = 4000;}
        else if(type.equals("Exotic Plant1")){category = 11; price = 2000;}
        else if(type.equals("Exotic Plant2")){category = 12; price = 3500;}
        else if(type.equals("Health")){category = 13; price = 100;}
        /*
        switch(type)
        {
            case "Money":
            category = 0;   
            break;
            case "Pistol":
            category = 1;
            price = 10;
            break;
            case "Energy":
            category = 2;
            price = 35;
            break;
            case "Shotgun":
            category = 3;
            price = 25;
            break;
            case "Intermidiate":
            category = 4;
            price = 15;
            break;
            case "Rifle":
            category = 5;
            price = 15;
            break;    
            case "Ununseptium":
            category = 6;
            price = 2500;
            break;
            case "Solar Cell":
            category = 7;
            price = 24000;
            break;
            case "Polychromite":
            category = 8;
            price = 1250;
            break;
            case "Polemian":
            category = 9;
            price = 2500;
            break;
            case "Guadium":
            category = 10;
            price = 4000;
            break;
            case "Exotic Plant1":
            category = 11;
            price = 2000;
            break;
            case "Exotic Plant2":
            category = 12;
            price = 3500;
            break;

        }
        */
    }

    public int getAmount()
    {
        return this.amount;
    }   

    public String getType()
    {
        return this.type;
    }

    public String toString()
    {
        return String.format("%s,%d",type,amount);
    }

}
