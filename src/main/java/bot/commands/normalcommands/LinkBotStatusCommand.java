package bot.commands.normalcommands;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandManager;
import bot.database.MongoConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import javax.annotation.Nonnull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

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

    public static void forEachLinked(JDA jda, Consumer<TextChannel> consumer){
        MongoCollection<Document> coll = MongoConnection.getChannelLinkCollection();
        Iterator<Document> iter = coll.find().iterator();
        while(iter.hasNext()){
            try {
                long id = iter.next().getLong("_id");
                consumer.accept((TextChannel)jda.getGuildChannelById(id));
            }catch(ClassCastException cce){
                cce.printStackTrace();
            }
        }
    }

    @Override
    public String getHelp() {
        return "links this bot to a channel which sets the bot status";
    }

    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {
        if(!event.getMember().hasPermission(Permission.MANAGE_WEBHOOKS)){
            event.getChannel().sendMessage("You need the MANAGE_WEBHOOKS permission to use this command").queue();
            return;
        }
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
