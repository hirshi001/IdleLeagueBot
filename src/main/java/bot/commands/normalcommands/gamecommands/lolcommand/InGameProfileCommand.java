package bot.commands.normalcommands.gamecommands.lolcommand;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandManager;
import bot.database.MongoConnection;
import bot.gameutil.champions.champion.Champion;
import bot.gameutil.champions.champion.ChampionRegistry;
import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import java.awt.Color;

import static com.mongodb.client.model.Filters.eq;

public class InGameProfileCommand extends Command {

    private CommandManager manager;

    public InGameProfileCommand(CommandManager manager){
        this.manager = manager;
    }

    @Override
    public String getHelp() {
        return "profile";
    }

    @Override
    public boolean requiredInGame() {
        return true;
    }

    @Override
    public String inGameRequireTitle() {
        return "You must be in a game to use check your in game profile";
    }

    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {
        MongoCollection<Document> collection= MongoConnection.getOneVOneBotCollection();
        Long id = event.getAuthor().getIdLong();
        Document d = collection.find(eq(id)).first();

        Document playerDoc = d.get("player", Document.class);
        Champion c = ChampionRegistry.getChampion(playerDoc.getLong("champion"));

        EmbedBuilder profile = new EmbedBuilder();
        profile.setThumbnail(event.getAuthor().getEffectiveAvatarUrl());
        if(c!=null) {
            String key = c.getKey();
            profile.setImage("http://ddragon.leagueoflegends.com/cdn/img/champion/splash/" + key + "_0.jpg");
        }
        profile.setColor(Color.blue);
        profile.setTitle(playerDoc.getString("name"));
        if(c!=null) {
            String champName = c.getName();
            profile.addField("Champion", champName, true);
        } else{
            profile.addField("Champion", "None", true);
        }
        profile.addField("Level", playerDoc.getInteger("level").toString(), true);
        profile.addBlankField(false);
        profile.addField("Experience", playerDoc.getInteger("experience").toString(),true);
        profile.addField("Gold", playerDoc.getInteger("gold").toString(),true);

        event.getChannel().sendMessage(profile.build()).queue();





    }
}
