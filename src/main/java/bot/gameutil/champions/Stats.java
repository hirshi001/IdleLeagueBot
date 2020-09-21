package bot.gameutil.champions;

public class Stats {


    private double attackDamage, attackDamagePerLevel;
    private double mp, mpPerLevel;
    private double armor, armorPerLevel;
    private double hp, hpPerLevel;
    private double spellblock, spellblockPerLevel;

    public Stats(){}

    public Stats( double attackDamage, double attackDamagePerLevel, double mp, double mpPerLevel, double armor, double armorPerLevel, double hp, double hpPerLevel, double spellblock, double spellblockPerLevel) {
        this.attackDamage = attackDamage;
        this.attackDamagePerLevel = attackDamagePerLevel;
        this.mp = mp;
        this.mpPerLevel = mpPerLevel;
        this.armor = armor;
        this.armorPerLevel = armorPerLevel;
        this.hp = hp;
        this.hpPerLevel = hpPerLevel;
        this.spellblock = spellblock;
        this.spellblockPerLevel = spellblockPerLevel;
    }


    public Stats setAttackDamage(double attackDamage) {
        this.attackDamage = attackDamage;
        return this;
    }

    public Stats setAttackDamagePerLevel(double attackDamagePerLevel) {
        this.attackDamagePerLevel = attackDamagePerLevel;
        return this;
    }

    public Stats setMp(double mp) {
        this.mp = mp;
        return this;
    }

    public Stats setMpPerLevel(double mpPerLevel) {
        this.mpPerLevel = mpPerLevel;
        return this;
    }

    public Stats setArmor(double armor) {
        this.armor = armor;
        return this;
    }

    public Stats setArmorPerLevel(double armorPerLevel) {
        this.armorPerLevel = armorPerLevel;
        return this;
    }

    public Stats setHp(double hp) {
        this.hp = hp;
        return this;
    }

    public Stats setHpPerLevel(double hpPerLevel) {
        this.hpPerLevel = hpPerLevel;
        return this;
    }

    public Stats setSpellblock(double spellblock) {
        this.spellblock = spellblock;
        return this;
    }

    public Stats setSpellblockPerLevel(double spellblockPerLevel) {
        this.spellblockPerLevel = spellblockPerLevel;
        return this;
    }




    public int getAttackDamage(int level) {
        return (int)(attackDamage+attackDamagePerLevel*(level-1));
    }

    public int getAbilityPower(int level) {
        return (int)(mp+mpPerLevel*(level-1));
    }

    public int getArmor(int level) {
        return (int)(armor+armorPerLevel*(level-1));
    }

    public int getMagicResist(int level) {
        return (int)(spellblock+spellblockPerLevel*(level-1));
    }

    public double getAttackSpeed(int level) {
        return 0;
    }
}
