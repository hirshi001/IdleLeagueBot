package bot.commands.commandutil;

import bot.commands.commandutil.CommandManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface ICommand {

    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager);


}
