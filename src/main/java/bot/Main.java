package bot;


import bot.database.MongoConnection;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

import static com.mongodb.client.model.Filters.eq;


public class Main{

    static String token;
    public static void main(String[] args) throws Exception{
    /*
        String path = Main.class.getClassLoader().getResource("bot.secrets").getPath().replace("%20"," ");
        File f = new File(path);
        System.out.println(path);
        Scanner scanner = new Scanner(new FileInputStream(f));
        token = scanner.nextLine();

        MongoConnection.setup(scanner.nextLine(), scanner.nextLine(), "idleleague");

        System.out.println(token);


     */
        new Thread(){
            @Override
            public void run() {
                try {
                    new Bot(token);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
