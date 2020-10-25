package bot.commands.normalcommands.other;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class DiscordCommand extends Command {
    @Override
    public String getHelp() {
        return "sends the main discord bot testing server for this bot";
    }

    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {
        event.getChannel().sendMessage("https://discord.gg/EJPywSG").queue();
    }
}
