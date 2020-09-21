package bot;


import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

import static com.mongodb.client.model.Filters.eq;


public class Main{

    static String token;
    public static void main(String[] args) throws Exception{
       // System.out.println(Calculator.calculate("(2+10)"));

        String path = Main.class.getClassLoader().getResource("bot.secrets").getPath().replace("%20"," ");
        File f = new File(path);
        Scanner scanner = new Scanner(new FileInputStream(f));
        token = scanner.nextLine();
        System.out.println(token);

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
