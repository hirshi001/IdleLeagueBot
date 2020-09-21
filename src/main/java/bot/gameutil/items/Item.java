package bot.gameutil.items;

import bot.gameutil.champions.StatChanger;
import bot.gameutil.champions.Stats;

public abstract class Item {

    protected StatChanger statChanger;
    private int id;

    public Item(StatChanger statChanger){this.statChanger = statChanger; }

    public abstract String getName();

    public abstract int getCost();

    public abstract int getSellCost();

    public abstract boolean updateStats(Stats stats);

    public abstract boolean hasSpecialEffects();

    public StatChanger getStats(){
        return statChanger;
    }

    public final void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public abstract String getDisplayInformation();

}
