package bot.commands.commandutil;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class CommandActionWaiter implements Runnable{


    final Queue<CommandAction> queue = new LinkedList<>();
    final Map<Long, CommandAction> map = new HashMap<>();
    long time;

    private TimerEndListener timerEndListener;

    public CommandActionWaiter(long time){
        this.time = time;
    }

    public void add(long id1, long id2){
        CommandAction ca = new CommandAction(id1, id2, System.currentTimeMillis()+time);
        synchronized (queue){
            synchronized (map) {
                queue.add(ca);
                map.put(id1, ca);
                if(queue.size()==1){
                    new Thread(this).start();
                }
            }
        }
    }

    public boolean contains(long id){
        return map.containsKey(id);
    }

    public boolean contains(long id1, long id2){
        synchronized (map) {
            if (!map.containsKey(id1)) return false;
            return map.get(id1).id2==id2;
        }
    }

    public CommandAction remove(long id){
        return map.remove(id);
    }


    @Override
    public void run() {
        CommandAction ce;
        while(!queue.isEmpty()) {
            ce = queue.peek();
            long sleepTime = ce.endTime - System.currentTimeMillis();
            if (sleepTime > 0){
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                }
            }
            synchronized (queue){
                synchronized (map) {
                    ce = map.remove(queue.remove().id1);
                    if(ce != null){
                        timerEndListener.onTimerEnd(ce.id1, ce.id2);
                    }
                }
                if (queue.isEmpty()){
                    return;
                }
            }
        }
    }

    public void onTimerEnd(TimerEndListener listener){
        this.timerEndListener = listener;
    }
}

class CommandAction implements Comparable<CommandAction>{

    long id1, id2;

    long endTime;

    public CommandAction(long id1, long id2, long endTime){
        this.id1 = id1;
        this.id2 = id2;
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object obj) {
        return id1 == ((CommandAction) obj).id1;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id1);
    }

    @Override
    public int compareTo(@NotNull CommandAction o) {
        return Long.compare(endTime, o.endTime);
    }
}
