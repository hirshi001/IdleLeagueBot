package bot.commands.normalcommands.gamecommands.lolcommand;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandActionWaiter;
import bot.commands.commandutil.CommandManager;
import bot.database.MongoConnection;
import bot.gameutil.LaneConstants;
import bot.gameutil.Location;
import bot.gameutil.champions.champion.Champion;
import bot.gameutil.champions.champion.ChampionRegistry;
import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import javax.annotation.Nonnull;
import java.util.ArrayList;

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

        MongoCollection<Document> userCollection = MongoConnection.getOneVOneBotCollection();

        Long id = event.getAuthor().getIdLong();
        Document userDoc = userCollection.find(eq(id)).first();

        boolean userInGame = userDoc.getBoolean("ingame");

        if(userInGame){
            event.getChannel().sendMessage("Your already in a game").queue();
            return;
        }

        if(startingGame.contains(id)){
            return;
        }
        startingGame.add(id, event.getChannel().getIdLong());
        event.getChannel().sendMessage("Choose a champion. Example: ```lee sin```").queue();

    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        Long id = event.getAuthor().getIdLong();
        if(!startingGame.contains(id, event.getChannel().getIdLong())) return;

        String champName = event.getMessage().getContentStripped().trim();
        Champion champ = ChampionRegistry.getChampion(champName);
        if(champ==null){
            event.getChannel().sendMessage("That is not a valid champion!").queue();
            startingGame.remove(id);
            return;
        }

        MongoCollection<Document> usersingame = MongoConnection.getOneVOneBotCollection();

       Champion botChamp = ChampionRegistry.getRandomChampion();

        Document newDoc = new Document("_id", id).
                append("ingame", true).
                append("player", createNewChampDoc(true, champ)).
                append("bot", createNewChampDoc(false, botChamp));

        usersingame.replaceOne(eq(id),newDoc);
        event.getChannel().sendMessage("Your game has started! Welcome to the howling abyss").queue();
        startingGame.remove(id);

    }

    public static Document defaultGameDoc(long id){
        return new Document("_id", id).
                append("ingame", false);
    }


    public static Document createNewChampDoc(boolean blueSide, Champion champion){
        return new Document().
                append("champion", champion.getId()).
                append("isdead", false).
                append("revivetime", 0).
                append("location", blueSide ? Location.BLUE_FOUNTAIN.getId() : Location.RED_FOUNTAIN.getId()).
                append("gold", 500).
                append("level", 1).
                append("experience", 0).
                append("items", new ArrayList<Integer>(0));
    }


    @Override
    public String getHelp() {
        return "Starts a new game";
    }
}
