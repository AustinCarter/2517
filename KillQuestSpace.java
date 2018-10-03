/**
 *A quest archetype for kill quests in the 1st person persepective
 */
import java.io.*;
import java.util.*;
public class KillQuestSpace extends Quest
{
    int shipType,shipAmount,counter;

    ///types:
    // type 0 = Freighter
    // type 1 = Standard
    // type 2 = BulletHell ship

    public KillQuestSpace()
    {
        super();
        counter = 0;
    }

    public KillQuestSpace(String n)
    {   
        this.name = n;
        loadQuest();
        message = new textBox(dialogue);
    }

    public void checkKill(int t)
    {
        if(t == shipType)
        {
            counter ++;
            checkComplete();
        }
    }

    public void checkComplete()
    {
        if(counter >= shipAmount)
        {
            for(Item i: rewards)
            {
                playerInv.addItem(i);
            }
            completed = true;
            message = new textBox("Space is a safer place now, thanks to you! Thank you traveler.");
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
            writer.print(shipType + " ");
            writer.print(shipAmount + " ");
            writer.println(counter);

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
            shipType = file.nextInt();
            shipAmount = file.nextInt();
            counter = file.nextInt();
            file.nextLine();

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
