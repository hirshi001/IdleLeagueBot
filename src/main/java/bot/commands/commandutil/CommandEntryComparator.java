package bot.commands.commandutil;

import java.util.Comparator;

public class CommandEntryComparator implements Comparator<CommandEntry> {

    @Override
    public int compare(CommandEntry o1, CommandEntry o2) {
        return o1.defaultName.compareTo(o2.defaultName);
    }
}
