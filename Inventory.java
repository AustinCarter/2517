import java.io.*;
import java.util.*;

public class Inventory
{
    Item[][] items;
    Stackable[] stackables = new Stackable[6];   

    String folder = "Inventory Stats";

    static final int MONEY = 0;
    static final int PISTOL = 1;
    static final int ENERGY = 2;
    static final int SHOTGUN = 3;
    static final int INTERMIDIATE = 4;
    static final int RIFLE = 5;

    int rows, columns;

    String name,baseName;
    String code = "UTF-8";

    public Inventory(String name)
    {
        this.name = name;
        baseName = name;
        rows = 7;
        columns = 10;
        items = new Item[rows][columns];
        populateStackables();
        loadInv();
    }

    public Inventory(String name, int r, int c)
    {
        this.name = name;
        baseName = name;
        rows = r;
        columns = c;
        items = new Item[rows][columns];
        populateStackables();
        loadInv();
    }

    public void addItem(Item e)
    {
        // loadInv();

        boolean placed = false;
        if(e instanceof Stackable && name.equals("playerInv"))
        {
            Stackable r = new Stackable(e.getData());
            for(Stackable s: stackables)
            {
                if(s.category == r.category)
                {
                    s.amount += r.amount;
                    placed = true;
                }
            }
        }
        for(int r =0; r < rows; r++)
        {
            for(int c = 0; c < columns; c++)
            {

                if(!checkItem(r,c) && !placed)
                {
                    items[r][c] = e;
                    placed = true;
                }
                else if(!placed && items[r][c] instanceof Stackable && e instanceof Stackable && e.category == items[r][c].category)
                {
                    items[r][c].amount += e.amount;
                    placed = true;
                }
            }
        }
        if(!placed)
            //System.out.println("Inventory is full");
        saveInv();
    }

    public void removeItem(int r, int c)
    {
        items[r][c] = null;
    }

    public void setItem(int r, int c,Item i)
    {
        items[r][c] = i;
    }

    public void swapItems(int itemAr,int itemAc,int itemBr,int itemBc)
    {
        Item temp = items[itemAr][itemAc];
        items[itemAr][itemAc] = items[itemBr][itemBc];
        items[itemBr][itemBc] = temp;
        //saveInv();
    }

    public Weapon2[] getLoadOut()
    {
        Weapon2[] loadOut = new Weapon2[10];
        for(int i = 0; i < 10; i++)
        {
            if(items[0][i] instanceof Weapon)
            {
                Weapon w = (Weapon) items[0][i];
                loadOut[i] = getWeapon2(w.baseName);
            }
            else {loadOut[i] = null;}             
        }
        return loadOut;
    }

    public Weapon2 getWeapon2(String weaponName)
    {
        String n = weaponName;
        if(n.equals("Glock")){ return new Weapon2(3); }        
        else if(n.equals("Mauser")) {return new Weapon2(4);}
        else if(n.equals("pumpShotgun")) {return new Weapon2(7);}
        else if(n.equals("Suomi")) { return new Weapon2(10);}
        else if(n.equals("Agram")) {return new Weapon2(11);}
        else if(n.equals("HK51")) {return new Weapon2(13);}
        else if(n.equals("AK")) {return new Weapon2(14);}
        else if(n.equals("G11")){return new Weapon2(15);}
        return null;
    }

    public Item getItem(int r, int c)
    {
        return items[r][c];
    }

    public boolean checkItem(int r, int c)
    {
        if(items[r][c] != null)
            return true;
        return false;
    }

    public void populateStackables()
    {
        stackables[MONEY] = new Stackable("Money", 100);
        stackables[PISTOL] = new Stackable("Pistol", 10);
        stackables[ENERGY] = new Stackable("Energy", 10);
        stackables[SHOTGUN] = new Stackable("Shotgun", 10);
        stackables[INTERMIDIATE] = new Stackable("Intermidiate", 10);
        stackables[RIFLE] = new Stackable("Rifle", 10);
    }

    public String[] getStackableAmounts()
    {
        String[] stackValues = new String[stackables.length];
        for(int i = 0; i < stackValues.length; i++)
        {
            stackValues[i] = Integer.toString(stackables[i].getAmount());
        }
        return stackValues;
    }

    public int[] getAmmoAmounts()
    {
        int[] ammoAmounts = new int[4];
        ammoAmounts[0] = stackables[PISTOL].amount;
        ammoAmounts[1] = stackables[ENERGY].amount;
        ammoAmounts[2] = stackables[SHOTGUN].amount;
        ammoAmounts[3] = stackables[RIFLE].amount;

        return ammoAmounts;
    }

    public void setAmmoAmounts(int[] ammoAmounts)
    {
        loadInv();

        stackables[PISTOL].amount = ammoAmounts[0];
        stackables[ENERGY].amount = ammoAmounts[1];
        stackables[SHOTGUN].amount = ammoAmounts[2];
        stackables[RIFLE].amount = ammoAmounts[3];

        saveInv();
    }

    public String[] getStackableTypes()
    {
        String[] stackTypes = new String[stackables.length];

        for(int i = 0; i < stackTypes.length; i++)
            stackTypes[i] = stackables[i].getType();

        return stackTypes;
    }

    public int getMoney()
    {
        return stackables[MONEY].getAmount();
    }

    public void addMoney(int c)
    {
        stackables[MONEY].amount += c;
    }

    public void pay(int c)
    {
        stackables[MONEY].amount -= c;
    }

    public boolean checkInventory(Item i)
    {
        boolean contained = false;
        loadInv();

        for(int r =0; r < rows; r++)
        {
            for(int c = 0; c < columns; c++)
            {
                if(checkItem(r,c) && items[r][c].amount >= i.amount && items[r][c].baseName.equals(i.baseName))
                {
                    contained = true;
                    items[r][c].amount -= i.amount;   
                }  
                if(checkItem(r,c) && items[r][c].amount <= 0)
                {
                   removeItem(r,c);
                }
            }
        }
        
        saveInv();
        return contained;        
    }

    public void saveInv()
    {
        try{
            File dir = new File(folder);
            dir.mkdir();

            //System.out.println("SAVE");

            PrintWriter clear = new PrintWriter(folder + "/" + name,code);
            clear.println("");
            clear.close();

            PrintWriter invWriter = new PrintWriter(folder + "/" + name,code);
            invWriter.println(rows);
            invWriter.println(columns);

            for(int i = 0; i < 6; i++)
            {
                invWriter.println(stackables[i].toString());
            }

            for(int r =0; r < rows; r++)
            {
                for(int c = 0; c < columns; c++)
                {
                    if(checkItem(r,c))
                    {
                        invWriter.println(items[r][c].iType);
                        invWriter.println(items[r][c].toString());
                        invWriter.printf("%d,%d\n",r,c);
                    }
                }
            }
            invWriter.close();
            //System.out.println("saved");

        }catch(IOException e){}
    }

    public void loadInv()
    {
        try{

            //File data = new File(folder "/ " + name);
            String fileDir = String.format("%s/%s",folder,name);
            Scanner file = new Scanner(new File(fileDir));
            rows = file.nextInt();
            columns = file.nextInt();
            file.nextLine();

            String[] rc;

            loadStackables(file);

            while(file.hasNextLine())
            {
                int iType = file.nextInt();
                file.nextLine();
                if(iType == 1) 
                {
                    Weapon w = new Weapon(file.nextLine().split(","));
                    rc = file.nextLine().split(",");
                    setItem(Integer.parseInt(rc[0]),Integer.parseInt(rc[1]),w);
                }
                else if(iType == 2)
                {
                    Armor a = new Armor(file.nextLine().split(","));
                    rc = file.nextLine().split(",");
                    setItem(Integer.parseInt(rc[0]),Integer.parseInt(rc[1]),a);
                }
                else if(iType == 3)
                {
                    Stackable s = new Stackable(file.nextLine().split(","));
                    rc = file.nextLine().split(",");
                    setItem(Integer.parseInt(rc[0]),Integer.parseInt(rc[1]),s);
                }
            }

        }catch(IOException e){}
    }

    public void loadStackables(Scanner file)
    {
        for(int i = 0; i < 6; i++)
        {
            stackables[i] = new Stackable(file.nextLine().split(","));
        }
    }

}