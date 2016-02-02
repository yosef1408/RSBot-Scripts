package helicopter12.scripts;

import org.powerbot.script.*;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.*;
import org.powerbot.script.rt6.Component;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;


@Script.Manifest(name="simpleThiever", properties = "author=Helicopter12; topic=1300564; client=6;", description="1-99 Thieving; Have food in bank and wear armour!")
public class simpleThiever extends PollingScript<ClientContext> implements PaintListener, MessageListener, MouseListener{
    private int optionMode, optionFood;
    final private int lobsterID = 379, tunaID = 359, maxTilesAwayToThieve = 7, thievingSkillIndexID = 17;
    final private Tile trapLocations[] = {};
    final private int doorIDs[] =  { 34808, 34811, 34807, 34812 };
    final private int stairIDs[] = { 34498, 34548, 34584, 24548 };
    private int HP, lvlUps, deviation, foodID = lobsterID;
    private double successes, failures;
    private boolean hovered = false;
    private int thievingID, compID;
    private Tile bankTile, locationTile, lodestoneTeleportTile;
    private int startingXP, currentXP, requiredXP;
    private Npc npcToThieve;
    private GameObject stallToThieve;
    private Image bg;
    private int hpToEatAt;
    String status = "";
    state step = state.SETUP;
    private boolean thievingStalls = false;

    //npc.interacting == players.local
    //players.local.interacting == npc

    enum state{
        START, THIEVE, BANK, SETUP, TRAPNPC, TRAVERSE, TRAVERSE_OPENLODEMAP, TRAVERSE_CLICKTELEPORT, TRAVERSE_WAITFORTELEPORTATION
    }

    public void start() {

        //Set parameters
        startingXP = ctx.skills.experience(thievingSkillIndexID);
        requiredXP = ctx.skills.experienceAt(ctx.skills.level(thievingSkillIndexID) + 1);
        hpToEatAt =  Random.nextInt(55,71);

        //Load Cursor
        try {
            final URL cursorURL = new URL("http://elder.comlu.com/testPaint.png");
           bg = ImageIO.read(cursorURL);
        }catch (Exception ex) {
            log.info("Error: Couldn't load image");
        }

        status = "Configuring...";
    }

    public void poll() {

        //Keep HP updated
        HP = (int)(((double)ctx.combatBar.health() / (double)ctx.combatBar.maximumHealth()) * 100.0);

        //Determine what part of the sequence we are in
        switch (step)
        {
            case START:
                if (locationTile.distanceTo(ctx.players.local().tile()) < deviation) {
                    if (ctx.backpack.select().isEmpty() && !thievingStalls) {
                        step = state.BANK;
                    }else{
                        step = state.THIEVE;
                    }
                }else{
                    step = state.TRAVERSE;
                }
                break;

            case THIEVE:
                if (!thievingStalls) {
                    eat(foodID);
                    pickpocketNPC(thievingID, locationTile);
                } else {
                    if (ctx.backpack.select().count() == 28) {
                        step = state.BANK;
                    } else {
                        stealFromStall(thievingID, locationTile);
                    }
                }
                break;

            case BANK:
                doBanking();
                break;

            case TRAPNPC:
                //Find an open house
                Tile tileToUse = usableHouseTrappingTile();
                if (tileToUse != null) {

                }
                break;

            case TRAVERSE:
                if (locationTile.distanceTo(ctx.players.local().tile()) > deviation) {
                    step = state.TRAVERSE_OPENLODEMAP;
                    break;
                }
                if (!locationTile.matrix(ctx).inViewport()) { //Change to distance?
                    status = "Walking...";
                    ctx.movement.step(locationTile);
                    Condition.sleep(Random.nextInt(1650, 4700));
                }else{
                    step = state.THIEVE;
                }
                break;

            case TRAVERSE_OPENLODEMAP:
                status = "Opening map";
                openLodestoneMap();
                break;

            case TRAVERSE_CLICKTELEPORT:
                status = "Clicking map";
                clickLodestoneTeleport(compID);
                break;

            case TRAVERSE_WAITFORTELEPORTATION:
                status = "Teleporting";
                if(atLodestoneLocation(lodestoneTeleportTile)){
                    step = state.TRAVERSE;
                }
                break;
        }
    }

    public void eat(int mFoodID){
        //Check if inventory is open, if not open it
        if(HP != 0 && HP <= hpToEatAt){
            final Item food = ctx.backpack.select().id(mFoodID).poll();
            if(food.valid()){
                status = "Eating";
                food.click();
            }else{
                step = state.BANK;
            }
        }
    }

    public void doBanking(){
        if(bankTile.distanceTo(ctx.players.local().tile()) < 10 && ctx.bank.present() ){
            if(ctx.bank.opened() || (ctx.bank.open() && ctx.bank.opened())){
                if(!thievingStalls) { //Get food we are doing NPCs
                    if (ctx.backpack.select().isEmpty() || ctx.backpack.select().id(foodID).count() == 0) {
                        if (ctx.bank.withdraw(foodID, Bank.Amount.ALL)) {
                            step = state.THIEVE;
                        } else {
                            //Log out - Out of food
                            ctx.game.logout(false);
                            ctx.controller.stop();
                        }
                    }
                }else{
                    if(ctx.backpack.select().count() > 0){
                        ctx.bank.depositInventory();
                        Condition.sleep(Random.nextInt(400,700));
                    }else{
                        if(ctx.bank.close()) {
                            step = state.THIEVE;
                        }
                    }
                }
            }
        }else{
            if(bankTile.matrix(ctx).reachable()) {
                status = "Walking to Bank";
                ctx.movement.step(bankTile);
                Condition.sleep(Random.nextInt(1050, 5900));
            }else{
                openDoor();
            }
        }
    }

    public void stealFromStall(int stallID, Tile safeTiles){

        status = "Stealing";
        if (stallToThieve == null || safeTiles.distanceTo(ctx.players.local().tile()) > maxTilesAwayToThieve || !stallToThieve.valid()) {
            stallToThieve = ctx.objects.select().id(stallID).nearest().poll();
        }

        if (safeTiles.equals(ctx.players.local().tile())) {
            if((ctx.menu.opened() && ctx.menu.items().length >= 4) || (stallToThieve.click(false) && ctx.menu.opened() && ctx.menu.items().length >= 4)){
                if (ctx.players.local().animation() == -1 && !ctx.players.local().inMotion() && stallToThieve.valid() && !ctx.players.local().inCombat()) {
                    if(hovered){
                        ctx.input.click(true);
                    }else{
                        clickMenuItem("Steal-from");
                    }
                    hovered = false;
                }else{
                    if(!hovered) {
                        hoverMenuItem("Steal-from");
                        hovered = true;
                    }
                }
            }else{
                if(ctx.menu.opened()) {
                    ctx.menu.close();
                }
            }
        } else {
            if(locationTile.matrix(ctx).inViewport() && !ctx.players.local().inMotion() && locationTile.matrix(ctx).click()) {
                Condition.sleep(Random.nextInt(3000, 4000));
            }else{
                ctx.camera.turnTo(locationTile.tile());
            }
        }
    }

    public void pickpocketNPC(int npcID, Tile centerTile){

        status = "Picking";
        if(npcToThieve == null || npcToThieve.tile().distanceTo(ctx.players.local().tile()) > maxTilesAwayToThieve){
            npcToThieve = ctx.npcs.select().id(npcID).nearest().poll();
        }

        if(npcToThieve.valid() && npcToThieve.tile().matrix(ctx).reachable()){
            npcToThieve.bounds(-100,100,-100,-800,-100,100);
            if (!npcToThieve.inViewport() && npcToThieve.tile().distanceTo(ctx.players.local().tile()) < 12) {
                ctx.camera.turnTo(npcToThieve.tile());
            } else if (!npcToThieve.inViewport() && npcToThieve.tile().distanceTo(ctx.players.local().tile()) > 11) {
                ctx.movement.step(npcToThieve.tile());
            } else {
               if((ctx.menu.opened() && ctx.menu.items().length > 4) || (npcToThieve.click(false) && ctx.menu.opened() && ctx.menu.items().length > 4)){
                   if (ctx.players.local().animation() == -1 && !ctx.players.local().inMotion() && !ctx.players.local().inCombat()) {
                       if(hovered){
                           ctx.input.click(true);
                       }else{
                           clickMenuItem("Pickpocket");
                       }
                       hovered = false;
                   }else{
                       if(!hovered) {
                           hoverMenuItem("Pickpocket");
                           hovered = true;
                       }
                   }
               }else{
                   npcToThieve.click(false);
               }
            }
        }else{
            if(centerTile.matrix(ctx).reachable()) {
                ctx.movement.step(centerTile);
            }else{
                openDoor();
            }
        }
    }

    public boolean canThieveNPC(int lvlRequired){
        if(ctx.skills.realLevel(Constants.SKILLS_THIEVING) >= lvlRequired){
            return true;
        }

        return false;
    }

    public Tile usableHouseTrappingTile(){
        Tile usableTile = null;

        for (Tile tiles : trapLocations) {
            if(tiles.matrix(ctx).reachable()){
                usableTile = tiles;
                break;
            }
        }

        return usableTile;
    }

    public void openDoor(){
        final GameObject door = ctx.objects.select().id(doorIDs).nearest().poll();
        if(door.valid()){
            ctx.camera.turnTo(door.tile());
            if(door.interact("Open",door.name())){
                Condition.sleep(Random.nextInt(1000,2700));
            }
        }
    }

    public void openLodestoneMap() {
        //Click the Home Lodestone button on minimap
        final Component homeButton = ctx.widgets.component(1465, 56);
        if(homeButton.valid() && homeButton.visible()) {
            homeButton.click();
            Condition.sleep(Random.nextInt(400,600));
            step = state.TRAVERSE_CLICKTELEPORT;
        }
    }

    public void clickLodestoneTeleport(int comp) {
        //Teleport using the lodestone interface
        final Component lodestoneInt = ctx.widgets.component(1092, comp);
        if(lodestoneInt.valid() && lodestoneInt.visible()){
            lodestoneInt.click();
            Condition.sleep(Random.nextInt(400,600));
            step = state.TRAVERSE_WAITFORTELEPORTATION;
        }
    }

    public boolean atLodestoneLocation(Tile lodeTile) {
        boolean ret = false;

        if (ctx.players.local().tile().equals(lodeTile)) {
            ret = true;
        }

        return ret;
    }

    public void hoverMenuItem(String action){
        ctx.menu.hover(new Filter<MenuCommand>() {
            @Override
            public boolean accept(final MenuCommand menuCommand) {
                return menuCommand.action.equals(action);
            }
        });
    }

    public void clickMenuItem(String action){
        ctx.menu.click(new Filter<MenuCommand>() {
            @Override
            public boolean accept(final MenuCommand menuCommand) {
                return menuCommand.action.equals(action);
            }
        });
    }

    @Override
    public void mouseClicked(MouseEvent m) {
        if(m.getX() <= 40 && m.getX() >= 23 && m.getY() <= 40 && m.getY() >= 23){
            if(canThieveNPC(thief.Man.lvlReq())) {
                optionMode = 0;
                thievingStalls = false;
                setVariablesForNPC(thief.Man);
            }
        }else if(m.getX() <= 40 && m.getX() >= 23 && m.getY() <= 64 && m.getY() >= 47){
            if(canThieveNPC(thief.TeaStall.lvlReq())) {
                optionMode = 1;
                thievingStalls = true;
                setVariablesForNPC(thief.TeaStall);
            }
        }else if(m.getX() <= 40 && m.getX() >= 23 && m.getY() <= 88 && m.getY() >= 71){
            if(canThieveNPC(thief.SilkStall.lvlReq())) {
                optionMode = 2;
                thievingStalls = true;
                setVariablesForNPC(thief.SilkStall);
            }
        }else if(m.getX() <= 40 && m.getX() >= 23 && m.getY() <= 111 && m.getY() >= 94){
            if(canThieveNPC(thief.Farmer.lvlReq())) {
                optionMode = 3;
                thievingStalls = false;
                setVariablesForNPC(thief.Farmer);
            }
        }else if(m.getX() <= 40 && m.getX() >= 23 && m.getY() <= 134 && m.getY() >= 117) {
            if(canThieveNPC(thief.Guards.lvlReq())) {
                optionMode = 4;
                thievingStalls = false;
                setVariablesForNPC(thief.Guards);
            }
        }else if(m.getX() <= 40 && m.getX() >= 23 && m.getY() <= 157 && m.getY() >= 140) {
            if(canThieveNPC(thief.Knights.lvlReq())) {
                optionMode = 5;
                thievingStalls = false;
                setVariablesForNPC(thief.Knights);
            }
        }else if(m.getX() <= 40 && m.getX() >= 23 && m.getY() <= 180 && m.getY() >= 163) {
            if(canThieveNPC(thief.Paladin.lvlReq())) {
                optionMode = 6;
                thievingStalls = false;
                setVariablesForNPC(thief.Paladin);
            }
        }else if(m.getX() <= 40 && m.getX() >= 23 && m.getY() <= 203 && m.getY() >= 186) {
            if(canThieveNPC(thief.Hero.lvlReq())) {
                optionMode = 7;
                thievingStalls = false;
                setVariablesForNPC(thief.Hero);
            }
        }else if(m.getX() <= 97 && m.getX() >= 80 && m.getY() <= 243 && m.getY() >= 226) {
            optionFood = 0;
            foodID = lobsterID;
        }else if(m.getX() <= 181 && m.getX() >= 164 && m.getY() <= 243 && m.getY() >= 226) {
            optionFood = 1;
            foodID = tunaID;
        }else if(m.getX() <= 338 && m.getX() >= 255 && m.getY() <= 246 && m.getY() >= 220) {

            //Default:
            if(optionMode == 0){
                setVariablesForNPC(thief.Man);
            }else if(optionMode != 1) {
                thievingStalls = false;
            }

            //Start thieving
            if(allowedToRunScript()) {
                step = state.START;
            }else{
                JOptionPane.showMessageDialog(null, "This script is under development and only white-listed accounts can run the script. If you would like to help test this script you need to follow the instructions given on powerbot.org and apply with your username: " + ctx.players.local().name());
            }
        }
    }

    private void setVariablesForNPC(thief npc) {
        thievingID = npc.ID();
        bankTile = npc.bankTile();
        locationTile = npc.location();
        compID = npc.getLodestoneComp();
        lodestoneTeleportTile = npc.lodestoneTile();
        deviation = npc.distanceDeviation();
    }

    //Credit to LodeStone class
    private boolean lodestoneUnlocked(int shift) {
        boolean ret = true;
        if (ctx.varpbits.varpbit(3, shift, 1) != 1) {
            ret = false;
        }
        return ret;
    }

    @Override
    public void repaint(Graphics graphics) {
        final Graphics2D g = (Graphics2D) graphics;
        if (step != state.SETUP) {
            drawPaint(g);
        }else{
            drawSettings(g);
        }
        drawMouse(g);
    }

    private final Color color1 = new Color(0, 0, 0, 207);
    private final Color color2 = new Color(255, 255, 255);
    private final Color color4 = new Color(152, 2, 208);
    private final Font font1 = new Font("Arial", 1, 16);
    private final Font font2 = new Font("Arial", 0, 12);

    public void drawPaint(Graphics g){
        currentXP = ctx.skills.experience(thievingSkillIndexID);

        if(bg != null) {
            g.drawImage(bg, 5, 35, null);
        }

        g.setColor(Color.white);
        g.drawString("HP: " + HP + "% (Eat @: " + hpToEatAt + "%)", 20, 100);
        g.drawString("Status: " + status, 20, 120);
        g.drawString("Distance: " + ctx.players.local().tile().distanceTo(locationTile), 20, 140);
        g.drawString("Levels Gained: " + lvlUps, 20, 160);
        final long hr = getTotalRuntime() / 3600000;
        final long min = getTotalRuntime() / 60000;
        final long sec = getTotalRuntime() / 1000;
        g.drawString("Run Time: " + hr + ":" + (min - (hr * 60)) + ":" + (sec - (60 * min)), 20, 180);

        final int xpGained = (currentXP - startingXP);
        final int xphr = (int)(xpGained / ( getTotalRuntime() / 3600000.0));
        g.drawString("XP Gained: " + (xpGained > 1000 ? xpGained/1000 + "k" : xpGained) + " (" + xphr/1000 + "k/hr)", 20, 200);

        int ttl =  (xphr == 0 ? ttl = 0 : (int)(((requiredXP - currentXP) *3600000.0) / xphr));
        final long ttlhr = ttl/3600000;
        final long ttlmin = ttl/60000;
        g.drawString("TTL: "+ ttlhr + "hr : " + (ttlmin - (ttlhr * 60)) + "min" , 20, 220);

        g.drawString("Success Rate: " + ((successes + failures) == 0 ? 0 : (int) (successes / (failures + successes) * 100)) + "%", 20, 240);
    }

    public void drawMouse(Graphics g){

        final Point pt =  ctx.input.getLocation();
        g.setColor(Color.RED);
        g.fillOval(pt.x - 5, pt.y -5, 10, 10);
    }

    public void drawSettings(Graphics g){
            g.setColor(color1);
            g.fillRect(9, 10, 335, 242);
            g.setColor(color2);
            g.drawRect(9, 10, 335, 242);
            g.drawLine(336, 210, 18, 210);
            g.setColor(color4);
            g.drawRect(255, 220, 83, 26);
            g.setFont(font1);
            g.drawString("Start", 278, 239);

            //Checkboxes
            g.setColor(color2);
            g.drawRect(23, 23,  17, 17);
            g.drawRect(23, 94,  17, 17);
            g.drawRect(23, 117, 17, 17);
            g.drawRect(23, 140, 17, 17);
            g.drawRect(23, 163, 17, 17);
            g.drawRect(23, 186, 17, 17);
            g.drawRect(23, 47,  17, 17);
            g.drawRect(23, 71,  17, 17);

            g.drawRect(80,  226, 17, 17);
            g.drawRect(164, 226, 17, 17);

            g.drawString("Man",            49, 38);
            g.drawString("Tea Stall",      49, 62);
            g.drawString("Silk Stall",     49, 86);
            g.drawString("Master Farmer",  49, 109);
            g.drawString("Guards",         49, 132);
            g.drawString("Knights",        49, 155);
            g.drawString("Padadins",       49, 179);
            g.drawString("Heroes",         49, 202);

            g.drawString("level   1 - 5",  245, 38);
            g.drawString("level   5 - 20", 245, 62);
            g.drawString("level 20 - 38",  245, 86);
            g.drawString("level 38 - 40",  245, 109);
            g.drawString("level 40 - 55",  245, 132);
            g.drawString("level 55 - 70",  245, 155);
            g.drawString("level 70 - 80",  245, 179);
            g.drawString("level 80 - 99",  245, 202);


            g.drawString("Food:", 20, 240);
            g.setFont(font2);
            g.drawString("Lobster", 102, 240);
            g.drawString("Tuna",    188, 240);

            g.setColor(color4);
            switch(optionMode){
                case 0:
                    g.fillRect(24, 24, 16, 16);
                    break;
                case 1:
                    g.fillRect(24, 48, 16, 16);
                    break;
                case 2:
                    g.fillRect(24, 72, 16, 16);
                    break;
                case 3:
                    g.fillRect(24, 95, 16, 16);
                    break;
                case 4:
                    g.fillRect(24, 118, 16, 16);
                    break;
                case 5:
                    g.fillRect(24, 141, 16, 16);
                    break;
                case 6:
                    g.fillRect(24, 164, 16, 16);
                    break;
                case 7:
                    g.fillRect(24, 187, 16, 16);
                    break;
            }
            switch(optionFood){
                case 0:
                    g.fillRect(81, 227, 16, 16);
                    break;
                case 1:
                    g.fillRect(165, 227, 16, 16);
                    break;
            }
    }

    @Override
    public void messaged(MessageEvent e) {
        final String msg = e.text().toLowerCase();
        if (msg.contains("just advanced a thieving level!")) {
            lvlUps++;
            requiredXP = ctx.skills.experienceAt(ctx.skills.level(17) + 1);
        }else if(msg.contains("coins have been added")) {
            successes++;
        }else if(msg.contains("you steal")) {
            successes++;
        }else if(msg.contains("you fail to pick")){
            failures++;
        }
    }

    public enum thief{
        Knights  (new Tile(2662,3305,0), new Tile(2653,3283,0), new Tile(2634,3348,0), 23,   55, 11, 55),
        Guards   (new Tile(2662,3305,0), new Tile(2653,3283,0), new Tile(2634,3348,0), 32,   40, 11, 55),
        Hero     (new Tile(2662,3305,0), new Tile(2653,3283,0), new Tile(2634,3348,0), 21,   80, 11, 55),
        Paladin  (new Tile(2662,3305,0), new Tile(2653,3283,0), new Tile(2634,3348,0), 2256, 70, 11, 55),
        Farmer   (new Tile(3080,3250,0), new Tile(3093,3243,0), new Tile(3105,3298,0), 2234, 38, 14, 60),
        Man      (new Tile(3202,3232,0), new Tile(3214,3257,0), new Tile(3233,3221,0), 7876, 1,  17, 35),
        TeaStall (new Tile(3268,3410,0), new Tile(3253,3421,0), new Tile(3214,3376,0), 635,  5,  21, 65),
        SilkStall(new Tile(0000,0000,0), new Tile(2653,3283,0), new Tile(2634,3348,0), 000,  20, 11, 55);
        //Need silkstall location tile and ID

        private final int lvlReq, lodestoneComp, ID, distanceDeviation;
        private final Tile location, bankTile, lodeTile;

        thief(final Tile location, final Tile bankTile, final Tile lodeTile , final int ID, final int lvlReq, final int lodestoneComp, final int distanceDeviation) {
            this.ID = ID;
            this.lvlReq = lvlReq;
            this.location = location;
            this.lodestoneComp = lodestoneComp;
            this.bankTile = bankTile;
            this.lodeTile = lodeTile;
            this.distanceDeviation = distanceDeviation;
        }

        private int ID(){
            return ID;
        }

        public int lvlReq() {
            return lvlReq;
        }

        public Tile location() {
            return location;
        }

        public int getLodestoneComp(){
            return lodestoneComp;
        }

        public Tile bankTile(){
            return bankTile;
        }

        public Tile lodestoneTile() { return lodeTile; }

        public int distanceDeviation() { return distanceDeviation; }
    }

    private boolean allowedToRunScript(){
        final String whiteList = downloadString("http://elder.comlu.com/thief/auth.php?name=" + ctx.players.local().name());
        if(whiteList.equals("OK")){
            return true;
        }
        return false;
    }

    @Override
    public void mouseReleased(MouseEvent m){

    }

    @Override
    public void mousePressed(MouseEvent m){

    }

    @Override
    public void mouseEntered(MouseEvent m){

    }

    @Override
    public void mouseExited(MouseEvent m){

    }
}

