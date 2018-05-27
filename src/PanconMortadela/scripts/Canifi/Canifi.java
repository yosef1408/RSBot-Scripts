package PanconMortadela.scripts.Canifi;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GroundItem;
import scripts.Utilitys.Areas;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Callable;

/**
 * Created by Putito on 13/05/2018.
 */
@Script.Manifest(name="CanifiAgi", description = "Run run",properties = "autor: PanconMortadela;topic=xx; client=4;")
public class Canifi extends PollingScript<ClientContext> implements PaintListener {
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
            new Tile(3506,3492,0),//0
            new Tile(3502,3504,2),//1
            new Tile(3492,3504,2),//2
            new Tile(3479,3499,2),//3
            new Tile(3478,3486,3),//4
            new Tile(3489,3476,2),//5
            new Tile(3510,3476,3),//6
            new Tile(3510,3485,2)};//7 length 8
    int l,count=0,cama=0; boolean camara;
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
    int[] id={10819,10820,10821,10828,10822,10831,10823,10832};
    String[] accion={"Climb","Jump","Jump","Jump","Jump","Vault","Jump","Jump"};
    Areas areas=new Areas();
    Area cuadro;
    Area[] regreso={
            new Area(new Tile(3503,3472,0),new Tile(3479,3502,0)),
            new Area(new Tile(3508,3471,0),new Tile(3504,3480,0))
    };
    @Override
    public void repaint(Graphics graphics) {

    }

    @Override
    public void poll() {
        regreso();
        estados();
        detener();
    }
    public void estados(){
        for(int i=0;i<t.length;i++){
            if(t[i].contains(ctx.players.local().tile())){
                if(t[i].contains(ctx.groundItems.select(10).id(11849).poll().tile()))marks();
                if(i==6)l=ctx.players.local().tile().floor();//         por si me caigo salir

                if(i==4)l=ctx.players.local().tile().floor();  //por si me caigo salir
                final int x=i;

                //hacer click

                while (!ctx.objects.select().id(id[i]).poll().interact(accion[i])){    //Si no puedo hacer el click hare lo que esta adentro
                    final Tile medio=areas.mitad(ctx.players.local().tile(),inicio[i],ctx.players.local().tile().floor()); //Tile a la mitad del camino
                    cuadro =areas.area1(medio);
                    if(medio.matrix(ctx).click()){  //Si puedo hacer click a la mitaddel camino entonces espero
                        System.out.println("voy a mitad");
                        Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return cuadro.contains(ctx.players.local().tile());  //espero hasta que el area tenga al personaje
                            }
                        }, 200, 50);

                    }else if(ctx.movement.step(inicio[i])) {   // en caso que no haga click en mitad hare step
                        cuadro =areas.area3(inicio[i]);
                        ctx.camera.turnTo(inicio[i]);
                        ctx.camera.pitch(99);
                        Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                System.out.println("Espero llegar al punto de cuadro");
                                return cuadro.contains(ctx.players.local().tile());
                            }
                        }, 500, 20);
                    }
                }//Fin de click
                cuadro =areas.area3(inicio[i]);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        if(x==6||x==4){
                            if(ctx.players.local().tile().floor()==l){
                                return true;
                            }
                        }
                        return cuadro.contains(ctx.players.local().tile())||(t[x+1].contains(ctx.players.local().tile())&&ctx.players.local().animation()==-1);
                    }
                }, 200, 40);
                camara=Random.nextBoolean();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        if(ctx.players.local().tile().floor()==0&&x!=0){
                            return true;
                        }
                        if(cuadro.contains(ctx.players.local().tile())){
                            count++;
                        }
                        if(camara&&cama==0){
                            cama=1;
                            System.out.println("Movere camara");
                            ctx.camera.turnTo(new Tile(Random.nextInt(inicio[x+1].x()-2,inicio[x+1].x()+2),Random.nextInt(inicio[x+1].y()-2,inicio[x+1].y()+2),inicio[x+1].floor()));
                            if(Random.nextBoolean()){
                                ctx.camera.pitch(99);
                            }else{
                                ctx.camera.pitch(Random.nextInt(60,90));
                            }
                        }
                        if(ctx.players.local().animation()!=-1){
                            count--;
                        }
                        if(count==2){
                            System.out.println("Toy parado como pendejo en vez de saltar e.e");
                            count=0;
                            return true;
                        }
                        if(x==7){
                            return t[0].contains(ctx.players.local().tile())&&ctx.players.local().animation()==-1;
                        }
                        System.out.println("Espero El cuadro");
                        if(x==6||x==4){
                            if(ctx.players.local().tile().floor()==l){
                                return true;
                            }
                        }
                        return t[x+1].contains(ctx.players.local().tile())&&ctx.players.local().animation()==-1;
                    }
                }, 1000, 10);

                break;
            }
        }
        count=0;cama=0;
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

        if(regreso[1].contains(ctx.players.local().tile())){//Este regreso es si estoy en la caida de 6 cerca del punto de inicio
            cuadro =areas.area3(t[0].getCentralTile());
            Tile x= cuadro.getRandomTile();
            ctx.movement.step(x);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    System.out.println("Espero llegar1");
                    return t[0].contains(ctx.players.local().tile());
                }
            }, 1000, 10);
        }
        else if(regreso[0].contains(ctx.players.local().tile())){
            Area x=new Area(new Tile(3497,3492,0),new Tile(3491,3486,0));
            cuadro =areas.area2(x.getCentralTile());
            Tile y= cuadro.getRandomTile();
            ctx.movement.step(y);
            cuadro =areas.area3(x.getCentralTile());
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    System.out.println("Espero llegar2");
                    return cuadro.contains(ctx.players.local().tile());
                }
            }, 1000, 30);
            cuadro =areas.area3(t[0].getCentralTile());
            ctx.movement.step(t[0].getCentralTile());
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    System.out.println("Espero llegar1");
                    return cuadro.contains(ctx.players.local().tile());
                }
            }, 1000, 10);
        }else if(estare()){

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
        int markCount=ctx.inventory.select().id(11849).count(true);
        while(mark.valid()){
            final int markcount=markCount;
            if(!mark.tile().matrix(ctx).click()){
                Tile medio=new Tile((ctx.players.local().tile().x()+mark.tile().x())/2,(ctx.players.local().tile().y()+mark.tile().y())/2);
                medio.matrix(ctx).click();
            }
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    System.out.println("Espero mark");
                    return ctx.inventory.select().id(11849).count(true)>markcount;
                }
            }, 1000, 1);
            markCount++;
        }

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


/*
*     public void estados(){
        for(int i=0;i<t.length;i++){
            if(t[i].contains(ctx.players.local().tile())){
                if(i==6){
                    l=Integer.parseInt(ctx.widgets.widget(160).component(5).text());//         por si me caigo salir
                    final Tile x=inicio[6];
                    ctx.movement.step(x);
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.movement.distance(x)<=4;
                        }
                    }, 1000, 10);
                }
                if(i==4)l=Integer.parseInt(ctx.widgets.widget(160).component(5).text());  //por si me caigo salir
                final int x=i;
                //if(i==0)t[0].getRandomTile().matrix(ctx).click();

                //hacer click
                Area cuadro=areas.area3(inicio[i]);
                do{
                    if (detener()) break;

                    while (!inicio[i].matrix(ctx).click()) {
                        if (detener()) break;
                        final Tile medio=new Tile((ctx.players.local().tile().x()+inicio[i].tile().x())/2,(ctx.players.local().tile().y()+inicio[i].tile().y())/2);
                        if(medio.matrix(ctx).click()){
                            System.out.println("voy a mitad");
                            Condition.wait(new Callable<Boolean>() {
                                @Override
                                public Boolean call() throws Exception {
                                    return medio.tile().equals(ctx.players.local().tile());
                                }
                            }, 200, 50);

                        }else if(ctx.movement.step(inicio[i])) {
                            ctx.camera.turnTo(inicio[i]);
                            ctx.camera.pitch(99);
                        }
                    }
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {

                            return ctx.players.local().animation() !=-1;
                        }
                    }, 100, Random.nextInt(30, 80));
                    if(l>Integer.parseInt(ctx.widgets.widget(160).component(5).text()))break;
                }while(ctx.players.local().animation()==-1);

                //esperar que estoy pasando el obstaculo
                if(ctx.players.local().animation()!=-1) {
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.players.local().animation() == -1;
                        }
                    }, 500, 10);
                }

                //Comprobar que estoy en la otra area
                if(i==t.length-1) {
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            System.out.println("Espero para final");
                            return t[0].contains(ctx.players.local().tile());
                        }
                    }, 1000, 10);
                }else{
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            System.out.println("Estoy pasando el obstaculo: "+x);
                            return t[x+1].contains(ctx.players.local().tile());
                        }
                    }, 500, 10);
                }
                break;
            }
        }
    }
* */
