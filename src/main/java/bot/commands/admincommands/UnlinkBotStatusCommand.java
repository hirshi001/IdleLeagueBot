package bot.commands.admincommands;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandManager;
import bot.database.MongoConnection;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import static com.mongodb.client.model.Filters.eq;

public class UnlinkBotStatusCommand extends Command {

    @Override
    public String getHelp() {
        return "Unlinks the bot status frpm the channel";
    }

    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {
        if(MongoConnection.getChannelLinkCollection().findOneAndDelete(eq(event.getChannel().getIdLong()))==null){
            event.getChannel().sendMessage("This channel was never even linked").queue();
        }
        else{
            event.getChannel().sendMessage("This channel has been succesfully unlinked").queue();
        }
    }
}
