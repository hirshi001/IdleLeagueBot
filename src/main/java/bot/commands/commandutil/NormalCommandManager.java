package bot.commands.commandutil;

import bot.database.MongoConnection;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nonnull;

import static com.mongodb.client.model.Filters.eq;

public class NormalCommandManager extends CommandManager {

    public NormalCommandManager(JDA jda) {
        super(jda);
    }


    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (MongoConnection.getDatabase().
                getCollection("banned").
                find(eq(event.getAuthor().getIdLong())).first() == null) {
            new Thread(() -> super.onGuildMessageReceived(event)).start();
        }
    }
}
