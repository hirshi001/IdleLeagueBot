package bot.gameutil.champions;

public class ChampionRespawn {

    public static long championRespawnMinutes(int level){
        long time = 0;
        if(level<=6){
            time = (level*2+4)/2;
        }
        else if(level==7){
            time =  21/2;
        }
        else{
            time = (long)((level*2.5 + 7.5)/2);
        }
        time = time*60*1000;
        return time;
    }


}
