package bot;


import bot.database.MongoConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.swing.SwingUtilities;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;


public class Main{

    static String token = "NzA1NTQ0NDA3MTU5NjY4ODQ2.XqtPkQ.5jrd05vt1t_TAA84y6ZOUHotPgo";
    public static void main(String[] args) throws Exception{
       // System.out.println(Calculator.calculate("(2+10)"));


        if(args.length>0){
            token = args[0];
        }

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
