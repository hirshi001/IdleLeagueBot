package bot.commands.commandutil;

import bot.commands.commandutil.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.Color;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CommandEntry{

    public Command command;
    public String defaultName;
    public String[] names;

    public MessageEmbed helpEmbed;


    public CommandEntry(String[] names, Command command, String prefix){
        this.defaultName = names[0];
        this.names = names;
        this.command = command;

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.GREEN);
        eb.setTitle(defaultName + " command");
        eb.setDescription(command.getHelp());
        eb.addField("How to call this command", "<...> - optional\n[...] - required\n`" + prefix+ " " +defaultName+" " + command.getArguments().toString()+ "`", true);
        StringBuilder sb = new StringBuilder();
        for(String name:names){
            sb.append(name).append("\n");
        }
        eb.addField("Command Names", sb.toString(), false);
        helpEmbed = eb.build();
    }


}
