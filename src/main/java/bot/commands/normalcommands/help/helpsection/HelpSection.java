package bot.commands.normalcommands.help.helpsection;

import bot.commands.commandutil.CommandEntry;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.Color;

public interface HelpSection {



    void addCommandEntry(CommandEntry ce);

    void buildHelpPage();

    EmbedBuilder getEmbedBuilder();

    void setEmbedBuilder();

    String getName();

    MessageEmbed getHelpPage();

}
