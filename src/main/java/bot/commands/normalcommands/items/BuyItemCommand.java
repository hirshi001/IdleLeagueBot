package bot.commands.normalcommands.items;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandManager;
import bot.database.MongoConnection;
import bot.gameutil.items.Item;
import bot.gameutil.items.ItemRegistry;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class BuyItemCommand extends Command {

    @Override
    public boolean requiredInGame() {
        return true;
    }

    @Override
    public String inGameRequireTitle() {
        return "You must be in a game to buy items";
    }

    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {
        MongoDatabase db = MongoConnection.getDatabase();
        msg = msg.toLowerCase();
        if(!ItemRegistry.containsItem(msg)){
            event.getChannel().sendMessage("This item doesn't exist").queue();
            return;
        }
        MongoCollection<Document> usersingame = db.getCollection("usersingame");
        Long id = event.getAuthor().getIdLong();
        Bson filter = eq(id);

        Document d = usersingame.find(filter).first();

        List<Integer> items = d.getList("items", Integer.class);

        if(items.size()>=6){
            event.getChannel().sendMessage("You have too many items to buy another one").queue();
            return;
        }

        Item itemToBuy = ItemRegistry.getItem(msg);

        int userMoney = d.getInteger("gold");
        int itemCost = itemToBuy.getCost();
        if(userMoney < itemCost){
            event.getChannel().sendMessage("You do not have enough gold").queue();
            return;
        }
        items.add(itemToBuy.getId());
        userMoney-=itemCost;

        Bson updateOp = set("gold", userMoney);
        usersingame.findOneAndUpdate(filter, updateOp);

        updateOp = set("items", items);
        usersingame.findOneAndUpdate(filter, updateOp);

        event.getChannel().sendMessage("You successfully bought a " +itemToBuy.getName()).queue();

    }

    @Override
    public String getHelp() {
        return "buy an item";
    }
}
