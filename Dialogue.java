public class Dialogue extends ActionPoint
{
    String output;
    boolean conditional;
    
    
    public Dialogue(int r, int c, String output)
    {
        super(r,c,r,c,5);
        this.output = output;
    }    
    
    public String getText()
    {
        return output;
    }
}
