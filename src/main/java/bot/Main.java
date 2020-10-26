package bot;


import bot.database.MongoConnection;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

import static com.mongodb.client.model.Filters.eq;


public class Main{

    public static void main(String[] args) throws Exception {


        /*
        String path = Main.class.getClassLoader().getResource("bot.secrets").getPath().replace("%20"," ");
        File f = new File(path);
        System.out.println(path);
        Scanner scanner = new Scanner(new FileInputStream(f));
        String token = scanner.nextLine();
         */

        String dbName = System.getenv("DB_NAME");
        String dbPassword = System.getenv("DB_PASSWORD");
        MongoConnection.setup(dbName, dbPassword, "idleleague");

        String token = System.getenv("BOT_TOKEN");
        System.out.println(token);



        new Bot(token);
    }

}
