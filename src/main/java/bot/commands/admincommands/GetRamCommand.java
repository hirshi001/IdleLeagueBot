package bot.commands.admincommands;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class GetRamCommand extends Command {


    @Override
    public String getHelp() {
        return "Sends total memory and total free memory";
    }

    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {
        long totalMemory = Runtime.getRuntime().totalMemory()/1048576;
        long freeMemory = Runtime.getRuntime().freeMemory()/1048576;

        event.getChannel().sendMessage("Total Memory = " + totalMemory +
                "MB\nFree Memory = " + freeMemory +
                "MB\nRemaining Memory = " + (totalMemory-freeMemory) + "MB").queue();


    }
}
