package bot.commands.admincommands;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandManager;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class SendAnnouncementCommand extends Command {
    @Override
    public String getHelp() {
        return "Sends an announcement to all linked channels";
    }


    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {
        for(TextChannel t : LinkBotStatusCommand.getLinkedChannels(event.getJDA())){
            t.sendMessage(msg).queue();
        }
    }
}
