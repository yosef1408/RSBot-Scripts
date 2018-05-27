package PanconMortadela.scripts.Canifi;;

import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;
import scripts.Utilitys.Areas;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Callable;

/**
 * Created by Putito on 13/05/2018.
 */
@Script.Manifest(
        name="PMCanifisAgi",
        description = "Train the agility from level 40 to 60, take the grace marks on the road and return to the starting point if something goes wrong.",
        properties = "autor: PanconMortadela;topic=1345429; client=4;")
public class Canifi2 extends PollingScript<ClientContext> implements PaintListener {
    Tile[] inicio ={
            new Tile(3508,3489,0),//0
            new Tile(3505,3497,2),//1
            new Tile(3497,3504,2),//2
            new Tile(3486,3499,2),//3
            new Tile(3478,3492,3),//4
            new Tile(3480,3483,2),//5
            new Tile(3503,3476,3),//6
            new Tile(3510,3483,2)};//7 lengtth 8
    Tile[] llegada={
            new Tile(3506,3492,2),//0
            new Tile(3502,3504,2),//1
            new Tile(3492,3504,2),//2
            new Tile(3479,3499,3),//3
            new Tile(3478,3486,2),//4
            new Tile(3489,3476,3),//5
            new Tile(3510,3476,2),//6
            new Tile(3510,3485,0)};//7 length 8
    int l,count=0,time=0; boolean camara;
    Area[] t={
            new Area(new Tile(inicio[0].x()-3,inicio[0].y()+3),new Tile(llegada[7].x()+3,llegada[7].y()-3)),     //0
            new Area(new Tile(inicio[1].x()-3,inicio[1].y()+3,2),new Tile(llegada[0].x()+3,llegada[0].y()-3,2)),//1
            new Area(new Tile(inicio[2].x()-3,inicio[2].y()+3,2),new Tile(llegada[1].x()+3,llegada[1].y()-3,2)),//2
            new Area(new Tile(inicio[3].x()-3,inicio[3].y()-3,2),new Tile(llegada[2].x()+3,llegada[2].y()+3,2)),//3
            new Area(new Tile(inicio[4].x()-3,inicio[4].y()-3,3),new Tile(llegada[3].x()+3,llegada[3].y()+3,3)),//4
            new Area(new Tile(inicio[5].x()+3,inicio[5].y()-3,2),new Tile(llegada[4].x()-3,llegada[4].y()+3,2)),//5
            new Area(new Tile(inicio[6].x()+3,inicio[6].y()-6,3),new Tile(llegada[5].x()-3,llegada[5].y()+3,3)),//6
            new Area(new Tile(inicio[7].x()+7,inicio[7].y()+3,2),new Tile(llegada[6].x()-3,llegada[6].y()-3,2)),//7
    };
    Tile centro=new Tile(3507,3486,0);
    int[] id={10819,10820,10821,10828,10822,10831,10823,10832};
    String[] accion={"Climb","Jump","Jump","Jump","Jump","Vault","Jump","Jump"};
    Areas areas=new Areas();
    Area cuadro;
    Area[] regreso={
            new Area(new Tile(3503,3472,0),new Tile(3479,3502,0)),
            new Area(new Tile(3508,3471,0),new Tile(3504,3480,0))
    };
    Item coal= ctx.inventory.select().id(12019).poll();
    private long initialTime;
    double runTime;
    private final Color color1 = new Color(255, 255, 255,100);
    private final Color color2 = new Color(0, 0, 0);
    private final BasicStroke stroke1 = new BasicStroke(1);
    private final Font font1 = new Font("Segoe Print", 1, 12);
    int markcount=0;
    int vueltas=0;
    boolean vuelta=false;
    org.powerbot.script.rt4.GeItem barr= new org.powerbot.script.rt4.GeItem(2353);
    org.powerbot.script.rt4.GeItem ore= new org.powerbot.script.rt4.GeItem(440);
    org.powerbot.script.rt4.GeItem coal1= new org.powerbot.script.rt4.GeItem(453);
    int value;
    int h, m, s;
    @Override
    public void repaint(Graphics g1){

        Graphics2D g= (Graphics2D)g1;
        int x= (int) ctx.input.getLocation().getX();
        int y= (int) ctx.input.getLocation().getY();

        g.drawLine(x,y - 10,x,y+10);
        g.drawLine(x-10,y,x+10,y);

        h= (int)((System.currentTimeMillis()- initialTime)/3600000);
        m= (int)((System.currentTimeMillis()- initialTime)/60000%60);
        s= (int)((System.currentTimeMillis()- initialTime)/1000)%60;
        runTime=(double)(System.currentTimeMillis()-initialTime)/3600000;
        Color text=new Color(0,0,0);
        g.setColor(color1);
        g.fillRect(7, 54, 220, 58);
        g.setColor(color2);
        g.setStroke(stroke1);
        g.drawRect(7, 54, 220, 58);
        g.setFont(font1);
        g.drawString("Time: "+ h + ":"+ m +":"+s, 10, 67);
        g.drawString("#Lap:" + vueltas, 11, 87);
        value=barr.price-ore.price-coal1.price;
        g.drawString("Marks: " + markcount, 12, 106);
    }

    public void start(){
        initialTime=System.currentTimeMillis();
    }

    @Override
    public void poll() {
        regreso();
        estados();
        detener();
    }
    public void estados(){

        count = estoy();
        cuadro=areas.area3(llegada[count]);

        if(count==0) {
            time = Random.nextInt(2, 4);
            vuelta=false;
        }else {
            time = Random.nextInt(7, 10);
            vuelta=true;
        }


        if(count==4||count==6){
            l=ctx.players.local().tile().floor();
        }

        if(t[count].contains(ctx.groundItems.select(10).id(11849).poll().tile()))marks();
        if(ctx.objects.select().id(id[count]).poll().valid()){
            if(ctx.objects.select().id(id[count]).poll().interact(accion[count])){
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        System.out.println("Voy a esperar 2");
                        if(count==4||count==6){
                            return l!=ctx.players.local().tile().floor();
                        }
                        return cuadro.contains(ctx.players.local().tile());
                    }
                }, 1000, time);
            }else if(inicio[count].matrix(ctx).click()) {
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        System.out.println("Voy a esperar 3");
                        if(count==4||count==6){
                            return l!=ctx.players.local().tile().floor();
                        }
                        return cuadro.contains(ctx.players.local().tile());
                    }
                }, 1000, time);
            }else{
                cuadro=areas.area2(inicio[count]);
                ctx.movement.step(inicio[count]);
                ctx.camera.turnTo(inicio[count]);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        System.out.println("Voy a esperar 4");
                        return cuadro.contains(ctx.players.local().tile());
                    }
                }, 1000, time);
            }
        }
        if(count==7&&vuelta==true){
            vuelta=false;
            vueltas++;
        }

    }

    public int estoy(){
        for(int i=0;i<t.length;i++){
            if(t[i].contains(ctx.players.local().tile())){
                return i;
            }
        }
        return 99;
    }

    public void regreso(){

        while(regreso[1].contains(ctx.players.local().tile())){//Este regreso es si estoy en la caida de 6 cerca del punto de inicio
if(detener())break;
            ctx.movement.step(centro);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    System.out.println("Espero llegar 1");
                    return t[0].contains(ctx.players.local().tile());
                }
            }, 1000, 10);
        }
        while(regreso[0].contains(ctx.players.local().tile())){
            if(detener())break;
            Area x=new Area(new Tile(3497,3492,0),new Tile(3491,3486,0));
            cuadro =areas.area2(x.getCentralTile());
            Tile y= cuadro.getRandomTile();
            ctx.movement.step(y);
            cuadro =areas.area3(x.getCentralTile());
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    System.out.println("Espero llegar 2");
                    return cuadro.contains(ctx.players.local().tile());
                }
            }, 1000, 30);
            ctx.movement.step(centro);
            cuadro =areas.area2(centro);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    System.out.println("Espero llegar 3");
                    return cuadro.contains(ctx.players.local().tile())&&ctx.players.local().speed()==0;
                }
            }, 1000, 30);
        }
        if(estare()) {
        }else{

                JOptionPane.showInputDialog("No esta en una zona permitida");
                ctx.controller.stop();
                detener();
        }

    }

    public boolean estare(){

        for(int i=0;i<t.length;i++){
            if(t[i].contains(ctx.players.local().tile())){
                System.out.println("Estoy en Area: "+ i);
                return true;
            }
        }
        return false;
    }
    public void marks(){
        System.out.println("Hay markitas??");
        GroundItem mark=ctx.groundItems.select(10).id(11849).poll();
        final int markCount=ctx.inventory.select().id(11849).count(true);
        while(mark.valid()){
            if(detener())break;
            if(!mark.tile().matrix(ctx).click()){
                Tile medio=new Tile((ctx.players.local().tile().x()+mark.tile().x())/2,(ctx.players.local().tile().y()+mark.tile().y())/2);
                medio.matrix(ctx).click();
            }
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    System.out.println("Espero mark");
                    return ctx.inventory.select().id(11849).count(true)>markCount;
                }
            }, 1000, 1);
        }
        if(ctx.inventory.select().id(11849).count(true)>markCount)
            markcount++;

    }

    public boolean detener(){
        if(ctx.controller.isStopping()){
            System.out.println("me detendre");
            ctx.controller.stop();
            stop();
            return true;

        }else if(!ctx.game.loggedIn()){
            System.out.println("me detendre");
            ctx.controller.stop();
            stop();
            return true;
        }else{
            return false;
        }
    }
}
