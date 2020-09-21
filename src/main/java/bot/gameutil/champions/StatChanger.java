package bot.gameutil.champions;

public class StatChanger {

    public StatChanger(){

    }

    private int atk = 0;
    private double atkScale = 0 ;

    private int ap = 0;
    private double apScale = 0;

    public StatChanger atk(int amt){
        atk = amt;
        return this;
    }

    public StatChanger atkScale(double amt){
        atkScale = amt;
        return this;
    }

    public StatChanger ap(int amt){
        ap = amt;
        return this;
    }

    public StatChanger apScale(double amt){
        apScale = amt;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(atk!=0){
            sb.append("`atk: +").append(atk).append("` ");
        }
        if(ap!=0){
            sb.append("`ap: +").append(ap).append("` ");
        }

        if(atkScale!=0){
            sb.append("`atk scale: ").append(atkScale).append("` ");
        }

        if(apScale!=0){
            sb.append("`ap scale: ").append(apScale).append("` ");
        }

        return sb.toString();
    }
}
