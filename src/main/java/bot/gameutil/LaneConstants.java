package bot.gameutil;

import java.util.HashMap;
import java.util.Map;

public class LaneConstants {

    public static final int
    NONE = 0,
    TOP = 1,
    MID = 2,
    JUNGLE = 3,
    ADC = 4,
    SUPPORT = 5;


    private static final Map<String, Integer> nameToId = new HashMap<String, Integer>();

    private static final Map<Integer, String> idToName = new HashMap<Integer, String>();

    static{
        nameToId.put("none",0);
        nameToId.put("top",1);
        nameToId.put("mid",2);
        nameToId.put("jg",3);
        nameToId.put("jungle",3);
        nameToId.put("adc",4);
        nameToId.put("bot",4);
        nameToId.put("support",5);
        nameToId.put("sup",5);

        idToName.put(0,"None");
        idToName.put(1, "Top");
        idToName.put(2, "Mid");
        idToName.put(3, "Jungle");
        idToName.put(4, "ADC");
        idToName.put(5, "Support");


    }


    public static final int getLane(String name){
       return nameToId.getOrDefault(name.toLowerCase(),-1);
    }

    public static final String getLaneString(String name){
       return idToName.get(nameToId.getOrDefault(name.toLowerCase(),0));
    }

    public static final String getLaneString(int i){
        return idToName.getOrDefault(i,"");
    }




}
