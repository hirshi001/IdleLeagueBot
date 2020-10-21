package bot.gameutil;

import java.util.ArrayList;

public class Location {

    public static final Location
    BLUE_FOUNTAIN = register(0,0),
    BLUE_NEXUS = register(2000,2000),
    BLUE_INNER_TURRET = register(4000, 4000),
    BLUE_OUTER_TURRET = register(6000,6000),
    CENTER = register(8000,8000),
    RED_OUTER_TURRET = register(10000, 10000),
    RED_INNER_TURRET = register(12000, 12000),
    RED_NEXUS = register(14000, 14000),
    RED_FOUNTAIN = register(16000,16000);




    private static final ArrayList<Location> map = new ArrayList<>();
    public static Location register(int x, int y){
        Location loc = new Location(x, y, map.size());
        map.add(loc);
        return loc;
    }

    private final int x, y;
    private final int id;
    private Location(int x, int y, int id){
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public int getId(){
        return id;
    }


}
