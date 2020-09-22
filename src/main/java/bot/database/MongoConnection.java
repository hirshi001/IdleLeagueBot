package bot.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Map;

public class MongoConnection {

    public static MongoClient client;
    public static MongoDatabase db;

    public static void setup(String name, String password, String dbname){
        client = MongoClients.create(new ConnectionString("mongodb+srv://idleleague:"+password+"@"+name+".wsx3r.mongodb.net/"+dbname+"?retryWrites=true&w=majority"));
        db = client.getDatabase("idleleague");
    }


    public static MongoDatabase getDatabase(){
        return db;
    }

    public static MongoCollection<Document> getOneVOneBotCollection(){
        return db.getCollection("onevonebotdata");
    }

}
