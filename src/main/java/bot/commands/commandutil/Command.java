package bot.commands.commandutil;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public abstract class Command extends ListenerAdapter implements ICommand {

    private Arguments args = Arguments.DEFAULT_ARGUMENTS;

    public abstract String getHelp();

    public Arguments getArguments(){
        return args;
    }

    public void setArguments(Arguments args){
        this.args = args;
    }


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
