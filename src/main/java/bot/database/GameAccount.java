package bot.database;

import bot.commands.normalcommands.gamecommands.lolcommand.OneVOneBotCommand;
import bot.database.MongoConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.Arrays;
import java.util.Collection;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;
import static com.mongodb.client.model.Updates.unset;

public class GameAccount {

    public static Document getOneVOne(long id){
        return get(MongoConnection.getOneVOneBotCollection(),id);
    }

    public static Document getUser(long id){
        return get(MongoConnection.getUsersCollection(),id);
    }

    public static Document get(MongoCollection<Document> coll,  long id){
        return coll.find(eq("_id",id)).first();
    }

    private final long id;
    private Document doc;

    public GameAccount(long id){
        this.id = id;
    }

    public long getId(){return id;}

    public <T> void updateValue(String value, T newValue){
       MongoConnection.getOneVOneBotCollection().findOneAndUpdate(eq(id), set(value, newValue));
    }
    public Document getDoc(){
        if(doc==null) return regenerateDoc();
        return doc;
    }

    public void create(){
        doc = OneVOneBotCommand.defaultGameDoc(id);
        MongoConnection.getOneVOneBotCollection().insertOne(doc);
    }

    public Document regenerateDoc(){
        return doc = GameAccount.getOneVOne(id);
    }

    public void setInGame(Document playerDoc, Document botDoc){
        MongoConnection.getOneVOneBotCollection().findOneAndUpdate(eq(id), Updates.combine(set("ingame",true), set("player", playerDoc), set("bot",botDoc)));
    }

    public void setNotInGame(){
        MongoConnection.getOneVOneBotCollection().findOneAndUpdate(eq(id), set("ingame",false));
    }

}
