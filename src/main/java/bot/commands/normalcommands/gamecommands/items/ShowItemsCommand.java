package bot.commands.normalcommands.gamecommands.items;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandManager;
import bot.database.MongoConnection;
import bot.gameutil.items.Item;
import bot.gameutil.items.ItemRegistry;
import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class ShowItemsCommand extends Command {

    @Override
    public boolean requiredInGame() {
        return true;
    }

    @Override
    public String inGameRequireTitle() {
        return "You must be in a game to show items";
    }

    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {
        Long id = event.getAuthor().getIdLong();
        MongoCollection<Document> userCollection = MongoConnection.getOneVOneBotCollection();
        Document userDoc = userCollection.find(eq(id)).first().get("player",Document.class);

        List<Integer> items = userDoc.getList("items", Integer.class);

        if(items==null){
            items = new LinkedList<>();
        }

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(new Color(250,150,0));
        eb.setTitle(event.getAuthor().getName()+"'s Items");

        String itemName;
        Item item;
        for(Integer i:items){
            item = ItemRegistry.getItem(i);
            if(item==null){
                eb.addField("Invalid item" , "Id of "+i, false);
                continue;
            }
            itemName = item.getName();
            eb.addField(itemName , item.getDisplayInformation(), false);
        }

        event.getChannel().sendMessage(eb.build()).queue();



    }

    @Override
    public String getHelp() {
        return "Displays the items the user has";
    }
}
