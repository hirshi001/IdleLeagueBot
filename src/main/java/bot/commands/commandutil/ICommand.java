package bot.commands.commandutil;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface ICommand {

    void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager);

    String getHelp();

    Arguments getArguments();

    void setArguments(Arguments args);

    boolean requiresAccount();

    String accountRequireTitle();

    boolean requiredInGame();

    String inGameRequireTitle();

    boolean requiresLiving();

    String requiresLivingTitle();

}
