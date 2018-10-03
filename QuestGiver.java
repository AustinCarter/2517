public class QuestGiver extends Npc
{
    int ident;
    Quest quest;
    public QuestGiver(float x,float y,int texture,int type, GameEngine myEngine, int ident, Quest quest)
    {
        super(x,y,texture,type,myEngine);
        this.ident = ident;
        this.quest = quest;
    }
    
    public void update()
    {
        aggressive = false;
        collRadius = 0;
    }    
}
