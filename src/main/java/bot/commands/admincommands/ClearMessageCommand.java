package bot.commands.admincommands;

import bot.commands.commandutil.Arguments;
import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ClearMessageCommand extends Command {

    public ClearMessageCommand(){
        Arguments args = new Arguments();
        args.addArgument("number of Messages", false);
        args.update();
        setArguments(args);
    }
    @Override
    public String getHelp() {
        return "Deletes the last messages sent in the channel";
    }


    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {

        try {
            int num = Integer.parseInt(msg);
            MessageHistory mh = event.getChannel().getHistoryBefore(event.getMessageIdLong(), num).complete();
            for(Message m: mh.getRetrievedHistory()){
                m.delete().queue();
            }
            event.getMessage().delete().queue();
            event.getChannel().sendMessage("deleted").queue((message -> message.delete().queueAfter(10, TimeUnit.SECONDS)));
        } catch(NumberFormatException nfe){
            event.getChannel().sendMessage("Invalid input").queue();
        }
    }
}
