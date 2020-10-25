package bot.commands.commandutil;

import bot.database.MongoConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;
import static com.mongodb.client.model.Updates.set;

public class CommandManager extends ListenerAdapter {

    CommandEntryComparator ceComparator = new CommandEntryComparator();
    Map<String, CommandEntry> aliases = new HashMap<>();
    CommandEntry defaultCommand;


    public String prefix;
    public JDA jda;

    public CommandManager(JDA jda){
        this.jda = jda;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if(event.isWebhookMessage() || event.getAuthor().isBot() || event.getAuthor().isFake()) return;
        long id = event.getAuthor().getIdLong();
        if(id==event.getJDA().getSelfUser().getIdLong()) return;

        String message = event.getMessage().getContentRaw();

        String[] messageSplit = message.split(" ",3);
        if(messageSplit.length<=0) return;
        else if(!messageSplit[0].equalsIgnoreCase(prefix)) return;
        else if(messageSplit.length==1){
            if(defaultCommand!=null){
                //defaultCommand.command.commandCalled("","", event, this);
                return;
            }
        }
        else {
            String name = messageSplit[1].toLowerCase();
            String msg = messageSplit.length > 2 ? messageSplit[2] : "";
            CommandEntry ce = aliases.getOrDefault(name, defaultCommand);
            MongoDatabase db = MongoConnection.getDatabase();
            if (ce.command.requiresAccount()) {
                Document userDoc = db.getCollection("users").find((eq(id))).first();
                if (userDoc == null) {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setColor(Color.blue);
                    eb.setTitle("Whoops, you pulled an uh oh");
                    eb.addField(ce.command.accountRequireTitle(), "To create an account, type " + prefix + " createaccount", false);
                    event.getChannel().sendMessage(eb.build()).queue();
                    return;
                }
            }

            if (ce.command.requiredInGame()) {
                Document userDoc = MongoConnection.getOneVOneBotCollection().find((eq(id))).first();
                if (!userDoc.getBoolean("ingame")) {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setColor(Color.blue);
                    eb.setTitle("Whoops, you pulled an uh oh");
                    eb.addField(ce.command.inGameRequireTitle(), "To start a game, type " + prefix + " onevonebot", false);
                    event.getChannel().sendMessage(eb.build()).queue();
                    return;
                }
            }

            if (ce.command.requiresLiving()) {
                MongoCollection<Document> usersingame = MongoConnection.getOneVOneBotCollection();
                Document userDoc = usersingame.find((eq(id))).first().get("player",Document.class);
                if (userDoc.getBoolean("isdead")) {
                    long revivetime = userDoc.getLong("revivetime");
                    long time = System.currentTimeMillis();
                    if (revivetime > time) {
                        usersingame.updateOne(eq(id), set("isdead", false));
                    }
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setColor(Color.blue);
                    eb.setTitle("Whoops, you pulled an uh oh");
                    eb.addField(ce.command.requiresLivingTitle(), "Wait " + userDoc.getInteger("deathlength"), false);
                    event.getChannel().sendMessage(eb.build()).queue();
                    return;
                }
            }
            ce.command.commandCalled(name, msg, event, this);
            return;
        }
    }

    public CommandEntry addCommand(ICommand command, String... names){
        CommandEntry ce = new CommandEntry(names, command, getPrefix());
        for(String name:names) aliases.put(name,ce);
        jda.addEventListener(command);
        return ce;
    }

    public void setDefaultCommand(ICommand command){
        defaultCommand = new CommandEntry(new String[]{""}, command, getPrefix());
    }

    public ICommand getDefaultCommand(){
        return defaultCommand.command;
    }

    public Map<String, CommandEntry> getCommandsMap(){
        return aliases;
    }

    public void setPrefix(String prefix){
        this.prefix = prefix;
    }

    public String getPrefix(){
        return prefix;
    }
}

