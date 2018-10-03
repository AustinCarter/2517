public class ArmorPrefix
{
    String prefix;
    int shielding, health, absorption;
    
    public ArmorPrefix(String[] data)
    {
        this.prefix = data[0];
        this.shielding = Integer.parseInt(data[1]);
        this.health = Integer.parseInt(data[2]);
        this.absorption = Integer.parseInt(data[3]);
    }
}
