package bot.gameutil.champions.championexperience;

public class ChampionExperience {

    public static int experienceToReachLevel(int level){
        if(level==1) return 0;
        return level*100+80;
    }

}
