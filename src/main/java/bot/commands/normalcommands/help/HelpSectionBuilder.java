package bot.commands.normalcommands.help;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandEntry;

import java.util.LinkedList;

public class HelpSectionBuilder {

    private String name;
    private LinkedList<CommandEntry> commands = new LinkedList<>();

    public HelpSectionBuilder(String name){
        this.name = name;
    }

    public void add(CommandEntry ce){
        commands.add(ce);
    }

    public HelpSection build(){
        return new HelpSection(name, commands.toArray(new CommandEntry[]{}));
    }





}
