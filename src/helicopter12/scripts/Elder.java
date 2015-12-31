//Project: Elder Log Collector
//Date: 12/20/15
//Author: Ryan(Helicopter12)
package helicopter12.scripts;

import org.powerbot.script.*;
import org.powerbot.script.rt6.*;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import java.awt.*;
import org.powerbot.script.Random;

@Script.Manifest(name="Elder Log Collector", properties = "author=Helicopter12; topic=1296549; client=6;", description="An autonomous Elder tree chopping and banking system used for money making")
public class Elder extends PollingScript<ClientContext> implements PaintListener, MessageListener {
    private String status = "Starting...";
    private final int elderID = 87508;
    private final int  deadElderID = 87509, elderLogID = 29556;
    private int logsCollected = 0;
    private int elderPrice;
    private long vRespawn = 0, sRespawn = 0, eRespawn = 0, dRespawn = 0;
    private long vSpawnTime = 0, sSpawnTime = 0, eSpawnTime = 0, dSpawnTime = 0;
    private final Tile lodeStoneBankTile = new Tile(2899,3544,0);
    private final Tile bankTile = new Tile(2887,3536,0);
    private final String locationNames[] = { "Varrock:", "Seer's:", "E-Ville:", "E-Peak:", "Draynor1:", "Draynor2:" };
    private final Tile elderLocation[] = { new Tile(3260,3367,0), new Tile(2737,3407,0), new Tile(3094,3457,0), new Tile(2324,3598,0), new Tile(3059,3319,0), new Tile(3098,3218,0) };
    private final Tile lodeStoneTile[] = { new Tile(3214,3376,0), new Tile(2689,3482,0), new Tile(3067,3505,0), new Tile(2366,3479,0), new Tile(3105,3298,0), new Tile(3105,3298,0) };
    private final int lodeStoneComponentID[] = { 21, 19, 15, 24, 14, 14 };
    private final Tile lFixTile = new Tile(3233,3221,0);
    private final int homeWidgetID = 1465, homeComponentID = 56, lodeStoneWidgetID = 1092, bankComponentID = 12;
    private final int hatchetIDs[] = { 1359, 1353, 1355, 1349, 1351, 1361, 1357, 6739 };
    private boolean depositAll = false;
    private int randomizedLocations[] = new int[4];
    private int step = 0;
    int i = 0;

    @Override
    public void start() {
        status = "Configuring...";

        //Check for starting materials (Hatchet) - EDIT: OBSOLETE WITH TOOLBELT, ALWAYS HAVE A HATCHET
        /*if(ctx.backpack.select().id(hatchetIDs).isEmpty()){
            if(ctx.equipment.select().id(hatchetIDs).isEmpty()) {
                status = "ERROR: No Hatchet Found";
            }
        }*/

        //Check for empty inventory
        if(shouldBank()){
            //Inventory full - go deposit everything
            depositAll = true;
            step = 6;
            status = "Inventory Full";
        }

        //Randomize the locations and patterns
        int rnd1 = Random.nextInt(0,6);
        int rnd2 = Random.nextInt(0,6);
        int rnd3 = Random.nextInt(0,6);
        int rnd4 = Random.nextInt(0,6);

        while(rnd2 == rnd1 || rnd2 == rnd3 || rnd2 == rnd4){
            rnd2 = Random.nextInt(0,6);
        }
        while(rnd3 == rnd1 || rnd3 == rnd2 || rnd3 == rnd4){
            rnd3 = Random.nextInt(0,6);
        }
        while(rnd4 == rnd1 || rnd4 == rnd3 || rnd4 == rnd2) {
            rnd4 = Random.nextInt(0,6);
        }
        randomizedLocations[0] = rnd1;
        randomizedLocations[1] = rnd2;
        randomizedLocations[2] = rnd3;
        randomizedLocations[3] = rnd4;

        //Grab the price of elder logs
        elderPrice = getPrice();

        //Flag ready state
        if(status.equals("Configuring...") && ctx.game.loggedIn()) {
            step = 1;
        }
    }

    @Override
    public void poll() {
        //Begin step sequence for easy debugging
        switch(step){
            case 1:
                //Click the Home Lodestone button on minimap
                final Component homeButton = ctx.widgets.component(homeWidgetID, homeComponentID);
                if(homeButton.valid() && homeButton.visible()) {
                    status = "Opening map";
                    homeButton.click();
                    Condition.sleep(Random.nextInt(700,1200));
                    step++;
                }
                break;
            case 2:
                //Teleport using the lodestone interface
                final Component lodestoneInt = ctx.widgets.component(lodeStoneWidgetID, lodeStoneComponentID[randomizedLocations[i]]);
                if(lodestoneInt.valid() && lodestoneInt.visible()){
                    status = "Teleporting...";
                    lodestoneInt.click();
                    Condition.sleep(Random.nextInt(700,1200));
                    step++;
                }
                break;
            case 3:
                //If bot accidently misclicked lumbridge and not draynor, try again
                if(lFixTile.tile().equals(ctx.players.local().tile())) {
                    if(ctx.players.local().animation() == -1) {
                        step = 1;
                        break;
                    }
                }
                //While we are teleporting wait
                if(!ctx.players.local().tile().equals(lodeStoneTile[randomizedLocations[i]])){
                    Condition.sleep(200);
                }else{
                    step++;
                }
                break;
            case 4:
                //If we are not at the desired tile then walk to it
                if(elderLocation[randomizedLocations[i]].distanceTo(ctx.players.local().tile()) > 7){
                    status = "Walking to Elder";
                    ctx.movement.step(elderLocation[randomizedLocations[i]]);
                    Condition.sleep(Random.nextInt(700,1200));
                }else{
                    step++;
                }
                break;
            case 5:
                //Handle the chopping now
                if(ctx.objects.select().id(deadElderID).within(6).nearest().isEmpty()) {
                    final GameObject elder = ctx.objects.select().id(elderID).nearest().poll();
                    if(elder.valid()) {
                        status = "Chopping";
                        if (!elder.inViewport()) {
                            ctx.camera.turnTo(elder.tile());
                        }
                        if (ctx.players.local().animation() == -1) {
                            Condition.sleep(1000);
                            if(ctx.players.local().animation() == -1) {
                                elder.interact("Chop down", elder.name());
                            }
                        }
                        antiBan();
                    }
                }else{
                    status = "Elder Dead";
                    //Tree is dead so start the timer and decide to bank or move the to next location
                    triggerTimer();

                    if(shouldBank()){
                        step = 6;
                    }else{
                        i = nextLocation(i);
                        step = 1;
                    }
                }
                break;
            case 6:
                //Begin bank sequence by opening the lodestone map
                final Component homeButton2 = ctx.widgets.component(homeWidgetID, homeComponentID);
                if(homeButton2.valid() && homeButton2.visible()) {
                    status = "Opening map";
                    homeButton2.click();
                    Condition.sleep(Random.nextInt(700,1200));
                    step++;
                }
                break;
            case 7:
                //Teleport to the bank using the lodestone interface
                final Component lodestoneInterface = ctx.widgets.component(lodeStoneWidgetID, bankComponentID);
                if(lodestoneInterface.valid() && lodestoneInterface.visible()){
                    status = "Teleporting(Bank)";
                    lodestoneInterface.click();
                    Condition.sleep(Random.nextInt(700,1200));
                    step++;
                }
                break;
            case 8:
                //While we are teleporting to the bank wait
                if(!ctx.players.local().tile().equals(lodeStoneBankTile)){
                    Condition.sleep(200);
                }else{
                    step++;
                }
                break;
            case 9:
                //If we are not at the bank tile then walk to it
                if (bankTile.distanceTo(ctx.players.local().tile()) > 4) {
                    status = "Walking to Bank";
                    ctx.movement.step(bankTile);
                    Condition.sleep(Random.nextInt(700, 1200));
                } else {
                    step++;
                }
                break;
            case 10:
                //Begin Banking
                if (!ctx.bank.opened()) {
                    status = "Opening Bank";
                    ctx.bank.open();
                }else if (!ctx.backpack.select().id(elderLogID).isEmpty()) {
                    status = "Depositing Logs";
                    final Item logs = ctx.backpack.id(elderLogID).poll();
                    logs.interact("Deposit-All", logs.name());

                }else if(depositAll){
                    status = "Depositing...";
                    ctx.bank.depositInventory();
                    Condition.sleep(200);
                    depositAll = false;
                } else {
                    status = "Closing Bank";
                    ctx.bank.close();
                    Condition.sleep(Random.nextInt(500,1000));
                    i = nextLocation(i);
                    step = 1;
                }
                break;
        }

    }

    private final Color color1 = new Color(0, 0, 0, 220);
    private final Color color2 = new Color(255, 215, 0);
    private final Color color3 = new Color(255, 215, 0, 229);
    private final Color color4 = new Color(255, 153, 0, 226);
    private final Color color5 = new Color(255, 255, 255);
    private final BasicStroke stroke1 = new BasicStroke(2);
    private final BasicStroke stroke2 = new BasicStroke(1);
    private final Font font1 = new Font("Mistral", 1, 25);
    private final Font font2 = new Font("Arial", 1, 10);
    private final Font font3 = new Font("Arial", 1, 15);
    private final Font font4 = new Font("Arial", 0, 15);
    private final Font font5 = new Font("Arial", 0, 9);


    @Override
    public void repaint(Graphics graphics) {
        final Graphics2D g = (Graphics2D) graphics;
        vSpawnTime = ((vRespawn - getTotalRuntime()) /1000);
        if(vSpawnTime <= 0){ vSpawnTime = 0; }
        sSpawnTime = ((sRespawn - getTotalRuntime()) /1000);
        if(sSpawnTime <= 0){ sSpawnTime = 0; }
        eSpawnTime = ((eRespawn - getTotalRuntime()) /1000);
        if(eSpawnTime <= 0){ eSpawnTime = 0; }
        dSpawnTime = ((dRespawn - getTotalRuntime()) /1000);
        if(dSpawnTime <= 0){ dSpawnTime = 0; }
        g.setColor(color1);
        g.fillRoundRect(7, 6, 185, 188, 16, 16);
        g.setColor(color2);
        g.setStroke(stroke1);
        g.drawRoundRect(7, 6, 185, 188, 16, 16);
        g.setColor(color3);
        g.setStroke(stroke2);
        g.drawLine(13, 33, 181, 33);
        g.setFont(font1);
        g.setColor(color4);
        g.drawString("Elder", 72, 29);
        g.setFont(font2);
        g.setColor(color5);
        g.drawString("v1.38", 129, 27);
        g.setFont(font3);
        g.drawString("Status:", 16, 54);
        g.drawString("Logs Collected:", 17, 72);
        g.drawString("Profit:", 16, 93);
        g.drawString(locationNames[randomizedLocations[0]], 16, 112);
        g.drawString(locationNames[randomizedLocations[1]], 16, 132);
        g.drawString(locationNames[randomizedLocations[2]], 16, 152);
        g.drawString(locationNames[randomizedLocations[3]], 16, 172);
        g.setFont(font4);
        g.drawString(status, 69, 54);
        g.drawString("" + logsCollected, 132, 72);
        g.drawString((logsCollected * elderPrice)/1000 + "K (" + ((int)(((logsCollected * elderPrice)/1000) * 3600000D) / getTotalRuntime()) +"k/hr)", 66, 93);
        g.drawString((vSpawnTime/60) + "." + (vSpawnTime - ((vSpawnTime/60) * 60)) + "min", 90, 112);
        g.drawString((sSpawnTime/60) + "." + (sSpawnTime - ((sSpawnTime/60) * 60)) + "min", 90, 132);
        g.drawString((eSpawnTime/60) + "." + (eSpawnTime - ((eSpawnTime/60) * 60)) + "min", 90, 152);
        g.drawString((dSpawnTime/60) + "." + (dSpawnTime - ((dSpawnTime/60) * 60)) + "min", 90, 172);
        g.setFont(font5);
        final long hr = getTotalRuntime()/3600000;
        final long min = getTotalRuntime()/60000;
        final long sec = getTotalRuntime()/1000;
        g.drawString(hr + ":" + (min - (hr*60)) + ":" + (sec - (60 *min)), 76, 188);
    }

    @Override
    public void messaged(MessageEvent e) {
        final String msg = e.text().toLowerCase();
        if (msg.equals("you get some elder logs.")) {
            logsCollected++;
        }else if(msg.contains("too full to")) {
            //Start banking routine
            step = 6;
            triggerTimer();
        }
    }

    private int getPrice() {
        final String content = downloadString("http://itemdb-rs.runescape.com/viewitem.ws?obj=29556");
        int ret = 0;
        if(content == null) {
            ret =  -1;
        }

        final String[] lines = content.split("\n");
        for(int i = 0; i < lines.length; i++) {
            if (lines[i].contains("Current Guide Price")) {
                String rString = lines[i].replace("<h3>Current Guide Price <span title='", "");
                rString = rString.substring(rString.indexOf("'>") + 2, 20);
                rString = rString.replace(",","");
                ret = Integer.parseInt(rString.trim());
            }
        }
        return ret;
    }

    private boolean shouldBank(){
        boolean ret = true;
        if(ctx.backpack.select().count() < 22){
            ret = false;
        }
        return ret;
    }

    private int nextLocation(int c){
        antiPattern();
        c++;
        if(c > 3){
            c = 0;
        }

        switch (c)
        {
            case 0:
                if((vSpawnTime) > 1){
                    c++;
                }
                break;
            case 1:
                if(sSpawnTime > 1){
                    c++;
                }
                break;
            case 2:
                if(eSpawnTime > 1){
                    c++;
                }
                break;
            case 3:
                if(dSpawnTime > 1){
                    c++;
                }
                break;
        }
        if(c > 3){
            c = 0;
        }

        return c;
    }
    private void antiPattern()
    {
        final int rnd  = Random.nextInt(0, 10);
        if(rnd == 2) {
            status = "Shifting Pattern";
            int missing1 = -1, missing2 = -1;
            for (int num = 0; num < 6; num++){
                if(randomizedLocations[0] == num || randomizedLocations[1] == num || randomizedLocations[2] == num || randomizedLocations[3] == num) {
                }else{
                    if (missing1 == -1) {
                        missing1 = num;
                    } else {
                        missing2 = num;
                    }
                }
            }
            log.info("Unused: " + missing1 + " + " + missing2);
            final int rnd2 = Random.nextInt(0,2);
            log.info("Random int: " + rnd2);
            if(rnd2 == 0) {
                randomizedLocations[i] = missing1;
            }else{
                randomizedLocations[i] = missing2;
            }

            switch(i) {
                case 0:
                    vRespawn = 0;
                    break;
                case 1:
                    sRespawn = 0;
                    break;
                case 2:
                    eRespawn = 0;
                    break;
                case 3:
                    dRespawn = 0;
                    break;
            }

        }
    }

    private void antiBan(){
        int rnd = Random.nextInt(0,176);
        switch(rnd){
            case 0:
                status = "Anti-ban(1)";
                ctx.camera.angle(Random.nextInt(0,300));
                break;
            case 1:
                status = "Anti-ban(1)";
                ctx.camera.pitch(Random.nextInt(0,71));
                break;
            case 10:
                status = "Anti-ban(1)";
                ctx.camera.angle(Random.nextInt(0,300));
                break;
            case 20:
                status = "Anti-ban(2)";
                final GameObject obj = ctx.objects.select().within(7).poll();
                if(obj.valid() && obj.inViewport()){
                    obj.interact("Examine", obj.name());
                }
                break;
            case 30:
                status = "Anti-ban(3)";
                final Npc npc =  ctx.npcs.select().within(7).poll();
                if(npc.valid() && npc.inViewport()){
                    npc.interact("Examine", npc.name());
                }
                break;
            case 40:
                status = "Anti-ban(4)";
                final Player plyr= ctx.players.select().within(7).poll();
                if(plyr.valid() && plyr.inViewport() && !ctx.players.local().name().equals(plyr.name())){
                    plyr.interact("Examine", plyr.name());
                    Condition.sleep(Random.nextInt(1000,10000));
                    final Component widg = ctx.widgets.component(1560,22);
                    if(widg.valid() && widg.visible()){
                        widg.click();
                    }
                }
                break;
        }
    }

    private void triggerTimer()
    {
        switch (i) {
            case 0:
                vRespawn = getTotalRuntime() + 600000;
                break;
            case 1:
                sRespawn = getTotalRuntime() + 600000;
                break;
            case 2:
                eRespawn = getTotalRuntime() + 600000;
                break;
            case 3:
                dRespawn = getTotalRuntime() + 600000;
        }
    }
}

