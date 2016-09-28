package manbearpigcat.scripts;

/**
 * Created by Shan on 2016-08-17.
 */
public class Stats {
    public int potFarmed = 0;
    public String state = "Waiting...";
    public int bag = 0;
    public int bank = 0; //1 = varrock, 2 = al-kharid

    public Stats(){
        this.potFarmed = 0;
        this.state = "Waiting...";
        this.bag = 0;
        this.bank = 0;
    }

    public Stats(int pot){
        this.potFarmed = pot;
        this.state = "Waiting...";
        this.bag = 0;
        this.bank = 0;
    }

    public int getBank(){
        return bank;
    }

    public void setBank(int bank){
        this.bank = bank;
    }
    public String getState(){
        return state;
    }

    public void setState(String state){
        this.state = state;
    }

    public int getPotFarmed(){
        return potFarmed;
    }

    public int getBag(){
        return bag;
    }

    public void setBag(int b){
        this.bag = b;
    }

    public void setPotFarmed(int pot){
        this.potFarmed = pot;
    }

    public void addPot(){
        this.potFarmed = potFarmed + 1;
    }
}
