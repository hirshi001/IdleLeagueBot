package bot.commands.admincommands;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandManager;
import bot.commands.normalcommands.LinkBotStatusCommand;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class StopBotCommand extends Command {

    volatile boolean stoppingBot = false;
    int exitCount;

    @Override
    public String getHelp() {
        return "Stops the bot";
    }



    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {
        LinkBotStatusCommand.forEachLinked(event.getJDA(), (c) -> c.sendMessage("Bot is shutting down...").queue());
        event.getJDA().shutdown();
    }

}
