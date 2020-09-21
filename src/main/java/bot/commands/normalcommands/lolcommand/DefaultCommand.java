package bot.commands.normalcommands.lolcommand;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandManager;
import bot.commands.normalcommands.Roasts;
import bot.database.MongoConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class DefaultCommand extends Command {


    public static final StringBuilder nut = new StringBuilder();
    static{
        nut.append("███╗░░██╗██╗░░░██╗████████╗\n");
        nut.append("████╗░██║██║░░░██║╚══██╔══╝\n");
        nut.append("██╔██╗██║██║░░░██║░░░██║░░░\n");
        nut.append("██║╚████║██║░░░██║░░░██║░░░\n");
        nut.append("██║░╚███║╚██████╔╝░░░██║░░░\n");
        nut.append("██║░╚███║╚██████╔╝░░░██║░░░\n");
        nut.append("╚═╝░░╚══╝░░░╚════════╝░░░░╚═╝░░░\n");
    }

    @Override
    public String getHelp() {
        return "";
    }

    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {
        System.out.println("HEIE");
        StringBuilder msgSend = new StringBuilder();

        Long channelId = event.getChannel().getIdLong();
        MongoDatabase db = MongoConnection.getDatabase();
        MongoCollection<Document> channelCommands =  db.getCollection("channelcommands");
        Document doc = channelCommands.find(eq(channelId)).first();
        if(doc!=null && doc.getBoolean("lol", false)){
            msgSend.append(Roasts.getRandomRoast());
        }


        if(name.trim().length()>0){
            msgSend.append("\nThat command doesn't exist!");
        }
        if(msgSend.length()!=0) event.getChannel().sendMessage(msgSend.toString()).queue();


    }

}
