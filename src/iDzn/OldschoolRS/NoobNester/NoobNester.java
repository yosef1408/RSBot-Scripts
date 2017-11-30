package iDzn.OldschoolRS.NoobNester;

import iDzn.OldschoolRS.Task;
import iDzn.OldschoolRS.Tasks.*;
import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


@Script.Manifest(name="NoobNester", description="Empties nets of seeds and jewellery.", properties="Client=4; author=iDzn; topic=1340464;")

public class NoobNester extends PollingScript<ClientContext> implements PaintListener {




    List<Task> taskList = new ArrayList<Task>();
    private int nestsOpened,nestPrice, LantadymeGained, LantadymePrice, DwarfWeedGained, DwarfWeedPrice, CadantineGained, CadantinePrice,
            RanarrGained, RanarrPrice, TorstolGained, TorstolPrice, SnapdragonGained, SnapdragonPrice, WillowGained, WillowPrice, YewGained, YewPrice,
            MagicGained, MagicPrice, AcornGained, AcornPrice, CalquatGained, CalquatPrice,  PineappleGained, PineapplePrice, TeakGained,
            TeakPrice, MahoganyGained, MahoganyPrice, WatermelonGained, WatermelonPrice, StrawberryGained, StrawberryPrice, SweetcornGained,
            SweetcornPrice, LimpwurtGained,LimpwurtPrice, TarrominGained, TarrominPrice, MarrentilGained, MarrentilPrice, KwuarmGained, KwuarmPrice,
            PapayaGained, PapayaPrice, MapleGained, MaplePrice, PalmTreeGained, PalmTreePrice, SkinPrice, ClawPrice, ringCount, sRingCount, dRingCount, eRingCount, rRingCount,
            ringOpened, sRingOpened, dRingOpened, eRingOpened, rRingOpened,
            LantadymeCount,
            DwarfWeedCount,
            CadantineCount,
            RanarrCount,
            TorstolCount,
            SnapdragonCount,
            WillowCount,
            YewCount,
            MagicCount,
            AcornCount,
            CalquatCount,
            PineappleCount,
            TeakCount,
            MahoganyCount,
            WatermelonCount,
            StrawberryCount,
            SweetcornCount,
            LimpwurtCount,
            TarrominCount,
            MarrentilCount,
            KwuarmCount,
            PapayaCount,
            MapleCount,
            PalmTreeCount,

    nestCount; private GeItem ge;

    @Override
    public void start() {
        ge = new org.powerbot.script.rt4.GeItem(5075); nestPrice = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(7416); ClawPrice = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(7416); SkinPrice = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(5302); LantadymePrice = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(5303); DwarfWeedPrice = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(5295); RanarrPrice = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(5304); TorstolPrice = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(5300); SnapdragonPrice = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(5313); WillowPrice = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(5316); MagicPrice = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(5312); AcornPrice = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(5290); CalquatPrice = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(5287); PineapplePrice = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(214865); TeakPrice = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(21488); MahoganyPrice = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(5321); WatermelonPrice = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(5323); StrawberryPrice = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(5320); SweetcornPrice = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(5100); LimpwurtPrice = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(5293); TarrominPrice = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(5292); MarrentilPrice = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(5299); KwuarmPrice = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(5288); PapayaPrice = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(5314); MaplePrice = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(5289); PalmTreePrice = ge.price;
        nestCount = ctx.inventory.select().id(5075).count();
        LantadymeCount = ctx.inventory.select().id(5302).count();
        DwarfWeedCount = ctx.inventory.select().id(5303).count();
        CadantineCount = ctx.inventory.select().id(5301).count();
        RanarrCount = ctx.inventory.select().id(5295).count();
        TorstolCount = ctx.inventory.select().id(5304).count();
        SnapdragonCount = ctx.inventory.select().id(5300).count();
        WillowCount = ctx.inventory.select().id(5313).count();
        YewCount = ctx.inventory.select().id(5315).count();
        MagicCount = ctx.inventory.select().id(5316).count();
        AcornCount = ctx.inventory.select().id(5312).count();
        CalquatCount = ctx.inventory.select().id(5290).count();
        PineappleCount = ctx.inventory.select().id(5287).count();
        TeakCount = ctx.inventory.select().id(21486).count();
        MahoganyCount = ctx.inventory.select().id(21488).count();
        WatermelonCount = ctx.inventory.select().id(5321).count();
        StrawberryCount = ctx.inventory.select().id(5323).count();
        SweetcornCount = ctx.inventory.select().id(5320).count();
        LimpwurtCount = ctx.inventory.select().id(5100).count();
        TarrominCount = ctx.inventory.select().id(5293).count();
        MarrentilCount = ctx.inventory.select().id(5292).count();
        KwuarmCount = ctx.inventory.select().id(5299).count();
        PapayaCount = ctx.inventory.select().id(5288).count();
        MapleCount = ctx.inventory.select().id(5314).count();
        PalmTreeCount = ctx.inventory.select().id(5289).count();
        ringCount = ctx.inventory.select().id(1635).count();
        dRingCount = ctx.inventory.select().id(1643).count();
        eRingCount = ctx.inventory.select().id(1639).count();
        rRingCount = ctx.inventory.select().id(1641).count();
        sRingCount = ctx.inventory.select().id(1637).count();


        String userOptions[] = {"Ring Nests", "Seed Nests"};
        String userChoice = "" + (String) JOptionPane.showInputDialog(null, "Seed Nests or Ring Nests?", "NoobNester", JOptionPane.PLAIN_MESSAGE, null, userOptions, userOptions[1]);

        if (userChoice.equals("Seed Nests")) {
            taskList.add(new Bank(ctx));
            taskList.add(new Withdraw(ctx));
            taskList.add(new OpenNests(ctx));
        } else if (userChoice.equals("Ring Nests")) {
            taskList.add(new Bank(ctx));
            taskList.add(new WithdrawRings(ctx));
            taskList.add(new OpenRings(ctx));
        } else {
            ctx.controller.stop();
        }

    }

    @Override
    public void poll() {

        for (Task task : taskList) {
            if (ctx.controller.isStopping()) {
                break;
            }
            if (task.activate()) {
                task.execute();
                updateNests();
                updateLantadyme();
                updateDwarfWeed();
                updateCadantine();
                updateRanarr();
                updateTorstol();
                updateSnapdragon();
                updateWillow();
                updateYew();
                updatePalm();
                updateMagic();
                updateAcorn();
                updateCalquat();
                updatePineapple();
                updateTeak();
                updateMahogany();
                updateWatermelon();
                updateStrawberry();
                updateSweetcorn();
                updateLimpwurt();
                updateTarromin();
                updateMarrentil();
                updateKwuarm();
                updatePapaya();
                updateMaple();
            }


        }

    }
    private void updaterRing(){

        int newrRingCount = ctx.inventory.select().id(1641).count();
        if(newrRingCount > rRingCount){
            int difference = newrRingCount - rRingCount;
            rRingOpened += difference;
        }
        rRingCount = newrRingCount;
    }

    private void updateeRing(){

        int neweRingCount = ctx.inventory.select().id(1639).count();
        if(neweRingCount > eRingCount){
            int difference = neweRingCount - eRingCount;
            eRingOpened += difference;
        }
        eRingCount = neweRingCount;
    }

    private void updatedRing(){

        int newdRingCount = ctx.inventory.select().id(1643).count();
        if(newdRingCount > dRingCount){
            int difference = newdRingCount - dRingCount;
            dRingOpened += difference;
        }
        dRingCount = newdRingCount;
    }

    private void updatesRing(){

        int newsRingCount = ctx.inventory.select().id(1635).count();
        if(newsRingCount > sRingCount){
            int difference = newsRingCount - sRingCount;
            sRingOpened += difference;
        }
        sRingCount = newsRingCount;
    }

    private void updateRing(){

        int newRingCount = ctx.inventory.select().id(1635).count();
        if(newRingCount > ringCount){
            int difference = newRingCount - ringCount;
            ringOpened += difference;
        }
        ringCount = newRingCount;
    }


    private void updateNests(){

        int newNestCount = ctx.inventory.select().id(5075).count();
        if(newNestCount > nestCount){
            int difference = newNestCount - nestCount;
            nestsOpened += difference;
        }
        nestCount = newNestCount;
    }
    private void updateLantadyme(){

        int newLantadymeCount = ctx.inventory.select().id(5302).count();
        if(newLantadymeCount > LantadymeCount){
            int difference = newLantadymeCount - LantadymeCount;
            LantadymeGained += difference;
        }
        LantadymeCount = newLantadymeCount;
    }
    private void updateDwarfWeed(){
        int newDwarfWeedCount = ctx.inventory.select().id(5303).count();
        if(newDwarfWeedCount > DwarfWeedCount){
            int difference = newDwarfWeedCount - DwarfWeedCount;
            DwarfWeedGained += difference;
        }
        DwarfWeedCount = newDwarfWeedCount;
    }

    private void updatePapaya(){
        int newPapayaCount = ctx.inventory.select().id(5288).count();
        if(newPapayaCount > PapayaCount){
            int difference = newPapayaCount - PapayaCount;
            PapayaGained += difference;
        }
        PapayaCount = newPapayaCount;
    }


    private void updateCadantine(){
        int newCadantineCount = ctx.inventory.select().id(5301).count();
        if(newCadantineCount > CadantineCount){
            int difference = newCadantineCount - CadantineCount;
            CadantineGained += difference;
        }
        CadantineCount = newCadantineCount;
    }

    private void updateMagic(){
        int newMagicCount = ctx.inventory.select().id(5316).count();
        if(newMagicCount > MagicCount){
            int difference = newMagicCount - MagicCount;
            MagicGained += difference;
        }
        MagicCount = newMagicCount;
    }

    private void updateRanarr(){
        int newRanarrCount = ctx.inventory.select().id(5295).count();
        if(newRanarrCount > RanarrCount){
            int difference = newRanarrCount - RanarrCount;
            RanarrGained += difference;
        }
        RanarrCount = newRanarrCount;
    }

    private void updateTorstol(){
        int newTorstolCount = ctx.inventory.select().id(5304).count();
        if(newTorstolCount > TorstolCount){
            int difference = newTorstolCount - TorstolCount;
            TorstolGained += difference;
        }
        TorstolCount = newTorstolCount;
    }

    private void updateMaple(){
        int newMapleCount = ctx.inventory.select().id(5314).count();
        if(newMapleCount > MapleCount){
            int difference = newMapleCount - MapleCount;
            MapleGained += difference;
        }
        MapleCount = newMapleCount;
    }

    private void updateKwuarm(){
        int newKwuarmCount = ctx.inventory.select().id(5299).count();
        if(newKwuarmCount > KwuarmCount){
            int difference = newKwuarmCount - KwuarmCount;
            KwuarmGained += difference;
        }
        KwuarmCount = newKwuarmCount;
    }

    private void updatePalm(){
        int newPalmCount = ctx.inventory.select().id(5289).count();
        if(newPalmCount > PalmTreeCount){
            int difference = newPalmCount - PalmTreeCount;
            PalmTreeGained += difference;
        }
        PalmTreeCount = newPalmCount;
    }

    private void updateSnapdragon(){
        int newSnapdragonCount = ctx.inventory.select().id(5300).count();
        if(newSnapdragonCount > SnapdragonCount){
            int difference = newSnapdragonCount - SnapdragonCount;
            SnapdragonGained += difference;
        }
        SnapdragonCount = newSnapdragonCount;
    }

    private void updateMarrentil(){
        int newMarrentilCount = ctx.inventory.select().id(5292).count();
        if(newMarrentilCount > MarrentilCount){
            int difference = newMarrentilCount - MarrentilCount;
            MarrentilGained += difference;
        }
        MarrentilCount = newMarrentilCount;
    }


    private void updateWillow(){
        int newWillowCount = ctx.inventory.select().id(5313).count();
        if(newWillowCount > WillowCount){
            int difference = newWillowCount - WillowCount;
            WillowGained += difference;
        }
        WillowCount = newWillowCount;
    }

    private void updateYew(){
        int newYewCount = ctx.inventory.select().id(5315).count();
        if(newYewCount > YewCount){
            int difference = newYewCount - YewCount;
            YewGained += difference;
        }
        YewCount = newYewCount;
    }

    private void updateAcorn(){
        int newAcornCount = ctx.inventory.select().id(5312).count();
        if(newAcornCount > AcornCount){
            int difference = newAcornCount - AcornCount;
            AcornGained += difference;
        }
        AcornCount = newAcornCount;
    }

    private void updateCalquat(){
        int newCalquatCount = ctx.inventory.select().id(5290).count();
        if(newCalquatCount > CalquatCount){
            int difference = newCalquatCount - CalquatCount;
            CalquatGained += difference;
        }
        CalquatCount = newCalquatCount;
    }

    private void updatePineapple(){
        int newPineappleCount = ctx.inventory.select().id(5287).count();
        if(newPineappleCount > PineappleCount){
            int difference = newPineappleCount - PineappleCount;
            PineappleGained += difference;
        }
        PineappleCount = newPineappleCount;
    }

    private void updateTeak(){
        int newTeakCount = ctx.inventory.select().id(21486).count();
        if(newTeakCount > TeakCount){
            int difference = newTeakCount - TeakCount;
            TeakGained += difference;
        }
        TeakCount = newTeakCount;
    }

    private void updateMahogany(){
        int newMahoganyCount = ctx.inventory.select().id(21488).count();
        if(newMahoganyCount > MahoganyCount){
            int difference = newMahoganyCount - MahoganyCount;
            MahoganyGained += difference;
        }
        MahoganyCount = newMahoganyCount;
    }

    private void updateWatermelon(){
        int newWatermelonCount = ctx.inventory.select().id(5321).count();
        if(newWatermelonCount > WatermelonCount){
            int difference = newWatermelonCount - WatermelonCount;
            WatermelonGained += difference;
        }
        WatermelonCount = newWatermelonCount;
    }

    private void updateStrawberry(){
        int newStrawberryCount = ctx.inventory.select().id(5323).count();
        if(newStrawberryCount > StrawberryCount){
            int difference = newStrawberryCount - StrawberryCount;
            StrawberryGained += difference;
        }
        StrawberryCount = newStrawberryCount;
    }

    private void updateSweetcorn(){
        int newSweetcornCount = ctx.inventory.select().id(5320).count();
        if(newSweetcornCount > SweetcornCount){
            int difference = newSweetcornCount - SweetcornCount;
            SweetcornGained += difference;
        }
        SweetcornCount = newSweetcornCount;
    }

    private void updateLimpwurt(){
        int newLimpwurtCount = ctx.inventory.select().id(5100).count();
        if(newLimpwurtCount > LimpwurtCount){
            int difference = newLimpwurtCount - LimpwurtCount;
            LimpwurtGained += difference;
        }
        LimpwurtCount = newLimpwurtCount;
    }

    private void updateTarromin(){
        int newTarrominCount = ctx.inventory.select().id(5293).count();
        if(newTarrominCount > TarrominCount){
            int difference = newTarrominCount - TarrominCount;
            TarrominGained += difference;
        }
        TarrominCount = newTarrominCount;
    }


    @Override
            public void repaint (Graphics graphics){
                long Milliseconds = this.getTotalRuntime();
                long Seconds = (Milliseconds / 1000) % 60;
                long Minutes = (Milliseconds / (1000 * 60)) % 60;
                long Hours = (Milliseconds / (1000 * 60 * 60)) % 24;
                int profit = (nestsOpened*nestPrice)+(LantadymeGained*LantadymePrice)+
                        (DwarfWeedPrice*DwarfWeedGained)+(CadantinePrice*CadantineGained)+(RanarrPrice*RanarrGained)+
                        (TorstolPrice*TorstolGained)+(SnapdragonPrice*SnapdragonGained)+
                        (WillowPrice*WillowGained)+(YewPrice*YewGained)+(MagicPrice*MagicGained)+
                        (AcornPrice*AcornGained)+(CalquatPrice*CalquatGained)+(PineapplePrice*PineappleGained)+
                        (TeakPrice*TeakGained)+(MahoganyPrice*MahoganyGained)+(WatermelonPrice*WatermelonGained)+(StrawberryPrice*StrawberryGained)+
                        (SweetcornPrice*SweetcornGained)+(LimpwurtPrice*LimpwurtGained)+
                        (TarrominPrice*TarrominGained)+(MarrentilPrice*MarrentilGained)+(KwuarmPrice*KwuarmGained)+
                        (PapayaPrice*PapayaGained)+(MaplePrice*MapleGained)+(PalmTreePrice*PalmTreeGained)+(rRingOpened*rRingCount)+(ringCount*ringOpened)+
                        (sRingCount*sRingOpened)+(eRingCount*eRingOpened)+(dRingCount*dRingOpened)+
                        ((nestsOpened * (int)0.15)*nestPrice)-(((SkinPrice+ClawPrice)/2)*nestsOpened);


        Graphics2D g = (Graphics2D) graphics;

                g.setColor(new Color(35, 58, 70));
                g.fillRect(6, 345, 507, 129);

                g.setColor(new Color(57, 185, 255));
                g.drawRect(6, 345, 507, 129);
                g.setFont(new Font("Impact", Font.BOLD, 30));
                g.drawString("NoobNester", 200, 375);
                g.setFont(new Font("Impact", Font.PLAIN, 20));
                g.setColor(new Color(255, 255, 255));
                g.drawString("Run Time: " + String.format("%02d:%02d:%02d", Hours, Minutes, Seconds), 22, 400);
                g.drawString("Nests Opened: " + nestsOpened, 22, 420);
                g.drawString("Profit: " + profit, 22, 440);
                g.drawString("Profit/Hr: " + (profit/Seconds)*3600, 22, 460);


            }

        }
