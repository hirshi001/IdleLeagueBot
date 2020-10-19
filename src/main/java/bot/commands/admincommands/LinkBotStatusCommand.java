package bot.commands.admincommands;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandManager;
import bot.database.MongoConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import javax.annotation.Nonnull;

import java.util.LinkedList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class LinkBotStatusCommand extends Command {

    public static List<TextChannel> getLinkedChannels(JDA jda){
        MongoCollection<Document> coll = MongoConnection.getChannelLinkCollection();
        LinkedList<TextChannel> channels = new LinkedList<>();
        for (MongoCursor<Document> it = coll.find().iterator(); it.hasNext(); ) {
            Document d = it.next();
            try{
                channels.add((TextChannel)jda.getGuildChannelById(d.getLong("_id")));
            } catch (ClassCastException cce){cce.printStackTrace();}

        }
        return channels;
    }

    @Override
    public String getHelp() {
        return "links this bot to a channel which sets the bot status";
    }

    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {
        long id = event.getChannel().getIdLong();
        MongoCollection<Document> coll = MongoConnection.getChannelLinkCollection();

        if(coll.find(eq("_id", id)).first()==null){
            if(coll.insertOne(new Document("_id", id)).wasAcknowledged()){
                event.getChannel().sendMessage("Link succesful").queue();
            }
            else{
                event.getChannel().sendMessage("Link not succesful").queue();
            }
        }
    }


}
