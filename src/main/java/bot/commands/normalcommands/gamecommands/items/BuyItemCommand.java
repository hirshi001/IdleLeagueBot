package bot.commands.normalcommands.gamecommands.items;

import bot.commands.commandutil.Arguments;
import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandManager;
import bot.database.GameAccount;
import bot.database.MongoConnection;
import bot.gameutil.items.Item;
import bot.gameutil.items.ItemRegistry;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class BuyItemCommand extends Command {

    public BuyItemCommand(){
        setArguments(new Arguments().addArgument("item name", false).update());
    }

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
        msg = msg.toLowerCase();
        if(!ItemRegistry.containsItem(msg)){
            event.getChannel().sendMessage("This item doesn't exist").queue();
            return;
        }

        GameAccount ga = new GameAccount(event.getAuthor().getIdLong());
        Document d = (Document)ga.getDoc().get("player");

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

        ga.updateValue("player.gold", userMoney);
        ga.updateValue("player.items", items);

        event.getChannel().sendMessage("You successfully bought a " +itemToBuy.getName()).queue();

    }

    @Override
    public String getHelp() {
        return "buy an item";
    }
}
