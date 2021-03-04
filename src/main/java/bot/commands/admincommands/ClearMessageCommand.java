package bot.commands.admincommands;

import bot.commands.commandutil.Arguments;
import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
        VoiceChannel vc = event.getJDA().getVoiceChannelById(1);
        AudioManager manager = event.getGuild().getAudioManager();
        manager.getSendingHandler().provide20MsAudio().put(new byte[]{});
        try {
            int num = Integer.parseInt(msg);
            MessageHistory mh = event.getChannel().getHistoryBefore(event.getMessageIdLong(), num).complete();
            ArrayList<String> messages = new ArrayList<>(mh.size());
            OffsetDateTime startTime = OffsetDateTime.now(Clock.systemDefaultZone()).minusDays(10);
            Iterator<Message> iter = mh.getRetrievedHistory().iterator();
            while(iter.hasNext()){
                Message m = iter.next();
                if(m.getTimeCreated().isBefore(startTime)){
                    break;
                }
                messages.add(m.getId());
            }
            event.getChannel().deleteMessagesByIds(messages).queue();
            while(iter.hasNext()) iter.next().delete().complete();

            event.getMessage().delete().queue();
            event.getChannel().sendMessage("deleted").queue((message -> message.delete().queueAfter(5, TimeUnit.SECONDS)));
        } catch(NumberFormatException nfe){
            event.getChannel().sendMessage("Invalid input").queue();
        }
    }

}
