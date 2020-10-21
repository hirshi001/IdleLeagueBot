package bot.commands.commandutil;

import java.util.LinkedList;
import java.util.List;

public class Arguments {

    public static final Arguments DEFAULT_ARGUMENTS = new Arguments();

    private List<Argument> args = new LinkedList<>();
    private String finalString = "";

    public Arguments(){

    }

    public Arguments addArgument(String argumentName, boolean optional){
        args.add(new Argument(argumentName, optional));
        return this;
    }

    public void update(){
        StringBuilder sb = new StringBuilder();
        for(Argument arg : args){
            if(arg.isOptional()) sb.append("<").append(arg.getName()).append(">");
            else sb.append("[").append(arg.getName()).append("]");
        }
        finalString = sb.toString();
    }

    @Override
    public String toString() {
        return finalString;
    }
}

class Argument{

    private String name;
    private boolean optional;

    public Argument(String name, boolean optional) {
        this.name = name;
        this.optional = optional;
    }

    public String getName() {
        return name;
    }

    public boolean isOptional() {
        return optional;
    }
}
