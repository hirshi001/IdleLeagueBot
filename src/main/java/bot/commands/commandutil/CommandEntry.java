package bot.commands.commandutil;

import bot.commands.commandutil.Command;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CommandEntry{

    public Command command;
    public String defaultName;
    public String[] names;


    public CommandEntry(String[] names, Command command){
        this.defaultName = names[0];
        this.names = names;
        this.command = command;
    }


}
