package bot;

import bot.commands.admincommands.BanCommand;
import bot.commands.admincommands.LinkBotStatusCommand;
import bot.commands.admincommands.SayCommand;
import bot.commands.admincommands.SendAnnouncementCommand;
import bot.commands.admincommands.StopBotCommand;
import bot.commands.admincommands.UnbanCommand;
import bot.commands.admincommands.UnlinkBotStatusCommand;
import bot.commands.commandutil.AdminCommandManager;
import bot.commands.commandutil.NormalCommandManager;
import bot.commands.normalcommands.InviteLink;
import bot.commands.normalcommands.items.BuyItemCommand;
import bot.commands.normalcommands.items.SellItemCommand;
import bot.commands.normalcommands.items.ShowItemsCommand;
import bot.commands.normalcommands.lolcommand.LolDisableCommand;
import bot.commands.normalcommands.lolcommand.LolEnableCommand;
import bot.commands.normalcommands.jungling.BaronCommand;
import bot.commands.normalcommands.HelpCommand;
import bot.commands.normalcommands.ResetCommand;
import bot.commands.commandutil.CommandManager;
import bot.commands.normalcommands.lolcommand.DefaultCommand;
import bot.commands.normalcommands.jungling.JungleCommand;
import bot.commands.normalcommands.InGameProfileCommand;
import bot.commands.normalcommands.CreateAccountCommand;
import bot.commands.normalcommands.lolcommand.OneVOneBotCommand;
import bot.gameutil.champions.champion.ChampionRegistry;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;


public class Bot extends ListenerAdapter{

    private JDA jda;
    private String name;

    private CommandManager manager;
    public Bot(String token) throws LoginException, InterruptedException {
        System.out.println("Registering Champs");
        ChampionRegistry.registerAllChampions();
        System.out.println("Champs finished registering");

        System.out.println("Connecting Bot");
        jda = new JDABuilder(AccountType.BOT).setToken(token).build().awaitReady();
        jda.getPresence().setActivity(Activity.playing("lol help"));
        System.out.println("Bot connected");


        LinkBotStatusCommand.forEachLinked(jda, textChannel -> textChannel.sendMessage("Bot is connected").queue());

        name = jda.getSelfUser().getName().toLowerCase();
        manager = new NormalCommandManager(jda);
        manager.setPrefix("lol");
        manager.setDefaultCommand(new DefaultCommand());
        manager.addCommand( new CreateAccountCommand(),"createaccount");
        manager.addCommand(new OneVOneBotCommand(jda),"onevonebot");
        manager.addCommand(new InGameProfileCommand(manager), "gameprofile", "gamep", "gp");
        manager.addCommand( new ResetCommand(),"reset");
        manager.addCommand( new JungleCommand(),"jungle", "jg");
        manager.addCommand( new HelpCommand(),"help", "h");
        manager.addCommand(new BaronCommand(jda), "baron");

        manager.addCommand( new LolEnableCommand(), "enablelol");
        manager.addCommand( new LolDisableCommand(),"disablelol");

        manager.addCommand( new BuyItemCommand(),"buy");
        manager.addCommand( new ShowItemsCommand(), "items");
        manager.addCommand( new SellItemCommand(), "sell");

        manager.addCommand(new InviteLink(), "invitelink");

        //manager.addCommand("help");
        jda.addEventListener(manager);

        CommandManager adminCommands = new AdminCommandManager(jda);
        adminCommands.setPrefix("adminlol");
        adminCommands.setDefaultCommand(new DefaultCommand());
        adminCommands.addCommand( new BanCommand(),"ban");
        adminCommands.addCommand(new UnbanCommand(),"unban");
        adminCommands.addCommand(new SayCommand(),"say");
        adminCommands.addCommand( new HelpCommand(),"help");
        adminCommands.addCommand(new StopBotCommand(), "stopbot");
        adminCommands.addCommand(new LinkBotStatusCommand(), "link");
        adminCommands.addCommand(new UnlinkBotStatusCommand(), "unlink");
        adminCommands.addCommand(new SendAnnouncementCommand(), "sendannouncement");
        jda.addEventListener(adminCommands);

        jda.addEventListener(this);


    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        //System.out.println(event.getMessage().getEmbeds().get(0).getFields().get(0).getName());
        if(event.getMessage().getContentRaw().toLowerCase().equals(name)){
            event.getChannel().sendMessage(":person_facepalming:the prefix for this bot is "+ manager.getPrefix()).queue();
        }
    }


    @Override
    public void onDisconnect(@Nonnull DisconnectEvent event) {
        for(TextChannel c:LinkBotStatusCommand.getLinkedChannels(event.getJDA())){
            c.sendMessage("Bot is disconnected").queue();
        }
    }

    @Override
    public void onReconnect(@Nonnull ReconnectedEvent event) {
        for(TextChannel c:LinkBotStatusCommand.getLinkedChannels(event.getJDA())){
            c.sendMessage("Bot is reconnected").queue();
        }
    }
}
