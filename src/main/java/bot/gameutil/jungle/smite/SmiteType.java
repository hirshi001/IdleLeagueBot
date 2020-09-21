package bot.gameutil.jungle.smite;

public enum SmiteType {

    NORMAL(100),
    RED(100),
    BLUE(100);

    private int baseDamage;

    private SmiteType(int baseDamage){
        this.baseDamage = baseDamage;
    }

}
