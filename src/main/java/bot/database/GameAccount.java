package bot.database;

import bot.database.MongoConnection;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class GameAccount {


    public static Document getOneVOne(long id){
        return get(MongoConnection.getOneVOneBotCollection(),id);
    }

    public static Document getUser(long id){
        return get(MongoConnection.getUsersCollection(),id);
    }

    public static Document getCooldown(long id){
        return get(MongoConnection.getCooldownsCollection(), id);
    }

    public static Document get(MongoCollection<Document> coll,  long id){
        return coll.find(eq("_id",id)).first();
    }
}
