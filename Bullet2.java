
/**
 *  A Class for intances of bullets fired by both the player and enemies
 */
public class Bullet2
{
    boolean friendly; // was the bullet fired by the player or an enemy
    float dx; // x component of the bullet's path
    float dy; // y component of the bullet's path
    float x; // x component of the bullet's location
    float y; // y component of the bullet's path
    int range; // the maximum amount of "updates" the bullet may undergo until it stops
    int damage; // how much damage the bullet inflicts
    int age = 0; //how many "updates" the bullet has undergone
    float speed = 0.4f; // how fast the projectile travels
    
    public Bullet2(float x, float y, float dx, float dy, int range, int damage, boolean friendly)
    {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.range = range;
        this.damage = damage;
        this.friendly = friendly;
    }
    
    public void update()
    {
        x += dx * speed;
        y += dy * speed;
        age++;
    }
    
    public void setSpeed(float s)
    {
        speed = s;
    }
}
