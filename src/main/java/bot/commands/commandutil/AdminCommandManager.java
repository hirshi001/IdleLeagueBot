package bot.commands.commandutil;

import bot.database.MongoConnection;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import javax.annotation.Nonnull;

import static com.mongodb.client.model.Filters.eq;

public class AdminCommandManager extends CommandManager {

    public AdminCommandManager(JDA jda) {
        super(jda);
    }

    public void addAdmin(Long id, String name){
        MongoConnection.getDatabase().
                getCollection("admins").
                insertOne(new Document("_id",id).append("name", name));
    }

    public boolean isAdmin(Long id){
        return MongoConnection.getDatabase().getCollection("admins").find(eq(id)).first()!=null;
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if(!isAdmin(event.getAuthor().getIdLong())) return;
        super.onGuildMessageReceived(event);
    }
}
