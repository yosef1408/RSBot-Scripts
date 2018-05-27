package PanconMortadela.scripts.Utilitys;

import org.powerbot.script.*;
import org.powerbot.script.Tile;

public class Areas {
    public Area area1(Tile x){
        Area area=new Area(new Tile(x.tile().x()+1,x.tile().y()+1,x.floor()),new Tile(x.tile().x()-1,x.tile().y()-1,x.floor()));
        return area;
    }
    public Area area2(Tile x){
        Area area=new Area(new Tile(x.tile().x()+2,x.tile().y()+2,x.floor()),new Tile(x.tile().x()-2,x.tile().y()-2,x.floor()));
        return area;
    }
    public Area area3(Tile x){
        Area area=new Area(new Tile(x.tile().x()+3,x.tile().y()+3,x.floor()),new Tile(x.tile().x()-3,x.tile().y()-3,x.floor()));
        return area;
    }
    public Area areaselect(Tile x,int y){
        Area area=new Area(new Tile(x.tile().x()+y,x.tile().y()+y,x.floor()),new Tile(x.tile().x()-y,x.tile().y()-y,x.floor()));
        return area;
    }

    public Tile mitad(Tile x,Tile y,int floor){
        int xx,yy;
        xx=(x.x()+y.x())/2;
        yy=(x.y()+y.y())/2;
        return new Tile(Random.nextInt(xx-2,xx+2),Random.nextInt(yy-2,yy+2),floor);
    }

    public int distancia(Tile x,Tile y){
        int d;
        double raiz;
        raiz=Math.sqrt((Math.pow((x.x()+y.x()),2))+(Math.pow((x.y()+y.y()),2)));
        d=(int)raiz;
        return d;
    }

    public boolean areacruz(Tile Mine,Tile Player){
        if(area1(new Tile(Mine.x()+1,Mine.y())).contains(Player)){
            return true;
        }else if(area1(new Tile(Mine.x()-1,Mine.y())).contains(Player)){
            return true;
        }else if(area1(new Tile(Mine.x(),Mine.y()+1)).contains(Player)){
            return true;
        }else if(area1(new Tile(Mine.x(),Mine.y()-1)).contains(Player)){
            return true;
        }else{
            return false;
        }

    }

}
