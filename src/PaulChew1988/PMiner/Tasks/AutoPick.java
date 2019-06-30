package PMiner.Tasks;

import PMiner.PMinerConst;
import PMiner.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.ItemQuery;

import java.util.Objects;
import java.util.concurrent.Callable;

public class AutoPick extends Task {
    public int[] pickID = {};

    public AutoPick(ClientContext ctx, int[] pickID) {
        super(ctx);
        this.pickID = pickID;
    }


    @Override
    public boolean activate() {
        if (ctx.bank.opened()) {

            if ((ctx.skills.level(Constants.SKILLS_MINING) >= 11 && ctx.skills.level(Constants.SKILLS_MINING) < 21) && (!checkPick(PMinerConst.BLACK_PICK) &&(checkBankForPick(PMinerConst.BLACK_PICK)))) {
                return true;
            } else if ((ctx.skills.level(Constants.SKILLS_MINING) >= 21 && ctx.skills.level(Constants.SKILLS_MINING) < 31) && (!checkPick(PMinerConst.MITH_PICK) &&(checkBankForPick(PMinerConst.MITH_PICK)))){
                return true;
            } else if ((ctx.skills.level(Constants.SKILLS_MINING) >= 31 && ctx.skills.level(Constants.SKILLS_MINING) < 41) &&(!checkPick(PMinerConst.ADDY_PICK) ||(checkBankForPick(PMinerConst.ADDY_PICK)))){
                return true;
            } else if((checkBankForPick(PMinerConst.RUNE_PICK) & !checkPick(PMinerConst.RUNE_PICK)) && (ctx.skills.level(Constants.SKILLS_MINING) > 40)){

                return true;

            }

        }

        return false;
    }





    @Override
    public void execute() {


        final ItemQuery<Item> currentPickId = ctx.inventory.select().id(PMinerConst.PICK_IDS);

        if (ctx.skills.level(Constants.SKILLS_MINING) >= 11 && ctx.skills.level(Constants.SKILLS_MINING) < 21) {
            System.out.println("11");

            if (ctx.bank.depositInventory()) {
                if (ctx.bank.select().id(PMinerConst.BLACK_PICK).count() != 0) {
                    if (ctx.bank.withdraw(PMinerConst.BLACK_PICK, 1)) {
                        Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return ctx.inventory.select().id(PMinerConst.BLACK_PICK).count() >=1;
                            }
                        }, 250, 20);
                    }
                }
            }
        }else if (ctx.skills.level(Constants.SKILLS_MINING) >= 21 && ctx.skills.level(Constants.SKILLS_MINING) < 31) {
            System.out.println("21");
            if (ctx.bank.depositInventory()) {
                if (ctx.bank.select().id(PMinerConst.MITH_PICK).count() != 0) {
                    if (ctx.bank.withdraw(PMinerConst.MITH_PICK, 1)) {
                        Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return ctx.inventory.select().id(PMinerConst.MITH_PICK).count() >=1;
                            }
                        }, 250, 20);
                    }

                }


            }
        } else if (ctx.skills.level(Constants.SKILLS_MINING) >= 31 && ctx.skills.level(Constants.SKILLS_MINING) < 41) {
            System.out.println("31");
            if (ctx.bank.depositInventory()) {
                if (ctx.bank.select().id(PMinerConst.ADDY_PICK).count() != 0) {
                    if (ctx.bank.withdraw(PMinerConst.ADDY_PICK, 1)) {
                        Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return ctx.inventory.select().id(PMinerConst.ADDY_PICK).count() >=1;
                            }
                        }, 250, 20);

                    }


                }
            }
        }else if (ctx.skills.level(Constants.SKILLS_MINING) > 40) {
                System.out.println("41");
                if (ctx.bank.depositInventory()) {
                    System.out.println("2");
                    if (ctx.bank.select().id(PMinerConst.RUNE_PICK).count() != 0) {
                        System.out.println("3");
                        if (ctx.bank.withdraw(PMinerConst.RUNE_PICK, 1)) {
                            System.out.println("4");
                            Condition.wait(new Callable<Boolean>() {
                                @Override
                                public Boolean call() throws Exception {
                                    System.out.println("5");
                                    return ctx.inventory.select().id(PMinerConst.PICK_IDS) != currentPickId;
                                }
                            }, 250, 20);
                        }
                    }
                }


            }
        }





    public boolean checkPick ( int pickID) {
        return ctx.inventory.select().id(pickID).count() > 0;
    }
    public boolean checkBankForPick(int pickID){
        return ctx.bank.select().id(pickID).count()> 0;

    }




}

