package bot.commands.admincommands.runtimecode;

import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandManager;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.PushbackInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class RunJavaCodeCommand extends Command {


    @Override
    public String getHelp() {
        return "runs java code";
    }

    @Override
    public void commandCalled(String name, String msg, GuildMessageReceivedEvent event, CommandManager commandManager) {
        msg = msg.trim();
        if(msg.startsWith("```") && msg.endsWith("```")){
            msg = msg.substring(3, msg.length()-3);
            if(msg.startsWith("java")){
                msg = msg.substring(4);
            }
        }
        String output =
                "package customCode;\n"+
                "public class Main{\n" +
                "\tpublic static void main(String[] args){\n\t\t" +
                    msg+"\n" +
                "\t}\n" +
                "\tpublic static void flushStream(){System.out.print('\\ue000');System.out.flush();}\n"+
                "}";


        try {
            File f = new File("customCode/Main.java");
            if(!f.exists()) f.createNewFile();
            PrintWriter pw = new PrintWriter(new FileOutputStream(f, false));
            pw.print(output);
            pw.flush();
            pw.close();
            runCommand(event.getChannel());




        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runCommand(TextChannel channel) {

        try {
            new ProcessBuilder("javac", "customCode/Main.java").start().waitFor();
        } catch(IOException | InterruptedException ioe){
            channel.sendMessage("A compile time error occured when running your code" + ioe.toString()).queue();
            return;
        }
        AtomicInteger prints = new AtomicInteger(0);
        AtomicBoolean shouldSend = new AtomicBoolean(false);
        try {
            Process p = new ProcessBuilder("java", "-Djava.security.manager", "customCode/Main").start();

            new Thread(()->{
                for(int i=0;i<25;i++) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    prints.getAndAdd(-1);
                    if(prints.get()<0){
                        prints.set(0);
                    }
                }
                p.destroyForcibly();
            }).start();


            StringBuffer buffer = new StringBuffer();
            InputStreamReader reader = new InputStreamReader(p.getInputStream());
            while(true) {
                int value = reader.read();
                if(value==-1) break;
                if(value=='\ue000'){
                    shouldSend.set(true);
                    /*
                    if (prints.get() < 5) {
                        String msg = buffer.toString();
                        if(msg.length()>1000){
                            int idx = msg.lastIndexOf("\n",1000);
                            if(idx==-1) idx = 1000;
                            else if(idx!=0)  channel.sendMessage(msg.substring(0,idx)).queue();
                            buffer = new StringBuffer(msg.substring(idx));

                        }else{
                            channel.sendMessage(buffer.toString()).queue();
                            buffer = new StringBuffer();
                        }
                        prints.incrementAndGet();
                    }else{
                    }

                     */
                }else{
                    buffer.append((char)value);
                }
                while(shouldSend.get() && prints.get()<5){
                    String msg = buffer.toString();
                    int idx = msg.lastIndexOf("\n",1000);
                    if(idx==-1) idx = 1000;
                    if(idx!=0) channel.sendMessage(msg.substring(0,idx)).queue();
                    buffer = new StringBuffer(msg.substring(idx));
                    if(buffer.length()==0) shouldSend.set(false);
                    prints.incrementAndGet();
                }
            }
            while(shouldSend.get()){
                String msg = buffer.toString();
                int idx = msg.lastIndexOf("\n",1000);
                if(idx==-1) idx = 1000;
                if(idx!=0) channel.sendMessage(msg.substring(0,idx)).queue();
                buffer = new StringBuffer(msg.substring(idx));
                if(buffer.length()==0) shouldSend.set(false);
                prints.incrementAndGet();
                try{
                    Thread.sleep(200);
                }catch (InterruptedException e){}
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

