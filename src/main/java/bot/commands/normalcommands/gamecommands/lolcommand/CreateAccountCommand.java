package bot.commands.normalcommands.gamecommands.lolcommand;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandManager;
import bot.database.MongoConnection;
import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.mongodb.client.model.Filters.eq;

public class CreateAccountCommand extends Command {

    private Set<Long> choosingLane = Collections.synchronizedSet(new HashSet<Long>());

    public CreateAccountCommand() {
    }

    @Override
    public String getHelp() {
        return "creates an account";
    }

    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {
        if (event.getAuthor().isFake()) {
            event.getChannel().sendMessage("Shut up you fake").queue();
            return;
        }

        final Long id = event.getAuthor().getIdLong();
        MongoCollection<Document> collection = MongoConnection.getUsersCollection();

        Bson filter = eq(id);

        if (collection.find(filter).first() != null) {
            event.getChannel().sendMessage("You already have an account").queue();
            return;
        }

        Document userDoc = new Document("_id", id).
                append("name", event.getAuthor().getName()).
                append("level", 1).
                append("experience", 0).
                append("be", 0);
        collection.insertOne(userDoc);

        MongoCollection<Document> inGameCollection = MongoConnection.getOneVOneBotCollection();
        Document inGameDoc = OneVOneBotCommand.defaultGameDoc(id);
        inGameCollection.insertOne(inGameDoc);

        MongoCollection<Document> cooldownCollection = MongoConnection.getCooldownsCollection();
        Document cooldownDoc = new Document("_id", id);
        cooldownCollection.insertOne(cooldownDoc);

        event.getChannel().sendMessage("<@"+id+">, you have succesfully created an account!").queue();

    }
}