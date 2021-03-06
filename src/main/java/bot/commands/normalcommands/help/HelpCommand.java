package bot.commands.normalcommands.help;

import bot.commands.commandutil.Arguments;
import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandManager;
import bot.commands.normalcommands.help.helpsection.DefaultHelpSection;
import bot.commands.normalcommands.help.helpsection.HelpSection;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class HelpCommand extends Command{

    private Map<String, HelpSection> map;
    private MessageEmbed helpPage;

    public HelpCommand(){

        Arguments args = new Arguments().addArgument("command/section name", true);
        args.update();
        setArguments(args);

    }

    public void createHelpPage(HelpSection... arr){

        map = new HashMap<>();
        for(HelpSection hs: arr){
            map.put(hs.getName().toLowerCase(), hs);
            hs.buildHelpPage();
        }

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.MAGENTA);
        eb.setTitle("Help");
        eb.setDescription("type lol help [section name] for help about commands in that section");
        for(HelpSection hs:arr){
            eb.addField("`"+hs.getName()+"`","",true);
        }
        helpPage = eb.build();
    }

    @Override
    public String getHelp() {
        return "shows help page";
    }

    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {
        msg = msg.toLowerCase();
        if(map.containsKey(msg)){
            event.getChannel().sendMessage(map.get(msg).getHelpPage()).queue();
        }
        else if(commandManager.getCommandsMap().containsKey(msg)){
            event.getChannel().sendMessage(commandManager.getCommandsMap().get(msg).helpEmbed).queue();
        }
        else{
            event.getChannel().sendMessage(helpPage).queue();
        }
    }

}
