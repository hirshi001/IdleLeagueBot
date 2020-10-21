package bot.commands.normalcommands.help;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandEntry;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.Color;

public class HelpSection {

    private String name;
    private CommandEntry[] commands;

    private MessageEmbed helpPage;

    public HelpSection(String name, CommandEntry... commands){
        this.name = name;
        this.commands = commands;
        buildHelpPage();
    }

    private void buildHelpPage(){
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.ORANGE);
        eb.setTitle(name);
        eb.setDescription("Type lol help [command name] for more help about that command");
        StringBuilder sb = new StringBuilder();
        for(CommandEntry c: commands){
            sb.append("``").append(c.defaultName).append("`` ");
        }
        eb.addField(sb.toString(),"",false);
        helpPage = eb.build();
    }

    public String getName(){
        return name;
    }

    public MessageEmbed getHelpPage(){
        return helpPage;
    }





}
