package bot.commands.normalcommands.gamecommands.jungling;
import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandManager;
import bot.commands.normalcommands.Roasts;
import bot.database.GameAccount;
import bot.database.MongoConnection;
import bot.gameutil.champions.championexperience.ChampionExperience;
import bot.gameutil.jungle.junglemobs.JungleMob;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.awt.Color;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.inc;
import static com.mongodb.client.model.Updates.set;

public class JungleCommand extends Command {

    Set<Long> jungling = Collections.synchronizedSet(new HashSet<Long>());
    int jungleTime = 5;
    long initCooldownTime = (10*60)*1000;


    public JungleCommand(){   }

    @Override
    public String getHelp() {
        return "jungles";
    }

    private long getReducedCooldown(int coolDownLevel){
        return coolDownLevel*10*1000L;
    }

    @Override
    public void commandCalled(String name, String msg, final GuildMessageReceivedEvent event, CommandManager commandManager) {

        long id = event.getAuthor().getIdLong();

        //MongoCollection<Document> userCollection =  MongoConnection.getOneVOneBotCollection();

        jungle(id, event);
    }



    private void jungle(long id, GuildMessageReceivedEvent event){

        //check if the user is currently jungling
       if(junglingCheck(id, event)) return;

       //check if the users cooldown is over
        if(cooldownCheck(id, event)) return;

        //Player has passed all checks and can start jungling
        jungling.add(id);
        AtomicReference<Message> messageSent = new AtomicReference<>();
        event.getChannel().sendMessage("Jungling...").queue(messageSent::set);

        Bson filter = eq(id);

        try {
            Thread.sleep(jungleTime*1000);
        } catch (InterruptedException e) {
            event.getChannel().sendMessage("Jungle failed. See this:\n\t "+e.getMessage()).queue();
            return;
        }

        //MongoConnection.getCooldownsCollection().findOneAndUpdate(filter, set("jungle",System.currentTimeMillis()+initCooldownTime));


        JungleMob mob = JungleMob.getRandomMob();

        GameAccount gameAccount = new GameAccount(id);
        gameAccount.regenerateDoc();
        Document playerDocument = (Document)gameAccount.getDoc().get("player");

        int gold = playerDocument.getInteger("gold");
        int level = playerDocument.getInteger("level");
        int experience = playerDocument.getInteger("experience");

        int mobLevel = level+(int)(Math.random()*3);
        int goldEarned=mob.getGoldKill(mobLevel);
        int experienceGained=mob.getExperience(mobLevel);

        gold+=goldEarned;
        experience+=experienceGained;

        int newLevel = level;
        while(newLevel<18 && ChampionExperience.experienceToReachLevel(newLevel+1)<experience){
            experience-=ChampionExperience.experienceToReachLevel(newLevel+1);
            newLevel++;
        }


        gameAccount.updateValue("player.gold", gold);
        if(newLevel!=level) gameAccount.updateValue("player.level", newLevel);
        gameAccount.updateValue("player.experience",experience);

        jungling.remove(id);

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.MAGENTA);

        eb.setTitle("JUNGLE");
        eb.setThumbnail(mob.getImageUrl());

        eb.addField(mob.getName() + " Killed!", "You earned " + goldEarned + " gold and gained " + experienceGained + " experience", false);

        if(newLevel!=level){
            eb.addField("You are now level " + newLevel, "", false);
        }
        String sb = "You now have " +
                gold +
                " gold.";
        eb.addField(sb,"", false);
        event.getChannel().sendMessage(eb.build()).queue();


        messageSent.get().delete().queue();

    }

    private boolean junglingCheck(long id, GuildMessageReceivedEvent event){
        if(jungling.contains(id)){
            StringBuilder sb = new StringBuilder(Roasts.getRandomRoast()).append("\n\"You are already jungling right now");
            event.getChannel().sendMessage(sb).queue();
            return true;
        }
        return false;
    }

    private boolean cooldownCheck(long id, GuildMessageReceivedEvent event){
        /*
        Document cooldownDoc = GameAccount.getCooldown(id);
        long finishTime = cooldownDoc.getLong("jungle");
        long currentTime = System.currentTimeMillis();
        if(finishTime>currentTime){
            int secs = (int)((finishTime-currentTime)/1000L);
            event.getChannel().sendMessage("Please wait "+secs+" seconds before jungling again").queue();
            return true;
        }
        return false;
         */
        return false;
    }

    @Override
    public boolean requiresAccount() {
        return true;
    }

    @Override
    public boolean requiredInGame() {
        return true;
    }

    @Override
    public String inGameRequireTitle() {
        return "You can't jungle unless your in a game";
    }

}
