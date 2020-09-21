package bot.gameutil.champions.champion;
import bot.gameutil.champions.Stats;
import net.dv8tion.jda.api.entities.Message;

public class Champion {

    protected String name, key;
    protected Stats stats;
    protected long id;

    public Champion(String name, String key, Stats stats){
        this.name = name;
        this.key = key;
        this.stats = stats;
    }

    public Stats getStats(){
        return stats;
    }

    public String getName(){
        return name;
    }

    public void setId(long id){
        this.id = id;
    }

    public long getId(){
        return this.id;
    }

    public Message.Attachment getImage(){
        return null;
    }

    public String getKey(){
        return key;
    }

}
