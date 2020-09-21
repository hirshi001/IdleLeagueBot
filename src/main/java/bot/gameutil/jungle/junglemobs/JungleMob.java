package bot.gameutil.jungle.junglemobs;

import java.util.Random;

public enum JungleMob {

    Gromp("Gromp",
            "https://pm1.narvii.com/5758/b63a9dcd21263f52682c254e0a77f868c3daa569_00.jpg",
            new int[]{135,135,138,145,155,155,169,169,182},
            new int[]{105}),
    Krugs("Krugs",
          "https://external-preview.redd.it/GYAD9p3cy9BztFFWQSMYoqjRWDB7AP9ZudXnxvhaQyE.jpg?auto=webp&s=9e7eb9b6c1853cf52e8a1e138a6fa0334c1c8269",
            new int[]{175,175,175,182,192,201,207,219,224,236},
            new int[]{135}),
    Raptors("Raptors",
            "https://i.ytimg.com/vi/qQ3v6stl5qE/maxresdefault.jpg",
            new int[]{95,95,97,102,109,109,119,119,128},
            new int[]{85}),
    Wolves("Murk wolves",
            "https://pm1.narvii.com/6863/aac8e76a961d8ece78009fca0214fff541c3bb36r1-819-546v2_hq.jpg",
            new int[]{95,95,97,102,109,109,119,119,128},
            new int[]{85}),
    BlueSentinel("Blue Sentinel",
            "https://cdnb.artstation.com/p/assets/images/images/001/207/429/medium/suke-749da8f126ea4fa4bc24e210b00d5239.jpg?1442229952",
            new int[]{110,110,113, 118,127,127, 138, 138, 149},
            new int[]{100}),
    RedSentinel("Red Sentinel",
            "https://vignette.wikia.nocookie.net/leagueoflegends/images/b/b3/Red_Brambleback_OriginalSkin.jpg/revision/latest?cb=20151202202628",
            new int[]{110,110,113, 118,127,127, 138, 138, 149},
            new int[]{100}),
    RiftScuttler("Rift Scuttler",
            "https://pa1.narvii.com/6130/2efdd61459262c0b75e8c1f586268fac28b2cb2a_00.gif",
            new int[]{115, 127, 138, 150, 161, 173, 184, 196, 207},
            new int[]{70, 77, 84, 91, 98, 105, 112, 119, 126});

    public static JungleMob[] jungleMobs = new JungleMob[]{Gromp, Krugs, Raptors, Wolves, BlueSentinel, RedSentinel, RiftScuttler};

    private String imageUrl;
    private String name;
    private int[] experiencePerLevel;
    private int[] goldPerLevel;

    private JungleMob(String name, String imageUrl, int[] experiencePerLevel, int[] goldPerLevel){
        this.name = name;
        this.imageUrl = imageUrl;
        this.experiencePerLevel = experiencePerLevel;
        this.goldPerLevel = goldPerLevel;
    }

    public String getName(){
        return name;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public int getGoldKill(int level){
        if(goldPerLevel.length<level){
            return goldPerLevel[goldPerLevel.length-1];
        }
        return goldPerLevel[level-1];
    }

    public int getExperience(int level){
        if(experiencePerLevel.length<level){
            return experiencePerLevel[experiencePerLevel.length-1];
        }
        return experiencePerLevel[level-1];
    }

    public static JungleMob getRandomMob(){
        int index = (int)(Math.random()*jungleMobs.length);
        return jungleMobs[index];
    }

}
