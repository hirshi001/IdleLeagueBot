package bot.commands.normalcommands;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandEntry;
import bot.commands.commandutil.CommandManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HelpCommand extends Command {

    @Override
    public String getHelp() {
        return "list of commands";
    }

    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {

        int commandsPerPage = 7;
        int pages = (int)Math.ceil((double)commandManager.getCommands().size()/commandsPerPage);
        try{
            int page = Integer.parseInt(msg);
            if(page>pages || page<1){
                displayPage(name, event,commandManager, commandsPerPage,pages, 1);
                return;
            }

            displayPage(name, event, commandManager, commandsPerPage,pages, page);
            return;

        } catch(NumberFormatException nfe){
            Map<String, CommandEntry> map= commandManager.getCommandsMap();
            String newMsg = msg.toLowerCase();
            if(map.containsKey(newMsg)){
                CommandEntry ce = map.get(newMsg);
                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(Color.PINK);
                eb.setTitle(ce.defaultName);
                StringBuilder sb = new StringBuilder();
                if(ce.names.length==1){
                    sb.append("No aliases for this command");
                }
                else{
                    for(int i=1;i<ce.names.length;i++){
                        sb.append("`").append(ce.names[i]).append("` ");
                    }
                }
                eb.addField("Aliases: "+sb.toString(),ce.command.getHelp(), false);
                event.getChannel().sendMessage(eb.build()).queue();
                return;
            }

            displayPage(name, event,commandManager, commandsPerPage,pages, 1);
            return;
        }
    }

    private void displayPage(String name, GuildMessageReceivedEvent event, CommandManager manager, int commandsPerPage, int pages, int page){
        String help, title, prefix = manager.getPrefix();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Commands");
        eb.addField("Type '"+prefix+" "+name +" [command name]' for additional help with that command", "\\_\\_\\_\\_\\_\\_\\_\\_\\_\\_\\_\\_\\_\\_\\_\\_\\_\\_\\_\\_\\_",false);
        eb.setColor(new Color(70,150,70));
        List<CommandEntry> ceList = manager.getCommands();

        int commandsTotal = ceList.size();
        int i;
        int initCommandNumber = commandsPerPage*(page-1);
        CommandEntry ce;

        for(i=0;i<commandsPerPage && i+initCommandNumber<commandsTotal;i++){

            ce = ceList.get(i+initCommandNumber);
            help = ce.command.getHelp();
            if(help==null){
                help = "No information provided.";
            }
            title = prefix + " " + ce.defaultName;
            eb.addField(title, help, false);
        }
        eb.setFooter("Page "+page+" out of "+pages);
        event.getChannel().sendMessage(eb.build()).queue();
    }
}
