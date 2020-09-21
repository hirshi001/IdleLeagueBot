package bot.commands.normalcommands.lolcommand;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandActionWaiter;
import bot.commands.commandutil.CommandManager;
import bot.database.MongoConnection;
import bot.gameutil.LaneConstants;
import bot.gameutil.champions.champion.Champion;
import bot.gameutil.champions.champion.ChampionRegistry;
import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import javax.annotation.Nonnull;
import javax.print.Doc;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;


public class OneVOneBotCommand extends Command {

    CommandActionWaiter startingGame;

    public OneVOneBotCommand(JDA jda){
        startingGame = new CommandActionWaiter(10 * 1000L);
        startingGame.onTimerEnd((long id1, long id2) ->
                ((TextChannel) jda.getGuildChannelById(id2)).sendMessage("<@!"+id1+">, You took too long to respond!").queue()
        );
    }

    @Override
    public boolean requiresAccount() {
        return true;
    }

    @Override
    public String accountRequireTitle() {
        return "You cannot use this command without an account";
    }

    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager)
    {

        if(true){
            event.getChannel().sendMessage("This command is temporarily disabled").queue();
            return;
        }

        MongoCollection<Document> userCollection = MongoConnection.getDatabase().getCollection("usersingame");

        Long id = event.getAuthor().getIdLong();
        Document userDoc = userCollection.find(eq(id)).first();

        if(userDoc==null){
            event.getChannel().sendMessage("You need to create an account in order to start a game").queue();
            return;
        }

        boolean userInGame = userDoc.getBoolean("ingame");

        if(userInGame){
            event.getChannel().sendMessage("Your already in a game").queue();
            return;
        }

        if(startingGame.contains(id)){
            return;
        }
        startingGame.add(id, event.getChannel().getIdLong());
        event.getChannel().sendMessage("Choose a lane and champion. Example: ```mid leesin```\nThe enemy bot will be the same lane as you").queue();

    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        Long id = event.getAuthor().getIdLong();
        if(!startingGame.contains(id, event.getChannel().getIdLong())) return;

        String[] args = event.getMessage().getContentRaw().split(" ",2);
        int lane =  LaneConstants.getLane(args[0]);
        if(lane==-1){
            event.getChannel().sendMessage("That is not a valid lane!").queue();
            startingGame.remove(id);
            return;
        }
        String champName = args[1];
        Champion c = ChampionRegistry.getChampion(champName);
        if(c==null){
            event.getChannel().sendMessage("That is not a valid champion!").queue();
            startingGame.remove(id);
            return;
        }

        MongoCollection<Document> usersingame = MongoConnection.getDatabase().getCollection("usersingame");

       // ChampionRegistry.getRandomChampion(lane);

        Document newDoc = new Document("_id", id).
                append("ingame", true).
                append("champion", c.getId()).
                append("isdead", false).
                append("revivetime", 0).
                append("lane", lane).
                append("gold", 500).
                append("level", 1).
                append("experience", 0).
                append("items", new LinkedList<Integer>());

        usersingame.replaceOne(eq(id),newDoc);
        event.getChannel().sendMessage("Your game has started! Welcome to the rift").queue();
        startingGame.remove(id);

    }

    private void createNewInGameDoc(long id, int lane, Champion champion){
        Document newDoc = new Document("_id", id).
                append("champion", champion.getId()).
                append("isdead", false).
                append("revivetime", 0).
                append("lane", lane).
                append("gold", 500).
                append("level", 1).
                append("experience", 0).
                append("items", new LinkedList<Integer>());
    }


    @Override
    public String getHelp() {
        return "Starts a new game";
    }
}
