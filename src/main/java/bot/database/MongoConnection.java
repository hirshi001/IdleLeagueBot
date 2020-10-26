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

    private static MongoClient CLIENT;
    private static MongoDatabase DATABASE;
    private static MongoCollection<Document> ONE_V_ONE_BOT_DATA_COLLECTION;
    private static MongoCollection<Document> USER_COLLECTION;
    private static MongoCollection<Document> BANNED;
    private static MongoCollection<Document> ADMINS;
    private static MongoCollection<Document> CHANNEL_COMMANDS;
    private static MongoCollection<Document> CHANNEL_LINK;

    public static void setup(String name, String password, String dbname){
        CLIENT = MongoClients.create("mongodb+srv://idleleague:"+password+"@"+name+".wsx3r.mongodb.net/"+dbname+"?retryWrites=true&w=majority");
        DATABASE = CLIENT.getDatabase("idleleague");

        ONE_V_ONE_BOT_DATA_COLLECTION = DATABASE.getCollection("onevonebotdata");
        USER_COLLECTION = DATABASE.getCollection("users");
        BANNED = DATABASE.getCollection("banned");
        ADMINS = DATABASE.getCollection("admins");
        CHANNEL_COMMANDS = DATABASE.getCollection("channelcommands");
        CHANNEL_LINK = DATABASE.getCollection("channellink");
    }


    public static MongoDatabase getDatabase(){
        return DATABASE;
    }

    public static MongoCollection<Document> getOneVOneBotCollection(){
        return ONE_V_ONE_BOT_DATA_COLLECTION;
    }

    public static MongoCollection<Document> getUsersCollection(){
        return USER_COLLECTION;
    }

    public static MongoCollection<Document> getBannedCollection(){
        return BANNED;
    }

    public static MongoCollection<Document> getAdminCollection(){
        return ADMINS;
    }

    public static MongoCollection<Document> getChannelCommandsCollection(){ return CHANNEL_COMMANDS; }

    public static MongoCollection<Document> getChannelLinkCollection(){ return CHANNEL_LINK; }

    public static void close(){
        CLIENT.close();
    }


}
