package bot.commands.normalcommands.link;

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

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.eq;

public class LinkBotStatusCommand extends Command {


    private static final List<TextChannel> channels = Collections.synchronizedList(new LinkedList<>());

    public static void cache(JDA jda){
        MongoCollection<Document> coll = MongoConnection.getChannelLinkCollection();
        for (MongoCursor<Document> it = coll.find().iterator(); it.hasNext(); ) {
            Document d = it.next();
            try{
                channels.add((TextChannel)jda.getGuildChannelById(d.getLong("_id")));
            } catch (ClassCastException cce){cce.printStackTrace();}

        }
    }

    public static List<TextChannel> getLinkedChannels(JDA jda){
        return channels;
    }


    public static void forEachLinked(JDA jda, Consumer<TextChannel> consumer) {
        Iterator<TextChannel> iter = channels.iterator();
        while(iter.hasNext()){
            TextChannel c = iter.next();
            try {
                consumer.accept(c);
            }catch(Exception e){
                if(jda.getGuildChannelById(c.getIdLong())==null){
                    removeChannelFromDB(c);
                    iter.remove();
                }
                throw e;
            }
        }
    }

    public static void removeChannel(TextChannel c){
        removeChannelFromDB(c);
        Iterator<TextChannel> iter = channels.iterator();
        while(iter.hasNext()) {
            TextChannel tc = iter.next();
            if(tc.getIdLong()==c.getIdLong()){
                iter.remove();
                return;
            }
        }
    }

    public static void removeChannelFromDB(TextChannel c){
        MongoConnection.getChannelLinkCollection().findOneAndDelete(eq(c.getIdLong()));
    }

    @Override
    public String getHelp() {
        return "links this bot to a channel which sets the bot status";
    }

    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {
        if(!event.getMember().hasPermission(Permission.MANAGE_WEBHOOKS) && !event.getMember().hasPermission(Permission.ADMINISTRATOR)){
            event.getChannel().sendMessage("You need the MANAGE_WEBHOOKS permission to use this command").queue();
            return;
        }
        long id = event.getChannel().getIdLong();
        MongoCollection<Document> coll = MongoConnection.getChannelLinkCollection();

        if(coll.find(eq("_id", id)).first()==null){
            if(coll.insertOne(new Document("_id", id)).wasAcknowledged()){
                event.getChannel().sendMessage("Link succesful").queue();
                channels.add(event.getChannel());
            }
            else{
                event.getChannel().sendMessage("Link not succesful").queue();
            }
        }
    }


}
