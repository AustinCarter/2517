
public class WeaponPrefix
{
    String prefix;
    int damage, range,magSize;
    
    public WeaponPrefix(String[] data)
    {
        this.prefix = data[0];
        this.damage = Integer.parseInt(data[1]);
        this.range = Integer.parseInt(data[2]);
        this.magSize = Integer.parseInt(data[3]);
    }
}

