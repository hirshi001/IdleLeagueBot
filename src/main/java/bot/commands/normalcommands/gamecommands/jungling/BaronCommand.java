package bot.commands.normalcommands.gamecommands.jungling;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandActionWaiter;
import bot.commands.commandutil.CommandManager;
import bot.commands.normalcommands.Roasts;
import bot.database.GameAccount;
import bot.database.MongoConnection;
import bot.gameutil.champions.championexperience.ChampionExperience;
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
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.inc;
import static com.mongodb.client.model.Updates.set;

public class BaronCommand extends Command {

    private long baronTime = 10;
    private long initBaronCooldown = (20*60+30)*1000;

    final String baronUrl = "https://vignette.wikia.nocookie.net/leagueoflegends/images/1/15/Baron_Nashor_OriginalSkin.jpg/revision/latest?cb=20151202202626";

    private CommandActionWaiter askingToDoBaron;
    private Set<Long> baroning = Collections.synchronizedSet(new HashSet<>());

    public BaronCommand(final JDA jda){
        askingToDoBaron = new CommandActionWaiter(15 * 1000L);
        askingToDoBaron.onTimerEnd((long id1, long id2) -> ((TextChannel)jda.getGuildChannelById(id2)).sendMessage("You took too long to respond").queue());
    }

    private long getReducedCooldown(int coolDownLevel){
        return coolDownLevel*30*1000L;
    }


    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {

        final long id = event.getAuthor().getIdLong();
        if(askingToDoBaron.contains(id)) return;

        Document userDoc = GameAccount.getOneVOne(id);
        Document d = userDoc.get("player", Document.class);
        int level = d.getInteger("level");

        if(level <=12 ){
            event.getChannel().sendMessage("You level is too low, there is a good change of dying.\nAre you sure you want to do baron? (y/yes or n/no)\n(respond in 15 seconds)").queue();
            askingToDoBaron.add(id, event.getChannel().getIdLong());
            return;
        }

        baron(id, event);
    }

    @Override
    public void onGuildMessageReceived(@Nonnull final GuildMessageReceivedEvent event) {
        final long id = event.getAuthor().getIdLong();
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


    private void baron(long id, GuildMessageReceivedEvent event){

        //check if the user is currently jungling
        if(baroningCheck(id, event)) return;

        //check if the users cooldown is over
        if(cooldownCheck(id, event)) return;

        //Player has passed all checks and can start jungling
        baroning.add(id);
        AtomicReference<Message> messageSent = new AtomicReference<>();
        event.getChannel().sendMessage("Doing Baron...").queue(messageSent::set);

        Bson filter = eq(id);
        System.out.println(baronTime*1000);
        try {
            Thread.sleep(baronTime*1000);
        } catch (InterruptedException e) {
            event.getChannel().sendMessage("Baron attempt failed. See this:\n\t "+e.getMessage()).queue();
            return;
        }

        //MongoConnection.getCooldownsCollection().findOneAndUpdate(filter, set("baron",System.currentTimeMillis()+initBaronCooldown));


        GameAccount gameAccount = new GameAccount(id);
        gameAccount.regenerateDoc();
        Document playerDocument = (Document)gameAccount.getDoc().get("player");

        int gold = playerDocument.getInteger("gold");
        int level = playerDocument.getInteger("level");
        int experience = playerDocument.getInteger("experience");

        int goldEarned = 600;
        int experienceEarned = 700;

        gold+=goldEarned;
        experience+=experienceEarned;

        int newLevel = level;
        while(newLevel<18 && ChampionExperience.experienceToReachLevel(newLevel+1)<experience){
            experience-=ChampionExperience.experienceToReachLevel(newLevel+1);
            newLevel++;
        }

        gameAccount.updateValue("player.gold", gold);
        if(newLevel!=level) gameAccount.updateValue("player.level", newLevel);
        gameAccount.updateValue("player.experience",experience);

        baroning.remove(id);

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.MAGENTA);

        eb.setTitle("JUNGLE");
        eb.setThumbnail(baronUrl);

        eb.addField("Baron Killed!", "You earned " + goldEarned + " gold and gained " + experienceEarned + " experience", false);

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

    private boolean baroningCheck(long id, GuildMessageReceivedEvent event){
        if(baroning.contains(id)){
            StringBuilder sb = new StringBuilder(Roasts.getRandomRoast()).append("\n\"You are already doing baron right now");
            event.getChannel().sendMessage(sb).queue();
            return true;
        }
        return false;
    }


    private boolean cooldownCheck(long id, GuildMessageReceivedEvent event){
        return false;
        /*
        Document cooldownDoc = GameAccount.getCooldown(id);
        long finishTime = cooldownDoc.getLong("baron");
        long currentTime = System.currentTimeMillis();
        if(finishTime>currentTime){
            int secs = (int)((finishTime-currentTime)/1000L);
            event.getChannel().sendMessage("Please wait "+secs+" seconds before doing baron again").queue();
            return true;
        }
        return false;
         */
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
    public String getHelp() {
        return "Fight baron";
    }
}
