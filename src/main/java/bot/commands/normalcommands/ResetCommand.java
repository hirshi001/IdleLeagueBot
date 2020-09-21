package bot.commands.normalcommands;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandManager;
import bot.database.MongoConnection;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.conversions.Bson;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.mongodb.client.model.Filters.eq;

public class ResetCommand extends Command {


    Set<Long> deleting = Collections.synchronizedSet( new HashSet<>());

    @Override
    public boolean requiresAccount() {
        return true;
    }

    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {


        final Long id = event.getAuthor().getIdLong();
        if(MongoConnection.getDatabase().getCollection("users").find(eq(id)).first()==null){
            event.getChannel().sendMessage(Roasts.getRandomRoast()+"\nYou can't reset without an account lmao.").queue();
            return;
        }
        deleting.add(id);
        event.getChannel().sendMessage("Type 'confirm' in the next 10 seconds to delete your account").queue();

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {  }

                if(deleting.contains(id)){
                    deleting.remove(id);
                    event.getChannel().sendMessage("Account not deleted").queue();
                }
            }
        }.start();
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        Long id = event.getAuthor().getIdLong();
        if(deleting.contains(id)){
            if(event.getMessage().getContentRaw().equals("confirm")){
                Bson filter = eq(id);
                MongoDatabase db = MongoConnection.getDatabase();

                db.getCollection("users").findOneAndDelete(filter);
                db.getCollection("cooldowns").findOneAndDelete(filter);
                db.getCollection("usersingame").findOneAndDelete(filter);

                event.getChannel().sendMessage("<@"+event.getAuthor().getId() + ">, your account has been deleted").queue();
                deleting.remove(id);
            }
        }
    }

    @Override
    public String getHelp() {
        return "deletes your account";
    }
}
