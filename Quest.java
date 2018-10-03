import java.io.*;
import java.util.*;
import java.awt.image.*;
/**
 * Object to represent quests
 */

public abstract class Quest
{
    String name;

    boolean started = false;
    boolean completed = false;

    Inventory playerInv = new Inventory("playerInv");

    textBox message;

    String dialogue;

    ArrayList<Item> rewards = new ArrayList<Item>();

    String folder = "Quest Data";
    String code = "UTF-8";
    String completeMessage;

    public Quest()
    {
        name = "Hello World!";
        started = true;
        dialogue = "Greetings!";
        message = new textBox(dialogue);
    }

    public Quest(String n)
    {
        this.name = n;
        loadQuest();
        message = new textBox(dialogue);
    }

    public void start()
    {
        started = true;
    }

    public void complete()
    {
        completed = true;
    }

    public BufferedImage output()
    {
        return message.output();
    }

    public void saveQuest()
    {
        try{
            File dir = new File(folder);
            dir.mkdir();

            PrintWriter writer = new PrintWriter(folder + "/" + name,code);

            writer.println(started);
            writer.println(completed);
            writer.println(dialogue);

            for(Item i: rewards)
            {
                writer.println(i.iType);
                writer.println(i.toString());
            }

            writer.close();
            //System.out.println("saved");

        }catch(IOException e){}
    }

    public void loadQuest()
    {
        try{
            String fileDir = String.format("%s/%s",folder,name);
            Scanner file = new Scanner(new File(fileDir));

            started = Boolean.parseBoolean(file.nextLine());
            completed = Boolean.parseBoolean(file.nextLine());
            dialogue = file.nextLine();

            int iType;

            while(file.hasNextLine())
            {
                iType = file.nextInt();
                if(iType == 1) 
                {
                    Weapon w = new Weapon(file.nextLine().split(","));
                    rewards.add(w);
                }
                else if(iType == 2)
                {
                    Armor a = new Armor(file.nextLine().split(","));
                    rewards.add(a);
                }
                else if(iType == 3)
                {
                    Stackable s = new Stackable(file.nextLine().split(","));
                    rewards.add(s);
                }
            }

        }catch(IOException e){}
    }

}
