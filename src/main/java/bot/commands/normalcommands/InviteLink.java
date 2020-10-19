package bot.commands.normalcommands;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class InviteLink extends Command {

    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {
        event.getChannel().sendMessage(event.getJDA().getInviteUrl()).queue();
    }

    @Override
    public String getHelp() {
        return "The invite link for this bot";
    }
}