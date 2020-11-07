package bot.commands.admincommands;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandManager;
import bot.database.MongoConnection;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import java.util.List;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.eq;

public class BanCommand extends Command {

    @Override
    public String getHelp() {
        return "Bans a user by their id or tag";
    }

    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {
        if(msg.isEmpty()){
            event.getChannel().sendMessage("Please include the snowflake id or tag of the user you want to ban").queue();
            return;
        }
        String reason;

        String[] args = event.getMessage().getContentRaw().split(" ",4);
        if(args.length<4){
            reason="";
        }
        else {
            reason=args[3];
        }


        if(args[2].length()>3 && args[2].startsWith("<@!")&& args[2].charAt(args[2].length()-1)=='>'){
            String id = args[2].substring(3,args[2].length()-1);
            try {
                System.out.println(id);
                Long longId = Long.parseLong(id);
                ban(longId, reason, event);
            } catch(NumberFormatException nfe){
                event.getChannel().sendMessage("Invalid tag id").queue();
            }
            return;
        }

        try{
            Long id = Long.parseLong(args[2]);
            ban(id, reason, event);
            return;
        } catch (NumberFormatException nfe) {
            List<User> usersToBan = event.getJDA().getUsersByName(args[2], true);
            if(usersToBan.size()!=1){
                event.getChannel().sendMessage("The bot cannot find the user, "+msg+", by name").queue();
                return;
            }
            ban(usersToBan.get(0).getIdLong(), reason, event);
        }
    }

    private void ban(long id, String reason, GuildMessageReceivedEvent event){
        MongoCollection<Document> bannedUsers  =MongoConnection.getDatabase().getCollection("banned");
        try{
            bannedUsers.insertOne(new Document("_id", id).append("reason", (reason.isEmpty()?"No reason provided":reason) ));
            event.getChannel().sendMessage("User with id "+id+" has been succesfully banned").queue();
            return;
        } catch (MongoWriteException mwe){
            if(mwe.getError().getMessage().contains("duplicate key error collection")){
                event.getChannel().sendMessage("User is already banned").queue();
            }
        }
    }
}
