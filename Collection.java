import java.io.*;
import java.util.*;

/**
 *A quest archetype for collection quests 
 */
public class Collection extends Quest
{
    Item needed;

    public Collection()
    {
        super();
    }

    public Collection(String n)
    {
        this.name = n;
        loadQuest();
        message = new textBox(dialogue);
    }

    public void checkComplete()
    {
        if(!started)
            started = true;
        else if(playerInv.checkInventory(needed) && !completed)
        {
            for(Item i: rewards)
            {
                playerInv.addItem(i);
            }
            completed = true;
            message = new textBox(completeMessage);          
        }
        saveQuest();
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
            writer.println(completeMessage);
            writer.println(needed.iType);
            writer.println(needed.toString());

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

            int iType;

            started = Boolean.parseBoolean(file.nextLine());
            completed = Boolean.parseBoolean(file.nextLine());
            dialogue = file.nextLine();
            completeMessage = file.nextLine();
            iType = file.nextInt();
            file.nextLine();
            if(iType == 1) 
            {
                needed = new Weapon(file.nextLine().split(","));
            }
            else if(iType == 2)
            {
                needed = new Armor(file.nextLine().split(","));
            }
            else if(iType == 3)
            {
                needed = new Stackable(file.nextLine().split(","));
            }

            while(file.hasNextLine())
            {
                iType = file.nextInt();
                file.nextLine();
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
