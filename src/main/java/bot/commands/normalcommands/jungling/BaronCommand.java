package bot.commands.normalcommands.jungling;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandActionWaiter;
import bot.commands.commandutil.CommandManager;
import bot.commands.normalcommands.DDragonImage;
import bot.database.MongoConnection;
import bot.gameutil.champions.championexperience.ChampionExperience;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class BaronCommand extends Command {

    private long baronTime = 10*1000;
    private long initBaronCooldown = (20*60+30)*1000;

    final String baronUrl = "https://vignette.wikia.nocookie.net/leagueoflegends/images/1/15/Baron_Nashor_OriginalSkin.jpg/revision/latest?cb=20151202202626";

    private CommandActionWaiter askingToDoBaron;

    public BaronCommand(final JDA jda){
        askingToDoBaron = new CommandActionWaiter(15 * 1000L);
        askingToDoBaron.onTimerEnd((long id1, long id2) -> ((TextChannel)jda.getGuildChannelById(id2)).sendMessage("You took too long to respond").queue());
    }

    private long getReducedCooldown(int coolDownLevel){
        return coolDownLevel*30*1000L;
    }

    @Override
    public boolean requiresLiving() {
        return true;
    }

    @Override
    public String requiresLivingTitle() {
        return "You must be alive in order to do baron";
    }

    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {
        MongoDatabase db = MongoConnection.getDatabase();
        MongoCollection<Document> userCollection = db.getCollection("usersingame");
        final Long id = event.getAuthor().getIdLong();
        if(askingToDoBaron.contains(id)){
            askingToDoBaron.remove(id);
            return;
        }

        Document userDoc = userCollection.find(eq(id)).first();
        int level = userDoc.getInteger("level");

        if(level <=12 ){
            event.getChannel().sendMessage("You level is too low, there is a good change of dying.\nAre you sure you want to do baron? (y/yes or n/no)\n(respond in 15 seconds)").queue();
            askingToDoBaron.add(id, event.getChannel().getIdLong());
            return;
        }

        baron(id, event);
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        long id = event.getAuthor().getIdLong();
        if(askingToDoBaron.contains(id, event.getChannel().getIdLong())){
            String msg = event.getMessage().getContentRaw();
            if(msg.equalsIgnoreCase("y") || msg.equalsIgnoreCase("yes")){
                new Thread(() -> baron(id, event)).start();
            }else{
                event.getChannel().sendMessage("Not doing baron...").queue();
            }
            askingToDoBaron.remove(id);
            return;
        }
    }

    private void baron(Long id, GuildMessageReceivedEvent event){

        MongoDatabase db = MongoConnection.getDatabase();
        MongoCollection<Document> cooldownCollection = db.getCollection("cooldowns");
        Document cooldownDoc = cooldownCollection.find(eq(id)).first();
        Long startCooldown = cooldownDoc.getLong("baron");
        Integer cooldownLevel = cooldownDoc.getInteger("jungle cooldown level");
        if(cooldownLevel==null){
            cooldownCollection.updateOne(eq(id), set("jungle cooldown level", 0));
            cooldownLevel=0;
        }
        long cooldownTime = initBaronCooldown-getReducedCooldown(cooldownLevel);

        long finishTime = System.currentTimeMillis();
        if(startCooldown!=null && startCooldown+cooldownTime>finishTime) {
            int seconds = -(int) (finishTime - (startCooldown + cooldownTime)) / 1000;
            event.getChannel().sendMessage("Please wait " + seconds + " seconds before attacking baron").queue();
            return;
        }
        cooldownCollection.updateOne(eq(id), set("baron",System.currentTimeMillis()));


        MongoCollection<Document> userCollection = db.getCollection("onevonedoc");

        AtomicReference<Message> messageSent = new AtomicReference<>();

        event.getChannel().sendMessage("fighting baron...").queue((messageSent::set));

        try {
            Thread.sleep(baronTime);
            messageSent.get().delete();
            Document userDoc = userCollection.find(eq(id)).first();

            int experience = userDoc.getInteger("experience");
            int gold = userDoc.getInteger("gold");
            int level = userDoc.getInteger("level");

            int goldEarned = 600;
            int experienceEarned = 700;

            gold+=goldEarned;
            experience+=experienceEarned;
            int newLevel = level;

            while(newLevel<18 && ChampionExperience.experienceToReachLevel(newLevel+1)<experience){
                experience-=ChampionExperience.experienceToReachLevel(newLevel+1);
                newLevel++;
            }

            Bson filter;

            filter = eq(id);

            userDoc.put("gold", gold);
            userDoc.put("level", newLevel);
            userDoc.put("experience", experience);

            userCollection.replaceOne(filter, userDoc);


            StringBuilder sb = new StringBuilder();
            sb.append("You now have ");
            sb.append(gold);
            sb.append(" gold.");

            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.MAGENTA);

            eb.setTitle("JUNGLE");
            eb.setThumbnail(baronUrl);

            eb.addField( "Baron Killed!", "You earned " + goldEarned + " gold and gained " + experienceEarned + " experience", false);

            if(newLevel!=level){
                eb.addField("You are now level " + newLevel, "", false);
            }
            event.getChannel().sendMessage(eb.build()).queue();


        } catch (InterruptedException e) {
            event.getChannel().sendMessage("attempt failed for unknown reason").queue();
        }

        messageSent.get().delete().queue();

    }

    @Override
    public String getHelp() {
        return "Fight baron";
    }
}
