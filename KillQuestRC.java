/**
 *A quest archetype for kill quests in the 1st person persepective
 */
import java.io.*;
import java.util.*;
public class KillQuestRC extends Quest
{
    int NPCType,NPCAmount,counter;

    ///types:
    // type 0 = spaceGuy
    // type 1 = armorAlien
    // type 2 = spaceGuy2
    // type 3 = spaceMerchant

    public KillQuestRC()
    {
        super();
        counter = 0;
    }

    public KillQuestRC(String n)
    {   
        this.name = n;
        loadQuest();
        message = new textBox(dialogue);
    }

    public void checkKill(int t)
    {
        if(t == NPCType)
        {
            counter ++;
            checkComplete();
        }
    }

    public void checkComplete()
    {
        if(counter >= NPCAmount)
        {
             for(Item i: rewards)
            {
                playerInv.addItem(i);
            }
            completed = true;
            message = new textBox("Thank you for... dispatching those nuisances for me");
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
            writer.print(NPCType + " ");
            writer.print(NPCAmount + " ");
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
            NPCType = file.nextInt();
            NPCAmount = file.nextInt();
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
