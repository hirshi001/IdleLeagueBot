package bot.commands.normalcommands.gamecommands.location;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandManager;
import bot.database.GameAccount;
import bot.gameutil.Location;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

public class GetLocationCommand extends Command {
    @Override
    public String getHelp() {
        return "gives you your location";
    }

    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {
        GameAccount ga = new GameAccount(event.getAuthor().getIdLong());
        Document d = (Document)ga.getDoc().get("player");

        event.getChannel().sendMessage("You are at "+Location.get(d.getInteger("location")).getName()).queue();
    }

    @Override
    public boolean requiredInGame() {
        return true;
    }
}
