public class Sprite
{
    // instance variables - replace the example below with your own
    float x; //x and y location
    float y;
    int texture; //index of the texture
    int texWidth = 64;
    int texHeight = texWidth;

    public Sprite(float x,float y,int texture)
    {
        this.x = y; // I messed up the whole r,c -> y,x business so that's why this is like this
        this.y = x;
        this.texture = texture;
    }

    public int getTex()
    {
        return texture;
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }
}
