package bot.commands.normalcommands.jungling;
import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandManager;
import bot.commands.normalcommands.Roasts;
import bot.database.MongoConnection;
import bot.gameutil.champions.championexperience.ChampionExperience;
import bot.gameutil.jungle.junglemobs.JungleMob;
import com.mongodb.client.MongoCollection;
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
import static com.mongodb.client.model.Updates.set;

public class JungleCommand extends Command {

    Set<Long> jungling = Collections.synchronizedSet(new HashSet<Long>());
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

        Long id = event.getAuthor().getIdLong();

        MongoCollection<Document> userCollection =  MongoConnection.getOneVOneBotCollection();

        Bson filter = eq(id);
        final Document document = userCollection.find(filter).first();
        jungle(id, event, userCollection, document.get("player",Document.class));
    }

    private void jungle(Long id, GuildMessageReceivedEvent event, MongoCollection<Document> userCollection, Document d) {

        Bson filter;
        Bson updateOperation;

        boolean currJungle = false;
        synchronized (jungling){
            if(jungling.contains(id)){
                currJungle = true;
            }
        }

        if(currJungle) {
            StringBuilder sb = new StringBuilder();
            sb.append(Roasts.getRandomRoast());
            sb.append("\nYour already jungling right now!!!");
            event.getChannel().sendMessage(sb.toString()).queue();
            return;

        }

        MongoCollection<Document> cooldownCollection = MongoConnection.getCooldownsCollection();
        Document cooldownDoc = cooldownCollection.find(eq(id)).first();
        assert cooldownDoc != null : "User doesn't have cooldownDoc?";
        Long cooldownStart = cooldownDoc.getLong("jungle");
        Integer cooldownLevel = cooldownDoc.getInteger("jungle cooldown level");

        if(cooldownLevel==null){
            filter = eq(id);
            updateOperation = set("jungle cooldown level",0);
            cooldownCollection.updateOne(filter,updateOperation);
            cooldownLevel = 0;
        }

        long cooldownTime = initCooldownTime-getReducedCooldown(cooldownLevel);

        if(cooldownStart!=null && cooldownStart+cooldownTime>System.currentTimeMillis()){
            int secs = -(int)(System.currentTimeMillis()-cooldownStart-cooldownTime)/1000;
            event.getChannel().sendMessage("Please wait "+secs+" seconds before jungling again").queue();
            return;
        }


        //update jungling
        jungling.add(id);
        //update cooldown
        filter = eq(id);
        updateOperation = set("jungle", System.currentTimeMillis());
        cooldownCollection.updateOne(filter, updateOperation);

        AtomicReference<Message> messageSent = new AtomicReference<>();

        event.getChannel().sendMessage("Jungling...").queue(message -> messageSent.set(message));

        int jungleTime = 5;

        try {
            Thread.sleep(jungleTime*1000);
        } catch (InterruptedException e) {
            event.getChannel().sendMessage("Jungling failed...").queue();
            e.printStackTrace();
        } finally {


            JungleMob mob = JungleMob.getRandomMob();

            int gold = d.getInteger("gold");
            int level = d.getInteger("level");
            int experience = d.getInteger("experience");

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

            filter = eq(id);
            updateOperation = set("player.gold", gold);
            userCollection.updateOne(filter, updateOperation);

            updateOperation = set("player.level", newLevel);
            userCollection.updateOne(filter, updateOperation);

            updateOperation = set("player.experience", experience);
            userCollection.updateOne(filter, updateOperation);


            StringBuilder sb = new StringBuilder();
            sb.append("You now have ");
            sb.append(gold);
            sb.append(" gold.");

            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.MAGENTA);

            eb.setTitle("JUNGLE");
            eb.setThumbnail(mob.getImageUrl());

            eb.addField(mob.getName() + " Killed!", "You earned " + goldEarned + " gold and gained " + experienceGained + " experience", false);

            if(newLevel!=level){
                eb.addField("You are now level " + newLevel, "", false);
            }


            eb.addField(sb.toString(),"", false);
            event.getChannel().sendMessage(eb.build()).queue();
        }


        if(messageSent.get()!=null){
            messageSent.get().delete().queue();
        }

        jungling.remove(id);
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
