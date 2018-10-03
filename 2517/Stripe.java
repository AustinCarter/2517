//this class is an abstraction created to solve the problem of transparancy. IE, you use this class to store the multiple
//walls in case you need to draw more than one per ray. Best Stored in a stack
//
public class Stripe
{
    
    int stepX;
    int stepY;    
    int mapX;
    int mapY;
    int side;
    boolean door;
    
    public Stripe(int stepX,int stepY,int mapX,int mapY, int side)
    {
        this.stepX = stepX;
        this.stepY = stepY;
        this.mapX = mapX;
        this.mapY = mapY;
        this.side = side;
        this.door = false;
    }
    
    public void setDoor()
    {
        this.door = true;
    }
}
