package bot.commands.normalcommands.gamecommands.items;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandManager;
import bot.database.MongoConnection;
import bot.gameutil.items.Item;
import bot.gameutil.items.ItemRegistry;
import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class SellItemCommand extends Command {

    @Override
    public boolean requiredInGame() {
        return true;
    }

    @Override
    public String inGameRequireTitle() {
        return "You must be in a game to sell items";
    }

    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {
        if(true){
            event.getChannel().sendMessage("this command is currently disabled").queue();
            return;
        }
        msg = msg.toLowerCase();
        if(!ItemRegistry.containsItem(msg)){
            event.getChannel().sendMessage("This item doesn't exist").queue();
            return;
        }

        MongoCollection<Document> usersCollection = MongoConnection.getOneVOneBotCollection();
        Long id = event.getAuthor().getIdLong();
        Bson filter = eq(id);

        Document d = usersCollection.find(filter).first().get("player", Document.class);

        List<Integer> items = d.getList("items", Integer.class);

        Item itemToSell = ItemRegistry.getItem(msg);

        if(!items.contains(itemToSell.getId())){
            event.getChannel().sendMessage("You do not have this item").queue();
            return;
        }

        items.remove(itemToSell.getId());
        int userMoney = d.getInteger("gold");
        int itemSellPrice = itemToSell.getSellCost();

        userMoney+=itemSellPrice;

        Bson updateOp = set("gold", userMoney);
        usersCollection.findOneAndUpdate(filter, updateOp);

        updateOp = set("items", items);
        usersCollection.findOneAndUpdate(filter, updateOp);

        event.getChannel().sendMessage("You successfully sold your " + itemToSell.getName() + " for "+itemSellPrice+" gold").queue();
    }

    @Override
    public String getHelp() {
        return "sell an item";
    }
}
