package bot.commands.commandutil;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public abstract class Command extends ListenerAdapter implements ICommand {

    private Arguments args = Arguments.DEFAULT_ARGUMENTS;


    @Override
    public Arguments getArguments(){
        return args;
    }

    @Override
    public void setArguments(Arguments args){
        this.args = args;
    }


    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {

    }

    @Override
    public boolean requiresAccount(){
        return requiredInGame();
    }

    @Override
    public String accountRequireTitle(){
        return "You cannot perform this action without an account!";
    }

    @Override
    public boolean requiredInGame(){
        return requiresLiving();
    }

    @Override
    public String inGameRequireTitle(){
        return "You cannot perform this action without being in a game";
    }

    @Override
    public boolean requiresLiving(){ return false; }

    @Override
    public String requiresLivingTitle() { return "You cannot perform this action if you dead."; }
}
