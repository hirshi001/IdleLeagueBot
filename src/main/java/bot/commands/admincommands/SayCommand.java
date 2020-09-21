package bot.commands.admincommands;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class SayCommand extends Command {

    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {
        event.getMessage().delete().queue();
        event.getChannel().sendMessage(msg).queue();
    }

    @Override
    public String getHelp() {
        return "Makes the bot say certain text";
    }
}
