package bot.commands.admincommands;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandManager;
import bot.commands.normalcommands.link.LinkBotStatusCommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class StopBotCommand extends Command {


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
