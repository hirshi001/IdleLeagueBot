package bot.commands.commandutil;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public abstract class Command extends ListenerAdapter implements ICommand {

    public abstract String getHelp();


    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {

    }

    public boolean requiresAccount(){
        return requiredInGame();
    }

    public String accountRequireTitle(){
        return "You cannot perform this action without an account!";
    }

    public boolean requiredInGame(){
        return requiresLiving();
    }

    public String inGameRequireTitle(){
        return "You cannot perform this action without being in a game";
    }

    public boolean requiresLiving(){ return false; }

    public String requiresLivingTitle() { return "You cannot perform this action if you dead."; }
}
