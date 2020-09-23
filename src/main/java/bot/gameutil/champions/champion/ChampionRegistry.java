package bot.gameutil.champions.champion;

import bot.gameutil.champions.Stats;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;


public class ChampionRegistry {

    public static final Map<Long, Champion> idChampMap = new Hashtable<>();
    public static final Map<String, Champion> stringChampMap = new Hashtable<>();
    public static final List<Champion> champList = new ArrayList<>();

    public static final Champion DEFAULT_CHAMPION = new Champion("default champion", "null", new Stats());


    public static void registerChampion(long id, Champion c){
        c.setId(id);
        idChampMap.put(id, c);
        stringChampMap.put(c.getName().toLowerCase(), c);
        champList.add(c);
    }

    public static Champion getRandomChampion(){
        return champList.get((int)(Math.random()*champList.size()));
    }

    public static Champion getRandomChampion(int lane){
        return getRandomChampion();
    }

    public static Champion getChampion(long id){
        return idChampMap.get(id);
    }

    public static Champion getChampion(String name){
        return stringChampMap.get(name.toLowerCase());
    }



    public static void registerAllChampions(){
        JSONParser parser = new JSONParser();
        try {
            String path = ChampionRegistry.class.getClassLoader().getResource("championInfo.json").getPath().replace("%20"," ");
            FileReader reader = new FileReader(new File(path));
            JSONObject obj = (JSONObject)parser.parse(reader);
            JSONObject data = (JSONObject)obj.get("data");
            data.forEach(new BiConsumer() {
                @Override
                public void accept(Object o, Object o2) {
                    JSONObject champion = ((JSONObject)o2);
                    JSONObject stats = (JSONObject)(champion.get("stats"));
                    String champName = champion.get("name").toString();
                    String key = champion.get("id").toString();
                    Long champId = Long.parseLong(champion.get("key").toString());
                    Stats s = new Stats();
                    System.out.println(champName);
                    s.setAttackDamage(getDoubleValue(stats.get("attackdamage")));
                    s.setAttackDamagePerLevel(getDoubleValue(stats.get("attackdamageperlevel")));

                    s.setMp(getDoubleValue(stats.get("attackdamageperlevel")));
                    s.setMpPerLevel(getDoubleValue(stats.get("mpperlevel")));

                    s.setArmor(getDoubleValue(stats.get("armor")));
                    s.setArmorPerLevel(getDoubleValue(stats.get("armorperlevel")));

                    s.setHp(getDoubleValue(stats.get("hp")));
                    s.setHpPerLevel(getDoubleValue(stats.get("hpperlevel")));

                    s.setSpellblock(getDoubleValue(stats.get("spellblock")));
                    s.setSpellblockPerLevel(getDoubleValue(stats.get("spellblockperlevel")));

                    Champion c = new Champion(champName, key, s);
                    registerChampion(champId, c);
                }
            });
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static Double getDoubleValue(Object o){
        if(o instanceof Long){
            return ((Long)o).doubleValue();
        }
        else{
            return (Double)o;
        }
    }





}
