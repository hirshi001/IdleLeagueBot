package bot.commands.normalcommands.help.helpsection;

import bot.commands.commandutil.CommandEntry;
import net.dv8tion.jda.api.EmbedBuilder;

public class GameCommandsHelpSection extends HelpSection {

    public GameCommandsHelpSection(String name) {
        super(name);
    }

    @Override
    protected void createEmbedBuilder(EmbedBuilder eb) {
        super.createEmbedBuilder(eb);
        eb.addField("for new users, to help with getting started, type", "lol tutorial", false);
    }
}
