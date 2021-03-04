package bot;

import bot.database.MongoConnection;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.util.Scanner;


public class Main{

    public static void main(String[] args) throws Exception {

        java.util.Set<Double> set = new java.util.TreeSet<Double>();
        for(int i=0;i<100;i++){
            set.add(Math.random());
        }

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


        try {
            new Bot(token);
        } catch (LoginException le){
            System.err.println("----LOGIN ERROR----");
            le.printStackTrace();
        }
    }

}
