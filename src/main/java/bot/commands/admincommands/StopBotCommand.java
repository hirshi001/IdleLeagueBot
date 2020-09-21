package bot.commands.admincommands;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class StopBotCommand extends Command {
    @Override
    public String getHelp() {
        return "Stops the bot";
    }



    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {
        System.exit(0);
    }
}
