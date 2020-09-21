package bot.commands.normalcommands;

public class Roasts {

    public static String[] roasts = new String[]{
            "Are you okay kid?", "You good fool?", "FOOLIGAN!", "idiot!",
            "What are you? An idiot sandwich.", "Dumbo.", "Where did your brain go?", "Gimme your money foo",
            "Can you stop being an idiot?", "hirshi001 is a god. You are a fool.", "Do you even know what your doing?",
            "You sound stupid", "Stop calling me", "Your annoying me", "Please stop. Get some help",
            "You need to be punished", "I DARE YOU, DO IT AGAIN.", "kid...", "You are Trump's son",
            "Obama's left nut is better looking than you.", "PLEASE STOP BEING DUMB",
            "This is why you don't drop out of school kids...","Uh Oh Stinky", "You probably watch Ezreal and Taric...",
            "Your someone who thinks Teemo is a good champion", "You probably play Yasuo", "You probably play Teemo",
            "Lemme guess, Yasuo Matery 7 1 million pts with zero kills...", "Probably bronze or iron...",
            "You will never make it out of iron", "You will never get past bronze", "You deserve to be flamed",
            "I need to mute you", "I should mute you", "You probably feed every game", "noob"
    };

    public static String getRandomRoast(){
        return roasts[(int)(Math.random()*(Roasts.roasts.length-1))];
    }

}
