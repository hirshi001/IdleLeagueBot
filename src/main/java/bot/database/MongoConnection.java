package bot.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoConnection {

    public static MongoClient client = MongoClients.create();
    public static MongoDatabase db = client.getDatabase("idleleague");


    public static MongoDatabase getDatabase(){
        return db;
    }

    public static MongoCollection<Document> getOneVOneBotCollection(){
        return db.getCollection("onevonebotdata");
    }


}
