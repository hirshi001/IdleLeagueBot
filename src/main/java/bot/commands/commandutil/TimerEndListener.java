package bot.commands.commandutil;

@FunctionalInterface
public interface TimerEndListener {

    public void onTimerEnd(long id1, long id2);
}
