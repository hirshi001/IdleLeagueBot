package bot.commands.admincommands;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandManager;
import bot.database.MongoConnection;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class UnbanCommand extends Command {

    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {
        if(msg.isEmpty()){
            event.getChannel().sendMessage("Please include the snowflake id or tag of the user you want to unban").queue();
            return;
        }
        String[] args = event.getMessage().getContentRaw().split(" ",3);


        if(args[2].length()>3 && args[2].startsWith("<@!")&& args[2].charAt(args[2].length()-1)=='>'){
            String id = args[2].substring(3,args[2].length()-1);
            try {
                System.out.println(id);
                Long longId = Long.parseLong(id);
                unban(longId, event);
            } catch(NumberFormatException nfe){
                event.getChannel().sendMessage("Invalid tag id").queue();
            }
            return;
        }

        try{
            Long id = Long.parseLong(args[2]);
            unban(id, event);
            return;
        } catch (NumberFormatException nfe) {
            List<User> usersToBan = event.getJDA().getUsersByName(args[2], true);
            if(usersToBan.size()!=1){
                event.getChannel().sendMessage("The bot cannot find the user, "+msg+", by name").queue();
                return;
            }
            unban(usersToBan.get(0).getIdLong(), event);
        }
    }

    private void unban(long id, GuildMessageReceivedEvent event){
        MongoCollection<Document> bannedUsers  =MongoConnection.getDatabase().getCollection("banned");
        try{
            DeleteResult dr = bannedUsers.deleteOne(eq(id));
            if(dr.wasAcknowledged() && dr.getDeletedCount()!=0){
                event.getChannel().sendMessage("User with id "+id+" has been unbanned").queue();;
            }
            else{
                event.getChannel().sendMessage("The user was not unbanned. Probably because he wasn't banned in the first place").queue();
            }
            return;
        } catch (Exception e){
            event.getChannel().sendMessage("An error occured while trying to unban the user").queue();
        }
    }

    @Override
    public String getHelp() {
        return "unbans a user";
    }
}
