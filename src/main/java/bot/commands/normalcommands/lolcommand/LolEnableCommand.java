package bot.commands.normalcommands.lolcommand;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandManager;
import bot.database.MongoConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class LolEnableCommand extends Command {

    @Override
    public String getHelp() {
        return "Enables the lol command";
    }

    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {
        if(!event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)){
            event.getChannel().sendMessage("you do not have sufficient permissions to use this command").queue();
            return;
        }
        MongoCollection<Document> channelCommands = MongoConnection.getDatabase().getCollection("channelcommands");
        Long channelId = event.getChannel().getIdLong();
        UpdateResult result = channelCommands.updateOne(eq(channelId), set("lol", true));
        if(result.getMatchedCount()==0){
            channelCommands.insertOne(new Document("_id", channelId).append("lol", true));
        }

        event.getChannel().sendMessage("lol command enabled").queue();

    }
}
