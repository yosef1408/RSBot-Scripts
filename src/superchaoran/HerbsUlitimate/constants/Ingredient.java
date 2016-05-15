package superchaoran.HerbsUlitimate.constants;

import org.powerbot.script.rt6.Item;

/**
 * Created by chaoran on 5/13/16.
 */
public enum Ingredient {

    vialOfWater(227);

    private final int id;
    private int price;
    private Item item;

    Ingredient(int id){
        this.id = id;
    }

    public int getId() {return this.id;}
    public int getPrice(){return this.price;}
    public void setPrice(int price){this.price = price;}
    public Item getItem(){
        return this.item;
    }
    public void setItem(Item item){
        this.item = item;
    }

}
