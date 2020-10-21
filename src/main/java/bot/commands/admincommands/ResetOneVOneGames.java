package bot.commands.admincommands;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandManager;
import bot.database.MongoConnection;
import com.mongodb.client.model.Updates;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import java.util.Arrays;
import java.util.Iterator;

import static com.mongodb.client.model.Updates.set;
import static com.mongodb.client.model.Updates.unset;

public class ResetOneVOneGames extends Command {
    @Override
    public String getHelp() {
        return "resets all the OneVOne bot games";
    }

    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {
       MongoConnection.getOneVOneBotCollection().updateMany(new Document(), set("ingame",false));
    }
}
