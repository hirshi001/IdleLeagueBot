package bot;


import bot.database.MongoConnection;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

import static com.mongodb.client.model.Filters.eq;


public class Main{

    public static void main(String[] args) throws Exception {



        String path = Main.class.getClassLoader().getResource("bot.secrets").getPath().replace("%20"," ");
        File f = new File(path);
        String token, dbName, dbPassword;
        if(f.exists()) {
            Scanner scanner = new Scanner(new FileInputStream(f));
            token = scanner.nextLine();
            dbName = scanner.nextLine();
            dbPassword = scanner.nextLine();
        }
        else {
            dbName = System.getenv("DB_NAME");
            dbPassword = System.getenv("DB_PASSWORD");
            token = System.getenv("BOT_TOKEN");
        }
        MongoConnection.setup(dbName, dbPassword, "idleleague");
        //System.out.println(token);



        new Bot(token);
    }

}
