package bot.gameutil;

import java.util.ArrayList;

public class Location {

    private static final ArrayList<Location> map = new ArrayList<>();

    public static final Location
    BLUE_FOUNTAIN = register("Blue Fountain", 0,0),
    BLUE_NEXUS = register("Blue Nexus", 2000,2000),
    BLUE_INNER_TURRET = register("Blue Inner Turret", 4000, 4000),
    BLUE_OUTER_TURRET = register("Blue Outer Turret", 6000,6000),
    CENTER = register("Center", 8000,8000),
    RED_OUTER_TURRET = register("Red Outer Turret", 10000, 10000),
    RED_INNER_TURRET = register("Red Inner Turret", 12000, 12000),
    RED_NEXUS = register("Red Nexus", 14000, 14000),
    RED_FOUNTAIN = register("Red Fountain", 16000,16000);

    public static Location register(String name, int x, int y){
        Location loc = new Location(name, x, y, map.size());
        map.add(loc);
        return loc;
    }

    public static Location get(int id){
        return map.get(id);
    }

    private final int x, y;
    private final int id;
    private final String name;
    private Location(String name, int x, int y, int id){
        this.name = name;
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }


}
