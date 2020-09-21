package bot.gameutil.items;

import bot.gameutil.champions.Stats;
import bot.gameutil.champions.StatChanger;

public class SimpleItem extends Item {

    String name;
    int cost;

    private double sellPrice = 0.7;

    public SimpleItem(String name, int cost, StatChanger statChanger){
        super(statChanger);
        this.name = name;
        this.cost = cost;
    }

    @Override
    public int getSellCost() {
        return (int)(cost*sellPrice);
    }

    @Override
    public boolean updateStats(Stats stats) {
        return true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public final boolean hasSpecialEffects() {
        return false;
    }

    public SimpleItem sellPercentage(double percentage){
        this.sellPrice = percentage;
        return this;
    }

    @Override
    public String getDisplayInformation() {
        StringBuilder sb = new StringBuilder();
        sb.append(getStats().toString());
        sb.append("sells for ");
        sb.append(getSellCost());
        sb.append(" gold");

        return sb.toString();

    }
}
