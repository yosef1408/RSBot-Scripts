package thebonobo.SalmonCollector.utils;

import org.powerbot.script.Random;
import org.powerbot.script.rt4.Bank;
import org.powerbot.script.rt4.ClientContext;

import java.io.*;
import java.util.*;

public class UserProfile{
    private Properties userProperties = null;
    private ClientContext ctx;

    public UserProfile(ClientContext ctx) {
        this.ctx = ctx;
        String fileLocation = ctx.controller.script().getStorageDirectory() + File.separator + "thebonobo.properties";
        File settingsFile = new File(fileLocation);
        if (settingsFile.exists() && !settingsFile.isDirectory()){
            userProperties = new Properties();
            try {
                InputStream in = new FileInputStream(fileLocation);
                userProperties.load(in);
            } catch (IOException ex){
                ex.printStackTrace();
            }
        } else {
            try {
                generateRandomProfile(fileLocation);
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    public void generateRandomProfile(String fileLocation) throws IOException{
        Properties prop = new Properties();

        prop.setProperty("click-speed", Integer.toString(Random.nextInt(0,100)));
        prop.setProperty("camera-turn-rate", Integer.toString(Random.nextInt(0,100)));
        prop.setProperty("double-click", Integer.toString(Random.nextInt(0,2)));
        prop.setProperty("right-click-bank", Integer.toString(Random.nextInt(0,2)));
        prop.setProperty("deposit-inventory", Integer.toString(Random.nextInt(0,2)));
        prop.setProperty("deposit-amount",getBankAmount());
        prop.setProperty("right-click-player-rate", Integer.toString(Random.nextInt(0,10)));

        userProperties = prop;

        OutputStream out = new FileOutputStream(fileLocation);
        prop.store(out, null);

    }

    public Properties getUserProperties() {
        return userProperties;
    }

    public String getBankAmount(){
        List<Bank.Amount> bankAmount = Arrays.asList(Bank.Amount.values()); //Bank.Amount.values()[Random.nextInt(0,Bank.Amount.values().length)].name())
        Collections.shuffle(bankAmount);
        Iterator iter = bankAmount.iterator();
        String amount = ((Bank.Amount) iter.next()).name();

        if (amount.contains("BUT_ONE"))
            return  ((Bank.Amount) iter.next()).name();
        else
            return amount;
    }
}
