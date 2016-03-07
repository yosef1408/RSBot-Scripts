package fighter;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt6.*;

public class Antiban extends Task<ClientContext> {

    public Antiban(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return Fighter.killed > 1;
    }

    @Override
    public void execute() {
        Random rnd = new Random();
        int random = rnd.nextInt(0,1000);
        if (random == 1) {
            System.out.println("Anti-Ban Started Pausing For 10 Seconds");
            Fighter.displayStatus = ("Anti-Ban");
            Condition.sleep(rnd.nextInt(10125,15015));
        }
    }
}