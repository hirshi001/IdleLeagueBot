package bot.gameutil.items;

import bot.gameutil.champions.StatChanger;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class ItemRegistry {

    public static Map<Integer, Item> itemIdMapping = new Hashtable<>();
    public static Map<String, Item> itemNameMapping = new Hashtable<>();

    public static void registerItem(Integer id, Item i){
        i.setId(id);
        itemIdMapping.put(id,i);
        itemNameMapping.put(i.getName().toLowerCase(), i);
    }

    public static Item getItem(String name){
        return itemNameMapping.get(name);
    }

    public static Item getItem(Integer id){
        return itemIdMapping.get(id);
    }

    public static boolean containsItem(String name){
        return itemNameMapping.containsKey(name);
    }

    public static boolean containsItem(Integer id){
        return itemIdMapping.containsKey(id);
    }

    public static final int
    DORANS_SHIELD = 0,
    DORANS_BLADE = 1,
    DORANS_RING = 2;


    static{
        registerItem(DORANS_SHIELD, new SimpleItem("Doran's Shield", 450, new StatChanger()).sellPercentage(0.5));
        registerItem(DORANS_BLADE, new SimpleItem("Doran's Blade", 450, new StatChanger().atk(10)).sellPercentage(0.5));
        registerItem(DORANS_RING, new SimpleItem("Doran's Ring", 450, new StatChanger().ap(10)).sellPercentage(0.5));
    }


}
