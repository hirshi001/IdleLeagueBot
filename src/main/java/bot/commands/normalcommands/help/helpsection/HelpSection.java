package bot.commands.normalcommands.help.helpsection;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandEntry;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

public class HelpSection {

    private String name;
    private List<CommandEntry> commandsList = new LinkedList<>();

    private MessageEmbed helpPage;
    private EmbedBuilder eb;

    public HelpSection(String name){
        this.name = name;
        eb = new EmbedBuilder();
        createEmbedBuilder(eb);
    }

    public void addCommandEntry(CommandEntry ce){
        commandsList.add(ce);
    }

    public void buildHelpPage(){
        helpPage = eb.build();
    }

    public EmbedBuilder getEmbedBuilder(){
        return eb;
    }

    public void createEmbedBuilder(EmbedBuilder eb){
        eb.setColor(Color.ORANGE);
        eb.setTitle(name);
        eb.setDescription("Type lol help [command name] for more help about that command");
        StringBuilder sb = new StringBuilder();
        for(CommandEntry c: commandsList){
            sb.append("``").append(c.defaultName).append("`` ");
        }
        eb.addField(sb.toString(),"",false);
    }

    public String getName(){
        return name;
    }

    public MessageEmbed getHelpPage(){
        return helpPage;
    }





}
