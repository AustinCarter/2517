
/**
 * A class writen by Thomas Schwalen to handle doors and switches in our Skills USA game Project
 */
public class ActionPoint
{
    int r;
    int c;    
    int open_r;
    int open_c;
    int type; // 0 = door, 1 = ladder/portal, 2 = start, 3 = exit, 4 = container/store ,5 = quest
    boolean on;
    
    int saveTex;
    // add variable for type (ladder, portal, door)
    //maybe add code for its respective texture?

    public ActionPoint(int r, int c, int open_r, int open_c, int type)
    {
        this.r = r;
        this.c = c;
        this.open_r = open_r;
        this.open_c = open_c;
        this.type = type;
        on = false;
    }

    public int get_r()
    {
        return r;
    }

    public int get_c()
    {
        return c;
    }

    public int getOpen_r()
    {
        return open_r;
    }

    public int getOpen_c()
    {
        return open_c;
    }
    
    public int getType()
    {
        return type;
    }
    
    public void switchSetting()
    {
        if(on == true)
        {
            on =false;
        }
        else
            on = true;
    }
    
    public void setSaveTex(int texNum)
    {
        this.saveTex = texNum;
    }
    
    public int getSaveTex()
    {
        return saveTex;
    }
    
    public boolean isOn()
    {
        return on;
    }
    
}
