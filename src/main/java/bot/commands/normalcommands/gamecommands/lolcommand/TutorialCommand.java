package bot.commands.normalcommands.gamecommands.lolcommand;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandActionWaiter;
import bot.commands.commandutil.CommandManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class TutorialCommand extends Command {

    MessageEmbed tutorialEmbed = tutorialEmbed();

    @Override
    public String getHelp() {
        return "Sends a tutorial for the user";
    }

    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {
        event.getChannel().sendMessage(tutorialEmbed).queue();
    }

    private MessageEmbed tutorialEmbed(){
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Welcome to Idle League");
        eb.addField("Getting started", "To start playing, you must first create an account. To do so, type\n`lol createaccount`", false);
        eb.addField("Starting a game", "to start a game, type\n`lol startgame`\nYou will then be asked to choose a champion. Send the champion name which you want to choose in the channel.", false);
        return eb.build();

    }
}
