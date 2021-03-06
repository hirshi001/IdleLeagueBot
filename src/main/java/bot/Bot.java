package bot;

import bot.commands.admincommands.BanCommand;
import bot.commands.admincommands.ClearMessageCommand;
import bot.commands.admincommands.GetRamCommand;
import bot.commands.admincommands.ResetOneVOneGames;
import bot.commands.admincommands.runtimecode.RunJavaCodeCommand;
import bot.commands.commandutil.Command;
import bot.commands.commandutil.CommandEntry;
import bot.commands.normalcommands.other.DiscordCommand;
import bot.commands.normalcommands.link.LinkBotStatusCommand;
import bot.commands.admincommands.SayCommand;
import bot.commands.admincommands.SendAnnouncementCommand;
import bot.commands.admincommands.StopBotCommand;
import bot.commands.admincommands.UnbanCommand;
import bot.commands.normalcommands.link.UnlinkBotStatusCommand;
import bot.commands.commandutil.AdminCommandManager;
import bot.commands.commandutil.NormalCommandManager;
import bot.commands.normalcommands.other.InviteLink;
import bot.commands.normalcommands.gamecommands.items.BuyItemCommand;
import bot.commands.normalcommands.gamecommands.items.SellItemCommand;
import bot.commands.normalcommands.gamecommands.items.ShowItemsCommand;
import bot.commands.normalcommands.gamecommands.location.GetLocationCommand;
import bot.commands.normalcommands.gamecommands.lolcommand.LolDisableCommand;
import bot.commands.normalcommands.gamecommands.lolcommand.LolEnableCommand;
import bot.commands.normalcommands.gamecommands.jungling.BaronCommand;
import bot.commands.normalcommands.gamecommands.lolcommand.TutorialCommand;
import bot.commands.normalcommands.help.HelpCommand;
import bot.commands.normalcommands.gamecommands.lolcommand.ResetCommand;
import bot.commands.commandutil.CommandManager;
import bot.commands.normalcommands.gamecommands.lolcommand.DefaultCommand;
import bot.commands.normalcommands.gamecommands.jungling.JungleCommand;
import bot.commands.normalcommands.gamecommands.lolcommand.InGameProfileCommand;
import bot.commands.normalcommands.gamecommands.lolcommand.CreateAccountCommand;
import bot.commands.normalcommands.gamecommands.lolcommand.OneVOneBotCommand;
import bot.commands.normalcommands.help.helpsection.DefaultHelpSection;
import bot.commands.normalcommands.help.helpsection.HelpSection;
import bot.database.MongoConnection;
import bot.gameutil.champions.champion.ChampionRegistry;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;


public class Bot extends ListenerAdapter{

    private JDA jda;
    private String name;

    private CommandManager manager;
    public Bot(String token) throws LoginException, InterruptedException {
        ChampionRegistry.registerAllChampions();
        jda = JDABuilder.createDefault(token).build().awaitReady();
        LinkBotStatusCommand.cache(jda); //this method needs instance of jda to get the text channels

        jda.getPresence().setActivity(Activity.playing("lol help"));
        System.out.println("Bot connected");
        // \u000d for(int i=0;i<10;i++) System.out.println( i%2==0?i:10);
        //LinkBotStatusCommand.forEachLinked(jda, textChannel -> textChannel.sendMessage("Bot is connected").queue());


        name = jda.getSelfUser().getName().toLowerCase();
        manager = new NormalCommandManager(jda);
        manager.setPrefix("lol");
        manager.setDefaultCommand(new DefaultCommand());

        HelpSection gameCommands = new DefaultHelpSection("me commands");

        addCommand(manager, new CreateAccountCommand(), gameCommands, "createaccount");

        addCommand(manager, new OneVOneBotCommand(jda), gameCommands,"onevonebot");
        addCommand(manager, new InGameProfileCommand(manager), gameCommands, "gameprofile", "gamep", "gp");

        addCommand(manager, new ResetCommand(), gameCommands,"reset");
        addCommand(manager, new JungleCommand(), gameCommands,"jungle", "jg");
        addCommand(manager, new HelpCommand(), gameCommands,"help", "h");
        addCommand(manager, new BaronCommand(jda), gameCommands, "baron");

        addCommand(manager, new BuyItemCommand(), gameCommands,"buy");
        addCommand(manager, new ShowItemsCommand(), gameCommands, "items");
        addCommand(manager, new SellItemCommand(), gameCommands, "sell");

        addCommand(manager, new GetLocationCommand(), gameCommands, "location", "gamelocation", "gl");

        addCommand(manager, new TutorialCommand(), gameCommands, "tutorial");

        gameCommands.setEmbedBuilder();
        gameCommands.getEmbedBuilder().addField("For new users, to get started, type","`lol tutorial`", false);
        gameCommands.buildHelpPage();

        HelpSection moderatorCommands = new DefaultHelpSection("moderator commands");

        addCommand(manager, new LolEnableCommand(), moderatorCommands, "enablelol");
        addCommand(manager, new LolDisableCommand(), moderatorCommands,"disablelol");

        addCommand(manager, new LinkBotStatusCommand(), moderatorCommands, "link");
        addCommand(manager, new UnlinkBotStatusCommand(), moderatorCommands, "unlink");

        moderatorCommands.setEmbedBuilder();
        moderatorCommands.buildHelpPage();


        HelpSection otherCommands = new DefaultHelpSection("other commands");

        addCommand(manager, new InviteLink(), otherCommands,"invitelink");
        addCommand(manager, new DiscordCommand(), otherCommands,"discord");
        addCommand(manager, new RunJavaCodeCommand(), otherCommands, "runcode");

        HelpCommand hc = new HelpCommand();
        addCommand(manager, hc, otherCommands, "help");
        otherCommands.setEmbedBuilder();
        otherCommands.buildHelpPage();
        hc.createHelpPage(gameCommands, moderatorCommands, otherCommands);


        jda.addEventListener(manager);


        CommandManager adminCommands = new AdminCommandManager(jda);

        adminCommands.setPrefix("adminlol");
        adminCommands.setDefaultCommand(new DefaultCommand());

        HelpSection adminBan = new DefaultHelpSection("moderating players");
        addCommand(adminCommands, new BanCommand(), adminBan, "ban");
        addCommand(adminCommands, new UnbanCommand(),adminBan,"unban");
        adminBan.setEmbedBuilder();
        adminBan.buildHelpPage();

        HelpSection adminSpeakSection = new DefaultHelpSection("bot speaking");
        addCommand(adminCommands, new SayCommand(),adminSpeakSection,"say");
        addCommand(adminCommands, new SendAnnouncementCommand(), adminSpeakSection,"sendannouncement");
        adminSpeakSection.setEmbedBuilder();
        adminSpeakSection.buildHelpPage();

        HelpSection adminBotTechnicals = new DefaultHelpSection("bot technicals");
        addCommand(adminCommands, new StopBotCommand(), adminBotTechnicals,"stopbot");
        addCommand(adminCommands, new ResetOneVOneGames(), adminBotTechnicals,"resetgames");
        addCommand(adminCommands, new GetRamCommand(), adminBotTechnicals, "getram");
        adminBotTechnicals.setEmbedBuilder();
        adminBotTechnicals.buildHelpPage();

        HelpSection adminOtherSection = new DefaultHelpSection("other");
        HelpCommand adminHelpCommand = new HelpCommand();
        addCommand(adminCommands,adminHelpCommand,adminOtherSection,"help");
        addCommand(adminCommands, new ClearMessageCommand(), adminOtherSection, "clearmessage");
        adminOtherSection.setEmbedBuilder();
        adminOtherSection.buildHelpPage();

        adminHelpCommand.createHelpPage(adminBan, adminBotTechnicals, adminOtherSection);
        jda.addEventListener(adminCommands);

        jda.addEventListener(this);
        printNumberOfGuilds();
    }

    private void addCommand(CommandManager manager, Command command, HelpSection helpSection, String... names){
        CommandEntry ce = manager.addCommand(command, names);
        helpSection.addCommandEntry(ce);
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        super.onReady(event);
        event.getJDA().getGuildCache();
    }

    @Override
    public void onGuildLeave(@Nonnull GuildLeaveEvent event) {
        printNumberOfGuilds();
    }

    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent event) {
        printNumberOfGuilds();
    }

    private void printNumberOfGuilds(){
        System.out.println("The bot is in " + jda.getGuilds().size() + " servers.");
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        //System.out.println(event.getMessage().getEmbeds().get(0).getFields().get(0).getName());
        if(event.getMessage().getContentRaw().equalsIgnoreCase(name)){
            event.getChannel().sendMessage(":person_facepalming:the prefix for this bot is "+ manager.getPrefix()).queue();
        }
    }

    @Override
    public void onShutdown(@Nonnull ShutdownEvent event) {
        MongoConnection.close();
    }


}
